package it.polimi.ingsw.stub;

import it.polimi.ingsw.Point;
import it.polimi.ingsw.view.ViewController;
import it.polimi.ingsw.view.model.ViewBoard;

public class StubViewController extends ViewController {
    public StubViewController(ViewBoard board) {
        super(board);
    }

    @Override
    public void validatePlaceStartCard() throws IllegalStateException, IllegalArgumentException{

    }

    @Override
    public void validateChooseColor() throws IllegalStateException, IllegalArgumentException{

    }
    @Override
    public void validateChooseObjective() throws IllegalStateException{

    }

    @Override
    public void validatePlaceCard(String cardID, Point placePos, String cornerDir) throws IllegalStateException, IllegalArgumentException{

    }

    @Override
    public void validateDraw(char deck, int card) throws IllegalStateException, IllegalArgumentException{

    }

    @Override
    public void validateMsg(String addressee) throws IllegalArgumentException {

    }
}
