(ns tic-tac-toe.helpers)

(defn toggle-turn
  "Flip the input between X and O after each turn"
  [current-turn]
  (if (= current-turn "X") "O" "X"))

(def win-patterns
  [[0 1 2] [3 4 5] [6 7 8] ;; rows
   [0 3 6] [1 4 7] [2 5 8] ;; columns
   [0 4 8] [2 4 6]])       ;; diagonal 

(def test-board-x-wins [nil "X" "O"
                        "O" "X" nil
                        nil "X" nil])
(def test-board-draw ["X" "X" "O"
                      "O" "O" "X"
                      "X" "O" "O"])

(defn create-new-board []
  (vec (repeat 9 nil)))

(defn all-same? [board pattern turn]
  (every? #(= turn (board %)) pattern))

(defn has-won? [board turn]
  (some #(all-same? board % turn) win-patterns))

(defn is-draw? [board]
  (every? #(not (nil? %)) board))

(defn is-empty? [board position]
  (nil? (board position)))

(defn is-occupied? [board position]
  (not (is-empty? board position)))
