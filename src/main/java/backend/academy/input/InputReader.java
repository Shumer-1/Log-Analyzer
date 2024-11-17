package backend.academy.input;

import backend.academy.config.Config;
import backend.academy.exceptions.ArgumentsParseException;
import backend.academy.log.Field;
import backend.academy.writer.OutputFormat;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import lombok.experimental.UtilityClass;

@UtilityClass
public class InputReader {

    @SuppressWarnings("CyclomaticComplexity")
    public static Config parse(String[] args) throws ArgumentsParseException {
        if (args.length == 0) {
            throw new ArgumentsParseException("Invalid value: nothing entered");
        }

        String path = "";
        LocalDate from = LocalDate.MIN;
        LocalDate to = LocalDate.MAX;
        OutputFormat outputFormat = OutputFormat.MARKDOWN;
        Field field = null;
        String value = "";

        boolean flagPath = false;
        int i = 0;
        while (i < args.length) {
            if (args[i].startsWith("--") && i + 1 == args.length) {
                throw new ArgumentsParseException("Invalid value: nothing after --{statement} ");
            }
            switch (args[i]) {
                case "--filter-field":
                    try {
                        field = Field.valueOf(args[i + 1].toUpperCase());
                    } catch (IllegalArgumentException e) {
                        throw new ArgumentsParseException("Invalid value after --filter-field");
                    }
                    break;
                case "--path":
                    path = args[i + 1];
                    flagPath = true;
                    break;
                case "--filter-value":
                    value = args[i + 1];
                    break;
                case "--from":
                    try {
                        from = LocalDate.parse(args[i + 1]);
                    } catch (DateTimeParseException e) {
                        throw new ArgumentsParseException("Invalid value after --from");
                    }
                    break;
                case "--to":
                    try {
                        to = LocalDate.parse(args[i + 1]);
                    } catch (DateTimeParseException e) {
                        throw new ArgumentsParseException("Invalid value after --to");
                    }
                    break;
                case "--format":
                    try {
                        outputFormat = OutputFormat.valueOf(args[i + 1].toUpperCase());
                    } catch (IllegalArgumentException e) {
                        throw new ArgumentsParseException("Invalid value after --format");
                    }
                    break;
                default:
                    throw new ArgumentsParseException("Invalid value: value without config (--{statement})");
            }
            i += 2;
        }


        if (!flagPath) {
            throw new ArgumentsParseException("Invalid value: path not specified");
        }

        if (field == null && !value.isEmpty() || field != null && value.isEmpty()) {
            throw new ArgumentsParseException(
                "Invalid value: only one of the two required values was entered: value or field");
        }

        return new Config(path, from, to, outputFormat, field, value);
    }
}
