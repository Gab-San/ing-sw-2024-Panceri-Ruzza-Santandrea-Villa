package it.polimi.ingsw.model.deck.cardfactory;

import com.fasterxml.jackson.databind.ObjectMapper;
import it.polimi.ingsw.model.cards.PlayCard;
import it.polimi.ingsw.model.cards.ResourceCard;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.util.List;

public class ResourceCardFactory extends CardFactory {

    public ResourceCardFactory(Path idFile, Path importFile) {
        super(idFile, importFile);
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
        String path = "src/main/java/it/polimi/ingsw/model/Json/ResourceCard.json"; // Path file JSON

        try {
            ObjectMapper objectMapper = new ObjectMapper();

            List<ResourceCardJSON> jsonList = objectMapper.readValue(new File(path), objectMapper.getTypeFactory().constructCollectionType(List.class, ResourceCardJSON.class));
            //jsonCards = jsonList;
        } catch (IOException e) {
            System.err.println("Error reading file JSON: " + e.getMessage());
        }
    }
}
