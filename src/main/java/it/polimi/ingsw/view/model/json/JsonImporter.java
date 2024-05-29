package it.polimi.ingsw.view.model.json;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;
import it.polimi.ingsw.view.Client;
import it.polimi.ingsw.view.model.cards.*;
import it.polimi.ingsw.view.model.json.deserializers.*;

import java.io.File;
import java.io.IOException;
import java.util.Hashtable;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class JsonImporter {
    private final Map<String, ViewResourceCard> resourceCards;
    private final Map<String, ViewGoldCard> goldCards;
    private final Map<String, ViewObjectiveCard> objectiveCards;
    private final Map<String, ViewStartCard> startCards;

    /**
     * Creates the Importer and imports all ViewCards from JSON
     * @throws IOException if an error occurs while loading cards
     */
    public JsonImporter() throws IOException{
        ObjectMapper objectMapper = new ObjectMapper();
        SimpleModule simpleModule = new SimpleModule();
        simpleModule.addDeserializer(ViewGoldCard.class, new GoldCardDeserializerView());
        objectMapper.registerModule(simpleModule);
        String basePath = "";
        if(Client.isRunningInIDE())
            basePath = "src/main/java/it/polimi/ingsw/view/model/json/";

        File json = new File(basePath+"GoldCard.json"); // Path file JSON
        goldCards = new Hashtable<>();
        List<ViewGoldCard> goldCardImport = objectMapper.readValue(json, objectMapper.getTypeFactory()
                .constructCollectionType(List.class, ViewGoldCard.class));
        goldCardImport.forEach(
                c -> goldCards.put(c.getCardID(), c)
        );


        objectMapper = new ObjectMapper();
        simpleModule = new SimpleModule();
        simpleModule.addDeserializer(ViewResourceCard.class, new ResourceCardDeserializerView());
        objectMapper.registerModule(simpleModule);

        json = new File(basePath+"ResourceCard.json"); // Path file JSON
        resourceCards = new Hashtable<>();
        List<ViewResourceCard> resourceCardImport = objectMapper.readValue(json, objectMapper.getTypeFactory()
                .constructCollectionType(List.class, ViewResourceCard.class));
        resourceCardImport.forEach(
                c -> resourceCards.put(c.getCardID(), c)
        );

        objectMapper = new ObjectMapper();
        simpleModule = new SimpleModule();
        simpleModule.addDeserializer(ViewObjectiveCard.class, new ObjectiveCardDeserializerView());
        objectMapper.registerModule(simpleModule);

        json = new File(basePath+"ObjectiveCard.json"); // Path file JSON
        objectiveCards = new Hashtable<>();
        List<ViewObjectiveCard> objectiveCardImport = objectMapper.readValue(json, objectMapper.getTypeFactory()
                .constructCollectionType(List.class, ViewObjectiveCard.class));
        objectiveCardImport.forEach(
                c -> objectiveCards.put(c.getCardID(), c)
        );

        objectMapper = new ObjectMapper();
        simpleModule = new SimpleModule();
        simpleModule.addDeserializer(ViewStartCard.class, new StartingCardDeserializerView());
        objectMapper.registerModule(simpleModule);

        json = new File(basePath+"StartingCard.json"); // Path file JSON
        startCards = new Hashtable<>();
        List<ViewStartCard> startCardImport = objectMapper.readValue(json, objectMapper.getTypeFactory()
                .constructCollectionType(List.class, ViewStartCard.class));
        startCardImport.forEach(
                c -> startCards.put(c.getCardID(), c)
        );
    }

    public ViewResourceCard getResourceCard(String ID){
        if(ID == null) return null;
        return resourceCards.get(ID);
    }
    public ViewGoldCard getGoldCard(String ID){
        if(ID == null) return null;
        return goldCards.get(ID);
    }
    public ViewObjectiveCard getObjectiveCard(String ID){
        if(ID == null) return null;
        return objectiveCards.get(ID);
    }
    public ViewStartCard getStartCard(String ID){
        if(ID == null) return null;
        return startCards.get(ID);
    }

    public ViewCard getCard(String ID){
        if(ID == null) return null;
        return switch (ID.toUpperCase().charAt(0)){
            case 'R' -> getResourceCard(ID);
            case 'G' -> getGoldCard(ID);
            case 'O' -> getObjectiveCard(ID);
            case 'S' -> getStartCard(ID);
            default -> null;
        };
    }
    public ViewPlayCard getPlayCard(String ID){
        if(ID == null) return null;
        return switch (ID.toUpperCase().charAt(0)){
            case 'R' -> getResourceCard(ID);
            case 'G' -> getGoldCard(ID);
            default -> null;
        };
    }

    public List<ViewCard> getCards(List<String> IDList){
        List<ViewCard> cardList = new LinkedList<>();
        if(IDList != null)
            IDList.forEach((id) -> {
                if(id != null ) {
                    cardList.add(getCard(id));
                }
            });
        return cardList;
    }

    public List<ViewPlayCard> getPlayCards(List<String> IDList){
        List<ViewPlayCard> cardList = new LinkedList<>();
        if(IDList != null)
            IDList.forEach((id) -> {
                if(id != null ) {
                    cardList.add(getPlayCard(id));
                }
            });
        return cardList;
    }
    public List<ViewObjectiveCard> getObjectiveCards(List<String> IDList){
        List<ViewObjectiveCard> cardList = new LinkedList<>();
        if(IDList != null)
            IDList.forEach((id) -> {
                if(id != null ) {
                    cardList.add(getObjectiveCard(id));
                }
            });
        return cardList;
    }
}
