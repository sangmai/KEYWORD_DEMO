package common;

import java.text.SimpleDateFormat;
import java.util.Calendar;

import org.testng.IInvokedMethod;
import org.testng.IInvokedMethodListener;
import org.testng.ITestContext;
import org.testng.ITestListener;
import org.testng.ITestResult;

import net.rcarz.jiraclient.BasicCredentials;
import net.rcarz.jiraclient.Field;
import net.rcarz.jiraclient.Issue;
import net.rcarz.jiraclient.JiraClient;
import net.rcarz.jiraclient.JiraException;
import testCases.ExecuteTest;

public class JiraListener implements IInvokedMethodListener, ITestListener {

	private final BasicCredentials auth = new BasicCredentials("maisang90dn@gmail.com", "Abcd123!@#");
	private final JiraClient jira = new JiraClient("https://sangmai.atlassian.net", auth);
	private Issue newIssue;
	private String issue;
	private final String project = "SEDEMO";
	ITestResult result;

	@Override
	public void onTestStart(ITestResult result) {

	}

	@Override
	public void onTestSuccess(ITestResult result) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onTestFailure(ITestResult result) {
		try {
			newIssue = jira.createIssue(project, "Bug").field(Field.SUMMARY, "Bug report by Automation Testing")
					.field(Field.DESCRIPTION,
							"Please re-check Testcase " + ExecuteTest.getTestcaseName() + " \nCause by : "
									+ result.getThrowable().getMessage())
					.field(Field.REPORTER, "admin").field(Field.ASSIGNEE, "admin").execute();
		} catch (JiraException e) {
			System.out.println(e.getMessage());
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
		try {
			newIssue = jira.createIssue("SEDEMO", "Task").field(Field.SUMMARY, "Start Automation Test")
					.field(Field.DESCRIPTION, "Automation Run Regression via Selenium. This is Auto Task")
					.field(Field.REPORTER, "admin").field(Field.ASSIGNEE, "admin").execute();
			System.out.println(newIssue);
			issue = newIssue.toString();
		} catch (JiraException ex) {
			System.err.println(ex.getMessage());

			if (ex.getCause() != null) {
				System.err.println(ex.getCause().getMessage());
			}
		}
	}

	@Override
	public void onFinish(ITestContext context) {
		try {
			newIssue = jira.getIssue(issue);
			newIssue.addComment("Automation test is completed at " + getTime(result.getEndMillis()));
			newIssue.transition().execute("Done");
		} catch (JiraException e) {
			System.out.println(e.getMessage());
		}
	}

	@Override
	public void beforeInvocation(IInvokedMethod method, ITestResult testResult) {
		// TODO Auto-generated method stub

	}

	@Override
	public void afterInvocation(IInvokedMethod method, ITestResult testResult) {
		// TODO Auto-generated method stub
		this.result = testResult;

	}

	private String getTime(long millis) {
		final Calendar calendar = Calendar.getInstance();
		final SimpleDateFormat format = new SimpleDateFormat("dd-MM-yyyy hh:mm:ss");
		calendar.setTimeInMillis(millis);
		return format.format(calendar.getTime());
	}

}
