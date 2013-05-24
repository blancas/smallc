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
