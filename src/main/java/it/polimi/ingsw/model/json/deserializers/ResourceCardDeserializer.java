package it.polimi.ingsw.model.json.deserializers;

import com.fasterxml.jackson.core.JacksonException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import it.polimi.ingsw.model.enums.GameResource;

import java.io.IOException;

public class ResourceCardDeserializer extends StdDeserializer<ResourceCardJSON> {
    public ResourceCardDeserializer(){
        this(null);
    }
    public ResourceCardDeserializer(Class<?> vc){
        super(vc);
    }

    @Override
    public ResourceCardJSON deserialize(JsonParser jsonParser, DeserializationContext deserializationContext)
            throws IOException, JacksonException {
        JsonNode node = jsonParser.getCodec().readTree(jsonParser);

        ResourceCardJSON resJS = new ResourceCardJSON();

        resJS.setCardId(node.get("cardId").asText());

        resJS.setBackResource(GameResource.getResourceFromName( node.get("backResource").asText() ));

        resJS.setPointsOnPlace(node.get("pointsOnPlace").asInt());


        resJS.setCornerJS(JsonFunctions.parseJsonCorners(node));
        return resJS;
    }
}
