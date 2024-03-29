(defproject factiva-fetcher "0.1.0-SNAPSHOT"
  :description "FIXME: write description"
  :url "http://example.com/FIXME"
  :license {:name "EPL-2.0 OR GPL-2.0-or-later WITH Classpath-exception-2.0"
            :url "https://www.eclipse.org/legal/epl-2.0/"}
  :dependencies [[org.clojure/clojure "1.11.1"]
                 [http-kit "2.3.0"]
                 [org.clojure/data.json "2.5.0"]
                 [org.clojure/core.async "1.6.681"]
                 [org.clojure/data.csv "1.1.0"]
                 [clojure.java-time "1.4.2"]]
  :main ^:skip-aot factiva-fetcher.core
  :target-path "target/%s"
  :profiles {:uberjar {:aot :all
                       :jvm-opts ["-Dclojure.compiler.direct-linking=true"]}})
