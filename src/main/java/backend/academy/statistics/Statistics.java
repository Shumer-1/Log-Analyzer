package backend.academy.statistics;

import java.nio.file.Path;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;

@SuppressWarnings("RecordComponentNumber")
public record Statistics(List<Path> filenames,
                         LocalDate startDate,
                         LocalDate endDate,
                         int totalRequests,
                         double averageResponseSize,
                         long percentile95ResponseSize,
                         List<Map.Entry<String, Long>> mostPopularResources,
                         List<Map.Entry<Integer, Long>> mostPopularResponseCodes,
                         List<Map.Entry<String, Long>> mostPopularMethods,
                         List<Map.Entry<String, Long>> mostActiveUsers) {
}
