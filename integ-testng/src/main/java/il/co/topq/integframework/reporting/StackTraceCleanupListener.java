package il.co.topq.integframework.reporting;

import il.co.topq.integframework.utils.StringUtils;

import org.testng.ITestContext;
import org.testng.ITestResult;
import org.testng.internal.IResultListener2;

public class StackTraceCleanupListener implements IResultListener2 {

	@Override
	public void onTestFailure(ITestResult result) {
		StringUtils.getStackTrace(result.getThrowable(), Reporter.packagesToFilter);
	}

	@Override
	public void onConfigurationFailure(ITestResult itr) {
		StringUtils.getStackTrace(itr.getThrowable(), Reporter.packagesToFilter);
	}

	@Override
	public void onTestStart(ITestResult result) {
	}

	@Override
	public void onTestSuccess(ITestResult result) {
	}

	@Override
	public void onTestSkipped(ITestResult result) {
	}

	@Override
	public void onTestFailedButWithinSuccessPercentage(ITestResult result) {
	}

	@Override
	public void onStart(ITestContext context) {
	}

	@Override
	public void onFinish(ITestContext context) {
	}

	@Override
	public void onConfigurationSuccess(ITestResult itr) {
	}

	@Override
	public void onConfigurationSkip(ITestResult itr) {
	}

	@Override
	public void beforeConfiguration(ITestResult tr) {
	}
}
