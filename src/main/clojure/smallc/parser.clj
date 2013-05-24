(ns smallc.parser
  (:use (clojure.contrib import-static)
	(smallc common symtbl)
	[smallc.asm :exclude (log)])
  (:import (java.io File FileInputStream FileOutputStream)
	   (java.util.regex Pattern)
           (org.antlr.runtime ANTLRInputStream CommonTokenStream RecognitionException)
	   (org.antlr.runtime.tree CommonTree CommonTreeNodeStream)
	   (org.objectweb.asm ClassWriter)
	   (smallc.antlr SmallcLexer SmallcParser TypedTree TypedTreeAdaptor)))

(import-static java.lang.Integer parseInt)
(import-static java.lang.Long parseLong)

(import-static smallc.antlr.SmallcLexer
    ARG
    ARR
    ASSIGN
    BLK
    BOOLEAN_TYPE
    BWAND
    BWNOT
    BWOR
    BYTE_TYPE
    CHAR_LITERAL
    CHAR_TYPE
    DEC
    DECIMAL_LITERAL
    DEFINE
    DIM
    DIV
    DOUBLE_TYPE
    FALSE
    FLOAT_TYPE
    FLOAT_LITERAL
    FUN
    HEX_LITERAL
    IDENTIFIER
    INC
    INV
    INT_TYPE
    LONG_TYPE
    MINUS
    MOD
    MULT
    NULL
    OCTAL_LITERAL
    PLUS
    PMM
    PPP
    QUALIFIED_NAME
    RETURN
    SHIFTL
    SHIFTR
    SHIFTU
    SHORT_TYPE
    STRING_LITERAL
    TRUE
    VAR
    VOID_TYPE
    XOR
)

(def log (get-log "parser"))

(declare block)
(declare expression)
(declare expr-type)

(defn- literal?
  "Tests if a tree node is a literal value of a native type.
   Returns nil if the node is nil or the test fails."
  [node]
  (if node
    (let [t (tree-type node)
          lits #{CHAR_LITERAL
                 DECIMAL_LITERAL
                 FLOAT_LITERAL
                 HEX_LITERAL
                 OCTAL_LITERAL
                 STRING_LITERAL
                 TRUE FALSE NULL}]
      (lits t))))

(defn- lvalue?
  "Tests whether an expression resolves to an L-value."
  [expr]
  (let [token (tree-type expr)]
    (when (= token IDENTIFIER)
      (let [name (tree-text expr)]
        (if-let [sym (sym-get name)]
          (sym-var? sym))))))

(defn- literal-type
  "Gets the type descriptor of a literal value."
  [node]
  (let [str  (.toUpperCase (tree-text node))
        L?   (fn [s] (.endsWith s "L"))
        F?   (fn [s] (.endsWith s "F"))
        t    (tree-type node)
	type (cond
               (= t CHAR_LITERAL)    "C"
               (= t DECIMAL_LITERAL) (if (L? str) "J" "I")
               (= t FLOAT_LITERAL)   (if (F? str) "F" "D")
               (= t HEX_LITERAL)     (if (L? str) "J" "I")
               (= t OCTAL_LITERAL)   (if (L? str) "J" "I")
               (= t STRING_LITERAL)  "Ljava/lang/String;"
               (= t TRUE)            "Z"
               (= t FALSE)           "Z"
               (= t NULL)            "V")]
    (.setLead node type)
    type))

(defn- parse-literal
  "Parses a literal value."
  [node]
  (let [str  (.toUpperCase (tree-text node))
	t    (tree-type node)
	L?   (fn [s] (.endsWith s "L"))
	F?   (fn [s] (.endsWith s "F"))
	sufx (fn [s] (.substring s 0 (dec (.length s))))
	pref (fn [s] (.substring s 2))
	trim (fn [s] (pref (sufx s)))]
      (cond
        (= t CHAR_LITERAL)    (second str)
        (= t DECIMAL_LITERAL) (if (L? str) (Long. (sufx str)) (Integer. str))
        (= t FLOAT_LITERAL)   (if (F? str) (Float. (sufx str)) (Double. str))
        (= t HEX_LITERAL)     (if (L? str) (parseLong (trim str) 16) (parseInt (pref str) 16))
        (= t OCTAL_LITERAL)   (if (L? str) (parseLong (sufx str) 8) (parseInt str 8))
        (= t STRING_LITERAL)  (.replaceAll str "\"" "")
        (= t TRUE)            true
        (= t FALSE)           false
        (= t NULL)            nil)))

