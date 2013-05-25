;; Copyright (c) 2013 Armando Blancas. All rights reserved.
;; The use and distribution terms for this software are covered by the
;; Eclipse Public License 1.0 (http://opensource.org/licenses/eclipse-1.0.php)
;; which can be found in the file epl-v10.html at the root of this distribution.
;; By using this software in any fashion, you are agreeing to be bound by
;; the terms of this license.
;; You must not remove this notice, or any other, from this software.

(ns smallc.main
  (:gen-class)
  (:use (smallc cc)))

(def src-path (atom "."))
(def out-path (atom "."))
(def class-path (atom "."))

(defn arguments
  [coll]
  (if-let [arg (first coll)]
    (if-let [val (second coll)]
      (do
        (cond (= arg "-sourcepath") (reset! src-path val)
	      (= arg "-d")          (reset! out-path val)
	      (= arg "-classpath")  (reset! class-path val))
	(recur (rest (rest coll)))))))

(defn -main
  "Usage: scc [options] <file>
       file         A Small-C source file
options:
       -sourcepath  Where to find the source file
       -d           Where to write the class file
       -classpath   Where to find user class files"
  [& args]
  (try
    (arguments args)
    (if-let [file (last args)]
      (let [code (cc @src-path @out-path @class-path file)]
	(when-not (zero? code)
	  (.println *err* (format "%d: %s" code (cc-msg)))))
      (println (:doc (meta (var -main)))))
    (catch Throwable t
      (.println *err* (.getMessage t)))))
