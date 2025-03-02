package api.utilities;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.reporter.ExtentSparkReporter;
import org.slf4j.*;

public class ExtentUtil {
    private static final Logger LOG = LoggerFactory.getLogger(ExtentUtil.class);
    private static ExtentUtil instance;
    private ExtentReports reports;

    private ExtentUtil(String filePath){
        initializeReport(filePath);
    }

    public static ExtentUtil getInstance(String filePath){
        if(instance == null){
            instance = new ExtentUtil(filePath);
        }
        return instance;
    }

    public ExtentReports getReports(){
        if(reports == null){
            throw new NullPointerException("Reports cannot be null");
        }
        return reports;
    }

    private void initializeReport(String filePath){
        try {
            reports = new ExtentReports();
            ExtentSparkReporter spark = new ExtentSparkReporter(filePath);
            spark.config().setReportName("Test Automation Results");
            reports.attachReporter(spark);
        }catch (Exception e){
            LOG.error("Couldn't initialize report", e);
            throw e;
        }
    }

}
