(ns kirke.spec.common
  (:require [clojure.spec.alpha :as spec]))

(spec/def ::id string?)
(spec/def ::has-id (spec/keys :req-un [::id]))
