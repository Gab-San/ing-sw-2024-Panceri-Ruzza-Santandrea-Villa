package it.polimi.ingsw.view.gui.scenes.areas.localarea;

import it.polimi.ingsw.GamePoint;
import it.polimi.ingsw.view.gui.CardListener;
import it.polimi.ingsw.view.gui.ChangeNotifications;
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

public class PlayAreaPanel extends JPanel implements PropertyChangeListener {
    private static final int AREA_WIDTH = 16620;
    private static final int AREA_HEIGHT = 11120;
    private static final int CENTER_X = AREA_WIDTH/2;
    private static final int CENTER_Y = AREA_HEIGHT/2;
    private final SpringLayout layout;
    private final List<PlaceHolder> placeHolderList;
    private SpringLayout.Constraints startingCard;
    private CardListener cardListener;
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
                    cardListener.setClickedCard(null, new GamePoint(0,0), null);
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

    public void setCardListener(CardListener cardListener) {
        this.cardListener = cardListener;
    }

    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        switch (evt.getPropertyName()){
            case ChangeNotifications.PLACED_CARD:
                assert evt.getNewValue() instanceof ViewPlaceableCard;
                ViewPlaceableCard placedCard = (ViewPlaceableCard) evt.getNewValue();
                SpringLayout.Constraints constraints = setCardPosition(placedCard);
                deletePlaceHolders();

                SwingUtilities.invokeLater(
                        () -> {
                            System.out.println("PLACING CARD: " + placedCard.getCardID());
                            placedCard.setBorder(BorderFactory.createEmptyBorder());
                            add(placedCard, constraints);
                            revalidate();
                            repaint();
                        }
                );
        }

    }

    private void deletePlaceHolders() {
       SwingUtilities.invokeLater(
               () -> {
                   placeHolderList.forEach(
                           this::remove
                           );
               }
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
        System.out.println(position.row() + " " + position.col());
        int centerX = position.col() * (ViewCard.getScaledWidth() - ViewCorner.getFixedWidth());
        // y axis direction is towards the bottom of the screen
        int centerY = -1 * position.row() * (ViewCard.getScaledHeight() - ViewCorner.getFixedHeight());
        System.out.println("CENTERX" + centerX);
        System.out.println("CENTERY" +centerY);

        System.out.println("X: " + startingCard.getX().getMinimumValue());
        System.out.println("X: " +startingCard.getX().getPreferredValue());
        System.out.println("X: " +startingCard.getX().getMaximumValue());
        System.out.println("Y: " +startingCard.getY().getMinimumValue());
        System.out.println("Y: " +startingCard.getY().getPreferredValue());
        System.out.println("Y: " +startingCard.getY().getMaximumValue());
        System.out.println("WDT: " +startingCard.getWidth().getMinimumValue());
        System.out.println("WDT: " +startingCard.getWidth().getPreferredValue());
        System.out.println("WDT: " +startingCard.getWidth().getMaximumValue());
        System.out.println("HGT: " +startingCard.getHeight().getMinimumValue());
        System.out.println("HGT: " +startingCard.getHeight().getPreferredValue());
        System.out.println("HGT: " +startingCard.getHeight().getMaximumValue());
//        layout.putConstraint(SpringLayout.HORIZONTAL_CENTER, placedCard,
//                centerX,
//                SpringLayout.HORIZONTAL_CENTER, startingCard);
//        layout.putConstraint(SpringLayout.VERTICAL_CENTER, placedCard,
//                centerY,
//                SpringLayout.VERTICAL_CENTER, startingCard);
//        layout.putConstraint(SpringLayout.EAST, placedCard,
//                centerX + ViewCard.getScaledWidth()/2,
//                SpringLayout.VERTICAL_CENTER, startingCard);
//        layout.putConstraint(SpringLayout.SOUTH, placedCard,
//                centerY + ViewCard.getScaledHeight()/2,
//                SpringLayout.HORIZONTAL_CENTER, startingCard);

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
        System.out.println("X: " + layout.getConstraints(placedCard).getX().getMinimumValue());
        System.out.println("X: " +layout.getConstraints(placedCard).getX().getPreferredValue());
        System.out.println("X: " +layout.getConstraints(placedCard).getX().getMaximumValue());
        System.out.println("Y: " +layout.getConstraints(placedCard).getY().getMinimumValue());
        System.out.println("Y: " +layout.getConstraints(placedCard).getY().getPreferredValue());
        System.out.println("Y: " +layout.getConstraints(placedCard).getY().getMaximumValue());
        System.out.println("WDT: " +layout.getConstraints(placedCard).getWidth().getMinimumValue());
        System.out.println("WDT: " +layout.getConstraints(placedCard).getWidth().getPreferredValue());
        System.out.println("WDT: " +layout.getConstraints(placedCard).getWidth().getMaximumValue());
        System.out.println("HGT: " +layout.getConstraints(placedCard).getHeight().getMinimumValue());
        System.out.println("HGT: " +layout.getConstraints(placedCard).getHeight().getPreferredValue());
        System.out.println("HGT: " +layout.getConstraints(placedCard).getHeight().getMaximumValue());
        return cardConstraints;
    }
}
