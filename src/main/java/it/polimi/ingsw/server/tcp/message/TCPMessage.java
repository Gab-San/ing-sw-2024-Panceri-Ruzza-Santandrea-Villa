package it.polimi.ingsw.server.tcp.message;

import java.io.Serializable;

public interface TCPMessage extends Serializable {
    //TODO check if necessary
    long serialVersionUID = 2031613L;
    boolean isCheck();
}
