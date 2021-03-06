package sat;

//import statements

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.Arrays;

public class randomSATTest {

    //this code takes in the whole filepath for the cnf file
    public static void main(String[] args){
        //start timing
        long startTime = System. currentTimeMillis();

        try {
            //read file
            //todo: CHANGE THE FILEPATH BEFORE TESTING
            File cnffile = new File(args[0]);
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
            boolean[] litArray = new boolean[NUM_LIT];
            boolean[][] negateArray = new boolean[NUM_CLAUSES][2];
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
                            clauseArray[linecounter][i] = Math.abs(clauseNumber);
                            if (clauseNumber < 0) {
                                negateArray[linecounter][i] = true;
                            }

                        }
                        linecounter++;
                    }
                }
            }
            boolean result = randomSATSolver.mainSolver(litArray, negateArray, clauseArray, NUM_CLAUSES, NUM_ITER);
            if(result){
                StringBuilder ans = new StringBuilder();
                //test printing
                for (int i = 0; i < litArray.length; i++) {
                    boolean assignedBoolean = litArray[i];
                    if (assignedBoolean) {
                        ans.append("1");
                    } else {
                        ans.append("0");
                    }
                    // Add fullstop '.' if last boolean value
                    // Otherwise, add space instead
                    if (i == litArray.length-1) {
                        ans.append(".");
                    } else {
                        ans.append(" ");
                    }
                }

                System.out.println(ans);

                System.out.println("SATISFIABLE");
            } else {
                System.out.println("UNSATISFIABLE");
            }

            //end timing and output to system.out.println
            long stopTime = System.currentTimeMillis();
            System.out.println("Time:"+ (stopTime - startTime));
            br.close();
        } catch(IOException e1){
            e1.printStackTrace();
        }
    }
}
