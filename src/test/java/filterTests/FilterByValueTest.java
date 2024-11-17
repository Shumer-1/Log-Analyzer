package filterTests;

import backend.academy.log.Field;
import backend.academy.log.Log;
import backend.academy.log.Request;
import backend.academy.logFilter.FilterByValue;
import java.util.stream.Stream;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(MockitoExtension.class)
public class FilterByValueTest {

    private static final Log log = Mockito.mock(Log.class);

    @ParameterizedTest
    @MethodSource("getArgumentsForFilterByAgentFieldTest")
    void filterByAgentFieldTest(String value, String logValue, boolean expectedResult) {
        Mockito.when(log.userAgent()).thenReturn(logValue);

        FilterByValue filter = new FilterByValue(Field.AGENT, value);
        boolean actualResult = filter.filter(log);

        assertThat(actualResult).isEqualTo(expectedResult);
    }

    @ParameterizedTest
    @MethodSource("getArgumentsForFilterByMethodFieldTest")
    void filterByMethodFieldTest(String value, String logValue, boolean expectedResult) {

        Mockito.when(log.request()).thenReturn(new Request(logValue, null, null));

        FilterByValue filter = new FilterByValue(Field.METHOD, value);
        boolean actualResult = filter.filter(log);

        assertThat(actualResult).isEqualTo(expectedResult);
    }

    @ParameterizedTest
    @MethodSource("getArgumentsForFilterByClientIpAddressFieldTest")
    void filterByClientIpAddressFieldTest(String value, String logValue, boolean expectedResult) {

        Mockito.when(log.clientIpAddress()).thenReturn(logValue);

        FilterByValue filter = new FilterByValue(Field.CLIENT_IP_ADDRESS, value);
        boolean actualResult = filter.filter(log);

        assertThat(actualResult).isEqualTo(expectedResult);
    }

    @ParameterizedTest
    @MethodSource("getArgumentsForFilterByUserIdFieldTest")
    void filterByUserIdFieldTest(String value, String logValue, boolean expectedResult) {
        Mockito.when(log.userId()).thenReturn(logValue);

        FilterByValue filter = new FilterByValue(Field.USER_ID, value);
        boolean actualResult = filter.filter(log);

        assertThat(actualResult).isEqualTo(expectedResult);
    }

    private static Stream<Arguments> getArgumentsForFilterByUserIdFieldTest() {
        return Stream.of(
            Arguments.of("123", "1230", false),
            Arguments.of("-", "-", true),
            Arguments.of("123", "123", true)
        );
    }

    private static Stream<Arguments> getArgumentsForFilterByClientIpAddressFieldTest() {
        return Stream.of(
            Arguments.of("0.11.144.120", "0.11.144.121", false),
            Arguments.of("0.11.123.110", "0.11.123.110", true),
            Arguments.of("10.12.123.110", "0.11.123.110", false),
            Arguments.of("1.1.1.1", "1.1.1.1", true)
        );
    }

    private static Stream<Arguments> getArgumentsForFilterByMethodFieldTest() {
        return Stream.of(
            Arguments.of("GET", "GET", true),
            Arguments.of("POST", "GET", false),
            Arguments.of("POST", "POST", true),
            Arguments.of("GET", "Hello_method", false)
        );
    }

    private static Stream<Arguments> getArgumentsForFilterByAgentFieldTest() {
        return Stream.of(
            Arguments.of("Mozilla",
                "Mozilla/5.0 (Windows NT 6.1) AppleWebKit/5322 (KHTML, like Gecko) Chrome/39.0.883.0 Mobile Safari/5322",
                true),
            Arguments.of("Opera",
                "Opera/10.61 (Macintosh; U; Intel Mac OS X 10_9_6; en-US) Presto/2.13.313 Version/10.00", true),
            Arguments.of("Firefox",
                "Mozilla/5.0 (X11; Linux x86_64) AppleWebKit/5321 (KHTML, like Gecko) Chrome/36.0.863.0 Mobile Safari/5321",
                false),
            Arguments.of("Ubuntu", "Mozilla/5.0 (Macintosh; PPC Mac OS X 10_8_9 rv:3.0) Gecko/2009-28-06 Firefox/35.0",
                false)
        );
    }
}
