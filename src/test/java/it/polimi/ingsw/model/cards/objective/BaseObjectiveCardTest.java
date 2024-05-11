package it.polimi.ingsw.model.cards.objective;

import it.polimi.ingsw.model.PlayArea;
import it.polimi.ingsw.Point;
import it.polimi.ingsw.model.cards.*;
import it.polimi.ingsw.model.enums.GameResource;
import org.junit.jupiter.api.BeforeEach;

import static it.polimi.ingsw.model.enums.CornerDirection.*;
import static it.polimi.ingsw.model.enums.GameResource.*;
import static org.junit.jupiter.api.Assertions.*;

public class BaseObjectiveCardTest {
    protected PlayArea playArea;
    protected PlaceableCard startingCard;

    @BeforeEach
    void setUpPlayArea(){
        playArea = new PlayArea();
        StartingCard card = new StartingCard(
                new GameResource[]{LEAF, WOLF, MUSHROOM},
                new Corner(null, LEAF, TL),
                new Corner(null, BUTTERFLY, TR),
                new Corner(FILLED, WOLF, BL),
                new Corner(FILLED, MUSHROOM, BR)
        );
        playArea.placeStartingCard(card);
        startingCard = playArea.getCardMatrix().get(new Point(0,0));
        assertEquals(1, playArea.getVisibleResources().get(LEAF));
        assertEquals(1, playArea.getVisibleResources().get(WOLF));
        assertEquals(1, playArea.getVisibleResources().get(BUTTERFLY));
        assertEquals(1, playArea.getVisibleResources().get(MUSHROOM));
        // 1 resource of each after setup
    }
    protected ResourceCard makeResourceCard(GameResource resource){
        return new ResourceCard(resource, 0,
                new Corner(resource, TL),
                new Corner(resource, TR),
                new Corner(resource, BL),
                new Corner(resource, BR)
        );
    }
    protected PlaceableCard placePlayCard(GameResource resource, Corner corner, boolean placeOnBack){
        PlayCard card = makeResourceCard(resource);
        if(!placeOnBack) card.turnFaceUp();
        playArea.placeCard(card, corner);
        return card.getCorner(TL).getCardRef();
    }
    protected PlaceableCard placePlayCard(GameResource resource, Corner corner) {
        return placePlayCard(resource,corner,false);
    }


}
