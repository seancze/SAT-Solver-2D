package sat;

import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;

public class TwoSATSolver {
    public static void fillOrder(Integer cur, HashSet<Integer> visited, HashMap<Integer, HashSet<Integer>> graph, LinkedList<Integer> stack) {

        // Mark current node as visited
        visited.add(cur);

        // Get neighbours
        HashSet<Integer> currentSet = graph.get(cur);

        // For each neighbour, if not visited, conduct DFS
        if(currentSet != null) {
            for (Integer num : graph.get(cur)) {
                if (!visited.contains(num))
                    fillOrder(num, visited, graph, stack);
            }
        }


        stack.add(cur);



    }

    public static HashMap<Integer, HashSet<Integer>> reverseGraph(HashMap<Integer, HashSet<Integer>> graph) {
        HashMap<Integer, HashSet<Integer>> result = new HashMap<Integer, HashSet<Integer>>();


        // For each key
        for (Integer key : graph.keySet()) {
            // For each neighbour of key
            for (Integer value : graph.get(key)) {
                HashSet<Integer> currentSet = result.get(value);
                if(currentSet == null)
                    currentSet = new HashSet<>();

                // Add key as value
                currentSet.add(key);
                result.put(value, currentSet);
            }

        }

        return result;

    }

    // Find strongly connected components given a reverse graph
    // Requires: A sorted stack where the node at the top of stack has the largest finishing time
    public static HashSet<Integer> findSCC(Integer cur, HashSet<Integer> visited, HashMap<Integer, HashSet<Integer>> graph, HashSet<Integer> out) {
        visited.add(cur);
        out.add(cur);

        HashSet<Integer> neighbours = graph.get(cur);

        if(neighbours != null) {
            for (Integer n : neighbours) {
                if (!visited.contains(n)) {
                    findSCC(n, visited, graph, out);
                }
            }
        }

        return out;
    }



    public static HashMap<Integer, Boolean> solveDFS(HashMap<Integer, HashSet<Integer>> graph) {
        LinkedList<Integer> stack = new LinkedList<>();
        HashSet<Integer> visited = new HashSet<Integer>();

        LinkedList<HashSet> scc = new LinkedList();

        // Store elements of stack in reverse topological order
        LinkedList<Integer> reversedStack = new LinkedList<>();
        HashMap<Integer, HashSet<Integer>> condensedGraph = new HashMap<>();

        HashMap<Integer, Boolean> result = new HashMap<>();

        // First DFS: Get finishing times of each node
        // Sorts nodes by finishing times in ascending order so that nodes in different SCCs all appear at the top of stack
        // Helps us to determine order to conduct DFS
        for (Integer key : graph.keySet()) {
            // Conduct DFS if not visited
            if(!visited.contains(key)) {
                fillOrder(key, visited, graph, stack);
            }

        }

        // Reset for second DFS
        visited.clear();

        // Reverse graph for second DFS
        // So that SCCs can be found
        // By definition, SCCs are components where there is a path between all vertexes (there is a cycle)
        // By reversing, cycles still can be found and are represented by a single SCC
        // Meanwhile, standalone nodes will become unreachable in a reversed graph helping us to identify them as an SCC
        HashMap<Integer, HashSet<Integer>> reverseGraph = reverseGraph(graph);

        // Second DFS: Find strongly connected components
        while(stack.size() > 0) {
            Integer topEl = stack.removeLast();
            HashSet<Integer> emptySet = new HashSet<>();
            if(!visited.contains(topEl))
                scc.add(findSCC(topEl, visited, reverseGraph, emptySet));

            reversedStack.add(topEl);
        }

        // Construct condensed graph using each SCC
        for (HashSet<Integer> currentSet : scc) {
            Integer key = null;
            HashSet<Integer> values = new HashSet<>();

            for (Integer el : currentSet) {
                // If SCC contains both non-negated and negated value of a literal, problem is unsatisfiable
                Integer negatedEl = el * -1;
                if(currentSet.contains(negatedEl)) {
                    return null;
                }

                // Otherwise, set first el as key, the rest as values
                if (key == null) {
                    key = el;
                } else {
                    values.add(el);
                }
            }

            condensedGraph.put(key, values);
        }

        // This algorithm for assigning of boolean expressions is explained in the same source listed above
        for (Integer el : reversedStack) {
            Integer absoluteValue = Math.abs(el);
            if(result.get(absoluteValue) == null) {
                // Set the literal to False (I.e. If '1', then False. If '-1', then True.)
                result.put(absoluteValue, el < 0);

                // Assign boolean to all its neighbours too
                HashSet<Integer> neighbours = condensedGraph.get(el);
                if(neighbours != null) {
                    for (Integer n : neighbours) {
                        Integer absoluteN = Math.abs(n);
                        if (result.get(absoluteN) == null) {
                            result.put(absoluteN, n < 0);
                        }
                    }
                }
            }

        }

        return result;

        }


}
