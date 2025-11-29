import java.util.Hashtable;
import java.util.NoSuchElementException;

public class Directory extends Node {
    Hashtable<String, Node> children;

    public Directory(String name, Directory parent) {
        super(name, parent);
        children = new Hashtable<>();
    }

    public Node getChild(String name) {
        if (exists(name))
            return children.get(name);
        else
            throw new NoSuchElementException("File or Directory with this name " + name + " doesn't exist."); // Throw exception if child doesn't exist
    }

    public void addChild(Node node) {
        children.put(node.name, node);
    }

    public void removeChild(String name) {
        children.remove(name);
    }

    public boolean exists(String name) { // True if string name is in keys, false otherwise
        return children.containsKey(name);
    }

    public String isFileorDir(Node node) { // Returns F for files and D for directories // Delete if unused
        if (node instanceof File)
            return "F";
        else if (node instanceof Directory)
            return "D";
        else
            return "Error";
    }

    public boolean isEmpty(){ //TO CHECK IF THE DIRECTORY IS EMPTY OR NOT
        return children.isEmpty();
    }

    public Hashtable<String, Node> getChildren() {
        return children;
    }

    @Override
    public String toString() {
        return name; // For testing purposes
    }
}
