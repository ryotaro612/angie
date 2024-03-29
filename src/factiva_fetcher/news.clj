(ns factiva-fetcher.news
  (:require [factiva-fetcher.news :as news]
            [clojure.string :as string]))

(def xf (map (fn [{{:keys [id title release-date]} :meta {:keys [paragraphs]} :news}]
               [id release-date title (string/join " " (map :value paragraphs))])))
