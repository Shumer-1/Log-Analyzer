package backend.academy.input;

import backend.academy.exceptions.ArgumentsParseException;
import backend.academy.writer.OutputFormat;

public class FormatHandler implements ArgumentHandler {

    private ArgumentHandler nextHandler = null;
    private final String argument = "--format";

    @Override
    public boolean handleArgument(String argument, Settings settings, String value) throws ArgumentsParseException {
        if (!this.argument.equals(argument)) {
            if (nextHandler != null) {
                return nextHandler.handleArgument(argument, settings, value);
            }
            return false;
        }
        try {
            settings.outputFormat(OutputFormat.valueOf(value.toUpperCase()));
        } catch (IllegalArgumentException e) {
            throw new ArgumentsParseException("Invalid value after --format");
        }
        return true;
    }

    @Override
    public void setNext(ArgumentHandler argumentHandler) {
        this.nextHandler = argumentHandler;
    }
}
