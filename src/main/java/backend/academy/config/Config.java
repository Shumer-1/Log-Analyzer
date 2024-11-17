package backend.academy.config;

import backend.academy.log.Field;
import backend.academy.writer.OutputFormat;
import java.time.LocalDate;

public record Config(String path, LocalDate from, LocalDate to,
                     OutputFormat format, Field field, String value) {
}
