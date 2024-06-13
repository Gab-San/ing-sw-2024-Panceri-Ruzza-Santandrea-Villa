package it.polimi.ingsw.stub;

import it.polimi.ingsw.GamePhase;
import it.polimi.ingsw.GamePoint;
import it.polimi.ingsw.view.ViewController;
import it.polimi.ingsw.view.model.ViewBoard;
import it.polimi.ingsw.view.model.ViewHand;
import it.polimi.ingsw.view.model.ViewPlayArea;
import it.polimi.ingsw.view.model.cards.ViewPlayCard;
import it.polimi.ingsw.view.model.cards.ViewStartCard;

public class StubViewController extends ViewController {
    public StubViewController(ViewBoard board) {
        super(board);
    }

    @Override
    public void validatePlaceStartCard() throws IllegalStateException, IllegalArgumentException {
        
    }

    @Override
    public void validateChooseColor() throws IllegalStateException {
        
    }

    @Override
    public void validateChooseObjective() throws IllegalStateException {
        
    }

    @Override
    public void validatePhase(GamePhase phase) throws IllegalStateException {
        
    }

    @Override
    public void validatePlaceCard(String cardID, GamePoint placePos, String cornerDir) throws IllegalStateException, IllegalArgumentException {
        
    }

    @Override
    public void validateDraw(char deck, int card) throws IllegalStateException, IllegalArgumentException {
        
    }

    @Override
    public void validateMsg(String addressee) throws IllegalArgumentException {
        
    }
}
