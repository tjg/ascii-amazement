(ns ascii-amazement.core
  (:gen-class)
  (:require [clojure.pprint :as pprint :refer [pprint cl-format]]
            [ascii-amazement.solve :as solve]
            [ascii-amazement.maze :as maze]
            [clojure.tools.cli :as cli :refer [cli]]))


(defn- write-solution [in-path out-path]
  (let [maze (maze/read-maze in-path)
        solution (solve/solve maze)]
    (->> (maze/format-maze maze solution)
         (spit out-path))))


;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;;; Commandline entrypoint

(def IO-ERROR-EXIT-CODE 74)

(defn -main [& args]
  (let [[options args banner]
        (cli args
             ["-h" "--help" "Show help" :default false]
             ["-i" "--input" "Input maze path" :default false]
             ["-o" "--output" "Output maze path" :default false])]
    (cond (or (:help options)
              (not (:input options))
              (not (:output options)))
          (println banner)

          (not (.exists (java.io.File. (:input options))))
          (binding [*out* *err*]
            (println "Error:" (:input options) "doesn't exist.")
            (System/exit IO-ERROR-EXIT-CODE))

          (and (.getParentFile (java.io.File. (:output options)))
               (not (.exists (.getParentFile (java.io.File. (:output options))))))
          (binding [*out* *err*]
            (println "Error: output directory"
                     (.getParent (java.io.File. (:output options)))
                     "doesn't exist.")
            (System/exit IO-ERROR-EXIT-CODE))

          :else
          (write-solution (:input options) (:output options))))
  (flush))
