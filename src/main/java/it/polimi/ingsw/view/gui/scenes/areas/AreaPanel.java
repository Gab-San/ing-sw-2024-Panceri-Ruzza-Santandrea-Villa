package it.polimi.ingsw.view.gui.scenes.areas;

import it.polimi.ingsw.GamePoint;
import it.polimi.ingsw.view.GameColor;
import it.polimi.ingsw.view.gui.ChangeNotifications;
import it.polimi.ingsw.view.model.cards.ViewCard;
import it.polimi.ingsw.view.model.cards.ViewCorner;
import it.polimi.ingsw.view.model.cards.ViewPlaceableCard;

import javax.swing.*;
import java.awt.*;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.HashSet;
import java.util.Set;

public abstract class AreaPanel extends JPanel implements PropertyChangeListener{
    protected static final int AREA_WIDTH = 16620;
    protected static final int AREA_HEIGHT = 11120;
    protected static final int CENTER_X = AREA_WIDTH/2;
    protected static final int CENTER_Y = AREA_HEIGHT/2;
    protected final SpringLayout layout;
    protected final JLayeredPane layeredPane;
    protected final Set<ViewPlaceableCard> placedCardsSet;

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
    public void propertyChange(PropertyChangeEvent evt) {
        assert evt.getNewValue() instanceof ViewPlaceableCard;
        ViewPlaceableCard placedCard = (ViewPlaceableCard) evt.getNewValue();
        if(evt.getPropertyName().equals(ChangeNotifications.CARD_LAYER_CHANGE)){
            if (!placedCardsSet.contains(placedCard)) return;
            ViewPlaceableCard oldCard = placedCardsSet.stream()
                    .filter(c -> c.equals(placedCard))
                    .findFirst().orElse(placedCard);

            SwingUtilities.invokeLater(
                    () -> {
                        layeredPane.remove(oldCard);
                        revalidate();
//                        repaint();  (maybe re add this)
                    }
            );
        }

        // In any case the card should be (re-)added
        if (placedCardsSet.contains(placedCard)) return;
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
    }
}
