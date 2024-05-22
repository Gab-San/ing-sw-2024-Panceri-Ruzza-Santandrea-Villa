package it.polimi.ingsw.network.tcp.message;

import java.io.Serializable;

public interface TCPMessage extends Serializable {
    long serialVersionUID = 2031613L;
    boolean isCheck();
}
