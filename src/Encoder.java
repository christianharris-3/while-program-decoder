import java.io.File;
import java.math.BigInteger;
import java.util.Arrays;
import java.util.Scanner;

public class Encoder {
    public static void main(String[] args) throws Exception{
        Scanner scanner = new Scanner(System.in);
        System.out.println("enter your program's filename: ");
        String file = scanner.nextLine();
        scanner.close();
        String program;
        if (file.endsWith(".txt")) {
            program = read_file(file);
        } else {
            program = read_file(file+".txt");
        }

        System.out.println("---- program input ----");
        System.out.println(clean_string(program));
        WhileStatement compiled = new WhileStatement(program);
        System.out.println("---- RECONSTRUCTED ----");
        System.out.println(compiled.reconstruct(0));
        System.out.println("------ loading... -----");
        System.out.println("value is: "+compiled.map_to_natural());
    }
    public static String read_file(String file) {
        String program = "";
        try {
            Scanner fileReader = new Scanner(new File(file));
            while (fileReader.hasNextLine()) {
                if (program.isEmpty()) {
                    program = fileReader.nextLine();
                } else {
                    program = program + "\n"+ fileReader.nextLine();
                }
            }
        } catch (java.io.FileNotFoundException e) {
            System.out.println("error loading file: "+file);
        }
        return program;
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
    public static String tab(int indent) {
        return " ".repeat(indent*4);
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
