public abstract class Node {
    String name;
    Directory parent;

    public Node(String name, Directory parent) {
        this.name = name;
        this.parent = parent;
    }

    public Directory getParent() {
        return parent;
    }



    public abstract void addChild(Node node);
}
