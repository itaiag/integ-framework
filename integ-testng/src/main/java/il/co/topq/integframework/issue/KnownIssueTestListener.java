package il.co.topq.integframework.issue;

import il.co.topq.integframework.issue.KnownIssue.KnownIssues;

import java.util.List;

import org.testng.ITestContext;
import org.testng.ITestListener;
import org.testng.ITestResult;

import com.google.common.collect.Lists;

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

		List<KnownIssue> allIssues = Lists.newArrayList();
		allIssues.add(issue);
		if (issues != null) {
			allIssues.addAll(Lists.newArrayList(issues.value()));
		}

		boolean isKnownIssue = false;
		for (KnownIssue knownIssue : allIssues) {
			if (knownIssue != null) {
				if (knownIssue.type().equals(whatItShouldHave)) {
					if (knownIssue.throwableType().isInstance(result.getThrowable())) {
						if (result.getThrowable().getMessage().matches(knownIssue.messageMustMatch())) {
							if (knownIssue.dueTo() < System.currentTimeMillis()) {
								isKnownIssue = true;
							}
						}
					}
				}
			}
		}
		if (isKnownIssue) {
			result.setStatus((whatItShouldHave == IssueType.PASS) ? ITestResult.SUCCESS : ITestResult.FAILURE);
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
