package it.polimi.ingsw.model.deck.cardfactory;

import it.polimi.ingsw.model.cards.Card;
import org.junit.jupiter.api.Test;

import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.*;

class CardFactoryTest{
    @Test
    void setup(){
        CardFactory cardFactory = new ResourceCardFactory(Path.of("C:\\Users\\gago1\\Projects\\IngSw\\ing-sw-2024-Panceri-Ruzza-Santandrea-Villa\\src\\main\\java\\it\\polimi\\ingsw\\model\\resources\\ResCard_Id"),
                Path.of("C:\\Users\\gago1\\Projects\\IngSw\\ing-sw-2024-Panceri-Ruzza-Santandrea-Villa\\src\\main\\java\\it\\polimi\\ingsw\\model\\resources\\ResCard_JSON"));

    }
}