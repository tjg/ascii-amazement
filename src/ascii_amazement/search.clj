(ns ascii-amazement.search
  "Depth-first search."
  (:require [clojure.pprint :as pprint :refer [pprint cl-format]]
            [clojure.set    :as set]))


;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;;; Graph search mechanism

(defn- make-node [item parent]
  {:parent parent
   :item item})

(defn- queue [& xs]
  (into clojure.lang.PersistentQueue/EMPTY xs))

(defn- search [queue-constructor start goal? children-fn]
  (loop [frontier (queue-constructor (make-node start nil))
         explored #{}]
    (let [current-node (peek frontier)]
      (cond (empty? frontier)
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

(defn- path
  "Given a solution node returned by depth-first-search or
   breadth-first-search, returns a seq of that solution vertex and its
   ancestors, in last-visited-first order."
  [node]
  (loop [curr node
         acc []]
    (if (empty? curr)
      acc
      (recur (:parent curr)
             (conj acc (:item curr))))))


;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;;; Graph searches

(defn depth-first-search
  "Perform graph DFS from start until (goal? vertex) is true or
   vertices are exhausted. Returns a seq:
     [goal-vertex, parent-n, ..., parent-2, parent-1, start-vertex]

   If goal isn't achieved, returns an empty sequence. On infinite
   graphs, this search isn't guaranteed to terminate.

   (children-fn vertex) should return the next vertices to
   search. Children are visited in reverse order (so the last child
   returned by children-fn will be the next to be visited.)"
  [start goal? children-fn]
  (path (search list start goal? children-fn)))

(defn- breadth-first-search [start goal? children-fn]
  (path (search queue start goal? children-fn)))
