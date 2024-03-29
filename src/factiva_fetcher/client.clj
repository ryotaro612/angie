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
  
  (http/get (str "http://10.101.18.65/v1/articles/" id)
            {:headers {"host" "legacy.news-api.ub-speeda.lan"}
             :timeout 2000}
            (fn [{:keys [status headers body error] :as response}]
              (if (= status 200)
                (async/>!! ch {:meta meta
                               :news (json/read-str body :key-fn keyword)})
                (async/>!! err-ch {:status status :response response})))))

(defn find-news
  "from and to should be java.time.ZonedDateTime objects."
  [base-url host {:keys [company-id query from to offset limit] :as request} ch err-ch]
  (let [option (cond company-id {:company-id company-id}
                     query {:query query}
                     :else {})]
    (http/get (str (or base-url "http://10.101.18.65") "/v2/news")
            {:headers {"x-speeda-client-industry-id" 110
                       "host" (or host "legacy.news-api.ub-speeda.lan")}
             :timeout 2000
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
                (async/>!! err-ch {:status status :response response}))))))
