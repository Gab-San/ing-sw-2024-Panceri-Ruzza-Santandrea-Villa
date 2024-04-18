package it.polimi.ingsw.model.cards;

import it.polimi.ingsw.model.PlayArea;
import it.polimi.ingsw.model.enums.CornerDirection;
import it.polimi.ingsw.model.enums.GameResource;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.security.InvalidParameterException;
import java.util.Hashtable;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class ResourceCardTest {
    private ResourceCard cardNoPoints = null;
    private ResourceCard cardWithPoints = null;
    private static final Map<CornerDirection, Corner> card_corners = new Hashtable<>();

    @BeforeAll
    public static void initialization() {
        card_corners.put(CornerDirection.TL, new Corner(GameResource.BUTTERFLY, CornerDirection.TL));
        card_corners.put(CornerDirection.BL, new Corner(GameResource.QUILL, CornerDirection.BL));
        card_corners.put(CornerDirection.BR, new Corner(GameResource.BUTTERFLY, CornerDirection.BR));
    }

    @BeforeEach
    void setup(){
        try {
            cardNoPoints = new ResourceCard(GameResource.BUTTERFLY,
                    card_corners.get(CornerDirection.TL),
                    card_corners.get(CornerDirection.BL),
                    card_corners.get(CornerDirection.BR)
                    );
            cardWithPoints = new ResourceCard(
                    GameResource.WOLF,
                    1,
                    card_corners.get(CornerDirection.TL),
                    card_corners.get(CornerDirection.BL),
                    card_corners.get(CornerDirection.BR)
            );
        } catch(InvalidParameterException invalidParameterException){
            fail("Resource card instantiation failed with error message: \n" + invalidParameterException.getMessage());
        }
    }

    @Test
    void getPlacementCost() {
        Map<GameResource, Integer> placementCost = cardNoPoints.getPlacementCost();
        for(GameResource res: GameResource.values()) {
            if(res != GameResource.FILLED) assertNull(placementCost.get(res));
        }

        placementCost = cardWithPoints.getPlacementCost();
        for(GameResource res: GameResource.values()) {
            if(res != GameResource.FILLED) assertNull(placementCost.get(res));
        }
    }

    @Test
    @DisplayName("Calculation on card with no points")
    void calculatePointsNoPoints() {
        assertEquals(0, cardNoPoints.calculatePointsOnPlace(new PlayArea()));
    }
    @Test
    @DisplayName("Calculation on card with points")
    void calculatePointsWithPoints(){
        assertEquals(1, cardWithPoints.calculatePointsOnPlace(new PlayArea()));
    }
}