import java.io.File;
import java.util.Scanner;

public class Compiler {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        System.out.println("enter your program: ");
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
        System.out.println("-----------------------");
        WhileStatement compiled = new WhileStatement(program);
        System.out.println("RECONSTRUCTED");
        System.out.println(compiled.reconstruct(0));
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
//    public static String preprocess_code(String program) {
//        String output = "";
//    }
    public static String clean_string(String program) {
        if (program.isEmpty()) {
            return "";
        }
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
}
