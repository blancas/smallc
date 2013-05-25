;; Copyright (c) 2013 Armando Blancas. All rights reserved.
;; The use and distribution terms for this software are covered by the
;; Eclipse Public License 1.0 (http://opensource.org/licenses/eclipse-1.0.php)
;; which can be found in the file epl-v10.html at the root of this distribution.
;; By using this software in any fashion, you are agreeing to be bound by
;; the terms of this license.
;; You must not remove this notice, or any other, from this software.

(ns smallc.symtbl)

;; The symbol table is a stack of scopes. The first is the global scope.
;; Inner scopes pile up at the TOS and then are popped as they close.

(def symtbl (atom nil))

(defn sym-create
  "Creates an empty symbol table. Discards any previous content.
   Must be called at the start of every compilation."
  [] (reset! symtbl (list (atom {}))))

(defn sym-push
  "Pushes an empty map to create an inner scope."
  [] (swap! symtbl conj (atom {})))

(defn sym-pop
  "Pops the map at the TOS to return to an outer scope."
  [] (swap! symtbl rest))

(defn sym-put
  "Puts the (k,v) pair in the current scope."
  [k v] (swap! (first @symtbl) assoc k v))

(defn sym-get
  "Looks up the key starting at the current scope
   and down the stack to higher-level scopes."
  [k]
  (loop [s @symtbl]
    (when (seq s)
      (or (@(first s) k) (recur (rest s))))))

(defn sym-check
  "Looks up the key in the current scope."
  [k] (@(first @symtbl) k))

;;
;; Symbol table entries store attributes necessary
;; to reference and validate declared objects.
;;

(defrecord symrec [kind    ; const, var or func
		   name    ; used as key to this record
		   type    ; type descriptor
		   vtype   ; a function's vector of param types
		   scope   ; scope number; global is 1
		   class   ; internal name of its class
		   slot])  ; local variable index, zero-based

;; Kind of an entry
(def rec-const 0)
(def rec-var   1)
(def rec-func  2)

(defn sym-global?
  "Tests if the entry has global scope."
  [rec] (= (.scope rec) 1))

(defn sym-const?
  "Tests if the entry is a constant."
  [rec] (= (.kind rec) rec-const))

(defn sym-var?
  "Tests if the entry is a variable."
  [rec] (= (.kind rec) rec-var))

(defn sym-local?
  "Tests if the entry is a local variable."
  [rec] (and (not (sym-global? rec)) (sym-var? rec)))

(defn sym-func?
  "Tests if the entry is a function."
  [rec] (= (.kind rec) rec-func))

(defn sym-constant
  "Creates an entry for a constant and puts it in the symbol
   table. If the name already exists simply returns nil."
  [name type class]
  (when-not (sym-check name)
    (sym-put name (symrec. rec-const name type nil 1 class 0))))

(defn sym-variable
  "Creates an entry for a variable and puts it in the symbol
   table. If name exists in the current scope, returns nil."
  [name type class slot]
  (when-not (sym-check name)
    (let [scope (count @symtbl)
          rec (symrec. rec-var name type nil scope class slot)]
      (sym-put name rec))))

(defn sym-function
  "Creates an entry for a function and puts it in the symbol
   table. If the name already exists simply returns nil."
  [name type vtype class]
  (when-not (sym-check name)
    (sym-put name (symrec. rec-func name type vtype 1 class 0))))
