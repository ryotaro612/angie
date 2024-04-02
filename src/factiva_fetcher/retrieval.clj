(ns factiva-fetcher.retrieval
  (:require [factiva-fetcher.client :as client]
            [factiva-fetcher.news :as news]            
            [clojure.core.async :as async]))


(defn- get-news
  [fetch-news news-meta-ch news-ch]
  (let [err-ch (async/chan)]
    (async/go-loop []
      (if-let [meta (async/<! news-meta-ch)]
        (do
          (fetch-news meta news-ch err-ch)
          (recur))
        (async/close! news-ch)))))


(defn- find-news-meta
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


(defn retrieve
  [find-meta find-news initial-options]
  (let [news-meta-ch (async/chan 5)
        news-ch (async/chan 10 news/xf)]
    (find-news-meta find-meta initial-options news-meta-ch)
    (get-news find-news news-meta-ch news-ch)
    news-ch))
