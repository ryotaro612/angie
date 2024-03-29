(ns factiva-fetcher.retrieval
  (:require [factiva-fetcher.client :as client]
            [clojure.core.async :as async]))


(defn get-news
  [fetch-news news-meta-ch news-ch]
  (let [err-ch (async/chan)]
    (async/go-loop []
      (if-let [meta (async/<! news-meta-ch)]
        (do
          (fetch-news meta news-ch err-ch)
          (recur))
        (async/close! news-ch)))))


(defn find-news-meta
  "initial-option is a map with "
  [find-news initial-options news-meta-ch]
  (let [found-news-ch (async/chan)
        error-ch (async/chan)]

    (async/go-loop [{:keys [offset] :as options} initial-options]
      (find-news options found-news-ch error-ch)

      (if-let [{{:keys [searchResults]} :response} (async/<! found-news-ch)]
        (let [num (count searchResults)]
          (if (= 0 num)
            (async/close! news-meta-ch)
            (do
              (doseq [result searchResults]
                (async/>! news-meta-ch (assoc result :release-date (:releaseDate result))))
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
