package sat;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;

public class TwoSATSolverTest {
    public static void main (String[] args) {

        HashMap<Integer, HashSet<Integer>> graph = new HashMap<>();

        try
        {
            File file=new File(args[0]);    //creates a new file instance
            FileReader fr=new FileReader(file);   //reads the file
            BufferedReader br=new BufferedReader(fr);  //creates a buffering character input stream
            StringBuffer sb=new StringBuffer();    //constructs a string buffer with no characters
            String line;


            while(((line=br.readLine())!=null))
            {
                if (line.length() > 0 && !((line.charAt(0) == 'c'))) {
                    // Breaks design pattern of static but used just to store NUM_VARS and NUM_CLAUSES
                    if (line.charAt(0) == 'p') {
                        String[] strArray = line.split(" ");
                    } else {
                        String[] clauseArray = line.split(" ");

                        // In 2-SAT, we are assuming that there must strictly be 2 clauses in each expression
                        final Integer first = Integer.parseInt(clauseArray[0]);
                        final Integer second = Integer.parseInt(clauseArray[1]);
                        final Integer negatedFirst = first * -1;
                        final Integer negatedSecond = second * -1;
                        // Construct graph in implicative normal form
                        // Source: https://cp-algorithms.com/graph/2SAT.html
                        // Set first to false => second must be true
                        HashSet<Integer> negatedFirstNodes = graph.get(negatedFirst);
                        if (negatedFirstNodes == null)
                            negatedFirstNodes = new HashSet<>();
                        negatedFirstNodes.add(second);
                        graph.put(negatedFirst, negatedFirstNodes);
                        // Set second to false => first must be true
                        HashSet<Integer> negatedSecondNodes = graph.get(negatedSecond);
                        if (negatedSecondNodes == null)
                            negatedSecondNodes = new HashSet<>();
                        negatedSecondNodes.add(first);
                        graph.put(negatedSecond, negatedSecondNodes);
                    }
                    sb.append(line);      //appends line to string buffer
                    sb.append("\n");     //line feed
                }
            }
            fr.close();    //closes the stream and release the resources

        } catch(IOException e) {
            e.printStackTrace();
        }

        System.out.println("2-SAT solver starts!!!");
        long started = System.nanoTime();
        HashMap<Integer, Boolean> result = TwoSATSolver.solveDFS(graph);
        long time = System.nanoTime();
        long timeTaken = time - started;
        if(result == null) {
            System.out.println("UNSATISFIABLE");
        } else {
            System.out.println("SATISFIABLE");
            System.out.println("Result: " + result);
        }
        System.out.println("Time Taken: " + timeTaken/1000000.0 + "ms");
    }

}
