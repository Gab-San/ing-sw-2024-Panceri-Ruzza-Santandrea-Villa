package it.polimi.ingsw.view.tui;

import it.polimi.ingsw.network.CommandPassthrough;
import it.polimi.ingsw.network.Parser;
import it.polimi.ingsw.view.Client;
import it.polimi.ingsw.view.SceneID;
import it.polimi.ingsw.view.View;
import it.polimi.ingsw.view.model.ViewBoard;
import it.polimi.ingsw.view.model.ViewPlayArea;
import it.polimi.ingsw.view.model.ViewPlayerHand;
import it.polimi.ingsw.view.tui.scenes.PrintBoardUI;
import it.polimi.ingsw.view.tui.scenes.PrintNicknameSelectUI;
import it.polimi.ingsw.view.tui.scenes.PrintOpponentUI;
import it.polimi.ingsw.view.tui.scenes.PrintPlayerUI;

import java.rmi.RemoteException;
import java.util.Scanner;

public class TUI extends View{
    Parser parser;
    ViewBoard board;
    Scanner scanner;

    public TUI(CommandPassthrough serverProxy) throws RemoteException {
        super(serverProxy, new PrintNicknameSelectUI());
        sceneIDMap.put(SceneID.getNicknameSelectSceneID(), currentScene);
        runNicknameSelectScene();

        // at this point, connection has concluded successfully.
        currentScene = sceneIDMap.get(SceneID.getMyAreaSceneID());
        //TODO: initialise ModelUpdater
        //      we probably should pass it to virtualClient after nickname select
    }

    private void runNicknameSelectScene() throws RemoteException{
        currentScene.display();
        String nickname;
        String regex = "[a-zA-Z0-9_]+";
        do {
            nickname = scanner.nextLine();
            if(nickname.matches(regex)){
                try{
                    //TODO: instantiate and pass ModelUpdater to the VirtualClient
                    board = new ViewBoard(nickname);
                    ViewPlayerHand myHand = board.getPlayerHand();
                    ViewPlayArea myArea = board.getPlayerArea(myHand.getNickname());
                    sceneIDMap.put(SceneID.getBoardSceneID(), new PrintBoardUI(board));
                    sceneIDMap.put(SceneID.getMyAreaSceneID(), new PrintPlayerUI(myHand, myArea));
                    parser = new Parser(serverProxy, board);
                    parser.parseCommand("connect " + nickname);
                }catch (IllegalStateException e){
                    currentScene.displayError("Join failed. Server can't accomodate you now.\n" + e.getMessage());
                    nickname = "";
                }
            }
            else currentScene.displayError("Invalid nickname, only use letters, numbers and _.");
        }while (!nickname.matches(regex));
    }

    public void run() throws RemoteException{
        TUI_Scene.cls();
        currentScene.display();
        //TODO: get command input and send to parser
    }

    @Override
    public void update(SceneID sceneID, String description) {
        TUI_Scene scene = (TUI_Scene) sceneIDMap.get(sceneID);
        if(scene != null){
            if(currentScene.equals(scene)){
                currentScene.display();
            }
            else{
                if(description != null && !description.isEmpty())
                    showNotification(description);
            }
        }
        else if(sceneID.isOpponentAreaScene()){
            String nick = sceneID.getNickname();
            sceneIDMap.put(sceneID, new PrintOpponentUI(board.getOpponentHand(nick), board.getPlayerArea(nick)));
            update(sceneID, description); // won't loop indefinitely as next iteration will have scene != null
        }
    }

    @Override
    public void showError(String errorMsg) {
        currentScene.displayError(errorMsg);
    }

    @Override
    public void showNotification(String notification) {
        currentScene.displayNotification(notification);
    }
}
