package logAnalyzerTests;

import backend.academy.log.Log;
import backend.academy.log.Request;
import backend.academy.statistics.LogAnalyzer;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.Mockito;
import static org.assertj.core.api.Assertions.assertThat;

public class LogAnalyzerTest {

    private static final Log MOCKED_LOG = Mockito.mock(Log.class);

    @ParameterizedTest
    @MethodSource("getArgumentsForAnalyzeClientPartLogTest")
    void analyzeClientPartLogTest(List<String> clientIpAddresses, List<Map.Entry<String, Long>> expectedResults) {
        LogAnalyzer logAnalyzer = new LogAnalyzer();
        Mockito.when(MOCKED_LOG.request()).thenReturn(new Request("", "", ""));
        Mockito.when(MOCKED_LOG.httpStatusCode()).thenReturn(0);
        Mockito.when(MOCKED_LOG.responseSize()).thenReturn(0L);
        for (String clientIpAddress : clientIpAddresses) {
            Mockito.when(MOCKED_LOG.clientIpAddress()).thenReturn(clientIpAddress);
            logAnalyzer.analyzeLog(MOCKED_LOG);
        }

        assertThat(logAnalyzer.getMostActiveUsers()).isEqualTo(expectedResults);
    }

    @ParameterizedTest
    @MethodSource("getArgumentsForAnalyzeMethodPartLogTest")
    void analyzeMethodPartLogTest(List<String> methods, List<Map.Entry<String, Long>> expectedResults) {
        LogAnalyzer logAnalyzer = new LogAnalyzer();
        Mockito.when(MOCKED_LOG.httpStatusCode()).thenReturn(0);
        Mockito.when(MOCKED_LOG.responseSize()).thenReturn(0L);
        Mockito.when(MOCKED_LOG.clientIpAddress()).thenReturn("");
        for (String method : methods) {
            Mockito.when(MOCKED_LOG.request()).thenReturn(new Request(method, "", ""));
            logAnalyzer.analyzeLog(MOCKED_LOG);
        }

        assertThat(logAnalyzer.getMostPopularMethods()).isEqualTo(expectedResults);
    }

    @ParameterizedTest
    @MethodSource("getArgumentsForAnalyzeResourcePartLogTest")
    void analyzeResourcePartLogTest(List<String> resources, List<Map.Entry<String, Long>> expectedResults) {
        LogAnalyzer logAnalyzer = new LogAnalyzer();
        Mockito.when(MOCKED_LOG.httpStatusCode()).thenReturn(0);
        Mockito.when(MOCKED_LOG.responseSize()).thenReturn(0L);
        Mockito.when(MOCKED_LOG.clientIpAddress()).thenReturn("");
        for (String resource : resources) {
            Mockito.when(MOCKED_LOG.request()).thenReturn(new Request("", resource, ""));
            logAnalyzer.analyzeLog(MOCKED_LOG);
        }

        assertThat(logAnalyzer.getMostPopularResources()).isEqualTo(expectedResults);
    }

    @ParameterizedTest
    @MethodSource("getArgumentsForAnalyzeStatusCodePartLogTest")
    void analyzeStatusCodePartLogTest(List<Integer> statusCodes, List<Map.Entry<String, Long>> expectedResults) {
        LogAnalyzer logAnalyzer = new LogAnalyzer();
        Mockito.when(MOCKED_LOG.responseSize()).thenReturn(0L);
        Mockito.when(MOCKED_LOG.clientIpAddress()).thenReturn("");
        Mockito.when(MOCKED_LOG.request()).thenReturn(new Request("", "", ""));
        for (Integer code : statusCodes) {
            Mockito.when(MOCKED_LOG.httpStatusCode()).thenReturn(code);
            logAnalyzer.analyzeLog(MOCKED_LOG);
        }

        assertThat(logAnalyzer.getMostPopularStatusCodes()).isEqualTo(expectedResults);
    }

    @ParameterizedTest
    @MethodSource("getArgumentsForAnalyzeAveragePartLogTest")
    void analyzeAveragePartLogTest(List<Long> responseSizes, double expectedResult) {
        LogAnalyzer logAnalyzer = new LogAnalyzer();
        Mockito.when(MOCKED_LOG.clientIpAddress()).thenReturn("");
        Mockito.when(MOCKED_LOG.httpStatusCode()).thenReturn(0);
        Mockito.when(MOCKED_LOG.request()).thenReturn(new Request("", "", ""));
        for (long size : responseSizes) {
            Mockito.when(MOCKED_LOG.responseSize()).thenReturn(size);
            logAnalyzer.analyzeLog(MOCKED_LOG);
        }

        assertThat(logAnalyzer.getAverageResponseSize()).isEqualTo(expectedResult);
    }

    @ParameterizedTest
    @MethodSource("getArgumentsForAnalyzePercentilePartLogTest")
    void analyzePercentilePartLogTest(List<Long> responseSizes, long expectedResult) {
        LogAnalyzer logAnalyzer = new LogAnalyzer();
        Mockito.when(MOCKED_LOG.clientIpAddress()).thenReturn("");
        Mockito.when(MOCKED_LOG.httpStatusCode()).thenReturn(0);
        Mockito.when(MOCKED_LOG.request()).thenReturn(new Request("", "", ""));
        for (long size : responseSizes) {
            Mockito.when(MOCKED_LOG.responseSize()).thenReturn(size);
            logAnalyzer.analyzeLog(MOCKED_LOG);
        }
        assertThat(logAnalyzer.getResponseSizePercentile()).isEqualTo(expectedResult);
    }

