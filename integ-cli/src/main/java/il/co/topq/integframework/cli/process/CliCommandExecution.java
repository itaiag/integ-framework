package il.co.topq.integframework.cli.process;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.TimeUnit;

import il.co.topq.integframework.AbstractModule;
import il.co.topq.integframework.cli.conn.CliCommand;
import il.co.topq.integframework.cli.conn.CliConnection;
import il.co.topq.integframework.utils.StringUtils;

public class CliCommandExecution {
	private final CliConnection cliConnection;
	private long timeout = -1;
	private String title = "";
	private String cmd = "";
	private List<String> musts;
	protected String result;
	public CliCommandExecution(CliConnection cliConnection) {
		this(cliConnection, "");
	}

	public CliCommandExecution(CliConnection cliConnection, String command) {
		this.cliConnection = cliConnection;
		this.cmd = command;
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
		if (musts == null || musts.isEmpty()) {
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
		if (musts!=null && !musts.isEmpty()) {
			cliCommand.addMusts(musts);
		}
		cliCommand.setTimeout(timeout);
		this.cliConnection.handleCliCommand(title, cliCommand);
		if (cliConnection instanceof AbstractModule){
			setResult(((AbstractModule)cliConnection).getActual(String.class));
		}
	}

	public String getResult() {
		return result;
	}

	protected void setResult(String result) {
		this.result = result;
	}
}
