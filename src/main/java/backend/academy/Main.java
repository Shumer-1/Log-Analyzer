package backend.academy;

import backend.academy.session.SessionPreparer;
import java.io.PrintStream;
import lombok.experimental.UtilityClass;

@UtilityClass
public class Main {
    public static void main(String[] args) {
        // https://pastebin.com/raw/wnHK5UDf
        // /home/vladislav/IdeaProjects/backend_academy/project3/backend_academy_2024_project_3-java-Shumer-1/**/logs.txt
        PrintStream consolePrintStream = new PrintStream(System.out);
        SessionPreparer sessionPreparer = new SessionPreparer(consolePrintStream);
        sessionPreparer.sessionPrepare(args);
    }
}
