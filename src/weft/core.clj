(ns weft.core
  (:require [clojure.pprint :as pprint :refer [pprint cl-format]]
            [clojure.java.io :as io]
            [clojure.string :as str]
            [clojure.set :as set]
            [loom.graph :as graph]
            [loom.alg :as graph-alg]))

(set! *print-length* 10)

(defonce lines
  (with-open [rdr (io/reader "resources/movies3.txt")]
    (->> (line-seq rdr)
         (map #(str/split % #" "))
         doall)))

(defn collapse [combine-fn vectors]
  (reduce (fn [acc [k v]]
            (assoc acc k (combine-fn (acc k [])
                                     v)))
          {}
          vectors))

(defn suffixes [s]
  (loop [sub (next s)
         acc []]
    (if (seq sub)
      (recur (next sub)
             (conj acc (vec sub)))
      acc)))

(defn prefixes [s]
  (->> s reverse suffixes (map reverse) (map vec)))

(defn prefix-or-suffix-map [prefixes-or-suffixes-fn titles]
  (->> titles
       (mapcat (fn [t]
                 (->> (prefixes-or-suffixes-fn t)
                      (map (fn [s] [s (str/join " " t)])))))
       (collapse conj)))

(defn adjacency-graph [titles]
  (let [ps (prefix-or-suffix-map prefixes titles)
        ss (prefix-or-suffix-map suffixes titles)]
    (->> (for [[suffix ts-with-suffix] ss :when (ps suffix)]
           (for [t ts-with-suffix]
             [t (ps suffix)]))
         (mapcat identity)
         (collapse concat))))

(defonce g (graph/digraph (adjacency-graph lines)))

(comment
 (let [good-nodes (->> g
                       graph-alg/connected-components
                       (sort-by count)
                       reverse
                       first)]
   (-> (graph/subgraph g good-nodes)
       (graph-alg/bf-path "JAWS 2" "NIGHT AT THE MUSEUM")
       pprint))

 (pprint (graph-alg/bf-path g "JAWS 2" "THE HOUSE OF MIRTH"))

 (pprint (graph-alg/bf-path g "JAWS 2" "NIGHT AT THE MUSEUM")))
