package commons;

import com.aventstack.extentreports.*;
import com.aventstack.extentreports.markuputils.CodeLanguage;
import com.aventstack.extentreports.markuputils.MarkupHelper;
import com.aventstack.extentreports.model.Media;
import com.aventstack.extentreports.reporter.ExtentSparkReporter;
import io.restassured.response.Response;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Base64;

public class ExtentUtil {

    private static ExtentReports extentReports;
    private ExtentUtil(){}

    public static ExtentReports initializeReport(){
        if(extentReports != null){
            return extentReports;
        }

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

    public static void report(ExtentTest node, String status, String message, Response response){
        if(extentReports == null){
            LogUtil.logError(ExtentUtil.class, "ExtentReports = null. Call initializeReport first.");
            throw new UnsupportedOperationException("ExtentReports = null. Call initializeReport first.");
        }
        if(status.equalsIgnoreCase("fail")){
            node.fail(message);
        }else if(status.equalsIgnoreCase("pass")){
            node.pass(message);
        }

        if(response.getHeader("Content-Type").contains("text/xml")){
            node.info(MarkupHelper.createCodeBlock(response.asString(), CodeLanguage.XML));
        }

        if(response.getHeader("Content-Type").contains("application/json")){
            node.info(MarkupHelper.createCodeBlock(response.asString(), CodeLanguage.JSON));
        }
    }

    private static Media getScreenshot(WebDriver driver){
        byte[] screenshot = ((TakesScreenshot) driver).getScreenshotAs(OutputType.BYTES);
        String screenShotAsBase64 = Base64.getEncoder().encodeToString(screenshot);
        return MediaEntityBuilder
                .createScreenCaptureFromBase64String(screenShotAsBase64).build();
    }

}
