(ns factiva-fetcher.parser)

(defn parse-arguments
  "Parse the arguments of the main function."
  [[type argument directory]]
  (if (and (and type argument directory)
           (contains? #{"company" "keyword"} type))
    {:type (keyword type) :argument argument :directory directory}))
