(ns factiva-fetcher.writer
  (:require [clojure.java.io :as io]
            [clojure.data.csv :as csv]
            [clojure.core.async :as async]))

(def convert-article-to-record
  (map (fn [{:keys [id title body release-date]}]
         [id release-date title body])))

(defn start-writer
  [ch writer]
  (async/go-loop []
    (if-let [{:keys [id title body release-date] :as article} (async/<! ch)]
      (do
        (println article)
        (csv/write-csv writer [[id release-date title body]])
        (recur)))))


;; (with-open [writer (io/writer "/home/ryotaro/a.csv")]
;;                           (let [ch (async/chan)
;;                                 result-ch (start-writer ch writer)]
;;                             (async/go
;;                               (async/>! ch {:id "a" :title "doge" :body "body" :date "date"})
;;                               (async/close! ch))
;;                             (async/<!! result-ch)))
                            

                        
