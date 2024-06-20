package it.polimi.ingsw.view.gui.scenes.areas.localarea;

import it.polimi.ingsw.CornerDirection;
import it.polimi.ingsw.GamePoint;
import it.polimi.ingsw.view.gui.CardListener;
import it.polimi.ingsw.view.gui.ChangeNotifications;
import it.polimi.ingsw.view.gui.CornerListener;
import it.polimi.ingsw.view.gui.scenes.areas.AreaPanel;
import it.polimi.ingsw.view.model.cards.ViewPlaceableCard;

import javax.swing.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.LinkedList;
import java.util.List;

public class PlayAreaPanel extends AreaPanel implements PropertyChangeListener, CornerListener, CardListener {
    private final List<PlaceHolder> placeHolderList;
    private CornerListener cornerListener;

    public PlayAreaPanel(){
        placeHolderList = new LinkedList<>();

        PlaceHolder placeHolder = setupPlaceHolder();

        layeredPane.add(placeHolder, Integer.valueOf(0));
        SwingUtilities.invokeLater(
                ()->{
                    revalidate();
                    repaint();
                }
        );
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
                } catch (IllegalStateException exception){
                    return; // if setClickedCard fails to place startCard
                }
                SwingUtilities.invokeLater(
                        () -> {
                            layeredPane.remove(p);
                            revalidate();
                            repaint();
                        }
                );
            }
        });
        placeHolderList.add(placeHolder);
        return placeHolder;
    }

    public void setCornerListener(CornerListener cornerListener) {
        this.cornerListener = cornerListener;
    }

    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        System.out.println("LOCAL AREA TRIGGERED " + evt.getPropertyName());
        if(!super.parentPropertyChange(evt)) return; //returns if parent detected invalid update
        assert evt.getNewValue() instanceof ViewPlaceableCard;
        ViewPlaceableCard placedCard = (ViewPlaceableCard) evt.getNewValue();

        switch (evt.getPropertyName()){
            case ChangeNotifications.CARD_LAYER_CHANGE:
                System.out.println("LOCAL Changed card " + placedCard.getCardID() + " layer to " + placedCard.getLayer());
                System.out.flush();
                break;
            case ChangeNotifications.PLACED_CARD:
                deletePlaceHolders();
                placedCard.setCornerListener(this);
                placedCard.setCardListener(this);
                System.out.println("LOCAL Placed card " + placedCard.getCardID() + " having layer " + placedCard.getLayer());
                System.out.flush();
                break;
        }
    }

    private void deletePlaceHolders() {
       SwingUtilities.invokeLater(
               () -> placeHolderList.forEach(
                       layeredPane::remove
                       )
       );
    }

    @Override
    public void setClickedCard(String cardID, GamePoint position, CornerDirection direction){
        try{
            cornerListener.setClickedCard(cardID, position, direction);
        }catch (IllegalStateException e){
            return; //on placement failure
        }
    }

    @Override
    public void setSelectedCard(ViewPlaceableCard card) {
        System.out.println("Clicked a placed card and triggered \"SetSelectedCard\"");
    }
}
