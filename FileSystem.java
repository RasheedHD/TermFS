import java.util.Hashtable;
import java.util.Stack;
import java.util.List;
import java.util.ArrayList;
import java.util.Collections;

public class FileSystem { // Most functions don't allow a path to be passed in, needs fixing later.
    Directory root;
    Directory currentDirectory;

    public FileSystem() {
        root = new Directory("/", null); // Root is a directory with name "/" and null parent
        currentDirectory = root; // In the start, currentDirectory points to root
    }

    public void cd(String path) {
        char firstChar = path.charAt(0);
        if (firstChar == '/') // Start at root if absolute path
            currentDirectory = root;
        String[] tokens = path.split("/");
        for (String token : tokens) {
            if (token.equals("..")) // If token is a parent, move up
                currentDirectory = currentDirectory.getParent();
            else if (token.equals(".") || token.isEmpty()); // If token is . or empty, skip
            else {
                currentDirectory = (Directory) currentDirectory.getChild(token);  // If token is a normal name, go to child
            }
        }
    }

    public void mkdir(String name) {
        Directory newD = new Directory(name, currentDirectory);
        currentDirectory.addChild(newD);
    }

    public void mkdirp(String path) {
        Directory temp = currentDirectory; // Needed to go back to original directory
        String[] tokens = path.split("/");
        for (String token: tokens) {
            if (currentDirectory.exists(token)) // If directory exists, go inside it
                cd(token);
            else {
                mkdir(token); // Create new directory in currentDirectory
                cd(token); // Go inside it to be able to create a new one for future tokens (changes original directory)
            }
        }
        currentDirectory = temp; // Returns to original directory
    }

    public void ls() { // Lists all files/directories in current directory
        Hashtable<String, Node> children = currentDirectory.getChildren();
        for (String key : children.keySet()) {
            if (children.get(key) instanceof Directory) // Print directory names as is
                System.out.println(key + "/\t");
            else {
                File f = (File) children.get(key);
                int size = f.getSize();
                System.out.println(key + " (" + size + "B)"); // Print file names with their sizes
            }
        }
        System.out.println();
    }

    public void touch(String name, int size) { // Adds file given size
        Directory temp = currentDirectory;
        File newF = new File(name, currentDirectory, size);
        currentDirectory.addChild(newF);
    }

    public void echo(String content, String name) { // Adds file given content, or writes to existing file
        if (currentDirectory.exists(name)) {
            File child = (File) currentDirectory.getChild(name);
            child.setContent(content);
        }
        else {
            File newF = new File(name, currentDirectory, content);
            currentDirectory.addChild(newF);
        }
    }

    public void pwd() { // Prints the path in currentDirectory
        Directory temp = currentDirectory; // Needed to return to original directory
        if (currentDirectory == root) { // Still in the root
            System.out.println("/");
            return;
        }
        String path = "/";
        Stack<String> names = new Stack<>();
        int length;
        while (currentDirectory.getParent() != root) {
            names.push(currentDirectory.getName()); // Incomplete path building.
            currentDirectory = currentDirectory.getParent();
        }
        names.push(currentDirectory.getName());
        length = names.toArray().length;
        for (int i = 0; i < length; i++) {
            path = (path + names.pop() + "/");
        }
        currentDirectory = temp; // Returns to original directory
        System.out.println(path.substring(0,path.length()-1));
    }

    public void rm(String name){ //removes a file or an empty directory

          boolean found = currentDirectory.exists(name); //checking if the file or dir exists

          if (found){
            Node node1 = currentDirectory.getChild(name);
            String FileorDir = currentDirectory.isFileorDir(node1); //Checking if the input is a file or directory

            if (FileorDir.equals("F")){ //If its a file then just straight up delete it 
                currentDirectory.removeChild(name);
            }
            else if (FileorDir.equals("D")){ //however if its a directory youll have to check if it's empty
                Directory dir = (Directory) node1;
                if (dir.isEmpty()){
                    currentDirectory.removeChild(name); //if its empty then just delete it 
                }
                else{
                    System.out.println("The directory is not empty!"); // if not empty throw errow
                }
            }
          }

          else if (!found) System.out.println("Such a file or directory was not found");
          //i said !found for purposes of better readability
    }

    public void rmr(String name){
        if (!currentDirectory.exists(name)) System.out.println("The file or directory does not exist");

        Node node1 = currentDirectory.getChild(name);
        rmr_helper(node1); // recursive helper
        currentDirectory.removeChild(name); //removing the target directory itself after deleting all the stuff inside
    }

    private void rmr_helper(Node node){
        String FileorDir = currentDirectory.isFileorDir(node); //Checking if the input is a file or directory

            if (FileorDir.equals("F")){  //base case
                return;
            }
            else if (FileorDir.equals("D")){
                Directory dir = (Directory) node;

                for (Node child : dir.getChildren().values()){ //clearing out the contents of each directories recursively
                    rmr_helper(child);
                }

                dir.getChildren().clear(); //then finally clearing the directories themselves
            }
    }

    public void du(){
        Directory dir = currentDirectory;
        int diskUsage = du_helper(currentDirectory);
        System.out.println("Disk Usage: " + diskUsage + "B");
    }

    public int du_helper(Node node){
        int size = 0;
        String FileorDir = currentDirectory.isFileorDir(node);


        if (FileorDir.equals("F")){
                File file = (File) node;  //base case
                size += file.getSize();
            }
        else if (FileorDir.equals("D")){
                Directory dir = (Directory) node;
                for (Node child : dir.getChildren().values()){ 
                    size += du_helper(child);
                }
        }
        return size;
    }

    public void tree(){
        printer(currentDirectory);
    }

    private void printer(Node node){
        System.out.println(currentDirectory.getName());    
        
    }

    public void grep(String pattern, String name) {

        Node file = currentDirectory.getChild(name);

        if (currentDirectory.isFileorDir(file) == "D") System.out.println("THIS IS A DIRECTORY AND NOT A FILE");  //error handle
    else{
        String text = ((File) file).getContent(); //extracting the text inside the file

        int[] next = new int[pattern.length()]; // initializing a next array
        boolean found = false; //default case is the pattern not being found
        findNext(pattern, next); // we call the helper method findNext to get the next array

        int i = 0;
        int j = 0;
        while (i <= (text.length() - pattern.length())) {
            while ((j == -1) || (j < pattern.length() && text.charAt(i) == pattern.charAt(j))) {
                i++;
                j++;
            }

            if (j == pattern.length()) {
                found = true;
                j = next[j - 1]; 
            } else {
                j = next[j];
            }
        }
        if (found) System.out.println("Match found");
        else System.out.println("Match not found");
    }
    }

    private void findNext(String p, int[] next) {
        next[0] = -1;
        int i = 0;
        int j = -1;
        while (i < p.length() - 1) {
            while ((j == -1) || (p.charAt(i) == p.charAt(j))) { //j==-1 takes care of the case where the second element of a next array is always zero
                i++;
                j++;
                next[i] = j; 
            }
            
            j = next[j];
        }
    
    }
    
}
