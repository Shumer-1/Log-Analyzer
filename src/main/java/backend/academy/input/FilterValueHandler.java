package backend.academy.input;

import backend.academy.exceptions.ArgumentsParseException;

public class FilterValueHandler implements ArgumentHandler {

    private ArgumentHandler nextHandler = null;
    private final String argument = "--filter-value";

    @Override
    public boolean handleArgument(String argument, Settings settings, String value) throws ArgumentsParseException {
        if (!this.argument.equals(argument)) {
            if (nextHandler != null) {
                return nextHandler.handleArgument(argument, settings, value);
            }
            return false;
        }
        settings.value(value);
        return true;
    }

    @Override
    public void setNext(ArgumentHandler argumentHandler) {
        this.nextHandler = argumentHandler;
    }
}
