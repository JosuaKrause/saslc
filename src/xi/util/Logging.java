package xi.util;

import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.LogRecord;
import java.util.logging.Logger;

/**
 * Handles all logging and provides a standard logger for standard behavior.
 * 
 * @author Leo Woerteler
 * 
 */
public class Logging {

    /**
     * The root Logger, which defines the standard behavior for all specialized
     * Loggers.
     */
    private static final Logger root = Logger.getLogger("xi");
    static {
        root.setUseParentHandlers(false);
        root.addHandler(new SysErrHandler());
        setLevel(Level.OFF);
    }

    /**
     * Gets the Logger for a specific class.
     * 
     * @param cls
     *            The class from which the Logger will be used.
     * @return The Logger.
     */
    public static Logger getLogger(final Class<?> cls) {
        return Logger.getLogger(cls.getName());
    }

    /**
     * Sets the logging Level.
     * 
     * @param lvl
     *            The level.
     */
    public static void setLevel(final Level lvl) {
        root.setLevel(lvl);
    }

    /**
     * The Logger handler for all xi Loggers. It writes everything to the
     * standard out.
     * 
     * @author Leo Woerteler
     * 
     */
    private static class SysErrHandler extends Handler {

        @Override
        public void close() throws SecurityException {
            System.err.close();
        }

        @Override
        public void flush() {
            System.err.flush();
        }

        @Override
        public void publish(final LogRecord record) {
            System.err.println(record.getSourceClassName() + ": "
                    + record.getMessage());
        }

    }

}
