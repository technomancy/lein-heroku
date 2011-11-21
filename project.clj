(defproject lein-heroku "1.0.0-SNAPSHOT"
  :description "Heroku plugin for Leiningen"
  :dependencies [[lein-newnew "0.1.0"]
                 [com.heroku.api/heroku-api "0.1-SNAPSHOT"]
                 [com.heroku.api/heroku-httpclient "0.1-SNAPSHOT"]
                 [com.heroku.api/heroku-json-gson "0.1-SNAPSHOT"]
                 [clj-http "0.2.4"]]
  :eval-in-leiningen true)