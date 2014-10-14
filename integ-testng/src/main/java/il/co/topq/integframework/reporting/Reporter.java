package il.co.topq.integframework.reporting;

import il.co.topq.integframework.utils.RandomUtils;
import il.co.topq.integframework.utils.StringUtils;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;
import java.util.Set;

import org.apache.commons.io.FileUtils;
import org.testng.ITestResult;

import com.google.common.collect.Sets;

/**
 * Wrapper for the TestNG HTML report
 * 
 * @author Itai Agmon
 * 
 */
public class Reporter extends org.testng.Reporter {
	private static boolean inToggle;

	static {
		// Allowing adding HTML to the report
		System.setProperty("org.uncommons.reportng.escape-output", "false");
		setEscapeHtml(false);
	}

	public enum Style {
		REGULAR(""), BOLD("b"), ITALIC("i"), PLAINTEXT("pre"), EMPHASIZED("em"), STRIKETHROUGH("strike"), CENTER("center");

		private final String value;

		private Style(String value) {
			this.value = value;
		}

	}

	public enum Color {
		RED, BLUE, YELLOW {
			@Override
			public String toString() {
				return "gold";
			}
		},
		GREEN
	}

	public void logEscapeHtml(String s) {
		setEscapeHtml(true);
		log(s);
		setEscapeHtml(false);

	}

	public static void log(String s) {
		log(s, false, null, null);
	}

	public static void log(String s, boolean logToStandardOut) {
		log(s, logToStandardOut, null, null);
	}

	public static void log(final String s, Style style) {
		log(s, false, style, null);
	}

	public static void log(final String s, Color color) {
		log(s, false, null, color);
	}

	public static void log(final String s, Style style, Color color) {
		log(s, false, style, color);
	}

	public static void step(String step) {
		log(step, Style.EMPHASIZED, Color.BLUE);

	}

	private static Set<String> packagesToFilter = Sets.newHashSet();

	public static void filterPackagesOnThrowables(String packageName) {
		if (StringUtils.isEmpty(packageName)) {
			packagesToFilter = Sets.newHashSet();
		} else {
			packagesToFilter.add(packageName);
		}
	}

	public static void filterPackagesOnThrowables(Set<String> packages) {
		for (String packageName : packages) {
			filterPackagesOnThrowables(packageName);
			if (StringUtils.isEmpty(packageName)) {
				return;
			}
		}
	}

	/**
	 * report an error or exception
	 * 
	 * @param t
	 *            the {@link Throwable} to be reported
	 */
	public static void log(final Throwable t) {
		log(t.getMessage(), t);
	}

	/**
	 * report an error or exception
	 * 
	 * @param t
	 * @param status
	 *            a status to set.<br>
	 *            shuold be either {@link ITestResult#SUCCESS},
	 *            {@link ITestResult#FAILURE} or
	 *            {@link ITestResult#SUCCESS_PERCENTAGE_FAILURE}<br>
	 *            default is {@link ITestResult#FAILURE}
	 * 
	 */
	public static void log(final Throwable t, int status) {
		log(t.getMessage(), t, status);
	}

	/**
	 * report an error or exception
	 * 
	 * @param title
	 *            the title of the link to put
	 * @param t
	 *            the {@link Throwable} to be reported
	 */
	public static void log(final String title, final Throwable t) {
		log(title, t, packagesToFilter);
	}

	/**
	 * report an error or exception
	 * 
	 * @param title
	 *            the title of the link to put
	 * @param t
	 *            the {@link Throwable} to be reported
	 * @param status
	 *            a status to set.<br>
	 *            shuold be either {@link ITestResult#SUCCESS},
	 *            {@link ITestResult#FAILURE} or
	 *            {@link ITestResult#SUCCESS_PERCENTAGE_FAILURE}<br>
	 *            default is {@link ITestResult#FAILURE}
	 */
	public static void log(final String title, final Throwable t, int status) {
		log(title, t, packagesToFilter, status);
	}

	/**
	 * report an error or exception
	 * 
	 * @param title
	 *            the title of the link to put
	 * @param t
	 *            the {@link Throwable} to be reported
	 * @param packagesToFilter
	 *            a set of packages (as can be gained from
	 *            {@link Class#getPackage()})
	 *            {@link StackTraceElement#getClassName()} to be omitted from
	 *            the reported StackTrace of <i>t</i>
	 */
	public static void log(final Throwable t, final Set<String> packagesToFilter) {
		log(t.getMessage(), t, packagesToFilter);
	}

	public static void log(final String title, final Throwable t, final Set<String> packagesToFilter) {
		log(title, StringUtils.getStackTrace(t, packagesToFilter), false);
	}

	public static void log(final String title, final Throwable t, final Set<String> packagesToFilter, int status) {
		log(title, StringUtils.getStackTrace(t, packagesToFilter), status);
	}

