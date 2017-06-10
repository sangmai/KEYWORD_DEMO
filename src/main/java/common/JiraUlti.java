package common;

import net.rcarz.jiraclient.BasicCredentials;
import net.rcarz.jiraclient.Field;
import net.rcarz.jiraclient.Issue;
import net.rcarz.jiraclient.JiraClient;
import net.rcarz.jiraclient.JiraException;

public class JiraUlti {
	private BasicCredentials creds;
	private JiraClient jira;
	private Issue issue;

	public JiraUlti(BasicCredentials creds, JiraClient jira, Issue issue) {
		this.creds = creds;
		this.jira = jira;
		this.issue = issue;
	}

	public JiraUlti() {
		super();
		// TODO Auto-generated constructor stub
	}

	public JiraClient connectToJira(String hostName, String username, String password) {
		creds = new BasicCredentials(username, password);
		jira = new JiraClient(hostName, creds);
		return jira;
	}

	public void createNewIssue(String projectName, String issueType, String summary, String description,
			String reporter, String assingee) {
		try {
			issue = jira.createIssue(projectName, issueType).field(Field.SUMMARY, summary)
					.field(Field.DESCRIPTION, description).field(Field.REPORTER, reporter)
					.field(Field.ASSIGNEE, assingee).execute();
		} catch (JiraException e) {
			System.out.println(e.getMessage());
		}
	}
}
