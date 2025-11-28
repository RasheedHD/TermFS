import java.util.Hashtable;

public class Directory extends Node {
    Hashtable<String, Node> children;

    public Directory(String name, Directory parent) {
        super(name, parent);
    }
}
