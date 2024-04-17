package it.polimi.ingsw.model.deck.cardfactory;

import com.fasterxml.jackson.databind.ObjectMapper;
import it.polimi.ingsw.model.cards.Corner;
import it.polimi.ingsw.model.cards.StartingCard;
import it.polimi.ingsw.model.enums.GameResource;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.function.Function;
import java.util.stream.Stream;

public class StartingCardFactory {
    private final Path importFile;
    private List<String> remainingCards;
    private List<StartingCardJSON> jsonCards;

    public StartingCardFactory (Path idFile, Path importFile){
        this.importFile = importFile;
        Charset chrset = Charset.forName(System.getProperty("file.encoding"));
        remainingCards = loadCardsIDs(idFile, chrset);
    }

    public StartingCard addCardToDeck() {
        String nextCardID = remainingCards.get(getRandomCardID());

        StartingCard newCard = instantiateCard(nextCardID);
        return null;
    }

    public StartingCard instantiateCard(String cardID) {
//        for(StartingCardJSON startJ : jsonCards){
//            // Se la carta trovata è quella richiesta
//            if(startJ.getCardId().equals(cardID)){
//                GameResource[] centralRes = new GameResource[startJ.getCentralFrontResources().size()];
//                for(int i = 0; i < startJ.getCentralFrontResources().size(); i++){
//                    centralRes[i] = GameResource.getResourceFromName(startJ.getCentralFrontResources().get(i));
//                }
//
//                Function<Stream<Corner>,Stream<it.polimi.ingsw.model.cards.Corner>>
//
//                return new StartingCard(centralRes,
//                        () ->{return startJ.getCorners().stream().map()}
//                        );
//            }
//        }
        return null;
    }

    public void importFromJSON(){
        String path = "src/main/java/it/polimi/ingsw/model/Json/StartingCard.json"; // Path file JSON

        try {
            ObjectMapper objectMapper = new ObjectMapper();

            List<StartingCardJSON> jsonList = objectMapper.readValue(new File(path), objectMapper.getTypeFactory().constructCollectionType(List.class, StartingCardJSON.class));
            jsonCards = jsonList;
        } catch (IOException e) {
            System.err.println("Error reading file JSON: " + e.getMessage());
        }
    }
    private List<String> loadCardsIDs(Path filePath, Charset charset){
        List<String> idList = new ArrayList<>();
        try(BufferedReader reader = Files.newBufferedReader(filePath, charset)){
            Function<String, Stream<String>> mapLinesIntoId = (e) ->{
                List<String> idStream = new ArrayList<>();
                for(int i = 0; i < e.length(); i += 3){
                    idStream.add(e.substring(i, i+2));
                }
                return idStream.stream();
            };

            reader.lines().flatMap(mapLinesIntoId).forEach(idList::add);
        } catch(IOException ioException){
            // TODO Handle exception
            ioException.printStackTrace();
        }
        return idList;
    }

    protected int getRandomCardID(){
        return new Random().nextInt(remainingCards.size());
    }
}
