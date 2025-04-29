import java.math.BigInteger;

public class WhileArithmetic {
    private int type;
    private WhileArithmetic left_arith;
    private WhileArithmetic right_arith;
    private WhileVariable variable;
    private BigInteger value;

    public WhileArithmetic(String statement) {
        statement = Utils.clean_string(statement);

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
                value = new BigInteger(statement);
                type = 5;
            } catch (NumberFormatException e) {
                type = 1;
                variable = new WhileVariable(statement);
            }
        }
    }
    public WhileArithmetic(BigInteger value) {
//        System.out.println("loading new while arithmetic of num: "+value.toString());

        BigInteger new_value = value.divide(BigInteger.valueOf(5));
        int mod_value = value.mod(BigInteger.valueOf(5)).intValue();
        BigInteger[] out = Utils.inverse_phi(new_value);
        switch (mod_value) {
            case 0:
                type = 5;
                this.value = new_value;
                break;
            case 1:
                type = 1;
                variable = new WhileVariable(new_value);
                break;
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
                left_arith = new WhileArithmetic(out[0]);
                right_arith = new WhileArithmetic(out[1]);
                break;
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
            case 5 -> BigInteger.valueOf(5).multiply(value);
            case 1 -> BigInteger.valueOf(1).add(BigInteger.valueOf(5).multiply(variable.map_to_natural()));
            case 2 -> BigInteger.valueOf(2).add(BigInteger.valueOf(5).multiply(Utils.phi(left_arith.map_to_natural(), right_arith.map_to_natural())));
            case 3 -> BigInteger.valueOf(3).add(BigInteger.valueOf(5).multiply(Utils.phi(left_arith.map_to_natural(), right_arith.map_to_natural())));
            case 4 -> BigInteger.valueOf(4).add(BigInteger.valueOf(5).multiply(Utils.phi(left_arith.map_to_natural(), right_arith.map_to_natural())));
            default -> BigInteger.ZERO;
        };
    }
    public String output_python() {
        return reconstruct();
    }
}
