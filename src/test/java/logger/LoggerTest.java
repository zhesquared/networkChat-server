package logger;

import org.junit.jupiter.api.Test;

import java.io.PrintWriter;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class LoggerTest {

    private Logger logger;

    @Test
    public void incomeMessageLogReturnTrue() {
        String message = "test message";
        PrintWriter writer = mock(PrintWriter.class);

        logger = Logger.getInstance();

        boolean actual = logger.log(message, LogType.MESSAGE, false);

        assertTrue(actual);
    }
}
