package backend.academy.log;

public record Request(String method, String resource, String httpVersion) {

    public static Request toRequest(String request) {
        String[] parts = request.split(" ");
        return new Request(parts[0], parts[1], parts[2]);
    }
}
