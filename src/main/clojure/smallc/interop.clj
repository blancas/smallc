(ns smallc.interop
  (:use smallc.cc)
  (:gen-class
   :name smallc.Factory
   :methods [^{:static true} [getInstance [] smallc.ISmallcc]]))

(defn -getInstance
  "Returns an instance of the Small-C Compiler."
  []
  (proxy [smallc.ISmallcc] []
    (compile [sd od cp fn] (cc sd od cp fn))
    (getMessage [] (cc-msg))))
