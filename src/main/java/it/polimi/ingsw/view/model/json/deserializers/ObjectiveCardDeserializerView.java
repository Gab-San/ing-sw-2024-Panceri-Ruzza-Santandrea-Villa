package it.polimi.ingsw.view.model.json.deserializers;

import com.fasterxml.jackson.core.JacksonException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import it.polimi.ingsw.model.cards.ObjectiveCard;
import it.polimi.ingsw.model.cards.objective.PatternObjectiveStrategy;
import it.polimi.ingsw.model.cards.objective.ResourceObjectiveStrategy;
import it.polimi.ingsw.model.json.deserializers.JsonFunctions;
import it.polimi.ingsw.view.gui.GUIFunc;
import it.polimi.ingsw.view.model.cards.ViewObjectiveCard;

import java.io.IOException;

public class ObjectiveCardDeserializerView extends StdDeserializer<ViewObjectiveCard> {
    public ObjectiveCardDeserializerView(){
        this(null);
    }
    public ObjectiveCardDeserializerView(Class<?> vc){
        super(vc);
    }

    //DOCS: add docs for deserialize (??)
    @Override
    public ViewObjectiveCard deserialize(JsonParser jsonParser, DeserializationContext deserializationContext)
            throws IOException, JacksonException {

        JsonNode node = jsonParser.getCodec().readTree(jsonParser);

        String id = node.get("cardId").asText();
        String patternType = node.get("type").asText();
        String patternValue = node.get("pattern").asText();
        String imgFront = GUIFunc.getGraphicsResourcesRootPath() + node.get("frontImageFileName").asText();
        String imgBack = GUIFunc.getGraphicsResourcesRootPath() + node.get("backImageFileName").asText();

        int pointsPerSolve = node.get("points").asInt();

        switch (patternType){
            case "PATTERNTYPE", "RESOURCETYPE":
                return new ViewObjectiveCard(id, imgFront, imgBack, patternType, pointsPerSolve, patternValue);
            default:
                System.err.println("ERROR WHILE READING JSON");
                throw new IOException();
        }
    }
}
