import java.util.Comparator;

public abstract class Node {
    String name;
    Directory parent;

    public Node(String name, Directory parent) {
        this.name = name;
        this.parent = parent;
    }

 // Getters
    public Directory getParent() {
        return parent;
    }

    public String getName() {
        return this.name;
    }


    public abstract void addChild(Node node); // Abstract method used solely for Directories
}

class sortByName implements Comparator<Node>{

    @Override
    public int compare(Node a, Node b){ //this was added for the tree method so we can sort the nodes by name
        return a.getName().compareTo(b.getName());
    }
}