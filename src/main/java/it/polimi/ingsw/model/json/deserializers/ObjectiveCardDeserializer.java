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
/**
 * This class implements the deserializer for objective cards.
 */
public class ObjectiveCardDeserializer extends StdDeserializer<ObjectiveCard> {
    /**
     * Default constructor.
     */
    public ObjectiveCardDeserializer(){
        this(null);
    }

    /**
     * Constructs objective card deserializer with specified class.
     * @param vc Type of values this deserializer handles:
     *           sometimes exact types, other time most specific supertype of types
     *           deserializer handles (which may be as generic as Object in some case)
     */
    public ObjectiveCardDeserializer(Class<?> vc){
        super(vc);
    }
    @Override
    public ObjectiveCard deserialize(JsonParser jsonParser, DeserializationContext deserializationContext)
            throws IOException, JacksonException {
        // CardID is ignored as it is useless

        JsonNode node = jsonParser.getCodec().readTree(jsonParser);

        String id = node.get("cardId").asText();
        String patternType = node.get("type").asText();


        int pointsPerSolve = node.get("points").asInt();


        switch (patternType){
            case "PATTERNTYPE":
                PatternObjectiveStrategy patternStr = JsonFunctions.parsePatternObjectiveStrategy(node);
                return new ObjectiveCard(id, patternStr, pointsPerSolve);
            case "RESOURCETYPE":
                ResourceObjectiveStrategy resStr = JsonFunctions.parseResourcesObjective(node);
                return new ObjectiveCard(id, resStr, pointsPerSolve);
            default:
                throw new IOException();
        }
    }
}
