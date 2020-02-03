(ns kirke.core
  (:require [kirke.executor :as ex]
            [clojure.tools.cli :as cli :refer [parse-opts]]
            [taoensso.timbre :as timbre :refer :all])
  (:gen-class))

(def cli-options
  ;; An option with a required argument
  [["-c" "--config CONFIG_FILE" "Config file" :default "./kirke.edn"]
   ["-v" "--verbose" "Verbose" :id :verbosity]
   ["-h" "--help" "Print help message"]])

(defn -main [& args]
  (let [{:keys [options errors summary]} (cli/parse-opts args cli-options)]
    (cond
      (:help options)
      (do
        (timbre/log summary)
        (System/exit 0))
      errors
      (do
        (timbre/error errors)
        (System/exit -1))
      :else
      (try
        (ex/start (slurp (:config options)))
        (catch Exception e (timbre/error (.getMessage e)))))))
