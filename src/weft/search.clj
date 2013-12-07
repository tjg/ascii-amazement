(ns weft.search
  (:require [clojure.pprint :as pprint :refer [pprint cl-format]]
            [clojure.set    :as set]
            [loom.graph     :as graph]
            [loom.alg       :as graph-alg]))

(defn make-node [item parent]
  {:parent parent
   :item item})

(defn queue [& xs]
  (into clojure.lang.PersistentQueue/EMPTY xs))

(defn search [queue-constructor start goal? children-fn]
  (loop [frontier (queue-constructor (make-node start nil))
         explored #{}]
    (let [current-node (peek frontier)]
      (cond (not frontier)
            nil

            (goal? (:item current-node))
            current-node

            :else
            (let [children (children-fn (:item current-node))]
              (recur (->> children
                          (remove explored)
                          (map #(make-node % current-node))
                          (#(into (pop frontier) (apply queue-constructor %))))
                     (set/union explored #{(:item current-node)} (set children))))))))

(defn depth-first-search [start goal? children-fn]
  (search list start goal? children-fn))

(defn breadth-first-search [start goal? children-fn]
  (search queue start goal? children-fn))

(defn path [node]
  (loop [curr node
         acc []]
    (if (empty? curr)
      acc
      (recur (:parent curr)
             (conj acc (:item curr))))))


(comment
  (path (depth-first-search 0 #(= % 10) #(list (inc %))))
  (path (depth-first-search 0 #(= % 10)
                            #(list (inc %) (+ 3 %) (+ 5 %)))))
