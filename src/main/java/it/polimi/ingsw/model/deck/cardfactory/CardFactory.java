package it.polimi.ingsw.model.deck.cardfactory;

import it.polimi.ingsw.model.cards.PlayCard;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.function.Function;
import java.util.stream.Stream;

public abstract class CardFactory extends Thread{
    protected final Path importFile;
    protected List<String> remainingCards;

    CardFactory(Path idFile, Path importFile){
        this.importFile = importFile;
        Charset chrset = Charset.forName(System.getProperty("file.encoding"));
        remainingCards = loadCardsIDs(idFile, chrset);
    }

    /**
     * Returns a random card to add to the deck.
     * @return a random card in the deck
     * @throws Exception if the deck is empty
     */
    abstract public PlayCard addCardToDeck() throws Exception;
    abstract protected PlayCard instantiateCard();
    abstract protected void importFromJSON();
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
