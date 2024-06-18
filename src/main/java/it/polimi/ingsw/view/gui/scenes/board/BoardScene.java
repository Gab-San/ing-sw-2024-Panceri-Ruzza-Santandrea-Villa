package it.polimi.ingsw.view.gui.scenes.board;

import it.polimi.ingsw.view.GameColor;
import it.polimi.ingsw.view.gui.*;
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
    private final ScoreboardPanel scoreboard;
    private final List<ViewDeck<? extends ViewPlaceableCard>> selectableDecks;
    private final GameInputHandler inputHandler;
    private char selectedDeck;
    private int selectedPosition;
    private final JLabel errorLabel;
    private Timer displayTimer;

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
        JPanel deckpanel = setupDeckPanel(board);
        scoreboard = new ScoreboardPanel();
        errorLabel = GUIFunc.createNotificationLabel();
        modifyErrorLabel();
        //Adding components
        add(errorLabel,BorderLayout.NORTH);
        add(deckpanel, BorderLayout.CENTER);
        add(scoreboard, BorderLayout.EAST);
    }



    private JPanel setupDeckPanel(ViewBoard board) {
        JPanel panel = new JPanel(new GridBagLayout());
        // Height isn't needed as in border layout it is automatically stretched
        panel.setPreferredSize(new Dimension(ViewCard.getScaledWidth() * 3 + 4*10,
                ViewCard.getScaledHeight() * 3 + 4 * 10 + 25));
        panel.setBackground(GameColor.BOARD_COLOUR.getColor());
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
        deselectCards();
        selectedDeck = 'A';
        selectedPosition = -1;
    }

    @Override
    public void displayError(String error) {
        displayError(error, 1.5f);
    }

    private void displayError(String errorMsg, float displayTimeSec){
        int displayTime =  GUIFunc.setupDisplayTimer(displayTimeSec, displayTimer);
        errorLabel.setForeground(GameColor.ERROR_COLOUR.getColor());
        errorLabel.setText(errorMsg);
        // The error will become visible
        errorLabel.setVisible(true);
        startDisplayTimer(displayTime);
    }
    private void startDisplayTimer(int displayTime) {
        // After delay time the notification will
        // disappear from the screen
        displayTimer = new Timer(displayTime,
                (event) -> {
                    errorLabel.setVisible(false);
                    // java.awt timers don't stop after
                    // the delay time has passed,
                    // so they need to be actively stopped
                    displayTimer.stop();
                    displayTimer = null;
                });
        displayTimer.start();
    }

    private void modifyErrorLabel() {
        errorLabel.setFont(new Font("Raleway", Font.BOLD, 30));
        errorLabel.setHorizontalAlignment(SwingConstants.CENTER);
        errorLabel.setVerticalAlignment(SwingConstants.CENTER);
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
            inputHandler.showError(exc.getMessage());
        } catch (RemoteException ex) {
            inputHandler.showError("CONNECTION LOST!");
            System.exit(-1);
        }
    }
}
