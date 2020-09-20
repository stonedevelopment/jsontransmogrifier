package app.illuminate.model.details;

import app.transmogrify.model.details.TransmogDetails;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import model.details.Details;

import java.util.Date;

import static util.Constants.*;

public class IlluminateDetails extends TransmogDetails {
    private final ArrayNode illuminatedFiles = new ObjectMapper().createArrayNode();

    @JsonCreator
    public IlluminateDetails(@JsonProperty(cUuid) String uuid,
                             @JsonProperty(cName) String name,
                             @JsonProperty(cDescription) String description,
                             @JsonProperty(cFilePath) String filePath,
                             @JsonProperty(cLogoFile) String logoFile,
                             @JsonProperty(cFolderFile) String folderFile,
                             @JsonProperty(cBackFolderFile) String backFolderFile) {
        super(uuid, name, description, filePath, logoFile, folderFile, backFolderFile);
    }

    public static IlluminateDetails from(JsonNode jsonNode) {
        return new ObjectMapper().convertValue(jsonNode, IlluminateDetails.class);
    }

    public JsonNode toJson() {
        return new ObjectMapper().valueToTree(this);
    }

    @JsonIgnore
    @Override
    public String getUuid() {
        return super.getUuid();
    }

    @JsonIgnore
    @Override
    public Date getLastUpdated() {
        return super.getLastUpdated();
    }

    @JsonIgnore
    public JsonNode getIlluminatedFiles() {
        return illuminatedFiles;
    }

    public void addIlluminatedFile(String type, String illuminatedFile) {
        ObjectNode illuminatedNode = new ObjectMapper().createObjectNode();
        illuminatedNode.put(cType, type);
        illuminatedNode.put(cFilePath, illuminatedFile);
        illuminatedFiles.add(illuminatedNode);
    }
}