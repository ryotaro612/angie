(ns factiva-fetcher.writer
  (:require [clojure.java.io :as io]
            [clojure.data.csv :as csv]
            [clojure.core.async :as async]))

(def convert-article-to-record
  (map (fn [{:keys [id title body release-date]}]
         [id release-date title body])))

(defn- listen-record-ch
  [ch writer]
  ;(csv/write-csv writer [["ID" "Release Date" "Title" "Body"]])
  (async/go-loop []
    (if-let [record (async/<! ch)]
      (do
        (csv/write-csv writer [record])
        (recur)))))


(defn start-writer
  [news-ch filename]
  (with-open [writer (io/writer filename)]
    (let [write-result-ch (listen-record-ch news-ch writer)]
      (async/<!! write-result-ch))))
