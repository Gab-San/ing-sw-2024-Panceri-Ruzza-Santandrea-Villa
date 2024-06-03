package it.polimi.ingsw.network.tcp.message.commands;

import it.polimi.ingsw.network.VirtualClient;
import it.polimi.ingsw.network.VirtualServer;
import it.polimi.ingsw.network.tcp.message.TCPClientMessage;

import java.io.Serial;
import java.rmi.RemoteException;

/**
 * This class implements tcp client message interface. Sent when a choice action is requested.
 * <p>
 *     This message can carry either one of the following information: <br>
 *     - chosen color; <br>
 *     - chosen objective.
 * </p>
 */
public class ChooseMessage implements TCPClientMessage {

    @Serial
    private static final long serialVersionUID = 0011L;
    private final boolean isColor;
    private final String nickname;
    private final int choice;
    private final char color;

    /**
     * Constructs choose message for objective choice.
     * @param nickname unique id of the user
     * @param choice index of the chosen objective card
     */
    public ChooseMessage(String nickname, int choice) {
        this.choice = choice;
        this.isColor = false;
        this.nickname = nickname;
        this.color = '0';
    }

    /**
     * Constructs choose message for color choice
     * @param nickname unique id of the user
     * @param color identifier of the player color
     */
    public ChooseMessage(String nickname, char color){
        this.nickname = nickname;
        this.color = color;
        this.isColor = true;
        this.choice = -1;
    }


    @Override
    public void execute(VirtualServer virtualServer, VirtualClient virtualClient) throws RemoteException {
        if(isColor){
            virtualServer.chooseColor(nickname, virtualClient, color);
            return;
        }

        virtualServer.chooseObjective(nickname, virtualClient, choice);
    }


    @Override
    public boolean isCheck() {
        return false;
    }
}
