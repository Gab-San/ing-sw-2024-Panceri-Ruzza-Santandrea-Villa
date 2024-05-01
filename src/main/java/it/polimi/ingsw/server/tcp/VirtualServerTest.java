package it.polimi.ingsw.server.tcp;

import it.polimi.ingsw.server.tcp.message.TCPMessage;

public interface VirtualServerTest {
    public void sendMsg(TCPMessage msg);
    public void placeCard(TCPMessage placeMsg);
    public void choose(TCPMessage chooseMsg);
    public void draw(TCPMessage drawMsg);
    public void start(TCPMessage startMsg);
    public void disconnect(TCPMessage disconnectMsg);
}
