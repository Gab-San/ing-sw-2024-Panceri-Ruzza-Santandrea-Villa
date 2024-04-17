package it.polimi.ingsw.model.deck.cardfactory;


import org.junit.jupiter.api.Test;

import java.nio.file.Path;

class CardFactoryTest{
    @Test
    void setup(){
        CardFactory cardFactory = new ResourceCardFactory(Path.of("src/main/java/it/polimi/ingsw/model/resources/ResCard_Id"),
                Path.of("src/main/java/it/polimi/ingsw/model/resources/ResCard_JSON"));

    }
}