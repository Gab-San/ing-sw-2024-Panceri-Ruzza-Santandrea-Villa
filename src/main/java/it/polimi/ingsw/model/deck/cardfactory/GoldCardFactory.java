package it.polimi.ingsw.model.deck.cardfactory;

import com.fasterxml.jackson.databind.ObjectMapper;
import it.polimi.ingsw.model.cards.GoldCard;
import it.polimi.ingsw.model.cards.PlayCard;
import it.polimi.ingsw.model.cards.ResourceCard;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.util.List;

public class GoldCardFactory extends CardFactory {

    public GoldCardFactory(Path idFile, Path importFile) {
        super(idFile, importFile);
    }

    @Override
    public PlayCard addCardToDeck() throws Exception {
        String cardId = remainingCards.remove(getRandomCardID());
        // TODO to implement when JSON is functional
        return instantiateCard();
    }

    @Override
    protected PlayCard instantiateCard() {
        return null;
    }

    @Override
    protected void importFromJSON() {
        String path = "src/main/java/it/polimi/ingsw/model/Json/GoldCard.json"; // Path file JSON

        try {
            ObjectMapper objectMapper = new ObjectMapper();

            List<GoldCardJSON> jsonList = objectMapper.readValue(new File(path), objectMapper.getTypeFactory().constructCollectionType(List.class, GoldCardJSON.class));
            //jsonCards = jsonList;
        } catch (IOException e) {
            System.err.println("Error reading file JSON: " + e.getMessage());
        }
    }
}
