package it.polimi.ingsw.controller.timer;

import it.polimi.ingsw.controller.BoardController;
import it.polimi.ingsw.model.Player;
import it.polimi.ingsw.model.exceptions.ListenException;
import it.polimi.ingsw.model.listener.remote.errors.PingEvent;
import it.polimi.ingsw.model.listener.remote.errors.TimeoutDisconnectEvent;

public class TurnTimer implements Runnable{

    private final Player player;
    private final BoardController controller;
    private final int pingTimeSeconds = 20;
    private int secondsElapsed;
    public TurnTimer(BoardController controller, Player player, int turnTime){
        this.player = player;
        secondsElapsed = turnTime;
        this.controller = controller;
    }

    @Override
    public void run() {
        while(secondsElapsed > 0){
            try {
                if(secondsElapsed % pingTimeSeconds == 0) {
                    try {
                        player.notifyAllListeners(new PingEvent(player.getNickname()));
                    } catch (ListenException connectionException) {
                        controller.disconnect(player.getNickname());
                    }
                }

                Thread.sleep(1000);
                secondsElapsed--;


            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
        player.notifyAllListeners(new TimeoutDisconnectEvent(player.getNickname()));
        controller.disconnect(player.getNickname());
    }

}
