package it.polimi.ingsw.view;

import it.polimi.ingsw.network.CommandPassthrough;
import it.polimi.ingsw.network.rmi.RMIClient;
import it.polimi.ingsw.network.tcp.client.TCPClientSocket;
import it.polimi.ingsw.view.gui.GUI;
import it.polimi.ingsw.view.tui.TUI;

import java.io.IOException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.util.Scanner;

import static it.polimi.ingsw.view.tui.ConsoleTextColors.*;

public class Client {
    public static View view = null;

    public static void cls(){
        synchronized (System.out) {
            //TODO: delete \n screen before release (needed for IDE console cls)
            boolean runningIntelliJ = false;
            try {
                runningIntelliJ = Client.class.getClassLoader().loadClass("com.intellij.rt.execution.application.AppMainV2") != null;
            } catch (ClassNotFoundException ignored) {
            }

            if (runningIntelliJ) System.out.print("\n".repeat(50));
            else {
                System.out.print("\033[H\033[2J");
                //FIXME: [Ale] choose which cls to use
                //source: https://stackoverflow.com/questions/2979383/how-to-clear-the-console-using-java
                try {
                    if (System.getProperty("os.name").contains("Windows"))
                        new ProcessBuilder("cmd", "/c", "cls").inheritIO().start().waitFor();
                    else Runtime.getRuntime().exec("clear");
                } catch (Exception ignored) {
                }
            }
            System.out.flush();
        }
    }

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        String serverIP = "", connectionTech = "";
        int port = -1;
        try {
            if (args.length > 2) {
                //first arg is the server IP
                serverIP = args[0];
                //second arg is the connection tech
                connectionTech = args[1];
                port = Integer.parseInt(args[2]);
            } else { //if running server on local machine, the only parameter is TCP/RMI
                serverIP = "localhost";
                connectionTech = args[0];
                port = Integer.parseInt(args[1]);
            }
        } catch (IndexOutOfBoundsException e) {
            System.err.println("Please pass valid parameters: <serverIP> <connection technology [TCP/RMI]> <serverPort>");
            System.exit(-1);
        } catch (NumberFormatException e) {
            System.err.println("Server port parameter must be a number.");
            System.exit(-1);
        }

        if (!serverIP.matches("\\d.\\d.\\d.\\d")) {
            System.err.println("Server IP parameter must be an IP address: x.y.z.w");
            System.exit(-1);
        }
        while(true) {
            try {
                CommandPassthrough proxy = null;
                try {
                    if (connectionTech.equalsIgnoreCase("tcp")) {
                        proxy = new TCPClientSocket(serverIP, port).getProxy();
                    } else if (connectionTech.equalsIgnoreCase("rmi")) {
                        proxy = new RMIClient(serverIP, port).getProxy();
                    } else {
                        System.err.println("Wrong connection technology passed as parameter. Must be TCP/RMI");
                        System.exit(-1);
                    }
                } catch (IOException | NotBoundException e) {
                    System.err.println("Couldn't locate server. Wrong IP or port");
                    System.exit(-1);
                }
                System.out.println(GREEN_TEXT + "Server located successfully!" + RESET);
                System.out.println("If the above text is not green, TUI may not be supported on the current console.");
                while (view == null) {
                    System.out.println("Enter the game mode (TUI or GUI). Please use fullscreen (Alt+Enter) if selecting TUI.");
                    System.out.print("> ");
                    String gameMode = scanner.nextLine();
                    if (gameMode.equalsIgnoreCase("GUI")) {
                        scanner.close();
                        view = new GUI(proxy); // proxy always not null at this point
                    }
                    else if (gameMode.equalsIgnoreCase("TUI")) {
                        scanner.close();
                        view = new TUI(proxy); // proxy always not null at this point
                    } else {
                        System.out.println(RED_TEXT + "Invalid input." + RESET);
                        view = null;
                    }
                    if(view != null) view.run();
                }
            } catch (RemoteException e) {
                cls();
                view = null;
                System.out.println(RED_TEXT + "Server connection lost. Trying to recover." + RESET);
            }
        }

    }
}