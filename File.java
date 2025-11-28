public class File extends Node {
    String content;
    int size;

    public File(String name, Directory parent, int size) {
        super(name, parent);
        this.size = size;
        this.content = "";
    }

    public File(String name, Directory parent, String content) {
        super(name, parent);
        this.content = content;
        this.size = content.length();
    }

    public void addChild(Node node) {}

    public int getSize() {
        return size;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
