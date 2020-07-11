package app.illuminate.controller;

import com.fasterxml.jackson.databind.JsonNode;
import controller.GameData;
import model.*;

import static util.Constants.*;

public abstract class IlluminateGameData extends GameData {

    public IlluminateGameData(JsonNode inNode) {
        super(inNode);
    }

    @Override
    protected void mapGameDataFromJson() {
        createDetailsObject();
        mapResourcesFromJson();
        mapStationsFromJson();
        mapFoldersFromJson();
        mapEngramsFromJson();
        mapCompositionFromJson();
        mapCompositesFromJson();
        mapDirectoryFromJson();
    }

    @Override
    protected void mapFoldersFromJson() {
        JsonNode foldersNode = inNode.get(cFolders);
        for (JsonNode folderNode : foldersNode) {
            Folder folder = Folder.fromJson(folderNode);
            addFolder(folder);
        }
    }

    @Override
    protected void mapResourcesFromJson() {
        JsonNode resourcesNode = inNode.get(cResources);
        for (JsonNode resourceNode : resourcesNode) {
            Resource resource = Resource.fromJson(resourceNode);
            addResource(resource);
        }
    }

    @Override
    protected void mapEngramsFromJson() {
        JsonNode engramsNode = inNode.get(cEngrams);
        for (JsonNode engramNode : engramsNode) {
            Engram engram = Engram.fromJson(engramNode);
            addEngram(engram);
        }
    }

    @Override
    protected void mapStationsFromJson() {
        JsonNode stationsNode = inNode.get(cStations);
        for (JsonNode stationNode : stationsNode) {
            Station station = Station.fromJson(stationNode);
            addStation(station);
        }
    }

    @Override
    protected void mapCompositionFromJson() {
        JsonNode compositionsNode = inNode.get(cComposition);
        for (JsonNode compositionNode : compositionsNode) {
            Composition composition = Composition.fromJson(compositionNode);
            String compositionName = getEngramNameByUUID(composition.getEngramId());
            addComposition(compositionName, composition);
        }
    }

    protected void mapCompositesFromJson() {
        JsonNode compositesArrayNode = inNode.get(cComposites);
        for (JsonNode compositeArrayNode : compositesArrayNode) {
            for (JsonNode compositeNode : compositeArrayNode) {
                Composite composite = Composite.fromJson(compositeNode);
                addComposite(composite);
            }
        }
    }

    @Override
    protected void mapDirectoryFromJson() {
        JsonNode directoryNode = inNode.get(cDirectory);
        for (JsonNode directoryItemNode : directoryNode) {
            DirectoryItem directoryItem = DirectoryItem.fromJson(directoryItemNode);
            addDirectoryItem(directoryItem);
        }
    }

    public String getFilePathForResources() {
        return getDetailsObject().getFilePath().concat(cIlluminateResourcesFileName);
    }

    public String getFilePathForDirectory() {
        return getDetailsObject().getFilePath().concat(cIlluminateDirectoryFileName);
    }
}
