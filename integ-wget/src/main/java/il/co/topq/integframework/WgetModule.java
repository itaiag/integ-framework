package il.co.topq.integframework;

import il.co.topq.integframework.cli.process.CliCommandExecution;
import il.co.topq.integframework.cli.process.LinuxCommandLineModule;
import il.co.topq.integframework.utils.StringUtils;

import java.io.IOException;
import java.net.URL;

import org.testng.Assert;

public class WgetModule extends LinuxCommandLineModule {

	String url;

	@Override
	public void init() throws Exception {

		super.init();
		Assert.assertNotNull(cliConnectionImpl, "cli property not set");
		Assert.assertNotSame(url, "", "url property not set");
	}
	public class AddIpCommand extends CliCommandExecution {
		public AddIpCommand(String ipAddress) {
			super(linux, "sudo ip -4 addr add " + ipAddress + "/32 dev lo scope host");
			withTitle("add ip address");
		}
	}

	public class DeleteIpCommand extends CliCommandExecution {
		public DeleteIpCommand(String ipAddress) {
			super(linux, "sudo ip -4 addr del " + ipAddress + "/32 dev lo scope host");
			withTitle("remove ip address");
		}
	}

	public class WgetCommand extends CliCommandExecution {
		private final StringBuilder command = new StringBuilder("wget");
		private boolean userAgentSet = false, downloadSet = false, bindAddressSet = false, referrerSet = false,
				postDataSet = false,
				alreadyRun = false;
		public WgetCommand() {
			super(linux);
			withTitle("wget");
		}

		private boolean setFlag(boolean flag, String exceptionString) {
			if (flag) {
				throw new IllegalStateException(exceptionString);
			}
			return true;
		}

		public WgetCommand withUserAgent(String userAgent) {
			userAgentSet = setFlag(userAgentSet, "User agent" + " already set!");
			command.append(" --user-agent='").append(userAgent).append("' ");
			return this;
		}

		public WgetCommand doNotDownloadAnything() {
			return downloadTo("/dev/null");
		}

		public WgetCommand downloadTo(String target) {
			downloadSet = setFlag(downloadSet, "Download target" + " already set!");
			command.append("-O ").append(target);
			return this;
		}

		public WgetCommand bindAddress(String address) {
			if (StringUtils.isEmpty(address))
				return this;
			bindAddressSet = setFlag(bindAddressSet, "Bind address" + " already set!");
			command.append(" --bind-address=").append(address);
			return this;
		}

		public WgetCommand referer(URL url) {
			referrerSet = setFlag(referrerSet, "Referrer" + " already set!");
			command.append(" --referer='").append(url).append("' ");
			return this;
		}

		public WgetCommand post(CharSequence data) {
			postDataSet = setFlag(postDataSet, "Post data" + " already set!");
			command.append(" --post-data=\'").append(data).append('\'');
			return this;
		}

		public WgetCommand postFile(String name) {
			postDataSet = setFlag(postDataSet, "Post data" + " already set!");
			command.append(" --post-file=\'").append(name).append('\'');
			return this;
		}

		public WgetCommand withHeader(String header) {
			// e.g: --header='Accept-Charset: iso-8859-2'
			command.append(" --header=\'").append(header).append('\'');
			return this;
		}
		@Override
		public void execute() throws IOException {
			alreadyRun = setFlag(alreadyRun, "A client can execute only once!");
			if (StringUtils.isEmpty(cmd)) {
				cmd = command.append(' ').append(url).toString();
			}
			super.execute();
		}

		public WgetCommand withHeaders(Iterable<String> headers) {
			for (String header : headers) {
				withHeader(header);
			}
			return this;
		}

	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

}
