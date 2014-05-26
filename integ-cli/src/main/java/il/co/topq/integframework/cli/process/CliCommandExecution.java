package il.co.topq.integframework.cli.process;

import il.co.topq.integframework.AbstractModule;
import il.co.topq.integframework.assertion.AbstractAssertionLogic;
import il.co.topq.integframework.assertion.Assert;
import il.co.topq.integframework.assertion.DefaultAssertionListener;
import il.co.topq.integframework.assertion.FindTextAssertion;
import il.co.topq.integframework.assertion.IAssertionLogic;
import il.co.topq.integframework.assertion.TextNotFoundAssertion;
import il.co.topq.integframework.cli.conn.CliCommand;
import il.co.topq.integframework.cli.conn.CliConnection;
import il.co.topq.integframework.utils.StringUtils;

import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.TimeUnit;

import org.apache.commons.io.output.NullOutputStream;

public class CliCommandExecution {
	private final CliConnection cliConnection;
	// setting default timeout to 1 minute.
	private long timeout = TimeUnit.MINUTES.toMillis(1);
	private String title = "";
	protected String cmd = "";
	private List<String> musts, errors;
	protected String result;
	private boolean silently = false;
	protected final List<IAssertionLogic<String>> assrtions;
	protected final PrintStream silentPrintStream = new PrintStream(new NullOutputStream());

	public CliCommandExecution(CliConnection cliConnection) {
		this(cliConnection, "");
	}

	public CliCommandExecution(CliConnection cliConnection, String command) {
		this.cliConnection = cliConnection;
		this.cmd = command;
		assrtions = new ArrayList<IAssertionLogic<String>>();
	}

	public CliCommandExecution withTimeout(long timeout) {
		this.timeout = timeout;
		return this;
	}

	public CliCommandExecution withTimeout(long duration, TimeUnit timeUnit) {
		this.timeout = timeUnit.toMillis(duration);
		return this;
	}

	public CliCommandExecution withTitle(String title) {
		this.title = title;
		return this;
	}

	public CliCommandExecution mustHaveResponse(String... strings) {
		if (musts == null) {
			musts = new ArrayList<String>(strings.length);
		}
		musts.addAll(Arrays.asList(strings));
		return this;
	}

	public void execute(String command) throws Exception {
		this.cmd = command;
		execute();
	}

	/**
	 * execute the command on the {@link CliConnection}. you can gain the result
	 * by {@link #getResult()}. if any error string was provided in
	 * {@link #error(String...)}, the result will <b>be</b> the error string
	 * instance<br>
	 * note that the actual result will be set to the {@link CliConnection}'s
	 * actual, if it is an {@link AbstractModule}, accessible by
	 * {@link AbstractModule#getActual()}. the value could contain either the
	 * first or last prompt.
	 * 
	 * @throws Exception
	 */
	public void execute() throws Exception {
		if (StringUtils.isEmpty(cmd)) {
			throw new NullPointerException("command is not set");
		}

		CliCommand cliCommand = new CliCommand(cmd);
		if (!(cliConnection instanceof AbstractModule)) {
			if (musts != null && !musts.isEmpty()) {
				cliCommand.addMusts(musts);
			}
			if (errors != null && !errors.isEmpty()) {
				for (String error : errors) {
					cliCommand.addErrors(error);
				}
			}
		}
		cliCommand.setTimeout(timeout);
		cliCommand.setSilent(silently);
		if (silently) {
			cliConnection.setPrintStream(silentPrintStream);
		}
		this.cliConnection.handleCliCommand(title, cliCommand);

		if (cliConnection instanceof AbstractModule) {
			AbstractModule cliModule = (AbstractModule) cliConnection;
			String result = cliModule.getActual(String.class);
			String commandLine = cmd + this.cliConnection.getEnterStr();
			result = StringUtils.getFirstSubStringSuffix(result, commandLine, true);
			if (result.contains(this.cliConnection.getEnterStr())) {
				result = StringUtils.getPrefix(result, this.cliConnection.getEnterStr());
			}
			setResult(result.trim());
			cliModule.setActual(this.result);
			if (musts != null && !musts.isEmpty()) {
				for (String must : musts) {
					Assert.assertLogic(cliModule.getActual(String.class), new FindTextAssertion(must));
				}
			}
			if (errors != null && !errors.isEmpty()) {
				for (final String error : errors) {
					Assert.assertLogic(cliModule.getActual(String.class), new TextNotFoundAssertion(error), new setResultOnError(
							error));
				}
			}
		}
	}

	private final class setResultOnError extends DefaultAssertionListener<String> {

		private final String error;

		public setResultOnError(String error) {
			this.error = error;
		}

		@Override
		public void assertionFailed(String actual, AbstractAssertionLogic<String> logic) {
			setResult(error);
			super.assertionFailed(actual, logic);
		}
	}

	public String getResult() {
		return result;
	}

	protected void setResult(String result) {
		this.result = result;
	}

	// public String untilResponse(final AbstractAssertionLogic<String> logic) {
	// CliConnectionWait wait = new CliConnectionWait(cliConnection);
	//
	// return wait.until(new CliExpectedCondition<String>() {
	// @Override
	// public String apply(CliConnection input) {
	// try {
	// execute();
	// Assert.assertLogic(getResult(), logic);
	// return getResult();
	//
	// } catch (Exception e) {
	// throw new RuntimeException(e);
	// }
	// }
	// });
	//
	//
	// }

	public CliCommandExecution error(String... errors) {
		if (this.errors == null) {
			this.errors = new ArrayList<String>(errors.length);
		}
		this.errors.addAll(Arrays.asList(errors));
		return this;
	}

	public CliCommandExecution silently() {
		this.silently = true;
		return this;
	}
}
