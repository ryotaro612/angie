(ns factiva-fetcher.core
  (:require  [clojure.data.csv :as csv]
            [clojure.java.io :as io]
            [clojure.core.async :as async]
            [factiva-fetcher.parser :as parser]
            [factiva-fetcher.order :as order]            
            [factiva-fetcher.client :as client]
            [factiva-fetcher.news :as news]
            [factiva-fetcher.retrieval :as retrieval]
            [factiva-fetcher.writer :as writer])
  (:gen-class))

(defn -main
  "I don't do a whole lot ... yet."
  [& args]
  (let [{:keys [type filename argument output input] :as options} (parser/parse-arguments args)]
    (if (= :sort type)
      
      (with-open [reader (io/reader input)
                  writer (io/writer output)]
        (let [coll (csv/read-csv reader)]
          (csv/write-csv writer
                         [["ID" "Release Date" "Title" "Body"]])
          (csv/write-csv writer
                         (order/sort-by-release-date coll))))

      
      (let [initial-options (merge {:offset 0 :limit 100 :from "2023-11-01T00:00:00.000Z" :to "2024-04-01T00:00:00.000Z"}
                                   (cond (= type :company) {:company-id argument}
                                         (= type :keyword) {:query argument}
                                         :else (System/exit 1)))
            news-ch (retrieval/retrieve (fn [params fetched-news-ch error-ch]
                                          (client/find-news nil params fetched-news-ch error-ch))
                                        (fn [news-meta news-ch err-ch]
                                          (client/get-news news-meta news-ch err-ch))
                                        initial-options)]

        (writer/start-writer news-ch filename)))))



