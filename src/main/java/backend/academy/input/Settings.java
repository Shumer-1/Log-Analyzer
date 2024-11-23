package backend.academy.input;

import backend.academy.log.Field;
import backend.academy.writer.OutputFormat;
import java.time.LocalDate;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Settings {
    private String path = null;
    private LocalDate from = LocalDate.MIN;
    private LocalDate to = LocalDate.MAX;
    private OutputFormat outputFormat = OutputFormat.MARKDOWN;
    private Field field = null;
    private String value = "";
}
