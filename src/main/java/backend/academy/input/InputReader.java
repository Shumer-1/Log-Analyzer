package backend.academy.input;

import backend.academy.config.Config;
import backend.academy.exceptions.ArgumentsParseException;
import backend.academy.log.Field;
import backend.academy.writer.OutputFormat;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;

public class InputReader {

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
            } else if ("--filter-field".equals(args[i])) {
                try {
                    field = Field.valueOf(args[i + 1].toUpperCase());
                } catch (IllegalArgumentException e) {
                    throw new ArgumentsParseException("Invalid value after --filter-field");
                }
            } else if ("--path".equals(args[i])) {
                path = args[i + 1];
                flagPath = true;
            } else if ("--filter-value".equals(args[i])) {
                value = args[i + 1];
            } else if ("--from".equals(args[i]) && i + 1 < args.length) {
                try {
                    from = LocalDate.parse(args[i + 1]);
                } catch (DateTimeParseException e) {
                    throw new ArgumentsParseException("Invalid value after --from");
                }
            } else if ("--to".equals(args[i])) {
                try {
                    to = LocalDate.parse(args[i + 1]);
                } catch (DateTimeParseException e) {
                    throw new ArgumentsParseException("Invalid value after --to");
                }
            } else if ("--format".equals(args[i])) {
                try {
                    outputFormat = OutputFormat.valueOf(args[i + 1].toUpperCase());
                } catch (IllegalArgumentException e) {
                    throw new ArgumentsParseException("Invalid value after --format");
                }
            } else {
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
