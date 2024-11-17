package backend.academy.log;

import backend.academy.exceptions.LogParseException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Locale;

@SuppressWarnings("RecordComponentNumber")
public record Log(String clientIpAddress, String userId, String username,
                  LocalDate timestamp, Request request, int httpStatusCode,
                  long responseSize, String referrer, String userAgent) {

    public static Log toLog(String logLine) throws LogParseException {
        try {
            String log = logLine;
            String clientIpAddress = log.substring(0, log.indexOf(' '));
            log = log.substring(log.indexOf(' ') + 1);

            String clientId = log.substring(0, log.indexOf(' '));
            log = log.substring(log.indexOf(' ') + 1);

            String username = log.substring(0, log.indexOf(' '));
            log = log.substring(log.indexOf(' ') + 1);

            int cursor = 0;
            while (log.charAt(cursor) != ']') {
                cursor++;
            }
            cursor++;
            LocalDate timestamp = getDate(log.substring(0, cursor));
            log = log.substring(cursor + 1);

            cursor = 1;
            while (log.charAt(cursor) != '"') {
                cursor++;
            }
            cursor++;
            Request request = Request.toRequest(log.substring(1, cursor - 1));
            log = log.substring(cursor + 1);

            int httpStatusCode = Integer.parseInt(log.substring(0, log.indexOf(' ')));
            log = log.substring(log.indexOf(' ') + 1);

            long responseSize = Long.parseLong(log.substring(0, log.indexOf(' ')));
            log = log.substring(log.indexOf(' ') + 1);

            cursor = 1;
            while (log.charAt(cursor) != '"') {
                cursor++;
            }
            cursor++;
            String referrer = log.substring(1, cursor - 1);

            String userAgent = log.substring(cursor + 2, log.length() - 2);

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
