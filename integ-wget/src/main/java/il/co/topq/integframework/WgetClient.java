package il.co.topq.integframework;

import static il.co.topq.integframework.utils.StringUtils.isEmpty;
import il.co.topq.integframework.cli.conn.LinuxDefaultCliConnection;
import il.co.topq.integframework.cli.process.CliCommandExecution;
import il.co.topq.integframework.utils.StringUtils;

import java.io.ByteArrayInputStream;
import java.io.OutputStream;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.concurrent.*;

import org.apache.commons.io.IOUtils;

//import com.google.common.base.Stopwatch;
import com.google.common.collect.Sets;
import com.google.common.util.concurrent.ThreadFactoryBuilder;

public class WgetClient {
	private volatile ExecutorService dispatcher;
	private volatile ExecutorService sender = null;
	final String userAgent, ip, connectionIP;
	final Set<String> headers = Sets.newConcurrentHashSet();
	private final WgetModule module;
	private PostDataGenerator dataGenerator;

	private volatile String response;

	public WgetClient(WgetModule module, String ip, String userAgent) {
		this.module = module;
		this.ip = ip;
		this.userAgent = StringUtils.either(userAgent).or("Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.17 (KHTML, like Gecko) Chrome/24.0.1312.56 Safari/537.17");
		String connectionIP;
		try {
			connectionIP = InetAddress.getByName(module.getCliConnectionImpl().getHost()).getHostAddress();
		} catch (UnknownHostException e) {
			connectionIP = "0.0.0.0";
		}
		this.connectionIP = connectionIP;
		module.setName("Wget client");

	}

	public String getUserAgent() {
		return userAgent;
	}

	public String getIp() {
		return ip;
	}

	public String getConnectionIp() {
		return connectionIP;
	}

	public void setDataGenerator(PostDataGenerator postDataGenerator) {
		if (null == this.dataGenerator) {
			this.dataGenerator = postDataGenerator;
		} else {
			synchronized (this) {

				this.dataGenerator = postDataGenerator;
			}
		}
	}

	public String post(CharSequence data) throws Exception {
		CliCommandExecution execution = getModule().new WgetCommand().bindAddress(ip).withUserAgent(userAgent).doNotDownloadAnything().withHeaders(headers).post(data).error("failed");
		execution.execute();
		return response = execution.getResult();
	}

	public String silentlyPost(CharSequence data) throws Exception {
		CliCommandExecution execution = getModule().new WgetCommand().bindAddress(ip).withUserAgent(userAgent).doNotDownloadAnything().withHeaders(headers).post(data).withTimeout(30, TimeUnit.SECONDS).error("failed").silently();
		execution.execute();
		return response = execution.getResult();
	}

	public Runnable silentlyPostLater(final CharSequence data) {
		final WgetClient parent = this;
		return new Runnable() {
			@Override
			public boolean equals(Object obj) {
				return getParent().equals(parent);
			};

			private WgetClient getParent() {
				return parent;
			}

			@Override
			public int hashCode() {
				return parent.hashCode();
			};

			@Override
			public String toString() {
				return "Posting " + data + " on " + parent.toString();
			};

			@Override
			public void run() {
				synchronized (parent.module.getCliConnectionImpl()) {
					try {
						silentlyPost(data);
					} catch (Exception e) {
						throw new RuntimeException("Silently post on " + this.toString() + " Failed!", e);
					}
				}
			}
		};

	}

	public Callable<String> postLater(final CharSequence data) {
		final WgetClient parent = this;
		return new Callable<String>() {
			@Override
			public boolean equals(Object obj) {
				return getParent().equals(parent);
			};

			private WgetClient getParent() {
				return parent;
			}

			@Override
			public int hashCode() {
				return parent.hashCode();
			};

			@Override
			public String toString() {
				return "Posting " + data + " on " + parent.toString();
			};

			@Override
			public String call() throws Exception {
				synchronized (parent) {
					post(data);
					return parent.getModule().getActual(String.class);
				}
			}
		};

	}

