package il.co.topq.integframework.junit.configuration;

public enum FrameworkOptions {

	REPORTER_CLASSES("reporter.classes", "com.rsa.fa.blackbox.integration.infra.report.StdoutReporter");

	private final String key;

	private final String defaultValue;

	private FrameworkOptions(String key, String defaultValue) {
		this.key = key;
		this.defaultValue = defaultValue;
	}

	public String getKey() {
		return key;
	}

	public String getDefaultValue() {
		return defaultValue;
	}

}
