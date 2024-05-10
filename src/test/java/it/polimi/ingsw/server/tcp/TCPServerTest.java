package it.polimi.ingsw.server.tcp;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class TCPServerTest {

    @Test
    void startServerException(){

        assertThrows(IllegalArgumentException.class,
                () -> new TCPServer(80000));

    }

}