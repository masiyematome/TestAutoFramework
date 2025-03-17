package commons;

import org.slf4j.*;

public class LogUtil {
    private static Logger LOG;

    public static void logInfo(Class<?> targetClass, String message){
        LOG = LoggerFactory.getLogger(targetClass);
        LOG.info(message);
    }

    public static void logError(Class<?> targetClass, String message){
        LOG = LoggerFactory.getLogger(targetClass);
        LOG.error(message);
    }

    public static void logWarning(Class<?> targetClass, String message){
        LOG = LoggerFactory.getLogger(targetClass);
        LOG.warn(message);
    }

}
