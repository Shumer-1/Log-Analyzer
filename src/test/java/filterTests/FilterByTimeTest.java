package filterTests;

import backend.academy.log.Log;
import backend.academy.logFilter.FilterByTime;
import java.time.LocalDate;
import java.util.stream.Stream;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(MockitoExtension.class)
public class FilterByTimeTest {

    private final Log log = Mockito.mock(Log.class);

    @ParameterizedTest
    @MethodSource("getArgumentsForFilterTest")
    void filterTest(LocalDate from, LocalDate to, LocalDate date, boolean expectedResult){
        Mockito.when(log.timestamp()).thenReturn(date);
        FilterByTime filter = new FilterByTime(from, to);

        boolean actualResult = filter.filter(log);

        assertThat(actualResult).isEqualTo(expectedResult);
    }

    private static Stream<Arguments> getArgumentsForFilterTest(){
        return Stream.of(
            Arguments.of(LocalDate.parse("2024-12-11"), LocalDate.parse("2024-12-14"),
                LocalDate.parse("2024-12-15"), false),
            Arguments.of(LocalDate.parse("2023-02-10"), LocalDate.parse("2024-12-14"),
                LocalDate.parse("2024-12-14"), true),
            Arguments.of(LocalDate.parse("2023-02-10"), LocalDate.parse("2024-12-14"),
                LocalDate.parse("2023-02-10"), true),
            Arguments.of(LocalDate.parse("2023-02-10"), LocalDate.parse("2024-12-14"),
                LocalDate.parse("9999-12-14"), false)
        );
    }

}
