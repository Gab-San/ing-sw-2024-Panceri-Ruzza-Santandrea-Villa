package it.polimi.ingsw.view.model.json.deserializers;

import com.fasterxml.jackson.core.JacksonException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import it.polimi.ingsw.GameResource;

import java.io.IOException;

public class ResourceCardDeserializerView extends StdDeserializer<ResourceCardJSONView> {
    public ResourceCardDeserializerView(){
        this(null);
    }
    public ResourceCardDeserializerView(Class<?> vc){
        super(vc);
    }

    @Override
    public ResourceCardJSONView deserialize(JsonParser jsonParser, DeserializationContext deserializationContext)
            throws IOException, JacksonException {
        JsonNode node = jsonParser.getCodec().readTree(jsonParser);

        ResourceCardJSONView resJS = new ResourceCardJSONView();

        resJS.setCardId(node.get("cardId").asText());

        resJS.setBackResource(GameResource.getResourceFromName( node.get("backResource").asText() ));

        resJS.setPointsOnPlace(node.get("pointsOnPlace").asInt());

        resJS.setCornerJS(JsonFunctionsView.parseJsonCorners(node));

        resJS.setImgFront(node.get("frontImageFileName").asText());
        resJS.setImgBack(node.get("backImageFileName").asText());

        return resJS;
    }
}
