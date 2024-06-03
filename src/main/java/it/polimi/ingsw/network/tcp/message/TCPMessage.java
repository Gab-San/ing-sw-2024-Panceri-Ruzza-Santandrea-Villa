package it.polimi.ingsw.network.tcp.message;

import java.io.Serializable;

/**
 * This interface represents a simple TCPMessage.
 * <p>
 *     A tcp message in this implementation is either
 *     a check or a normal message.
 * </p>
 */
public interface TCPMessage extends Serializable {
    long serialVersionUID = 2031613L;
    boolean isCheck();
}
