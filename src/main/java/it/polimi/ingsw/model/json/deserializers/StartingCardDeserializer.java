package it.polimi.ingsw.model.json.deserializers;

import com.fasterxml.jackson.core.JacksonException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class StartingCardDeserializer extends StdDeserializer<StartingCardJSON> {
    public StartingCardDeserializer(){
        this(null);
    }
    public StartingCardDeserializer(Class<?> vc){
        super(vc);
    }

    @Override
    public StartingCardJSON deserialize(JsonParser jsonParser, DeserializationContext deserializationContext)
            throws IOException, JacksonException {
        JsonNode node = jsonParser.getCodec().readTree(jsonParser);
        StartingCardJSON startJS = new StartingCardJSON();
        // Setting ID
        startJS.setCardId(node.get("cardId").asText());
        // Setting Images
        startJS.setFrontImageFileName(node.get("frontImageFileName").asText());
        startJS.setBackImageFileName(node.get("backImageFileName").asText());
        // Setting Central Front Resources
        List<String> centralFrontResources = new ArrayList<>();
        node.get("centralFrontResources").forEach((e) -> centralFrontResources.add(e.asText()));
        startJS.setCentralFrontResources(centralFrontResources);

        startJS.setCorners(JsonFunctions.parseJsonCorners(node));
        return startJS;
    }
}
