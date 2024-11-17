package backend.academy.session;

import backend.academy.config.Config;
import backend.academy.exceptions.ArgumentsParseException;
import backend.academy.exceptions.SetUpReaderException;
import backend.academy.input.InputReader;
import backend.academy.logFilter.Filter;
import backend.academy.logFilter.FilterByTime;
import backend.academy.logFilter.FilterByValue;
import backend.academy.logFilter.LogFilter;
import backend.academy.logReader.LogReader;
import backend.academy.logReader.LogReaderManager;
import backend.academy.statistics.LogAnalyzer;
import backend.academy.writer.OutputFormat;
import backend.academy.writer.Writer;
import java.io.IOException;
import java.io.PrintStream;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public class SessionPreparer {

    private PrintStream errorPrintStream;

    public void sessionPrepare(String[] args) {
        Config config;
        try {
            config = InputReader.parse(args);
        } catch (ArgumentsParseException e) {
            errorPrintStream.println(e.getMessage());
            return;
        }

        Filter filter = new Filter();
        LogFilter timeFilter = new FilterByTime(config.from(), config.to());
        LogFilter valueFilter = new FilterByValue(config.field(), config.value());
        filter.addFilter(timeFilter);
        filter.addFilter(valueFilter);

        LogAnalyzer analyzer = new LogAnalyzer();
        LogReader logReader;
        try {
            logReader = LogReaderManager.getLogReader(config.path());
        } catch (IOException | InterruptedException e) {
            errorPrintStream.println("Problem getting log file.");
            return;
        } catch (SetUpReaderException e){
            errorPrintStream.println(e.getMessage());
            return;
        }

        PrintStream printStream;
        Writer writer;
        try {
            if (config.format() == OutputFormat.ADOC) {
                printStream = new PrintStream("./output.adoc");
                writer = new Writer(OutputFormat.ADOC, printStream);
            } else {
                printStream = new PrintStream("./output.md");
                writer = new Writer(OutputFormat.MARKDOWN, printStream);
            }
        } catch (IOException e){
            System.out.println("Problem getting statistics file.");
            return;
        }

        Session session = new Session(filter, analyzer, logReader, writer, config, errorPrintStream);
        session.startSession();
    }
}
