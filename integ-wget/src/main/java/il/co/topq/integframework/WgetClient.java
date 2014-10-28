package il.co.topq.integframework;

import static il.co.topq.integframework.utils.StringUtils.isEmpty;
import il.co.topq.integframework.WgetModule.WgetCommand;
import il.co.topq.integframework.cli.conn.LinuxDefaultCliConnection;
import il.co.topq.integframework.cli.process.CliCommandExecution;
import il.co.topq.integframework.utils.StringUtils;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.OutputStream;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.TimeUnit;

import org.apache.commons.io.IOUtils;

public class WgetClient implements Callable<String> {
	final String userAgent, ip;
	final List<String> headers = new ArrayList<>();
	private final WgetModule module;
	private PostDataGenerator dataGenerator;

	private String response;

	public WgetClient(WgetModule module, String ip, String userAgent) {
		this.module = module;
		this.ip = ip;
		this.userAgent = userAgent;
		module.setName("Wget client");
	}

	public String getUserAgent() {
		return userAgent;
	}

	public String getIp() {
		return ip;
	}

	public synchronized void setDataGenerator(PostDataGenerator postDataGenerator) {
		if (null == this.dataGenerator) {
			this.dataGenerator = postDataGenerator;
		}
	}

	public void post(CharSequence data) throws Exception {
		CliCommandExecution execution = getModule().new WgetCommand().bindAddress(ip).withUserAgent(userAgent).doNotDownloadAnything()
				.withHeaders(headers).post(data).error("failed");
		execution.execute();
		response = execution.getResult();
	}

	public void silentlyPost(CharSequence data) throws Exception {
		CliCommandExecution execution = getModule().new WgetCommand().bindAddress(ip).withUserAgent(userAgent).doNotDownloadAnything()
				.withHeaders(headers).post(data).withTimeout(30, TimeUnit.SECONDS).error("failed").silently();
		execution.execute();
		response = execution.getResult();
	}

	public Runnable silentlyPostLater(final CharSequence data) {
		return new Runnable() {
			@Override
			public void run() {
				try {
					synchronized (this) {
						silentlyPost(data);
					}
				} catch (Exception e) {

				}

			}
		};

	}

	public void postFile(String remoteFile) throws Exception {
		CliCommandExecution execution = getModule().new WgetCommand().bindAddress(ip).withUserAgent(userAgent).doNotDownloadAnything()
				.withHeaders(headers).postFile(remoteFile).error("failed");
		execution.execute();
		response = execution.getResult();
	}

	public void post(byte[] data, String remoteDir, String remoteFile) throws Exception {
		OutputStream put = ((LinuxDefaultCliConnection) getModule().getCliConnectionImpl())
				.put(remoteDir, remoteFile, null, data.length);
		IOUtils.copy(new ByteArrayInputStream(data), put);
		IOUtils.closeQuietly(put);

		postFile(remoteDir + "/" + remoteFile);
	}

	public void bindAddress() throws Exception {
		if (!isEmpty(ip))
			getModule().new AddIpCommand(ip).execute();
	}

	public void unbindAddress() throws Exception {
		if (!isEmpty(ip))
			getModule().new DeleteIpCommand(ip).execute();
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((ip == null) ? 0 : ip.hashCode());
		result = prime * result + ((userAgent == null) ? 0 : userAgent.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		WgetClient other = (WgetClient) obj;
		if (ip == null) {
			if (other.ip != null)
				return false;
		} else if (!ip.equals(other.ip))
			return false;
		if (userAgent == null) {
			if (other.userAgent != null)
				return false;
		} else if (!userAgent.equals(other.userAgent))
			return false;
		return true;
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("WgetClient [ip=");
		builder.append(ip);
		builder.append(", userAgent=");
		builder.append(userAgent);
		builder.append("]");
		return builder.toString();
	}

	@Override
	public String call() throws Exception {
		WgetCommand wgetCommand = getModule().new WgetCommand();
		wgetCommand.bindAddress(ip).withUserAgent(userAgent).withHeaders(headers).doNotDownloadAnything()
				.post(this.dataGenerator.generateData(this));
		wgetCommand.error("failed").silently().execute();
		String httpRequestSentMessage = "HTTP request sent, awaiting response... ";
		wgetCommand.mustHaveResponse(httpRequestSentMessage);
		BufferedReader responseReader = new BufferedReader(new StringReader(wgetCommand.getResult()));
		String resultLine;

		while (null != (resultLine = responseReader.readLine())) {
			if (resultLine.startsWith(httpRequestSentMessage)) {
				return StringUtils.getFirstSubStringSuffix(resultLine, httpRequestSentMessage);
			}
		}
		return null;
	}

	public synchronized String getResponse() {
		return response;
	}

	public WgetClient addHeader(String httpHeader) {
		this.headers.add(httpHeader);
		return this;
	}

	public WgetClient noHeaders() {
		this.headers.clear();
		return this;
	}

	public void setMaxIdleTime(long maxIdleTimeMillies) {
		getModule().getCliConnectionImpl().setMaxIdleTime(maxIdleTimeMillies);
	}

	public void activateIdleMonitor() {
		getModule().setName("wget" + (isEmpty(ip) ? " from " + ip : "") + " as " + userAgent);
		getModule().getCliConnectionImpl().activateIdleMonitor();
	}

	public void deactivateIdleMonitor() {
		getModule().getCliConnectionImpl().deactivateIdleMonitor();
	}

	public WgetModule getModule() {
		return module;
	}

	public boolean idleMonitorIsActive() {
		return module.getCliConnectionImpl().idleMonitorIsActive();
	}

}
