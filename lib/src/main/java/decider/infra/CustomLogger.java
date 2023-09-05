package decider.infra;

import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.LogManager;
import java.util.logging.Logger;

public class CustomLogger {
    private static CustomLogger instance = null;
    public Logger logger;
    private static final Level logLevel = Level.OFF;

    private CustomLogger() {

        Logger rootLogger = LogManager.getLogManager().getLogger("");
        rootLogger.setLevel(logLevel);
        for (Handler h : rootLogger.getHandlers()) {
            h.setLevel(logLevel);
        }
        logger = rootLogger;
    }

    public static synchronized CustomLogger getInstance() {
        if (instance == null)
            instance = new CustomLogger();

        return instance;
    }
}