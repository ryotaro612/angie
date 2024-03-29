(ns factiva-fetcher.retrieval
  (:require [factiva-fetcher.client :as client]
            [clojure.core.async :as async]))


(defn find-news-body
  [get-news-body news-news-body-ch]

  (async/go-loop []
    (if-let [])
    (recur))
  )

(defn find-news
  "initial-option is a map with "
  [fetch-news initial-options article-ch]
  (let [fetched-news-ch (async/chan)
        error-ch (async/chan)]

    (async/go-loop [{:keys [offset] :as options} initial-options]
      (fetch-news options fetched-news-ch error-ch)

      (if-let [{{:keys [searchResults]} :response} (async/<! fetched-news-ch)]
        (let [num (count searchResults)]
          (if (= 0 num)
            (async/close! article-ch)
            (do
              (doseq [article searchResults]
                (async/>! article-ch (assoc article :release-date (:releaseDate article))))
              (recur (assoc options :offset (+ offset num))))))))))
;; {
;;   "searchResults": [
;;     {
;;       "id": "PROFDS0020240329ek410000b",
;;       "title": "In Focus: Streamlight Wedge XT Flashlight",
;;       "body": "Content brought to you by Professional Distributor. To subscribe, click here.\n\nApplication\nThe high-...",
;;       "relations": {
;;         "companies": [
;;           "US9029733048"
;;         ],
;;         "industries": []
;;       },
;;       "hitText": " will be ready to illuminate a space when needed. <em>Red</em> and green indicator LEDs above the charge port",
;;       "publicationDate": "2024-04-01T00:00:00.000Z",
;;       "releaseDate": "2024-03-29T06:06:18.000Z",
;;       "modificationDate": "2024-03-29T06:06:45.417Z",
;;       "medium": {
;;         "code": "PROFDS",
;;         "name": "Professional Distributor",
;;         "owner": false,
;;         "downloadable": true,
;;         "translated": false
;;       },
;;       "original": null,
;;       "sourceUrl": null
;;     }
;;   ],
;;   "hitCount": 84157
;; }
