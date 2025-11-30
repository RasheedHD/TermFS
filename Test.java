import java.util.Scanner;

public class Test {
    public static void main(String[] args) {
        Scanner scnr = new Scanner(System.in);
        FileSystem fs = new FileSystem(); // Class that we'll be using throughout the program

        while (true){
            String input = scnr.nextLine();
            String[] tokens = input.split(" "); //splitting the input by space
            switch(tokens[0]){ //so basically, in every kind of input, the first word is always the command and using a switch statement for better readability
                case "mkdir":
                    fs.mkdirp(tokens[1]);
                    break;
                case "touch":
                    fs.touch(tokens[1], Integer.parseInt(tokens[2]));
                    break;
                case "echo": //first, ill have to get the content, this ill do by locating the two quotation marks and getting the text in between them
                    int IndexOfFirstQuot = input.indexOf('"'); // first "
                    int IndexOfSecondQuot = input.lastIndexOf('"'); // second "
                    String content = input.substring(IndexOfFirstQuot + 1, IndexOfSecondQuot); //there is a +1 to exclude writing the quotation mark itself
                    String file = tokens[tokens.length - 1]; //the file name is always the last word when inputting this command
                    fs.echo(content, file);
                    break;                           
                case "ls":
                    fs.ls();
                    break;                                    //the rest are just calling the corresponding function according to input
                case "cd":
                    fs.cd(tokens[1]);
                    break;
                case "pwd":
                    fs.pwd();
                    break;
                case "rm":
                    fs.rm(tokens[1]);
                    break;
                case "rmr":
                    fs.rmr(tokens[1]);
                    break;
                case "tree":
                    break;
                case "grep":
                    break;
                case "du":
                    fs.du();
                    break;
                default:      //when the input is not one of the commands, the case is handled by printing invalid command
                    System.out.println("Invalid command");
                    break;
            }

        }

    }
}
