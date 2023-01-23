package logger;

import settings.MessageSettings;

import java.io.*;

public class Logger {

    private final File log;
    private PrintWriter out;
    public static Logger logger;

    private Logger() {
        log = new File("log.txt");
        if (!log.exists()) {
            try {
                log.createNewFile();
            } catch (IOException exception) {
                System.out.println(MessageSettings.getProperty("cannotCreateLog"));
            }
        }
    }

    public synchronized boolean log(String message, LogType type, boolean timeIsSet) {
        try (FileWriter fileWriter = new FileWriter(log, true);
             BufferedWriter bufferedWriter = new BufferedWriter(fileWriter)) {
            out = new PrintWriter(bufferedWriter);
            if (!timeIsSet) {
                message = DateTime.getCurrentTime() + message;
            }
            switch (type) {
                case MESSAGE -> out.println("message-" + message);
                case INFO -> out.println("info-" + message);
                case ERROR -> out.println("!ERROR-" + message);
            }
            return true;
        } catch (IOException exception) {
            System.out.println(MessageSettings.getProperty("noAccessToLog"));
            return false;
        } finally {
            out.close();
        }
    }

    public static Logger getInstance() {
        if (logger == null) {
            synchronized (Logger.class) {
                if (logger == null) {
                    logger = new Logger();
                }
            }
        }
        return logger;
    }
}
