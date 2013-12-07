(ns weft.core-test
  (:use midje.sweet)
  (:require [weft.core :as core]
            [weft.maze :as maze]))

(facts "Solves all the test mazes which ITA provided"
 (doseq [n (range 1 7)]
   (let [expected-input-file  (format "resources/mazes/input%s.txt"  n)
         expected-output-file (format "resources/mazes/output%s.txt" n)
         expected-output (slurp expected-output-file)]
     (fact "Solves maze exactly as ITA did"
        (core/write-solution expected-input-file "out.txt") => irrelevant
        (provided
          (spit "out.txt" expected-output) => irrelevant :times 1)))))

