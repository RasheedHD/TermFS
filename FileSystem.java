public class FileSystem {
    Directory root;
    Directory currentDirectory;

    public FileSystem() {
        root = new Directory("/", null);
        currentDirectory = root;
    }



}
