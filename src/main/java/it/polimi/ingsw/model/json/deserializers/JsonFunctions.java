package it.polimi.ingsw.model.json.deserializers;

import com.fasterxml.jackson.databind.JsonNode;
import it.polimi.ingsw.model.json.deserializers.CornerJ;

import java.util.ArrayList;
import java.util.List;

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
}
