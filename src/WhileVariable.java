import java.math.BigInteger;
import java.util.ArrayList;

public class WhileVariable {
    public static ArrayList<String> names = new ArrayList<>();
    private BigInteger var_id;
    public WhileVariable(String statement) {
        String var_name = Utils.clean_string(statement);
        try {
            if (var_name.startsWith("x_")) {
                var_id = new BigInteger(Utils.remove_prefix(var_name, "x_"));
            } else {
                throw new NumberFormatException();
            }
        } catch (NumberFormatException e) {
            if (!names.contains(var_name)) {
                names.add(var_name);
            }
            var_id = BigInteger.valueOf(names.indexOf(var_name));
        }
    }
    public WhileVariable(BigInteger value) {
//        System.out.println("loading new while var of num: "+value.toString());
        var_id = value;
    }
    public String reconstruct() {
        return "x_"+var_id;
    }
    public BigInteger map_to_natural() {
        return var_id;
    }
    public String output_python() {
        return reconstruct();
    }
}
