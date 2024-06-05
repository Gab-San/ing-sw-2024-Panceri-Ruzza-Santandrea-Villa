package it.polimi.ingsw.view.gui.scenes.board;

import it.polimi.ingsw.view.model.ViewDeck;
import it.polimi.ingsw.view.model.cards.ViewCard;
import it.polimi.ingsw.view.model.cards.ViewGoldCard;
import it.polimi.ingsw.view.model.cards.ViewObjectiveCard;
import it.polimi.ingsw.view.model.cards.ViewResourceCard;

import javax.swing.*;
import java.awt.*;

public class BoardPanel extends JPanel {
    private ViewDeck<ViewResourceCard> resourceCardViewDeck;
    private  ViewDeck<ViewGoldCard> goldCardViewDeck;
    private final ViewDeck<ViewObjectiveCard> objectiveCardViewDeck;
    private final JLabel label;
    public BoardPanel(ViewDeck<ViewResourceCard> resourceCardViewDeck,
                      ViewDeck<ViewGoldCard> goldCardViewDeck,
                      ViewDeck<ViewObjectiveCard> objectiveCardViewDeck){

//        this.resourceCardViewDeck = resourceCardViewDeck;
//        this.goldCardViewDeck = goldCardViewDeck;
        this.objectiveCardViewDeck = objectiveCardViewDeck;
        label = new JLabel("TEST");
        setLayout(new GridBagLayout());

        setComponentConstraints();

        setVisible(true);
    }

    private void setComponentConstraints(){
//        GridBagConstraints gbc = new GridBagConstraints();
//        // RIGA 1 COLONNA 1: RESOURCE CARD DECK
//        // Setting x and y-axis grid positions
//        gbc.gridx = 0;
//        gbc.gridy = 0;
//        // Setting grid weights
//        gbc.weightx = 1.0;
//        gbc.weighty = 1.0;
//
//        gbc.anchor = GridBagConstraints.CENTER;
//
//        ViewCard card = resourceCardViewDeck.getTopCard();
//        if(card != null) {
//            card.turnFaceDown();
//            add(card, gbc);
//            card.setVisible(true);
//        }
//
//        // RIGA 2 COLONNA 1
//        gbc = new GridBagConstraints();
//        gbc.gridx = 0;
//        gbc.gridy = 1;
//
//        gbc.weightx = 1.0;
//        gbc.weighty = 1.0;
//
//        gbc.anchor = GridBagConstraints.CENTER;
//        card = resourceCardViewDeck.getFirstRevealed();
//        if(card != null) {
//            card.turnFaceUp();
//            add(card, gbc);
//            card.setVisible(true);
//        }
//
//        // RIGA 3 COLONNA 1
//        gbc = new GridBagConstraints();
//        gbc.gridx = 0;
//        gbc.gridy = 2;
//
//        gbc.weightx = 1.0;
//        gbc.weighty = 1.0;
//
//        gbc.anchor = GridBagConstraints.CENTER;
//        card = resourceCardViewDeck.getSecondRevealed();
//        if(card != null) {
//            card.turnFaceUp();
//            add(card, gbc);
//            card.setVisible(true);
//        }
//
//        // RIGA 1 COLONNA 2
//        gbc = new GridBagConstraints();
//        gbc.gridx = 1;
//        gbc.gridy = 0;
//
//        gbc.weightx = 1.0;
//        gbc.weighty = 1.0;
//
//        gbc.anchor = GridBagConstraints.CENTER;
//        card = goldCardViewDeck.getTopCard();
//        if(card != null) {
//            card.turnFaceDown();
//            add(card, gbc);
//            card.setVisible(true);
//        }
//
//        // RIGA 2 COLONNA 2
//        gbc = new GridBagConstraints();
//        gbc.gridx = 1;
//        gbc.gridy = 1;
//
//        gbc.weightx = 1.0;
//        gbc.weighty = 1.0;
//
//        gbc.anchor = GridBagConstraints.CENTER;
//        card = goldCardViewDeck.getFirstRevealed();
//        if(card != null) {
//            card.turnFaceUp();
//            add(card, gbc);
//            card.setVisible(true);
//        }
//
//        // RIGA 3 COLONNA 2
//        gbc = new GridBagConstraints();
//        gbc.gridx = 1;
//        gbc.gridy = 2;
//
//        gbc.weightx = 1.0;
//        gbc.weighty = 1.0;
//
//        gbc.anchor = GridBagConstraints.CENTER;
//        card = goldCardViewDeck.getSecondRevealed();
//        if(card != null) {
//            card.turnFaceUp();
//            add(card, gbc);
//            card.setVisible(true);
//        }

        // RIGA 1 COLONNA 3
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 2;
        gbc.gridy = 0;

        gbc.weightx = 1.0;
        gbc.weighty = 1.0;

        gbc.anchor = GridBagConstraints.CENTER;
        ViewCard card = objectiveCardViewDeck.getTopCard();
        System.out.println("CARD IN GRID: " + card.getCardID());
        if(card != null) {
            card.turnFaceDown();
            add(card, gbc);
            card.setVisible(true);
        }

        // RIGA 2 COLONNA 3
        gbc = new GridBagConstraints();
        gbc.gridx = 2;
        gbc.gridy = 1;

        gbc.weightx = 1.0;
        gbc.weighty = 1.0;

        gbc.anchor = GridBagConstraints.CENTER;
        card = objectiveCardViewDeck.getFirstRevealed();
        if(card != null) {
            card.turnFaceUp();
            add(card, gbc);
            card.setVisible(true);
        }
        add(label, gbc);
        // RIGA 3 COLONNA 3
        gbc = new GridBagConstraints();
        gbc.gridx = 2;
        gbc.gridy = 2;

        gbc.weightx = 1.0;
        gbc.weighty = 1.0;

        gbc.anchor = GridBagConstraints.CENTER;
        card = objectiveCardViewDeck.getSecondRevealed();
        if(card != null) {
            card.turnFaceUp();
            add(card, gbc);
            card.setVisible(true);
        }
    }

}
