(ns tic-tac-toe.ai
  (:require [tic-tac-toe.helpers :refer [win-patterns toggle-turn is-empty? is-occupied?]]))

(def test-board-center-occupied ["X" nil nil
                                 "X" nil "X"
                                 nil "O" nil])
(def test-board-center-not-occupied ["X" nil nil
                                     nil nil nil
                                     nil nil nil])

(defn play-center-or-corners [board]
  (let [center-position 4
        corner-positions [0 2 6 8]
        available-corner-positions (filter #(is-empty? board %) corner-positions)]
    (cond (is-empty? board center-position) center-position
          (seq available-corner-positions) (rand-nth available-corner-positions)
          :else nil)))

(defn available-wins [turn board]
  (letfn [(two-positions-filled? [pattern]
            (= 2 (count (filter #(= turn (board %)) pattern))))
          (one-position-empty? [pattern]
            (= 1 (count (filter #(is-empty? board %) pattern))))]
    (filter #(and (two-positions-filled? %)
                  (one-position-empty? %))
            win-patterns)))

(defn defend-lose
  "Check if opponent has a winning position, if true defend the position"
  [board turn]
  (let [opponent-wins-available? (available-wins turn board)]
    (some #(when (is-empty? board %) %) (first opponent-wins-available?))))

(defn attempt-win [board turn]
  (some #(when (is-empty? board %) %) (first (available-wins turn board))))

(defn choose-move [board turn]
  (or (attempt-win board turn)
      (defend-lose board (toggle-turn turn))
      (play-center-or-corners board)
      (some #(when (is-empty? board %) %) (range (count board)))))

