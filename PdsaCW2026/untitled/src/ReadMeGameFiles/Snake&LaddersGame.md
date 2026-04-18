# Snake and Ladder Game - Algorithm Design Documentation

## 1. Overview

The Snake and Ladder game is implemented as a graph-based shortest path problem. Each cell on the board represents a node in a graph, and each dice roll represents possible transitions between nodes. Snakes and ladders introduce additional edges that modify the normal traversal path.

The objective of the game is to determine the minimum number of dice throws required to reach the final cell starting from cell 1.

---

## 2. System Components

### 2.1 Board Generator

The board generator creates a dynamic NxN grid where the value of N ranges between 6 and 12 for each game round.

Responsibilities:
- Generates a board of size N × N
- Randomly assigns:
    - N - 2 snakes
    - N - 2 ladders
- Ensures each snake connects a higher cell to a lower cell
- Ensures each ladder connects a lower cell to a higher cell

This creates a dynamic directed graph for each game round.

---

### 2.2 Graph Representation

- Each cell is treated as a node
- Dice rolls (1–6) define edges between nodes
- Snakes and ladders act as teleport edges
    - Snake: moves player to a lower node
    - Ladder: moves player to a higher node

---

## 3. Algorithms Used

### 3.1 Breadth First Search (BFS)

Purpose:
To compute the minimum number of dice throws required to reach the final cell.

Working Principle:
- Uses a queue data structure
- Explores all possible moves level by level
- Each move represents one dice throw
- Applies snake or ladder transitions immediately
- Stops when the final node is reached

Justification:
BFS guarantees the shortest path in an unweighted graph, making it suitable for this problem.

---

### 3.2 Dijkstra's Algorithm

Purpose:
To compute the shortest path using a weighted graph approach for comparison purposes.

Working Principle:
- Uses a priority queue
- Maintains the minimum distance to each node
- Each dice throw is treated as a cost of 1
- Updates shortest paths dynamically

Justification:
Although BFS is more efficient for this problem, Dijkstra is implemented to compare performance and demonstrate alternative approaches.

---

## 4. Game Flow

1. The user starts a new game round
2. A random board is generated with snakes and ladders
3. BFS algorithm calculates the correct minimum number of dice throws
4. Dijkstra algorithm runs for performance comparison
5. Execution time of both algorithms is recorded in the database
6. Three multiple-choice options are generated for the player
7. The player selects an answer
8. The system checks correctness
9. Player responses and results are stored in the database
10. A new round can begin automatically

---

## 5. Database Integration

The system stores the following information:

- Game round details (game type, round ID)
- Algorithm execution times (BFS and Dijkstra)
- Correct solution for each round
- Player name and selected answers
- Whether the answer is correct or incorrect

This allows performance analysis and user tracking.

---

## 6. Key Concepts Demonstrated

- Graph representation of board games
- Breadth First Search for shortest path computation
- Dijkstra’s algorithm for comparative analysis
- Randomized problem generation
- Real-time performance measurement
- User interaction through GUI
- Database integration for persistent storage

---

## 7. Summary

This implementation transforms the classic Snake and Ladder game into a graph theory problem. It demonstrates the application of fundamental algorithms in a dynamic, interactive environment while also enabling performance comparison and data logging for academic analysis.