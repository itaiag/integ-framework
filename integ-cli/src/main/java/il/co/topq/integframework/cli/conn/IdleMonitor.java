/*
 * Copyright 2005-2010 Ignis Software Tools Ltd. All rights reserved.
 */
package il.co.topq.integframework.cli.conn;

import static il.co.topq.integframework.reporting.Reporter.log;
import static il.co.topq.integframework.utils.StringUtils.isEmpty;
import static java.lang.System.currentTimeMillis;
import static java.lang.System.out;
import static org.apache.commons.io.output.NullOutputStream.NULL_OUTPUT_STREAM;
import il.co.topq.integframework.cli.process.CliCommandExecution;

import java.io.PrintStream;

/**
 * Monitors the allowed idle time of a machine. (Many devices forces log out in
 * case of the maximum idle time has passed) In order to activate this monitor
 * the maxIdleTime tag(in miliSeconds) should be added to the SUT file. under
 * conn / cli
 * 
 * Note that the actual keep alive 'Enter' will be done at idleTime * 0.9
 * 
 */
public class IdleMonitor extends Thread {
	CliConnectionImpl cli;
	final PrintStream silentPrintStream = new PrintStream(NULL_OUTPUT_STREAM);
	long timeout;

	/**
	 * @param cli
	 *            CliConnection
	 * @param timeout
	 *            (miliSeconds) the maximum idleTime
	 */
	public IdleMonitor(CliConnectionImpl cli, long timeout) {
		super("Idle monitor for " + cli.getName());
		setDaemon(true);
		this.cli = cli;
		this.timeout = timeout;
	}

	@Override
	public void run() {
		out.println(this.getName() + " started");
		String position = null;
		while (!isInterrupted()) {
			try {
				synchronized (cli) {
					if (!cli.terminal.isConnected()) {
						cli.connect();
					}
				}
			} catch (Exception e) {
				continue;
			}
			long lastCommandTime = cli.getLastCommandTime();
			if (lastCommandTime == 0) {
				try {
					sleep(timeout / 2);
				} catch (InterruptedException e) {
					setStop();
				}
				continue;
			}
			if (currentTimeMillis() - lastCommandTime > (timeout * 0.9)) {
				synchronized (cli) {
					CliCommand cmd = new CliCommand("");
					if (!isEmpty(position)) {
						cmd.setPosition(position);
					}
					PrintStream stream =  cli.terminal.getPrintStream();
					cli.setPrintStream(silentPrintStream);
					cli.command(cmd);
					cli.setPrintStream(stream);
					position = cmd.getPosition();
					if (cmd.isFailed()) {
						log(getName() + " keepalive failed", null, false);
					} else {
						// System.out.println(getName() + " keepalive success");
					}
				}
			} else {
				try {
					long toSleep = (long) (timeout * 0.9) - (currentTimeMillis() - lastCommandTime);
					if (toSleep > 0) {
						sleep(toSleep);
					}
				} catch (InterruptedException e) {
					setStop();
				}
			}
		}
	}

	public void setStop() {
		this.interrupt();
	}
}
