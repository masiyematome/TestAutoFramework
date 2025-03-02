package api.utilities;

import org.slf4j.*;

public class LogUtil {
    private static Logger LOG;

    public static void logInfo(Object targetClass, String message){
        LOG = LoggerFactory.getLogger(targetClass.getClass());
        LOG.info(message);
    }

    public static void logError(Object targetClass, String message){
        LOG = LoggerFactory.getLogger(targetClass.getClass());
        LOG.error(message);
    }

    public static void logWarning(Object targetClass, String message){
        LOG = LoggerFactory.getLogger(targetClass.getClass());
        LOG.warn(message);
    }

}
