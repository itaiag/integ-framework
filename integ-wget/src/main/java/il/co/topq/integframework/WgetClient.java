package il.co.topq.integframework;


public class WgetClient {
	final String userAgent, ip;

	private final WgetModule module;


	public WgetClient(WgetModule module, String ip, String userAgent) {
		this.module = module;
		this.ip = ip;
		this.userAgent = userAgent;
	}

	public String getUserAgent() {
		return userAgent;
	}

	public String getIp() {
		return ip;
	}

	public void post(CharSequence data) throws Exception {
		module.new WgetCommand().bindAddress(ip).withUserAgent(userAgent).doNotDownloadAnything().post(data).error("failed")
				.execute();
	}

	public void bindAddress() throws Exception {
		module.new AddIpCommand(ip).execute();
	}

	public void unbindAddress() throws Exception {
		module.new DeleteIpCommand(ip).execute();
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

}
