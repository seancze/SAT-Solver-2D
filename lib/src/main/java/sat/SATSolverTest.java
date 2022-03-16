package sat;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

import sat.env.Bool;
import sat.env.Environment;
import sat.env.Variable;
import sat.formula.Clause;
import sat.formula.Formula;
import sat.formula.Literal;
import sat.formula.NegLiteral;
import sat.formula.PosLiteral;

public class SATSolverTest {
    private static int NUM_VARS;

    public static void main(String[] args) {

        Formula formula = new Formula();
        Clause c = new Clause();

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
                        NUM_VARS = Integer.parseInt(strArray[2]);
                    } else {
                        String[] clauseArray = line.split(" ");
                        for (String el : clauseArray) {
                            Integer elementAsInt = Integer.parseInt(el);
                            // End of clause - Append to formula
                            if(elementAsInt == 0) {
                                formula = formula.addClause(c);
                                c = new Clause();
                            } else {
                                if (elementAsInt > 0) {
                                    c = c.add(PosLiteral.make(el));
                                } else {
                                    c = c.add(NegLiteral.make(el.substring(1)));
                                }
                            }
                            // Handle scenario where c is unsatisfiable; empty clause
                            if (c == null) {
                                c = new Clause();
                            }
                        }
                    }
                    sb.append(line);      //appends line to string buffer
                    sb.append("\n");     //line feed
                }
            }
            fr.close();    //closes the stream and release the resources

            // For any remaining non-empty clauses
            if (c.size() > 0) {
                formula = formula.addClause(c);
            }
        } catch(IOException e) {
            e.printStackTrace();
        }

        System.out.println("SAT solver starts!!!");
        long started = System.nanoTime();
        Environment env = SATSolver.solve(formula);
        long time = System.nanoTime();
        long timeTaken = time - started;
        System.out.println("Time:" + timeTaken/1000000.0 + "ms");
        //System.out.println(env);        //delete this if viewing results only through text file.

        Variable emptyVar = new Variable(new Clause().toString());
        if(env.get(emptyVar) == Bool.FALSE)  {
            System.out.println("not satisfiable");
            return;
        }
        System.out.println("satisfiable");
        //result file creation - creation only happens if the file does not exist
        File result = new File(System.getProperty("user.dir") + "BoolAssignment.txt");

        try {
            //if (result.createNewFile()) {     //this is to prevent overwrite
            FileWriter output = new FileWriter(System.getProperty("user.dir")+"\\BoolAssignment.txt");

            //get variable-assignment pairs
            for(int i=1; i<NUM_VARS+1; i++){
                String key = String.valueOf(i);
                Literal l = PosLiteral.make(key);
                Bool value = env.get(l.getVariable());

                if (value != Bool.UNDEFINED) {
                    output.write(key + ":" + value);
                } else {
                    // If UNDEFINED, it means the value can be either TRUE or FALSE
                    // By default, set it to TRUE
                    output.write(key + ":TRUE");
                }
                output.append('\n');
            }
            output.close();
            //System.out.println("Write success!");
        } catch (IOException e) {
            //System.out.println("An error occurred.");
            e.printStackTrace();
        }
    }
}