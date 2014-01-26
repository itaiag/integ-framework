/*
 * Copyright 2005-2010 Ignis Software Tools Ltd. All rights reserved.
 */
package il.co.topq.integframework.cli.conn;

import il.co.topq.integframework.cli.terminal.Prompt;
import il.co.topq.integframework.cli.terminal.SSH;
import il.co.topq.integframework.cli.terminal.VT100FilterInputStream;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;

/**
 * Default CliConnection for a Cli connection to a linux machine.
 * Protocol is ssh
 * Default port 22
 * @author goland
 */
public class LinuxDefaultCliConnection extends CliConnectionImpl {

	public LinuxDefaultCliConnection(){
		setDump(true);
		setUseTelnetInputStream(true);
		setProtocol("ssh");
		setPort(22);
	}

	public LinuxDefaultCliConnection(String host,String user,String password){
		this();
		setUser(user);
		setPassword(password);
		setHost(host);
	}
	
	@Override
	public void init() throws Exception {
		super.init();
	}
	
	@Override
	public void connect() throws Exception {
		super.connect();
		terminal.addFilter(new VT100FilterInputStream());
	}
	
	public Position[] getPositions() {
		return null;
	}

	public Prompt[] getPrompts() {
		ArrayList<Prompt> prompts = new ArrayList<Prompt>();		
		Prompt p = new Prompt();
		p.setCommandEnd(true);
		p.setPrompt("$ ");
		prompts.add(p);
		
		p = new Prompt();
		p.setPrompt("login: ");
		p.setStringToSend(getUser());
		prompts.add(p);

		p = new Prompt();
		p.setPrompt("login as: "); // ubuntu style
		p.setStringToSend(getUser());
		prompts.add(p);
		
		p = new Prompt();
		p.setPrompt("Password: ");
		p.setStringToSend(getPassword());
		prompts.add(p);
		return prompts.toArray(new Prompt[prompts.size()]);
	}
	
	/**
	 * get an {@link InputStream} for a remote file<br /> 
	 * The session for opened for this SCP transfer must be closed using
	 * {@link InputStream#close()}
	 * @param remoteFile
	 * @return
	 * @throws IOException
	 */
	public InputStream get(String remoteFile) throws IOException {
		if (terminal instanceof SSH) {
			SSH ssh = (SSH) terminal;
			return ssh.get(remoteFile);
		}
		return  null;
	}
	
	public void get(String remoteFile, File dst) throws IOException {
	    byte buf[] = new byte[10240];
	    InputStream in = get(remoteFile);
	    OutputStream out = new FileOutputStream(dst);
	    int bytesRead = in.read(buf);
	    while (bytesRead >= 0) {
	      out.write(buf, 0, bytesRead);
	      bytesRead = in.read(buf);
	    }
	    in.close();out.close();
	}

}
