package it.polimi.ingsw.model.deck.cardfactory;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;
import it.polimi.ingsw.model.json.deserializers.ResourceCardDeserializer;
import it.polimi.ingsw.model.json.deserializers.ResourceCardJSON;
import it.polimi.ingsw.model.cards.PlayCard;
import it.polimi.ingsw.model.cards.ResourceCard;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.util.List;

public class ResourceCardFactory extends CardFactory {

    public ResourceCardFactory(String idFile) {
        super(idFile);
    }

    @Override
    public PlayCard addCardToDeck() {
        String cardId = remainingCards.remove(getRandomCardID());
        //TODO complete when JSON is functional
        return instantiateCard();
    }

    @Override
    protected PlayCard instantiateCard() {
        ResourceCard emptyCard = new ResourceCard();
        return emptyCard;
    }

    @Override
    protected void importFromJSON() {

        try {
            ObjectMapper objectMapper = new ObjectMapper();
            SimpleModule module = new SimpleModule();
            module.addDeserializer(ResourceCardJSON.class,new ResourceCardDeserializer());
            objectMapper.registerModule(module);
            File json = new File("src/main/java/it/polimi/ingsw/model/json/ResourceCard.json");
            List<ResourceCardJSON> jsonList = objectMapper.readValue(json, objectMapper.getTypeFactory().constructCollectionType(List.class, ResourceCardJSON.class));
            //jsonCards = jsonList;
        } catch (IOException e) {
            System.err.println("Error reading file JSON: " + e.getMessage());
        }
    }

    @Override
    public String toString() {
        return remainingCards.toString();
    }
}
