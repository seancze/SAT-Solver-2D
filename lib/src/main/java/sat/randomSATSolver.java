package randomsat;

//import statements
import java.util.Arrays;

public class randomSATSolver {

    //coin flipper
    static int coinFlip(){
        //using system time
        long currentTime = System. currentTimeMillis();
        return (int)(currentTime%2);

        /* using random method (1ms slower per call)
        Random rand = new Random(); //instance of random class
        return rand.nextInt(2);
         */
    }

    //clause solver (or a very elaborate OR)
    static boolean clauseSolver(Boolean[] litArray, Boolean[][] negateArray, Integer[][] clauseArray, Integer clauseNumber){
        Integer lit1address = clauseArray[clauseNumber][0];
        Integer lit2address = clauseArray[clauseNumber][1];
        Boolean lit1bool = litArray[lit1address-1];
        if(negateArray[clauseNumber][0]){
            lit1bool = !lit1bool;
        }
        Boolean lit2bool = litArray[lit2address];
        if(negateArray[clauseNumber][1]){
            lit2bool = !lit2bool;
        }
        return lit1bool|lit2bool;
    }

    //todo: troubleshoot this cus result is always true for some reason?
    //formula solver, returns index of one random unsat clause if not satisfiable and NUM_CLAUSES if satisfiable
    static Integer formulaSolver(Boolean[] litArray, Boolean[][] negateArray, Integer[][] clauseArray,  Integer NUM_CLAUSES){
        boolean result = true;      //flag for satisfiability
        Integer[] unsatClauses = new Integer[2];
        Integer numUnsat = 0;
        for(int i=0; i<NUM_CLAUSES; i++){
            boolean output = clauseSolver(litArray, negateArray, clauseArray, i);
//            if(output){
//                continue;
//            } else if (numUnsat==0){
//                result = false;
//                unsatClauses[numUnsat] = i;
//                numUnsat=1;
//            } else if (numUnsat==1){
//                unsatClauses[numUnsat] = i;
//                numUnsat = 2;
//            } else if (numUnsat ==2){
//                return unsatClauses[coinFlip()];
//            }
            if(!output) {
                result = false;
                return i;
            }
        }
        return NUM_CLAUSES;
    }

    //todo: main solver - issue could be here too
    static boolean mainSolver(Boolean[] litArray, Boolean[][] negateArray, Integer[][] clauseArray, Integer NUM_CLAUSES, Integer NUM_ITER){
        for(int i=0; i<NUM_ITER; i++){
            Integer output = formulaSolver(litArray, negateArray, clauseArray, NUM_CLAUSES);
            if(output == NUM_CLAUSES){
                //test printing
                System.out.println(Arrays.toString(litArray));
                System.out.println(Arrays.deepToString(negateArray));
                System.out.println(Arrays.deepToString(clauseArray));
                return true;
            } else {
                Integer coin = coinFlip();
                litArray[clauseArray[output][coin]-1] = !litArray[clauseArray[output][coin]-1];
                //test printing
                System.out.println(Arrays.toString(litArray));
                System.out.println(Arrays.deepToString(negateArray));
                System.out.println(Arrays.deepToString(clauseArray));
            }
        }
        return false;
    }
    //start a loop to exit after a certain number of iterations

    //run formula solver

    //if true is obtained, exit loop and return truth assignment
    //else coin flip for the first unsat clause and change literal value

}
