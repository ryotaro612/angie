(ns factiva-fetcher.client
  (:require [org.httpkit.client :as http]))



(defn get-by-company-id
  [base-url channel {:keys [company-id from to offset limit]}]
  (http/get (str base-url "/v2/news")
            {:query-params {:companyId company-id}}))
