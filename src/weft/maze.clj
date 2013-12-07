(ns weft.maze
  (:require [clojure.pprint :as pprint :refer [pprint cl-format]]
            [weft.search :as search]
            [weft.io :as io]))

(defn solve [maze]
  (search/depth-first-search (io/start-coordinates maze)
                             (fn [pos]
                               (let [[line-offset idx-offset]
                                     (io/coordinate- pos (io/end-coordinates maze))]
                                 (and (>= 1 (Math/abs line-offset))
                                      (>= 1 (Math/abs idx-offset)))))
                             (fn [pos]
                               (io/children maze pos))))

(comment
  (let [solution (solve io/test-maze)]
    (println (count (search/path solution)))
    (doseq [pos (search/path solution)]
      (println pos))))
