package backend.academy.statistics;

import backend.academy.log.Log;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import lombok.Getter;

public class LogAnalyzer {
    private final double percentile = 0.95;

    @Getter private int count = 0;

    private Map<String, Long> clientIpMap = new HashMap<>();
    private final Map<String, Long> methodMap = new HashMap<>();
    private final Map<String, Long> resourceMap = new HashMap<>();
    private final Map<Integer, Long> codeMap = new HashMap<>();
    private final List<Long> responseSizes = new ArrayList<>(1000);

    public void analyzeLog(Log log) {
        count++;
        clientIpMap.merge(log.clientIpAddress(), 1L, Long::sum);
        methodMap.merge(log.request().method(), 1L, Long::sum);
        resourceMap.merge(log.request().resource(), 1L, Long::sum);
        codeMap.merge(log.httpStatusCode(), 1L, Long::sum);
        responseSizes.add(log.responseSize());
    }

    // Определяет наиболее часто используемые HTTP методы (доп.1)
    public List<Map.Entry<String, Long>> getMostPopularMethods() {
        return methodMap.entrySet()
            .stream()
            .sorted((entry1, entry2) -> Long.compare(entry2.getValue(), entry1.getValue()))
            .toList();
    }

    // Определяет наиболее активных клиентов (проверка на DoS атаки - доп.2)
    public List<Map.Entry<String, Long>> getMostActiveUsers() {
        return clientIpMap.entrySet()
            .stream()
            .sorted((entry1, entry2) -> Long.compare(entry2.getValue(), entry1.getValue()))
            .toList();
    }

    public List<Map.Entry<String, Long>> getMostPopularResources() {
        return resourceMap.entrySet()
            .stream()
            .sorted((entry1, entry2) -> Long.compare(entry2.getValue(), entry1.getValue()))
            .collect(Collectors.toList());
    }

    public List<Map.Entry<Integer, Long>> getMostPopularStatusCodes() {
        return codeMap.entrySet()
            .stream()
            .sorted((entry1, entry2) -> Long.compare(entry2.getValue(), entry1.getValue()))
            .collect(Collectors.toList());
    }

    public double getAverageResponseSize() {
        BigInteger sum = responseSizes.stream()
            .map(BigInteger::valueOf)
            .reduce(BigInteger.ZERO, BigInteger::add);

        return sum.doubleValue() / count;
    }

    public Long getResponseSizePercentile() {
        List<Long> sortedSizes = responseSizes.stream()
            .sorted()
            .toList();
        int index = (int) Math.ceil(percentile * sortedSizes.size()) - 1;
        return sortedSizes.get(index);
    }
}
