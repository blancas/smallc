;; Copyright (c) 2013 Armando Blancas. All rights reserved.
;; The use and distribution terms for this software are covered by the
;; Eclipse Public License 1.0 (http://opensource.org/licenses/eclipse-1.0.php)
;; which can be found in the file epl-v10.html at the root of this distribution.
;; By using this software in any fashion, you are agreeing to be bound by
;; the terms of this license.
;; You must not remove this notice, or any other, from this software.

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
