package util;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class JSONUtil {


    public static JsonNode parseIn(String filename) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        InputStream inputStream = ClassLoader.getSystemResourceAsStream(filename);
        return mapper.readTree(inputStream);
    }

    public static void writeOut(String filePath, JsonNode outObject) throws IOException {
        Path path = Paths.get(filePath);
        if (Files.notExists(path.getParent())) {
            Files.createDirectory(path.getParent());
        }

        ObjectMapper mapper = new ObjectMapper();
        mapper.writeValue(new File(filePath), outObject);
    }
}
