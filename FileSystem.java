public class FileSystem {
    Directory root;
    Directory currentDirectory;

    public FileSystem() {
        root = new Directory("/", null);
        currentDirectory = root;
    }

    public void changeDirectory(String path) {
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



    // If path is relative: go through children
    // If path is absolute: go from the root
    // If path starts with .. (relative): go to parent
}
