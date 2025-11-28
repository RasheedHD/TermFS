import java.util.Hashtable;

public class Directory extends Node {
    Hashtable<String, Node> children;

    public Directory(String name, Directory parent) {
        super(name, parent);
    }

    public Node getChild(String name) {
        return children.get(name);
    }

    public void addChild(Node node) {
        children.put(node.name, node);
    }
}
