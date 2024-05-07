package it.polimi.ingsw.model.deck.cardfactory;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;
import it.polimi.ingsw.model.cards.PlayCard;
import it.polimi.ingsw.model.cards.ResourceCard;
import it.polimi.ingsw.model.exceptions.DeckException;
import it.polimi.ingsw.model.exceptions.DeckInstantiationException;
import it.polimi.ingsw.model.json.deserializers.JsonFunctions;
import it.polimi.ingsw.model.json.deserializers.ResourceCardDeserializer;
import it.polimi.ingsw.model.json.deserializers.ResourceCardJSON;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.NoSuchElementException;

public class ResourceCardFactory extends CardFactory {
    private final List<ResourceCardJSON> jsonCards;

    public ResourceCardFactory() throws DeckInstantiationException{
        super("src/main/java/it/polimi/ingsw/model/resources/ResourceCard_Id");

        try {
            jsonCards = importFromJson();
        } catch (DeckException deckException){
            throw new DeckInstantiationException(deckException.getMessage(), deckException.getCause(),
                    deckException.getDeck());
        }
    }

    public ResourceCardFactory(String idFile) throws DeckInstantiationException{
        super(idFile);
        try{
            jsonCards = importFromJson();
        } catch (DeckException deckException){
            throw new DeckInstantiationException(deckException.getMessage(), deckException.getCause(),
                    deckException.getDeck());
        }
    }

    @Override
    public synchronized PlayCard addCardToDeck() throws DeckException {
        if(remainingCards.isEmpty()){
            throw new DeckException("Deck is Empty", ResourceCardFactory.class);
        }
        String cardId = remainingCards.remove(getRandomCard());
        PlayCard resCard;
        try {
            resCard = instantiateCard(cardId);
        } catch(NoSuchElementException exception){
            throw new DeckException("Requested card was not found", exception, ResourceCardFactory.class);
        }
        return resCard;
    }

    @Override
    protected synchronized PlayCard instantiateCard(String cardId) throws NoSuchElementException {
        for(ResourceCardJSON resCard : jsonCards){
            if(resCard.getCardId().equals(cardId)){
                return new ResourceCard(
                        resCard.getCardId(),
                        resCard.getBackResource(),
                        resCard.getPointsOnPlace(),
                        JsonFunctions.parseCorners(resCard.getCornerJS())
                );
            }
        }
        throw new NoSuchElementException("Error in instantiating card");
    }

    private List<ResourceCardJSON> importFromJson() throws DeckException{
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            SimpleModule module = new SimpleModule();
            module.addDeserializer(ResourceCardJSON.class,new ResourceCardDeserializer());
            objectMapper.registerModule(module);

            File json = new File("src/main/java/it/polimi/ingsw/model/json/ResourceCard.json");
            return objectMapper.readValue(json, objectMapper.getTypeFactory().constructCollectionType(List.class, ResourceCardJSON.class));
        } catch (IOException e) {
            throw new DeckException("Error reading file JSON", e, ResourceCardFactory.class);
        }
    }

    @Override
    public synchronized String toString() {
        return remainingCards.toString();
    }
}
