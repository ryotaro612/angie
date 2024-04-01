(ns factiva-fetcher.writer
  (:require [clojure.java.io :as io]
            [clojure.data.csv :as csv]
            [clojure.core.async :as async]))

(def convert-article-to-record
  (map (fn [{:keys [id title body release-date]}]
         [id release-date title body])))

(defn start-writer
  [ch writer]
  ;(csv/write-csv writer [["ID" "Release Date" "Title" "Body"]])
  (async/go-loop []
    (if-let [record (async/<! ch)]
      (do
        (csv/write-csv writer [record])
        (recur)))))


;; (with-open [writer (io/writer "/home/ryotaro/a.csv")]
;;                           (let [ch (async/chan)
;;                                 result-ch (start-writer ch writer)]
;;                             (async/go
;;                               (async/>! ch {:id "a" :title "doge" :body "body" :date "date"})
;;                               (async/close! ch))
;;                             (async/<!! result-ch)))
                            

                        
