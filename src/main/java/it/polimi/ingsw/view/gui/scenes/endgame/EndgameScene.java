package it.polimi.ingsw.view.gui.scenes.endgame;

import it.polimi.ingsw.PlayerColor;
import it.polimi.ingsw.view.GameColor;
import it.polimi.ingsw.view.gui.GUIFunc;
import it.polimi.ingsw.view.gui.GUI_Scene;
import it.polimi.ingsw.view.gui.GameInputHandler;
import it.polimi.ingsw.view.gui.scenes.board.ScoreboardPanel;
import it.polimi.ingsw.view.model.ViewBoard;
import it.polimi.ingsw.view.model.ViewHand;
import it.polimi.ingsw.view.model.ViewOpponentHand;
import it.polimi.ingsw.view.model.cards.ViewCard;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

public class EndgameScene extends JPanel implements GUI_Scene, ActionListener {
    private final GameInputHandler inputHandler;
    private final ViewBoard board;
    private final ScoreboardPanel scoreboard;
    private final boolean atLeast2Players;
    protected Timer displayTimer;
    private final JLabel notificationLabel;
    private static final int STANDARD_WINNER_TEXT_FONT = 50;

    public EndgameScene(ViewBoard board, GameInputHandler inputHandler){
        this.inputHandler = inputHandler;
        this.board = board;
        setLayout(new BorderLayout());

        JPanel winnerPanel = setupWinnerPanel();
        atLeast2Players = board.getOpponents().stream()
                .anyMatch(ViewOpponentHand::isConnected);

        String winnerNick = board.getAllPlayerHands().stream()
                .map(ViewHand::getNickname)
                .min((n1, n2) -> -Integer.compare(
                        board.getScore(n1),
                        board.getScore(n2)
                )).orElseThrow(); //getAllPlayerHands can't be empty

        JLabel winnerTextLabel;
        JLabel winnerNickLabel;
        if(winnerNick.equals(board.getPlayerHand().getNickname())) {
            winnerTextLabel = setupLabel("Congratulations!", null);

            String nick = "You won";
            if(!atLeast2Players)
                nick += " BY DEFAULT.";
            else nick += ".";

            winnerNickLabel = setupLabel(nick, board.getPlayerHand().getColor());
        }
        else {
            winnerTextLabel = setupLabel("The winner is: ", null);
            winnerNickLabel = setupLabel(winnerNick, board.getOpponentHand(winnerNick).getColor());
        }

        winnerPanel.add(winnerTextLabel,
                new GridBagConstraints(0,0,2 ,1 ,0.5 ,0.5,
                        GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(10,10,10,10), 0,0));

        winnerPanel.add(winnerNickLabel,
                new GridBagConstraints(2,0,1 ,1 ,0.5 ,0.5,
                        GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(10,10,10,10), 0,0));

        scoreboard = new ScoreboardPanel(board);
        //TODO: init scoreboard

        notificationLabel = GUIFunc.createNotificationLabel();
        modifyNotificationLabel();

        //Adding components
        add(notificationLabel,BorderLayout.NORTH);
        add(winnerPanel, BorderLayout.CENTER);
        add(scoreboard, BorderLayout.EAST);
    }

    private void modifyNotificationLabel() {
        notificationLabel.setBounds(WIDTH/4,0, WIDTH/2, 200);
        notificationLabel.setHorizontalAlignment(SwingConstants.CENTER);
        notificationLabel.setVerticalAlignment(SwingConstants.CENTER);
        notificationLabel.setFont(new Font("Raleway", Font.PLAIN, 30));
        notificationLabel.setBackground(GameColor.BOARD_COLOUR.getColor());
    }

