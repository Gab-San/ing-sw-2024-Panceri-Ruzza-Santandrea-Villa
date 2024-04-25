package it.polimi.ingsw.model.deck.cardfactory;

import it.polimi.ingsw.model.exceptions.DeckException;
import it.polimi.ingsw.model.exceptions.DeckInstantiationException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class ResourceCardFactoryTest {

    @Test
    @DisplayName("Wrong Card")
    void deckExceptionWrongCard(){
        ResourceCardFactory resourceCardFactory = null;
        try {
             resourceCardFactory = new ResourceCardFactory("src/test/java/it/polimi/ingsw/model/deck/TestFile");
        } catch (DeckInstantiationException deckInstantiationException){
            deckInstantiationException.printStackTrace(System.err);
        }
        
        try {
            assert resourceCardFactory != null;
            resourceCardFactory.addCardToDeck();
        } catch (DeckException exception){
            exception.printStackTrace(System.err);
            if ( exception.getCause() != null){
                System.out.println("This exc was caught by something else");
            }
        }


    }

    @Test
    @DisplayName("No Cards Remaining")
    void deckExceptionNoCardsRemaining(){
        ResourceCardFactory resourceCardFactory = null;
        try {
            resourceCardFactory = new ResourceCardFactory("src/main/java/it/polimi/ingsw/model/resources/ResourceCard_Id");
        } catch (DeckInstantiationException deckExc){
            deckExc.printStackTrace(System.err);
        }

        System.out.println(resourceCardFactory);
        int counter = 0;
        while(true) {
            try {
                resourceCardFactory.addCardToDeck();
                counter++;
            } catch (DeckException exception) {
                exception.printStackTrace(System.err);
                if(exception.getCause() == null){
                    System.out.println("This exc was not caught by something else");
                }
                System.out.println(counter);
                break;
            }
        }
    }
}