(ns kirke.spec.common
  (:require [clojure.spec.alpha :as spec]))

(spec/def ::id string?)
(spec/def ::has-id (spec/keys :req-un [::id]))
(spec/def ::url string?)
(spec/def ::username string?)
(spec/def ::password string?)
(spec/def ::location (spec/keys :req-un [::url] :opt-un [::username ::password]))
(spec/def ::locations (spec/every (spec/or :file-url-task string? :database ::location) :kind vector?))
(spec/def ::async? boolean?)
(spec/def ::input (spec/keys :req-un [::locations]))
(spec/def ::output ::input)
(spec/def ::operation map?)
