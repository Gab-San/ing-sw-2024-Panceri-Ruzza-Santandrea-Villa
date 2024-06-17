package it.polimi.ingsw.view.gui.scenes.board;

import it.polimi.ingsw.view.gui.CardListener;
import it.polimi.ingsw.view.gui.DeckListener;
import it.polimi.ingsw.view.gui.GUI_Scene;
import it.polimi.ingsw.view.gui.GameInputHandler;
import it.polimi.ingsw.view.model.ViewBoard;
import it.polimi.ingsw.view.model.ViewDeck;
import it.polimi.ingsw.view.model.cards.ViewCard;
import it.polimi.ingsw.view.model.cards.ViewGoldCard;
import it.polimi.ingsw.view.model.cards.ViewPlaceableCard;
import it.polimi.ingsw.view.model.cards.ViewResourceCard;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.List;

//DOCS [Gamba] add docs;

//FIXME add error display

/**
 * This class implements GUI scene interface. It displays the decks and the scoreboard.
 */
public class BoardScene extends JPanel implements GUI_Scene, ActionListener, CardListener, DeckListener {
    private final JPanel deckpanel;
    private final JPanel scoreboard;
    private final List<ViewDeck<? extends ViewPlaceableCard>> selectableDecks;
    private static final int SCOREBOARD_WIDTH = 300;
    private final GameInputHandler inputHandler;
    private char selectedDeck;
    private int selectedPosition;

    /**
     * Constructs the board scene with the deck and the scoreboard to be
     * populated as the updates arrive.
     *
     * @param inputHandler game interaction controller
     */
    public BoardScene(ViewBoard board, GameInputHandler inputHandler){
        this.inputHandler = inputHandler;
        selectedDeck = 'A';
        selectedPosition = -1;
        selectableDecks = new ArrayList<>(2);

        setLayout(new BorderLayout());
        deckpanel = setupDeckPanel(board);
        scoreboard = new JPanel();
        scoreboard.setPreferredSize(new Dimension(SCOREBOARD_WIDTH, 10));
        scoreboard.setBackground(new Color(0xc76f30));

        //Adding components
        add(deckpanel, BorderLayout.CENTER);
        add(scoreboard, BorderLayout.EAST);
    }

    private JPanel setupDeckPanel(ViewBoard board) {
        JPanel panel = new JPanel(new GridBagLayout());
        // Height isn't needed as in border layout it is automatically stretched
        panel.setPreferredSize(new Dimension(ViewCard.getScaledWidth() * 3 + 4*10,
                ViewCard.getScaledHeight() * 3 + 4 * 10 + 25));
        panel.setBackground(new Color(0xc76f30));
        addResourceDeck(panel, board);
        addGoldDeck(panel, board);

        panel.add(board.getObjectiveCardDeck(),new GridBagConstraints(2,0,1,1,1.0,1.0,
                GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(10,0,0,0), 0,0));

        JButton drawButton = new JButton("DRAW");
        drawButton.setFocusable(false);
        drawButton.addActionListener(this);

        panel.add(drawButton,new GridBagConstraints(0,1 ,2 ,1 ,0.5 ,0.5,
                GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(10,10,10,10), 0,0));
        return panel;
    }

    private void addGoldDeck(JPanel panel, ViewBoard board) {
        ViewDeck<ViewGoldCard> goldDeck = board.getGoldCardDeck();
        goldDeck.setCardListener(this);
        goldDeck.setDeckListener(this);
        selectableDecks.add(goldDeck);
        panel.add(goldDeck, new GridBagConstraints(1,0,1,1,1.0,1.0,
                GridBagConstraints.CENTER, GridBagConstraints.NONE,
                new Insets(10,0,0,10), 0,0));
    }

    private void addResourceDeck(JPanel panel, ViewBoard board) {
        ViewDeck<ViewResourceCard> resDeck = board.getResourceCardDeck();
        resDeck.setCardListener(this);
        resDeck.setDeckListener(this);
        selectableDecks.add(resDeck);
        panel.add(resDeck, new GridBagConstraints(0,0,1,1,1.0,1.0,
                GridBagConstraints.CENTER, GridBagConstraints.NONE,
                new Insets(10,0,0,10), 0,0));
    }


    @Override
    public void display() {
        setVisible(true);
    }

    @Override
    public void displayError(String error) {

    }

    @Override
    public void displayNotification(List<String> backlog) {

    }

    @Override
    public void close() {
        setVisible(false);
    }

    @Override
    public void setSelectedCard(ViewPlaceableCard card) {
        SwingUtilities.invokeLater(
                () ->{
                    deselectCards();
                    card.setBorder(BorderFactory.createLineBorder(Color.red, 3));
                }
        );
    }

    private void deselectCards() {
        selectableDecks.forEach(
                (deck) -> {
                    deck.getTopCard().setBorder(BorderFactory.createEmptyBorder());
                    deck.getFirstRevealed().setBorder(BorderFactory.createEmptyBorder());
                    deck.getSecondRevealed().setBorder(BorderFactory.createEmptyBorder());
                }
        );
    }

    @Override
    public void setSelectedPosition(char deckId, int cardPosition) {
        selectedDeck = deckId;
        selectedPosition = cardPosition;
        System.out.println("SELECTED POSITION: " + deckId + selectedPosition);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if(selectedDeck == 'A' || selectedPosition == -1){
            System.err.println("NO CARD IS SELECTED!");
            return;
        }

        try{
            inputHandler.draw(selectedDeck, selectedPosition);
        } catch (IllegalArgumentException | IllegalStateException exc){
          //TODO handle possible exception
            System.err.println(exc.getMessage());
        } catch (RemoteException ex) {
            throw new RuntimeException(ex);
        }
    }
}
