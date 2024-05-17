package it.polimi.ingsw.model.deck;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;
import it.polimi.ingsw.listener.GameEvent;
import it.polimi.ingsw.listener.GameListener;
import it.polimi.ingsw.listener.GameSubject;
import it.polimi.ingsw.model.cards.StartingCard;
import it.polimi.ingsw.model.exceptions.DeckException;
import it.polimi.ingsw.model.exceptions.DeckInstantiationException;
import it.polimi.ingsw.model.exceptions.ListenException;
import it.polimi.ingsw.model.json.deserializers.StartingCardDeserializer;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class StartingCardDeck implements GameSubject {

    private final List<StartingCard> cardDeck;

    public StartingCardDeck() throws DeckInstantiationException {
        try {
            cardDeck = importFromJson();
        } catch (DeckException deckException){
            throw new DeckInstantiationException(deckException.getMessage(), deckException.getCause(),
                    deckException.getDeck());
        }
    }

    public StartingCard getCard(){
        int cardIdx = getRandomCard();
        return cardDeck.remove(cardIdx);
    }

    private List<StartingCard> importFromJson() throws DeckException {
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            SimpleModule simpleModule = new SimpleModule();
            simpleModule.addDeserializer(StartingCard.class, new StartingCardDeserializer());
            objectMapper.registerModule(simpleModule);

            File json = new File("src/main/java/it/polimi/ingsw/model/json/StartingCard.json");
            return objectMapper.readValue(json, objectMapper.getTypeFactory().constructCollectionType(ArrayList.class, StartingCard.class));
        } catch (IOException e) {
            throw new DeckException("Error reading file JSON", StartingCard.class);
        }
    }

    protected int getRandomCard() {
        return new Random().nextInt(cardDeck.size());
    }

    @Override
    public void addListener(GameListener listener) {

    }

    @Override
    public void removeListener(GameListener listener) {

    }

    @Override
    public void notifyAllListeners(GameEvent event) {

    }

    @Override
    public void notifyListener(GameListener listener, GameEvent event) throws ListenException {

    }
}
