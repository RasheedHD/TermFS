import java.util.Hashtable;
import java.util.Stack;

public class FileSystem {
    Directory root;
    Directory currentDirectory;

    public FileSystem() {
        root = new Directory("/", null);
        currentDirectory = root;
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

    public void mkdir(String name, String path) {

    }

    public void ls() {
        Hashtable<String, Node> children = currentDirectory.getChildren();
        for (String key : children.keySet()) {
            if (children.get(key) instanceof Directory)
                System.out.print(key + "/\t");
            else {
                File f = (File) children.get(key);
                int size = f.getSize();
                System.out.print(key + " (" + size + "B)");
            }
        }
        System.out.println();
    }

    public void touch(String name, int size) {
        File newF = new File(name, currentDirectory, size);
        currentDirectory.addChild(newF);
    }

    public void echo(String content, String name) {
        if (currentDirectory.exists(name)) {
            File child = (File) currentDirectory.getChild(name);
            child.setContent(content);
        }
        else {
            File newF = new File(name, currentDirectory, content);
            currentDirectory.addChild(newF);
        }
    }

    public void pwd() {
        Stack<String> names = new Stack<>();
        while (currentDirectory.getParent() != root) {
            names.push(currentDirectory.getName()); // Incomplete path building.
        }
    }



    // If path is relative: go through children
    // If path is absolute: go from the root
    // If path starts with .. (relative): go to parent
}
