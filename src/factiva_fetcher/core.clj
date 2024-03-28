(ns factiva-fetcher.core
  (:gen-class))

(defn -main
  "I don't do a whole lot ... yet."
  [& [company-id]]
  ;; (go-loop []
  ;;   (recur))
  )

;; (defn -fetch-company
;;   [])



;; (defn create-writer
  
;;   [{:keys [filename body]}]
;;   (println filename body))

(defn create-writer
  []
  (fn [{:keys [filename body]}]
    (println body)))


