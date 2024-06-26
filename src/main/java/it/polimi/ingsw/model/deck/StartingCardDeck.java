package it.polimi.ingsw.model.deck;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;
import it.polimi.ingsw.model.cards.StartingCard;
import it.polimi.ingsw.model.json.deserializers.StartingCardDeserializer;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

//DOCS add docs
public class StartingCardDeck{

    private final List<StartingCard> cardDeck;

    public StartingCardDeck() throws IllegalStateException{
        cardDeck = importFromJson();
    }

    public StartingCard getCard(){
        int cardIdx = getRandomCard();
        return cardDeck.remove(cardIdx);
    }

    private InputStream getJsonFromResources(){
        ClassLoader cl = this.getClass().getClassLoader();
        try {
            return cl.getResourceAsStream("server/StartingCard.json");
        } catch(NullPointerException e){
            System.exit(-1); //crash app if a json can't be read
            throw new RuntimeException("File could not be read");
        }
    }

    private List<StartingCard> importFromJson() throws IllegalStateException{
        List<StartingCard> startingCardsList;

        try {
            ObjectMapper objectMapper = new ObjectMapper();
            SimpleModule simpleModule = new SimpleModule();
            simpleModule.addDeserializer(StartingCard.class, new StartingCardDeserializer());
            objectMapper.registerModule(simpleModule);

            InputStream json = getJsonFromResources();
            startingCardsList = objectMapper.readValue(json, objectMapper.getTypeFactory().constructCollectionType(ArrayList.class, StartingCard.class));
        } catch (IOException e) {
            throw new IllegalStateException("SYSTEM SHUT DOWN: An error occured while initializing the starting deck");
        }

        return startingCardsList;
    }

    protected int getRandomCard() {
        return new Random().nextInt(cardDeck.size());
    }


}
