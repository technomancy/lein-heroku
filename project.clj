(defproject lein-heroku "0.1.0-SNAPSHOT"
  :description "Heroku plugin for Leiningen"
  :dependencies [[lein-newnew "0.1.2"]
                 [org.clojure/tools.cli "0.2.1"]
                 [org.clojars.technomancy/heroku-api "0.1-20111019.122151-1"]
                 [org.clojars.technomancy/heroku-http-apache "0.1-20111019.122151-1"]
                 [org.clojars.technomancy/heroku-json-gson "0.1-20111019.122151-1"]
                 [doric "0.6.0"]]
  :eval-in-leiningen true
  ;; :repositories {"sontype-snapshots"
  ;;                "https://oss.sonatype.org/content/repositories/snapshots/"}
  )