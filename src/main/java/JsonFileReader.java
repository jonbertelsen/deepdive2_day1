import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.stream.Collectors;

public class JsonFileReader {

    public static String readJsonFromFile(String filename) {
        // Get the resource file from the classpath
        InputStream inputStream = JsonFileReader.class.getClassLoader().getResourceAsStream(filename);

        if (inputStream == null) {
            throw new IllegalArgumentException("File not found: " + filename);
        }

        // Read the file content into a string
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream, StandardCharsets.UTF_8))) {
            return reader.lines().collect(Collectors.joining("\n"));
        } catch (IOException e) {
            throw new RuntimeException("Failed to read the file: " + filename, e);
        }
    }
}
