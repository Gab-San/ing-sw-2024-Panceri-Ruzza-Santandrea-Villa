package it.polimi.ingsw.server.tcp.message;

import it.polimi.ingsw.server.VirtualClient;
import it.polimi.ingsw.server.VirtualServer;

import java.io.Serial;
import java.rmi.RemoteException;

public class ChooseMessage implements TCPClientMessage{

    @Serial
    private static final long serialVersionUID = 0011L;
    private final boolean isColor;
    private final String nickname;
    private final int choice;
    private final char color;

    public ChooseMessage(String nickname, int choice) {
        this.choice = choice;
        this.isColor = false;
        this.nickname = nickname;
        this.color = '0';
    }

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
    public boolean isError() {
        return false;
    }
}
