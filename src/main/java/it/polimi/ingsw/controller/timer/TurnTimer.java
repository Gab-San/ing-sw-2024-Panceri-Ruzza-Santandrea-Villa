package it.polimi.ingsw.controller.timer;

import it.polimi.ingsw.controller.BoardController;
import it.polimi.ingsw.model.Player;

public class TurnTimer implements Runnable{

    private final Player player;
    private final BoardController controller;
    private boolean reset;
    private final int turnTime;
    private int secondsElapsed;
    public TurnTimer(BoardController controller, Player player, int turnTime){
        this.player = player;
        this.turnTime = turnTime;
        secondsElapsed = turnTime;
        reset = false;
        this.controller = controller;
    }

    @Override
    public void run() {
        while(secondsElapsed > 0){
            if(reset){
                secondsElapsed = turnTime;
                reset = false;
            }
            try {

//                try {
//                    player.pingListener(player.getNickname());
                    System.err.println("Seconds remaining " + secondsElapsed);
//                } catch (RemoteException e) {
//                    controller.disconnect(player.getNickname());
//                    break;
//                }

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
