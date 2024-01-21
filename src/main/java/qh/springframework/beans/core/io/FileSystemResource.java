package qh.springframework.beans.core.io;


import java.io.*;
import java.nio.file.Files;

public class FileSystemResource implements Resource{

    private final File file;

    private final String path;

    public FileSystemResource(String path) {
        this.file = new File(path);
        this.path = path;
    }


    @Override
    public InputStream getInputStream() throws IOException {
        return Files.newInputStream(file.toPath());
    }

    public String getPath() {
        return path;
    }
}
