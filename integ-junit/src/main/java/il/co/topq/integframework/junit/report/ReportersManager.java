package il.co.topq.integframework.junit.report;

import il.co.topq.integframework.junit.report.ReporterI.Status;
import il.co.topq.integframework.junit.runner.RunListenerManager;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.junit.runner.notification.RunListener;

public class ReportersManager {

	private static ReportersManager instance;

	private static SimpleDateFormat timeStampFormatter = new SimpleDateFormat("HH:mm:ss: ");

	private List<ReporterI> reporters;

//	private String[] reportersClasses;

	private ReportersManager() {
	}

	public void addReporter(ReporterI reporter) {
		if (null == reporters) {
			reporters = new ArrayList<>();
		}
		reporters.add(reporter);
		if (reporter instanceof RunListener) {
			RunListenerManager.getInstance().addRunListener((RunListener) reporter);
		}

	}

//	@PostConstruct
//	public void createReportersList() {
//		if (reportersClasses == null) {
//			return;
//		}
//		for (String className : reportersClasses) {
//			try {
//				Class<?> clazz = Class.forName(className);
//				if (Reporter.class.isAssignableFrom(clazz)) {
//					addReporter((Reporter) clazz.newInstance());
//
//				} else {
//					System.err.println(className + " is not a reporter");
//				}
//			} catch (Exception e) {
//				System.err.println("Failed to create instance of class " + className);
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			}
//
//		}
//	}

	public synchronized static ReportersManager getInstance() {
		if (null == instance) {
			instance = new ReportersManager();
		}
		return instance;
	}

	public void report(String title, String message, Status status) {
		switch (status) {
			case SUCCESS:

				break;
			case FAILURE:
				break;
			default:
				break;
		}
		if (reporters != null) {
			title = timeStampFormatter.format(new Date()) + title;
			for (ReporterI reporter : reporters) {
				reporter.report(title, message, status);
			}
		}

	}

	public void report(String title, Status status) {
		report(title, null, status);
	}

	public void report(String message) {
		report(message, Status.SUCCESS);
	}

//	public List<Reporter> getReporters() {
//		return reporters;
//	}
//
//	public void setReporters(List<Reporter> reporters) {
//		this.reporters = reporters;
//	}

//	public String[] getReportersClasses() {
//		return reportersClasses;
//	}
//
//	public void setReportersClasses(String[] reportersClasses) {
//		this.reportersClasses = reportersClasses;
//	}

}
