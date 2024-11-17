package backend.academy.logFilter;

import backend.academy.log.Log;
import java.time.LocalDate;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public class FilterByTime implements LogFilter {

    private final LocalDate from;
    private final LocalDate to;

    @Override
    public boolean filter(Log log) {
        return (log.timestamp().isAfter(from) || log.timestamp().isEqual(from))
            && (log.timestamp().isBefore(to) || log.timestamp().isEqual(to));
    }
}
