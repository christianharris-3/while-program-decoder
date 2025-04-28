public class WhileBoolean {
    private int type = -1;
    private WhileBoolean left_bool;
    private WhileBoolean right_bool;
    private WhileArithmetic left_arith;
    private WhileArithmetic right_arith;

    public WhileBoolean(String statement) {
        statement = Compiler.clean_string(statement);

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
            left_bool = new WhileBoolean(Compiler.remove_prefix(statement, "¬"));
        } else if (statement.contains("<=")) {
            type = 3;
            String[] split = statement.split("<=",2);
            left_arith = new WhileArithmetic(split[0]);
            right_arith = new WhileArithmetic(split[1]);
        } else if (statement.contains(">")) {
            type = 3;
            String[] split = statement.split(">",2);
            left_arith = new WhileArithmetic(split[1]);
            right_arith = new WhileArithmetic(split[0]);
        } else if (statement.contains("=")) {
            type = 2;
            String[] split = statement.split("=",2);
            left_arith = new WhileArithmetic(split[0]);
            right_arith = new WhileArithmetic(split[1]);
        }
    }
    public String reconstruct() {
        switch (type) {
            case 0:
                return "False";
            case 1:
                return "True";
            case 2:
                return left_arith.reconstruct()+"="+right_arith.reconstruct();
            case 3:
                return left_arith.reconstruct()+"<="+right_arith.reconstruct();
            case 4:
                return "¬"+left_arith.reconstruct();
            case 5:
                return left_bool.reconstruct()+"∧"+right_bool.reconstruct();
            default:
                return "bool";
        }
    }
}