	public static void logToFile(Throwable t) {
		logToFile(t.getMessage(), t);
	}

	public static void logToFile(final String title, Throwable t) {
		logToFile(title, t, packagesToFilter);
	}

	public static void logToFile(final String title, final Throwable t, final Set<String> packagesToFilter) {
		logToFile(title, StringUtils.getStackTrace(t, packagesToFilter), Color.RED);
	}

	protected static DateFormat df = new SimpleDateFormat("HH:mm:ss.SSS");
	private static Random random = new Random(System.currentTimeMillis());

	/**
	 * Appending <code>s</code> to the report with time stamp
	 * 
	 * @param s
	 */
	public static void log(String s, boolean logToStandardOut, Style style, Color color) {
		String reportDate = "";
		if (!s.startsWith("</")) { // No need to add time stamp for close tag
			reportDate = df.format(new Date(System.currentTimeMillis())) + " - ";
			// s = reportDate + s + "\n";
		}
		String newS = s;
		if (null != style && !Style.REGULAR.equals(style)) {
			newS = appendStyleParagraph(newS, style);
		}
		if (null != color) {
			newS = appendColorParagraph(newS, color);
		}
		writeToLog(reportDate + newS + ((StringUtils.isEmpty(reportDate)) ? "" : "\n"), logToStandardOut);
	}

	/**
	 * Adds toggle element to the report
	 * 
	 * @param title
	 *            Will appear as link. If none given the link will appear with
	 *            the test 'link'
	 * @param body
	 *            Will appear when clicking on the title.
	 */
	public static void log(String title, String body) {
		log(title, body, Style.REGULAR);
	}

	/**
	 * Adds toggle element to the report
	 * 
	 * @param title
	 *            Will appear as link. If none given the link will appear with
	 *            the test 'link'
	 * @param body
	 *            Will appear when clicking on the title.
	 * @param style
	 *            The {@link Style} to style the body
	 */
	public static void log(String title, String body, Style style) {
		if (null == title) {
			title = "title";
		}
		// if i want to print to output stream, is should really use
		// log(title, true);
		// System.out.println(title + "\n");
		// if (body != null) {
		// System.out.println(body + "\n");
		// }
		if (null == body || body.isEmpty()) {
			log(title, style);
			return;
		}
		startLogToggle(title);
		log(body, style);
		stopLogToggle();

	}

	/**
	 * Adds toggle element to the report
	 * 
	 * @param title
	 *            Will appear as link. If none given the link will appear with
	 *            the test 'link'
	 * @param body
	 *            Will appear when clicking on the title.
	 * @param color
	 *            The color of the link element
	 */
	public synchronized static void log(String title, String body, Color color) {
		if (null == title) {
			title = "title";
		}
		System.out.println(title + "\n");
		if (body != null) {
			System.out.println(body + "\n");
		}
		if (null == body || body.isEmpty()) {
			log(title, color);
			return;
		}
		startLogToggle(title, color);
		log(body);
		stopLogToggle();

	}

	/**
	 * Adds toggle element to the report
	 * 
	 * @param title
	 *            Will appear as link. If none given the link will appear with
	 *            the test 'link'
	 * @param body
	 *            Will appear when clicking on the title.
	 * @param color
	 *            The color of the link element
	 */
	public static void logToFile(String title, String body, Color color) {
		if (null == title) {
			title = "title";
		}
		System.out.println(title + "\n");
		if (body != null) {
			System.out.println(body + "\n");
		}
		if (null == body || body.isEmpty()) {
			log(title, color);
			return;
		}
		fileToggle(title, body);
	}

	public static void log(String title, String body, boolean status) {
		getCurrentTestResult().setStatus(
				(!status || !getCurrentTestResult().isSuccess()) ? ITestResult.FAILURE : getCurrentTestResult().getStatus());
		log(title, body, status ? Color.BLUE : Color.RED);
	}

	/**
	 * report a
	 * 
	 * @param title
	 * @param body
	 * @param status
	 */
	public static void log(String title, String body, int status) {
		getCurrentTestResult().setStatus(status);
		Color statusColor;
		switch (status) {
		case ITestResult.FAILURE:
			statusColor = Color.RED;
			break;
		case ITestResult.SUCCESS_PERCENTAGE_FAILURE:
			statusColor = Color.YELLOW;
			break;
		case ITestResult.SUCCESS:
			statusColor = Color.GREEN;
			break;
		default:
			statusColor = Color.BLUE;
		}
		log(title, body, statusColor);
	}

	public synchronized static void startLogToggle(String title) {
		startLogToggle(title, null);
	}

