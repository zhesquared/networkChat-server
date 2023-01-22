package server;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.*;
import java.net.Socket;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class MessagesTest {

    private Messages messages;
    private Socket clientSocket;
    private BufferedReader income;
    private PrintWriter outcome;

    @BeforeEach
    public void mockDependencies() {
        clientSocket = mock(Socket.class);
        income = mock(BufferedReader.class);
        outcome = mock(PrintWriter.class);
    }

    @AfterEach
    public void resetDependencies() {
        clientSocket = null;
        income = null;
        outcome = null;
        messages = null;
    }

    @Test
    public void userExistenceTestFalse() {
        List<Messages> usersOnline = new CopyOnWriteArrayList<>();
        messages = new Messages(clientSocket, usersOnline);
        messages.setIn(income);
        messages.setOut(outcome);

        assertFalse(messages.userExist("username"));
    }

    @Test
    public void userExistenceTestTrue() {
        Messages user = new Messages(clientSocket, new CopyOnWriteArrayList<>());
        user.setUsername("username");
        List<Messages> usersOnline = new CopyOnWriteArrayList<>(List.of(user));

        messages = new Messages(clientSocket, usersOnline);
        messages.setIn(income);
        messages.setOut(outcome);

        assertTrue(messages.userExist("username"));
    }

    @Test
    public void exitMessageIncome() throws IOException {
        when(income.readLine()).thenReturn("test");

        List<Messages> usersOnline = new CopyOnWriteArrayList<>();

        messages = new Messages(clientSocket, usersOnline);
        messages.setIn(income);
        messages.setOut(outcome);
        messages.setUsername("username");

        assertTrue(messages.messageReader());
    }
}
