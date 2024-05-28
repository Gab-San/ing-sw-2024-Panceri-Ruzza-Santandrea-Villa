package it.polimi.ingsw.view.model.json.deserializers;

import com.fasterxml.jackson.core.JacksonException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import it.polimi.ingsw.GameResource;
import it.polimi.ingsw.view.model.cards.ViewCorner;
import it.polimi.ingsw.view.model.cards.ViewResourceCard;

import java.io.IOException;
import java.util.List;

public class ResourceCardDeserializerView extends StdDeserializer<ViewResourceCard> {
    public ResourceCardDeserializerView(){
        this(null);
    }
    public ResourceCardDeserializerView(Class<?> vc){
        super(vc);
    }

    @Override
    public ViewResourceCard deserialize(JsonParser jsonParser, DeserializationContext deserializationContext)
            throws IOException, JacksonException {

        JsonNode node = jsonParser.getCodec().readTree(jsonParser);


        String cardId = node.get("cardId").asText();
        GameResource backResource = GameResource.getResourceFromNameInitial(node.get("backResource").asText());
        String imageFront = node.get("frontImageFileName").asText();
        String imageBack = node.get("backImageFileName").asText();
        List<ViewCorner> corners = JsonFunctionsView.parseCorners(node);
        int pointsOnPlace = node.get("pointsOnPlace").asInt();

        return new ViewResourceCard(cardId, imageFront, imageBack, corners, pointsOnPlace, backResource);
    }
}
