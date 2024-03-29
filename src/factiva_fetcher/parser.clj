(ns factiva-fetcher.parser)

(defn parse-arguments
  "Parse the arguments of the main function."
  [[type argument filename]]
  (if (and (and type argument filename)
           (contains? #{"company" "keyword"} type))
    {:type (keyword type) :argument argument :filename filename}))
