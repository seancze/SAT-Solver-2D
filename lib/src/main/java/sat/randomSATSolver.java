package sat;

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

    //clause solver (aka a very elaborate OR)
    static boolean clauseSolver(boolean[] litArray, boolean[][] negateArray, Integer[][] clauseArray, Integer clauseNumber){
        if(litArray[clauseArray[clauseNumber][0]-1]){
            negateArray[clauseNumber][0] = !negateArray[clauseNumber][0];
        }
        if(litArray[clauseArray[clauseNumber][1]-1]){
            negateArray[clauseNumber][1] = !negateArray[clauseNumber][1];
        }
        return negateArray[clauseNumber][0]|negateArray[clauseNumber][1];
    }

    //formula solver, returns index of one random unsat clause if not satisfiable and NUM_CLAUSES if satisfiable
    static Integer formulaSolver(boolean[] litArray, boolean[][] negateArray, Integer[][] clauseArray,  Integer NUM_CLAUSES){
        for(int i=0; i<NUM_CLAUSES; i++){
            //make copy of negate
            boolean[][] negateCopy = new boolean[negateArray.length][negateArray[0].length];
            for (int j = 0; j < negateCopy.length; j++){
                negateCopy[j] = Arrays.copyOf(negateArray[j], negateArray[j].length);
            }
                    
            boolean output = clauseSolver(litArray, negateCopy, clauseArray, i);
            if(!output) {
                return i;
            }
        }
        return NUM_CLAUSES;
    }

    //main solver 
    static boolean mainSolver(boolean[] litArray, boolean[][] negateArray, Integer[][] clauseArray, Integer NUM_CLAUSES, Integer NUM_ITER){
        for(int i=0; i<NUM_ITER; i++){
            Integer output = formulaSolver(litArray, negateArray, clauseArray, NUM_CLAUSES);
            if(output == NUM_CLAUSES){
                for (int j=0; j<NUM_CLAUSES;j++){
                    if(litArray[clauseArray[j][0]-1]){
                        negateArray[j][0] = !negateArray[j][0];
                    }
                    if(litArray[clauseArray[j][1]-1]){
                        negateArray[j][1] = !negateArray[j][1];
                    }
                }
                return true;
            }
            Integer coin = coinFlip();
            
            litArray[clauseArray[output][coin]-1] = !litArray[clauseArray[output][coin]-1];
        }

        return false;
    }
    //start a loop to exit after a certain number of iterations

    //run formula solver

    //if true is obtained, exit loop and return truth assignment
    //else coin flip for the first unsat clause and change literal value

}
