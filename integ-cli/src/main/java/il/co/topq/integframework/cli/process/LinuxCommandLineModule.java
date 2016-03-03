package il.co.topq.integframework.cli.process;

import il.co.topq.integframework.cli.conn.LinuxDefaultCliConnection;

public class LinuxCommandLineModule extends CommandLineModule {
	protected LinuxDefaultCliConnection linux;

	@Override
	public void init() throws Exception {
		super.init();
		linux = (LinuxDefaultCliConnection) cliConnectionImpl;
		// casting exception is good!!! misuse of this class will cause it!!
	}
}
