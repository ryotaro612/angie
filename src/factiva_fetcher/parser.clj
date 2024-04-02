(ns factiva-fetcher.parser)

(defn parse-arguments
  "Parse the arguments of the main function."
  [[type argument argument2]]
  (cond
    (and (and type argument argument2)
         (contains? #{"company" "keyword"} type))
    {:type (keyword type) :argument argument :filename argument2}
    (and (= type "sort") argument)
    {:type (keyword type) :input argument :output argument2}))
