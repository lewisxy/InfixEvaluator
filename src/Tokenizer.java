import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Tokenizer {
    private static boolean isNumeric(String strNum) {
        try {
            double d = Double.parseDouble(strNum);
        } catch (NumberFormatException | NullPointerException nfe) {
            return false;
        }
        return true;
    }

    // simple tokenizer (there is a better tokenizer below)
    public static List<Token> tokenize(String str) {
        List<Token> rtn = new ArrayList<>();
        Scanner input = new Scanner(str);
        input.useDelimiter("[\t ]+");
        while (input.hasNext()) {
            String tmp = input.next();
            if (isNumeric(tmp)) {
                rtn.add(new Num(Double.parseDouble(tmp)));
            } else {
                if (tmp.equals("+")) {
                    rtn.add(Symbol.PLUS);
                } else if (tmp.equals("-")) {
                    rtn.add(Symbol.MINUS);
                } else if (tmp.equals("*")) {
                    rtn.add(Symbol.TIMES);
                } else if (tmp.equals("/")) {
                    rtn.add(Symbol.DIVIDED_BY);
                } else if (tmp.equals("=")) {
                    rtn.add(Symbol.EQUAL);
                } else if (tmp.equals("(")) {
                    rtn.add(Symbol.LPAREN);
                } else if (tmp.equals(")")) {
                    rtn.add(Symbol.RPAREN);
                } else {
                    rtn.add(new Var(tmp));
                }
            }
        }
        return rtn;
    }

    private static int consumeNum(String str, int idx, List<Token> tokenList) {
        int i = idx;
        String tmp = str.charAt(idx) + "";
        while (i < str.length() && ((str.charAt(i) >= '0' && str.charAt(i) <= '9') || str.charAt(i) == '.')) {
            i++;
        }
        String numStr = str.substring(idx, i);
        // let java to handle the conversion detail
        try {
            double res = Double.parseDouble(numStr);
            tokenList.add(new Num(res));
            return i;
        } catch (Exception e) {
            throw new TokenizerError(numStr + " is not a number");
        }
    }

    private static int consumeAlpha(String str, int idx, List<Token> tokenList) {
        int i = idx;
        String tmp = str.charAt(idx) + "";
        while (i < str.length() && str.charAt(i) != ' ' && str.charAt(i) != '\t'
            && str.charAt(i) != '+' && str.charAt(i) != '-' && str.charAt(i) != '*' && str.charAt(i) != '/' && str.charAt(i) != '=') {
            i++;
        }
        tokenList.add(new Var(str.substring(idx, i)));
        return i;
    }

    // a better tokenizer that can deal with expressions with no spaces
    public static List<Token> betterTokenize(String str) {
        List<Token> rtn = new ArrayList<>();
        int i = 0;
        while (i < str.length()) {
            char tmp = str.charAt(i);
            if (tmp == ' ' ||  tmp == '\t') {
                // do nothing
                i++;
            } else if (tmp == '(') {
                rtn.add(Symbol.LPAREN);
                i++;
            } else if (tmp == ')') {
                rtn.add(Symbol.RPAREN);
                i++;
            } else if (tmp == '+') {
                rtn.add(Symbol.PLUS);
                i++;
            } else if (tmp == '-') {
                rtn.add(Symbol.MINUS);
                i++;
            } else if (tmp == '*') {
                rtn.add(Symbol.TIMES);
                i++;
            } else if (tmp == '/') {
                rtn.add(Symbol.DIVIDED_BY);
                i++;
            } else if (tmp == '=') {
                rtn.add(Symbol.EQUAL);
                i++;
            } else if (tmp >= '0' && tmp <= '9') { // number
                i = consumeNum(str, i, rtn);
            } else if ((tmp >= 'A' && tmp <= 'Z') || (tmp >= 'a' && tmp <= 'z')) { // alpha
                i = consumeAlpha(str, i, rtn);
            } else {
                throw new TokenizerError("Unknown Symbol: " + tmp);
            }
        }
        return rtn;
    }
}
