(ns motto.lib.xls
  (:require [incanter.core :as ic]
            [incanter.excel :as ixls]
            [motto.lib.tab :as tab]))

(defn load-data [filename]
  (let [dset (ixls/read-xls filename)]
    (tab/dset->tab dset)))
