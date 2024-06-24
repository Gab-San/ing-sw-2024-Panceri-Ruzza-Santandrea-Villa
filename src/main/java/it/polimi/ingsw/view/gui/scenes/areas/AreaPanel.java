package it.polimi.ingsw.view.gui.scenes.areas;

import it.polimi.ingsw.GamePoint;
import it.polimi.ingsw.PlayerColor;
import it.polimi.ingsw.view.GameColor;
import it.polimi.ingsw.view.gui.scenes.board.PlayerPawn;
import it.polimi.ingsw.view.model.cards.ViewCard;
import it.polimi.ingsw.view.model.cards.ViewCorner;
import it.polimi.ingsw.view.model.cards.ViewPlaceableCard;

import javax.swing.*;
import java.awt.*;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.lang.reflect.InvocationTargetException;
import java.util.HashSet;
import java.util.Set;

import static it.polimi.ingsw.view.gui.ChangeNotifications.*;
//DOCS add docs
public abstract class AreaPanel extends JPanel implements PropertyChangeListener{
    protected static final int AREA_WIDTH = 16620;
    protected static final int AREA_HEIGHT = 11120;
    protected static final int CENTER_X = AREA_WIDTH/2;
    protected static final int CENTER_Y = AREA_HEIGHT/2;
    protected final SpringLayout layout;
    protected final JLayeredPane layeredPane;
    protected final Set<ViewPlaceableCard> placedCardsSet;
    private static final int PAWN_xOFFSET = (int) (ViewCard.getScaledWidth()/5.4f);
    private static final int PAWN_yOFFSET = -ViewCard.getScaledHeight()/3;

    /**
     * Constructs the AreaPanel, setting size, layout and background color.
     */
    protected AreaPanel(){
        this.placedCardsSet = new HashSet<>();

        setSize(new Dimension(AREA_WIDTH, AREA_HEIGHT));
        setBackground(GameColor.BOARD_COLOUR.getColor());
        setLayout(new BorderLayout());

        layout = new SpringLayout();
        layeredPane = new JLayeredPane();
        layeredPane.setLayout(layout);

        add(layeredPane, BorderLayout.CENTER);
    }

    @Override
    public Dimension getPreferredSize() {
        // 831/4 * 80 , 556/4 * 80
        return new Dimension(AREA_WIDTH, AREA_HEIGHT);
    }

    @Override
    public int getWidth() {
        return AREA_WIDTH;
    }

    @Override
    public int getHeight() {
        return AREA_HEIGHT;
    }

    @Override
    public Dimension getSize() {
        return new Dimension(AREA_WIDTH, AREA_HEIGHT);
    }

    /**
     * Places a card on the playArea and sets the constraints to set the position and size of the card.
     * @param placedCard the newly placed card
     */
    protected void setCardPosition(ViewPlaceableCard placedCard) {
        GamePoint position = placedCard.getPosition();
        int xOffset = position.col() * (ViewCard.getScaledWidth() - ViewCorner.getFixedWidth());
        // y axis direction is towards the bottom of the screen
        int yOffset = -1 * position.row() * (ViewCard.getScaledHeight() - ViewCorner.getFixedHeight());

        layout.putConstraint(SpringLayout.HORIZONTAL_CENTER, placedCard,
                CENTER_X + xOffset,
                SpringLayout.WEST, this);
        layout.putConstraint(SpringLayout.VERTICAL_CENTER, placedCard,
                CENTER_Y + yOffset,
                SpringLayout.NORTH, this);
        layout.putConstraint(SpringLayout.EAST, placedCard,
                CENTER_X + ViewCard.getScaledWidth() / 2 + xOffset,
                SpringLayout.WEST, this);
        layout.putConstraint(SpringLayout.SOUTH, placedCard,
                CENTER_Y + ViewCard.getScaledHeight() / 2 + yOffset,
                SpringLayout.NORTH, this);

        layout.getConstraints(placedCard);
    }

