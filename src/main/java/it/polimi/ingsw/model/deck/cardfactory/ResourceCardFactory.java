package it.polimi.ingsw.model.deck.cardfactory;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;
import it.polimi.ingsw.model.cards.PlayCard;
import it.polimi.ingsw.model.cards.ResourceCard;
import it.polimi.ingsw.model.json.deserializers.JsonFunctions;
import it.polimi.ingsw.model.json.deserializers.ResourceCardDeserializer;
import it.polimi.ingsw.model.json.deserializers.ResourceCardJSON;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.NoSuchElementException;

public class ResourceCardFactory extends CardFactory {
    List<ResourceCardJSON> jsonCards;

    public ResourceCardFactory(String idFile) {
        super(idFile);
        importFromJSON();
    }

    @Override
    public PlayCard addCardToDeck() throws RuntimeException {
        if(remainingCards.isEmpty()){
            throw new RuntimeException("Deck is Empty");
        }
        String cardId = remainingCards.remove(getRandomCardID());
        PlayCard resCard = null;
        try {
            resCard = instantiateCard(cardId);
        } catch(NoSuchElementException exception){
            //TODO handle exception
        }
        return resCard;
    }

    @Override
    protected PlayCard instantiateCard(String cardId) throws NoSuchElementException {
        for(ResourceCardJSON resCard : jsonCards){
            if(resCard.getCardId().equals(cardId)){
                // Should the used card be deleted?
//                jsonCards.remove(resCard);
                return new ResourceCard(
                        resCard.getBackResource(),
                        resCard.getPointsOnPlace(),
                        JsonFunctions.parseCorners(resCard.getCornerJS())
                );
            }
        }
        throw new NoSuchElementException();
    }

    @Override
    protected void importFromJSON() {

        try {
            ObjectMapper objectMapper = new ObjectMapper();
            SimpleModule module = new SimpleModule();
            module.addDeserializer(ResourceCardJSON.class,new ResourceCardDeserializer());
            objectMapper.registerModule(module);

            File json = new File("src/main/java/it/polimi/ingsw/model/json/ResourceCard.json");
            jsonCards = objectMapper.readValue(json, objectMapper.getTypeFactory().constructCollectionType(List.class, ResourceCardJSON.class));
        } catch (IOException e) {
            System.err.println("Error reading file JSON: " + e.getMessage());
        }
    }

    @Override
    public String toString() {
        return remainingCards.toString();
    }
}
