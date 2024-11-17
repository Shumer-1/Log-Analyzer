package readLogTests;

import backend.academy.logReader.FilesLogReader;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;
import lombok.SneakyThrows;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import static org.assertj.core.api.Assertions.assertThat;

public class ReadGlobLogTest {

    @SneakyThrows @ParameterizedTest
    @MethodSource("getArgumentsForReadLogTest")
    void readLogTest(String path, List<String> expectedResult) {
        FilesLogReader filesLogReader = new FilesLogReader(path);

        List<String> actualResult = new ArrayList<>();
        String log = filesLogReader.readLog();
        while (log != null) {
            actualResult.add(log);
            log = filesLogReader.readLog();
        }
        actualResult.sort(String::compareTo);
        assertThat(actualResult).containsExactlyElementsOf(expectedResult);

    }

    private static Stream<Arguments> getArgumentsForReadLogTest() {
        return Stream.of(
            Arguments.of("src/test/resources/logFiles/*",
                new ArrayList<>() {{
                    add("Log1");
                    add("Log2");
                    add("LogFile");
                    add("LogTxT");
                }}
            ),
            Arguments.of("src/test/resources/logFiles/logFile1.txt",
                new ArrayList<>() {{
                    add("Log1");
                }}
            ),
            Arguments.of("src/test/resources/**/logFile1.txt",
                new ArrayList<>() {{
                    add("Log1");
                    add("Log1");
                }}
            ),
            Arguments.of("src/test/resources/logFiles/logFile?.txt",
                new ArrayList<>() {{
                    add("Log1");
                    add("Log2");
                }}
            ),
            Arguments.of("src/test/resources/logFiles/logFile{1,2}.txt",
                new ArrayList<>() {{
                    add("Log1");
                    add("Log2");
                }}
            ),
            Arguments.of("src/test/resources/logFiles/logFile.{txt,html}",
                new ArrayList<>() {{
                    add("LogFile");
                    add("LogTxT");
                }}
            ),
            Arguments.of("src/test/resources/logXYZ/[xyz].txt",
                new ArrayList<>() {{
                    add("X");
                    add("Y");
                    add("Z");
                }}
            )

        );
    }

}
