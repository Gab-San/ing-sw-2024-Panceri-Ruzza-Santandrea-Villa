package it.polimi.ingsw.model.deck.cardfactory;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;
import it.polimi.ingsw.model.cards.GoldCard;
import it.polimi.ingsw.model.cards.PlayCard;
import it.polimi.ingsw.model.exceptions.DeckException;
import it.polimi.ingsw.model.json.deserializers.GoldCardDeserializer;
import it.polimi.ingsw.model.json.deserializers.GoldCardJSON;
import it.polimi.ingsw.model.json.deserializers.JsonFunctions;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.NoSuchElementException;

/**
 * This class represents a card factory for gold cards.
 */
public class GoldCardFactory extends CardFactory {
    private final List<GoldCardJSON> jsonCards;

    /**
     * Default constructor.
     * @throws IllegalStateException if an error occurs while reading the json
     */
    public GoldCardFactory() throws IllegalStateException{
        this("GoldCard_Id");
    }

    /**
     * Constructs a gold card factory using deck cards at specified path.
     * @param idFile path to the deck file
     * @throws IllegalStateException if an error occurs while reading the json
     */
    public GoldCardFactory(String idFile) throws IllegalStateException {
        super(idFile);
        try {
            jsonCards = importFromJson();
        } catch (DeckException deckException){
            throw new IllegalStateException(deckException.getMessage());
        }
    }

    @Override
    public synchronized PlayCard addCardToDeck() throws DeckException {
        if(remainingCards.isEmpty()){
            throw new DeckException("Deck is Empty", GoldCardFactory.class);
        }
        String cardId = remainingCards.remove(getRandomCard());
        PlayCard goldCard;
        try{
            goldCard = instantiateCard(cardId);
        } catch (NoSuchElementException exception){
            throw new DeckException("Requested card was not found", exception, ResourceCardFactory.class);
        }
        return goldCard;
    }

    @Override
    protected synchronized PlayCard instantiateCard(String cardId) throws NoSuchElementException {
        for(GoldCardJSON goldCard : jsonCards){
            if(goldCard.getCardId().equals(cardId)){
                return new GoldCard(
                        goldCard.getCardId(),
                        goldCard.getBackResource(),
                        goldCard.getPointsOnPlace().getAmount(),
                        JsonFunctions.parsePlacementCost(goldCard.getPlacementCost()),
                        JsonFunctions.parseGoldCardStrategy(goldCard.getPointsOnPlace()),
                        JsonFunctions.parseCorners(goldCard.getCornersJS())
                );
            }
        }
        throw new NoSuchElementException("Error in instantiating card");
    }

    private List<GoldCardJSON> importFromJson() throws DeckException {
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            SimpleModule simpleModule = new SimpleModule();
            simpleModule.addDeserializer(GoldCardJSON.class, new GoldCardDeserializer());
            objectMapper.registerModule(simpleModule);

            InputStream json = getFromResources("GoldCard.json"); // Path file JSON
            return objectMapper.readValue(json, objectMapper.getTypeFactory().constructCollectionType(List.class, GoldCardJSON.class));
        } catch (IOException e) {
            throw new DeckException("Error reading file JSON", e, GoldCardFactory.class);
        }
    }

}
