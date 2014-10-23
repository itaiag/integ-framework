/*
 * Copyright 2005-2010 Ignis Software Tools Ltd. All rights reserved.
 */
package il.co.topq.integframework.cli.conn;

import static il.co.topq.integframework.reporting.Reporter.log;
import static il.co.topq.integframework.utils.StringUtils.isEmpty;
import static java.lang.System.currentTimeMillis;
import static java.lang.System.out;

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
	long timeout;
	boolean stop = false;

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
		while (!stop) {
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
				CliCommand cmd = new CliCommand("");
				if (!isEmpty(position)) {
					cmd.setPosition(position);
				}
				cli.setPrintStream(null);
				cli.command(cmd);
				cli.setPrintStream(out);
				position = cmd.getPosition();
				if (cmd.isFailed()) {
					log(getName() + " keepalive failed", null, false);
					try {
						synchronized (cli) {
							cli.connect();
						}
					} catch (Exception e) {
						continue;
					}
				} else {
					// System.out.println(getName() + " keepalive success");
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
		stop = true;
	}
}