	public static void fileToggle(String title, String body) {
		String fileTitle = "file";
		if (null != title && !title.contains(",") && !title.contains(" ") && !title.contains("/")) {
			fileTitle = title;
		}
		StringBuilder toggleElement = new StringBuilder();
		final String id = System.currentTimeMillis() + "_" + RandomUtils.getRandomInt(1000, 9999, random);
		File file;
		try {
			file = File.createTempFile(fileTitle, ".partial", parentFolder());// work
																	// directly
																// on report
																// file
			// File newfile = copyFileToReporterFolder(file);
			// file.delete();
			// file = newfile;
			try (FileWriter writer = new FileWriter(file)) {
				writer.write(body);
			}
		} catch (IOException e) {
			log(title, body);
			return;
		}

		// Creating link
		toggleElement.append("<a href='#' onclick=\"toggleElement('");
		toggleElement.append(id);
		toggleElement.append("', 'block');").append("loadExternal('").append(id).append("','").append(file.getName())
				.append("');return false;\" title=\"Click to expand/collapse\">");
		toggleElement.append("<b>").append(title).append("</b></a>");

		// Creating body
		toggleElement.append("<div class='stackTrace' id='");
		toggleElement.append(id);
		toggleElement.append("' style='display: none;' ></div>");
		log(toggleElement.toString(), false, null, null);
	}

	public synchronized static void startLogToggle(String title, Color color) {
		if (inToggle) {
			return;
		}
		inToggle = true;
		if (null == title) {
			title = "link";
		}
		StringBuilder toggleElement = new StringBuilder();
		final String id = System.currentTimeMillis() + "_" + RandomUtils.getRandomInt(1000, 9999, random);

		// Creating link
		toggleElement.append(" <a href=\"javascript:toggleElement('");
		toggleElement.append(id);
		toggleElement.append("', 'block')\" title=\"Click to expand/collapse\"><b>");
		toggleElement.append(appendColorParagraph(title, color)).append("</b></a><br />");

		// Creating body
		toggleElement.append("<div class='stackTrace' id='");
		toggleElement.append(id);
		toggleElement.append("' style='display: none;'>");
		log(toggleElement.toString(), false, null, null);
	}

	public static void stopLogToggle() {
		if (!inToggle) {
			return;
		}
		log("</div>", false);
		inToggle = false;
	}

	public static void logImage(String title, final File file) {
		File newFile = copyFileToReporterFolder(file);
		if (null == newFile) {
			// return;
		}
		if (null == title) {
			title = file.getName();
		}
		log("<img src='" + newFile.getName() + "' alt='" + title + "' >");

	}

	/**
	 * Copy file to the report and add link. If another file is alrady exists in
	 * the reports folder with the same name the old file will be deleted.
	 * 
	 * @param title
	 *            The title to appear as link to the file
	 * @param file
	 *            The file to copy to the report
	 */
	public static void logFile(String title, final File file) {
		File newFile = copyFileToReporterFolder(file);
		if (null == newFile) {
			return;
		}
		// Creating link

		if (null == title || title.isEmpty()) {
			title = file.getName();
		}
		System.out.println(title);
		log("<a href='" + newFile.getName() + "'>" + title + "</a>", false);
	}

	private static void writeToLog(String s, boolean logToStandardOut) {
		org.testng.Reporter.log(toHtml(s), false);
		if (logToStandardOut) {
			System.out.println(s);
		}
	}

	private static String appendStyleParagraph(String s, Style style) {
		final StringBuilder sb = new StringBuilder();
		sb.append("<").append(style.value).append(">");
		if (Style.PLAINTEXT.equals(style)) {
			s = s.replaceAll("<", "&lt;");
			s = s.replaceAll(">", "&gt;");
		}
		sb.append(s);
		sb.append("</").append(style.value).append(">");
		return sb.toString();
	}

	private static String appendColorParagraph(String s, Color color) {
		if (color == null) {
			return s;
		}
		final StringBuilder sb = new StringBuilder();
		sb.append("<span style='color:").append(color.toString()).append("'>");
		sb.append(s);
		sb.append("</span>");
		return sb.toString();
	}

	private static File copyFileToReporterFolder(File file) {
		if (null == file || !file.exists() || !file.isFile()) {
			// File is not exist
			return null;
		}

		// Creating parent folder
		// Copying the file to the parent folder
		final File newFile = new File(parentFolder(), file.getName());
		if (newFile.exists()) {
			newFile.delete();
		}
		try {
			FileUtils.copyFile(file, newFile);
		} catch (IOException e1) {
			log("Failed copying file " + file.getAbsolutePath());
		}
		return newFile;
	}

	private static final File parentFolder() {
		File parentFolder = new File(new File(getCurrentTestResult().getTestContext().getOutputDirectory()).getParent()
				+ File.separator + "html");
		if (!parentFolder.exists()) {
			if (!parentFolder.mkdirs()) {
				log("Failed to create folder for logging file");
			}
		}
		return parentFolder;
	}

	private static String toHtml(String str) {
		return str.contains("<pre>") ? str : str.replace("\n", "<br/>");
	}

}