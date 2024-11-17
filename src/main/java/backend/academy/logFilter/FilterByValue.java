package backend.academy.logFilter;

import backend.academy.log.Field;
import backend.academy.log.Log;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public class FilterByValue implements LogFilter {

    private Field field;
    private String value;

    @Override
    public boolean filter(Log log) {
        try {
            return field.check(value, log);
        } catch (NumberFormatException e) {
            return false;
        } catch (NullPointerException e) {
            return true;
        }
    }
}
