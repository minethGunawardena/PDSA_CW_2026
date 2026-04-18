# Traffic Simulation Problem

## 1. Overview

The Traffic Simulation Problem is an algorithm-based game module developed as part of an academic coursework system. It models a simplified road network as a directed graph where intersections are nodes and roads are edges with limited capacity (vehicles per minute).

The main objective of the game is to compute the maximum possible flow of traffic from a source node (A) to a sink node (T) using two different algorithmic approaches, and allow the user to predict the result.

---

## 2. Problem Model

The traffic network consists of the following nodes:

A (Source), B, C, D, E, F, G, H, T (Sink)

The directed edges are:

A → B, C, D  
B → E, F  
C → E, F  
D → F  
E → G, H  
F → H  
G → T  
H → T

Each edge is assigned a random capacity between 5 and 15 vehicles per minute at the start of each game round.

This creates a dynamic graph structure for every execution.

---

## 3. Algorithmic Approaches

### 3.1 Edmonds-Karp Algorithm (Breadth First Search)

This algorithm is an implementation of the Ford-Fulkerson method using Breadth First Search (BFS).

Functionality:
- Finds the shortest augmenting path from source (A) to sink (T)
- Uses BFS to explore the graph level by level
- Determines bottleneck capacity along the path
- Updates residual graph capacities
- Repeats until no augmenting path exists

Role in system:
- Provides a stable and optimal method for computing maximum flow
- Used as the reference solution for correctness comparison
- Execution time is recorded for performance analysis

---

### 3.2 Ford-Fulkerson Algorithm (Depth First Search)

This algorithm uses Depth First Search (DFS) to find augmenting paths.

Functionality:
- Searches for any available path from source to sink
- Uses DFS traversal to explore possible routes
- Determines bottleneck capacity in each recursive path
- Updates residual capacities after each flow push
- Repeats until no valid path exists

Role in system:
- Provides an alternative approach to solving the same problem
- Performance depends on path selection strategy
- Used for comparison with BFS-based solution

---

## 4. User Interaction Flow

The user does not directly solve the graph.

Instead, the system operates as follows:

1. A new traffic graph is generated with random capacities
2. The system displays the network structure in the UI
3. Both algorithms compute the maximum flow from A to T
4. Execution times for both algorithms are recorded
5. The correct maximum flow value is stored internally
6. The user is asked to predict the maximum flow value
7. The system checks the user’s answer
8. The result (correct or incorrect) is displayed

---

## 5. Input and Output Behavior

### User Input:
- Player name
- Predicted maximum flow value from A to T

### System Output:
- Display of traffic network graph
- Algorithm execution results (hidden correctness value)
- Correct or incorrect feedback
- Correct answer if user is wrong

---

## 6. Database Operations

The system stores the following data for each round:

- Game round ID
- Player name
- Player answer
- Correct answer (maximum flow)
- Result status (correct or incorrect)
- Execution time of Edmonds-Karp algorithm
- Execution time of Ford-Fulkerson algorithm

This enables performance comparison and analytical reporting.

---

## 7. Complexity Analysis

### Edmonds-Karp (BFS):
- Time complexity: O(V × E²)
- More predictable performance
- Preferred for correctness and stability

### Ford-Fulkerson (DFS):
- Time complexity: O(E × max_flow)
- Performance depends on path selection
- Can be less efficient in dense graphs

---

## 8. Key Concepts Demonstrated

- Graph theory modeling
- Network flow problem solving
- Breadth First Search (BFS)
- Depth First Search (DFS)
- Residual graph concept
- Algorithm performance comparison
- Randomized input generation
- Database integration
- User interface design
- Input validation and exception handling

---

## 9. Conclusion

The Traffic Simulation Problem demonstrates a real-world application of maximum flow algorithms using a dynamically generated network. By implementing both Edmonds-Karp (BFS) and Ford-Fulkerson (DFS) approaches, the system enables direct comparison of algorithm performance and behavior.

The user interaction layer transforms the algorithmic computation into a prediction-based game, while backend systems handle computation, timing analysis, and data persistence.