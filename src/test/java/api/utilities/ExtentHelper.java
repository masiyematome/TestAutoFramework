package api.utilities;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.reporter.ExtentSparkReporter;
import org.slf4j.*;

public class ExtentHelper {
    private static final Logger LOG = LoggerFactory.getLogger(ExtentHelper.class);
    private static ExtentHelper instance;
    private ExtentReports reports;

    private ExtentHelper(String filePath){
        initializeReport(filePath);
    }

    public static ExtentHelper getInstance(String filePath){
        if(instance == null){
            instance = new ExtentHelper(filePath);
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
