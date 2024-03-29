(ns factiva-fetcher.core
  (:require [factiva-fetcher.parser :as parser]
            [factiva-fetcher.client :as client]
            [factiva-fetcher.retrieval :as retrieval]
            [clojure.java.io :as io]
            [clojure.core.async :as async]
            [factiva-fetcher.writer :as writer])
  (:gen-class))

(defn -main
  "I don't do a whole lot ... yet."
  [& args]
  (let [{:keys [type filename argument] :as options} (parser/parse-arguments args)
        article-ch (async/chan 10 writer/convert-article-to-record)
        initial-options (merge {:offset 0 :limit 100 :from "2023-11-01T00:00:00.000Z" :to "2024-04-01T00:00:00.000Z"}
                               (cond (= type :company) {:company-id argument}
                                     (= type :keyword) {:query argument}
                                     :else (System/exit 1)))]
    
    (retrieval/find-news (fn [params fetched-news-ch error-ch]
                           (client/find-news nil nil params fetched-news-ch error-ch))
                         initial-options
                         article-ch)
    
    (with-open [writer (io/writer filename)]
      (let [write-result-ch (writer/start-writer article-ch writer)]
        (async/<!! write-result-ch)))))

