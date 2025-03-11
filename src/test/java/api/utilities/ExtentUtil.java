package api.utilities;

import com.aventstack.extentreports.*;
import com.aventstack.extentreports.markuputils.CodeLanguage;
import com.aventstack.extentreports.markuputils.MarkupHelper;
import com.aventstack.extentreports.reporter.ExtentSparkReporter;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class ExtentUtil {

    private static ExtentUtil instance;
    private static ExtentReports extentReports;
    private ExtentUtil(){}

    public static ExtentReports initializeReport(){
        String time = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMddHHmmssSSS"));
        try {
            extentReports = new ExtentReports();
            ExtentSparkReporter spark = new ExtentSparkReporter("target/reports/test_report_" + time + ".html");
            spark.config().setReportName("Test Automation Results");
            extentReports.attachReporter(spark);
        }catch (Exception e){
            LogUtil.logError(ExtentUtil.class, "Couldn't initialize report " + e);
            throw e;
        }
        return extentReports;
    }

    public static void report(ExtentTest node, String status, String message, String extraInfo){
        if(extentReports == null){
            LogUtil.logError(ExtentUtil.class, "ExtentReports = null. Call initializeReport first.");
            throw new UnsupportedOperationException("ExtentReports = null. Call initializeReport first.");
        }

        if(status.equalsIgnoreCase("fail")){
            node.fail(message);
        }else if(status.equalsIgnoreCase("pass")){
            node.pass(message);
        }

        node.info(MarkupHelper.createCodeBlock(extraInfo, CodeLanguage.JSON));
    }

}
