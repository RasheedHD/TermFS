import java.util.Scanner;

public class Test {
    public static void main(String[] args) {
        Scanner scnr = new Scanner(System.in);
        FileSystem fs = new FileSystem(); // Class that we'll be using throughout the program

        fs.root.addChild(new Directory("a", fs.root));
        fs.root.addChild(new Directory("b", fs.root));
        fs.root.getChild("a").addChild(new Directory("1", (Directory) fs.root.getChild("a")));
        fs.changeDirectory("a");
        fs.changeDirectory("../b");
        System.out.println(fs.currentDirectory);
    }
}
