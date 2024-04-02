(ns factiva-fetcher.order
  (:require [java-time.api :as jt]))


(defn sort-by-release-date
  [news-coll]

  (let [date-formatted (map
                        (fn [[id release-date title body]]
                          [id
                           (jt/format "yyyy-MM-dd"
                                      (jt/zoned-date-time "yyyy-MM-dd'T'HH:mm:ss.SSSX" release-date))
                           title
                           body])
                        news-coll)
        sorted (reverse (sort-by (fn [rec] (nth rec 1)) date-formatted))]
    sorted))
