package backend.academy.logReader;

import backend.academy.exceptions.SetUpReaderException;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import lombok.Getter;

public class URLLogReader implements LogReader {
    private final String url;
    private final HttpClient client;
    private final HttpRequest request;
    private HttpResponse<InputStream> response;
    private BufferedReader reader;
    @Getter private List<Path> pathsOut = new ArrayList<>();

    public URLLogReader(String url) throws SetUpReaderException, IOException, InterruptedException {
        this.url = url;
        pathsOut.add(Path.of(url));
        this.client = HttpClient.newHttpClient();
        request = HttpRequest.newBuilder()
            .uri(URI.create(url))
            .GET()
            .header("Accept", "text/plain")
            .build();
        setUpReader();
    }

    private void setUpReader() throws SetUpReaderException, IOException, InterruptedException {
        response = client.send(request, HttpResponse.BodyHandlers.ofInputStream());

        String contentType = response.headers().firstValue("Content-Type").orElse("");

        if (response.statusCode() == 200 && contentType.contains("text/plain")) {
            reader = new BufferedReader(new InputStreamReader(response.body()));
        } else {
            throw new SetUpReaderException(
                "Request error: expected content type 'text/plain' but received '" + contentType + "'"
            );
        }
    }

    @Override
    public String readLog() {
        String line;
        try {
            line = reader.readLine();
            return line;
        } catch (IOException e) {
            throw new RuntimeException("IO Error while reading from URL: " + url, e);
        }
    }
}
