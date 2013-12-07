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

(defn coordinate+ [[y0 x0] [y1 x1]]
  [(+ y0 y1) (+ x0 x1)])

(defn char-at [maze [line-nr idx]]
  (nth (nth maze line-nr)
       idx))

(defn down [maze pos]
  (try
    (let [step-0 (char-at maze pos)
          step-1 (char-at maze (coordinate+ pos [1 0]))
          step-2 (char-at maze (coordinate+ pos [2 0]))
          new-pos (coordinate+ pos [2 0])]
      (if (and (= step-0 \space)
               (= step-1 \space)
               (or (= step-2 \space)
                   (= step-2 \_)))
        new-pos
        false))
    (catch Exception e
      false)))

(defn up [maze pos]
  (try
    (let [step-0 (char-at maze pos)
          step-1 (char-at maze (coordinate+ pos [-1 0]))
          step-2 (char-at maze (coordinate+ pos [-2 0]))
          new-pos (coordinate+ pos [-2 0])]
      (if (and (= step-1 \space)
               (= step-2 \space))
        new-pos
        false))
    (catch Exception e
      false)))

(defn left [maze pos]
  (try
    (let [step-0 (char-at maze pos)
          step-1 (char-at maze (coordinate+ pos [0 -1]))
          step-2 (char-at maze (coordinate+ pos [0 -2]))
          new-pos (coordinate+ pos [0 -2])]
      (if (and (= step-1 \space)
               (= step-2 \space))
        new-pos
        false))
    (catch Exception e
      false)))

(defn right [maze pos]
  (try
    (let [step-0 (char-at maze pos)
          step-1 (char-at maze (coordinate+ pos [0 1]))
          step-2 (char-at maze (coordinate+ pos [0 2]))
          new-pos (coordinate+ pos [0 2])]
      (if (and (= step-1 \space)
               (= step-2 \space))
        new-pos
        false))
    (catch Exception e
      false)))

(defn children [maze pos]
  (let [moves (->> [up down left right]
                   (map #(% maze pos))
                   (filter identity))]
    moves))


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
