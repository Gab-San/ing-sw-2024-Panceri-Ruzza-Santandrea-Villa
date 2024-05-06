package it.polimi.ingsw.view;

import com.fasterxml.jackson.core.JacksonException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import it.polimi.ingsw.model.enums.GameResource;
import it.polimi.ingsw.model.json.deserializers.JsonFunctions;
import it.polimi.ingsw.model.json.deserializers.PointOnPlace;

import java.io.IOException;

public class GoldCardDeserializerView extends StdDeserializer<GoldCardJSONView> {

    public GoldCardDeserializerView(){
        this(null);
    }

    public GoldCardDeserializerView(Class<?> vc){
        super(vc);
    }

    @Override
    public GoldCardJSONView deserialize(JsonParser jsonParser, DeserializationContext deserializationContext)
            throws IOException, JacksonException {

        JsonNode node = jsonParser.getCodec().readTree(jsonParser);

        GoldCardJSONView goldJ = new GoldCardJSONView();

        goldJ.setCardId(node.get("cardId").asText());

        goldJ.setBackResource(GameResource.getResourceFromName(node.get("backResource").asText()));

        goldJ.setColour(node.get("colour").asText());

        goldJ.setCornersJS( JsonFunctions.parseJsonCorners(node)  );

        goldJ.setPlacementCost(JsonFunctions.parsePlacementCost(node));

        PointOnPlace pp = new PointOnPlace();
        pp.setType(node.get("pointsOnPlace").get("type").asText());
        pp.setAmount(node.get("pointsOnPlace").get("amount").asInt());

        goldJ.setPointsOnPlace(pp);

        return goldJ;
    }
}
