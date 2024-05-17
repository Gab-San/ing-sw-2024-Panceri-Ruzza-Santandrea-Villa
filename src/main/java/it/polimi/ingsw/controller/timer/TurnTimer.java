package it.polimi.ingsw.controller.timer;

import it.polimi.ingsw.controller.BoardController;
import it.polimi.ingsw.listener.remote.RemoteHandler;
import it.polimi.ingsw.model.Player;
import it.polimi.ingsw.listener.remote.events.PingEvent;
import it.polimi.ingsw.model.exceptions.ListenException;

public class TurnTimer implements Runnable{

    private final Player player;
    private final BoardController controller;
    private boolean reset;
    private final int turnTime;
    private int secondsElapsed;
    private final RemoteHandler remoteHandler;
    public TurnTimer(BoardController controller, Player player, int turnTime){
        this.player = player;
        this.turnTime = turnTime;
        secondsElapsed = turnTime;
        reset = false;
        this.controller = controller;
        remoteHandler = RemoteHandler.getInstance();
    }

    @Override
    public void run() {
        while(secondsElapsed > 0){
            if(reset){
                secondsElapsed = turnTime;
                reset = false;
            }
            try {
                try {
                    player.notifyListener(remoteHandler, new PingEvent(player.getNickname()));
                    System.err.println("Seconds remaining " + secondsElapsed);
                } catch (ListenException connectionException){
                    controller.disconnect(player.getNickname());
                }

                Thread.sleep(1000);
                secondsElapsed--;


            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
        controller.disconnect(player.getNickname());
    }

    public void reset(){
        reset = true;
    }
}
