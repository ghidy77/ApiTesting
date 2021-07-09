package com.cipa.base;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.testng.ITestContext;
import org.testng.ITestListener;
import org.testng.ITestResult;

public class TestListener implements ITestListener {
    private static final Logger LOG = LogManager.getLogger(TestListener.class);

    @Override
    public void onTestStart(ITestResult result) {
        LOG.info("Starting Test: " + result.getName() + "\n");
    }

    @Override
    public void onTestSuccess(ITestResult result) {
        LOG.info("Test " + result.getName() + " PASSED! \n");
    }

    @Override
    public void onTestFailure(ITestResult result) {
        LOG.info("Test " + result.getName() + " FAILED! \n");
    }

    @Override
    public void onTestSkipped(ITestResult result) {
        LOG.info("Test " + result.getName() + " SKIPPED! \n");
    }

    @Override
    public void onTestFailedButWithinSuccessPercentage(ITestResult result) {
        // n/a
    }

    @Override
    public void onStart(ITestContext context) {
        // n/a
    }

    @Override
    public void onFinish(ITestContext context) {
        // n/a
    }
}
