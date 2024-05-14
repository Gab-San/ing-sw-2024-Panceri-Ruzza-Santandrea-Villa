package it.polimi.ingsw.view.model.json.deserializers;


import com.fasterxml.jackson.databind.JsonNode;
import it.polimi.ingsw.model.enums.CornerDirection;
import it.polimi.ingsw.view.model.cards.ViewCorner;
import it.polimi.ingsw.view.model.enums.GameResourceView;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public interface JsonFunctionsView {
    static List<CornerJView> parseJsonCorners(@NotNull JsonNode node){
        List<CornerJView> cornerJS = new ArrayList<>();
        node.get("corners").forEach((e)->{
            CornerJView toAdd = new CornerJView();
            toAdd.setDirection(e.get("direction").asText());
            toAdd.setFrontResource(e.get("frontResource").asText());
            toAdd.setBackResource(e.get("backResource").asText());
            cornerJS.add(toAdd);
        });
        return cornerJS;
    }

    static List<ViewCorner> parseCorners(@NotNull JsonNode node){
        List<ViewCorner> corners = new ArrayList<>();
        node.get("corners").forEach(
                (e) -> {
                    CornerDirection dir = CornerDirection.getDirectionFromString(e.get("direction").asText());
                    GameResourceView frontRes = GameResourceView.getResourceFromName(e.get("frontResource").asText());
                    GameResourceView backRes = GameResourceView.getResourceFromName(e.get("backResource").asText());

                    ViewCorner toAdd = new ViewCorner(frontRes, backRes, dir);
                    corners.add(toAdd);
                }
        );

        return corners;
    }

    static List<ViewCorner> parseCorners(@NotNull List<CornerJView> jCorners){
        return jCorners.stream().map(e -> new ViewCorner(
                GameResourceView.getResourceFromName(e.getFrontResource()),
                GameResourceView.getResourceFromName(e.getBackResource()),
                CornerDirection.getDirectionFromString(e.getDirection())
        )).collect(Collectors.toList());
    }
}
