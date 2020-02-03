(ns kirke.core
  (:require [kirke.configurer :as cfg]
            [clojure.tools.cli :refer [parse-opts]])
  (:gen-class))

(def cli-options
  ;; An option with a required argument
  [["-c" "--config CONFIG_FILE" "Config file"]
   ["-v" "--verbose" "Verbose" :id :verbosity]
   ["-h" "--help" "Print help message"]])

(defn -main [& args]
  (let [{:keys [options errors summary]} (parse-opts args cli-options)]
    (cond
      (:help options) (do (println summary) (System/exit 0))
      errors (do (println errors) (System/exit -1))
      :else (println (slurp (:config options))))))
