(ns factiva-fetcher.core
  (:require [factiva-fetcher.parser :as parser]
            [factiva-fetcher.client :as client]
            [factiva-fetcher.news :as news]
            [factiva-fetcher.retrieval :as retrieval]
            [clojure.java.io :as io]
            [clojure.core.async :as async]
            [factiva-fetcher.writer :as writer])
  (:gen-class))

(defn -main
  "I don't do a whole lot ... yet."
  [& args]
  (let [{:keys [type filename argument] :as options} (parser/parse-arguments args)
        initial-options (merge {:offset 0 :limit 100 :from "2023-11-01T00:00:00.000Z" :to "2024-04-01T00:00:00.000Z"}
                               (cond (= type :company) {:company-id argument}
                                     (= type :keyword) {:query argument}
                                     :else (System/exit 1)))
        news-meta-ch (async/chan)
        news-ch (async/chan 10 news/xf)]
    
    (retrieval/find-news-meta (fn [params fetched-news-ch error-ch]
                                (client/find-news nil nil params fetched-news-ch error-ch))
                              initial-options
                              news-meta-ch)

    (retrieval/get-news (fn [news-meta news-ch err-ch]
                          (client/get-news news-meta news-ch err-ch))
                        news-meta-ch
                        news-ch)
    
    (with-open [writer (io/writer filename)]
      (let [write-result-ch (writer/start-writer news-ch writer)]
        (async/<!! write-result-ch)))))

