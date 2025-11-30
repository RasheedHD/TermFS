import java.util.Scanner;

public class Test {
    public static void main(String[] args) {
        Scanner scnr = new Scanner(System.in);
        FileSystem fs = new FileSystem(); // Class that we'll be using throughout the program

        fs.mkdirp("a/b/c");
        fs.cd("a/b/");
        System.out.println();
        System.out.println("CURRENT DIRECTORY----------------");
        fs.pwd();
        fs.touch("file1", 1);
        fs.touch("file2", 13);
        fs.touch("file3", 12);
        fs.touch("file4", 122);
        fs.ls();
        System.out.println("DU TEST----------------");
        fs.du();
        System.out.println("DU TEST----------------");
        fs.mkdir("dir1");
        fs.cd("dir1");
        fs.pwd();
        fs.touch("file1", 2);
        fs.touch("file2", 13);
        fs.touch("file3", 23);
        fs.touch("file4", 10);
        fs.ls();
        fs.cd("..");
        fs.pwd();
        fs.du();

    }
}
