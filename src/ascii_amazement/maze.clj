(ns ascii-amazement.maze
  "Maze operations.

   Main functions:
   * Reading a maze from the filesystem. (read-maze)
   * Printing maze and a successful path through it. (format-maze)
   * Start and end coordinates. (start-coordinates and end-coordinates)
   * Moving through maze legally. (possible-moves)

  ITA's mazes, and sample solutions, seem to imply a peculiar motion
  through the maze. When traversing it, we'll take two character-sized
  steps. When printing it, we'll portray a kind of hopping."
  (:require [clojure.pprint :as pprint :refer [pprint cl-format]]
            [clojure.java.io :as io]))


;; INTERNALS: a maze is represented internally as a vector of text lines.
;; A coordinate is [line-number, column-number]


;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;;; Input

(defn read-maze
  "Read in maze object from filesystem at path."
  [path]
  (with-open [rdr (io/reader path)]
    (->> (line-seq rdr)
         doall
         (into (vector)))))


;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;;; Output

(defn- divisible-by-3? [n]
  (= 0 (mod n 3)))

(defn- format-path [line column-nrs]
  (with-out-str
    (doseq [col (range (count line))]
      ;; ITA's solutions use a rather odd style to depict one's path
      ;; through the maze.
      (if (or (and (divisible-by-3? col)
                   (or (column-nrs col)
                       (column-nrs (dec col))
                       (column-nrs (inc col))))
              (and (divisible-by-3? (dec col))
                   (or (column-nrs col)
                       (column-nrs (dec col))
                       (column-nrs (dec (dec col))))))
        (print "X")
        (print (.charAt line col))))))

(defn format-maze
  "String representation of a maze, including one's path through it."
  [maze path-through-maze]
  (with-out-str
    (let [path-per-line-nr (group-by first path-through-maze)
          maze (doseq [line-nr (range (count maze))]
                 (let [line (maze line-nr)
                       column-nrs (->> (path-per-line-nr line-nr)
                                       (map second)
                                       (into #{}))]
                   (println (format-path line column-nrs))))])))


;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;;; Positions and movement

(defn- get-coordinates [maze-lines marker coordinate-translation]
  (let [[line-nr col-nr] (->> (range (count maze-lines))
                              (map (fn [line-nr]
                                     [line-nr (.indexOf (nth maze-lines line-nr)
                                                        marker)]))
                              ;; Which line contains start/end marker?
                              (filter (fn [[line-nr col-nr]]
                                        (not= col-nr -1)))
                              first)
        ;; Move coordinates a bit, to ITA's particular start/end
        ;; positions.
        [line-nr col-nr] (coordinate-translation [line-nr col-nr])]
    [line-nr col-nr]))

(defn start-coordinates [maze-lines]
  (let [start-marker "Start|"]
    (get-coordinates maze-lines
                     start-marker
                     (fn [[line-nr col-nr]]
                       [(dec line-nr)
                        (+ col-nr (count start-marker))]))))

(defn end-coordinates [maze-lines]
  (let [start-marker "ITA"]
    (get-coordinates maze-lines
                     start-marker
                     (fn [[line-nr col-nr]]
                       [(inc line-nr) (dec col-nr)]))))

(defn coordinate- [[y0 x0] [y1 x1]]
  [(- y0 y1) (- x0 x1)])

(defn- coordinate+ [[y0 x0] [y1 x1]]
  [(+ y0 y1) (+ x0 x1)])

(defn- char-at [maze [line-nr col-nr]]
  (nth (nth maze line-nr)
       col-nr))

(defn- move [maze pos step-1-offset step-2-offset test-steps-for-legality]
  (try
    (let [step-0 (char-at maze pos)
          step-1 (char-at maze (coordinate+ pos step-1-offset))
          step-2 (char-at maze (coordinate+ pos step-2-offset))
          new-pos (coordinate+ pos step-2-offset)]
      (if (test-steps-for-legality step-0 step-1 step-2)
        new-pos
        false))
    (catch Exception e
      false)))

(defn- down [maze pos]
  (move maze pos [1 0] [2 0]
        (fn [step-0 step-1 step-2]
          (and (= step-0 \space)
               (= step-1 \space)
               (#{\space \_} step-2)))))

(defn- up [maze pos]
  (move maze pos [-1 0] [-2 0]
        (fn [step-0 step-1 step-2]
          (and (= step-1 \space)
               (= step-2 \space)))))

(defn- left [maze pos]
  (move maze pos [0 -1] [0 -2]
        (fn [step-0 step-1 step-2]
          (and (#{\space \_} step-1)
               (#{\space \_} step-2)))))

(defn- right [maze pos]
  (move maze pos [0 1] [0 2]
        (fn [step-0 step-1 step-2]
          (and (#{\space \_} step-1)
               (#{\space \_} step-2)))))

(defn possible-moves
  "Sequence of coordinates, representing the legal moves in the maze
  at pos."
  [maze pos]
  (let [moves (->> [up down left right]
                   (map #(% maze pos))
                   (filter identity))]
    moves))
