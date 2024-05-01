package it.polimi.ingsw.server.tcp;

import it.polimi.ingsw.server.tcp.message.TCPMessage;

import java.io.PrintWriter;

public class ServerProxy implements VirtualServerTest{
    private final PrintWriter output;
    ServerProxy(PrintWriter writer){
        this.output = writer;
    }
    @Override
    public void sendMsg(TCPMessage msg) {
        output.println(msg.serialize());
        output.flush();
    }
    public void sendMsg(String msg){
        output.println(msg);
        output.flush();
    }
    @Override
    public void placeCard(TCPMessage placeMsg){

    }

    @Override
    public void choose(TCPMessage chooseMsg) {

    }

    @Override
    public void draw(TCPMessage drawMsg) {

    }

    @Override
    public void start(TCPMessage startMsg) {

    }

    @Override
    public void disconnect(TCPMessage disconnectMsg) {

    }
}
