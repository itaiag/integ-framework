/*
 * Copyright 2005-2010 Ignis Software Tools Ltd. All rights reserved.
 */
package il.co.topq.integframework.cli.conn;

import static il.co.topq.integframework.reporting.Reporter.log;
import static java.lang.System.currentTimeMillis;

/**
 * Monitors the allowed idle time of a machine.
 * (Many devices forces log out in case of the maximum idle time has passed)
 * In order to activate this monitor the maxIdleTime tag(in miliSeconds) should be added to the SUT file.
 * under conn / cli 
 * 
 * Note that the actual keep alive 'Enter' will be done at idleTime * 0.9
 *
 */
public class IdleMonitor extends Thread {
	CliConnectionImpl cli;
	long timeout;
	boolean stop = false;
	
	/**	 
	 * @param cli CliConnection 
	 * @param timeout (miliSeconds) the maximum idleTime
	 */
	public IdleMonitor(CliConnectionImpl cli, long timeout){
		super("Idle monitor for " + cli.getName());
		setDaemon(true);
		this.cli = cli;
		this.timeout = timeout;
	}
	
	@Override
	public void run(){
		System.out.println(this.getName() + " started");
		while(!stop){
			long lastCommandTime = cli.getLastCommandTime();
			if(lastCommandTime == 0){
				try {
					sleep(timeout / 2);
				} catch (InterruptedException e) {
					setStop();
				}
				continue;
			}
			if (currentTimeMillis() - lastCommandTime > (timeout * 0.9)) {
				CliCommand cmd = new CliCommand();
				cmd.setCommands(new String[]{""});
				cmd.setSilent(true);
				cli.command(cmd);
				if(cmd.isFailed()){
					log(getName() + " keepalive failed", null, false);
				} else {
					System.out.println(getName() + " keepalive success");
				}
			} else {
				try {
					long toSleep = (long) (timeout * 0.9) - (currentTimeMillis() - lastCommandTime);
					if(toSleep > 0){
						sleep(toSleep);
					}
				} catch (InterruptedException e) {
					setStop();
				}
			}
		}
	}
	public void setStop(){
		stop = true;
	}
}
