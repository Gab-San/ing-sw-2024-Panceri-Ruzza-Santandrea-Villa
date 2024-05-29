package it.polimi.ingsw.model.deck;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;
import it.polimi.ingsw.model.cards.StartingCard;
import it.polimi.ingsw.model.exceptions.DeckException;
import it.polimi.ingsw.model.exceptions.DeckInstantiationException;
import it.polimi.ingsw.model.json.deserializers.StartingCardDeserializer;
import it.polimi.ingsw.model.listener.remote.errors.CrashStateError;
import it.polimi.ingsw.network.Server;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class StartingCardDeck{

    private final List<StartingCard> cardDeck;
    private static final String jsonPath =
            Server.getBaseJSONPath() + "StartingCard.json";

    public StartingCardDeck() throws IllegalStateException{
        cardDeck = importFromJson();
    }

    public StartingCard getCard(){
        int cardIdx = getRandomCard();
        return cardDeck.remove(cardIdx);
    }

    private List<StartingCard> importFromJson() throws IllegalStateException{
        List<StartingCard> startingCardsList;

        try {
            ObjectMapper objectMapper = new ObjectMapper();
            SimpleModule simpleModule = new SimpleModule();
            simpleModule.addDeserializer(StartingCard.class, new StartingCardDeserializer());
            objectMapper.registerModule(simpleModule);

            File json = new File(jsonPath);
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
