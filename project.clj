(defproject lein-heroku "1.0.0-SNAPSHOT"
  :description "Heroku plugin for Leiningen"
  :dependencies [[lein-newnew "0.1.2"]
                 [org.clojure/tools.cli "0.2.1"]
                 [com.heroku.api/heroku-api "0.1-SNAPSHOT"]
                 [com.heroku.api/heroku-http-apache "0.1-SNAPSHOT"]
                 [com.heroku.api/heroku-json-gson "0.1-SNAPSHOT"]
                 [doric "0.6.0"]]
  :eval-in-leiningen true
  :repositories {"sontype-snapshots"
                 "https://oss.sonatype.org/content/repositories/snapshots/"})