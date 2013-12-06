(ns weft.core
  (:require [clojure.pprint :as pprint :refer [pprint cl-format]]
            [clojure.java.io :as io]
            [clojure.string :as str]
            [loom.graph :as graph]
            [loom.alg :as graph-alg]))

(set! *print-length* 30)

(defonce lines
  (with-open [rdr (io/reader "resources/movies3.txt")]
    (->> (line-seq rdr)
         (map #(str/split % #" "))
         doall)))

(defn collapse [combine-fn vectors]
  (reduce (fn [acc [k v]]
            (assoc acc k (combine-fn (get acc k [])
                                     v)))
          {}
          vectors))

(defn suffixes [titles]
  (collapse conj
            (mapcat identity
                    (for [t titles]
                      (for [x (next (range (count t)))]
                        [(subvec t x) t])))))

(defn prefixes [titles]
  (collapse conj
            (mapcat identity
                    (for [t titles]
                      (for [x (butlast (map inc (range (count t))))]
                        [(subvec t 0 x) t])))))

(defn adjacency-graph [titles]
  (let [ps (prefixes titles)
        ss (suffixes titles)]
    (collapse concat
              (mapcat identity
                      (for [[suffix ts-with-suffix] ss :when (ps suffix)]
                        (for [t ts-with-suffix]
                          [t (ps suffix)]))))))

(defonce g (graph/graph (adjacency-graph lines)))

(let [good-nodes (->> g
                      graph-alg/connected-components
                      (sort-by count)
                      reverse
                      first)]
  (->> (graph/subgraph g good-nodes)
       graph-alg/bf-traverse
       float))
