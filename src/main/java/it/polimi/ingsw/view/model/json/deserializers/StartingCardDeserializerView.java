package it.polimi.ingsw.view.model.json.deserializers;

import com.fasterxml.jackson.core.JacksonException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import it.polimi.ingsw.GameResource;
import it.polimi.ingsw.view.gui.GUIFunc;
import it.polimi.ingsw.view.model.cards.ViewCorner;
import it.polimi.ingsw.view.model.cards.ViewStartCard;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
/**
 * View resource card deserializer class.
 */
public class StartingCardDeserializerView extends StdDeserializer<ViewStartCard> {

    /**
     * Default constructor.
     */
    public StartingCardDeserializerView(){
        this(null);
    }

    /**
     * Constructs view resource card deserializer.
     */
    public StartingCardDeserializerView(Class<?> vc){
        super(vc);
    }

    @Override
    public ViewStartCard deserialize(JsonParser jsonParser, DeserializationContext deserializationContext)
            throws IOException, JacksonException {
        JsonNode node = jsonParser.getCodec().readTree(jsonParser);

        String id = node.get("cardId").asText();
        String imgFront = GUIFunc.getGraphicsResourcesRootPath() + node.get("frontImageFileName").asText();
        String imgBack = GUIFunc.getGraphicsResourcesRootPath() + node.get("backImageFileName").asText();

        // Getting Central Front Resources
        List<GameResource> centralFrontResources = new ArrayList<>();
        node.get("centralFrontResources")
                .forEach((e) -> centralFrontResources.add(GameResource.getResourceFromNameInitial(e.asText())) );

        // Instantiating Corners
        List<ViewCorner> cornerList = JsonFunctionsView.parseCorners(node);

        return new ViewStartCard(id, imgFront, imgBack, cornerList, centralFrontResources);
    }
}
