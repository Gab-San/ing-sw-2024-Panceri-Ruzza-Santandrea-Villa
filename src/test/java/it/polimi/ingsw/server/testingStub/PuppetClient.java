package it.polimi.ingsw.server.testingStub;

import com.diogonunes.jcolor.Attribute;
import it.polimi.ingsw.Point;
import it.polimi.ingsw.server.CommandPassthrough;

import java.rmi.RemoteException;

import static com.diogonunes.jcolor.Ansi.colorize;

public class PuppetClient implements CommandPassthrough {

    private final Attribute textColorFormat = Attribute.BRIGHT_CYAN_TEXT();

    @Override
    public void sendMsg(String msg) {
        System.out.println(colorize("SEND COMMAND CALLED", textColorFormat));
    }

    @Override
    public void connect(String nickname) throws IllegalStateException {
        System.out.println(colorize("CONNECT COMMAND CALLED WITH ARGS:\n"
                + nickname, textColorFormat));
    }

    @Override
    public void setNumOfPlayers(int num) throws IllegalStateException {
        System.out.println(colorize("SET NUM OF PLAYERS COMMAND CALLED WITH ARGS:\n"
                + num , textColorFormat));
    }

    @Override
    public void disconnect() throws IllegalStateException {
        System.out.println(colorize("DISCONNECT COMMAND CALLED", textColorFormat));
    }

    @Override
    public void placeStartCard(boolean placeOnFront) throws IllegalStateException {
        System.out.println(colorize("PLACE STARTING CARD COMMAND CALLED WITH ARGS:\n"
                + placeOnFront, textColorFormat));
    }

    @Override
    public void chooseColor(char color) throws IllegalStateException {
        System.out.println(colorize("CHOOSE COLOR COMMAND CALLED WITH ARGS:\n"
                + color, textColorFormat));
    }

    @Override
    public void chooseObjective(int choice) throws IllegalStateException {
        System.out.println(colorize("CHOOSE OBJECTIVE COMMAND CALLED WITH ARGS:\n"
                + choice, textColorFormat));
    }

    @Override
    public void placeCard(String cardID, Point placePos, String cornerDir, boolean placeOnFront) throws IllegalStateException, RemoteException {
        System.out.println(colorize("PLACE CARD COMMAND CALLED WITH ARGS:\n"
                + cardID + "\n" +
                placePos + "\n" +
                cornerDir + "\n" +
                placeOnFront, textColorFormat));
    }

    @Override
    public void draw(char deck, int card) throws IllegalStateException {
        System.out.println(colorize("DRAW COMMAND CALLED WITH ARGS:\n"
                + deck + "\n"
                + card , textColorFormat));
    }

    @Override
    public void restartGame(int numOfPlayers) throws IllegalStateException {
        System.out.println(colorize("START GAME COMMAND CALLED WITH ARGS:\n"
                + numOfPlayers, textColorFormat));
    }

    @Override
    public void ping() throws RemoteException {
        System.out.println("Ping Called");
    }
}
