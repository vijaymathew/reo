(ns motto.expr-io
  (:require [clojure.string :as str]
            [motto.type :as tp]))

(defn writeln [x]
  (let [v  (cond
             (boolean? x) (if x 't 'f)
             (or (tp/function? x)
                 (fn? x)) '<fn>
             :else x)]
    (println v)))

(defn- match-curlies [s]
  (loop [ss (seq s), opn 0, cls 0]
    (if (seq ss)
      (let [c (first ss)
            [opn cls]
            (cond
              (= \{ c) [(inc opn) cls]
              (= \} c) [opn (inc cls)]
              :else [opn cls])]
        (recur (rest ss) opn cls))
      (- opn cls))))

(defn readln []
  (if-let [s (read-line)]
    (if (str/ends-with? s " ")
      [:more s]
      (let [c (match-curlies s)]
        (cond
          (<= c 0) [:done s]
          (pos? c) [:more s])))
    [:eof nil]))

(defn read-multiln
  ([stepper]
   (loop [[flag s] (readln)
          ss []]
     (case flag
       :more (do (when stepper (stepper))
                 (recur (readln)
                        (conj ss s)))
       :done (str/join (conj ss s))
       :eof nil)))
  ([] (read-multiln nil)))
