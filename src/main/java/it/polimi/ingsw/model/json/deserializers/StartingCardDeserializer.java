package it.polimi.ingsw.model.json.deserializers;

import com.fasterxml.jackson.core.JacksonException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import it.polimi.ingsw.model.cards.Corner;
import it.polimi.ingsw.model.cards.StartingCard;
import it.polimi.ingsw.GameResource;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
/**
 * This class implements the deserializer for starting cards.
 */
public class StartingCardDeserializer extends StdDeserializer<StartingCard> {
    /**
     * Default constructor.
     */
    public StartingCardDeserializer(){
        this(null);
    }

    /**
     * Constructs starting card deserializer handling specified class.
     * @param vc Type of values this deserializer handles:
     *           sometimes exact types, other time most specific supertype of types
     *           deserializer handles (which may be as generic as Object in some case)
     */
    public StartingCardDeserializer(Class<?> vc){
        super(vc);
    }

    @Override
    public StartingCard deserialize(JsonParser jsonParser, DeserializationContext deserializationContext)
            throws IOException, JacksonException {
        JsonNode node = jsonParser.getCodec().readTree(jsonParser);

        String id = node.get("cardId").asText();
        

        // Getting Central Front Resources
        List<GameResource> centralFrontResources = new ArrayList<>();
        node.get("centralFrontResources")
                .forEach((e) -> centralFrontResources.add(GameResource.getResourceFromName(e.asText())) );

        // Instantiating Corners
        List<Corner> cornerList = JsonFunctions.parseCorners(node);

        return new StartingCard(id, centralFrontResources, cornerList);
    }
}
