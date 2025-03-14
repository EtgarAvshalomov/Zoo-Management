package file_connection;

import java.io.File;

// Abstract class for file management
public abstract class FileManager {

    // Static method to delete a file
    public static boolean deleteFile(String filePath) {
        File file = new File(filePath);
        if (file.exists()) {
            return file.delete(); // Returns true if the file was successfully deleted
        }
        return true; // File doesn't exist, so no need to delete
    }
}