package it.polimi.ingsw.server;

// TODO: implement command pattern
//Commands should have a reference to CentralServer in order to get games or handle the disconnection (or connection loss)
public abstract class ClientUpdateCommand implements Runnable {
    @Override
    public abstract void run();
}

