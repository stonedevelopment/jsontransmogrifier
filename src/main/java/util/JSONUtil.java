package util;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import static util.Constants.cAssetsFilePath;

public class JSONUtil {


    public static JsonNode parseIn(String filename) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        InputStream inputStream = ClassLoader.getSystemResourceAsStream(filename);
        return mapper.readTree(inputStream);
    }

    public static void writeOut(String filePath, JsonNode outObject) throws IOException {
        Path path = Paths.get(cAssetsFilePath.concat(filePath));
        if (Files.notExists(path.getParent())) {
            Files.createDirectory(path.getParent());
        }
        ObjectMapper mapper = new ObjectMapper();
        mapper.writeValue(path.toFile(), outObject);
    }
}
