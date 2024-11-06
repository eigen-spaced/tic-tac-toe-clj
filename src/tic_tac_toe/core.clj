(ns tic-tac-toe.core
  (:require [clojure.string :as str])
  (:require [tic-tac-toe.rules :refer [toggle-turn turn has-won? is-draw? is-occupied?]])
  (:gen-class))

(defn create-new-board []
  (vec (repeat 9 nil)))

(defn format-board [board]
  (->> board
       (map #(if (= % nil) " " %))
       (partition 3)
       (map #(str/join " | " %))
       (str/join "\n")))          ;; Format each row as a string

(defn update-board [board pos turn]
  (assoc board pos turn))

(defn get-user-input []
  (println (str @turn " turn"))
  (print "Enter a position (0-8):")
  (flush)
  (let [input (try (Integer/parseInt (read-line))
                   (catch NumberFormatException _
                     nil))]
    (if (and (number? input) (<= 0 input 8))
      input
      (do
        (println "Invalid move.")
        (recur)))))

(defn play-turn [board turn]
  (let [position (get-user-input)]
    (if (is-occupied? board position)
      (do
        (println "Position is already occupied")
        (recur board turn))
      (update-board board position turn))))

(defn -main
  [& args]
  (println "Xs and Os, built with Clojure!")
  (let [board (create-new-board)]
    (loop [current-board board]
      (println (format-board current-board))
      (let [updated-board (play-turn current-board @turn)]
        (cond
          (has-won? updated-board @turn) (println (str @turn " is the winner"))
          (is-draw? updated-board) (println "Match is a draw")
          :else (do
                  (toggle-turn)
                  (recur updated-board)))))))
