package it.polimi.ingsw.model.deck.cardfactory;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;
import it.polimi.ingsw.model.cards.StartingCard;
import it.polimi.ingsw.model.json.deserializers.StartingCardDeserializer;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class StartingCardFactory{

    private List<StartingCard> startingCards;

    public StartingCardFactory (){
        importFromJSON();
    }

    public StartingCard addCardToDeck() throws Exception {
        if(startingCards.isEmpty()){
            throw new Exception("Deck is Empty");
        }
        int cardIdx = getRandomCardIdx();
        return startingCards.remove(cardIdx);
    }

    public void importFromJSON(){
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            SimpleModule simpleModule = new SimpleModule();
            simpleModule.addDeserializer(StartingCard.class, new StartingCardDeserializer());
            objectMapper.registerModule(simpleModule);

            File json = new File("src/main/java/it/polimi/ingsw/model/json/StartingCard.json");
            startingCards = objectMapper.readValue(json, objectMapper.getTypeFactory().constructCollectionType(ArrayList.class, StartingCard.class));
        } catch (IOException e) {
            System.err.println("Error reading file JSON: " + e.getMessage());
        }
    }

    protected int getRandomCardIdx(){
        return new Random().nextInt(startingCards.size());
    }
}
