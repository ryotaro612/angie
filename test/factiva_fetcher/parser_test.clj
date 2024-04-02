(ns factiva-fetcher.parser-test
  (:require [clojure.test :refer :all]
            [factiva-fetcher.parser :refer :all]))

(deftest parse-arguments-test
  (testing "Parse the arguments of the main arguments. the first arugment can be company."
    (let [result
          (parse-arguments ["company" "bentz" "~/"])]
      (is (= {:type :company :argument "bentz" :filename "~/"} result))))
  
  (testing "Parse the arguments of the main arguments. the first arugment can be keyword."
    (let [result
          (parse-arguments ["keyword" "bentz" "~/"])]
      (is (= {:type :keyword :argument "bentz" :filename "~/"} result))))
  
  (testing "Parse the arguments such as 'sort filename.csv'"
    (let [result 
         (parse-arguments ["sort" "a.csv" "b.csv"])]
      (is (= {:type :sort :input "a.csv" :output "b.csv"} result)))))
