package it.polimi.ingsw.view.model.json.deserializers;

import com.fasterxml.jackson.core.JacksonException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import it.polimi.ingsw.GameResource;

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

        goldJ.setBackResource(GameResource.getResourceFromNameInitial(node.get("backResource").asText()));

        goldJ.setCornersJS(JsonFunctionsView.parseJsonCorners(node));

        goldJ.setPlacementCost(node.get("placementCost").asText());

        goldJ.setPointsOnPlace(node.get("pointsOnPlace").asText());

        return goldJ;
    }
}
