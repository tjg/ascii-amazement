(defproject ascii-amazement "0.1.0-SNAPSHOT"
  :description "FIXME: write description"
  :url "http://example.com/FIXME"
  :license {:name "Eclipse Public License"
            :url "http://www.eclipse.org/legal/epl-v10.html"}
  :main ascii-amazement.core
  :dependencies [[org.clojure/clojure "1.5.1"]
                 [org.clojure/tools.cli "0.2.4"]
                 [com.taoensso/timbre "2.7.1"]]
  :profiles {:dev {:dependencies [[midje "1.6.0"]]}})
