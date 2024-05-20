package it.polimi.ingsw.network.tcp.message.commands;

import it.polimi.ingsw.network.VirtualClient;
import it.polimi.ingsw.network.VirtualServer;
import it.polimi.ingsw.network.tcp.message.TCPClientMessage;

import java.io.Serial;
import java.rmi.RemoteException;

public class PlaceCardMessage implements TCPClientMessage {
    @Serial
    private static final long serialVersionUID = 7382L;
    private final String nickname;
    private final String cardID;
    private final int xPosition;
    private final int yPosition;
    private final String cornDirection;
    private final boolean placeOnFront;

    public PlaceCardMessage(String nickname, String cardID,
                            int xPosition, int yPosition, String cornDirection, boolean placeOnFront) {
        this.nickname = nickname;
        this.cardID = cardID;
        this.xPosition = xPosition;
        this.yPosition = yPosition;
        this.cornDirection = cornDirection;
        this.placeOnFront = placeOnFront;
    }

    @Override
    public void execute(VirtualServer virtualServer, VirtualClient virtualClient) throws RemoteException {
        virtualServer.placeCard(nickname, virtualClient, cardID,
                xPosition, yPosition,cornDirection, placeOnFront);
    }

    @Override
    public boolean isCheck() {
        return false;
    }
}