    private JPanel setupWinnerPanel() {
        JPanel panel = new JPanel(new GridBagLayout());
        // Height isn't needed as in border layout it is automatically stretched
        panel.setPreferredSize(new Dimension(ViewCard.getScaledWidth() * 3 + 4*10,
                ViewCard.getScaledHeight() * 3 + 4 * 10 + 25));
        panel.setBackground(GameColor.BOARD_COLOUR.getColor());

    //region Add_All_Players_Score
        int gridY = 1;
        List<ViewHand> handsByScore = board.getAllPlayerHands().stream()
                .sorted((h1,h2) -> -Integer.compare(board.getScore(h1.getNickname()), board.getScore(h2.getNickname())))
                .toList();
        int scoreFontSize = 35;
        for(ViewHand hand : handsByScore){
            JLabel nickLabel = setupLabel(hand.getNickname(), hand.getColor(), scoreFontSize);
            String scoreStr = Integer.toString(board.getScore(hand.getNickname()));
            JLabel scoreLabel = setupLabel(scoreStr, null, scoreFontSize);

            panel.add(nickLabel,
                    new GridBagConstraints(0, gridY,2 ,1 ,0.5 ,0.5,
                            GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(10,10,10,10), 0,0));
            panel.add(scoreLabel,
                    new GridBagConstraints(2, gridY ,1 ,1 ,0.5 ,0.5,
                            GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(10,10,10,10), 0,0));
            gridY++;
        }
    //endregion

    //region Add_Restart_Buttons
        for (int i = 2; i <= 4; i++) {
            JButton restartButton = new JButton("RESTART "+i);
            restartButton.setFocusable(false);
            restartButton.addActionListener(this);

            int gridX = i-2;
            //gridY is as updated in the previous scoreLabel additions
            panel.add(restartButton,
                    new GridBagConstraints(gridX, gridY ,1 ,1 ,0.5 ,0.5,
                    GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(10,10,10,10), 0,0));
        }
    //endregion
        return panel;
    }

    private JLabel setupLabel(String text, PlayerColor color){
        return setupLabel(text, color, STANDARD_WINNER_TEXT_FONT);
    }
    private JLabel setupLabel(String text, PlayerColor color, int size){
        text = GUIFunc.correctToLabelFormat(text);
        JLabel label = new JLabel(text);
        label.setFont(new Font("Raleway", Font.PLAIN, size));
        label.setForeground(PlayerColor.getColor(color));
        label.setAlignmentX(CENTER_ALIGNMENT);
        label.setVisible(true);
        return label;
    }

    @Override
    public synchronized void display() {
        revalidate();
        repaint();
        setVisible(true);
    }
    @Override
    public void displayNotification(List<String> backlog) {
        displayNotification(backlog.get(0));
    }

//region NOTIFICATION LABEL METHODS
    @Override
    public synchronized void displayError(String errorMessage) {
        int displayTime =  GUIFunc.setupDisplayTimer(1.5f, displayTimer);
        notificationLabel.setForeground(GameColor.ERROR_COLOUR.getColor());
        notificationLabel.setText(GUIFunc.correctToLabelFormat(errorMessage));
        // The error will become visible
        notificationLabel.setVisible(true);
        startDisplayTimer(displayTime);
    }

    private void startDisplayTimer(int displayTime) {
        // After delay time the notification will
        // disappear from the screen
        displayTimer = new Timer(displayTime,
                (event) -> {
                    notificationLabel.setVisible(false);
                    // java.awt timers don't stop after
                    // the delay time has passed,
                    // so they need to be actively stopped
                    displayTimer.stop();
                    displayTimer = null;
                });
        displayTimer.start();
    }

    private synchronized void displayNotification(String successMessage){
        int displayTime = GUIFunc.setupDisplayTimer(2f, displayTimer);
        notificationLabel.setForeground(Color.black);
        notificationLabel.setText(GUIFunc.correctToLabelFormat(successMessage));
        notificationLabel.setVisible(true);
        startDisplayTimer(displayTime);
    }
//endregion

    @Override
    public synchronized void close() {
        setVisible(false);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        JButton button = (JButton) e.getSource();
        int restartNum = Integer.parseInt(button.getText().split(" ")[1]);
//        if(restartNum >= 2 && restartNum <= 4)
//            inputHandler.restartGame(restartNum);

        //TODO: restart GUI and send restartGame command
    }
}
