/*
 * Created on 13/09/2005
 *
 * Copyright 2005-2010 Ignis Software Tools Ltd. All rights reserved.
 */
package il.co.topq.integframework.cli.terminal;

import il.co.topq.integframework.utils.StringUtils;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.IOException;


/**
 * Terminal implementation for working on top
 * of windows's cmd.exe terminal emulation.
 *  
 * When set to true the output stream of the cmd.exe 
 * process is closed after string is sent. 
 * This was added for applications that require closing the output stream
 * in order for the data to get to their input stream.
 * An example application is wmi command line tool (wmic)
 * @author yaels,golan derazon
 */
public class Cmd extends Terminal {
	
	private String processDir;
	private Process process;

	private boolean closeOutputOnSend;
	
	public Cmd(){
	}

	public Cmd(String dir){
		this.processDir = dir;
	}
	
	/* (non-Javadoc)
	 * @see il.co.topq.integframework.cli.terminal.Terminal#connect()
	 */
	@Override
	public void connect() throws IOException {
		File root = null;
		if (!StringUtils.isEmpty(processDir)){
			root = new File(processDir);
		}
		ProcessBuilder builder = new ProcessBuilder("cmd.exe");
		builder.directory(root);
		builder.redirectErrorStream(true);
		process = builder.start();
		in =  process.getInputStream();
		out = new BufferedOutputStream(process.getOutputStream());
	}

    @Override
	public synchronized void sendString(String command, boolean delayedTyping) throws IOException, InterruptedException{
    	super.sendString(command, delayedTyping);
    	if (isCloseOutputOnSend()){
    		out.close();
    	}
    }
    /* (non-Javadoc)
	 * @see il.co.topq.integframework.cli.terminal.Terminal#disconnect()
	 */
	@Override
	public void disconnect() throws IOException {
		process.destroy();
	}

	/* (non-Javadoc)
	 * @see il.co.topq.integframework.cli.terminal.Terminal#isConnected()
	 */
	@Override
	public boolean isConnected() {
		if (process == null){
			return false;
		}
		try {
			process.exitValue();
			return false;
		} catch (IllegalThreadStateException e) {
			return true;
		}
	}

	/* (non-Javadoc)
	 * @see il.co.topq.integframework.cli.terminal.Terminal#getConnectionName()
	 */
	@Override
	public String getConnectionName() {
		return "cmd";
	}
	
	private boolean isCloseOutputOnSend() {
		return closeOutputOnSend;
	}

	/**
	 * 
	 */
	public void setCloseOutputOnSend(boolean closeOutPutOnSend) {
		this.closeOutputOnSend = closeOutPutOnSend;
	}
}
