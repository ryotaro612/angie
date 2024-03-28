(ns factiva-fetcher.core
  (:require [factiva-fetcher.parser :as parser]
            [factiva-fetcher.client :as client]
            [factiva-fetcher.writer :as writer])
  (:gen-class))

(defn -main
  "I don't do a whole lot ... yet."
  [& args]
  (let [{:keys [type directory] :as options} (parser/parse-arguments args)
        writer (writer/create-writer directory)]
    )
  ;; (go-loop []
  ;;   (recur))
  )


;; (defn create-writer
  
;;   [{:keys [filename body]}]
;;   (println filename body))




