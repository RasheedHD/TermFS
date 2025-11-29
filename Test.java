import java.util.Scanner;

public class Test {
    public static void main(String[] args) {
        Scanner scnr = new Scanner(System.in);
        FileSystem fs = new FileSystem(); // Class that we'll be using throughout the program

        fs.mkdirp("a/b/c");
        fs.cd("a/b/c");
        fs.pwd();
        fs.cd("..");
        fs.pwd();
        fs.cd("/");
        fs.pwd();
    }
}
