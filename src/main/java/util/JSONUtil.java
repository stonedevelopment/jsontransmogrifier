package util;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import static util.Constants.cAssetsFilePath;

public class JSONUtil {
    public static JsonNode parseIn(String filePath) throws IOException {
        Path path = Paths.get(cAssetsFilePath.concat(filePath));
        ObjectMapper mapper = new ObjectMapper();
        return mapper.readTree(path.toFile());
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
