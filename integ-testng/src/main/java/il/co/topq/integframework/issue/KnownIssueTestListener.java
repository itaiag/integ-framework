package il.co.topq.integframework.issue;

import static com.google.common.collect.Lists.newArrayList;
import static il.co.topq.integframework.assertion.CompareMethod.BEFORE;
import static il.co.topq.integframework.assertion.CompareMethod.is;
import static il.co.topq.integframework.issue.IssueType.PASS;
import static org.testng.ITestResult.FAILURE;
import static org.testng.ITestResult.SUCCESS;
import static org.testng.ITestResult.SUCCESS_PERCENTAGE_FAILURE;
import il.co.topq.integframework.issue.KnownIssue.KnownIssues;
import il.co.topq.integframework.reporting.Reporter;

import java.util.List;

import org.testng.ITestContext;
import org.testng.ITestListener;
import org.testng.ITestResult;

public class KnownIssueTestListener implements ITestListener {

	@Override
	public void onTestStart(ITestResult result) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onTestSuccess(ITestResult result) {
		// handleIssue(result, IssueType.FAIL);
		// TBD: negative tests

	}

	@Override
	public void onTestFailure(ITestResult result) {
		handleIssue(result, IssueType.PASS);
	}

	protected static void handleIssue(ITestResult result, IssueType whatItShouldHave) {
		KnownIssue issue = result.getMethod().getConstructorOrMethod().getMethod().getAnnotation(KnownIssue.class);
		KnownIssues issues = result.getMethod().getConstructorOrMethod().getMethod().getAnnotation(KnownIssues.class);

		List<KnownIssue> allIssues = newArrayList();
		allIssues.add(issue);
		if (issues != null) {
			allIssues.addAll(newArrayList(issues.value()));
		}

		boolean isKnownIssue = false;
		for (KnownIssue knownIssue : allIssues) {
			if (knownIssue != null) {
				if (knownIssue.type().equals(whatItShouldHave)) {
					if (knownIssue.throwableType().isInstance(result.getThrowable())) {
						if (result.getThrowable().getMessage().matches(knownIssue.messageMustMatch())) {
							boolean passedDueDate;
							if (knownIssue.dueTo() == -1) {
								passedDueDate = false;
							} else {
								passedDueDate = is(knownIssue.dueTo(), BEFORE, System.currentTimeMillis());
							}
							Reporter.log(knownIssue.issue(), result.getThrowable(), passedDueDate ? FAILURE
									: SUCCESS_PERCENTAGE_FAILURE);
							isKnownIssue = true;
						}
					}
				}
			}
		}
		if (isKnownIssue) {
			result.setStatus((whatItShouldHave == PASS) ? SUCCESS : FAILURE);
		}
	}

	@Override
	public void onTestSkipped(ITestResult result) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onTestFailedButWithinSuccessPercentage(ITestResult result) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onStart(ITestContext context) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onFinish(ITestContext context) {
		// TODO Auto-generated method stub

	}

}
