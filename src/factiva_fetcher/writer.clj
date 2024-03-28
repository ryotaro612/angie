(ns factiva-fetcher.writer)

(defn create-writer
  [directory]
  (fn [{:keys [filename body]}]
    (println body)))
