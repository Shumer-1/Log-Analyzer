package backend.academy.writer;

import lombok.Getter;

@Getter public enum OutputFormat {

    MARKDOWN("#", "|------|------|\n", "", " |"),
    ADOC("=", ADOC_TABLE_SYMBOL(), ADOC_TABLE_SYMBOL(), "");

    @Getter private static final String ADOC_TABLE_SYMBOL = "|===\n";
    private final String header;
    private final String startTable;
    private final String endTable;
    private final String endColumn;

    OutputFormat(String header, String startTable, String endTable, String endColumn) {
        this.header = header;
        this.startTable = startTable;
        this.endTable = endTable;
        this.endColumn = endColumn;
    }
}
