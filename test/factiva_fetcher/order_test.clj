(ns factiva-fetcher.order-test
  (:require [clojure.test :refer :all]
            [factiva-fetcher.order :refer :all]))

(deftest sort-by-release-date-test
  (testing "Sort records by release date"
    (let [result (sort-by-release-date
                  [["id0" "2023-03-23T07:47:33.000Z" "title0" "body0"]
                   ["id1" "2024-03-23T07:47:33.000Z" "title1" "body1"]
                   ])]
      (is (= [["id1" "2024-03-23" "title1" "body1"]
              ["id0" "2023-03-23" "title0" "body0"]]
             result)))))
