package il.co.topq.integframework;

import il.co.topq.integframework.cli.process.ProcessHandler;

import java.io.File;
import java.io.IOException;

import junit.framework.Assert;

import org.testng.Reporter;

/**
 * Abstract system module that needs to be inherited by system modules that
 * modules jars that can be launched and shutdown using external scripts.
 * 
 * @author Itai Agmon
 * 
 */
public abstract class AbstractProcessExecutorSystemModule {
	protected static final String WINDOWS_EXECUTOR = "cmd.exe /c";
	protected static final String LINUX_EXECUTOR = "bash";
	protected static final String WINDOWS_START_SCRIPT_FILE = "startup.bat";
	protected static final String LINUX_START_SCRIPT_FILE = "startup.sh";
	protected static final String WINDOWS_STOP_SCRIPT_FILE = "shutdown.bat";
	protected static final String LINUX_STOP_SCRIPT_FILE = "shutdown.sh";
	protected static final String ARTIFACTS_FOLDER = "target";

	/**
	 * Starts the process and asserts error level of 0.
	 * 
	 * @throws IOException
	 */
	public void start() throws IOException {
		Reporter.log("About to start '" + getProcessName() + "'", true);

		final String executor = (System.getProperty("os.name").toLowerCase().contains("windows")) ? WINDOWS_EXECUTOR
				: LINUX_EXECUTOR;
		final String script = (System.getProperty("os.name").toLowerCase().contains("windows")) ? WINDOWS_START_SCRIPT_FILE
				: LINUX_START_SCRIPT_FILE;

		if (!new File(getBinFolder(), script).exists()) {
			throw new IOException("Script file " + script + " is not exist");
		}
		ProcessHandler process = new ProcessHandler();
		Assert.assertEquals("Failed to start " + getProcessName() + " process", 0,
				process.execute(new File(getBinFolder()), executor + " " + script, true, 10000));
		Reporter.log("'" + getProcessName() + "' process is up and running", true);

	}

	/**
	 * Stops the process
	 * 
	 * @throws IOException
	 */
	public void stop() throws IOException {
		Reporter.log("About to stop '" + getProcessName() + "' process", true);
		final String executor = (System.getProperty("os.name").toLowerCase().contains("windows")) ? WINDOWS_EXECUTOR
				: LINUX_EXECUTOR;
		final String script = (System.getProperty("os.name").toLowerCase().contains("windows")) ? WINDOWS_STOP_SCRIPT_FILE
				: LINUX_STOP_SCRIPT_FILE;

		if (!new File(getBinFolder(), script).exists()) {
			throw new IOException("Script file " + script + " is not exist");
		}
		ProcessHandler process = new ProcessHandler();
		Assert.assertEquals("Failed to stop Blocking process", 0,
				process.execute(new File(getBinFolder()), executor + " " + script, true, 10000));
		Reporter.log("'" + getProcessName() + "' process is down", true);
	}

	/**
	 * The location of the startup and shutdown scripts.
	 * 
	 * @return Binary folder
	 */
	protected abstract String getBinFolder();

	/**
	 * Override it to provide the process name. Mostly used for reporting.
	 * 
	 * @return process name
	 */
	protected abstract String getProcessName();

	protected String findExistingFolder(String path) {
		final String aDir = ARTIFACTS_FOLDER + File.separator;
		String s = path;
		if (!new File(s).exists()) {
			s = s.substring(aDir.length()); // remove target/
		}
		return s;

	}

}
