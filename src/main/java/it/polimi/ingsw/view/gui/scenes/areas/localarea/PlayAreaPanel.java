package it.polimi.ingsw.view.gui.scenes.areas.localarea;

import it.polimi.ingsw.GamePoint;
import it.polimi.ingsw.view.gui.CardListener;
import it.polimi.ingsw.view.gui.ChangeNotifications;
import it.polimi.ingsw.view.model.cards.ViewCard;
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
    private List<PlaceHolder> placeHolderList;
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
                    cardListener.setClickedCard(null, 0, 0, null);
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
                setCardPosition(placedCard);
                deletePlaceHolders();

                SwingUtilities.invokeLater(
                        () -> {
                            placedCard.setBorder(BorderFactory.createEmptyBorder());
                            add(placedCard);
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

    private void setCardPosition(ViewPlaceableCard placedCard) {
        if(placedCard.getPosition().equals(new GamePoint(0,0))) {
            layout.putConstraint(SpringLayout.HORIZONTAL_CENTER, placedCard, CENTER_X,
                    SpringLayout.WEST, this);
            layout.putConstraint(SpringLayout.VERTICAL_CENTER, placedCard, CENTER_Y,
                    SpringLayout.NORTH, this);
            layout.putConstraint(SpringLayout.EAST, placedCard,
                    CENTER_X + ViewCard.getScaledWidth() / 2, SpringLayout.WEST, this);
            layout.putConstraint(SpringLayout.SOUTH, placedCard,
                    CENTER_Y + ViewCard.getScaledHeight() / 2, SpringLayout.NORTH, this);
            return;
        }

    }
}
