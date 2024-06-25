package it.polimi.ingsw.model.deck.cardfactory;

import it.polimi.ingsw.model.exceptions.DeckException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.NoSuchElementException;

class ResourceCardFactoryTest {

    @Test
    @DisplayName("Wrong Card")
    void deckExceptionWrongCard(){
        ResourceCardFactory resourceCardFactory = null;
        try {
             resourceCardFactory = new ResourceCardFactory("TestFile");
        } catch (IllegalStateException deckInstantiationException){
            deckInstantiationException.printStackTrace(System.err);
        }
        
        try {
            assert resourceCardFactory != null;
            resourceCardFactory.addCardToDeck();
        } catch (NoSuchElementException exception){
            exception.printStackTrace(System.err);
            if ( exception.getCause() != null){
                System.out.println("This exc was caught by something else");
            }
        } catch (DeckException ignore) {
        }


    }

    @Test
    @DisplayName("No Cards Remaining")
    void deckExceptionNoCardsRemaining(){
        ResourceCardFactory resourceCardFactory = null;
        try {
            resourceCardFactory = new ResourceCardFactory();
        } catch (IllegalStateException deckExc){
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