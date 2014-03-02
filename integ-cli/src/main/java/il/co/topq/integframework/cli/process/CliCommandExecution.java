package il.co.topq.integframework.cli.process;

import il.co.topq.integframework.AbstractModule;
import il.co.topq.integframework.assertion.Assert;
import il.co.topq.integframework.assertion.FindTextAssertion;
import il.co.topq.integframework.assertion.IAssertionLogic;
import il.co.topq.integframework.assertion.TextNotFoundAssertion;
import il.co.topq.integframework.cli.conn.CliCommand;
import il.co.topq.integframework.cli.conn.CliConnection;
import il.co.topq.integframework.utils.StringUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class CliCommandExecution {
	private final CliConnection cliConnection;
	private long timeout = -1;
	private String title = "";
	protected String cmd = "";
	private List<String> musts, errors;
	protected String result;
	private boolean silently = false;
	protected final List<IAssertionLogic<String>> assrtions;

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
				for (String error : errors) {
					Assert.assertLogic(cliModule.getActual(String.class), new TextNotFoundAssertion(error));
				}
			}
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
