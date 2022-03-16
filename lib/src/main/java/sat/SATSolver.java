package sat;

import immutable.EmptyImList;
import immutable.ImList;
import sat.env.Bool;
import sat.env.Environment;
import sat.env.Variable;
import sat.formula.Clause;
import sat.formula.Formula;
import sat.formula.Literal;
import sat.formula.NegLiteral;
import sat.formula.PosLiteral;

public class SATSolver {

    private static Variable emptyVar = new Variable(new Clause().toString());

    public static Environment solve(Formula formula) {
        Environment newEnv = new Environment();     //env to be returned

        //if there are no clauses, the formula is trivially satisfiable
        if(formula.getClauses().size()==0){
            return newEnv;
        }
        //else start the recursion sequence
        ImList<Clause> clauses = formula.getClauses();
        return solve(clauses, newEnv);
    }

    private static Environment solve(ImList<Clause> clauses, Environment env) {

        // If there are no clauses, the formula is trivially satisfiable
        if (clauses.isEmpty()) {
            return env;
        }

        Clause cur = smallestClause(clauses);

        //if there is an empty clause, clause list is unsatisfiable - use empty clause to denote a clause evaluated to FALSE
        if (cur.isEmpty()) {
            return env.putFalse(emptyVar);
        }
        //if clause has only one literal
        else if (cur.isUnit()) {
            Literal singleLiteral = cur.chooseLiteral();
            //bind its variable
            env = bindVariable(env, singleLiteral, Bool.TRUE);
            // If variable is bounded to bool.FALSE, substitute() will take in ~a
            clauses = substitute(clauses, singleLiteral);
            return solve(clauses, env);

        } else {
            ImList<Clause> clausesWhenCurIsTrue;
            ImList<Clause> clausesWhenCurIsFalse;
            //pick an arbitrary literal
            Literal currentLiteral = cur.chooseLiteral();

            // Case 1: Set literal to true
            env = env.putTrue(currentLiteral.getVariable());
            // Set literal to true and substitute
            // Note: Do not use clause.rest() because substitute will not take into account all the clauses
            clausesWhenCurIsTrue = substitute(clauses, PosLiteral.make(currentLiteral.getVariable()));

            env = solve(clausesWhenCurIsTrue, env);

            // If it failed, set literal to false
            if (env.get(emptyVar) == Bool.FALSE) {
                // Reset
                env = env.put(emptyVar, Bool.UNDEFINED);
                // Try setting currentLiteral to False
                env = env.putFalse(currentLiteral.getVariable());
                // Set literal to false and substitute
                clausesWhenCurIsFalse = substitute(clauses, NegLiteral.make(currentLiteral.getVariable()));

                env = solve(clausesWhenCurIsFalse, env);
            }
            return env;
        }
    }

    private static ImList<Clause> substitute(ImList<Clause> clauses,
                                             Literal l) {
        ImList<Clause> result = new EmptyImList<>();
        for(Clause cur: clauses){
            cur = cur.reduce(l);
            if(cur != null){
                result = result.add(cur);
            }
        }
        return result;
    }

    private static Environment bindVariable(Environment env, Literal l, Bool b ) {


        // Binds variable to TRUE (I.e. if literal is ~a, it will be binded to Bool.FALSE)
        if (b == Bool.TRUE) {
            // If literal is positive, bind it to Bool.TRUE
            if (l.equals(PosLiteral.make(l.getVariable()))) {
                env = env.putTrue((l.getVariable()));
            } else {
                // Otherwise, bind it to Bool.FALSE
                env = env.putFalse((l.getVariable()));
            }
        } else {
            // Binds variable to FALSE (I.e. if literal is ~a, it will be binded to Bool.TRUE)
            // If literal is positive, bind it to Bool.FALSE
            if (l.equals(PosLiteral.make(l.getVariable()))) {
                env = env.putFalse((l.getVariable()));
            } else {
                // Otherwise, bind it to Bool.TRUE
                env = env.putTrue((l.getVariable()));
            }
        }
        return env;
    }

    //used to find the smallest clause in O(n)
    private static Clause smallestClause(ImList<Clause> clauses){
        Clause smallest = clauses.first();
        for(Clause current: clauses){
            if(current.size()<=1){
                smallest = current;
                return smallest;
            } else if(current.size()<=smallest.size()){
                smallest = current;
            }
        }
        return smallest;
    }

}
