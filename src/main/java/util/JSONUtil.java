package util;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class JSONUtil {
    public static JsonNode parseIn(String assetsFilePath, String inFilePath) {
        try {
            Path path = Paths.get(assetsFilePath.concat(inFilePath));
            ObjectMapper mapper = new ObjectMapper();
            return mapper.readTree(path.toFile());
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static void writeOut(String assetsFilePath, String outFilePath, JsonNode jsonNode) {
        try {
            Path path = Paths.get(assetsFilePath.concat(outFilePath));
            if (Files.notExists(path.getParent())) {
                Files.createDirectory(path.getParent());
            }

            ObjectMapper mapper = new ObjectMapper();
            mapper.writeValue(path.toFile(), jsonNode);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}