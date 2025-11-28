public class File extends Node {
    String content;
    int size;

    public File(String name, Directory parent, String content, int size) {
        super(name, parent);
        this.content = content;
        this.size = size;
    }
}
