import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        int programValue = getValue();

        System.out.println(valueToProgram(programValue));
    }
    public static int getValue() {
        Scanner scanner = new Scanner(System.in);
        System.out.println("enter your int: ");
        int programValue = scanner.nextInt();
        scanner.close();

        return programValue;
    }
    public static String valueToProgram(int programValue) {
        return inverse_phi_statement(programValue, 0);
    }
    public static String inverse_phi_statement(int value, int indent) {
        if (value == 0) {
            return "skip";
        }
        int new_value = value/4;
        int mod_value = value%4;
        if (mod_value == 0) {
            mod_value += 4;
            new_value -= 1;
        }
        int[] out = inverse_phi(new_value);
        switch (mod_value) {
            case 1:
                return inverse_phi_var(out[0])+":="+inverse_phi_arithmetic(out[1]);
            case 2:
                return inverse_phi_statement(out[0], indent)+";\n"+tab(indent)+inverse_phi_statement(out[1], indent);
            case 3:
                int[] out2 = inverse_phi(out[1]);
                return "if "+inverse_phi_bool(out[0])+" then {\n"+tab(indent+1)+inverse_phi_statement(out2[0],indent+1)+"\n"+tab(indent)+"} else {\n"+tab(indent+1)+inverse_phi_statement(out2[1],indent+1)+"\n}\n";
            case 4:
                return "while "+inverse_phi_bool(out[0])+" do {\n"+tab(indent+1)+inverse_phi_statement(out[1],indent+1)+"\n"+tab(indent)+"}";
        }
        return "statement broke";
    }
    public static String inverse_phi_var(int value) {
        return "x_"+value;
    }
    public static String inverse_phi_bool(int value) {
        if (value == 0) {
            return "False";
        } else if (value == 1) {
            return "True";
        }
        int new_value = value/4;
        int mod_value = value%4;
        if (mod_value < 2) {
            mod_value += 4;
            new_value -= 1;
        }
        int[] out = inverse_phi(new_value);
        switch (mod_value) {
            case 2:
                return inverse_phi_arithmetic(out[0])+"="+inverse_phi_arithmetic(out[1]);
            case 3:
                return inverse_phi_arithmetic(out[0])+"<="+inverse_phi_arithmetic(out[1]);
            case 4:
                return "¬"+inverse_phi_bool(new_value);
            case 5:
                return inverse_phi_bool(out[0])+"∧"+inverse_phi_bool(out[1]);
        }
        return "bool broke";
    }
    public static String inverse_phi_arithmetic(int value) {
        int new_value = value/5;
        int mod_value = value%5;
        int[] out = inverse_phi(new_value);
        switch (mod_value) {
            case 0:
                return String.valueOf(new_value);
            case 1:
                return inverse_phi_var(new_value);
            case 2:
                return inverse_phi_arithmetic(out[0])+"+"+inverse_phi_arithmetic(out[1]);
            case 3:
                return inverse_phi_arithmetic(out[0])+"-"+inverse_phi_arithmetic(out[1]);
            case 4:
                return inverse_phi_arithmetic(out[0])+"*"+inverse_phi_arithmetic(out[1]);
        }
        return "arith broke";
    }
    public static int[] inverse_phi(int value) {
        value += 1;
        int m = 0;
        while (value%2 == 0) {
            m+=1;
            value/=2;
        }
        int n = (value-1)/2;
        return new int[] {m ,n};
    }
    public static String tab(int indent) {
        return " ".repeat(indent*4);
    }
}