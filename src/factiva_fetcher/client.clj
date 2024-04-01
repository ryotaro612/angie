(ns factiva-fetcher.client
  (:require [org.httpkit.client :as http]
            [clojure.data.json :as json]
            [clojure.core.async :as async]
            [java-time.api :as jt]))


(defn- coerce-to-datetime-str
  [zoned-date-time]
  (jt/format "yyyy-MM-dd'T'HH:mm:ss.SSSX" zoned-date-time))

(defn get-news
  [{:keys [id] :as meta} ch err-ch]
  (http/get (str "http://legacy.news-api.ub-speeda.lan/v1/articles/" id)
            {:timeout 10000}
            (fn [{:keys [status headers body error] :as response}]
              (if (= status 200)
                (async/>!! ch {:meta meta
                               :news (json/read-str body :key-fn keyword)})
                (do
                  (println "get-news error: " id)
                  (async/>!! err-ch {:status status :response response})
                  )))))

(defn find-news
  "from and to should be java.time.ZonedDateTime objects."
  [base-url {:keys [company-id query from to offset limit] :as request} ch err-ch]
  (let [option (cond company-id {:companyId company-id}
                     query {:query query}
                     :else {})]
    (http/get (str (or base-url "http://legacy.news-api.ub-speeda.lan") "/v2/news")
            {:headers {"x-speeda-client-industry-id" 110}
             :timeout 10000
             :query-params (merge option
                                  {:limit limit
                                   :offset offset
                                   :language "en"
                                   :from from
                                   :to to})}
            
            (fn [{:keys [status headers body error] :as response}]
              (if (= status 200)
                (async/>!! ch {:request request
                               :response (json/read-str body :key-fn keyword)})
                (do
                  (println "find-news error: offfset->" offset " limit->" limit " body: " body)
                  (async/>!! err-ch {:status status :response response})
                  ))))))
