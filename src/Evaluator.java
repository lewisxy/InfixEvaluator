import java.util.*;

public class Evaluator {
    // create a static precedence map
    public static final Map<Token, Integer> precedence;
    static {
        Map<Token, Integer> aMap = new HashMap<>();
        aMap.put(Symbol.PLUS, 1);
        aMap.put(Symbol.MINUS, 1);
        aMap.put(Symbol.TIMES, 2);
        aMap.put(Symbol.DIVIDED_BY, 2);
        // aMap.put(Symbol.LPAREN, 3);
        // aMap.put(Symbol.RPAREN, 3);
        precedence = Collections.unmodifiableMap(aMap);
    }

    private static Token evalSimpleUnderEnv(Token operator, Token operand1, Token operand2, Map<String, Double> env) {
        double num1, num2;
        if (operand1 instanceof Var) {
            try {
                num1 = env.get(((Var) operand1).getName());
            } catch (Exception e) {
                throw new EvaluatorError(((Var) operand1).getName() + " undefined");
            }
        } else { // it must be a number otherwise
            num1 = ((Num) operand1).getValue();
        }
        if (operand2 instanceof Var) {
            try {
                num2 = env.get(((Var) operand2).getName());
            } catch (Exception e) {
                throw new EvaluatorError(((Var) operand2).getName() + " undefined");
            }
        } else { // it must be a number otherwise
            num2 = ((Num) operand2).getValue();
        }
        switch ((Symbol) operator) {
            case PLUS:
                return new Num(num1 + num2);
            case MINUS:
                return new Num(num1 - num2);
            case TIMES:
                return new Num(num1 * num2);
            case DIVIDED_BY:
                return new Num(num1 / num2);
            case LPAREN:
                throw new EvaluatorError(") expected");
            default:
                throw new EvaluatorError("Unknown Symbol");
        }
    }

    private static void process(Stack<Token> operatorStack, Stack<Token> operandStack, Map<String, Double> env) {
        Token operator, operand1, operand2;
        try {
            operator = operatorStack.pop();
        } catch (Exception e) {
            throw new EvaluatorError("not enough operators");
        }
        try {
            operand1 = operandStack.pop();
            operand2 = operandStack.pop();
        } catch (Exception e) {
            throw new EvaluatorError("not enough operands");
        }
        // !!! IMPORTANT !!!: reverse the order of operands
        Token ans = evalSimpleUnderEnv(operator, operand2, operand1, env);
        operandStack.push(ans);
    }

    public static Token evalUnderEnv(List<Token> tokenList, Map<String, Double> env) {
        Stack<Token> operators = new Stack<>();
        Stack<Token> operands = new Stack<>();

        while (tokenList.size() != 0) {
            Token tmp = tokenList.remove(0);
            if (!(tmp instanceof Symbol)) {
                operands.push(tmp);
            } else if (tmp == Symbol.LPAREN) {
                operators.push(tmp);
            } else if (tmp == Symbol.RPAREN) {
                while (!operators.empty() && operators.peek() != Symbol.LPAREN) {
                    process(operators, operands, env);
                }
                if (operators.empty()) {
                    throw new EvaluatorError("( expected");
                }
                operators.pop(); // pop the "("
            } else { // symbols
                if (operators.empty() || operators.peek() == Symbol.LPAREN) {
                    operators.push(tmp);
                } else {
                    while (!operators.empty() && precedence.get(operators.peek()) >= precedence.get(tmp)) {
                        process(operators, operands, env);
                    }
                    operators.push(tmp);
                }
            }
        }
        if (operators.contains(Symbol.LPAREN)) {
            throw new EvaluatorError(") expected");
        }
        while (!operators.empty() && operands.size() >= 2) {
            process(operators, operands, env);
        }
        // expecting operators to be empty and there is exactly 1 element in operands
        if (!operators.empty()) {
            throw new EvaluatorError("too many operators");
        }
        if (operands.size() > 1) {
            throw new EvaluatorError("not enough operators");
        }
        Token rtn = operands.peek();
        // if it is a variable, return the corresponding value, or throw an error if undefined
        if (rtn instanceof Var) {
            try {
                double num = env.get(((Var) rtn).getName());
                rtn = new Num(num);
            } catch (Exception e) {
                throw new EvaluatorError(((Var) rtn).getName() + " undefined");
            }
        }
        return rtn;
    }
}
