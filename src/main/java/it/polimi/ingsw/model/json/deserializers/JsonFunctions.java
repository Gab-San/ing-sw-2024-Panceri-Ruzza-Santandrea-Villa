package it.polimi.ingsw.model.json.deserializers;

import com.fasterxml.jackson.databind.JsonNode;
import it.polimi.ingsw.model.cards.Corner;
import it.polimi.ingsw.model.cards.cardstrategies.CornerCoverGoldCard;
import it.polimi.ingsw.model.cards.cardstrategies.GoldCardStrategy;
import it.polimi.ingsw.model.cards.cardstrategies.ItemCountGoldCard;
import it.polimi.ingsw.model.cards.cardstrategies.SimpleGoldCard;
import it.polimi.ingsw.model.cards.objective.PatternObjective;
import it.polimi.ingsw.model.cards.objective.PatternObjectiveStrategy;
import it.polimi.ingsw.model.cards.objective.ResourceObjectiveStrategy;
import it.polimi.ingsw.CornerDirection;
import it.polimi.ingsw.model.enums.GameResource;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public interface JsonFunctions {
    static List<CornerJ> parseJsonCorners(@NotNull JsonNode node){
        List<CornerJ> cornerJS = new ArrayList<>();
        node.get("corners").forEach((e)->{
            CornerJ toAdd = new CornerJ();
            toAdd.setDirection(e.get("direction").asText());
            toAdd.setFrontResource(e.get("frontResource").asText());
            toAdd.setBackResource(e.get("backResource").asText());
            cornerJS.add(toAdd);
        });
        return cornerJS;
    }

    static List<Corner> parseCorners(@NotNull JsonNode node){
        List<Corner> corners = new ArrayList<>();
        node.get("corners").forEach(
                (e) -> {
                    CornerDirection dir = CornerDirection.getDirectionFromString(e.get("direction").asText());
                    GameResource frontRes = GameResource.getResourceFromName(e.get("frontResource").asText());
                    GameResource backRes = GameResource.getResourceFromName(e.get("backResource").asText());

                    Corner toAdd = new Corner(frontRes, backRes, dir);
                    corners.add(toAdd);
                }
        );

        return corners;
    }

    static List<Corner> parseCorners(@NotNull List<CornerJ> jCorners){
        return jCorners.stream().map(e -> new Corner(
                GameResource.getResourceFromName(e.getFrontResource()),
                GameResource.getResourceFromName(e.getBackResource()),
                CornerDirection.getDirectionFromString(e.getDirection())
        )).collect(Collectors.toList());
    }

    static PatternObjectiveStrategy parsePatternObjectiveStrategy(@NotNull JsonNode node){
        List<String> patternList = new ArrayList<>();
        node.get("pattern").forEach(
                e -> patternList.add(e.asText())
        );

        PatternObjective pattern = new PatternObjective(
                patternList.get(0)
        );

        return new PatternObjectiveStrategy(pattern);
    }

    static ResourceObjectiveStrategy parseResourcesObjective(@NotNull JsonNode node){
        List<GameResource> resourceList = new ArrayList<>();
        node.get("pattern").forEach(
                e -> resourceList.add(
                        GameResource.getResourceFromName(e.asText())
                )
        );
        Hashtable<GameResource, Integer> resourceMap = new Hashtable<>();
        resourceList.forEach( (e) ->
                {
                    if(!resourceMap.contains(e)) {
                        resourceMap.put(e, (int) resourceList.stream().filter((x) -> x.equals(e)).count());
                    }
                }
        );
        return new ResourceObjectiveStrategy(resourceMap);
    }

    static Map<String, Integer> parsePlacementCost(@NotNull JsonNode node){
        Map<String, Integer> plCost = new Hashtable<>();

        node.get("placementCost").forEach(
                (e) -> plCost.put(
                  e.get("type").asText(), e.get("amount").asInt()
                )
        );

        return plCost;
    }

    static Map<GameResource, Integer> parsePlacementCost(@NotNull Map<String, Integer> plCost){
        Map<GameResource, Integer> placementCost = new Hashtable<>();
        for(String gameRes: plCost.keySet()){
            placementCost.put(
                    GameResource.getResourceFromName(gameRes),
                    plCost.get(gameRes)
            );
        }

        return placementCost;
    }

    static GoldCardStrategy parseGoldCardStrategy(@NotNull PointOnPlace pp){
        String cardStrat = pp.getType();
        switch (cardStrat){
            case "CORNERCOVER":
                return new CornerCoverGoldCard();
            case "SIMPLEPOINT":
                return new SimpleGoldCard();
            default:
                return new ItemCountGoldCard( GameResource.getResourceFromName(cardStrat) );
        }
    }
}
