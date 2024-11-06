(ns tic-tac-toe.rules)

(def turn (atom "X"))

(defn toggle-turn
  "Flip the input between X and O after each turn"
  []
  (swap! turn #(if (= % "X") "O" "X")))

(def win-patterns
  [[0 1 2]  ;; rows
   [3 4 5]
   [6 7 8]
   [0 3 6]  ;; columns
   [1 4 7]
   [2 5 8]
   [0 4 8]  ;; diagonal
   [2 4 6]])

(def test-board-x-wins [nil "X" "O"
                        "O" "X" nil
                        nil "X" nil])
(def test-board-draw ["X" "X" "O"
                      "O" "O" "X"
                      "X" "O" "O"])

(defn all-same? [board pattern turn]
  (every? #(= turn (board %)) pattern))

(defn has-won? [board turn]
  (some #(all-same? board % turn) win-patterns))

(defn is-draw? [board]
  (every? #(not (nil? %)) board))

(defn is-occupied? [board position]
  (not (nil? (board position))))
