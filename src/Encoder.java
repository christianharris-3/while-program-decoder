import java.io.File;
import java.math.BigInteger;
import java.util.Scanner;

public class Encoder {
    public static void main(String[] args) throws Exception{
        Scanner scanner = new Scanner(System.in);
        System.out.println("enter your program's file: ");
        String file = scanner.nextLine();
        scanner.close();

        String code = read_file(file);
        WhileStatement program = load_program(code);

        System.out.println("---- program input ----");
        System.out.println(code);
        System.out.println("---- RECONSTRUCTED ----");
        System.out.println(program.reconstruct(0));
        System.out.println("------ loading... -----");
        System.out.println("value is: "+program.map_to_natural());
    }
    public static String read_file(String file) {
        if (!file.endsWith(".txt")) {
            file = file+".txt";
        }
        StringBuilder builder = new StringBuilder();
        try {
            Scanner fileReader = new Scanner(new File(file));
            while (fileReader.hasNextLine()) {
                if (builder.isEmpty()) {
                    builder.append(fileReader.nextLine());
                } else {
                    builder.append("\n");
                    builder.append(fileReader.nextLine());
                }
            }
        } catch (java.io.FileNotFoundException e) {
            builder.append(Utils.remove_suffix(file, ".txt"));
//            try {
//                String file_name= Utils.remove_suffix(file, ".txt");
//                new BigInteger(file_name);
//                builder.append(file_name);
//            } catch (NumberFormatException formatException) {
//                System.out.println("error loading file: "+file);
//            }
        }
        return builder.toString();
    }
    public static WhileStatement load_program(String code) throws Exception {
        WhileStatement program;
        try {
            BigInteger value = new BigInteger(code);
            program = new WhileStatement(value);
        } catch (NumberFormatException e) {
            program = new WhileStatement(code);
        }
        return program;
    }
}
