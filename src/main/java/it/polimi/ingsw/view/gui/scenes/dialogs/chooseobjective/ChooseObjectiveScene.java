package it.polimi.ingsw.view.gui.scenes.dialogs.chooseobjective;

import it.polimi.ingsw.CornerDirection;
import it.polimi.ingsw.GamePoint;
import it.polimi.ingsw.view.gui.ChangeNotifications;
import it.polimi.ingsw.view.gui.GUIFunc;
import it.polimi.ingsw.view.gui.GUI_Scene;
import it.polimi.ingsw.view.gui.GameInputHandler;
import it.polimi.ingsw.view.model.cards.ViewObjectiveCard;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.rmi.RemoteException;
import java.util.List;
import java.util.ListIterator;

public class ChooseObjectiveScene extends JDialog implements PropertyChangeListener, GUI_Scene, MouseListener, ActionListener {
    private final GameInputHandler inputHandler;
    private final List<ViewObjectiveCard> secretCards;
    private final JLabel errorLabel;
    private Timer displayTimer;
    private final JButton selectButton;
    private ViewObjectiveCard selectedCard;

    public ChooseObjectiveScene(JFrame owner, String title, GameInputHandler inputHandler){
        super(owner, title, true);
        this.inputHandler = inputHandler;
        this.secretCards =  inputHandler.getLocalPlayer().getSecretObjectives();
        setLayout(new GridBagLayout());
        JLabel cardLabel = createObjectiveLabel();
        errorLabel = createErrorLabel();
        selectButton = createSelectionButton();
        //Adding components
        addGridComponent(cardLabel, 0, 0,1, GridBagConstraints.EAST,
                GridBagConstraints.VERTICAL, new Insets(0,0,0,20));
        addObjectives();
        addGridComponent(selectButton, 2,1,1, GridBagConstraints.CENTER,
                GridBagConstraints.NONE, new Insets(0,0,0,0));
        addGridComponent(errorLabel, 0,1,2, GridBagConstraints.CENTER, GridBagConstraints.NONE,
                new Insets(0,0,0,10));
    }

    private JButton createSelectionButton() {
        JButton button = new JButton();
        button.setText("CONFIRM CHOICE");
        button.setFocusable(false);
        button.addActionListener(this);
        return button;
    }

    private void addObjectives() {
        ListIterator<ViewObjectiveCard> iterator = secretCards.listIterator();
        while (iterator.hasNext()) {
            ViewObjectiveCard objectiveCard = iterator.next();
            int count = iterator.nextIndex();
            SwingUtilities.invokeLater(
                    () -> {
                        addGridComponent(objectiveCard, count, 0,1,
                                GridBagConstraints.CENTER, GridBagConstraints.NONE,
                                new Insets(0, 0, 0, 0));
                        revalidate();
                        repaint();
                    }
            );

            objectiveCard.addMouseListener(this);
        }
    }

    private void deselectCards() {
        secretCards.forEach(
                c -> c.setBorder(BorderFactory.createEmptyBorder())
        );
    }

    private JLabel createErrorLabel() {
        JLabel label = new JLabel();
        label.setText("");
        label.setEnabled(false);
        label.setFocusable(false);
        label.setForeground(Color.red);
        return label;
    }

    private void addGridComponent(Component component, int x, int y, int width,
                                  int anchor, int fill, Insets insets){
        add(component, new GridBagConstraints(x, y, width, 1, 1.0, 1.0, anchor, fill,
                insets, 0, 0));
    }

    private JLabel createObjectiveLabel() {
        JLabel label = new JLabel();
        label.setText("CHOOSE YOUR SECRET OBJECTIVE:");
        return label;
    }


    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        SwingUtilities.invokeLater(this::close);
    }

    @Override
    public synchronized void display() {
        GUIFunc.setupDialog(this, 1200, 400);
        setVisible(true);
    }

    @Override
    public synchronized void displayError(String error) {
        displayError(error, 1.5f, false);
    }

    private synchronized void displayError(String error, float displayTimeSec, boolean isClosing) {
        int displayTime = GUIFunc.setupDisplayTimer(displayTimeSec, displayTimer);
        errorLabel.setText(GUIFunc.correctToLabelFormat(error));
        errorLabel.setEnabled(true);
        errorLabel.setVisible(true);
        // After delay time the notification will
        // disappear from the screen
        displayTimer = new Timer(displayTime,
                (event) -> {
                    errorLabel.setEnabled(false);
                    errorLabel.setVisible(false);
                    // java.awt timers don't stop after
                    // the delay time has passed,
                    // so they need to be actively stopped
                    displayTimer.stop();
                    displayTimer = null;
                    if(isClosing){
                        close();
                    }
                });
        displayTimer.start();
    }


    @Override
    public synchronized void displayNotification(List<String> backlog) {

    }

    @Override
    public synchronized void moveView(List<CornerDirection> cornerDirections) {

    }

    @Override
    public void setCenter(int row, int col) {

    }

    @Override
    public void setCenter(GamePoint center) {

    }

    @Override
    public synchronized void close() {
        this.dispose();
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        assert e.getSource() instanceof ViewObjectiveCard;
        deselectCards();
        selectedCard = (ViewObjectiveCard) e.getSource();
        selectedCard.setBorder(BorderFactory.createLineBorder(Color.red, 3));
    }

    @Override
    public void mousePressed(MouseEvent e) {

    }

    @Override
    public void mouseReleased(MouseEvent e) {

    }

    @Override
    public void mouseEntered(MouseEvent e) {
        assert e.getSource() instanceof ViewObjectiveCard;
        ViewObjectiveCard card = (ViewObjectiveCard) e.getSource();
        card.setFocusable(true);
        card.setEnabled(true);
    }

    @Override
    public void mouseExited(MouseEvent e) {
        assert e.getSource() instanceof ViewObjectiveCard;
        ViewObjectiveCard card = (ViewObjectiveCard) e.getSource();
        card.setFocusable(false);
        card.setEnabled(false);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        try {
            deselectCards();
            selectButton.setEnabled(false);
            inputHandler.chooseObjective(selectedCard);
            for(ViewObjectiveCard objectiveCard : secretCards){
                objectiveCard.removeMouseListener(this);
            }
        } catch (IllegalStateException ex){
            displayError(ex.getMessage(), 2f, false);
            selectButton.setEnabled(true);
        } catch (RemoteException ex) {
            displayError("Connection Lost!", 1.5f, true);
            inputHandler.notifyDisconnection();
        }
    }
}
