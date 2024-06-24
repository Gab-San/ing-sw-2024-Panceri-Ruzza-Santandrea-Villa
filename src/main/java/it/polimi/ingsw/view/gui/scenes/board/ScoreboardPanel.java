package it.polimi.ingsw.view.gui.scenes.board;

import it.polimi.ingsw.PlayerColor;
import it.polimi.ingsw.view.GameColor;
import it.polimi.ingsw.view.gui.ChangeNotifications;
import it.polimi.ingsw.view.gui.GUIFunc;
import it.polimi.ingsw.view.gui.GameWindow;
import it.polimi.ingsw.view.model.ViewBoard;
import it.polimi.ingsw.view.model.ViewHand;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import static it.polimi.ingsw.view.gui.ChangeNotifications.COLOR_CHANGE;
import static it.polimi.ingsw.view.gui.ChangeNotifications.SCORE_CHANGE;

/**
 * This class represents the scoreboard panel. Displays score for each player.
 */
public class ScoreboardPanel extends JPanel implements PropertyChangeListener {
//region PANEL DIMENSIONS
    private static final float scaleFactor = GameWindow.SCALE_FACTOR;
    private final int PANEL_WIDTH = (int) (350 * scaleFactor);
    private final int PANEL_HEIGHT = (int) (1100 * scaleFactor);
    private final int SCOREBOARD_WIDTH = (int) (484/1.5f * scaleFactor);
    private final int SCOREBOARD_HEIGHT = (int) (948/1.5f * scaleFactor);
//endregion

//region COMPONENTS CONSTANTS AND DIMENSIONS
    private final int xOffset = (getWidth() - SCOREBOARD_WIDTH)/2;
    private final int yOffset = (getHeight() - SCOREBOARD_HEIGHT)/4;
    private final int gridGap = Math.round(6 * scaleFactor);
    private final int compSide = 46;
//endregion
    private static final String SCOREBOARD_PATH = GUIFunc.getGraphicsResourcesRootPath() + "icons/plateau_score.jpg";
    private BufferedImage scoreboardImage;
    private final ViewBoard board;
    private final Map<Integer, JPanel> scorePosition;
    private final Map<String, Integer> oldScore;
    private final PlayerPawn redPlayer, yellowPlayer, greenPlayer, bluePlayer;

