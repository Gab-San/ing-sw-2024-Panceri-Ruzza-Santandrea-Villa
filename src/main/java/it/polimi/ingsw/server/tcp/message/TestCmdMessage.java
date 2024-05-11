package it.polimi.ingsw.server.tcp.message;

import it.polimi.ingsw.server.VirtualClient;
import it.polimi.ingsw.server.VirtualServer;

import java.io.Serial;
import java.rmi.RemoteException;

public class TestCmdMessage implements TCPClientMessage{

    @Serial
    private static final long serialVersionUID = 10000L;
    private final String nickname;
    private final String testMsg;

    public TestCmdMessage(String nickname, String testMsg) {
        this.nickname = nickname;
        this.testMsg = testMsg;
    }


    @Override
    public void execute(VirtualServer virtualServer, VirtualClient virtualClient) throws RemoteException {
        virtualServer.testCmd(nickname, virtualClient,testMsg);
    }

    @Override
    public boolean isError() {
        return false;
    }
}
