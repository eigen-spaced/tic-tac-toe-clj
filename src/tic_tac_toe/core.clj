(ns tic-tac-toe.core
  (:require [clojure.string :as str])
  (:require [tic-tac-toe.helpers :refer [create-new-board
                                         toggle-turn
                                         has-won?
                                         is-draw?
                                         is-occupied?]])
  (:require [tic-tac-toe.ai :refer [choose-move]])
  (:gen-class))

(def test-board-x-can-win [nil "X" "O"
                           "O" "X" nil
                           nil nil nil])

(def starting-turn (atom "X"))

(defn format-board [board]
  (->> board
       (map #(if (= % nil) " " %))
       (partition 3)
       (map #(str/join " | " %))
       (str/join "\n") ;; Format each row as a string
       (str "\n")))

(defn update-board [board pos turn]
  (assoc board pos turn))

(defn get-user-input []
  (println "Player turn")
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

(defn player-turn [board turn]
  (let [position (get-user-input)]
    (if (is-occupied? board position)
      (do
        (println "Position is already occupied")
        (recur board turn))
      (update-board board position turn))))

(defn ai-turn [board turn]
  (println "AI turn")
  (let [position (choose-move board turn)]
    (update-board board position turn)))

(defn game-status [board turn]
  (cond
    (has-won? board turn) (str turn " is the winner")
    (is-draw? board) "Match is a draw"
    :else nil))

(defn game-loop [board starting-mark]
  (let [player-mark starting-mark
        ai-mark (if (= "X" player-mark) "O" "X")]
    (loop [current-board board
           current-mark player-mark]
      (println (format-board current-board))
      (let [updated-board (if (= current-mark player-mark)
                            (player-turn current-board current-mark)
                            (ai-turn current-board ai-mark))
            next-mark (toggle-turn current-mark)] ;; swap turn after each move
        (if-let [status (game-status updated-board current-mark)]
          (println status)
          (recur updated-board next-mark))))))

(defn -main
  [& args]
  (println "Xs and Os, built with Clojure!")
  (loop
    ;; Ensure "X" always starts each game
   (when (= @starting-turn "O")
      ;; Toggle to "X" if "O" would start
     (swap! starting-turn toggle-turn))
    (println "Starting new game")
    (let [new-board (create-new-board)]
      (game-loop new-board @starting-turn))
    ;; Toggle starting turn for the next game
    (swap! starting-turn toggle-turn)
    (recur)))
