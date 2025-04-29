import java.math.BigInteger;

public class WhileBoolean {
    private final int type;
    private WhileBoolean left_bool;
    private WhileBoolean right_bool;
    private WhileArithmetic left_arith;
    private WhileArithmetic right_arith;

    public WhileBoolean(String statement) throws Exception{
        statement = Encoder.clean_string(statement);

        if (statement.equalsIgnoreCase("false")) {
            type = 0;
        } else if (statement.equalsIgnoreCase("true")) {
            type = 1;
        } else if (statement.contains("∧")) {
            type = 5;
            String[] split = statement.split("∧",2);
            left_bool = new WhileBoolean(split[0]);
            right_bool = new WhileBoolean(split[1]);
        } else if (statement.startsWith("¬")) {
            type = 4;
            left_bool = new WhileBoolean(Encoder.remove_prefix(statement, "¬"));
        } else if (statement.contains("<=")) {
            type = 3;
            String[] split = statement.split("<=",2);
            left_arith = new WhileArithmetic(split[0]);
            right_arith = new WhileArithmetic(split[1]);
        } else if (statement.contains(">=")) {
            type = 3;
            String[] split = statement.split(">=",2);
            left_arith = new WhileArithmetic(split[1]);
            right_arith = new WhileArithmetic(split[0]);
        } else if (statement.contains("=")) {
            type = 2;
            String[] split = statement.split("=",2);
            left_arith = new WhileArithmetic(split[0]);
            right_arith = new WhileArithmetic(split[1]);
        } else {
            throw new Exception("Can't compile boolean expression: "+statement);
        }
    }
    public String reconstruct() {
        return switch (type) {
            case 0 -> "False";
            case 1 -> "True";
            case 2 -> left_arith.reconstruct() + "=" + right_arith.reconstruct();
            case 3 -> left_arith.reconstruct() + "<=" + right_arith.reconstruct();
            case 4 -> "¬" + left_bool.reconstruct();
            case 5 -> left_bool.reconstruct() + "∧" + right_bool.reconstruct();
            default -> "bool";
        };
    }
    public BigInteger map_to_natural() {
        return switch (type) {
            case 1 -> BigInteger.ONE;
            case 2 -> BigInteger.valueOf(2).add(BigInteger.valueOf(4).multiply(Encoder.phi(left_arith.map_to_natural(), right_arith.map_to_natural())));
            case 3 -> BigInteger.valueOf(3).add(BigInteger.valueOf(4).multiply(Encoder.phi(left_arith.map_to_natural(), right_arith.map_to_natural())));
            case 4 -> BigInteger.valueOf(4).add(BigInteger.valueOf(4).multiply(left_bool.map_to_natural()));
            case 5 -> BigInteger.valueOf(5).add(BigInteger.valueOf(4).multiply(Encoder.phi(left_bool.map_to_natural(), right_bool.map_to_natural())));
            default -> BigInteger.ZERO;
        };
    }
}
