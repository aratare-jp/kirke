(ns
  kirke.configurer
  (:require [clojure.spec.alpha :as spec]
            [kirke.spec.common :as cm]))

(defrecord Input
  [locations triggers])


(defrecord Task
  [id input operation output])
(spec/def ::task
  (spec/merge
    ::cm/has-id
    (spec/keys
      :req-un [::input])))

(defrecord Graph
  [id tasks])
(spec/def ::graph
  (spec/merge
    ::cm/has-id
    (spec/coll-of
      (partial instance? Task)
      :kind vector?)))
