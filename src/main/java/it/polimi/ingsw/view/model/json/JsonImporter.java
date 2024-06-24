package it.polimi.ingsw.view.model.json;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;
import it.polimi.ingsw.view.model.cards.*;
import it.polimi.ingsw.view.model.json.deserializers.GoldCardDeserializerView;
import it.polimi.ingsw.view.model.json.deserializers.ObjectiveCardDeserializerView;
import it.polimi.ingsw.view.model.json.deserializers.ResourceCardDeserializerView;
import it.polimi.ingsw.view.model.json.deserializers.StartingCardDeserializerView;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Hashtable;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * Class that provides an interface to get cards from JSON.
 */
public class JsonImporter {
    private final Map<String, ViewResourceCard> resourceCards;
    private final Map<String, ViewGoldCard> goldCards;
    private final Map<String, ViewObjectiveCard> objectiveCards;
    private final Map<String, ViewStartCard> startCards;

    private InputStream getJsonAsResource(String jsonFileName) throws IOException {
        ClassLoader cl = this.getClass().getClassLoader();
        try {
            return cl.getResourceAsStream("client/" + jsonFileName);
        } catch(NullPointerException e){
            e.printStackTrace(System.err);
            throw new IOException("Json could not be read");
        }
    }

    /**
     * Creates the Importer and imports all ViewCards from JSON
     * @throws IOException if an error occurs while loading cards
     */
    public JsonImporter() throws IOException{
        ObjectMapper objectMapper = new ObjectMapper();
        SimpleModule simpleModule = new SimpleModule();
        simpleModule.addDeserializer(ViewGoldCard.class, new GoldCardDeserializerView());
        objectMapper.registerModule(simpleModule);

        InputStream json = getJsonAsResource("GoldCard.json"); // Path file JSON
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

        json = getJsonAsResource("ResourceCard.json"); // Path file JSON
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

        json = getJsonAsResource("ObjectiveCard.json"); // Path file JSON
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

        json = getJsonAsResource("StartingCard.json"); // Path file JSON
        startCards = new Hashtable<>();
        List<ViewStartCard> startCardImport = objectMapper.readValue(json, objectMapper.getTypeFactory()
                .constructCollectionType(List.class, ViewStartCard.class));
        startCardImport.forEach(
                c -> startCards.put(c.getCardID(), c)
        );
    }

    /**
     * @param ID a card ID (can be null)
     * @return returns a copy of the resource card with given ID
     *        (or null if the ID is null or doesn't correspond to a resource card)
     */
    public ViewResourceCard getResourceCard(String ID){
        if(ID == null) return null;
        return new ViewResourceCard(resourceCards.get(ID));
    }

    /**
     * @param ID a card ID (can be null)
     * @return returns a copy of the gold card with given ID
     *        (or null if the ID is null or doesn't correspond to a gold card)
     */
    public ViewGoldCard getGoldCard(String ID){
        if(ID == null) return null;
        return new ViewGoldCard(goldCards.get(ID));
    }
    /**
     * @param ID a card ID (can be null)
     * @return returns the objective card with given ID
     *        (or null if the ID is null or doesn't correspond to an objective card)
     */
    public ViewObjectiveCard getObjectiveCard(String ID){
        if(ID == null) return null;
        return objectiveCards.get(ID);
    }
    /**
     * @param ID a card ID (can be null)
     * @return returns a starting card with given ID
     *        (or null if the ID is null or doesn't correspond to a starting card)
     */
    public ViewStartCard getStartCard(String ID){
        if(ID == null) return null;
        return new ViewStartCard(startCards.get(ID));
    }

    /**
     * @param ID a card ID (can be null)
     * @return the card with the given ID (a copy if it's a resource or gold card)
     *        (or null if the ID is null or doesn't correspond to any card)
     */
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
    /**
     * @param ID a card ID (can be null)
     * @return a copy of the playCard with the given ID
     *        (or null if the ID is null or doesn't correspond to a playCard)
     */
    public ViewPlayCard getPlayCard(String ID){
        if(ID == null) return null;
        return switch (ID.toUpperCase().charAt(0)){
            case 'R' -> getResourceCard(ID);
            case 'G' -> getGoldCard(ID);
            default -> null;
        };
    }
    /**
     * @param IDList a list of card IDs
     * @return the list of cards with the given IDs, in the same order as the IDs
     *        (the list may contain null values for the IDs that don't correspond to a card)
     */
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
    /**
     * @param IDList a list of playCard IDs
     * @return the list of copies of the playCards with the given IDs, in the same order as the IDs
     *        (the list may contain null values for the IDs that don't correspond to a playCard)
     */
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
    /**
     * @param IDList a list of objectiveCard IDs
     * @return the list of objectiveCards with the given IDs, in the same order as the IDs
     *        (the list may contain null values for the IDs that don't correspond to an objectiveCard)
     */
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
