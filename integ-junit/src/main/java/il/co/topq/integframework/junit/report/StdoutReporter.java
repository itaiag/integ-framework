package il.co.topq.integframework.junit.report;

import org.junit.runner.Description;
import org.junit.runner.notification.Failure;
import org.junit.runner.notification.RunListener;


public class StdoutReporter extends RunListener implements ReporterI {

	private static final String SEPARATOR = "-----------------------------------------------";

	@Override
	public void report(String title, String message, Status status) {
		if (message != null) {
			System.out.println(SEPARATOR);
		}
		switch (status) {
			case SUCCESS:
				System.out.println(title);
				break;
			case FAILURE:
				System.err.println(title);
				break;

			default:
				break;
		}
		if (message != null) {
			System.out.println(SEPARATOR);
			System.out.println(message);
			System.out.println(SEPARATOR);
		}

	}

	/**
	 * Called before any tests have been run.
	 * 
	 * @param description
	 *            describes the tests to be run
	 */
	@Override
	public void testRunStarted(Description description) throws Exception {
		System.out.println("****************************");
	}

	/**
	 * Called when all tests have finished
	 * 
	 * @param result
	 *            the summary of the test run, including all the tests that failed
	 */
//	@Override
//	public void testRunFinished(Result result) throws Exception {
//		System.out.println(String.format("Test run has finished: %d tests | %d failures | time %d seconds", result.getRunCount(),
//				result.getFailureCount(), result.getRunTime() / 1000));
//	}

	/**
	 * Called when an atomic test is about to be started.
	 * 
	 * @param description
	 *            the description of the test that is about to be run
	 *            (generally a class and method name)
	 */
	@Override
	public void testStarted(Description description) throws Exception {
		System.out.println(String.format("Test %s has started", description.getDisplayName()));
	}

	/**
	 * Called when an atomic test has finished, whether the test succeeds or fails.
	 * 
	 * @param description
	 *            the description of the test that just ran
	 */

	@Override
	public void testFinished(Description description) throws Exception {
		System.out.println(String.format("Test %s has finished", description.getDisplayName()));
	}

	/**
	 * Called when an atomic test fails.
	 * 
	 * @param failure
	 *            describes the test that failed and the exception that was thrown
	 */
	@Override
	public void testFailure(Failure failure) throws Exception {
		System.out.println(String.format("Test %s has failed due to %s", failure.getDescription().getDisplayName(),
				failure.getMessage()));
	}

	/**
	 * Called when an atomic test flags that it assumes a condition that is
	 * false
	 * 
	 * @param failure
	 *            describes the test that failed and the {@link AssumptionViolatedException} that was thrown
	 */
	@Override
	public void testAssumptionFailure(Failure failure) {
		System.out.println(String.format("Test %s has failed due to %s", failure.getDescription().getDisplayName(),
				failure.getMessage()));

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
		System.out.println(String.format("Test %s was ignored", description.getDisplayName()));

	}

}