    private static Stream<Arguments> getArgumentsForAnalyzePercentilePartLogTest() {
        return Stream.of(
            Arguments.of(List.of(1000L, 1000L, 1000L, 2000L, 2000L,
                3000L), 3000L),
            Arguments.of(List.of(12341L, 232342L, 30941L, 88912L, 78120L,
                19938219L), 19938219L),
            Arguments.of(List.of(1000000000000L, 2000000000000L), 2000000000000L),
            Arguments.of(List.of(9000000000000000000L, 9000000000000000000L, 1000000000000000000L), 9000000000000000000L)
        );
    }

    private static Stream<Arguments> getArgumentsForAnalyzeAveragePartLogTest() {
        return Stream.of(
            Arguments.of(List.of(1000L, 1000L, 1000L, 2000L, 2000L,
                    3000L), 1666.6666666666667),
            Arguments.of(List.of(12341L, 232342L, 30941L, 88912L, 78120L,
                19938219L), 3396812.5),
            Arguments.of(List.of(1000000000000L, 2000000000000L), 1500000000000.0),
            Arguments.of(List.of(9000000000000000000L, 9000000000000000000L, 1000000000000000000L), 6.333333333333333E18)
        );
    }

    private static Stream<Arguments> getArgumentsForAnalyzeStatusCodePartLogTest() {
        return Stream.of(
            Arguments.of(List.of(100, 100, 100, 200, 200,
                300), List.of(
                Map.entry(100, 3L),
                Map.entry(200, 2L),
                Map.entry(300, 1L)
            )),
            Arguments.of(List.of(100, 100, 100, 100, 100,
                100), List.of(
                Map.entry(100, 6L)
            )),
            Arguments.of(List.of(100, 200, 100, 200, 100,
                300), List.of(
                Map.entry(100, 3L),
                Map.entry(200, 2L),
                Map.entry(300, 1L)
            ))
        );
    }

    private static Stream<Arguments> getArgumentsForAnalyzeResourcePartLogTest() {
        return Stream.of(
            Arguments.of(
                List.of("/resource1.txt", "/resource1.txt", "/resource1.txt", "/resource2.txt", "/resource2.txt",
                    "/resource3.txt"), List.of(
                    Map.entry("/resource1.txt", 3L),
                    Map.entry("/resource2.txt", 2L),
                    Map.entry("/resource3.txt", 1L)
                )),
            Arguments.of(
                List.of("/resource1.txt", "/resource1.txt", "/resource1.txt", "/resource1.txt", "/resource1.txt",
                    "/resource1.txt"), List.of(
                    Map.entry("/resource1.txt", 6L)
                )),
            Arguments.of(
                List.of("/resource1.txt", "/resource2.txt", "/resource1.txt", "/resource2.txt", "/resource1.txt",
                    "/resource3.txt"), List.of(
                    Map.entry("/resource1.txt", 3L),
                    Map.entry("/resource2.txt", 2L),
                    Map.entry("/resource3.txt", 1L)
                ))
        );
    }

    private static Stream<Arguments> getArgumentsForAnalyzeMethodPartLogTest() {
        return Stream.of(
            Arguments.of(List.of("GET", "GET", "GET", "POST", "POST",
                "DELETE"), List.of(
                Map.entry("GET", 3L),
                Map.entry("POST", 2L),
                Map.entry("DELETE", 1L)
            )),
            Arguments.of(List.of("GET", "GET", "GET", "GET", "GET",
                "GET"), List.of(
                Map.entry("GET", 6L)
            )),
            Arguments.of(List.of("GET", "POST", "GET", "POST", "GET",
                "DELETE"), List.of(
                Map.entry("GET", 3L),
                Map.entry("POST", 2L),
                Map.entry("DELETE", 1L)
            ))
        );
    }

    private static Stream<Arguments> getArgumentsForAnalyzeClientPartLogTest() {
        return Stream.of(
            Arguments.of(List.of("1.1.1.1", "1.1.1.1", "1.1.1.1", "2.2.2.2", "2.2.2.2",
                "3.3.3.3"), List.of(
                Map.entry("1.1.1.1", 3L),
                Map.entry("2.2.2.2", 2L),
                Map.entry("3.3.3.3", 1L)
            )),
            Arguments.of(List.of("1.1.1.1", "1.1.1.1", "1.1.1.1", "1.1.1.1", "1.1.1.1",
                "1.1.1.1"), List.of(
                Map.entry("1.1.1.1", 6L)
            )),
            Arguments.of(List.of("1.1.1.1", "2.2.2.2", "1.1.1.1", "2.2.2.2", "1.1.1.1",
                "3.3.3.3"), List.of(
                Map.entry("1.1.1.1", 3L),
                Map.entry("2.2.2.2", 2L),
                Map.entry("3.3.3.3", 1L)
            ))
        );
    }
}
