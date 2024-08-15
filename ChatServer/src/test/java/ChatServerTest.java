import org.example.ChatServer;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class ChatServerTest {

    private static final String LOG_FILE = "file.log";

    @BeforeEach
    void setUp() throws IOException {
        Files.deleteIfExists(Paths.get(LOG_FILE));
    }

    @AfterEach
    void tearDown() throws IOException {
        Files.deleteIfExists(Paths.get(LOG_FILE));
    }

    @Test
    void testServerStartupLog() throws IOException {
        ChatServer.main(new String[0]);

        String testMessage = "Test Message";
        ChatServer.broadcastMessage(testMessage);

        List<String> logLines = Files.readAllLines(Paths.get(LOG_FILE));
        assertFalse(logLines.isEmpty(), "Лог не должен быть пустым");
        assertTrue(logLines.contains(testMessage), "Лог должен содердать сообщение:" + testMessage + ".");
        assertTrue(logLines.stream().anyMatch(line -> line.contains("Сервер запущен на порту:" + ChatServer.port)));
    }
}