    @Override
    public abstract void propertyChange(PropertyChangeEvent evt);

    /**
     * A propertyChange method modified to return a boolean. <br>
     * Should be called by the children's propertyChange method.
     * @param evt the propertyChange event triggered by some object this is listening to
     * @return false if the update was invalid and ignored, <br>
     *         true if the update was valid and successful
     */
    protected boolean parentPropertyChange(PropertyChangeEvent evt) {
        ViewPlaceableCard placedCard;
        if(evt.getNewValue() instanceof ViewPlaceableCard)
            placedCard = (ViewPlaceableCard) evt.getNewValue();
        else placedCard = null;

        switch (evt.getPropertyName()){
            case CARD_LAYER_CHANGE:
                if (!placedCardsSet.contains(placedCard)){
                    System.out.println("RETURNED due to CARD_LAYER_CHANGE on nonExisting card");
                    return false;
                }
                ViewPlaceableCard oldCard = placedCardsSet.stream()
                        .filter(c -> c.equals(placedCard))
                        .findFirst().orElse(placedCard);
                try {
                    SwingUtilities.invokeAndWait(
                            () -> {
                                layeredPane.remove(oldCard);
                                revalidate();
                                repaint();  // (maybe re add this)
                            }
                    );
                } catch (InterruptedException | InvocationTargetException e) {
                    System.out.println("ERROR while removing oldCard " + e.getMessage());
                    return false;
                }
                //remove from set in order to pass the following ".contains()" check
                placedCardsSet.remove(oldCard);
        //note that CARD_LAYER_CHANGE execution continues into PLACED_CARD
            case PLACED_CARD:
                assert placedCard != null;
                if (placedCardsSet.contains(placedCard)){
                    System.out.println("RETURNED due to placement of alreadyExisting card");
                    return false;
                }
                setCardPosition(placedCard);
                placedCardsSet.add(placedCard);
                int cardLayer = placedCard.getLayer();
                SwingUtilities.invokeLater(
                        () -> {
                            placedCard.setBorder(BorderFactory.createEmptyBorder());
                            layeredPane.add(placedCard, Integer.valueOf(cardLayer));
                            revalidate();
                            repaint();
                        }
                );

                break;
            case COLOR_CHANGE:
                assert evt.getNewValue() instanceof PlayerColor;
                if(evt.getNewValue() == null) return false;
                Color color = PlayerColor.getColor((PlayerColor) evt.getNewValue());
                PlayerPawn pawn = new PlayerPawn(color);
                placePawn(pawn, -PAWN_xOFFSET);
                break;
            case PLAYER_TURN_UPDATE:
                assert evt.getNewValue() instanceof Integer;
                if(evt.getNewValue() == null) return false;
                int turn = (Integer) evt.getNewValue();
                if(turn == 1){
                    placePawn(new PlayerPawn(Color.black), PAWN_xOFFSET);
                }
                break;
        }
        return true;
    }

    /**
     * Places a colored pawn on the starting card.
     * @param pawn the pawn to place
     * @param xOffset the offset on which to place it
     *                (should be set to either + or - PAWN_xOFFSET)
     */
    private void placePawn(PlayerPawn pawn, int xOffset){
        layout.putConstraint(SpringLayout.HORIZONTAL_CENTER, pawn,
                CENTER_X + xOffset,
                SpringLayout.WEST, this);
        layout.putConstraint(SpringLayout.VERTICAL_CENTER, pawn,
                CENTER_Y + PAWN_yOFFSET,
                SpringLayout.NORTH, this);

        SwingUtilities.invokeLater(
                ()->{
                    layeredPane.add(pawn, Integer.valueOf(Integer.MAX_VALUE));
                    revalidate();
                    repaint();
                }
        );
    }

    /**
     * Clears the panel (removes all the components on the panel).
     */
    public void clear(){
        layeredPane.removeAll();
        SwingUtilities.invokeLater(
                ()->{
                    revalidate();
                    repaint();
                }
        );
    }
}
