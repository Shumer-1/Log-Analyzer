package backend.academy.logReader;

import java.io.IOException;
import java.nio.file.Path;
import java.util.List;

public interface LogReader {
    String readLog() throws IOException;

    List<Path> pathsOut();

}
