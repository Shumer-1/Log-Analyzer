package backend.academy.session;

import backend.academy.config.Config;
import backend.academy.exceptions.LogParseException;
import backend.academy.log.Log;
import backend.academy.logFilter.Filter;
import backend.academy.logReader.LogReader;
import backend.academy.statistics.LogAnalyzer;
import backend.academy.statistics.Statistics;
import backend.academy.writer.Writer;
import java.io.IOException;
import java.io.PrintStream;
import java.nio.file.Path;
import java.util.List;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public class Session {

    private Filter filter;
    private LogAnalyzer analyzer;
    private LogReader logReader;
    private Writer writer;
    private Config config;
    private PrintStream printStream;

    public void startSession() {
        try{
            String logString = logReader.readLog();
            while (logString != null) {
                Log log = Log.toLog(logString);
                if (filter.filter(log)) {
                    analyzer.analyzeLog(log);
                    System.out.println(logString);
                }
                logString = logReader.readLog();
            }
        } catch (IOException e) {
            printStream.println("Error occurred while reading the log file.");
            return;
        } catch (LogParseException e) {
            printStream.println(e.getMessage());
            return;
        }
        List<Path> pathList = logReader.pathsOut().stream().toList();

        Statistics statistics = new Statistics(pathList, config.from(), config.to(), analyzer.count(),
            analyzer.getAverageResponseSize(), analyzer.getResponseSizePercentile(),
            analyzer.getMostPopularResources(), analyzer.getMostPopularStatusCodes(),
            analyzer.getMostPopularMethods(), analyzer.getMostActiveUsers());
        writer.write(statistics);
    }
}
