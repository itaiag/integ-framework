package il.co.topq.integframework.junit.configuration;

import il.co.topq.integframework.junit.report.ReporterI;
import il.co.topq.integframework.junit.report.ReportersManager;
import il.co.topq.integframework.junit.utils.BeanUtils;

public class Configurator {

	private static Configurator instance;

	public static Configurator getInstance() {
		if (null == instance) {
			instance = new Configurator();
		}
		return instance;
	}

	private Configurator() {
		configureSystem();
	}

	private void configureSystem() {
		final String reporterClasses = IntegrationProperites.getInstance().getOptionValue(FrameworkOptions.REPORTER_CLASSES);
		if (reporterClasses != null) {
			for (String reportClass : reporterClasses.split(";")) {
				Object reporter = BeanUtils.createInstanceFromString(reportClass, ReporterI.class);
				if (reporter != null) {
					ReportersManager.getInstance().addReporter((ReporterI) reporter);
				}
			}
		}
	}

}
