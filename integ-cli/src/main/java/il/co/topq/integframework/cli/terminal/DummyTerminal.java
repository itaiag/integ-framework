package il.co.topq.integframework.cli.terminal;

import java.io.IOException;

public class DummyTerminal extends Terminal {

	public DummyTerminal() {
		
	}

	private boolean conneted = false;
	@Override
	public void connect() throws IOException {
		conneted = true;		
	}

	@Override
	public void disconnect() throws IOException {
		conneted = false;
	}

	@Override
	public boolean isConnected() {
		return conneted;
	}

	@Override
	public String getConnectionName() {
		return "dummy connection";
	}

}
