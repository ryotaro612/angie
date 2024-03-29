(ns factiva-fetcher.client
  (:require [org.httpkit.client :as http]
            [clojure.data.json :as json]
            [clojure.core.async :as async]
            [java-time.api :as jt]))


(defn- coerce-to-datetime-str
  [zoned-date-time]
  (jt/format "yyyy-MM-dd'T'HH:mm:ss.SSSX" zoned-date-time))

(defn get-news
  [news-id ch err-ch]
  (http/get (str "http://10.101.18.65/v1/articles/" news-id)
            {:headers {"host" (or host "legacy.news-api.ub-speeda.lan")}}
            (fn [{:keys [status headers body error] :as response}]
              (if (= status 200)
                (async/go (async/>! ch {:news-id news-id
                                        :news (json/read-str body :key-fn keyword)}))
                (async/go (async/>! err-ch {:status status :response response}))))))

(defn find-news
  "from and to should be java.time.ZonedDateTime objects."
  [base-url host {:keys [company-id query from to offset limit] :as request} channel error-channel]
  (let [option (cond company-id {:company-id company-id}
                     query {:query query}
                     :else {})]
    (http/get (str (or base-url "http://10.101.18.65") "/v2/news")
            {:headers {"x-speeda-client-industry-id" 110
                       "host" (or host "legacy.news-api.ub-speeda.lan")}
             :query-params (merge option
                                  {:limit limit
                                   :offset offset
                                   :language "en"
                                   :from from
                                   :to to})}
            
            (fn [{:keys [status headers body error] :as response}]
              (if (= status 200)
                (async/go (async/>! channel {:request request
                                             :response (json/read-str body :key-fn keyword)}))
                (async/go (async/>! error-channel {:status status :response response})))))))
