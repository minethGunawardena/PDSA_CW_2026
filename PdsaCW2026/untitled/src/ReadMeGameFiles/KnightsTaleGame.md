# Knight’s Tour Problem – Game Module Documentation

## Overview
This module is part of a Java Swing-based Algorithm Game System developed for the PDSA coursework.  
The Knight’s Tour game visualizes and solves the classic chess problem where a knight must visit every square on an N×N chessboard exactly once.

The board size is dynamic (8×8 or 16×16), and each game round generates a random starting position. The system demonstrates algorithmic solving, performance measurement, database logging, and UI visualization with animation.

---

## Problem Definition
- A knight starts from a random position on an N×N chessboard.
- The knight must visit every cell exactly once.
- The solution must generate a valid sequence of moves covering all cells.
- Players can run or animate the solution and observe the traversal.

---

## Algorithms Used

### 1. Heuristic Algorithm (Warnsdorff-based approach)
**Purpose:**
Finds a valid Knight’s Tour path efficiently.

**How it works:**
- At each step, the knight moves to the square with the fewest onward moves.
- This reduces the chance of getting stuck.
- It is a greedy approach guided by accessibility.

**Role in the system:**
- Used as the primary solver for generating the full tour.
- Produces the board matrix containing move order (0 → N²-1).
- Ensures fast computation even for 16×16 boards.

**Complexity:**
- Average: O(N²)
- Efficient for both 8×8 and 16×16 grids

---

### 2. Backtracking (Conceptual / fallback reference)
**Purpose:**
A theoretical brute-force method for solving Knight’s Tour.

**How it works:**
- Tries all possible knight moves recursively.
- Backtracks when a dead-end is reached.
- Explores full search space.

**Role in system:**
- Used as an academic comparison reference.
- Not used in real-time gameplay due to high computation cost.

**Complexity:**
- O(8^(N²)) worst case (very slow)

---

## UI & Game Logic Flow

### Step 1: Game Initialization
- User selects board size (8×8 or 16×16).
- A random starting position is generated.
- A new game round is stored in the database.

---

### Step 2: Algorithm Execution
- Heuristic algorithm generates a full Knight’s Tour path.
- The result is stored in a 2D array where each cell represents move order.
- Execution time is recorded.

---

### Step 3: Visualization (Animation Mode)
- A Swing Timer is used to animate the knight movement.
- Each step highlights:
    - Current knight position (red cell with knight symbol)
    - Previously visited cells (colored trail)
- UI updates without freezing (non-blocking animation)

---

### Step 4: Player Interaction
- Player can:
    - Run the algorithm directly
    - Watch animation
- Player submits name and game result is stored

---

### Step 5: Database Logging
The system records:
- Player name
- Game round ID
- Result (success/failure)
- Execution time of algorithm
- Board configuration per round

---

## Data Structures Used

- `int[][] board`  
  Stores move order of knight traversal.

- `Timer (Swing)`  
  Controls animation steps safely without freezing UI.

- `Random`  
  Generates starting position dynamically.

---

## Validation & Exception Handling
- Empty player name validation
- Safe numeric handling for board selection
- Animation stop condition checks
- Exception handling for algorithm failures

---

## Performance Analysis

| Board Size | Algorithm Type | Performance |
|------------|---------------|-------------|
| 8×8        | Heuristic     | Very fast   |
| 16×16      | Heuristic     | Moderate    |
| 8×8        | Backtracking  | Slow        |
| 16×16      | Backtracking  | Extremely slow (not practical) |

---

## Comparison of Approaches

| Feature        | Heuristic (Used) | Backtracking (Reference) |
|----------------|------------------|----------------------------|
| Speed          | Fast             | Very slow                  |
| Scalability    | Good             | Poor                       |
| Implementation | Moderate         | Complex                    |
| Practical Use  | Yes              | No                         |

---

## Conclusion

The Knight’s Tour module demonstrates:
- Greedy heuristic optimization
- Real-time visualization using Swing
- Safe animation using Timer
- Database integration for tracking results
- Comparison between efficient and brute-force approaches

This module successfully fulfills algorithm design, visualization, and performance analysis requirements of the coursework.