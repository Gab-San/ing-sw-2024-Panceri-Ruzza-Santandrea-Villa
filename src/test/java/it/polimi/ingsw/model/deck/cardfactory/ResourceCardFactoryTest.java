package it.polimi.ingsw.model.deck.cardfactory;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ResourceCardFactoryTest {

    @Test
    void importFromJSON() {
        ResourceCardFactory rFactory = new ResourceCardFactory("src/main/java/it/polimi/ingsw/model/resources/ResourceCard_Id");
        System.out.println(rFactory);
        rFactory.importFromJSON();
    }
}