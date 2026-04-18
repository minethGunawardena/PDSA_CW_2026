# Minimum Cost Assignment Game - Algorithm Design Documentation

## 1. Overview

The Minimum Cost Assignment Game is based on the classical Assignment Problem in combinatorial optimization. The objective is to assign N tasks to N employees such that the total assignment cost is minimized.

Each assignment has a randomly generated cost, and the system evaluates two different algorithmic approaches to compute the solution and compare their performance.

---

## 2. Problem Representation

The problem is modeled using a cost matrix:

- Rows represent tasks
- Columns represent employees
- Each cell `cost[i][j]` represents the cost of assigning task i to employee j

The system generates a random cost matrix for each game round, where:
- N ranges from 50 to 100
- Each cost value ranges from 20 to 200

---

## 3. System Components

### 3.1 Cost Matrix Generator

The cost matrix generator is responsible for creating a random NxN matrix for each game round.

Responsibilities:
- Generate random assignment costs
- Ensure values are within a defined range (20–200)
- Provide input for both algorithms

This component ensures that each game round has a unique problem instance.

---

### 3.2 Greedy Algorithm

Purpose:
Provides a fast heuristic solution for the assignment problem.

Working Principle:
- Iterates through each task
- Selects the cheapest available employee for that task
- Marks employee as assigned
- Repeats until all tasks are assigned

Characteristics:
- Fast execution
- Does not guarantee optimal solution
- Used as a baseline comparison

---

### 3.3 Hungarian Algorithm

Purpose:
Computes the optimal solution for the assignment problem.

Working Principle:
- Performs row reduction by subtracting minimum values
- Performs column reduction
- Uses matrix transformation to optimize zero assignments
- Derives optimal assignment from reduced matrix

Characteristics:
- Guarantees optimal solution
- Higher computational complexity
- Used as the benchmark for correctness

---

## 4. Game Flow

1. A new game round is initiated
2. A random cost matrix (N × N) is generated
3. Greedy algorithm computes an approximate solution
4. Hungarian algorithm computes the optimal solution
5. Execution time for both algorithms is recorded in the database
6. The system generates multiple-choice answers based on computed results
7. The player selects an answer
8. The system verifies correctness
9. Player response is stored in the database
10. A new round can begin automatically

---

## 5. Database Integration

The system records the following data:

- Game round information (game type, round ID)
- Cost matrix execution results
- Algorithm execution time (Greedy and Hungarian)
- Correct optimal solution
- Player responses and correctness status

This enables performance comparison and user tracking across multiple rounds.

---

## 6. Key Concepts Demonstrated

- Assignment problem modeling using cost matrices
- Greedy heuristic algorithm
- Hungarian algorithm for optimal solution
- Time complexity comparison between algorithms
- Randomized problem generation
- GUI-based interactive decision system
- Database persistence for analytics

---

## 7. Summary

The Minimum Cost Assignment Game demonstrates a real-world optimization problem solved using both heuristic and optimal approaches. The system highlights the trade-off between computational efficiency and solution optimality while providing interactive gameplay and performance tracking.