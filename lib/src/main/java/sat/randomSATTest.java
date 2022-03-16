package randomsat;

//import statements
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.lang.*;
import java.util.*;

public class randomSATTest {

    //this code takes in the whole filepath for the cnf file
    public static void main(String[] args){
        //start timing
        long startTime = System. currentTimeMillis();

        try {
            //read file
            //todo: CHANGE THE FILEPATH BEFORE TESTING
            File cnffile = new File("C:\\Users\\weean\\Desktop\\test.cnf");
            FileReader fr = new FileReader(cnffile);
            BufferedReader br = new BufferedReader(fr);
            int NUM_LIT = 0;
            int NUM_CLAUSES = 0;
            int NUM_ITER = 10000;     //change this depending on how many times the code should try before exiting with a false

            String line;
            //initializing by looking for problem line
            while ((line = br.readLine()) != null) {
                //checking if comment and if there is information in that line
                if(line.length()>0 && line.charAt(0)!='c'){
                    //look for problem line
                    if(line.charAt(0)=='p'){
                        String[] information = line.split(" ");
                        NUM_LIT = Integer.parseInt(information[2]);
                        NUM_CLAUSES = Integer.parseInt(information[3]);
                        break;
                    }
                }
            }

            //initing arrays
            Boolean[] litArray = new Boolean[NUM_LIT];
            Boolean[][] negateArray = new Boolean[NUM_CLAUSES][2];
            Integer[][] clauseArray = new Integer[NUM_CLAUSES][2];      //2 because 2SAT

            //set litArray to false
            Arrays.fill(litArray, false);
            for(int i=0; i<NUM_CLAUSES; i++) {
                Arrays.fill(negateArray[i], false);
            }

            //setting up negateArray and clauseArray
            int linecounter=0;
            while ((line = br.readLine()) != null) {
                if(line.length()>0 && line.charAt(0)!='c') {
                    //check if line is a problem line
                    if (line.charAt(0) != 'p') {
                        String[] clauses = line.split(" ");

                        for (int i=0; i<2; i++) {
                            Integer clauseNumber = Integer.parseInt(clauses[i]);
                            System.out.println(clauseNumber);
                            clauseArray[linecounter][i] = Math.abs(clauseNumber);
                            if (clauseNumber < 0) {
                                negateArray[linecounter][i] = true;
                                System.out.println("NEGATIVE");
                            }

                        }
                        linecounter++;
                    }
                }
            }

            //todo: run main solver
            boolean result = randomSATSolver.mainSolver(litArray, negateArray, clauseArray, NUM_CLAUSES, NUM_ITER);
            if(result){
                //test printing
                System.out.println(Arrays.toString(litArray));
                System.out.println(Arrays.deepToString(negateArray));
                System.out.println(Arrays.deepToString(clauseArray));
                System.out.println("SATISFIABLE");
            } else {
                System.out.println("UNSATISFIABLE");
            }

            //end timing and output to system.out.println
            long stopTime = System.currentTimeMillis();
            System.out.println(stopTime - startTime);
            //todo: obtain truth assignment as output to system.out.println
        } catch(IOException e1){
            e1.printStackTrace();
        }
    }
}