package it.polimi.ingsw.view.model.json.deserializers;

import com.fasterxml.jackson.databind.JsonNode;
import it.polimi.ingsw.CornerDirection;
import it.polimi.ingsw.GameResource;
import it.polimi.ingsw.view.model.cards.ViewCorner;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

/**
 * This class is a helper for json functions.
 */
public interface JsonFunctionsView {

    /**
     * Parses view corners from the given json node.
     * @param node the node of the card that is being imported
     * @return a list of that card's ViewCorners
     */
    static List<ViewCorner> parseCorners(@NotNull JsonNode node){
        List<ViewCorner> corners = new ArrayList<>();
        node.get("corners").forEach(
                (e) -> {
                    CornerDirection dir = CornerDirection.getDirectionFromString(e.get("direction").asText());
                    GameResource frontRes = GameResource.getResourceFromNameInitial(e.get("frontResource").asText());
                    GameResource backRes = GameResource.getResourceFromNameInitial(e.get("backResource").asText());

                    ViewCorner toAdd = new ViewCorner(frontRes, backRes, dir);
                    corners.add(toAdd);
                }
        );

        return corners;
    }
}
