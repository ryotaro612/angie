(ns factiva-fetcher.client
  (:require [org.httpkit.client :as http]
            [clojure.data.json :as json]
            [clojure.core.async :as async]
            [java-time.api :as jt]))


(defn- coerce-to-datetime-str
  [zoned-date-time]
  (jt/format "yyyy-MM-dd'T'HH:mm:ss.SSSX" zoned-date-time))

(defn get-news
  "from and to should be java.time.ZonedDateTime objects."
  [base-url host channel error-channel {:keys [company-id query from to offset limit] :as request}]
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
                                   :from (coerce-to-datetime-str from)
                                   :to (coerce-to-datetime-str to)})}
            
            (fn [{:keys [status headers body error] :as response}]
              (if (= status 200)
                (async/go (async/>! channel {:request request
                                             :response (json/read-str body :key-fn keyword)}))
                (async/go (async/>! error-channel {:status status :response response})))))))
