;; Copyright (c) 2013 Armando Blancas. All rights reserved.
;; The use and distribution terms for this software are covered by the
;; Eclipse Public License 1.0 (http://opensource.org/licenses/eclipse-1.0.php)
;; which can be found in the file epl-v10.html at the root of this distribution.
;; By using this software in any fashion, you are agreeing to be bound by
;; the terms of this license.
;; You must not remove this notice, or any other, from this software.

(ns smallc.asm
  (:use (clojure.contrib import-static)
	(smallc common))
  (:import (org.objectweb.asm ClassWriter MethodVisitor)))

(import-static org.objectweb.asm.Opcodes
    AALOAD
    AASTORE
    ACONST_NULL
    ACC_FINAL
    ACC_PRIVATE
    ACC_PUBLIC
    ACC_STATIC
    ALOAD
    ARETURN
    ANEWARRAY
    ASTORE
    BALOAD
    BASTORE
    BIPUSH
    CALOAD
    CASTORE
    DADD
    DALOAD
    DASTORE
    DCONST_0
    DCONST_1
    DDIV
    DLOAD
    DMUL
    DNEG
    DREM
    DRETURN
    DSTORE
    DSUB
    DUP
    DUP_X2
    DUP2
    DUP2_X2
    FADD
    FALOAD
    FASTORE
    FCONST_0
    FCONST_1
    FCONST_2
    FDIV
    FLOAD
    FMUL
    FNEG
    FREM
    FRETURN
    FSTORE
    FSUB
    GETSTATIC
    IADD
    IALOAD
    IAND
    IASTORE
    ICONST_0
    ICONST_1 
    ICONST_2
    ICONST_3
    ICONST_4
    ICONST_5
    ICONST_M1
    IDIV
    ILOAD
    IMUL
    INVOKESPECIAL
    INVOKESTATIC
    IOR
    IREM
    INEG
    IRETURN
    ISHL
    ISHR
    ISTORE
    ISUB
    IUSHR
    IXOR
    LADD
    LALOAD
    LAND
    LASTORE
    LCONST_0
    LCONST_1
    LDIV
    LLOAD
    LMUL
    LNEG
    LOR
    LRETURN
    LREM
    LSHL
    LSHR
    LSTORE
    LSUB
    LUSHR
    LXOR
    NEWARRAY
    POP
    POP2
    PUTSTATIC
    RETURN
    SALOAD
    SASTORE
    SIPUSH
    T_BOOLEAN
    T_BYTE
    T_CHAR
    T_DOUBLE
    T_FLOAT
    T_INT
    T_LONG
    T_SHORT
    V1_6
)

(def log (get-log "asm"))

;; Arguments for newarray keyed by type descriptors.
(def array-types {
    \Z T_BOOLEAN
    \B T_BYTE
    \C T_CHAR
    \S T_SHORT
    \I T_INT
    \F T_FLOAT
    \D T_DOUBLE
    \J T_LONG
})

(defn scope
  "Returns the scope of a name."
  [s] (if (= (first s) \_) ACC_PRIVATE ACC_PUBLIC))

(defn final-access
  "Returns the access flags for symbolic constants."
  [s] (+ (scope s) ACC_STATIC ACC_FINAL))

(defn access
  "Returns the access flags for a variable or function."
  [s] (+ (scope s) ACC_STATIC))

(defn byte?
  "Tests for a byte value."
  [n] (and (>= n Byte/MIN_VALUE) (<= n Byte/MAX_VALUE)))

(defn short?
  "Tests for a short value."
  [n] (and (>= n Short/MIN_VALUE) (<= n Short/MAX_VALUE)))

(defn desc-to-internal
  [s]
  (let [start (inc (.indexOf s "L"))
	end (dec (.length s))]
    (.substring s start end)))

(defn emit-header
  "Emits the class declaration and default constructor."
  [cw name]
  (.visit cw V1_6 ACC_PUBLIC name nil "java/lang/Object" nil)
  (.visitSource cw (str name ".c") nil)
  (doto (.visitMethod cw ACC_PUBLIC "<init>" "()V" nil nil)
    (.visitCode)
    (.visitVarInsn ALOAD 0)
    (.visitMethodInsn INVOKESPECIAL "java/lang/Object" "<init>" "()V")
    (.visitInsn RETURN)
    (.visitMaxs 1 1)
    (.visitEnd)))

(defn emit-define
  "Emits a symbolic constant."
  [cw name desc value]
  (if-let [fv (.visitField cw (final-access name) name desc nil value)]
    (.visitEnd fv)))

(defn emit-var
  "Emits a global variable."
  [cw name desc value]
  (if-let [fv (.visitField cw (access name) name desc nil value)]
    (.visitEnd fv)))

(defn init-clinit
  "Test for, and start if needed, a visit to <clinit>."
  [cw mv]
  (if (nil? @mv)
    (reset! mv (.visitMethod cw ACC_STATIC "<clinit>" "()V" nil nil))
    (.visitCode @mv)))

(defn visit-end
  "End visits to <clinit> and the class writer."
  [cw mv]
  (when mv (doto mv
             (.visitInsn RETURN)
             (.visitMaxs 0 0)
             (.visitEnd)))
  (.visitEnd cw))

(defn emit-constant
  "Emits a constant value."
  [mv type c]
  (case type
        "V" (.visitInsn mv ACONST_NULL)
	"F" (cond (= c 0.0) (.visitInsn mv FCONST_0)
		  (= c 1.0) (.visitInsn mv FCONST_1)
		  (= c 2.0) (.visitInsn mv FCONST_2)
                   :default (.visitLdcInsn mv (float c)))
	"D" (cond (= c 0.0) (.visitInsn mv DCONST_0)
		  (= c 1.0) (.visitInsn mv DCONST_1)
                   :default (.visitLdcInsn mv (double c)))
        "J" (cond (= c 0) (.visitInsn mv LCONST_0)
                  (= c 1) (.visitInsn mv LCONST_1)
                   :default (.visitLdcInsn mv (long c)))
       ("Z" "C" "B" "S" "I")
            (cond (= c -1)   (.visitInsn mv ICONST_M1)
                  (= c 0)    (.visitInsn mv ICONST_0)
                  (= c 1)    (.visitInsn mv ICONST_1)
                  (= c 2)    (.visitInsn mv ICONST_2)
                  (= c 3)    (.visitInsn mv ICONST_3)
                  (= c 4)    (.visitInsn mv ICONST_4)
                  (= c 5)    (.visitInsn mv ICONST_5)
                  (byte? c)  (.visitIntInsn mv BIPUSH c)
                  (short? c) (.visitIntInsn mv SIPUSH c)
		  :default  (.visitLdcInsn mv (int c)))
        "Ljava/lang/String;" (.visitLdcInsn mv c)))

(defn emit-default
  "Emits a type's default value."
  [mv type]
  (case type "F" (emit-constant mv "F" 0)
	     "D" (emit-constant mv "D" 0)
             "J" (emit-constant mv "J" 0)
            ("Z" "C" "B" "S" "I") (emit-constant mv type 0)
    (emit-constant mv "V" nil)))

(defn emit-array-def
  "Emits an array definition."
  [mv desc dims]
  (if (= (count dims) 1)
    (let [native-type (array-types (last desc))]
      (emit-constant mv "I" (first dims))
      (if native-type
        (.visitIntInsn mv NEWARRAY native-type)
        (.visitTypeInsn mv ANEWARRAY (desc-to-internal desc))))
    (do
      (doseq [d dims] (emit-constant mv "I" d))
      (.visitMultiANewArrayInsn mv desc (count dims)))))

(defn emit-putstatic
  "Emits a store into a static field."
  [mv cname name desc]
  (.visitFieldInsn mv PUTSTATIC cname name desc))

(defn emit-getstatic
  "Emits a load of a static field."
  [mv cname name desc]
  (.visitFieldInsn mv GETSTATIC cname name desc))

(defn emit-invoke-static
  "Emits an invokestatic opcode."
  [mv cname name desc]
  (.visitMethodInsn mv INVOKESTATIC cname name desc))

(defn init-clinit
  "Test for, and start if needed, a visit to <clinit>."
  [cw mv]
  (if (nil? @mv)
    (reset! mv (.visitMethod cw ACC_STATIC "<clinit>" "()V" nil nil))
    (.visitCode @mv)))

(defn init-method
  "Creates a visitor for a new method definition."
  [cw name desc]
  (let [mv (.visitMethod cw (access name) name desc nil, nil)]
    (.visitCode mv)
    mv))

(defn end-method
  "Finish the work on a method definition."
  [mv]
  (doto mv
    (.visitMaxs 0 0)
    (.visitEnd)))

(defn emit-return
  "Emits a return opcode according to the type."
  [mv type]
  (case type "V" (.visitInsn mv RETURN)
             "F" (.visitInsn mv FRETURN)
             "D" (.visitInsn mv DRETURN)
             "J" (.visitInsn mv LRETURN)
            ("Z" "C" "B" "S" "I") (.visitInsn mv IRETURN)
    (.visitInsn mv ARETURN)))

(defn emit-load
  "Emits a load opcode according to the type."
  [mv type slot]
  (case type "F" (.visitVarInsn mv FLOAD slot)
	     "D" (.visitVarInsn mv DLOAD slot)
             "J" (.visitVarInsn mv LLOAD slot)
            ("Z" "C" "B" "S" "I") (.visitVarInsn mv ILOAD slot)
    (.visitVarInsn mv ALOAD slot)))

(defn emit-aload
  "Emits an aload opcode according to the type."
  [mv type]
  (case type "Z" (.visitInsn mv BALOAD)
             "B" (.visitInsn mv BALOAD)
             "C" (.visitInsn mv CALOAD)
             "S" (.visitInsn mv SALOAD)
             "I" (.visitInsn mv IALOAD)
             "J" (.visitInsn mv LALOAD)
             "F" (.visitInsn mv FALOAD)
             "D" (.visitInsn mv DALOAD)
    (.visitInsn mv AALOAD)))

(defn emit-store
  "Emits a store opcode according to the type."
  [mv type slot]
  (case type "F" (.visitVarInsn mv FSTORE slot)
	     "D" (.visitVarInsn mv DSTORE slot)
             "J" (.visitVarInsn mv LSTORE slot)
            ("Z" "C" "B" "S" "I") (.visitVarInsn mv ISTORE slot)
    (.visitVarInsn mv ASTORE slot)))

(defn emit-astore
  "Emits an astore opcode according to the type."
  [mv type]
  (case type "Z" (.visitInsn mv BASTORE)
             "B" (.visitInsn mv BASTORE)
             "C" (.visitInsn mv CASTORE)
             "S" (.visitInsn mv SASTORE)
             "I" (.visitInsn mv IASTORE)
             "J" (.visitInsn mv LASTORE)
             "F" (.visitInsn mv FASTORE)
             "D" (.visitInsn mv DASTORE)
    (.visitInsn mv AASTORE)))

(defn emit-add
  "Emits an add opcode."
  [mv type]
  (case type "F" (.visitInsn mv FADD)
             "D" (.visitInsn mv DADD)
             "J" (.visitInsn mv LADD)
            ("Z" "C" "B" "S" "I") (.visitInsn mv IADD)))

(defn emit-sub
  "Emits a sub opcode."
  [mv type]
  (case type "F" (.visitInsn mv FSUB)
             "D" (.visitInsn mv DSUB)
             "J" (.visitInsn mv LSUB)
            ("Z" "C" "B" "S" "I") (.visitInsn mv ISUB)))

(defn emit-mul
  "Emits a mul opcode."
  [mv type]
  (case type "F" (.visitInsn mv FMUL)
             "D" (.visitInsn mv DMUL)
             "J" (.visitInsn mv LMUL)
            ("Z" "C" "B" "S" "I") (.visitInsn mv IMUL)))

(defn emit-div
  "Emits a div opcode."
  [mv type]
  (case type "F" (.visitInsn mv FDIV)
             "D" (.visitInsn mv DDIV)
             "J" (.visitInsn mv LDIV)
            ("Z" "C" "B" "S" "I") (.visitInsn mv IDIV)))

(defn emit-rem
  "Emits a rem opcode."
  [mv type]
  (case type "F" (.visitInsn mv FREM)
             "D" (.visitInsn mv DREM)
             "J" (.visitInsn mv LREM)
            ("Z" "C" "B" "S" "I") (.visitInsn mv IREM)))

(defn emit-neg
  "Emits a neg opcode."
  [mv type]
  (case type "F" (.visitInsn mv FNEG)
             "D" (.visitInsn mv DNEG)
             "J" (.visitInsn mv LNEG)
            ("Z" "C" "B" "S" "I") (.visitInsn mv INEG)))

(defn emit-dup
  "Emits a dup opcode."
  [mv type]
  (case type ("D" "J") (.visitInsn mv DUP2)
	("Z" "C" "B" "S" "I" "F") (.visitInsn mv DUP)
    (.visitInsn mv DUP)))

(defn emit-dupx2
  "Emits a dup x2 opcode."
  [mv type]
  (case type ("D" "J") (.visitInsn mv DUP2_X2)
	("Z" "C" "B" "S" "I" "F") (.visitInsn mv DUP_X2)
    (.visitInsn mv DUP_X2)))

(defn emit-pop
  "Emits a pop opcode."
  [mv type]
  (case type ("D" "J") (.visitInsn mv POP2)
	("Z" "C" "B" "S" "I" "F") (.visitInsn mv POP)
    (.visitInsn mv POP)))

(defn emit-shiftl
  "Emits a shift left opcode."
  [mv type]
  (case type "I" (.visitInsn mv ISHL)
             "J" (.visitInsn mv LSHL)))

(defn emit-shiftr
  "Emits a shift right opcode."
  [mv type]
  (case type "I" (.visitInsn mv ISHR)
             "J" (.visitInsn mv LSHR)))

(defn emit-shiftu
  "Emits an unsigned shift right opcode."
  [mv type]
  (case type "I" (.visitInsn mv IUSHR)
             "J" (.visitInsn mv LUSHR)))

(defn emit-and
  "Emits a bitwise and opcode."
  [mv type]
  (case type "I" (.visitInsn mv IAND)
             "J" (.visitInsn mv LAND)))

(defn emit-or
  "Emits a bitwise or opcode."
  [mv type]
  (case type "I" (.visitInsn mv IOR)
             "J" (.visitInsn mv LOR)))

(defn emit-not
  "Emits a bitwise not opcode."
  [mv type]
  (case type "I" (do
                   (.visitInsn mv ICONST_M1)
                   (.visitInsn mv IXOR))
	     "J" (do
                   (.visitLdcInsn mv (long -1))
                   (.visitInsn mv LXOR))))

(defn emit-xor
  "Emits a bitwise xor opcode."
  [mv type]
  (case type "I" (.visitInsn mv IXOR)
             "J" (.visitInsn mv LXOR)))

(defn emit-cast
  "For now, just checks that types aren't nil."
  [mv from to expr]
  (when (or (nil? from) (nil? to))
    (panic expr INVCAST from to)))
