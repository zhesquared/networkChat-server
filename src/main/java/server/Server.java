package server;

import logger.LogType;
import logger.Logger;
import settings.MessageSettings;
import settings.ServerSettings;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

import static logger.DateTime.getCurrentTime;

public class Server {

    private ServerSocket serverSocket;
    private Logger logger = Logger.getInstance();
    private List<Messages> usersInChat = new CopyOnWriteArrayList<>();

    public boolean startServer() {
        int port = Integer.parseInt(ServerSettings.getProperty("port"));
        try {
            serverSocket = new ServerSocket(port);
        } catch (IOException exception) {
            logger.log(MessageSettings.getProperty("unableServer"), LogType.INFO, false);
            return false;
        }
        String start = MessageSettings.getProperty("enableServer");
        System.out.println(getCurrentTime() + " " + start);
        logger.log(start, LogType.INFO, false);
        return true;
    }

    public void connectionListener() {
        while (true) {
            try {
                Socket clientSocket = serverSocket.accept();
                new Thread(new Messages(clientSocket, usersInChat)).start();
            } catch (IOException exception) {
                logger.log(MessageSettings.getProperty("serverErrors"), LogType.ERROR, false);
                break;
            }
        }
    }
}
