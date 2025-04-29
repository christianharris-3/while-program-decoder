import java.math.BigInteger;

public class WhileArithmetic {
    private int type;
    private WhileArithmetic left_arith;
    private WhileArithmetic right_arith;
    private WhileVariable variable;
    private int value;

    public WhileArithmetic(String statement) {
        statement = Encoder.clean_string(statement);

        if (statement.contains("*")) {
            type = 4;
            String[] split = statement.split("\\*",2);
            left_arith = new WhileArithmetic(split[0]);
            right_arith = new WhileArithmetic(split[1]);
        } else if (statement.contains("+")) {
            type = 2;
            String[] split = statement.split("\\+",2);
            left_arith = new WhileArithmetic(split[0]);
            right_arith = new WhileArithmetic(split[1]);
        } else if (statement.contains("-")) {
            type = 3;
            String[] split = statement.split("-",2);
            left_arith = new WhileArithmetic(split[0]);
            right_arith = new WhileArithmetic(split[1]);
        } else {
            try {
                value = Integer.parseInt(statement);
                type = 5;
            } catch (NumberFormatException e) {
                type = 1;
                variable = new WhileVariable(statement);
            }
        }
    }
    public String reconstruct() {
        return switch (type) {
            case 5 -> String.valueOf(value);
            case 1 -> variable.reconstruct();
            case 2 -> left_arith.reconstruct() + "+" + right_arith.reconstruct();
            case 3 -> left_arith.reconstruct() + "-" + right_arith.reconstruct();
            case 4 -> left_arith.reconstruct() + "*" + right_arith.reconstruct();
            default -> "arithmetic";
        };
    }
    public BigInteger map_to_natural() {
        return switch (type) {
            case 5 -> BigInteger.valueOf(5).multiply(BigInteger.valueOf(value));
            case 1 -> BigInteger.valueOf(1).add(BigInteger.valueOf(5).multiply(variable.map_to_natural()));
            case 2 -> BigInteger.valueOf(2).add(BigInteger.valueOf(5).multiply(Encoder.phi(left_arith.map_to_natural(), right_arith.map_to_natural())));
            case 3 -> BigInteger.valueOf(3).add(BigInteger.valueOf(5).multiply(Encoder.phi(left_arith.map_to_natural(), right_arith.map_to_natural())));
            case 4 -> BigInteger.valueOf(4).add(BigInteger.valueOf(5).multiply(Encoder.phi(left_arith.map_to_natural(), right_arith.map_to_natural())));
            default -> BigInteger.ZERO;
        };
    }
    public String output_python() {
        return reconstruct();
    }
}
