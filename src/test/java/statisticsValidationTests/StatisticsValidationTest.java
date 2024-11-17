package statisticsValidationTests;

import backend.academy.statistics.Statistics;
import backend.academy.writer.OutputFormat;
import backend.academy.writer.Writer;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.nio.file.Path;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;

public class StatisticsValidationTest {

    @ParameterizedTest
    @MethodSource("provideDataForTests")
    void testWriterOutput(OutputFormat format, Statistics statistics, List<String> expectedSections) {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        Writer writer = new Writer(format, new PrintStream(outputStream));

        writer.write(statistics);

        String output = outputStream.toString();

        for (String section : expectedSections) {
            assertThat(output).contains(section);
        }
    }

    private static Stream<Arguments> provideDataForTests() {
        return Stream.of(
            Arguments.of(
                OutputFormat.MARKDOWN,
                new Statistics(
                    List.of(Path.of("log1.txt"), Path.of("log2.txt")),
                    LocalDate.of(2023, 1, 1),
                    LocalDate.of(2023, 12, 31),
                    1000,
                    512.34,
                    1024,
                    List.of(Map.entry("/resource1", 150L), Map.entry("/resource2", 100L)),
                    List.of(Map.entry(200, 9789L), Map.entry(500, 422L), Map.entry(404, 405L), Map.entry(400, 399L), Map.entry(301, 392L)),
                    List.of(Map.entry("GET", 7786L), Map.entry("POST", 845L), Map.entry("PATCH", 843L), Map.entry("DELETE", 804L), Map.entry("PUT", 768L)),
                    List.of(Map.entry("119.249.216.1", 2L), Map.entry("89.222.50.22", 1L), Map.entry("70.234.112.21", 1L), Map.entry("177.105.205.3", 1L), Map.entry("177.164.136.68", 1L))
                ),
                List.of(
                    "# Статистика\n",
                    "## Файлы\n",
                    "| log1.txt | " + Path.of("log1.txt").toAbsolutePath(),
                    "## Период\n",
                    "**Начальная дата:** 2023-01-01",
                    "**Конечная дата:** 2023-12-31",
                    "## Общее количество запросов\n",
                    "1000",
                    "## Средний размер ответа\n",
                    "512",
                    "## 95-й процентиль размера ответа\n",
                    "1024 байт",
                    "## Наиболее популярные ресурсы\n",
                    "| /resource1 | 150",
                    "| /resource2 | 100",
                    "## Наиболее популярные коды ответа\n",
                    "| 200 | 9789 |",
                    "| 500 | 422 |",
                    "| 404 | 405 |",
                    "| 400 | 399 |",
                    "| 301 | 392 |",
                    "## Наиболее популярные методы\n",
                    "| GET | 7786 |",
                    "| POST | 845 |",
                    "| PATCH | 843 |",
                    "| DELETE | 804 |",
                    "| PUT | 768 |",
                    "## Наиболее активные пользователи\n",
                    "| 119.249.216.1 | 2 |",
                    "| 89.222.50.22 | 1 |",
                    "| 70.234.112.21 | 1 |",
                    "| 177.105.205.3 | 1 |",
                    "| 177.164.136.68 | 1 |"
                )
            ),
            Arguments.of(
                OutputFormat.ADOC,
                new Statistics(
                    List.of(Path.of("log3.txt")),
                    LocalDate.of(2022, 6, 1),
                    LocalDate.of(2022, 6, 30),
                    500,
                    256.78,
                    512,
                    List.of(Map.entry("/resourceA", 250L)),
                    List.of(Map.entry(200, 450L), Map.entry(500, 50L)),
                    List.of(Map.entry("GET", 300L), Map.entry("PUT", 200L)),
                    List.of(Map.entry("user3", 300L), Map.entry("user4", 200L))
                ),
                List.of(
                    "= Статистика\n",
                    "== Файлы\n",
                    "| log3.txt | " + Path.of("log3.txt").toAbsolutePath(),
                    "== Период\n",
                    "**Начальная дата:** 2022-06-01",
                    "**Конечная дата:** 2022-06-30",
                    "== Общее количество запросов\n",
                    "500",
                    "== Средний размер ответа\n",
                    "256",
                    "== 95-й процентиль размера ответа\n",
                    "512 байт",
                    "== Наиболее популярные ресурсы\n",
                    "| /resourceA | 250",
                    "== Наиболее популярные коды ответа\n",
                    "| 200 | 450",
                    "| 500 | 50",
                    "== Наиболее популярные методы\n",
                    "| GET | 300",
                    "| PUT | 200",
                    "== Наиболее активные пользователи\n",
                    "| user3 | 300",
                    "| user4 | 200"
                )
            ),
            Arguments.of(
                OutputFormat.MARKDOWN,
                new Statistics(
                    List.of(),
                    LocalDate.MIN,
                    LocalDate.MAX,
                    0,
                    0.0,
                    0,
                    List.of(),
                    List.of(),
                    List.of(),
                    List.of()
                ),
                List.of(
                    "# Статистика\n",
                    "## Файлы\n",
                    "## Период\n",
                    "**Начальная дата:** -",
                    "**Конечная дата:** -",
                    "## Общее количество запросов\n",
                    "0",
                    "## Средний размер ответа\n",
                    "0",
                    "## 95-й процентиль размера ответа\n",
                    "0 байт",
                    "## Наиболее популярные коды ответа\n",
                    "## Наиболее популярные методы\n",
                    "## Наиболее активные пользователи\n"
                )
            )
        );
    }
}
