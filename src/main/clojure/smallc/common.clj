(ns smallc.common)

(def NFOUND   1)
(def ANTLR    2)
(def NULLDEF  3)
(def ARRDIM   4)
(def REDEF    5)
(def UNDECL   6)
(def INVFUNR  7)
(def RVEXPEC  8)
(def INVRETV  9)
(def LVEXPEC 10)
(def INVAIDX 11)
(def FNEXPEC 12)
(def INVCAST 13)

(def cat {
    NFOUND  "%s not found: %s"
    ANTLR   "Parsing error: %s"
    NULLDEF "Invalid symbolic constant; unknown type for null"
    ARRDIM  "Incorrect array dimensions"
    REDEF   "Cannot redefine identifier: %s"
    UNDECL  "Undeclared identifier: %s"
    INVFUNR "Reference must be function call: %s"
    RVEXPEC "Return value expected"
    INVRETV "Return value invalid for void function"
    LVEXPEC "Invalid operand; an L-value expected"
    INVAIDX "Invalid array indexing"
    FNEXPEC "Function name expected; found: %s"
    INVCAST "Invalid cast from %s to %s"
})

(defn get-position
  "Gets a string with a token's position in the source code."
  [t]
  (if (nil? t)
    ""
    (format " [line %d col %d]"
	    (.getLine t)
	    (.getCharPositionInLine t))))

(defn panic
  "Throws an exception to stop the compilation."
  [token code & args]
  (let [msg (apply format (cat code) args)
        pos (get-position token)]
    (throw (smallc.CompileException. code (str msg pos)))))

(defn get-log
  "Creates a logger object."
  [name]
  (org.slf4j.LoggerFactory/getLogger name))

(defn trace
  "Logs a message at the TRACE level."
  ([logger format]
    (.trace logger format))
  ([logger format & args]
    (.trace logger format args)))

(defn debug
  "Logs a message at the DEBUG level."
  ([logger format]
    (.debug logger format))
  ([logger format & args]
    (.debug logger format args)))

(defn info
  "Logs a message at the INFO level."
  ([logger format]
    (.info logger format))
  ([logger format & args]
    (.info logger format args)))

(defn error
  "Logs a message at the ERROR level."
  ([logger format]
    (.error logger format))
  ([logger format & args]
    (.error logger format args)))

(defn tree-text
  "Gets the text of a tree root or that of its nth child."
  ([t] (.getText t))
  ([t n] (tree-text (.getChild t n))))

(defn tree-type
  "Gets the type of a tree root or that of its nth child."
  ([t] (.getType t))
  ([t n] (tree-type (.getChild t n))))

(defn parent-type
  "Gets the parent's type."
  [t] (tree-type (.getParent t)))

(defn child-count
  "Returns the number of children in the tree."
  [t] (.getChildCount t))

(defn children?
  "Returns whether the tree has any children."
  [t] (pos? (child-count t)))

(defn child
  "Gets the nth child from a tree node; otherwise, nil."
  [t n]
  (let [count (child-count t)]
    (if (or (zero? count) (>= n count))
      nil
      (.getChild t n))))

(defn seq-children
  "Returns a sequence of a tree's children."
  [tree]
  (if (children? tree)
    (seq (.getChildren tree))
    (list)))

(defn tree-str
  "Returns a Common Tree as a string."
  [t] (.toStringTree t))

(defn print-tree
  "Prints a tree to stdout."
  [t] (println (tree-str t)))
