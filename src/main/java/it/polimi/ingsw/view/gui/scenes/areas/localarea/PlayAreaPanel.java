package it.polimi.ingsw.view.gui.scenes.areas.localarea;

import it.polimi.ingsw.CornerDirection;
import it.polimi.ingsw.GamePoint;
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

/**
 * This class extends area panel and represents the player play area view:
 * displays placed cards and highlights free corners in order to let the user
 * interact and place cards on them.
 */
public class PlayAreaPanel extends AreaPanel implements PropertyChangeListener, CornerListener {
    private final List<PlaceHolder> placeHolderList;
    private CornerListener cornerListener;

    /**
     * Default constructor.
     */
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

    /**
     * Sets the corner listener of user clicked corners.
     * @param cornerListener listener of cards corners.
     */
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
        }catch (IllegalStateException ignore){/*on placement failure*/}
    }

    /**
     * Removes all components from the panel and
     * adds a new placeholder.
     */
    @Override
    public void clear() {
        super.clear();
        PlaceHolder placeHolder = setupPlaceHolder();
        layeredPane.add(placeHolder, Integer.valueOf(0));
        SwingUtilities.invokeLater(
                ()->{
                    revalidate();
                    repaint();
                }
        );
    }
}
