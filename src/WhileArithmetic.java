public class WhileArithmetic {
    private int type;
    private WhileArithmetic left_arith;
    private WhileArithmetic right_arith;
    private WhileVariable variable;
    private int value;

    public WhileArithmetic(String statement) {
        statement = Compiler.clean_string(statement);

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
        switch (type) {
            case 5:
                return String.valueOf(value);
            case 1:
                return variable.reconstruct();
            case 2:
                return left_arith.reconstruct()+"+"+right_arith.reconstruct();
            case 3:
                return left_arith.reconstruct()+"-"+right_arith.reconstruct();
            case 4:
                return left_arith.reconstruct()+"*"+right_arith.reconstruct();
            default:
                return "arithmetic";
        }
    }
}
