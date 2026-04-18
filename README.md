# Algorithm Game System - Coursework Project

## 1. Project Overview

This project is an interactive Algorithm Game System developed as part of an academic coursework assignment for the BSc (Hons) Computing program. The system is designed to demonstrate the application of fundamental and advanced data structures and algorithms through a set of interactive game-based problems.

The application is implemented using Java with Swing for the graphical user interface and MySQL for persistent data storage. Each game module focuses on a different algorithmic problem, allowing performance comparison, complexity analysis, and user interaction.

---

## 2. Purpose of the System

The main objectives of this project are:

- To understand and implement core algorithmic techniques
- To compare different algorithmic approaches for the same problem
- To measure and analyze time complexity in practical scenarios
- To apply data structures effectively in problem-solving
- To integrate database systems for persistent storage and analysis
- To develop a modular, interactive desktop application

---

## 3. System Architecture

The system follows a modular architecture consisting of:

- User Interface Layer (Java Swing)
- Algorithm Layer (Problem-solving logic)
- Database Layer (MySQL integration)
- Utility Layer (support functions such as data generation and helpers)

Each game operates independently but shares a common framework for:
- Game rounds
- Timing execution
- Database logging
- User interaction

---

## 4. Implemented Game Modules

### 4.1 Snake and Ladder Problem

This module models the classic Snake and Ladder board game as a graph traversal problem.

#### Algorithmic Approach:
- Breadth First Search (BFS) for shortest path calculation
- Dijkstra’s Algorithm for performance comparison

#### Functionality:
- Randomly generated snakes and ladders for each round
- Calculation of minimum dice throws from start to finish
- Multiple-choice user interaction system
- Performance timing comparison between algorithms
- Storage of results and player responses in database

---

### 4.2 Minimum Cost Assignment Problem

This module solves the assignment problem where tasks are assigned to employees with minimum total cost.

#### Algorithmic Approach:
- Greedy Algorithm for heuristic solution
- Hungarian Algorithm for optimal solution

#### Functionality:
- Random generation of cost matrix (50–100 tasks/employees)
- Calculation of optimal and approximate solutions
- Execution time comparison between algorithms
- Multiple-choice user interface for player input
- Database storage of results and player performance

---

### 4.3 Traffic Simulation Problem

This module models a traffic network as a directed graph with capacity constraints.

#### Algorithmic Approach:
- Maximum Flow using appropriate flow algorithms (e.g., Edmonds-Karp / Ford-Fulkerson)

#### Functionality:
- Random capacity assignment for road segments
- Calculation of maximum flow from source to sink
- Graph-based representation of traffic system
- User interaction for solution prediction
- Performance measurement and database logging

---

### 4.4 Knight’s Tour Problem

This module solves the Knight’s Tour problem on a chessboard.

#### Algorithmic Approach:
- Recursive backtracking approach
- Iterative or optimized heuristic-based approach

#### Functionality:
- Random starting position for the knight
- Exploration of full board traversal path
- Validation of complete tour solution
- Performance comparison between two approaches
- Storage of valid solutions and execution time

---

### 4.5 Sixteen Queens Problem

This module extends the classic N-Queens problem to a 16x16 chessboard.

#### Algorithmic Approach:
- Sequential backtracking solution
- Multithreaded parallel solution for performance improvement

#### Functionality:
- Generation of all valid queen placements
- Comparison between sequential and threaded execution
- Storage of solutions in database
- Duplicate solution detection and validation
- Performance analysis and comparison

---

## 5. Database Integration

The system uses MySQL for persistent data storage. The database records:

- Game rounds and game types
- Algorithm execution times
- Correct solutions for each round
- Player names and responses
- Answer correctness status

This allows analysis of:
- Algorithm efficiency
- Player performance
- Comparative study of different approaches

---

## 6. Key Concepts Demonstrated

- Graph theory and traversal algorithms
- Dynamic programming and optimization techniques
- Backtracking and recursion
- Greedy and heuristic approaches
- Divide and conquer and multithreading
- Time complexity analysis
- Database design and integration
- GUI-based application development

---

## 7. Technologies Used

- Java (Core programming language)
- Java Swing (User Interface)
- MySQL (Database management system)
- JDBC (Database connectivity)

---

## 8. Conclusion

This project demonstrates the practical implementation of multiple algorithmic techniques through interactive game-based modules. Each module focuses on a specific computational problem, allowing comparison between different algorithmic strategies in terms of efficiency, correctness, and execution time.

The system successfully integrates algorithms, data structures, user interaction, and database management into a unified application suitable for academic evaluation.