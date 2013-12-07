(defproject ascii-amazement "0.1.0-SNAPSHOT"
  :description "ASCII-art Maze solver"
  :url "https://github.com/tjg/ascii-amazement"
  :license {:name "Eclipse Public License"
            :url "http://www.eclipse.org/legal/epl-v10.html"}
  :main ascii-amazement.core
  :dependencies [[org.clojure/clojure "1.5.1"]
                 [org.clojure/tools.cli "0.2.4"]]
  :profiles {:dev {:dependencies [[midje "1.6.0"]]}})
