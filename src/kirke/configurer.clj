(ns
  kirke.configurer
  (:require [clojure.spec.alpha :as spec]
            [kirke.spec.common :as cm]))

(defrecord Input
  [locations triggers])
(spec/def ::url string?)
(spec/def ::username string?)
(spec/def ::password string?)
(spec/def ::location
  (spec/keys :req-un [::url ::username ::password]))
(spec/def ::locations
  (spec/every
    (spec/or
      :file-url-task string?
      :database ::location)
    :kind vector?))
(spec/def ::async? boolean?)
(spec/def ::trigger
  (spec/keys :req-un [::async?]))
(spec/def ::triggers
  (spec/nilable
    (spec/every
      (spec/or
        :string string?
        :trigger ::trigger)
      :kind vector?)))
(spec/def ::input
  (spec/keys :req-un [::locations ::triggers]))
(spec/def ::output ::input)

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
