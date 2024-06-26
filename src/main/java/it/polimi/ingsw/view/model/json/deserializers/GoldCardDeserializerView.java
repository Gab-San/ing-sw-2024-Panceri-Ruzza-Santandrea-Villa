package it.polimi.ingsw.view.model.json.deserializers;

import com.fasterxml.jackson.core.JacksonException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import it.polimi.ingsw.GameResource;
import it.polimi.ingsw.view.gui.GUIFunc;
import it.polimi.ingsw.view.model.cards.ViewCorner;
import it.polimi.ingsw.view.model.cards.ViewGoldCard;

import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

/**
 * View gold card deserializer class.
 */
public class GoldCardDeserializerView extends StdDeserializer<ViewGoldCard> {

    /**
     * Default constructor
     */
    public GoldCardDeserializerView(){
        this(null);
    }

    /**
     * Constructs view gold card deserializer.
     * @param vc Type of values this deserializer handles:
     *           sometimes exact types, other time most specific supertype of types
     *           deserializer handles (which may be as generic as Object in some case)
     */
    public GoldCardDeserializerView(Class<?> vc){
        super(vc);
    }

    @Override
    public ViewGoldCard deserialize(JsonParser jsonParser, DeserializationContext deserializationContext)
            throws IOException, JacksonException {

        JsonNode node = jsonParser.getCodec().readTree(jsonParser);

        String cardId = node.get("cardId").asText();
        GameResource backResource = GameResource.getResourceFromNameInitial(node.get("backResource").asText());
        String imageFront = GUIFunc.getGraphicsResourcesRootPath() + node.get("frontImageFileName").asText();
        String imageBack = GUIFunc.getGraphicsResourcesRootPath() + node.get("backImageFileName").asText();
        List<ViewCorner> corners = JsonFunctionsView.parseCorners(node);

        String[] placementCosts = node.get("placementCost").asText().split(", ");
        List<GameResource> placementCostAsResourceList = new LinkedList<>();
        for(String cost : placementCosts){
            int rep = Integer.parseInt(cost.substring(0, 1));
            GameResource resource = GameResource.getResourceFromNameInitial(cost.substring(2));
            for (int i = 0; i < rep; i++) {
                placementCostAsResourceList.add(resource);
            }
        }

        String[] fullPointsOnPlace = node.get("pointsOnPlace").asText().split("-");
        int pointsOnPlace = Integer.parseInt(fullPointsOnPlace[0]);
        String strategyAsString = "";
        if(fullPointsOnPlace.length > 1)
            strategyAsString = fullPointsOnPlace[1];

        return new ViewGoldCard(cardId, imageFront, imageBack, corners, pointsOnPlace, backResource, placementCostAsResourceList, strategyAsString);
    }
}
