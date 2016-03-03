package il.co.topq.integframework.cli.process;

import il.co.topq.integframework.AbstractModuleImpl;
import il.co.topq.integframework.cli.conn.CliConnectionImpl;

public class CommandLineModule extends AbstractModuleImpl {

	protected CliConnectionImpl cliConnectionImpl;

	public CliCommandExecution executer() {
		return new CliCommandExecution(cliConnectionImpl);
	}

	public CliConnectionImpl getCliConnectionImpl() {
		return cliConnectionImpl;
	}

	public void setCliConnectionImpl(CliConnectionImpl cliConnectionImpl) {
		this.cliConnectionImpl = cliConnectionImpl;
	}

}
