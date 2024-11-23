package backend.academy.input;

import backend.academy.config.Config;
import backend.academy.exceptions.ArgumentsParseException;
import lombok.experimental.UtilityClass;

@UtilityClass
public class InputReader {

    @SuppressWarnings("CyclomaticComplexity")
    public static Config parse(String[] args) throws ArgumentsParseException {
        if (args.length == 0) {
            throw new ArgumentsParseException("Invalid value: nothing entered");
        }

        Settings settings = new Settings();
        ArgumentHandler pathHandler = new PathHandler();
        ArgumentHandler filterFieldHandler = new FilterFieldHandler();
        ArgumentHandler filterValueHandler = new FilterValueHandler();
        ArgumentHandler formatHandler = new FormatHandler();
        ArgumentHandler toTimeHandler = new ToTimeHandler();
        ArgumentHandler fromTimeHandler = new FromTimeHandler();

        pathHandler.setNext(filterFieldHandler);
        filterFieldHandler.setNext(filterValueHandler);
        filterValueHandler.setNext(formatHandler);
        formatHandler.setNext(toTimeHandler);
        toTimeHandler.setNext(fromTimeHandler);

        int i = 0;
        while (i < args.length) {
            if (args[i].startsWith("--") && i + 1 == args.length) {
                throw new ArgumentsParseException("Invalid value: nothing after --{statement} ");
            }
            if (!pathHandler.handleArgument(args[i], settings, args[i + 1])) {
                throw new ArgumentsParseException("Invalid value: value without config (--{statement})");
            }
            i += 2;
        }

        if (settings.path() == null) {
            throw new ArgumentsParseException("Invalid value: path not specified");
        }

        if (settings.field() == null && !settings.value().isEmpty() || settings.field() != null
            && settings.value().isEmpty()) {
            throw new ArgumentsParseException(
                "Invalid value: only one of the two required values was entered: value or field");
        }

        return new Config(settings.path(), settings.from(), settings.to(),
            settings.outputFormat(), settings.field(), settings.value());
    }
}
