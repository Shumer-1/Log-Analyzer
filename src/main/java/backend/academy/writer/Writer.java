package backend.academy.writer;

import backend.academy.statistics.Statistics;
import java.io.PrintStream;
import java.nio.file.Path;
import java.time.LocalDate;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public class Writer {

    private static final int FIRST_HEADER_LEVEL = 1;
    private static final int SECOND_HEADER_LEVEL = 2;
    private static final String LINE_BREAK = "\n";
    private static final int COUNT_OF_STRING = 5;
    private static final String SEPARATOR = " | ";
    private static final String ROW_START = "| ";

    private final OutputFormat format;
    private final PrintStream printStream;

    public void write(Statistics statistics) {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(makeHeaderLine());
        stringBuilder.append(makeFilesSection(statistics));
        stringBuilder.append(makePeriodSection(statistics));
        stringBuilder.append(makeTotalRequestsSection(statistics));
        stringBuilder.append(makeAverageResponseSizeSection(statistics));
        stringBuilder.append(makePercentileResponseSizeSection(statistics));
        stringBuilder.append(makeMostPopularResourcesSection(statistics));
        stringBuilder.append(makeMostPopularResponseCodesSection(statistics));
        stringBuilder.append(makeMostPopularMethodsSection(statistics));
        stringBuilder.append(makeMostActiveUsersSection(statistics));
        printStream.println(stringBuilder);
    }

    private String makeHeaderLine() {
        return format.header().repeat(FIRST_HEADER_LEVEL) + " Статистика\n\n";
    }

    private String makeFilesSection(Statistics stats) {
        StringBuilder lines = new StringBuilder();
        lines.append(format.header().repeat(SECOND_HEADER_LEVEL)).append(" Файлы\n");
        if (format == OutputFormat.ADOC) {
            lines.append(format.startTable());
            lines.append("| Файл | Путь").append(LINE_BREAK);
        } else {
            lines.append("| Файл | Путь |").append(LINE_BREAK);
            lines.append(format.startTable());
        }
        for (Path filename : stats.filenames()) {
            lines.append(ROW_START).append(filename.getFileName()).append(SEPARATOR).append(filename.toAbsolutePath())
                .append(format.endColumn()).append(LINE_BREAK);
        }
        lines.append(format.endTable());
        lines.append(LINE_BREAK);

        return lines.toString();
    }

    private String makePeriodSection(Statistics stats) {
        StringBuilder lines = new StringBuilder();
        String startDateValue = stats.startDate().isEqual(LocalDate.MIN) ? "-" : stats.startDate().toString();
        String endDateValue = stats.endDate().isEqual(LocalDate.MAX) ? "-" : stats.endDate().toString();

        lines.append(format.header().repeat(SECOND_HEADER_LEVEL)).append(" Период").append(LINE_BREAK);
        lines.append("- **Начальная дата:** ").append(startDateValue).append(LINE_BREAK);
        lines.append("- **Конечная дата:** ").append(endDateValue).append(LINE_BREAK);
        lines.append(LINE_BREAK);
        return lines.toString();
    }

    private String makeTotalRequestsSection(Statistics stats) {
        StringBuilder lines = new StringBuilder();
        lines.append(format.header().repeat(SECOND_HEADER_LEVEL)).append(" Общее количество запросов")
            .append(LINE_BREAK);
        lines.append(stats.totalRequests()).append(LINE_BREAK);
        lines.append(LINE_BREAK);
        return lines.toString();
    }

    private String makeAverageResponseSizeSection(Statistics stats) {
        StringBuilder lines = new StringBuilder();
        lines.append(format.header().repeat(SECOND_HEADER_LEVEL)).append(" Средний размер ответа").append(LINE_BREAK);
        lines.append(String.format("%.2f байт", stats.averageResponseSize())).append(LINE_BREAK);
        lines.append(LINE_BREAK);
        return lines.toString();
    }

    private String makePercentileResponseSizeSection(Statistics stats) {
        StringBuilder lines = new StringBuilder();
        lines.append(format.header().repeat(SECOND_HEADER_LEVEL)).append(" 95-й процентиль размера ответа")
            .append(LINE_BREAK);
        lines.append(String.format("%d байт", stats.percentile95ResponseSize())).append(LINE_BREAK);
        lines.append(LINE_BREAK);
        return lines.toString();
    }

    private String makeMostPopularResourcesSection(Statistics stats) {
        StringBuilder lines = new StringBuilder();
        lines.append(format.header().repeat(SECOND_HEADER_LEVEL)).append(" Наиболее популярные ресурсы")
            .append(LINE_BREAK);
        if (format == OutputFormat.ADOC) {
            lines.append(format.startTable());
            lines.append("| Ресурс | Количество запросов ").append(LINE_BREAK);
        } else {
            lines.append("| Ресурс | Количество запросов |").append(LINE_BREAK);
            lines.append(format.startTable());
        }
        for (int i = 0; i < Math.min(COUNT_OF_STRING, stats.mostPopularResources().size()); i++) {
            lines.append(ROW_START).append(stats.mostPopularResources().get(i).getKey()).append(SEPARATOR)
                .append(stats.mostPopularResources().get(i).getValue())
                .append(format.endColumn()).append(LINE_BREAK);
        }
        lines.append(format.endTable());
        lines.append(LINE_BREAK);
        return lines.toString();
    }

    private StringBuilder makeMostPopularResponseCodesSection(Statistics stats) {
        StringBuilder lines = new StringBuilder();
        lines.append(format.header().repeat(SECOND_HEADER_LEVEL)).append(" Наиболее популярные коды ответа")
            .append(LINE_BREAK);
        if (format == OutputFormat.ADOC) {
            lines.append(format.startTable());
            lines.append("| Код ответа | Количество ").append(LINE_BREAK);
        } else {
            lines.append("| Код ответа | Количество |").append(LINE_BREAK);
            lines.append(format.startTable());
        }
        for (int i = 0; i < Math.min(COUNT_OF_STRING, stats.mostPopularResponseCodes().size()); i++) {
            lines.append(ROW_START).append(stats.mostPopularResponseCodes().get(i).getKey()).append(SEPARATOR)
                .append(stats.mostPopularResponseCodes().get(i).getValue())
                .append(format.endColumn()).append(LINE_BREAK);
        }
        lines.append(format.endTable());
        lines.append(LINE_BREAK);
        return lines;
    }

    private String makeMostPopularMethodsSection(Statistics stats) {
        StringBuilder lines = new StringBuilder();
        lines.append(format.header().repeat(SECOND_HEADER_LEVEL)).append(" Наиболее популярные методы")
            .append(LINE_BREAK);
        if (format == OutputFormat.ADOC) {
            lines.append(format.startTable());
            lines.append("| Метод | Количество ").append(LINE_BREAK);
        } else {
            lines.append("| Метод | Количество |").append(LINE_BREAK);
            lines.append(format.startTable());
        }
        for (int i = 0; i < Math.min(COUNT_OF_STRING, stats.mostPopularMethods().size()); i++) {
            lines.append(ROW_START).append(stats.mostPopularMethods().get(i).getKey()).append(SEPARATOR)
                .append(stats.mostPopularMethods().get(i).getValue())
                .append(format.endColumn()).append(LINE_BREAK);
        }
        lines.append(format.endTable());
        lines.append(LINE_BREAK);
        return lines.toString();
    }

    private String makeMostActiveUsersSection(Statistics stats) {
        StringBuilder lines = new StringBuilder();
        lines.append(format.header().repeat(SECOND_HEADER_LEVEL)).append(" Наиболее активные пользователи")
            .append(LINE_BREAK);
        if (format == OutputFormat.ADOC) {
            lines.append(format.startTable());
            lines.append("| Метод | Количество запросов ").append(LINE_BREAK);
        } else {
            lines.append("| Метод | Количество запросов |").append(LINE_BREAK);
            lines.append(format.startTable());
        }
        for (int i = 0; i < Math.min(COUNT_OF_STRING, stats.mostActiveUsers().size()); i++) {
            lines.append(ROW_START).append(stats.mostActiveUsers().get(i).getKey()).append(SEPARATOR)
                .append(stats.mostActiveUsers().get(i).getValue())
                .append(format.endColumn()).append(LINE_BREAK);
        }
        lines.append(format.endTable());
        lines.append(LINE_BREAK);
        return lines.toString();
    }
}
