(ns ascii-amazement.search-test
  (:use midje.sweet)
  (:require [ascii-amazement.search :as search]))

(fact "Depth-first search works"
   (search/depth-first-search 0 #(= % 10) #(list (inc %)))
   => [10 9 8 7 6 5 4 3 2 1 0]

   (search/depth-first-search 0
                              #(= % 10)
                              #(list (inc %) (+ 3 %) (+ 5 %)))
   => [10 5 0])
