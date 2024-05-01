package it.polimi.ingsw.server.tcp;

import it.polimi.ingsw.server.CentralServer;
import it.polimi.ingsw.server.Commands.TestCmd;
import it.polimi.ingsw.server.tcp.message.BaseMessage;
import it.polimi.ingsw.server.tcp.message.TCPMessage;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

public class ClientHandler extends Thread {
    private final CentralServer centralServer;
    private final BlockingQueue<TCPMessage> commands;
    private final BufferedReader reader;
    private final PrintWriter output;

    //FIXME: FLOW STATE:
    // - ARRIVA IL COMANDO
    // - IL CLIENT HANDLER FA UN PARSING
    // - AGGIUNGE ALLA SUA CODA DI COMANDI
    // - UN THREAD ESEGUE I COMANDI IN FILA
    // (IN TEORIA IN BASE AL COMANDO DOVREBBE CREARE UN THREAD
    // CHE SVOLGE IL COMANDO OPPURE AGGIUNGERE IN CODA AI COMANDI AL CENTRAL SERVER)
    public ClientHandler(BufferedReader reader, PrintWriter output, CentralServer centralServer) {
        this.centralServer = centralServer;
        this.commands = new LinkedBlockingQueue<>();
        this.reader = reader;
        this.output = output;
    }

    @Override
    public void run() {
        try{
            output.println("CONNECTED TO SERVER");
            output.flush();
            String inputLine;
            while ((inputLine = reader.readLine()) != null) {
                String[] fullCmd = inputLine.split("\\s+");
                switch (fullCmd[0]) {
                    case "place":
                    case "play":
                        output.println("Message not recognised");
                        output.flush();
                        break;
                    case "send":
                        String msg = new BaseMessage(fullCmd[1]).deserialize();
                        output.println(msg);
                        output.flush();
                        centralServer.issueGameCommand(new TestCmd(centralServer.getGameRef(),"Giovanni", msg));
                        break;
                    default:
                        output.println("Msg not recognised");
                        output.flush();
                        break;
                }
            }
        } catch (IOException | InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
}
