package it.polimi.ingsw.model.json.deserializers;

import com.fasterxml.jackson.core.JacksonException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import it.polimi.ingsw.model.cards.Corner;
import it.polimi.ingsw.model.cards.StartingCard;
import it.polimi.ingsw.model.enums.GameResource;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class StartingCardDeserializer extends StdDeserializer<StartingCard> {
    public StartingCardDeserializer(){
        this(null);
    }
    public StartingCardDeserializer(Class<?> vc){
        super(vc);
    }

    @Override
    public StartingCard deserialize(JsonParser jsonParser, DeserializationContext deserializationContext)
            throws IOException, JacksonException {
        JsonNode node = jsonParser.getCodec().readTree(jsonParser);
        // ID can be ignored since it's creating already starting cards.
        // We can just use it as a ref but has no concrete attribute.

        // TODO: Setting Images
//        startJS.setFrontImageFileName(node.get("frontImageFileName").asText());
//        startJS.setBackImageFileName(node.get("backImageFileName").asText());

        // Getting Central Front Resources
        List<GameResource> centralFrontResources = new ArrayList<>();
        node.get("centralFrontResources")
                .forEach((e) -> centralFrontResources.add(GameResource.getResourceFromName(e.asText())) );

        // Instantiating Corners
        List<Corner> cornerList = JsonFunctions.parseCorners(node);

        return new StartingCard(centralFrontResources, cornerList);
    }
}
