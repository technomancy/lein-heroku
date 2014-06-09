(defproject heroku/lein-template "0.4.1-SNAPSHOT"
  :description "Template for new Heroku Compojure web apps."
  :url "https://github.com/technomancy/lein-heroku"
  :license {:name "Eclipse Public License"
            :url "http://www.eclipse.org/legal/epl-v10.html"}
  :release-tasks [["vcs" "assert-committed"]
                  ["change" "version"
                   "leiningen.release/bump-version" "release"]
                  ["vcs" "commit"]
                  ["deploy"]
                  ["change" "version"
                   "leiningen.release/bump-version"]
                  ["vcs" "commit"]]
  :vcs :git)
