package it.polimi.ingsw.view.gui.scenes.board;

import it.polimi.ingsw.CornerDirection;
import it.polimi.ingsw.GamePoint;
import it.polimi.ingsw.view.Scene;
import it.polimi.ingsw.view.gui.GUI;
import it.polimi.ingsw.view.gui.GameWindow;
import it.polimi.ingsw.view.model.ViewBoard;

import javax.swing.*;
import java.awt.*;
import java.util.List;

/**
 * This class implements the scene interface and displays the board.
 */
public class BoardScene extends JPanel implements Scene {
    private final PlayersPanel playersPanel;
    private final InformationPanel infoPanel;
    private final BoardPanel boardPanel;

    public BoardScene(GUI gui){
        setPreferredSize(new Dimension(GameWindow.SCREEN_WIDTH, GameWindow.HEIGHT_WIDTH));
        setLayout(new BorderLayout());

        //players panel
        playersPanel = new PlayersPanel();

        //scoreboard and chat panel
        infoPanel = new InformationPanel();

        //board panel
        ViewBoard board = gui.getBoard();
        boardPanel = new BoardPanel(board.getResourceCardDeck(), board.getGoldCardDeck(), board.getObjectiveCardDeck());

        //add components
        add(playersPanel, BorderLayout.LINE_START);
        add(infoPanel, BorderLayout.LINE_END);
        add(boardPanel, BorderLayout.CENTER);
        //Make components visible
        playersPanel.setVisible(true);
        infoPanel.setVisible(true);
        boardPanel.setVisible(true);
    }

    @Override
    public void display() {
        repaint();
        setVisible(true);
    }

    @Override
    public void displayError(String error) {

    }

    @Override
    public void displayNotification(List<String> backlog) {

    }

    @Override
    public void displayChatMessage(List<String> backlog) {

    }

    @Override
    public void moveView(List<CornerDirection> cornerDirections) {

    }

    @Override
    public void setCenter(int row, int col) {

    }

    @Override
    public void setCenter(GamePoint center) {

    }
}
