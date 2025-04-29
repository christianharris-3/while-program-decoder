import java.math.BigInteger;
import java.util.ArrayList;

public class WhileVariable {
    public static ArrayList<String> names = new ArrayList<>();
    private int var_id;
    public WhileVariable(String statement) {
        String var_name = Encoder.clean_string(statement);
        try {
            if (var_name.startsWith("x_")) {
                var_id = Integer.parseInt(Encoder.remove_prefix(var_name, "x_"));
            } else {
                throw new NumberFormatException();
            }
        } catch (NumberFormatException e) {
            if (!names.contains(var_name)) {
                names.add(var_name);
            }
            var_id = names.indexOf(var_name);
        }
    }
    public String reconstruct() {
        return "x_"+var_id;
    }
    public BigInteger map_to_natural() {
        return BigInteger.valueOf(var_id);
    }
}
