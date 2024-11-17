package backend.academy.log;

import backend.academy.exceptions.LogParseException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Locale;

public record Log(String clientIpAddress, String userId, String username,
                  LocalDate timestamp, Request request, int httpStatusCode,
                  long responseSize, String referrer, String userAgent) {

    //119.249.216.1 - - [30/Oct/2024:14:18:24 +0000] "GET /tertiary/5th%20generation.php HTTP/1.1" 200 2743 "-" "Mozilla/5.0 (Macintosh; U; Intel Mac OS X 10_6_8) AppleWebKit/5342 (KHTML, like Gecko) Chrome/38.0.831.0 Mobile Safari/5342"

    public static Log toLog(String logLine) throws LogParseException {
        try {
            String clientIpAddress = logLine.substring(0, logLine.indexOf(' '));
            logLine = logLine.substring(logLine.indexOf(' ') + 1);

            String clientId = logLine.substring(0, logLine.indexOf(' '));
            logLine = logLine.substring(logLine.indexOf(' ') + 1);

            String username = logLine.substring(0, logLine.indexOf(' '));
            logLine = logLine.substring(logLine.indexOf(' ') + 1);

            int cursor = 0;
            while (logLine.charAt(cursor) != ']') {
                cursor++;
            }
            cursor++;
            LocalDate timestamp = getDate(logLine.substring(0, cursor));
            logLine = logLine.substring(cursor + 1);

            cursor = 1;
            while (logLine.charAt(cursor) != '"') {
                cursor++;
            }
            cursor++;
            Request request = Request.toRequest(logLine.substring(1, cursor - 1));
            logLine = logLine.substring(cursor + 1);

            int httpStatusCode = Integer.parseInt(logLine.substring(0, logLine.indexOf(' ')));
            logLine = logLine.substring(logLine.indexOf(' ') + 1);

            long responseSize = Long.parseLong(logLine.substring(0, logLine.indexOf(' ')));
            logLine = logLine.substring(logLine.indexOf(' ') + 1);

            cursor = 1;
            while (logLine.charAt(cursor) != '"') {
                cursor++;
            }
            cursor++;
            String referrer = logLine.substring(1, cursor - 1);

            String userAgent = logLine.substring(cursor + 2, logLine.length() - 2);

            return new Log(clientIpAddress, clientId, username,
                timestamp, request, httpStatusCode,
                responseSize, referrer, userAgent);
        } catch (Exception e) {
            throw new LogParseException("Invalid value: log parse failure");
        }
    }

    private static LocalDate getDate(String timestamp) throws DateTimeParseException {
        int cursor = 0;
        while (timestamp.charAt(cursor) != ':') {
            cursor++;
        }

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MMM/yyyy", Locale.ENGLISH);
        String date = timestamp.substring(1, cursor);
        return LocalDate.parse(date, formatter);
    }
}
