//import java.util.Arrays;

public class WhileVariable {
//    public static = new String[0];
    public WhileVariable(String statement) {
        var_name = Compiler.clean_string(statement);
    }
    public String reconstruct() {
        return var_name;
    }
}
