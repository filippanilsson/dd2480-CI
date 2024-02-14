package ciServer;
    
import ch.qos.logback.core.AppenderBase;
import ch.qos.logback.classic.spi.ILoggingEvent;
import java.util.ArrayList;
import java.util.List;

public class InMemoryAppender extends AppenderBase<ILoggingEvent> {
    private static final List<String> logMessages = new ArrayList<>();

    @Override
    protected void append(ILoggingEvent eventObject) {
        logMessages.add(eventObject.getFormattedMessage());
    }

    public static List<String> getLogMessages() {
        return logMessages;
    }

    public static void clearLogMessages() {
        logMessages.clear();
    }
}