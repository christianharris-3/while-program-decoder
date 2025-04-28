import java.util.Arrays;

public class WhileStatement {
    public int type = -1;
    public WhileStatement first_statement;
    public WhileStatement second_statement;
    public WhileStatement final_statement;
    public WhileVariable assigning_variable;
    public WhileArithmetic assigning_statement;
    public WhileBoolean boolean_statement;


    public WhileStatement(String statement) {
        statement = Compiler.clean_string(statement);

        if (statement.startsWith("if ")) {
            type = 3;
            String[] split = sep_curley_bracket(statement);

            String boolString = Compiler.clean_string(Compiler.remove_suffix(Compiler.remove_prefix(split[0],"if"),"then"));
            String bodyString = Compiler.clean_string(split[1]);
            String elseString = Compiler.clean_string(Compiler.remove_prefix(split[2],"}"));
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

            String boolString = Compiler.clean_string(Compiler.remove_suffix(Compiler.remove_prefix(split[0],"while"),"do"));
            String bodyString = Compiler.clean_string(split[1]);
            String finalString = Compiler.clean_string(split[1]);

            boolean_statement = new WhileBoolean(boolString);
            first_statement = new WhileStatement(bodyString);
            final_statement = new WhileStatement(finalString);
        } else {
            String[] lines = statement.split(";",2);
            if (lines[0].contains(":=")) {
                type = 1;
                String[] split = lines[0].split(":=",2);
                String varString = Compiler.clean_string(split[0]);
                String arithString = Compiler.clean_string(split[1]);
                assigning_variable = new WhileVariable(varString);
                assigning_statement = new WhileArithmetic(arithString);

                if (lines.length > 1) {
                    final_statement = new WhileStatement(lines[1]);
                } else {
                    final_statement = new WhileStatement("");
                }

            }
        }
    }
    private static String[] sep_curley_bracket(String program) {
        String[] output = new String[3];
        String[] sep = program.split("\\{",2);
        output[0] = Compiler.clean_string(sep[0]);
        StringBuilder builder = new StringBuilder();
        int bracket_counter = 0;
        for (int i=0;i<sep[1].length();i++) {
            if (sep[1].charAt(i) == '{') {
                bracket_counter += 1;
            } else if (sep[1].charAt(i) == '}') {
                bracket_counter -= 1;
            }
            if (bracket_counter == -1) {
                output[1] = Compiler.clean_string(builder.toString());
                output[2] = Compiler.clean_string(sep[1].substring(i+1));
                return output;
            } else {
                builder.append(sep[1].charAt(i));
            }
        }

        return output;
    }

    public String reconstruct(int indent) {
        String output;
        switch (type) {
            case 1:
                output = Compiler.tab(indent)+assigning_variable.reconstruct()+":="+assigning_statement.reconstruct()+";";
                break;
            case 3:
                output = Compiler.tab(indent)+"if "+boolean_statement.reconstruct()+" then {\n"+first_statement.reconstruct(indent+1)+"\n"+Compiler.tab(indent)+"} else {\n"+second_statement.reconstruct(indent+1)+"\n"+Compiler.tab(indent)+"}";
                break;
            case 4:
                output = Compiler.tab(indent)+"while "+boolean_statement.reconstruct()+" do {\n"+first_statement.reconstruct(indent+1)+"\n"+Compiler.tab(indent)+"}";
                break;
            default:
                output = "";
        }
        if (type != -1 && final_statement.type != -1) {
            output = output+"\n"+final_statement.reconstruct(indent);
        }
        return output;
    }
}
