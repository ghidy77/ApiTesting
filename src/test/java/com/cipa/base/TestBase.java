package com.cipa.base;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.reporter.ExtentSparkReporter;
import com.cipa.utils.DbConn;
import com.cipa.utils.PropertyReader;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.testng.ITestResult;
import org.testng.annotations.*;

import java.lang.reflect.Method;
import java.sql.SQLException;

@Listeners({TestListener.class})
public class TestBase {

	private static final Logger LOG = LogManager.getLogger(TestBase.class);
	protected static ExtentReports reports;
	protected static ExtentTest test;

	@BeforeSuite(alwaysRun = true)
	protected void startReporter(){
		LOG.info("Execute Before Suite");
		ExtentSparkReporter html = new ExtentSparkReporter(PropertyReader.getProperty("report.folder")+"extentreport.html");
		html.config().setDocumentTitle("API Automation Report");
		reports = new ExtentReports();
		reports.attachReporter(html);
	}

	@BeforeMethod(alwaysRun = true)
	protected void setUp(Method method) {
		LOG.info("Execute Before Method");
		String testName = method.getName();
		test = reports.createTest(this.getClass().getSimpleName()+":"+testName);
		test.info("Starting Test");
	}

	@AfterMethod(alwaysRun = true)
	protected void tearDown(ITestResult result) {
		LOG.info("Execute Teardown");
		switch (result.getStatus()){
			case ITestResult.FAILURE:
				test.fail("Test FAILED! Reason:" + result.getThrowable());
				break;
			case ITestResult.SUCCESS:
				test.pass("Test PASSED!");
				break;
			case ITestResult.SKIP:
				test.skip("Test SKIPPED!");
				break;
			default:
				test.fail("Test STARTED but not finished.");
				break;
		}
	}

	@AfterSuite(alwaysRun = true)
	protected void endReport() throws SQLException {
		// Close DB
		LOG.info("Closing DB");
		DbConn.close();
		// Generate Extent Report
		reports.flush();
	}

}
