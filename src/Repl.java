import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

public class Repl {

    public static void evaluate(String expr) {
        //List<Token> tokenList = Tokenizer.tokenize(expr);
        List<Token> tokenList = Tokenizer.betterTokenize(expr);
        //System.out.println(tokenList);
        System.out.println(Evaluator.evalUnderEnv(tokenList, new HashMap<>()));
    }

    public static void mainLoop() {
        Map<String, Double> env = new HashMap<>();

        Scanner input = new Scanner(System.in);
        System.out.print(">>> ");

        while (input.hasNextLine()) {
            String tmp = input.nextLine();
            if (tmp.equals("exit")) {
                break;
            }

            List<Token> tokenList = Tokenizer.betterTokenize(tmp);
            int eq = tokenList.indexOf(Symbol.EQUAL);
            if (eq == -1) { // not an assignment
                Token res = Evaluator.evalUnderEnv(tokenList, env);
                if (res instanceof Num) {
                    System.out.println(((Num) res).getValue());
                }
                //System.out.println(res);
                //System.out.println(Evaluator.evalUnderEnv(tokenList, env));
            } else if (eq == 1 && tokenList.get(0) instanceof Var) { // must be in the form "varname = expr"
                try {
                    String varName = ((Var) tokenList.get(0)).getName();
                    double res = ((Num) Evaluator.evalUnderEnv(tokenList.subList(eq + 1, tokenList.size()), env)).getValue();
                    env.put(varName, res);
                    System.out.println(varName + " = " + res);
                } catch (Exception e) {
                    throw new EvaluatorError("illegal assignment");
                }
            } else {
                throw new EvaluatorError("illegal assignment");
            }
            //System.out.println(tmp);

            System.out.print(">>> ");
        }
    }

    public static void main(String[] args) {
        System.out.println("Expression evaluator REPL");
        mainLoop();
    }
}
