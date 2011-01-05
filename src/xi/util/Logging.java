package xi.util;

import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.LogRecord;
import java.util.logging.Logger;

public class Logging {

    private static final Logger root = Logger.getLogger("xi");
    static {
        root.setUseParentHandlers(false);
        root.addHandler(new SysErrHandler());
        setLevel(Level.OFF);
    }

    public static Logger getLogger(final Class<?> cls) {
        return Logger.getLogger(cls.getPackage().getName());
    }

    public static void setLevel(final Level lvl) {
        root.setLevel(lvl);
    }

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
