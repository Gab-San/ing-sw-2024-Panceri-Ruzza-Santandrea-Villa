package it.polimi.ingsw.model.deck.cardfactory;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

interface CardReader {
    static List<String> loadCardsIDs(Path filePath, Charset charset){
        List<String> idList = new ArrayList<>();
        try(BufferedReader reader = Files.newBufferedReader(filePath, charset)){

            reader.lines().flatMap(e -> Arrays.stream(e.trim().split("\\s+"))).forEach(idList::add);
        } catch(IOException ioException){
            // TODO Handle exception
            ioException.printStackTrace();
        }
        return idList;
    }
}
