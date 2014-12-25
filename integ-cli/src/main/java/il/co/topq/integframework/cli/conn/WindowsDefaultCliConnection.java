/*
 * Copyright 2005-2010 Ignis Software Tools Ltd. All rights reserved.
 */
package il.co.topq.integframework.cli.conn;

import il.co.topq.integframework.cli.terminal.Prompt;
import il.co.topq.integframework.cli.terminal.VT100FilterInputStream;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Default CliConnection for a Cli connection to a windows xm/vista machine.
 * Protocol is Telnet.
 * Default port is 23.
 * 
 * @author goland
 */
public class WindowsDefaultCliConnection extends CliConnectionImpl {

	public WindowsDefaultCliConnection(){
		setDump(true);
		setUseTelnetInputStream(true);
		setProtocol("telnet");
		setPort(23);
	}

	public WindowsDefaultCliConnection(String host,String user,String password){
		this();
		setUser(user);
		setPassword(password);
		setHost(host);
	}
	
	@Override
	public void init() throws IOException {
		super.init();
	}
	
	@Override
	public void connect() throws IOException {
		super.connect();
		terminal.addFilter(new VT100FilterInputStream());
	}
	
	@Override
	public Position[] getPositions() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Prompt[] getPrompts() {
		List<Prompt> prompts = new ArrayList<>();
		Prompt p = new Prompt();
		p.setPrompt("login:");
		p.setStringToSend(getUser());
		prompts.add(p);
		p = new Prompt();
		p.setPrompt("password:");
		p.setStringToSend(getPassword());
		prompts.add(p);
		p = new Prompt();
		p.setPrompt(">");
		p.setCommandEnd(true);
		prompts.add(p);
		return prompts.toArray(new Prompt[prompts.size()]);
	}

}
