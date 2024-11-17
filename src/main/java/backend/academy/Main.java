package backend.academy;

import backend.academy.session.SessionPreparer;
import java.io.PrintStream;
import lombok.experimental.UtilityClass;

@UtilityClass
public class Main {
    public static void main(String[] args) {
        PrintStream consolePrintStream = new PrintStream(System.out);
        SessionPreparer sessionPreparer = new SessionPreparer(consolePrintStream);
        sessionPreparer.sessionPrepare(args);
    }
}
