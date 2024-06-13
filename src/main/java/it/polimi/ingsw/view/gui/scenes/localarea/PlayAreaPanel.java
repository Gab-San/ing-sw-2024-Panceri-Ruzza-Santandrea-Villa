package it.polimi.ingsw.view.gui.scenes.localarea;

import it.polimi.ingsw.view.gui.CardListener;
import it.polimi.ingsw.view.gui.GameInputHandler;
import it.polimi.ingsw.view.model.cards.ViewPlaceableCard;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class PlayAreaPanel extends JPanel{
    private static final int AREA_WIDTH = 16620;
    public static final int CARD_WIDTH = AREA_WIDTH/80; //80 cards wide
    private static final int AREA_HEIGHT = 11120;
    public static final int CARD_HEIGHT = AREA_HEIGHT/80; //80 cards high
    private final SpringLayout layout;
    private CardListener cardListener;
    public PlayAreaPanel(GameInputHandler inputHandler){
        setSize(new Dimension(AREA_WIDTH, AREA_HEIGHT));
        setBackground(new Color(0xc76f30));

        layout = new SpringLayout();
        PlaceHolder placeHolder = setupPlaceHolder();
        placeHolder.addMouseListener(
                new MouseAdapter() {
                    @Override
                    public void mouseClicked(MouseEvent e) {
                        System.out.println("PLACING CARD!");
                        cardListener.setClickedCard(null, placeHolder.getX(), placeHolder.getY(), null);
                    }
                }
        );

        setLayout(layout);
        add(placeHolder);
    }

    private PlaceHolder setupPlaceHolder() {
        PlaceHolder placeHolder = new PlaceHolder();
        layout.putConstraint(SpringLayout.HORIZONTAL_CENTER, placeHolder, this.getWidth()/2,
                SpringLayout.WEST, this);
        layout.putConstraint(SpringLayout.VERTICAL_CENTER, placeHolder, this.getHeight()/2,
                SpringLayout.NORTH, this);
        layout.putConstraint(SpringLayout.EAST, placeHolder,
                this.getWidth()/2 + placeHolder.getWidth()/2, SpringLayout.WEST, this);
        layout.putConstraint(SpringLayout.SOUTH, placeHolder,
            this.getHeight()/2 + placeHolder.getHeight()/2, SpringLayout.NORTH, this);
        placeHolder.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                PlaceHolder p = (PlaceHolder) e.getSource();
                System.out.println(p);

            }
        });
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
}
