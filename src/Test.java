import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

public class Test {

    public static void evaluate(String expr) {
        //List<Token> tokenList = Tokenizer.tokenize(expr);
        List<Token> tokenList = Tokenizer.betterTokenize(expr);
        System.out.println(tokenList);
        System.out.println(Evaluator.evalUnderEnv(tokenList, new HashMap<>()));
    }

    public static void main(String[] args) {
        // the below 2 are equivalent (to test new tokenizer)
        //evaluate("1+2+3.0");
        //evaluate("1 + 2 + 3");

        //evaluate("( 2 + 3 )");
        //evaluate("1 + ( 2 + 3 )");
        //evaluate("48 / 2 * ( 9 + 3 )");
        //evaluate("48 / ( 2 * ( 9 + 3 ) )");

        // Tokenizer errors
        //evaluate("0.0.1"); // not a number
        //evaluate("0.1 ^ 2"); // unknown symbol

        // Evaluator errors
        //evaluate("(1 + 2"); // ) expected
        //evaluate("1 + 2)"); // ( expected
        //evaluate("((1 + 2)"); // ) expected
        //evaluate("(1 + 2))"); // ) expected
        //evaluate("1 2"); // not enough operators
        //evaluate("1 + + 2"); // not enough operands/too many operators
    }
}
