(ns weft.maze
  (:require [clojure.pprint :as pprint :refer [pprint cl-format]]
            [weft.search :as search]
            [weft.io :as io]))

(search/depth-first-search
 (io/start-coordinates io/test-maze)
 (fn [pos]
   (println "goal test:" pos)
   (let [[line-offset idx-offset]
         (io/coordinate- pos (io/end-coordinates io/test-maze))]
     (and (>= 1 (Math/abs line-offset))
          (>= 1 (Math/abs idx-offset)))))
 (fn [pos]
   (println "children:" pos)
   (io/children io/test-maze pos)))

;;(io/children io/test-maze [40 12])

