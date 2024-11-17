package backend.academy.logReader;

import backend.academy.exceptions.SetUpReaderException;
import java.io.IOException;
import java.util.regex.Pattern;
import lombok.experimental.UtilityClass;

@UtilityClass
public class LogReaderManager {
    private static final String URL_REGEX = "^https://[^/]+(/[^?]*)?";

    public static LogReader getLogReader(String path) throws SetUpReaderException, IOException, InterruptedException {
        if (Pattern.matches(URL_REGEX, path)) {
            return new URLLogReader(path);
        }
        return new FilesLogReader(path);
    }

}
