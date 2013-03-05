package il.co.topq.integframework.junit.report;

public interface ReporterI {
	public enum Status {
		SUCCESS, FAILURE
	}

	public void report(String title, String message, Status status);

}
