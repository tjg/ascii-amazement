(ns weft.io
  (:require [clojure.pprint :as pprint :refer [pprint cl-format]]
            [clojure.java.io :as io]))

(set! *print-length* 10)

(defn read-maze [path]
  (with-open [rdr (io/reader path)]
    (->> (line-seq rdr)
         doall
         (into (vector)))))

(defn get-coordinates [lines marker coordinate-translation]
  (let [[line-nr idx] (->> (map (fn [line-nr]
                                  [line-nr (.indexOf (nth lines line-nr)
                                                     marker)])
                                (range (count lines)))
                           (filter (fn [[line-nr idx]]
                                     (not= idx -1)))
                           first)
        [line-nr idx] (coordinate-translation [line-nr idx])]
    [line-nr idx]))

(defn start-coordinates [lines]
  (let [start-marker "Start|"]
    (get-coordinates lines
                     start-marker
                     (fn [[line-nr idx]]
                       [line-nr (+ idx (count start-marker))]))))

(defn end-coordinates [lines]
  (let [start-marker "ITA"]
    (get-coordinates lines
                     start-marker
                     (fn [[line-nr idx]]
                       [(inc line-nr) (dec idx)]))))


(def test-maze-path "resources/mazes/input1.txt")

(def test-maze (read-maze test-maze-path))

(comment
  (doseq [line (read-maze test-maze-path)]
    (println line))

  (->> (read-maze test-maze-path)
       start-coordinates)

  (->> (read-maze test-maze-path)
       end-coordinates)

  (children test-maze (start-coordinates test-maze)))

(wall? test-maze [1 34])
(wall? test-maze [0 34])
