package it.polimi.ingsw.view.gui.scenes.areas.localarea;

import it.polimi.ingsw.CornerDirection;
import it.polimi.ingsw.GamePoint;
import it.polimi.ingsw.view.gui.CardListener;
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
                    cornerListener.placeOnCorner(null, new GamePoint(0,0), null);
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
        if(!super.parentPropertyChange(evt))
            return; //returns if parent detected invalid update

        if(evt.getNewValue() instanceof ViewPlaceableCard placedCard){
            deletePlaceHolders();
            placedCard.setCornerListener(this);
            placedCard.setCardListener(this);
        }
    }

    private void deletePlaceHolders() {
        if(!placeHolderList.isEmpty())
           SwingUtilities.invokeLater(
                   () -> {
                       placeHolderList.forEach(
                               layeredPane::remove
                       );
                       placeHolderList.clear();
                       revalidate();
                       repaint();
                   }
           );
    }

    @Override
    public void placeOnCorner(String cardID, GamePoint position, CornerDirection direction){
        try{
            cornerListener.placeOnCorner(cardID, position, direction);
        }catch (IllegalStateException e){
            return; //on placement failure
        }
    }

    @Override
    public void setSelectedCard(ViewPlaceableCard card) {
        //A placed card should not be clickable, so this method should never be called.
        //The placed card's CardListener is set to this just to avoid NullPointerExceptions
        // or accidental side effects that would arise should this method be called on other CardListeners
        System.out.println("Clicked a placed card and triggered \"SetSelectedCard\"");
    }
}
