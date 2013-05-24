(ns smallc.cc
  (:gen-class)
  (:use [smallc.common]
	[smallc.parser :only (parse transform translate generate)])
  (:import (java.io File)
	   (java.util.regex Pattern)
	   (smallc CompileException)))

(def log (get-log "cc"))

(def success 0)
(def message (atom nil))

(defn- dextend
  "Strips the extension from a file name."
  [name]
  (let [idx (.lastIndexOf name ".")]
    (if (pos? idx) (.substring name 0 idx) name)))

(defn- internal
  "Returns the internal form of a Small-C file name."
  [name]
  (let [s (dextend name)
	dot (Pattern/quote ".")
	sep (Pattern/quote File/separator)]
    (.replaceAll (.replaceAll s dot "/") sep "/")))

(defn- open-file
  "Opens the file to compile."
  [source name]
  (let [file (File. source name)]
    (if (and (.isFile file) (.exists file))
      file
      (panic nil NFOUND "File" name))))

(defn- handler
  "Exception handler for the main entry point."
  [e]
  (.error log "handler" e)
  (reset! message (.getMessage e))
  (if (instance? CompileException e)
    (.getCode e) -1))

(defn cc
  "Entry point for the Small-C Compiler. Takes a source
   directory,  output directory, classpath, and a file name.
   The return value is zero for success an error code."
  [sd od cp fn]
  (try
    (let [name (internal fn)
         file (open-file sd fn)]
      (-> file (parse)
               (transform cp)
               (translate cp name)
               (generate od name))
      (reset! message "Compilation successful")
      success)
    (catch Throwable t
      (handler t))))

(defn cc-msg
  "Returns the message for the last compilation."
  [] @message)
