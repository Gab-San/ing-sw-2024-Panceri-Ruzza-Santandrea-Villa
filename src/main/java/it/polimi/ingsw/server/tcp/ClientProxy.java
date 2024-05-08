package it.polimi.ingsw.server.tcp;

import it.polimi.ingsw.server.VirtualClient;
import it.polimi.ingsw.server.tcp.message.PingMessage;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.PrintWriter;
import java.rmi.RemoteException;

public class ClientProxy implements VirtualClient {

    private final PrintWriter writer;
    private final ObjectOutputStream outputStream;
    private final BufferedReader reader;
    public ClientProxy(ObjectOutputStream outputStream, PrintWriter writer, BufferedReader reader){
        this.writer = writer;
        this.outputStream = outputStream;
        this.reader = reader;
    }

    void start(){
        System.out.println("Started");
        writer.println("CONNECTED TO SERVER");
        writer.flush();
    }


    @Override
    public void update(String msg) throws RemoteException {
        synchronized (writer) {
            writer.println(msg);
            writer.flush();
        }
    }

    @Override
    public void ping() throws RemoteException {
        try {
            outputStream.writeObject(new PingMessage(false));
        } catch (IOException e) {
            throw new RemoteException("Connection Lost: ", e);
        }

        try{
            String updateMsg;
            if ((updateMsg = reader.readLine()) != null){
                String msg = updateMsg.split("\\s+")[0];
                if(msg.equalsIgnoreCase("ping")) {
                    System.out.println(updateMsg);
                }
            } else {
                throw new RemoteException("Connection Lost: ");
            }
        } catch (IOException ioException){
            throw new RemoteException("Connection Lost: ", ioException);
        }

    }

    void pingClient(){
        synchronized (writer){
            writer.println("Ping received...");
            writer.flush();
        }
    }

    @Override
    public void close(){
        writer.close();
    }
}
