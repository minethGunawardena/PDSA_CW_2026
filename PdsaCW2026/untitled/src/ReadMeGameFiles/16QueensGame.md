## Overview
This module is part of a Java Swing-based Algorithm Game System developed for the PDSA coursework.  
The Sixteen Queens Puzzle game extends the classic N-Queens problem, where 16 queens must be placed on a 16×16 chessboard such that no two queens threaten each other.

The system allows interactive gameplay where users place queens manually and submit solutions. It also demonstrates algorithmic solving using both sequential and multithreaded approaches, while tracking performance and storing results in a database.

---

## Game Objective
The main objective of this module is to:
- Find all valid configurations of 16 queens on a 16×16 board
- Ensure no two queens share the same row, column, or diagonal
- Allow players to discover and submit valid solutions
- Compare algorithm performance between sequential and threaded execution

---

## Sequential Algorithm
The sequential solution uses a recursive backtracking approach.

### Working Principle:
- The algorithm starts from the first row
- It attempts to place a queen in each column
- Each placement is validated against previously placed queens
- If valid, the algorithm proceeds to the next row
- If invalid, it backtracks and tries a different position
- Every complete valid arrangement is counted as a solution

### Characteristics:
- Single-threaded execution
- High correctness and simplicity
- Slower for large board sizes due to exhaustive search

---

## Threaded Algorithm
The threaded solution improves performance by dividing the computation across multiple threads.

### Working Principle:
- The board is divided into independent starting positions
- Each thread executes backtracking from a different starting state
- Threads run concurrently on multiple CPU cores
- Partial results are merged to produce the final solution count

### Characteristics:
- Multi-threaded execution
- Faster performance on multi-core systems
- Efficient utilization of system resources

---

## Game Interaction
The player interface allows:
- Clicking on a 16×16 grid to place queens
- Submitting a full configuration as a solution
- Validation of correctness in real-time

If a solution is valid:
- It is checked against previously found solutions
- Duplicate solutions are rejected
- Unique solutions are stored in the database

---

## Database Integration
The system stores:
- Game rounds (SixteenQueens sessions)
- Execution time of both algorithms
- Player submissions
- Unique valid solutions

This ensures persistence and allows performance comparison over time.

---

## Performance Analysis
At the start of each game round:
- Both sequential and threaded algorithms are executed
- Execution time is recorded in nanoseconds
- Results are compared and displayed in the performance chart system

The system highlights:
- Faster algorithm for each run
- Average, minimum, and maximum execution times
- Overall performance comparison

---

## Summary
This module demonstrates:
- Backtracking algorithm design
- Multithreading and parallel computation
- Real-time user interaction
- Database-driven persistence
- Performance analysis and visualization