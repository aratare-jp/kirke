(ns kirke.executor
  (:require [taoensso.timbre :as timbre]
            [clojure.spec.alpha :as spec]
            [kirke.spec.common :as cm]))

(defn start
  "Start the executor with a given Kirke config map"
  [config-map]
  (timbre/debug config-map))

(spec/fdef start :args (spec/cat :config-map ::cm/pipelines) :ret nil?)
