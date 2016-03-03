package il.co.topq.integframework.cli.support;

import il.co.topq.integframework.cli.conn.CliConnection;
import il.co.topq.integframework.support.FluentWait;

import java.util.concurrent.TimeUnit;

public class CliConnectionWait extends FluentWait<CliConnection> {

	private static final int DEFAULT_POLLING_INTERVAL_IN_MILLISECONDS = 500;
	private static final int DEFAULT_TIMEOUT_IN_MINUTES = 2;

	public CliConnectionWait(CliConnection input) {
		super(input);
		withTimeout(DEFAULT_TIMEOUT_IN_MINUTES, TimeUnit.MINUTES);
		pollingEvery(DEFAULT_POLLING_INTERVAL_IN_MILLISECONDS, TimeUnit.MILLISECONDS);
		ignoring(AssertionError.class);
	}

}
