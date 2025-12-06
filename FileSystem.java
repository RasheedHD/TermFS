import java.util.*;

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
                Node child;
                try {child = currentDirectory.getChild(token);}
                catch (NoSuchElementException e) {
                    System.out.printf("Error: Path '%s' not found.\n", path);
                    return;
                }
                if (child instanceof File) {
                    System.out.printf("Error: '%s' is not a directory.\n", token);
                    return;
                }
                assert child instanceof Directory;  // If child is not instanceof Directory, raise an error (should never happen since children can only be Files or directories, and we handled if it is a File)
                currentDirectory = (Directory) child;  // If token is a normal name, go to child
            }
        }
    }

    public void mkdir(String name) {
        Directory temp = currentDirectory;
        String[] tokens = name.split("/");
        for (String token: tokens) {
            if (currentDirectory.exists(token)) // If directory exists, go inside it
                cd(token);
            else {
                if (currentDirectory.exists(token)) {
                    System.out.printf("Error: '%s' already exists.\n", token);
                    return;
                }
                Directory newD = new Directory(token, currentDirectory);
                currentDirectory.addChild(newD);
            }
        }
        currentDirectory = temp;
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
        String result = "";
        Hashtable<String, Node> children = currentDirectory.getChildren();
        for (String key : children.keySet()) {
            if (children.get(key) instanceof Directory) // Print directory names as is
                result += (key + "/\t");
            else {
                File f = (File) children.get(key);
                int size = f.getSize();
                result += (key + " (" + size + "B)"); // Print file names with their sizes
            }
        }
        System.out.println(result);
    }

    public void touch(String name, int size) { // Adds file given size
        Directory temp = currentDirectory;
        String[] tokens = name.split("/");
        for (String token: tokens) {
            if (currentDirectory.exists(token)) // If directory exists, go inside it
                cd(token);
            else {
                File newF = new File(token, currentDirectory, size); // Create new file if no directory exists with name token
                currentDirectory.addChild(newF);
            }
        }
        currentDirectory = temp;

    }

    public void echo(String content, String name) { // Adds file given content, or writes to existing file
        Directory temp = currentDirectory;
        String[] tokens = name.split("/");
        for (String token: tokens) {
            if (currentDirectory.exists(token)) // If directory exists, go inside it
                cd(token);
            else {
                if (currentDirectory.exists(token)) {
                    File child = (File) currentDirectory.getChild(token);
                    child.setContent(content);
                }
                else {
                    File newF = new File(token, currentDirectory, content);
                    currentDirectory.addChild(newF);
                }
            }
        }
        currentDirectory = temp;
    }

    public void pwd() { // Prints the path in currentDirectory
        Directory temp = currentDirectory; // Needed to return to original directory
        if (currentDirectory == root) { // Still in the root
            System.out.print("/");
            return;
        }
        String path = "/";
        Stack<String> names = new Stack<>();
        int length;
        while (currentDirectory.getParent() != root) {
            names.push(currentDirectory.getName());
            currentDirectory = currentDirectory.getParent();
        }
        names.push(currentDirectory.getName());
        length = names.toArray().length;
        for (int i = 0; i < length; i++) {
            path = (path + names.pop() + "/");
        }
        currentDirectory = temp; // Returns to original directory
        System.out.print(path.substring(0,path.length()-1));
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
                    System.out.printf("Error: Cannot remove directory '%s'. It is not empty.\n", name); // if not empty throw error
                }
            }
          }

          else if (!found) System.out.printf("Error: '%s' not found.\n", name);
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
        System.out.println("Total size: " + diskUsage + "B");
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

    public void tree() {
    System.out.println(".");
    printer(currentDirectory, "");
}

   private void printer(Node node, String prefix) {
    Directory directory = (Directory) node;
    List<Node> children = new ArrayList<>(directory.getChildren().values()); //creating an array list of all children from the directory
    Collections.sort(children, new sortByName()); //sort BY NAME using the comparator we initialized in node.java

    for (int i = 0; i < children.size(); i++) {
        Node child = children.get(i);
        boolean isLast = (i == children.size() - 1 ? true : false); //Checking if this child is the last child of the dir or not
        String connector = isLast ? "└── " : "├── "; //if it is last the first version will be printed, otherwise second
  //the connector is the same for file or directory, connector only depends whether last/ not last
        if (child instanceof Directory) {
            System.out.println(prefix + connector + child.getName() + "/");
            String childPrefix = prefix + (isLast ? "    " : "│   ");
            printer(child, childPrefix);
        } else {
            File file = (File) child;
            System.out.println(prefix + connector + file.getName() + " (" + file.getSize() + "B)");
        }
    }
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
        while (i < text.length() && j < pattern.length()){
            if (j == -1 || (text.charAt(i) == pattern.charAt(j))){
                i++; j++;
            } 
            else  j = next[j];
        }

        if (j == pattern.length()) {found = true; j = next[j - 1];}
        else  j = next[j];
        if (found) System.out.printf("Pattern \"%s\" found in %s.%n", pattern, name);
        else System.out.printf("Pattern \"%s\" not found in %s.%n", pattern, name);
    }
    }

    private void findNext(String p, int[] next) {
        next[0] = -1;
        int i = 0;
        int j = -1;
        while (i < p.length() - 1) {
            if ((j == -1) || (p.charAt(i) == p.charAt(j))) { //j==-1 takes care of the case where the second element of a next array is always zero
                i++;
                j++;
                next[i] = j; 
            }
            
            else j = next[j];
        }
    
    }
    
}
