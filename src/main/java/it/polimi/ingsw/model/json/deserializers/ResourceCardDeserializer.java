package it.polimi.ingsw.model.json.deserializers;

import com.fasterxml.jackson.core.JacksonException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import it.polimi.ingsw.GameResource;

import java.io.IOException;
/**
 * This class implements the deserializer for lightweight resource cards.
 */
public class ResourceCardDeserializer extends StdDeserializer<ResourceCardJSON> {
    /**
     * Default constructor.
     */
    public ResourceCardDeserializer(){
        this(null);
    }

    /**
     * Constructs resource card deserializer handling specified class.
     * @param vc Type of values this deserializer handles:
     *           sometimes exact types, other time most specific supertype of types
     *           deserializer handles (which may be as generic as Object in some case)
     */
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
