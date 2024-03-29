(ns factiva-fetcher.retrieval-test
  (:require [clojure.test :refer :all]
            [factiva-fetcher.retrieval :refer :all]))


;; (defn temp-fetch-news
;;   [{:keys [offset]} fetched-news-ch error-ch]
;;   (async/go
;;       (if (< offset 5)
;;         (async/>! fetched-news-ch {:searchResults [{:company-id "c1"}]})
;;         (async/>! fetched-news-ch {:searchResults []}))))

;; (def temp-initial-options {:offset 0 :limit 2})

;; (def temp-article-ch (async/chan))