    /**
     * Constructs scoreboard panel.
     * @param board playing board
     */
    public ScoreboardPanel(ViewBoard board){
        // Image import
        if(scoreboardImage == null){
            try(InputStream is = this.getClass().getClassLoader().getResourceAsStream(SCOREBOARD_PATH)){
                assert is != null;
                scoreboardImage = ImageIO.read(is);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }

        this.board = board;
        board.addPropertyChangeListener(SCORE_CHANGE,this);
        this.scorePosition = initializeScorePosition();
        this.oldScore = new HashMap<>();

        setPreferredSize(new Dimension(PANEL_WIDTH, PANEL_HEIGHT));
        setBackground(GameColor.SCOREBOARD_COLOUR.getColor());
        setLayout(null);

        redPlayer = new PlayerPawn(GameColor.PLAYER_RED.getColor());
        bluePlayer = new PlayerPawn(GameColor.PLAYER_BLUE.getColor());
        yellowPlayer = new PlayerPawn(GameColor.PLAYER_YELLOW.getColor());
        greenPlayer = new PlayerPawn(GameColor.PLAYER_GREEN.getColor());
    }

    // Positioning absolute values panel in which
    // tokens will be placed
    private Map<Integer, JPanel> initializeScorePosition() {
        Map<Integer, JPanel> mapScorePanel = new HashMap<>();
        int maxOffsetBottomFromLeftSide = 65;
        int offsetFromBottomSide = 26;
        int i = 0;
        int hgap = 27;
        // 0 to 2 share same y;
        int yFirstRow = (int) (yOffset + SCOREBOARD_HEIGHT - (offsetFromBottomSide + compSide) * scaleFactor);
        while(i < 3){
            int x = (int) (xOffset + (maxOffsetBottomFromLeftSide + (compSide + hgap) * i) * scaleFactor);
            JPanel panel = initializePanel(x, yFirstRow);
            mapScorePanel.put(i, panel);
            i++;
        }
        // 3 to 19 are positioned in a grid, so the position
        // can be algorithmically defined.
        // Starting from right to left
        int dirX = -1;
        int minOffsetFromLeftSide = 29;
        int offsetFromRightSide = 28;
        int vgap = 21;
        while (i < 20){
            if((i-3)%4 == 0 && i != 3){
                dirX *= -1;
            }
            // When going from right to left must start from the right side of the scoreboard
            int x = (int) (xOffset + (dirX == -1 ? SCOREBOARD_WIDTH : 0)
                                + (dirX * ((dirX == -1 ? offsetFromRightSide : minOffsetFromLeftSide)
                                + (dirX == -1 ? compSide : 0) + (hgap + compSide) * ((i - 3) % 4))) * scaleFactor);

            int y = (int) (yOffset + SCOREBOARD_HEIGHT - ((offsetFromBottomSide + compSide + vgap
                                + compSide + (vgap + compSide) * ((i-3)/4))) * scaleFactor);

            JPanel panel = initializePanel(x,y);
            mapScorePanel.put(i, panel);
            i++;
        }

        // 21 to 23 share same x
        int x = (int) (xOffset + minOffsetFromLeftSide * scaleFactor);
        i = 21;
        while(i < 24){
            int y = (int) (yOffset + SCOREBOARD_HEIGHT - (offsetFromBottomSide + compSide + vgap
                                + compSide + (vgap + compSide) * (4 + i % 21))* scaleFactor);
            JPanel panel = initializePanel(x,y);
            mapScorePanel.put(i, panel);
            i++;
        }

        // Numbers 20 to 29 have different position,
        // so it is simpler to position them manually
        int y;
        JPanel panel;

        i = 20;
        x = xOffset + Math.round((minOffsetFromLeftSide + compSide + 63) * scaleFactor);
        // y position of the number 22
        int y22 = offsetFromBottomSide + compSide + vgap + compSide + (vgap + compSide) * 5;
        y = (int) (yOffset + SCOREBOARD_HEIGHT - (y22 - 34)* scaleFactor);
        panel = initializePanel(x,y);
        mapScorePanel.put(i, panel);

        // 20 and 29 have the same x
        i = 29;
        int y23 = (offsetFromBottomSide + compSide + vgap + compSide + (vgap + compSide) * 6);
        y = yOffset + SCOREBOARD_HEIGHT - Math.round( (y23 - 14)* scaleFactor);
        panel = initializePanel(x,y);
        mapScorePanel.put(i, panel);

        int minOffsetFromTop = 27;
        int maxOffsetFromTop = 38;
        int maxOffsetTopFromLeftSide = 71;
        i = 24;
        x = xOffset + Math.round(maxOffsetTopFromLeftSide * scaleFactor);
        y = yOffset + Math.round(maxOffsetFromTop * scaleFactor);
        panel= initializePanel(x,y);
        mapScorePanel.put(i, panel);
        // 26 has same y of 24
        i = 26;
        // The offset of the top part from the left side is the same of the right side
        x = xOffset + SCOREBOARD_WIDTH - Math.round((maxOffsetTopFromLeftSide + compSide) * scaleFactor);
        panel = initializePanel(x,y);
        mapScorePanel.put(i,panel);

        i = 25;
        x = xOffset + Math.round((maxOffsetTopFromLeftSide + compSide + 21) * scaleFactor );
        y = yOffset + Math.round(minOffsetFromTop * scaleFactor);
        panel = initializePanel(x,y);
        mapScorePanel.put(i, panel);

        i = 27;
        x = xOffset + SCOREBOARD_WIDTH - Math.round((offsetFromRightSide + compSide) * scaleFactor);
        y = (int) (yOffset + SCOREBOARD_HEIGHT - y23 * scaleFactor);
        panel = initializePanel(x,y);
        mapScorePanel.put(i, panel);

        i = 28;
        y = (int) (yOffset + SCOREBOARD_HEIGHT - y22 * scaleFactor);
        panel = initializePanel(x,y);
        mapScorePanel.put(i, panel);

        return mapScorePanel;
    }

    private JPanel initializePanel(int x, int y) {
        JPanel panel = new JPanel(new GridLayout(2,2,gridGap,gridGap));
        int actualCompSide = Math.round(compSide * scaleFactor);
        panel.setBounds(x,y,actualCompSide, actualCompSide);
        panel.setSize(actualCompSide,actualCompSide);
        panel.setPreferredSize(new Dimension(actualCompSide,actualCompSide));
        panel.setMaximumSize(new Dimension(actualCompSide,actualCompSide));
        panel.setOpaque(false);
        this.add(panel);
        return panel;
    }

    @Override
    public int getWidth() {
        return PANEL_WIDTH;
    }

    @Override
    public int getHeight() {
        return PANEL_HEIGHT;
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D graphics2D = (Graphics2D) g;


        graphics2D.drawImage(scoreboardImage, xOffset, yOffset,
                SCOREBOARD_WIDTH, SCOREBOARD_HEIGHT, null);
    }

    /**
     * Adjourns player color token on scoreboard track.
     * @param evt A PropertyChangeEvent object describing the event source
     *          and the property that has changed.
     */
    @Override
    public synchronized void propertyChange(PropertyChangeEvent evt) {
        switch (evt.getPropertyName()) {
            case SCORE_CHANGE:
                handleScoreChange(evt);
                break;
            case COLOR_CHANGE:
                assert evt.getSource() instanceof ViewHand;
                ViewHand hand = (ViewHand) evt.getSource();
                String playerNick = hand.getNickname();
                assert evt.getNewValue() instanceof PlayerColor;
                PlayerColor color = (PlayerColor) evt.getNewValue();
                // When color is chosen the player are still in setup phase
                oldScore.put(playerNick, 0);
                JPanel panel = scorePosition.get(oldScore.get(playerNick));
                if(color == null){
                    return;
                }
                SwingUtilities.invokeLater(
                        () -> addColor(panel, color)
                );
                break;
        }
    }

    private void handleScoreChange(PropertyChangeEvent evt) {
        assert evt.getNewValue() instanceof ViewHand;
        ViewHand hand = (ViewHand) evt.getNewValue();
        if (hand.getColor() == null) {
            if(Arrays.asList(hand.getPropertyChangeListeners()).contains(this)) return;
            hand.addPropertyChangeListener(ChangeNotifications.COLOR_CHANGE, this);
            return;
        }
        // If another score is already present than the token shall
        // not display on it.
        if (oldScore.containsKey(hand.getNickname())) {
            int prevScore = oldScore.get(hand.getNickname());
            JPanel panel = scorePosition.get(prevScore);
            SwingUtilities.invokeLater(
                    () -> removeColor(panel, hand.getColor())
            );
        }

        int currentScore = board.getScore(hand.getNickname());
        if(currentScore > 29) return;
        oldScore.put(hand.getNickname(), currentScore);
        JPanel scorePanel = scorePosition.get(currentScore);
        SwingUtilities.invokeLater(
                () -> addColor(scorePanel, hand.getColor())
        );
    }

    private void addColor(JPanel scorePanel, PlayerColor color) {
        PlayerPawn pawn = switch (color) {
            case RED -> redPlayer;
            case YELLOW -> yellowPlayer;
            case BLUE -> bluePlayer;
            case GREEN -> greenPlayer;
        };
        scorePanel.add(pawn);
    }

    private void removeColor(JPanel panel,PlayerColor c) {
        switch (c){
            case RED:
                panel.remove(redPlayer);
                break;
            case BLUE:
                panel.remove(bluePlayer);
                break;
            case GREEN:
                panel.remove(greenPlayer);
                break;
            case YELLOW:
                panel.remove(yellowPlayer);
                break;
        }
    }
}
