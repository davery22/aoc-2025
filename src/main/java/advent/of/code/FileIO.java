package advent.of.code;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Paths;

public class FileIO {
    private FileIO() {}
    
    public static String read(String resourcePath) throws URISyntaxException, IOException {
        return Files.readString(Paths.get(FileIO.class.getResource(resourcePath).toURI()));
    }
}
