import java.math.BigInteger;

public class WhileStatement {
    public int type;
    public WhileStatement first_statement;
    public WhileStatement second_statement;
    public WhileStatement final_statement;
    public WhileVariable assigning_variable;
    public WhileArithmetic assigning_statement;
    public WhileBoolean boolean_statement;


    public WhileStatement(String statement) throws Exception{
        statement = Encoder.clean_string(statement);

        if (statement.isEmpty()) {
            type = -1;
        } else if (statement.startsWith("if ")) {
            type = 3;
            String[] split = sep_curley_bracket(statement);

            String boolString = Encoder.clean_string(Encoder.remove_suffix(Encoder.remove_prefix(split[0],"if"),"then"));
            String bodyString = Encoder.clean_string(split[1]);
            String elseString = Encoder.clean_string(Encoder.remove_prefix(split[2],"}"));
            String finalString;
            if (elseString.startsWith("else")) {
                String[] elseSplit = sep_curley_bracket(elseString);
                elseString = elseSplit[1];
                finalString = elseSplit[2];
            } else {
                finalString = elseString;
                elseString = "skip";
            }

            boolean_statement = new WhileBoolean(boolString);
            first_statement = new WhileStatement(bodyString);
            second_statement = new WhileStatement(elseString);
            final_statement = new WhileStatement(finalString);

        } else if (statement.startsWith("while ")) {
            type = 4;
            String[] split = sep_curley_bracket(statement);

            String boolString = Encoder.clean_string(Encoder.remove_suffix(Encoder.remove_prefix(split[0], "while"), "do"));
            String bodyString = Encoder.clean_string(split[1]);
            String finalString = Encoder.clean_string(split[2]);

            boolean_statement = new WhileBoolean(boolString);
            first_statement = new WhileStatement(bodyString);
            final_statement = new WhileStatement(finalString);
        } else {
            String[] lines = statement.split(";",2);
            if (lines[0].equals("skip")) {
                type = 0;
            } else if (lines[0].contains(":=")) {
                type = 1;
                String[] split = lines[0].split(":=",2);
                String varString = Encoder.clean_string(split[0]);
                String arithString = Encoder.clean_string(split[1]);
                assigning_variable = new WhileVariable(varString);
                assigning_statement = new WhileArithmetic(arithString);
            } else {
                throw new Exception("Line Can't be Compiled: "+lines[0]);
            }
            if (lines.length > 1) {
                final_statement = new WhileStatement(lines[1]);
            } else {
                final_statement = new WhileStatement("");
            }
        }
    }
    private static String[] sep_curley_bracket(String program) {
        String[] output = new String[3];
        String[] sep = program.split("\\{",2);
        output[0] = Encoder.clean_string(sep[0]);
        StringBuilder builder = new StringBuilder();
        int bracket_counter = 0;
        for (int i=0;i<sep[1].length();i++) {
            if (sep[1].charAt(i) == '{') {
                bracket_counter += 1;
            } else if (sep[1].charAt(i) == '}') {
                bracket_counter -= 1;
            }
            if (bracket_counter == -1) {
                output[1] = Encoder.clean_string(builder.toString());
                output[2] = Encoder.clean_string(sep[1].substring(i+1));
                return output;
            } else {
                builder.append(sep[1].charAt(i));
            }
        }

        return output;
    }

    public String reconstruct(int indent) {
        String output = switch (type) {
            case 0 -> Encoder.tab(indent) + "skip;";
            case 1 -> Encoder.tab(indent) + assigning_variable.reconstruct() + ":=" + assigning_statement.reconstruct() + ";";
            case 3 -> Encoder.tab(indent) + "if " + boolean_statement.reconstruct() + " then {\n" + first_statement.reconstruct(indent + 1) + "\n" + Encoder.tab(indent) + "} else {\n" + second_statement.reconstruct(indent + 1) + "\n" + Encoder.tab(indent) + "}";
            case 4 -> Encoder.tab(indent) + "while " + boolean_statement.reconstruct() + " do {\n" + first_statement.reconstruct(indent + 1) + "\n" + Encoder.tab(indent) + "}";
            default -> "";
        };
        if (type != -1 && final_statement.type != -1) {
            output = output+"\n"+final_statement.reconstruct(indent);
        }
        return output;
    }
    public BigInteger map_to_natural() throws Exception{
        if (type != -1) {
            BigInteger value;
            value = switch (type) {
                case 1 -> BigInteger.valueOf(1).add(BigInteger.valueOf(4).multiply(Encoder.phi(assigning_variable.map_to_natural(), assigning_statement.map_to_natural())));
                case 3 -> BigInteger.valueOf(3).add(BigInteger.valueOf(4).multiply(Encoder.phi(boolean_statement.map_to_natural(), Encoder.phi(first_statement.map_to_natural(), second_statement.map_to_natural()))));
                case 4 -> BigInteger.valueOf(4).add(BigInteger.valueOf(4).multiply(Encoder.phi(boolean_statement.map_to_natural(), first_statement.map_to_natural())));
                default -> BigInteger.ZERO;
            };
            if (final_statement.type != -1) {
                return BigInteger.valueOf(2).add(BigInteger.valueOf(4).multiply(Encoder.phi(value, final_statement.map_to_natural())));
            }
            return value;
        } else {
            throw new Exception("Statement of type -1 called");
        }
    }
}
