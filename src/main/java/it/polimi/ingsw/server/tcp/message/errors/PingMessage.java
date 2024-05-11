package it.polimi.ingsw.server.tcp.message.errors;

import com.diogonunes.jcolor.Attribute;
import it.polimi.ingsw.server.tcp.ClientHandler;
import it.polimi.ingsw.server.tcp.TCPClient;
import it.polimi.ingsw.server.tcp.message.TCPClientErrorMessage;
import it.polimi.ingsw.server.tcp.message.TCPServerErrorMessage;

import java.io.Serial;

import static com.diogonunes.jcolor.Ansi.colorize;

public class PingMessage implements TCPClientErrorMessage, TCPServerErrorMessage {
    @Serial
    private static final long serialVersionUID = 0000L;

    @Override
    public boolean isError() {
        return true;
    }

    @Override
    public void handle(ClientHandler server) {
        System.out.println(colorize("Pinging server...", Attribute.MAGENTA_TEXT()));
    }

    @Override
    public void handle(TCPClient client) {
        System.out.println(colorize("Pinging client...", Attribute.BLUE_TEXT()));
    }
}
