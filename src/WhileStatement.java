import java.math.BigInteger;

public class WhileStatement {
    public int type = -1;
    public WhileStatement first_statement;
    public WhileStatement second_statement;
    public WhileStatement final_statement;
    public WhileVariable assigning_variable;
    public WhileArithmetic assigning_statement;
    public WhileBoolean boolean_statement;


    public WhileStatement(String statement) throws Exception{
        statement = Utils.clean_string(statement);

        if (statement.isEmpty()) {
            type = -1;
        } else if (statement.startsWith("if ")) {
            type = 3;
            String[] split = sep_curley_bracket(statement);

            String boolString = Utils.clean_string(Utils.remove_suffix(Utils.remove_prefix(split[0],"if"),"then"));
            String bodyString = Utils.clean_string(split[1]);
            String elseString = Utils.clean_string(Utils.remove_prefix(split[2],"}"));
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

            String boolString = Utils.clean_string(Utils.remove_suffix(Utils.remove_prefix(split[0], "while"), "do"));
            String bodyString = Utils.clean_string(split[1]);
            String finalString = Utils.clean_string(split[2]);

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
                String varString = Utils.clean_string(split[0]);
                String arithString = Utils.clean_string(split[1]);
                assigning_variable = new WhileVariable(varString);
                assigning_statement = new WhileArithmetic(arithString);
            } else {
                throw new Exception("Statement Can't be Compiled: "+lines[0]);
            }
            if (lines.length > 1) {
                final_statement = new WhileStatement(lines[1]);
            } else {
                final_statement = new WhileStatement("");
            }
        }
    }
    public WhileStatement(BigInteger value) throws Exception{
//        System.out.println("loading new while statement of num: "+value.toString());
        final_statement = new WhileStatement("");
        if (value.signum() == 0) {
            type = 0;
        } else {
            BigInteger new_value = value.divide(BigInteger.valueOf(4));
            int mod_value = value.mod(BigInteger.valueOf(4)).intValue();
            if (mod_value == 0) {
                mod_value += 4;
                new_value = new_value.subtract(BigInteger.ONE);
            }
            BigInteger[] out = Utils.inverse_phi(new_value);
            switch (mod_value) {
                case 1:
                    type = 1;
                    assigning_variable = new WhileVariable(out[0]);
                    assigning_statement = new WhileArithmetic(out[1]);
                    break;
                case 2:
                    type = 2;
                    first_statement = new WhileStatement(out[0]);
                    final_statement = new WhileStatement(out[1]);
                    break;
                case 3:
                    type = 3;
                    BigInteger[] out2 = Utils.inverse_phi(out[1]);
                    boolean_statement = new WhileBoolean(out[0]);
                    first_statement = new WhileStatement(out2[0]);
                    second_statement = new WhileStatement(out2[1]);
                    break;
                case 4:
                    type = 4;
                    boolean_statement = new WhileBoolean(out[0]);
                    first_statement = new WhileStatement(out[1]);
                    break;
            }
        }
    }
    private static String[] sep_curley_bracket(String program) throws Exception{
        String[] output = new String[3];
        String[] sep = program.split("\\{",2);
        output[0] = Utils.clean_string(sep[0]);
        if (sep.length == 1) {
            String[] lines = program.split("\n");
            output[0] = lines[0];
            StringBuilder builder = new StringBuilder();
            int base_indent = Utils.get_indent(lines[0]);
            boolean block_found = false;
            for (int i=1;i<lines.length;i++) {
                if (!block_found) {
                    int indent = Utils.get_indent(lines[i]);
                    if (indent > base_indent) {
                        builder.append(lines[i]);
                        builder.append('\n');
                    } else if (indent < base_indent) {
                        throw new Exception("invalid indentation in line: "+lines[i]);
                    } else {
                        output[1] = Utils.clean_string(builder.toString());
                        block_found = true;
                        builder = new StringBuilder();
                    }
                }
                if (block_found) {
                    builder.append(lines[i]);
                    builder.append('\n');
                }
            }
            if (block_found) {
                output[2] = builder.toString();
            } else {
                output[1] = builder.toString();
                output[2] = "";
            }

        } else {
            StringBuilder builder = new StringBuilder();
            int bracket_counter = 0;
            for (int i=0;i<sep[1].length();i++) {
                if (sep[1].charAt(i) == '{') {
                    bracket_counter += 1;
                } else if (sep[1].charAt(i) == '}') {
                    bracket_counter -= 1;
                }
                if (bracket_counter == -1) {
                    output[1] = Utils.clean_string(builder.toString());
                    output[2] = Utils.clean_string(sep[1].substring(i+1));
                    return output;
                } else {
                    builder.append(sep[1].charAt(i));
                }
            }
        }

        return output;
    }

    public String reconstruct(int indent) {
//        System.out.println("outputing statement type: "+Utils.tab(indent)+type+" final statement type: "+final_statement.type);
        String output = switch (type) {
            case 0 -> Utils.tab(indent) + "skip;";
            case 1 -> Utils.tab(indent) + assigning_variable.reconstruct() + ":=" + assigning_statement.reconstruct() + ";";
            case 2 -> first_statement.reconstruct(indent);
            case 3 -> Utils.tab(indent) + "if " + boolean_statement.reconstruct() + " then {\n" + first_statement.reconstruct(indent + 1) + "\n" + Utils.tab(indent) + "} else {\n" + second_statement.reconstruct(indent + 1) + "\n" + Utils.tab(indent) + "}";
            case 4 -> Utils.tab(indent) + "while " + boolean_statement.reconstruct() + " do {\n" + first_statement.reconstruct(indent + 1) + "\n" + Utils.tab(indent) + "}";
            default -> "";
        };
        if (type != -1 && final_statement.type != -1) {
            output = output+"\n"+final_statement.reconstruct(indent);
        } else if (type == 2) {
            output = output+"\n"+final_statement.reconstruct(indent);
        }
        return output;
    }
    public BigInteger map_to_natural() throws Exception{
        if (type != -1) {
            BigInteger value;
            value = switch (type) {
                case 1 -> BigInteger.valueOf(1).add(BigInteger.valueOf(4).multiply(Utils.phi(assigning_variable.map_to_natural(), assigning_statement.map_to_natural())));
                case 2 -> first_statement.map_to_natural();
                case 3 -> BigInteger.valueOf(3).add(BigInteger.valueOf(4).multiply(Utils.phi(boolean_statement.map_to_natural(), Utils.phi(first_statement.map_to_natural(), second_statement.map_to_natural()))));
                case 4 -> BigInteger.valueOf(4).add(BigInteger.valueOf(4).multiply(Utils.phi(boolean_statement.map_to_natural(), first_statement.map_to_natural())));
                default -> BigInteger.ZERO;
            };
            if (final_statement.type != -1) {
                return BigInteger.valueOf(2).add(BigInteger.valueOf(4).multiply(Utils.phi(value, final_statement.map_to_natural())));
            }
            return value;
        } else {
            throw new Exception("Statement of type -1 called");
        }
    }

    public String output_python(int indent) {
        String output = switch (type) {
            case 0 -> Utils.tab(indent) + "pass";
            case 1 -> Utils.tab(indent) + assigning_variable.output_python() + " = " + assigning_statement.output_python();
            case 2 -> Utils.tab(indent) + first_statement.output_python(indent);
            case 3 -> Utils.tab(indent) + "if " + boolean_statement.output_python() + ":\n" + first_statement.output_python(indent + 1) + "\n" + Utils.tab(indent) + "else:\n" + second_statement.output_python(indent + 1);
            case 4 -> Utils.tab(indent) + "while " + boolean_statement.output_python() + ":\n" + first_statement.output_python(indent + 1);
            default -> "statement";
        };
        if (type != -1 && final_statement.type != -1) {
            output = output+"\n"+final_statement.output_python(indent);
        }
        return output;
    }
}
