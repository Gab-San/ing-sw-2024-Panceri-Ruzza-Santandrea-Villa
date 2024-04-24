package it.polimi.ingsw.model.deck.cardfactory;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;
import it.polimi.ingsw.model.cards.Corner;
import it.polimi.ingsw.model.cards.StartingCard;
import it.polimi.ingsw.model.enums.CornerDirection;
import it.polimi.ingsw.model.enums.GameResource;
import it.polimi.ingsw.model.json.deserializers.CornerJ;
import it.polimi.ingsw.model.json.deserializers.StartingCardDeserializer;
import it.polimi.ingsw.model.json.deserializers.StartingCardJSON;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.function.Function;
import java.util.stream.Collectors;

public class StartingCardFactory{

    private final List<String> remainingCards;
    private List<StartingCardJSON> jsonCards;

    public StartingCardFactory (String idFile){
        importFromJSON();
        Charset chrset = Charset.forName(System.getProperty("file.encoding"));
        remainingCards = CardReader.loadCardsIDs(Path.of(idFile), chrset);
    }

    public StartingCard addCardToDeck() {
        int cardIdx = getRandomCardIdx();
        String nextCardID = remainingCards.get(cardIdx);
        System.out.println(nextCardID);
        StartingCard newCard = parseFromJson(nextCardID);
        remainingCards.remove(cardIdx);
        return newCard;
    }

    public StartingCard parseFromJson(String cardID) {
        for(StartingCardJSON startJ : jsonCards){
            // Se la carta trovata è quella richiesta
            if(startJ.getCardId().equals(cardID)){
                List<GameResource> centralRes = startJ.getCentralFrontResources().stream().map(
                        GameResource::getResourceFromName
                ).collect(Collectors.toList());


                Function<CornerJ,Corner> cornerJToCorner = (cornJ) -> new Corner(
                        GameResource.getResourceFromName(cornJ.getFrontResource())
                        , GameResource.getResourceFromName(cornJ.getBackResource())
                        , CornerDirection.getDirectionFromString(cornJ.getDirection())
                );

                List<Corner> corners = startJ.getCorners().stream().map(cornerJToCorner).collect(Collectors.toList());

                return new StartingCard(centralRes,corners);
            }
        }
        return null;
    }

    public void importFromJSON(){
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            SimpleModule simpleModule = new SimpleModule();
            simpleModule.addDeserializer(StartingCardJSON.class, new StartingCardDeserializer());
            objectMapper.registerModule(simpleModule);

            File json = new File("src/main/java/it/polimi/ingsw/model/json/StartingCard.json");
            jsonCards = objectMapper.readValue(json, objectMapper.getTypeFactory().constructCollectionType(ArrayList.class, StartingCardJSON.class));
        } catch (IOException e) {
            System.err.println("Error reading file JSON: " + e.getMessage());
        }
    }

    protected int getRandomCardIdx(){
        return new Random().nextInt(remainingCards.size());
    }
}
