package il.co.topq.integframework.cli.process;

import java.util.concurrent.TimeUnit;

import il.co.topq.integframework.cli.conn.CliCommand;
import il.co.topq.integframework.cli.conn.CliConnection;
import il.co.topq.integframework.utils.StringUtils;

public class CliCommandExecution {
	private final CliConnection cliConnection;
	private long timeout = -1;
	private String title = "";
	private String cmd = "";

	public CliCommandExecution(CliConnection cliConnection) {
		this(cliConnection, "");
	}

	public CliCommandExecution(CliConnection cliConnection, String command){
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

	public void execute(String command) throws Exception {
		this.cmd = command;
		execute();
	}
	

	public void execute() throws Exception {
		if (StringUtils.isEmpty(cmd)) {
			throw new NullPointerException("command is not set");
		}
		CliCommand cliCommand = new CliCommand(cmd);
		cliCommand.setTimeout(timeout);
		this.cliConnection.handleCliCommand(title, cliCommand);
	}

}
