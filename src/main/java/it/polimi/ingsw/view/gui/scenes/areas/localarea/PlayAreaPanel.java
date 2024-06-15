package it.polimi.ingsw.view.gui.scenes.areas.localarea;

import it.polimi.ingsw.CornerDirection;
import it.polimi.ingsw.GamePoint;
import it.polimi.ingsw.view.gui.CornerListener;
import it.polimi.ingsw.view.model.cards.ViewCard;
import it.polimi.ingsw.view.model.cards.ViewCorner;
import it.polimi.ingsw.view.model.cards.ViewPlaceableCard;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.LinkedList;
import java.util.List;

public class PlayAreaPanel extends JPanel implements PropertyChangeListener, CornerListener {
    private static final int AREA_WIDTH = 16620;
    private static final int AREA_HEIGHT = 11120;
    private static final int CENTER_X = AREA_WIDTH/2;
    private static final int CENTER_Y = AREA_HEIGHT/2;
    private final SpringLayout layout;
    private final List<PlaceHolder> placeHolderList;
    private SpringLayout.Constraints startingCard;
    private CornerListener cornerListener;
    public PlayAreaPanel(){
        setSize(new Dimension(AREA_WIDTH, AREA_HEIGHT));
        setBackground(new Color(0xc76f30));
        placeHolderList = new LinkedList<>();
        //FIXME USE LAYERED PANE
        layout = new SpringLayout();
        PlaceHolder placeHolder = setupPlaceHolder();

        setLayout(layout);
        add(placeHolder);
    }

    private PlaceHolder setupPlaceHolder() {
        PlaceHolder placeHolder = new PlaceHolder();
        layout.putConstraint(SpringLayout.HORIZONTAL_CENTER, placeHolder, CENTER_X,
                SpringLayout.WEST, this);
        layout.putConstraint(SpringLayout.VERTICAL_CENTER, placeHolder, CENTER_Y,
                SpringLayout.NORTH, this);
        layout.putConstraint(SpringLayout.EAST, placeHolder,
                CENTER_X + placeHolder.getWidth()/2, SpringLayout.WEST, this);
        layout.putConstraint(SpringLayout.SOUTH, placeHolder,
            CENTER_Y + placeHolder.getHeight()/2, SpringLayout.NORTH, this);
        placeHolder.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                PlaceHolder p = (PlaceHolder) e.getSource();
                try {
                    cornerListener.setClickedCard(null, new GamePoint(0,0), null);
                    remove(p);
                } catch (IllegalStateException exception){
                    //TODO display error
                }
                SwingUtilities.invokeLater(
                        () -> {
                            revalidate();
                            repaint();
                        }
                );
            }
        });
        placeHolderList.add(placeHolder);
        return placeHolder;
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

    public void setCornerListener(CornerListener cornerListener) {
        this.cornerListener = cornerListener;
    }

    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        assert evt.getNewValue() instanceof ViewPlaceableCard;
        ViewPlaceableCard placedCard = (ViewPlaceableCard) evt.getNewValue();
        SpringLayout.Constraints constraints = setCardPosition(placedCard);
        deletePlaceHolders();
        placedCard.setCornerListener(this);

        SwingUtilities.invokeLater(
                () -> {
                    placedCard.setBorder(BorderFactory.createEmptyBorder());
                    add(placedCard, constraints);
                    revalidate();
                    repaint();
                }
        );
    }

    private void deletePlaceHolders() {
       SwingUtilities.invokeLater(
               () -> placeHolderList.forEach(
                       this::remove
                       )
       );
    }

    private SpringLayout.Constraints setCardPosition(ViewPlaceableCard placedCard) {
        if(placedCard.getPosition().equals(new GamePoint(0,0))) {
            layout.putConstraint(SpringLayout.HORIZONTAL_CENTER, placedCard, CENTER_X,
                    SpringLayout.WEST, this);
            layout.putConstraint(SpringLayout.VERTICAL_CENTER, placedCard, CENTER_Y,
                    SpringLayout.NORTH, this);
            layout.putConstraint(SpringLayout.EAST, placedCard,
                    CENTER_X + ViewCard.getScaledWidth() / 2, SpringLayout.WEST, this);
            layout.putConstraint(SpringLayout.SOUTH, placedCard,
                    CENTER_Y + ViewCard.getScaledHeight() / 2, SpringLayout.NORTH, this);
            startingCard = layout.getConstraints(placedCard);
            return startingCard;
        }

        GamePoint position = placedCard.getPosition();
        int centerX = position.col() * (ViewCard.getScaledWidth() - ViewCorner.getFixedWidth());
        // y axis direction is towards the bottom of the screen
        int centerY = -1 * position.row() * (ViewCard.getScaledHeight() - ViewCorner.getFixedHeight());


        SpringLayout.Constraints cardConstraints = layout.getConstraints(placedCard);
        cardConstraints.setX(
                Spring.sum(startingCard.getX(), Spring.constant(centerX))
        );

        cardConstraints.setY(
                Spring.sum(startingCard.getY(), Spring.constant(centerY))
        );

        cardConstraints.setWidth(
                Spring.sum(cardConstraints.getX(), Spring.constant(ViewCard.getScaledWidth()))
        );

        cardConstraints.setHeight(
                Spring.sum(cardConstraints.getY(), Spring.constant(ViewCard.getScaledHeight()))
        );

        return cardConstraints;
    }

    @Override
    public void setClickedCard(String cardID, GamePoint position, CornerDirection direction) {
        cornerListener.setClickedCard(cardID, position, direction);
    }
}
