package backend.academy.input;

import backend.academy.exceptions.ArgumentsParseException;
import backend.academy.log.Field;

public class FilterFieldHandler implements ArgumentHandler {

    private ArgumentHandler nextHandler = null;
    private final String argument = "--filter-field";

    @Override
    public boolean handleArgument(String argument, Settings settings, String value) throws ArgumentsParseException {
        if (!this.argument.equals(argument)) {
            if (nextHandler != null) {
                return nextHandler.handleArgument(argument, settings, value);
            }
            return false;
        }
        try {
            settings.field(Field.valueOf(value.toUpperCase()));
        } catch (IllegalArgumentException e) {
            throw new ArgumentsParseException("Invalid value after --filter-field");
        }
        return true;
    }

    @Override
    public void setNext(ArgumentHandler argumentHandler) {
        this.nextHandler = argumentHandler;
    }
}
