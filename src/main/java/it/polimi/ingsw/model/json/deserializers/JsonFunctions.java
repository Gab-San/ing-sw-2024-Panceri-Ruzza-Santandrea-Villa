package it.polimi.ingsw.model.json.deserializers;

import com.fasterxml.jackson.databind.JsonNode;
import it.polimi.ingsw.model.cards.Corner;
import it.polimi.ingsw.model.enums.CornerDirection;
import it.polimi.ingsw.model.enums.GameResource;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public interface JsonFunctions {
    public static List<CornerJ> parseJsonCorners(JsonNode node){
        List<CornerJ> cornerJS = new ArrayList<>();
        node.get("corners").forEach((e)->{
            CornerJ toAdd = new CornerJ();
            toAdd.setDirection(e.get("direction").asText());
            toAdd.setFrontResource(e.get("frontResource").asText());
            toAdd.setBackResource(e.get("backResource").asText());
            cornerJS.add(toAdd);
        });
        return cornerJS;
    }

    public static List<Corner> parseCorners(JsonNode node){
        List<Corner> corners = new ArrayList<>();
        node.get("corners").forEach(
                (e) -> {
                    CornerDirection dir = CornerDirection.getDirectionFromString(e.get("direction").asText());
                    GameResource frontRes = GameResource.getResourceFromName(e.get("frontResource").asText());
                    GameResource backRes = GameResource.getResourceFromName(e.get("backResource").asText());

                    Corner toAdd = new Corner(frontRes, backRes, dir);
                    corners.add(toAdd);
                }
        );

        return corners;
    }

    public static List<Corner> parseCorners(List<CornerJ> jCorners){
        return jCorners.stream().map(e -> new Corner(
                GameResource.getResourceFromName(e.getFrontResource()),
                GameResource.getResourceFromName(e.getBackResource()),
                CornerDirection.getDirectionFromString(e.getDirection())
        )).collect(Collectors.toList());
    }

}
