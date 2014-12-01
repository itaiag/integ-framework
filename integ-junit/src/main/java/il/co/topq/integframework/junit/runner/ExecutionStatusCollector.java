package il.co.topq.integframework.junit.runner;

import org.junit.runner.Description;
import org.junit.runner.Result;
import org.junit.runner.notification.Failure;
import org.junit.runner.notification.RunListener;

public class ExecutionStatusCollector extends RunListener {

	private int numberOfTests;
	private int numberOfFailures;
	private int numberOfIgnored;
	private long executionTime;

	@Override
	public void testStarted(Description description) throws Exception {
		numberOfTests++;
	}

	@Override
	public void testFailure(Failure failure) throws Exception {
		numberOfFailures++;
	}

	/**
	 * Called when a test will not be run, generally because a test method is annotated
	 * with {@link org.junit.Ignore}.
	 * 
	 * @param description
	 *            describes the test that will not be run
	 */
	@Override
	public void testIgnored(Description description) throws Exception {
		numberOfIgnored++;
	}

	@Override
	public void testRunFinished(Result result) throws Exception {
		executionTime += result.getRunTime();
	}

}
