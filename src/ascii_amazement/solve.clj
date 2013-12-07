(ns ascii-amazement.solve
  "Solves a given maze."
  (:require [clojure.pprint :as pprint :refer [pprint cl-format]]
            [ascii-amazement.search :as search]
            [ascii-amazement.maze :as maze]))


(defn solve
  "Given a maze, return a solution path through it."
  [maze]
  (search/depth-first-search (maze/start-coordinates maze)
                             (fn [pos]
                               ;; Since we move 2 text characters at
                               ;; a time, we only need to be within 1
                               ;; char of goal.
                               (let [[line-offset idx-offset]
                                     (maze/coordinate- pos
                                                       (maze/end-coordinates maze))]
                                 (and (>= 1 (Math/abs line-offset))
                                      (>= 1 (Math/abs idx-offset)))))
                             (fn [pos]
                               (maze/possible-moves maze pos))))
