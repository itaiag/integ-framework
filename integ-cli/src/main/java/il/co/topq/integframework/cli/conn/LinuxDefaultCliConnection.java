/*
 * Copyright 2005-2010 Ignis Software Tools Ltd. All rights reserved.
 */
package il.co.topq.integframework.cli.conn;

import il.co.topq.integframework.cli.process.CliCommandExecution;
import il.co.topq.integframework.cli.terminal.Prompt;
import il.co.topq.integframework.cli.terminal.SSH;
import il.co.topq.integframework.cli.terminal.VT100FilterInputStream;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.*;

/**
 * Default CliConnection for a Cli connection to a linux machine. Protocol is
 * ssh Default port 22
 * 
 * @author goland
 */
public class LinuxDefaultCliConnection extends CliConnectionImpl {

	public LinuxDefaultCliConnection() {
		setDump(true);
		setUseTelnetInputStream(true);
		setProtocol("ssh");
		setPort(22);
	}

	public LinuxDefaultCliConnection(String host, String user, String password) {
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
		return null;
	}

	@Override
	public Prompt[] getPrompts() {
		List<Prompt> prompts = new ArrayList<>();
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
	 * 
	 * @param remoteFile
	 * @return
	 * @throws IOException
	 */
	public synchronized InputStream get(String remoteFile) throws IOException {

			if (terminal instanceof SSH) {
				SSH ssh = (SSH) terminal;
				return ssh.get(remoteFile);
			}
		return null;
	}

	public synchronized void get(String remoteFile, File dst) throws IOException {
		byte buf[] = new byte[10240];
		InputStream in = get(remoteFile);
		OutputStream out = new FileOutputStream(dst);
		int bytesRead = in.read(buf);
		while (bytesRead >= 0) {
			out.write(buf, 0, bytesRead);
			bytesRead = in.read(buf);
		}
		in.close();
		out.close();
	}

	/**
	 * get an {@link OutputStream} for a remote file<br />
	 * The session for opened for this SCP transfer must be closed using
	 * {@link OutputStream#close()}
	 * 
	 * @param remoteFile
	 *            The filename to create
	 * @param remoteDir
	 *            The folder in which to put the file to
	 * @param mode
	 *            a Linux octal mode, default "0600" if null
	 * @param length
	 *            the total length of the file
	 * @return an output stream to which put the file's data to
	 * @throws IOException
	 *             when
	 */
	public synchronized OutputStream put(String remoteDir, String remoteFile, String mode, long length) throws IOException {
		if (terminal instanceof SSH) {
			SSH ssh = (SSH) terminal;
			return ssh.put(remoteFile, length, remoteDir, mode);
		}
		return null;
	}

	public Iterator<String> fileList(String directory) throws Exception {
		CliCommandExecution execution = new CliCommandExecution(this, "find '" + directory + "' -maxdepth 1 -type f");
		execution.withTitle("list files in " + directory).error("No such file or directory").execute();
		String[] files = execution.getResult().split("\n");
		return Arrays.asList(files).iterator();
	}

	public boolean isProccessRunning(String name) throws Exception {
		CliCommandExecution execution = new CliCommandExecution(this, processInstancesCounterCommand(name));
		execution.withTitle("check if process " + name + " is running").execute();
		return !"0".equals(execution.getResult());
	}

	public boolean isProccessNotRunning(String name) throws Exception {
		CliCommandExecution execution = new CliCommandExecution(this, processInstancesCounterCommand(name));
		execution.withTitle("check if process " + name + " is not running").execute();
		return "0".equals(execution.getResult());
	}

	public static String processInstancesCounterCommand(String cmd) {
		return "ps -C '" + cmd + "' -o pid= |wc -l";
	}

	public Date getRemoteMachineDate() throws IOException {
		CliCommandExecution execution = new CliCommandExecution(this, "date +%s");
		// seconds since epoch
		execution.execute();
		return new Date(Long.parseLong(execution.getResult() + "000"));
	}

	public Calendar getRemoteMachineCalendar() throws Exception {
		Calendar calendar = new GregorianCalendar(getRemoteMachineTimeZone());
		calendar.setTimeInMillis(getRemoteMachineDate().getTime());
		return calendar;
	}

	public TimeZone getRemoteMachineTimeZone() throws Exception {
		CliCommandExecution execution = new CliCommandExecution(this, "date +%Z");
		execution.execute();
		return TimeZone.getTimeZone(execution.getResult());
	}
}
