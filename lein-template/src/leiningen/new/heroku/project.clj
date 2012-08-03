(defproject {{name}} "1.0.0-SNAPSHOT"
  :description "FIXME: write description"
  :url "http://{{name}}.herokuapp.com"
  :license {:name "FIXME: choose"
            :url "http://example.com/FIXME"}
  :dependencies [[org.clojure/clojure "1.4.0"]
                 [compojure "1.1.1"]
                 [ring/ring-jetty-adapter "1.1.0"]
                 [ring/ring-devel "1.1.0"]
                 [environ "0.2.1"]
                 [com.cemerick/drawbridge "0.0.6"]])