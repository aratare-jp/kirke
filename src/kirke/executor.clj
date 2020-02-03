(ns kirke.executor
  (:require [taoensso.timbre :as timbre]
            [clojure.spec.alpha :as spec]))

(defn start
  "Start the executor with a given Kirke config map"
  [config-map]
  (timbre/debug config-map))
