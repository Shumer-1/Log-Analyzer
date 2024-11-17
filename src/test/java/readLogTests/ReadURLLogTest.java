package readLogTests;

import backend.academy.exceptions.SetUpReaderException;
import backend.academy.logReader.URLLogReader;
import com.sun.net.httpserver.HttpServer;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import lombok.SneakyThrows;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;

public class ReadURLLogTest {
    private HttpServer server;

    @SneakyThrows @BeforeEach
    void setUp() {
        server = HttpServer.create(new InetSocketAddress(0), 0);
        server.createContext("/logs.txt", exchange -> {
            String response = "line1\nline2\nline3";
            exchange.getResponseHeaders().add("Content-Type", "text/plain");
            exchange.sendResponseHeaders(200, response.getBytes().length);
            try (OutputStream os = exchange.getResponseBody()) {
                os.write(response.getBytes());
            }
        });
        server.createContext("/notFound.txt", exchange -> {
            exchange.sendResponseHeaders(404, -1);
        });
        server.createContext("/wrongType.txt", exchange -> {
            String response = "line1\nline2\nline3";
            exchange.getResponseHeaders().add("Content-Type", "application/json");
            exchange.sendResponseHeaders(200, response.getBytes().length);
            try (OutputStream os = exchange.getResponseBody()) {
                os.write(response.getBytes());
            }
        });
        server.start();
    }

    @AfterEach
    void tearDown() {
        server.stop(0);
    }

    @Test
    @SneakyThrows
    void testURLLogReaderWithHttpServer() {
        String url = "http://localhost:" + server.getAddress().getPort() + "/logs.txt";
        URLLogReader logReader = new URLLogReader(url);

        assertThat(logReader.readLog()).isEqualTo("line1");
        assertThat(logReader.readLog()).isEqualTo("line2");
        assertThat(logReader.readLog()).isEqualTo("line3");
        assertThat(logReader.readLog()).isNull();
    }

    @Test
    void testNotFoundError() {
        String url = "http://localhost:" + server.getAddress().getPort() + "/notFound.txt";
        try {
            new URLLogReader(url);
        } catch (Exception e) {
            assertThat(e).isInstanceOf(SetUpReaderException.class).hasMessageContaining(
                "Request error: expected content type 'text/plain'");
        }
    }

    @Test
    void testWrongContentType() {
        String url = "http://localhost:" + server.getAddress().getPort() + "/wrongType.txt";
        try {
            new URLLogReader(url);
        } catch (Exception e) {
            assertThat(e).isInstanceOf(SetUpReaderException.class).hasMessageContaining("expected content type 'text/plain'");
        }
    }
}