	public Callable<String> postLaterSilently(final CharSequence data) {
		final WgetClient parent = this;
		return new Callable<String>() {
			@Override
			public boolean equals(Object obj) {
				return getParent().equals(parent);
			};

			private WgetClient getParent() {
				return parent;
			}

			@Override
			public int hashCode() {
				return parent.hashCode();
			};

			@Override
			public String toString() {
				return "Posting " + data + " on " + parent.toString();
			};

			@Override
			public String call() throws Exception {
				synchronized (parent) {
					silentlyPost(data);
					return response;
				}
			}
		};

	}

	public void postFile(String remoteFile) throws Exception {
		CliCommandExecution execution = getModule().new WgetCommand().bindAddress(ip).withUserAgent(userAgent).doNotDownloadAnything().withHeaders(headers).postFile(remoteFile).error("failed");
		execution.execute();
		response = execution.getResult();
	}

	public void post(byte[] data, String remoteDir, String remoteFile) throws Exception {
		OutputStream put = ((LinuxDefaultCliConnection) getModule().getCliConnectionImpl()).put(remoteDir, remoteFile, null, data.length);
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

	public synchronized void resetDispatcher() throws InterruptedException {
		if (dispatcher != null) {
			dispatcher.shutdown();
			if (!dispatcher.awaitTermination(5, TimeUnit.MINUTES)) {
				throw new IllegalStateException("Dispatcher failed to stop within 5 minutes");
			}
		}
		dispatcher = Executors.newSingleThreadExecutor(new ThreadFactoryBuilder().setNameFormat("Wget dispatcher @" + StringUtils.either(ip).or(this.module.getCliConnectionImpl().getName())).build());
	}

	public synchronized Future<List<Future<String>>> start() {
		final WgetClient client = this;
		if (sender != null) {
			if (sender.isShutdown()) {
				if (sender.isTerminated()) {
					sender = null;
				} else {
					try {
						if (!sender.awaitTermination(5, TimeUnit.MINUTES)) {
							throw new IllegalStateException("Wget client sender did not finish yet!");
						}
					} catch (InterruptedException e) {
						throw new IllegalStateException("Wget client sender did not finish yet!", e);
					}
					sender = null;
				}
			}
		}
		try {
			return dispatcher.submit(new Callable<List<Future<String>>>() {
				@Override
				public List<Future<String>> call() throws InterruptedException {
					final List<Future<String>> result = new ArrayList<>(dataGenerator.getCapacity());
					synchronized (client) {
						if (sender == null) {
							sender = Executors.newSingleThreadExecutor(new ThreadFactoryBuilder().setNameFormat("Sender for " + StringUtils.either(ip).or(client.getModule().getName())).build());
						}
					}
					while (!dataGenerator.isDone()) {
						String postData = dataGenerator.getPostData();
						if (postData != null) {
							result.add(sender.submit(postLaterSilently(postData)));
						} else {
							continue;
						}
					}
					sender.shutdown();
					return result;
				}
			});
		} finally {
			dispatcher.shutdown();
		}
		// WgetCommand wgetCommand = getModule().new WgetCommand();
		// wgetCommand.bindAddress(ip).withUserAgent(userAgent).withHeaders(headers).doNotDownloadAnything().post(this.dataGenerator.generateData(this));
		// wgetCommand.error("failed").silently().execute();
		// String httpRequestSentMessage =
		// "HTTP request sent, awaiting response... ";
		// wgetCommand.mustHaveResponse(httpRequestSentMessage);
		// BufferedReader responseReader = new BufferedReader(new
		// StringReader(wgetCommand.getResult()));
		// String resultLine;
		//
		// while (null != (resultLine = responseReader.readLine())) {
		// if (resultLine.startsWith(httpRequestSentMessage)) {
		// return StringUtils.getFirstSubStringSuffix(resultLine,
		// httpRequestSentMessage);
		// }
		// }
		// return null;
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

	public PostDataGenerator getPostDataGenerator() {
		return dataGenerator;
	}

}
