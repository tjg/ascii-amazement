(ns weft.solve
  (:require [clojure.pprint :as pprint :refer [pprint cl-format]]
            [weft.search :as search]
            [weft.maze :as maze]))

(defn solve [maze]
  (search/depth-first-search (maze/start-coordinates maze)
                             (fn [pos]
                               (let [[line-offset idx-offset]
                                     (maze/coordinate- pos
                                                       (maze/end-coordinates maze))]
                                 (and (>= 1 (Math/abs line-offset))
                                      (>= 1 (Math/abs idx-offset)))))
                             (fn [pos]
                               (maze/children maze pos))))

(comment
  (let [solution (solve maze/test-maze)]
    (println (count (search/path solution)))
    (doseq [pos (search/path solution)]
      (println pos)))

  (let [path (search/path (solve maze/test-maze))]
    (maze/write-maze "test-output.txt" maze/test-maze path)))
