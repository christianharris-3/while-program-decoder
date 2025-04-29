import java.math.BigInteger;
import java.util.Scanner;


public class Decoder {
    private final static String lBrace = "";//" { ";
    private final static String rBrace = "";//"} ";
    public static void main(String[] args) {
        BigInteger programValue = getValue();
        System.out.println(valueToProgram(programValue));

//        for (int i=0;i<10000;i++) {
//            System.out.println("----- code for "+i+" ------");
//            System.out.println(valueToProgram(BigInteger.valueOf(i)));
//        }
    }
    public static BigInteger getValue() {
        Scanner scanner = new Scanner(System.in);
        System.out.println("enter your int: ");
        BigInteger programValue = new BigInteger(scanner.nextLine());
        scanner.close();

        return programValue;
    }
    public static String valueToProgram(BigInteger programValue) {
        return inverse_phi_statement(programValue, 0);
    }
    public static String inverse_phi_statement(BigInteger value, int indent) {
        if (value.signum() == 0) {
            return "skip;";
        }
        BigInteger new_value = value.divide(BigInteger.valueOf(4));
        int mod_value = value.mod(BigInteger.valueOf(4)).intValue();
        if (mod_value == 0) {
            mod_value += 4;
            new_value = new_value.subtract(BigInteger.ONE);
        }
        BigInteger[] out = inverse_phi(new_value);
        switch (mod_value) {
            case 1:
                return inverse_phi_var(out[0])+":="+inverse_phi_arithmetic(out[1])+";";
            case 2:
                return inverse_phi_statement(out[0], indent)+"\n"+tab(indent)+inverse_phi_statement(out[1], indent);
            case 3:
                BigInteger[] out2 = inverse_phi(out[1]);
                return "if "+inverse_phi_bool(out[0])+" then"+lBrace+"\n"+tab(indent+1)+inverse_phi_statement(out2[0],indent+1)+"\n"+tab(indent)+rBrace+"else"+lBrace+"\n"+tab(indent+1)+inverse_phi_statement(out2[1],indent+1);//+"\n"+tab(indent)+rBrace;
            case 4:
                return "while "+inverse_phi_bool(out[0])+" do"+lBrace+"\n"+tab(indent+1)+inverse_phi_statement(out[1],indent+1)+"\n"+tab(indent)+rBrace;
        }
        return "statement broke";
    }
    public static String inverse_phi_var(BigInteger value) {
        return "x_"+value;
    }
    public static String inverse_phi_bool(BigInteger value) {
        //System.out.println("phi boolean - "+value);
        if (value.signum() == 0) {
            return "False";
        } else if (value.equals(BigInteger.ONE)) {
            return "True";
        }
        BigInteger new_value = value.divide(BigInteger.valueOf(4));
        int mod_value = value.mod(BigInteger.valueOf(4)).intValue();
        if (mod_value < 2) {
            mod_value += 4;
            new_value = new_value.subtract(BigInteger.ONE);
        }
        BigInteger[] out = inverse_phi(new_value);
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
    public static String inverse_phi_arithmetic(BigInteger value) {
        BigInteger new_value = value.divide(BigInteger.valueOf(5));
        int mod_value = value.mod(BigInteger.valueOf(5)).intValue();
        BigInteger[] out = inverse_phi(new_value);
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
    public static BigInteger[] inverse_phi(BigInteger value) {
        value = value.add(BigInteger.ONE);
        BigInteger m = BigInteger.ZERO;
        while (value.mod(BigInteger.TWO).signum() == 0) {
            m = m.add(BigInteger.ONE);
            value = value.divide(BigInteger.TWO);
        }
        BigInteger n = (value.subtract(BigInteger.ONE)).divide(BigInteger.TWO);
        return new BigInteger[] {m ,n};
    }
    public static String tab(int indent) {
        return " ".repeat(indent*4);
    }
}