package it.polimi.ingsw.model.json.deserializers;

import com.fasterxml.jackson.core.JacksonException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import it.polimi.ingsw.model.cards.ObjectiveCard;
import it.polimi.ingsw.model.cards.objective.PatternObjectiveStrategy;
import it.polimi.ingsw.model.cards.objective.ResourceObjectiveStrategy;

import java.io.IOException;

public class ObjectiveCardDeserializer extends StdDeserializer<ObjectiveCard> {
    public ObjectiveCardDeserializer(){
        this(null);
    }
    public ObjectiveCardDeserializer(Class<?> vc){
        super(vc);
    }
    @Override
    public ObjectiveCard deserialize(JsonParser jsonParser, DeserializationContext deserializationContext)
            throws IOException, JacksonException {
        // CardID is ignored as it is useless

        JsonNode node = jsonParser.getCodec().readTree(jsonParser);

        String patternType = node.get("type").asText();


        int pointsPerSolve = node.get("points").asInt();

        // Setting Card Images
        //TODO [GAMBA] {After View} Set Images
//        String frontImageFile = node.get("frontImageFileName").asText();
//        String backImageFile = node.get("backImageFileName").asText();

        switch (patternType){
            case "PATTERNTYPE":
                PatternObjectiveStrategy patternStr = JsonFunctions.parsePatternObjectiveStrategy(node);
                return new ObjectiveCard( patternStr, pointsPerSolve);
            case "RESOURCETYPE":
                ResourceObjectiveStrategy resStr = JsonFunctions.parseResourcesObjective(node);
                return new ObjectiveCard( resStr, pointsPerSolve);
            default:
                throw new IOException();
        }
    }
}
