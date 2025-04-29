import java.math.BigInteger;

public class Utils {
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
    public static String clean_string(String program) {
        if (program.isEmpty()) {
            return "";
        }

        String[] lines = program.split("\n");
        StringBuilder builder = new StringBuilder();
        for (String line: lines) {
            if (!line.replace(" ","").isEmpty()) {
                builder.append(line);
                builder.append("\n");
            }
        }
        program = builder.toString();
        lines = program.split("\n");

        boolean all_indented = true;
        while (all_indented) {
            for (String line: lines) {
                if (get_indent(line) == 0) {
                    all_indented = false;
                }
            }
            if (all_indented) {
                builder = new StringBuilder();
                for (String line: lines) {
                    builder.append(remove_prefix(line,"    "));
                    builder.append("\n");
                }
                program = builder.toString();
                lines = program.split("\n");
            }
        }
        program = program.replace("≔",":=");
        program = program.replace("×","*");
        program = program.replace("−","-");

        int i = 0;
        int j = program.length();
        while (program.charAt(i) == ' ' || program.charAt(i) == '\n') {
            i += 1;
            if (i>=program.length()) {
                return "";
            }
        }
        while (program.charAt(j-1) == ' ' || program.charAt(j-1) == '\n') {
            j -= 1;
        }
        return program.substring(i, j);
    }
    public static String remove_prefix(String str, String prefix) {
        if (str.startsWith(prefix)) {
            return str.substring(prefix.length());
        }
        return str;
    }
    public static String remove_suffix(String str, String suffix) {
        if (str.endsWith(suffix)) {
            return str.substring(0,str.length()-suffix.length());
        }
        return str;
    }
    public static int get_indent(String line) {
        int i = 0;
        while (line.startsWith("    ")) {
            i+=1;
            line = remove_prefix(line, "    ");
        }
        return i;
    }
    public static BigInteger phi(BigInteger m, BigInteger n) {

        BigInteger result = BigInteger.ONE;
        BigInteger base = BigInteger.TWO;

        while (m.signum() > 0) {
            if (m.testBit(0)) {
                result = result.multiply(base);
            }
            try {
                base = base.multiply(base);
            } catch (ArithmeticException e) {
                System.out.println("error happened when multiplying base, log10(base) = "+base.toString().length()+" error: "+e);
            }

            m = m.shiftRight(1);
        }

        return result.multiply(n.shiftLeft(1).add(BigInteger.ONE)).subtract(BigInteger.ONE);
    }
}
