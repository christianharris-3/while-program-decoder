import java.math.BigInteger;

public class WhileBoolean {
    private final int type;
    private WhileBoolean left_bool;
    private WhileBoolean right_bool;
    private WhileArithmetic left_arith;
    private WhileArithmetic right_arith;

    public WhileBoolean(String statement) throws Exception{
        statement = Utils.clean_string(statement);

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
            left_bool = new WhileBoolean(Utils.remove_prefix(statement, "¬"));
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
    public WhileBoolean(BigInteger value) {
//        System.out.println("loading new while boolean of num: "+value.toString());

        if (value.signum() == 0) {
            type = 0;
        } else if (value.equals(BigInteger.ONE)) {
            type = 1;
        } else {
            BigInteger new_value = value.divide(BigInteger.valueOf(4));
            int mod_value = value.mod(BigInteger.valueOf(4)).intValue();
            if (mod_value < 2) {
                mod_value += 4;
                new_value = new_value.subtract(BigInteger.ONE);
            }
            BigInteger[] out = Utils.inverse_phi(new_value);
            switch (mod_value) {
                case 2:
                    type = 2;
                    left_arith = new WhileArithmetic(out[0]);
                    right_arith = new WhileArithmetic(out[1]);
                    break;
                case 3:
                    type = 3;
                    left_arith = new WhileArithmetic(out[0]);
                    right_arith = new WhileArithmetic(out[1]);
                    break;
                case 4:
                    type = 4;
                    left_bool = new WhileBoolean(new_value);
                    break;
                case 5:
                    type = 5;
                    left_bool = new WhileBoolean(out[0]);
                    right_bool = new WhileBoolean(out[1]);
                    break;
                default:
                    type = 0;
            }
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
            case 2 -> BigInteger.valueOf(2).add(BigInteger.valueOf(4).multiply(Utils.phi(left_arith.map_to_natural(), right_arith.map_to_natural())));
            case 3 -> BigInteger.valueOf(3).add(BigInteger.valueOf(4).multiply(Utils.phi(left_arith.map_to_natural(), right_arith.map_to_natural())));
            case 4 -> BigInteger.valueOf(4).add(BigInteger.valueOf(4).multiply(left_bool.map_to_natural()));
            case 5 -> BigInteger.valueOf(5).add(BigInteger.valueOf(4).multiply(Utils.phi(left_bool.map_to_natural(), right_bool.map_to_natural())));
            default -> BigInteger.ZERO;
        };
    }
    public String output_python() {
        return switch (type) {
            case 0 -> "False";
            case 1 -> "True";
            case 2 -> left_arith.reconstruct() + " == " + right_arith.reconstruct();
            case 3 -> left_arith.reconstruct() + " <= " + right_arith.reconstruct();
            case 4 -> "not " + left_bool.reconstruct();
            case 5 -> left_bool.reconstruct() + " and " + right_bool.reconstruct();
            default -> "bool";
        };
    }
}
