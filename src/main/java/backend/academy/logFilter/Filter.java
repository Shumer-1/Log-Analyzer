package backend.academy.logFilter;

import backend.academy.log.Log;
import java.util.ArrayList;
import java.util.List;

public class Filter {

    private List<LogFilter> filters = new ArrayList<>();

    public void addFilter(LogFilter logFilter) {
        filters.add(logFilter);
    }

    public boolean filter(Log log) {
        boolean filterFlag = true;
        for (LogFilter logFilter : filters) {
            filterFlag &= logFilter.filter(log);
        }
        return filterFlag;
    }

}