(defn- identifier-type
  "Returns an identifier's type."
  [id]
  (let [name (tree-text id)]
    (if-let [sym (sym-get name)]
      (if (sym-func? sym)
        (panic id INVFUNR name)
        (let [type (.type sym)]
          (.setLead id type)
          type))
      (panic id UNDECL name))))

(defn- lead-type
  "The lead type determines an expression's type."
  [t1 t2] t1)

(defn- unary-type
  "Returns the type of a unary-operator expression."
  [expr]
  (let [type (expr-type (child expr 0))]
    (doto expr
      (.setFirst type)
      (.setLead type))
    type))

(defn- binary-type
  "Returns the type of a binary-operator expression."
  [expr]
  (let [left (expr-type (child expr 0))
	right (expr-type (child expr 1))
        type (lead-type left right)]
    (doto expr
      (.setFirst left)
      (.setSecond right)
      (.setLead type))
    type))

(defn- dualop-type
  "Returns the type of dual-arity operators."
  [expr]
  (if (= (child-count expr) 1)
    (unary-type expr)
    (binary-type expr)))

(defn- shift-type
  "Returns the type of a shift-operator expression."
  [expr]
  (let [left (expr-type (child expr 0))
	right (expr-type (child expr 1))
        type (if (#{"D" "J"} left) "J" "I")]
    (doto expr
      (.setFirst left)
      (.setSecond right)
      (.setLead type))
    type))

(defn- integral-type
  "Returns the type of an integral expression."
  [expr]
  (let [left (expr-type (child expr 0))
        tl (if (#{"D" "J"} left) "J" "I")
        right (expr-type (child expr 1))
        tr (if (#{"D" "J"} right) "J" "I")
        type (lead-type tl tr)]
    (doto expr
      (.setFirst left)
      (.setSecond right)
      (.setLead type))
    type))

(defn- unary-integral-type
  "Returns the type of a unary integral expression."
  [expr]
  (let [left (expr-type (child expr 0))
        type (if (#{"D" "J"} left) "J" "I")]
    (doto expr
      (.setFirst left)
      (.setLead type))
    type))

(defn- incdec-type
  "Returns the type of an increment or decrement expression."
  [expr]
  (let [node (child expr 0)
        type (expr-type node)]
    (when-not (lvalue? node)
      (panic expr LVEXPEC))
    (doto expr
      (.setFirst type)
      (.setLead type))
    type))

(defn- assign-type
  "Returns the type of an assignment expression."
  [expr]
  (let [left (expr-type (child expr 0))
	right (expr-type (child expr 1))]
    (doto expr
      (.setFirst left)
      (.setSecond right)
      (.setLead left))
    left))

(defn- element-type
  "Returns the type of an array element."
  [expr]
  (let [atype (expr-type (child expr 0))
	itype (expr-type (child expr 1))]
    (when-not (= (first atype) \[)
      (panic expr INVAIDX))
    (when (and (not= (parent-type expr) ARR) (= (second atype) \[))
       (panic expr INVAIDX))
    (let [type (apply str (rest atype))]
      (doto expr
        (.setFirst atype)
        (.setSecond itype)
        (.setLead type))
      type)))

(defn- fcall-type
  "Returns the type of the value that a function returns,
   as coded in the function descriptor, after the parens."
  [stmt]
  (let [fun (child stmt 0)
	name (tree-text fun)
        sym (sym-get name)]
    (when (nil? sym)
      (panic fun UNDECL name))
    (when-not (sym-func? sym)
      (panic fun FNEXPEC name))
    (let [desc (.type sym)
          type (.substring desc (inc (.indexOf desc ")")))]
      (.setLead stmt type)
      type)))

(defn- expr-type
  "Returns the type of an expression."
  [expr]
  (let [t (tree-type expr)]
    (cond (literal? expr)  (literal-type expr)
          (= t IDENTIFIER) (identifier-type expr)
          (= t ASSIGN)     (assign-type expr)
          (= t PLUS)       (dualop-type expr)
          (= t MINUS)      (dualop-type expr)
          (= t MULT)       (binary-type expr)
          (= t DIV)        (binary-type expr)
          (= t MOD)        (binary-type expr)
          (= t SHIFTL)     (shift-type expr)
          (= t SHIFTR)     (shift-type expr)
          (= t SHIFTU)     (shift-type expr)
          (= t BWAND)      (integral-type expr)
          (= t BWOR)       (integral-type expr)
          (= t XOR)        (integral-type expr)
          (= t BWNOT)      (unary-integral-type expr)
          (= t INC)        (incdec-type expr)
          (= t DEC)        (incdec-type expr)
          (= t PPP)        (incdec-type expr)
          (= t PMM)        (incdec-type expr)
          (= t ARR)        (element-type expr)
	  (= t INV)        (fcall-type expr))))

(defn- class-descriptor
  "Returns the type descriptor of a qualified class name."
  [s]
  (let [dot (Pattern/quote ".")
	sep (Pattern/quote File/separator)]
    (format "L%s;" (.replaceAll (.replaceAll s dot "/") sep "/"))))

(defn- convert-type
  [node]
  "Converts an AST type into a type descriptor."
  (let [n (child-count node)
	t (tree-type node)]
    (str
      (apply str (repeat n "["))
      (cond (= t BOOLEAN_TYPE)   "Z"
            (= t BYTE_TYPE)      "B"
            (= t CHAR_TYPE)      "C"
            (= t SHORT_TYPE)     "S"
            (= t INT_TYPE)       "I"
            (= t FLOAT_TYPE)     "F"
            (= t LONG_TYPE)      "J"
            (= t DOUBLE_TYPE)    "D"
	    (= t VOID_TYPE)      "V"
	    (= t IDENTIFIER)     (format "Ljava/lang/%s;" (tree-text node))
	    (= t QUALIFIED_NAME) (class-descriptor (tree-text node))))))

(defn- define-decl
  "Compiles a #define declaration. Literal atoms are assigned
   in the declaration, expressions in the static initializer."
  [cw mv cname node]
  (let [name (tree-text node 0)
	init (child node 1)
	desc (expr-type init)]
    (when (= desc "V")
      (panic init NULLDEF))
    (when-not (sym-constant name desc cname)
      (panic (child node 0) REDEF name))
    (if (literal? init)
      (emit-define cw name desc (parse-literal init))
      (do
	(emit-define cw name desc nil)
        (init-clinit cw mv)
	(expression init @mv desc)
	(emit-putstatic @mv cname name desc)))))

(defn- array-dims
  "Returns a sequence of an array-type's dimensions
   as integers, if any. Otherwise it returns nil."
  [tree]
  (when (children? tree)
    (let [nodes (seq-children tree)
          types (map (memfn getType) nodes)]
      (when-not (every? #(= % DIM) types)
        (if (every? #(= % DECIMAL_LITERAL) types)
          (let [nums (map parse-literal nodes)]
            (if (every? pos? nums)
	      nums
	      (panic (first nodes) ARRDIM)))
	  (panic (first nodes) ARRDIM))))))

 (defn- global-decl
  "Compiles a global variable declaration."
  [cw mv cname node]
  (let [type (child node 0)
        desc (convert-type type)
	name (tree-text node 1)
	init (child node 2)]
    (when-not (sym-variable name desc cname 0)
      (panic (child node 1) REDEF name))
    (if (literal? init)
      (emit-var cw name desc (parse-literal init))
      (do
        (emit-var cw name desc nil)
        (if-let [dims (array-dims type)]
          (do
            (init-clinit cw mv)
            (emit-array-def @mv desc dims)
            (emit-putstatic @mv cname name desc))
          (when init
            (init-clinit cw mv)
            (expr-type init)
            (expression init @mv desc)
            (emit-putstatic @mv cname name desc)))))))

(defn- param-types
  "Constructs a vector of type descriptors for a parameters list."
  [parms]
  (loop [types (vector)
         nodes (seq-children parms)]
    (if (seq nodes)
      (recur (conj types (convert-type (child (first nodes) 0)))
             (rest nodes))
      types)))

(defn- method-desc
  "Constructs a method descriptor from AST nodes of
   a function's return type and parameters."
  [type coll]
  (format "(%s)%s"
    (loop [desc ""
           types coll]
      (if (seq types)
        (recur (str desc (first types))
               (rest types))
        desc))
    (convert-type type)))

(defn- declare-params
  "Stores a function's formal parameters into the symbol table,
   each with its assigned index into the local variables array.
   Returns the next available index number."
  [node]
  (loop [count 0
         parms (seq-children node)]
    (if-let [p (first parms)]
      (let [name (tree-text p)
	    desc (convert-type (child p 0))
	    size (if (#{"D" "J"} desc) 2 1)]
        (when-not (sym-variable name desc nil count)
          (panic p REDEF name))
	(recur (+ count size)
	       (rest parms)))
      count)))

(defn- local-decl
  "Stores a block local variable into the symbol table with
   its assigned index slot into the local variables array.
   Returns the next available index number."
  [decl mv slot]
  (let [type (child decl 0)
        desc (convert-type type)
        name (tree-text (child decl 1))
        init (child decl 2)
        size (if (#{"D" "J"} desc) 2 1)]
    (when-not (sym-variable name desc nil slot)
      (panic (child decl 1) REDEF name))
    (if init
      (do
        (expr-type init)
        (expression init mv desc))
      (if-let [dims (array-dims type)]
        (emit-array-def mv desc dims)
        (emit-default mv desc)))
    (emit-store mv desc slot)
    (+ slot size)))

(defn- literal
  "Compiles a literal constant."
  [val mv]
  (emit-constant mv (literal-type val) (parse-literal val)))

(defn- lvalue
  "Compiles an identifier as an lvalue."
  [id mv]
  (let [name (tree-text id)
        sym (sym-get name)]
    (if (sym-global? sym)
      (emit-putstatic mv (.class sym) name (.type sym))
      (emit-store mv (.type sym) (.slot sym)))))

(defn- rvalue
  "Compiles an identifier as an rvalue."
  [id mv]
  (let [name (tree-text id)
        sym (sym-get name)]
    (if (sym-global? sym)
      (emit-getstatic mv (.class sym) name (.type sym))
      (emit-load mv (.type sym) (.slot sym)))))

(defn- binary
  "Compiles a binary-operator expression."
  [expr fn mv]
  (let [left (child expr 0)
        right (child expr 1)
        first (.getFirst expr)
        second (.getSecond expr)
        lead (.getLead expr)]
    (expression left mv lead)
    (expression right mv lead)
    (fn mv lead)))

(defn- unary
  "Compiles a unary-operator expression."
  [expr fn mv]
  (let [left (child expr 0)
        first (.getFirst expr)
        lead (.getLead expr)]
    (expression left mv lead)
    (fn mv lead)))

(defn- shift
  "Compiles a shift-operator expression."
  [expr fn mv]
  (let [left (child expr 0)
        right (child expr 1)
        first (.getFirst expr)
        second (.getSecond expr)
        lead (.getLead expr)]
    (expression left mv lead)
    (expression right mv "I")
    (fn mv lead)))

(defn- plus
  "Compiles a plus expression as unary or binary."
  [expr mv]
  (if (= (child-count expr) 2)
    (binary expr emit-add mv)
    (expression (child expr 0) mv)))

(defn- minus
  "Compiles a minus expression as unary or binary."
  [expr mv]
  (if (= (child-count expr) 2)
    (binary expr emit-sub mv)
    (let [val (child expr 0)
	  lead (.getLead expr)]
      (if (literal? val)
        (emit-constant mv lead (- (parse-literal val)))
	(do (expression val mv)
	    (emit-neg mv lead))))))

(defn- delta-post
  "Compiles a postfix delta expression."
  [expr delta mv]
  (let [tgt (child expr 0)
        lead (expr-type tgt)]
    (expression tgt mv)
    (emit-dup mv lead)
    (emit-constant mv lead delta)
    (emit-add mv lead)
    (lvalue tgt mv)))

(defn- delta-pref
  "Compiles a prefix delta expression."
  [expr delta mv]
  (let [tgt (child expr 0)
        lead (expr-type tgt)]
    (expression tgt mv)
    (emit-constant mv lead delta)
    (emit-add mv lead)
    (emit-dup mv lead)
    (lvalue tgt mv)))

(defn- lval-element
  "Compiles an lvalue array-element access expression."
  [expr mv]
  (let [left (child expr 0)
        right (child expr 1)
        lead (.getLead expr)]
    (expression left mv)
    (expression right mv "I")
    (when (= (first lead) \[)
      (emit-aload mv lead))))

(defn- rval-element
  "Compiles an rvalue array-element access expression."
  [expr mv]
  (let [left (child expr 0)
        right (child expr 1)
        first (.getFirst expr)
        second (.getSecond expr)
        lead (.getLead expr)]
    (expression left mv)
    (expression right mv "I")
    (emit-aload mv lead)))

(defn- assign
  "Compiles an assignment expression."
  [expr mv]
  (let [left (child expr 0)
        right (child expr 1)
        first (expr-type left)
        second (expr-type right)]
    (if (= (tree-type left) ARR)
      (do
	(lval-element left mv)
	(expression right mv first)
	(emit-dupx2 mv first)
	(emit-astore mv first))
      (do
        (expression right mv first)
        (emit-dup mv first)
        (lvalue left mv)))))
 
(defn- fcall
  "Compiles a function call."
  [stmt mv]
  (let [fun (child stmt 0)
	name (tree-text fun)
        args (child stmt 1)
        sym (sym-get name)]
    (if (children? args)
      (let [nodes (seq-children args)
            types (map expr-type nodes)
            vtype (.vtype sym)]
	(doall types)
        (doseq [arg nodes] (expression arg mv))))
    (emit-invoke-static mv (.class sym) name (.type sym))))

(defn- expression
  "Compiles an espression whose result may be
   cast to its parent's type, if any."
  ([expr mv] (expression expr mv nil))
  ([expr mv parent]
   (let [token (tree-type expr)
         type (.getLead expr)]
     (cond (literal? expr)  (literal expr mv)
           (= token IDENTIFIER) (rvalue expr mv)
           (= token ASSIGN)     (assign expr mv)
           (= token PLUS)       (plus expr mv)
           (= token MINUS)      (minus expr mv)
           (= token MULT)       (binary expr emit-mul mv)
           (= token DIV)        (binary expr emit-div mv)
           (= token MOD)        (binary expr emit-rem mv)
           (= token SHIFTL)     (shift expr emit-shiftl mv)
           (= token SHIFTR)     (shift expr emit-shiftr mv)
           (= token SHIFTU)     (shift expr emit-shiftu mv)
           (= token BWAND)      (binary expr emit-and mv)
           (= token BWOR)       (binary expr emit-or mv)
           (= token XOR)        (binary expr emit-xor mv)
           (= token BWNOT)      (unary expr emit-not mv)
           (= token INC)        (delta-pref expr 1 mv)
           (= token DEC)        (delta-pref expr -1 mv)
           (= token PPP)        (delta-post expr 1 mv)
           (= token PMM)        (delta-post expr -1 mv)
	   (= token ARR)        (rval-element expr mv)
	   (= token INV)        (fcall expr mv))
     (when parent
       (when-not (= type parent)
         (emit-cast mv type parent expr))))))

(defn- get-fun
  "Gets a node's enclosing function definition tree."
  [node]
  (loop [parent (.getParent node)]
    (if (= (.getType parent) FUN)
      parent
      (recur (.getParent parent)))))

(defn- assign-stmt
  "Compiles an assignment statement."
  [expr mv]
  (let [left (child expr 0)
        right (child expr 1)
        first (expr-type left)
        second (expr-type right)]
    (if (= (tree-type left) ARR)
      (do
	(lval-element left mv)
	(expression right mv first)
	(emit-astore mv first))
      (do
        (expression right mv first)
        (lvalue left mv)))))
  
(defn- delta-stmt
  "Compiles statement that adds a delta to an lvalue."
  [expr delta mv]
  (let [tgt (child expr 0)
        lead (expr-type tgt)]
    (expression tgt mv)
    (emit-constant mv lead delta)
    (emit-add mv lead)
    (lvalue tgt mv)))

(defn- return-stmt
  "Compiles a return statement."
  [stmt mv]
  (let [fun (get-fun stmt)
	desc (convert-type (child fun 1))]
    (if (children? stmt)
      (if (= (last desc) \V)
        (panic stmt INVRETV)
        (let [expr (child stmt 0)]
          (expr-type expr)
          (expression expr mv desc)
          (emit-return mv desc)))
      (if (= (last desc) \V)
        (emit-return mv "V")
        (panic stmt RVEXPEC)))))

(defn- fcall-stmt
  "Compiles a function call."
  [stmt mv]
  (let [name (tree-text stmt 0)
        sym (sym-get name)]
    (fcall-type stmt)
    (fcall stmt mv)
    (when-not (= (last (.type sym)) \V)
      (emit-pop mv (.type sym)))))

(defn- statement
  "Compiles a statement or expression. Returns the
   next slot into the JVM local variables array."
  [stmt mv slot]
  (let [t (tree-type stmt)]
    (cond (= t VAR)    (local-decl stmt mv slot)
          (= t BLK)    (block stmt mv slot)
          (= t ASSIGN) (do (assign-stmt stmt mv) slot)
	  (= t RETURN) (do (return-stmt stmt mv) slot)
          (= t INC)    (do (delta-stmt stmt 1 mv) slot)
          (= t DEC)    (do (delta-stmt stmt -1 mv) slot)
          (= t PPP)    (do (delta-stmt stmt 1 mv) slot)
          (= t PMM)    (do (delta-stmt stmt -1 mv) slot)
	  (= t INV)    (do (fcall-stmt stmt mv) slot)
          :default
	    (do (expression stmt mv) slot))))

(defn- block
  "Compiles a compound statement. The caller must create and
   destroy the block's scope. Returns the locals slot it received."
  [blk mv slot]
  (loop [slot slot
         nodes (seq-children blk)]
    (when (seq nodes)
      (recur (statement (first nodes) mv slot)
             (rest nodes))))
  slot)

(defn- function-def
  "Compiles a function definition."
  [cw cname node]
  (let [name (tree-text node 0)
        type (child node 1)
        parm (child node 2)
        blk  (child node 3)
        vtyp (param-types parm)
        desc (method-desc type vtyp)]
    (when-not (sym-function name desc vtyp cname)
      (panic (child node 0) REDEF name))
    (sym-push)
    (let [mv (init-method cw name desc)
          slot (declare-params parm)]
      (block blk mv slot)
      (when (= (last desc) \V)
        (emit-return mv "V"))
      (end-method mv))
    (sym-pop)))

(defn- seq-ast
  "Returns an AST root as a sequence of nodes."
  [ast]
  (if (.isNil ast)
    (seq (.getChildren ast))
    (list ast)))

(defn parse
  "Parses the file and returns an Abstract Syntax Tree."
  [file]
  (try
    (let [fis (FileInputStream. file)
  	  ais (ANTLRInputStream. fis)
	  lex (SmallcLexer. ais)
	  tok (CommonTokenStream. lex)
	  psr (SmallcParser. tok)
          adp (TypedTreeAdaptor.)]
      (try
	(.setTreeAdaptor psr adp)
        (.. psr (compilation_unit) (getTree))
        (catch RecognitionException re
          (panic (.token re) ANTLR
	         (.getErrorMessage psr re (.getTokenNames psr))))))
    (catch Throwable t
      (.error log "parse" t)
      (panic nil ANTLR (.getMessage t)))))

(defn- func-trans
  "Carries out AST transformations on a function.
   * Removes redundant return statements."
  [node]
  (let [name (tree-text node 0)
        type (child node 1)
        parm (child node 2)
        blk  (child node 3)
        vtyp (param-types parm)
        desc (method-desc type vtyp)]
    (loop [nodes (seq-children blk)]
      (if-let [decl (first nodes)]
        (let [type (.getType decl)]
          (cond (= type RETURN)
		(when (and (= (count nodes) 1) (= (last desc) \V) (not (children? decl)))
		  (.deleteChild blk (dec (child-count blk)))))
          (recur (rest nodes)))))))

(defn transform
  "Walks the Abstract Syntax Tree to apply following:
   * Automatic resolution of forward references;
   * Evaluation of constant expressions;
   * Removal of redundant code.
   * User-defined AST transformations, a.k.a. macros."
  [ast cp]
  (doseq [node (seq-ast ast)]
    (let [type (.getType node)]
      (cond (= type FUN) (func-trans node))))
  ast)

(defn translate
  "Walks the AST, applies the type system and compiles."
  [ast cp cname]
  (let [cw (ClassWriter. ClassWriter/COMPUTE_FRAMES)
	clinit (atom nil)]
    (sym-create)
    (emit-header cw cname)
    (doseq [node (seq-ast ast)]
      (let [type (.getType node)]
        (cond (= type DEFINE) (define-decl cw clinit cname node)
              (= type VAR)    (global-decl cw clinit cname node)
              (= type FUN)    (function-def cw cname node))))
    (visit-end cw @clinit)
    cw))

(defn generate
  "Writes the ASM model into a class file."
  [cw od cname]
  (let [file (File. od (str cname ".class"))
        dir (File. (.getParent file))]
    (when-not (.exists dir)
      (.mkdirs dir))
    (.delete file)
    (.createNewFile file)
    (doto (FileOutputStream. file)
      (.write (.toByteArray cw))
      (.close))))
