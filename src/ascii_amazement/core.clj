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

(defn -main [& args]
  (let [[options args banner]
        (cli args
             ["-h" "--help" "Show help" :default false]
             ["-i" "--input" "Input maze path" :default false]
             ["-o" "--output" "Output maze path" :default false])]
    (if (or (:help options)
            (not (:input options))
            (not (:output options)))
      (println banner)
      (write-solution (:input options) (:output options))))
  (flush))
