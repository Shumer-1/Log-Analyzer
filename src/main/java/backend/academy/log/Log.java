package backend.academy.log;

import backend.academy.exceptions.LogParseException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Locale;

@SuppressWarnings("RecordComponentNumber")
public record Log(String clientIpAddress, String userId, String username,
                  LocalDate timestamp, Request request, int httpStatusCode,
                  long responseSize, String referrer, String userAgent) {

    public static Log toLog(String logLine) throws LogParseException {
        try {
            String log = logLine.replaceAll("[\\[\\]\"\n]", "");

            String[] parts = log.split(" ");
            int partNumber = 0;
            String clientIpAddress = parts[partNumber++];

            String clientId = parts[partNumber++];

            String username = parts[partNumber++];

            LocalDate timestamp = getDate(parts[partNumber++]);

            partNumber++;

            Request request = new Request(parts[partNumber++], parts[partNumber++], parts[partNumber++]);

            int httpStatusCode = Integer.parseInt(parts[partNumber++]);

            long responseSize = Long.parseLong(parts[partNumber++]);

            String referrer = parts[partNumber++];

            String userAgent;
            StringBuilder temp = new StringBuilder();
            for (int i = partNumber; i < parts.length; i++) {
                temp.append(parts[i]);
                if (i != parts.length - 1) {
                    temp.append(" ");
                }
            }
            userAgent = temp.toString();

            return new Log(clientIpAddress, clientId, username,
                timestamp, request, httpStatusCode,
                responseSize, referrer, userAgent);
        } catch (Exception e) {
            throw new LogParseException("Invalid value: log parse failure");
        }
    }

    private static LocalDate getDate(String timestamp) throws DateTimeParseException {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MMM/yyyy:HH:mm:ss", Locale.ENGLISH);
        LocalDateTime dateTime = LocalDateTime.parse(timestamp, formatter);
        return dateTime.toLocalDate();
    }
}
