package logParserTests;

import backend.academy.exceptions.LogParseException;
import backend.academy.log.Log;
import backend.academy.log.Request;
import java.time.LocalDate;
import java.util.stream.Stream;
import lombok.SneakyThrows;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import static org.assertj.core.api.Assertions.assertThat;

public class LogParserTest {

    @ParameterizedTest @SneakyThrows
    @MethodSource("getArgumentsForToLogWithoutExceptionTest")
    void toLogWithoutExceptionTest(String inputValue, Log expectedResult) {
        Log log = Log.toLog(inputValue);

        assertThat(log).isEqualTo(expectedResult);
    }

    @ParameterizedTest
    @MethodSource("getArgumentsForToLogWithExceptionTest")
    void toLogWithExceptionTest(String inputValue) {
        try {
            Log.toLog(inputValue);
        } catch (LogParseException e) {
            assertThat(e).isInstanceOf(LogParseException.class).hasMessageContaining(
                "Invalid value: log parse failure"
            );
        }
    }

    private static Stream<Arguments> getArgumentsForToLogWithExceptionTest() {
        return Stream.of(
            Arguments.of("strange string"),
            // нет client_id
            Arguments.of("93.120.197.68 - [30/Oct/2024:11:44:04 +0000] \"HEAD /Managed_" +
                "dedicated_incremental_uniform.htm HTTP/1.1\" 200 2122 \"-\" \"Opera/10.61 (Macintosh; U; " +
                "Intel Mac OS X 10_9_6; en-US) Presto/2.13.313 Version/10.00\"\n", ""),
            // нет метода
            Arguments.of("93.120.197.68 - [30/Oct/2024:11:44:04 +0000] \"/Managed_" +
                "dedicated_incremental_uniform.htm HTTP/1.1\" 200 2122 \"-\" \"Opera/10.61 (Macintosh; U; " +
                "Intel Mac OS X 10_9_6; en-US) Presto/2.13.313 Version/10.00\"\n"),
            // день некорректный
            Arguments.of("93.120.197.68 - [299/Oct/2024:11:44:04 +0000] \"HEAD /Managed_" +
                "dedicated_incremental_uniform.htm HTTP/1.1\" 2122 \"-\" \"Opera/10.61 (Macintosh; U; " +
                "Intel Mac OS X 10_9_6; en-US) Presto/2.13.313 Version/10.00\"\n"),
            // лишний элемент
            Arguments.of("93.120.197.68 - [299/Oct/2024:11:44:04 +0000] \"HEAD /Managed_" +
                "dedicated_incremental_uniform.htm HTTP/1.1\" 200 10101 2122 \"-\" \"Opera/10.61 (Macintosh; U; " +
                "Intel Mac OS X 10_9_6; en-US) Presto/2.13.313 Version/10.00\"\n")

        );
    }

    private static Stream<Arguments> getArgumentsForToLogWithoutExceptionTest() {
        return Stream.of(
            Arguments.of("93.120.197.68 - - [30/Oct/2024:11:44:04 +0000] \"HEAD /Managed_" +
                    "dedicated_incremental_uniform.htm HTTP/1.1\" 200 2122 \"-\" \"Opera/10.61 (Macintosh; U; " +
                    "Intel Mac OS X 10_9_6; en-US) Presto/2.13.313 Version/10.00\"\n",
                new Log("93.120.197.68", "-", "-", LocalDate.parse("2024-10-30"),
                    new Request("HEAD", "/Managed_dedicated_incremental_uniform.htm", "HTTP/1.1"),
                    200, 2122, "-", "Opera/10.61 (Macintosh; U; Intel Mac OS" +
                    " X 10_9_6; en-US) Presto/2.13.313 Version/10.00")
            ),
            Arguments.of("178.75.57.150 - - [30/Oct/2024:11:44:04 +0000] \"GET /interface%20motivating" +
                    "/Monitored.php HTTP/1.1\" 301 68 \"-\" \"Mozilla/5.0 (Windows NT 5.0; en-US; rv:1.9.1.20) " +
                    "Gecko/1995-29-01 Firefox/37.0\"\n",
                new Log("178.75.57.150", "-", "-", LocalDate.parse("2024-10-30"),
                    new Request("GET", "/interface%20motivating/Monitored.php", "HTTP/1.1"),
                    301, 68, "-", "Mozilla/5.0 (Windows NT 5.0; en-US; rv:1.9.1.20) Gecko/1995-29-01 Firefox/37.0")
            ),
            Arguments.of("45.164.131.128 - - [30/Oct/2024:11:44:04 +0000] \"DELETE /Implemented_" +
                    "Configurable%20radical-customer%20loyalty/empowering.png HTTP/1.1\" 200 3007 \"-\" \"Opera/8.30 " +
                    "(Macintosh; Intel Mac OS X 10_7_8; en-US) Presto/2.10.187 Version/11.00\"\n",
                new Log("45.164.131.128", "-", "-", LocalDate.parse("2024-10-30"),
                    new Request("DELETE", "/Implemented_Configurable%20radical-customer%20loyalty/empowering.png",
                        "HTTP/1.1"), 200, 3007, "-", "Opera/8.30 " +
                    "(Macintosh; Intel Mac OS X 10_7_8; en-US) Presto/2.10.187 Version/11.00")
            )
        );
    }
}
