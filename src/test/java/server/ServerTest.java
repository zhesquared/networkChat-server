package server;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class ServerTest {

    Server server;

    @Test
    public void serverStartTest() {
        server = new Server();
        Assertions.assertTrue(server.startServer());
        server = null;
    }
}
