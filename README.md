# SAT Solver
Term 4 2D Project (January 2022)

## Authors
- Sean @Seancze
- Chun Hui @hithfaernith
- Jit @asdfash
- Min Khant @MinKhant9747
- Umang @Usgupta

## Contents
The following code can be found within `lib/src/main/java/sat`
1. Code for SAT Solver (Recursion)
2. Code for 2-SAT Solver (DFS via Kosaraju's algorithm)
3. Code for 2-SAT Solver (Randomised algorithm)
4. Code to parse cnf file

## SAT Solver (Recursion)
The following outlines how the recursive algorithm was implemented.
1. Parse cnf file and dump contents into an Immutable List of clauses (`ImList`)
2. If there are no clauses, the formula is trivially satisfiable. Exit the function by returning the environment.
3. Otherwise, find the smallest clause from the list of clauses
    1. If the clause is empty, the list of clauses is unsatisfiable. Bind an empty clause to FALSE and backtrack by returning the environment.
    2. If the clause has a single literal, bind the literal to the environment such that the clause is satisfiable
    3. Otherwise, pick an arbitrary literal from this small clause
        1. Set the literal to TRUE
        2. Substitute this literal to TRUE in all other clauses (using the `substitute()` method)
        3. Solve recursively. 
        4. If it fails, try setting the literal to FALSE. Then, repeat steps 1 to 3.

## 2-SAT Solver (DFS via Kosaraju's algorithm)
The following outlines how the DFS algorithm was implemented.
1. Parse cnf file and dump contents into a dictionary such that an implication graph is created
2. Using DFS on this graph, store the vertices into a sorted stack whereby the topmost element contains the vertex with the highest finishing time
3. Get the transposed of the graph
4. For each element of the stack, conduct DFS on the transposed graph to identify all strongly connected components (SCCs) of the implication graph
    - SCCs are stored in a two-dimensional (2D) array
    - Store all elements in a reversed stack too
5. For each SCC,
    - Check that the SCC does not contain both a literal and its negated form. If it does, the solution is unsatisfiable. Return early.
    - Otherwise, create a condensed graph by storing the first element within each SCC as the key and the rest of the elements as the value
6. Pick the topmost element of the reversed stack until the stack is empty
    1. Ceate a dictionary (called `result`) that stores the element as the key, and its negated boolean expression (e.g. If the element is '1', then assign it to `false`. If the element is '-1', assign it to `true`) as the value.
    2. Do the same for nodes adjacent to the element.
    3. Note: Only store the element and the nodes adjacent to it within the dictionary if it cannot be found within the dictionary
7. Return `result`
