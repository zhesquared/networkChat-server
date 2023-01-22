package server;

import logger.LogType;
import logger.Logger;
import settings.MessageSettings;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.List;

import static logger.DateTime.getCurrentTime;

public class Messages implements Runnable {

    private final Socket clientSocket;
    private PrintWriter out;
    private BufferedReader in;
    private String username;
    private List<Messages> usersOnline;
    private final Logger logger = Logger.getInstance();

    public Messages(Socket clientSocket, List<Messages> usersOnline) {
        this.clientSocket = clientSocket;
        this.usersOnline = usersOnline;
    }

    public PrintWriter getOut() {
        return out;
    }

    public void setOut(PrintWriter out) {
        this.out = out;
    }

    public void setIn(BufferedReader in) {
        this.in = in;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    @Override
    public void run() {
        try {
            out = new PrintWriter(clientSocket.getOutputStream(), true);
            in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
            register();
            while (messageReader());
        } catch (IOException exception) {
            usersOnline.remove(this);
            String info = formatEvent("' " + username + "' leave this chat");
            messagePrinter(getCurrentTime() + " " + info);
            logger.log(info, LogType.INFO, true);
        } finally {
            out.close();
            try {
                in.close();
            } catch (IOException exception) {
                exception.printStackTrace();
            }
        }
    }

    public boolean register() {
        out.println(MessageSettings.getProperty("enterUsername"));
        String name;
        try {
            do {
                name = in.readLine();
            } while (userExist(name));
            username = name;
            String info = formatEvent("'" + username + "' has joined the chat");
            messagePrinter(info);
            logger.log(info, LogType.INFO, true);
            out.println(getCurrentTime() + " " + MessageSettings.getProperty("userGreetings"));
            usersOnline.add(this);
            return true;
        } catch (IOException exception) {
            System.out.println(getCurrentTime() + " " + MessageSettings.getProperty("userLeaving"));
            return false;
        }
    }

    public boolean userExist(String name) {
        for (Messages user : usersOnline) {
            if (user.getUsername().equals(name)) {
                out.println(getCurrentTime() + " " + MessageSettings.getProperty("userIsExist"));
                return true;
            }
        }
        return false;
    }

    public boolean messageReader() throws IOException {
        String message = in.readLine();
        if ("/exit".equalsIgnoreCase(message)) {
            String info = formatEvent("'" + username + "' leaving this chat");
            messagePrinter(info);
            usersOnline.remove(this);
            logger.log(info, LogType.INFO, true);
            return false;
        } else if (message != null) {
            message = formatMessage(message);
            messagePrinter(message);
            logger.log(message, LogType.MESSAGE, true);
        }
        return true;
    }

    public void messagePrinter(String message) {
        System.out.println(message);
        for (Messages user : usersOnline) {
            if (!user.getUsername().equals(username)) {
                user.getOut().println(message);
            }
        }
    }

    public String formatEvent(String msg) {
        return getCurrentTime() + msg;
    }

    public String formatMessage(String msg) {
        return getCurrentTime() + username + ": " + msg;
    }
}
