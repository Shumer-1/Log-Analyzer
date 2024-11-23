package backend.academy.input;

import backend.academy.exceptions.ArgumentsParseException;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;

public class FromTimeHandler implements ArgumentHandler {

    private ArgumentHandler nextHandler = null;
    private final String argument = "--from";

    @Override
    public boolean handleArgument(String argument, Settings settings, String value) throws ArgumentsParseException {
        if (!this.argument.equals(argument)) {
            if (nextHandler != null) {
                return nextHandler.handleArgument(argument, settings, value);
            }
            return false;
        }
        try {
            settings.from(LocalDate.parse(value));
        } catch (DateTimeParseException e) {
            throw new ArgumentsParseException("Invalid value after --from");
        }
        return true;
    }

    @Override
    public void setNext(ArgumentHandler argumentHandler) {
        nextHandler = argumentHandler;
    }
}
