package it.polimi.ingsw.model.json.deserializers;

import com.fasterxml.jackson.core.JacksonException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import it.polimi.ingsw.GameResource;

import java.io.IOException;

/**
 * This class implements the deserializer for lightweight gold cards.
 */
public class GoldCardDeserializer extends StdDeserializer<GoldCardJSON> {
    /**
     * Default constructor.
     */
    public GoldCardDeserializer(){
        this(null);
    }

    /**
     * Constructs a gold card deserializer
     * @param vc Type of values this deserializer handles:
     *           sometimes exact types, other time most specific supertype of types
     *           deserializer handles (which may be as generic as Object in some case)
     */
    public GoldCardDeserializer(Class<?> vc){
        super(vc);
    }

    @Override
    public GoldCardJSON deserialize(JsonParser jsonParser, DeserializationContext deserializationContext)
            throws IOException, JacksonException {

        JsonNode node = jsonParser.getCodec().readTree(jsonParser);

        GoldCardJSON goldJ = new GoldCardJSON();

        goldJ.setCardId(node.get("cardId").asText());

        goldJ.setBackResource(GameResource.getResourceFromName(node.get("backResource").asText()));

        goldJ.setCornersJS( JsonFunctions.parseJsonCorners(node)  );

        goldJ.setPlacementCost(JsonFunctions.parsePlacementCost(node));

        PointOnPlace pp = new PointOnPlace();
        pp.setType(node.get("pointsOnPlace").get("type").asText());
        pp.setAmount(node.get("pointsOnPlace").get("amount").asInt());

        goldJ.setPointsOnPlace(pp);

        return goldJ;
    }
}
