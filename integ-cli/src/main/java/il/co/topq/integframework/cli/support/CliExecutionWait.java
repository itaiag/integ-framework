package il.co.topq.integframework.cli.support;

import il.co.topq.integframework.cli.conn.CliConnection;
import il.co.topq.integframework.cli.process.CliCommandExecution;
import il.co.topq.integframework.support.FluentWait;

import java.util.concurrent.TimeUnit;

public class CliExecutionWait extends FluentWait<CliCommandExecution> {

	private static final int DEFAULT_POLLING_INTERVAL_IN_MILLISECONDS = 500;
	private static final int DEFAULT_TIMEOUT_IN_MINUTES = 2;

	public CliExecutionWait(CliCommandExecution execution) {
		super(execution);
		withTimeout(DEFAULT_TIMEOUT_IN_MINUTES, TimeUnit.MINUTES);
		pollingEvery(DEFAULT_POLLING_INTERVAL_IN_MILLISECONDS, TimeUnit.MILLISECONDS);
		ignoring(AssertionError.class);
	}

	public CliExecutionWait(CliConnection cliConnection, String command) {
		this(new CliCommandExecution(cliConnection, command));
	}

}
