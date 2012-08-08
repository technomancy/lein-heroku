(ns leiningen.new.heroku
  (:require [leiningen.new.templates :as t]))

(defn heroku
  "Generate a new Compojure project for use on Heroku."
  [name]
  (let [render (t/renderer "heroku")
        data {:name name
              :dir (t/sanitize name)
              :year (+ (.getYear (java.util.Date.)) 1900)}]
    (t/->files data
               [".gitignore" (render "gitignore" data)]
               ["Procfile" (render "Procfile" data)]
               ["README.md" (render "README.md" data)]
               ["src/{{dir}}/web.clj" (render "web.clj" data)]
               ["resources/500.html" (render "500.html" data)]
               ["resources/404.html" (render "404.html" data)]
               ["test/{{dir}}/web_test.clj" (render "test.clj" data)]
               ["project.clj" (render "project.clj" data)]))
  (println "Generated new Heroku project in" name "directory."))
