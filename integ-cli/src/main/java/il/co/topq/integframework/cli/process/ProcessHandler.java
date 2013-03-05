package il.co.topq.integframework.cli.process;

import java.io.File;
import java.io.IOException;
import java.util.LinkedList;
import java.util.Queue;

import org.apache.commons.exec.CommandLine;
import org.apache.commons.exec.DefaultExecuteResultHandler;
import org.apache.commons.exec.DefaultExecutor;
import org.apache.commons.exec.ExecuteException;
import org.apache.commons.exec.ExecuteWatchdog;
import org.apache.commons.exec.LogOutputStream;
import org.apache.commons.exec.PumpStreamHandler;

/**
 * Method for executing processes in blocking and unblocking way.
 * 
 * @author Itai
 * 
 */
public class ProcessHandler {

	private CollectingLogOutputStream out;

	private DefaultExecuteResultHandler resultHandler;

	/**
	 * Sets the working dir to <code>workingDir</code> launches process
	 * <code>commandLine</code>
	 * 
	 * @param workingDir
	 *            If null the working dir will be the current application dir.s
	 * @param commandLine
	 *            The line to execute for launching the process
	 * @param blocking
	 * @throws IOException
	 *             If the working dir is not exist or if fails to execute the
	 *             process
	 */
	public int execute(File workingDir, CommandLine commandLine, boolean blocking, int timeout) throws IOException {
		DefaultExecutor executor = new DefaultExecutor();
		if (null != workingDir) {
			executor.setWorkingDirectory(workingDir.getCanonicalFile());
		}

		if (!blocking) {
			out = new CollectingLogOutputStream();
			executor.setStreamHandler(new PumpStreamHandler(out));
			resultHandler = new DefaultExecuteResultHandler();
			executor.execute(commandLine, resultHandler);
			return 0;
		} else {
			// Sets the stream to null because of a bug that causes the process
			// to stuck when it tries to close the stream.
			executor.setStreamHandler(new PumpStreamHandler(null));
			executor.setWatchdog(new ExecuteWatchdog(timeout));
			return executor.execute(commandLine);
		}

	}

	/**
	 * Returns the process error level or -1 if process is not exist
	 * 
	 * @return
	 */
	public int getExitValue() {
		if (resultHandler != null) {
			return resultHandler.getExitValue();
		}
		return -1;
	}

	/**
	 * Returns the process execution exception if exists
	 * 
	 * @return
	 */
	public ExecuteException getException() {
		if (resultHandler != null) {
			return resultHandler.getException();

		}
		return null;
	}

	/**
	 * Sets the working dir to <code>workingDir</code> launches process
	 * <code>commandLine</code>
	 * 
	 * @param workingDir
	 *            If null the working dir will be the current application dir.s
	 * @param commandLine
	 *            The line to execute for launching the process
	 * @param blocking
	 * @throws IOException
	 *             If the working dir is not exist or if fails to execute the
	 *             process
	 */
	public int execute(File workingDir, String commandLine, boolean blocking, int timeout) throws IOException {
		return execute(workingDir, CommandLine.parse(commandLine), blocking, timeout);
	}

	/**
	 * Gets the process stdout
	 * 
	 * @return
	 */
	public String[] getOut() {
		return out.getOut();
	}

	class CollectingLogOutputStream extends LogOutputStream {
		private static final int QUEUE_SIZE = 1000;
		private final Queue<String> lines = new LinkedList<String>();

		@Override
		protected void processLine(String line, int level) {
			if (lines.size() >= QUEUE_SIZE) {
				lines.remove();
			}
			lines.add(line);
		}

		public String[] getOut() {
			return lines.toArray(new String[] {});
		}

	}

}
