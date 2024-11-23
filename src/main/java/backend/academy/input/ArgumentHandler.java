package backend.academy.input;

import backend.academy.exceptions.ArgumentsParseException;

public interface ArgumentHandler {
    boolean handleArgument(String argument, Settings settings, String value) throws ArgumentsParseException;

    void setNext(ArgumentHandler argumentHandler);
}
