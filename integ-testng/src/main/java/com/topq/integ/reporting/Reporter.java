package com.topq.integ.reporting;

import java.io.File;
import java.io.IOException;
import java.util.Random;

import org.apache.commons.io.FileUtils;

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
		REGULAR("r"), BOLD("b"), ITALIC("i");

		private final String value;

		private Style(String value) {
			this.value = value;
		}

	}

	public enum Color {
		RED, BLUE, YELLOW, GREEN
	}

	public void logEscapeHtml(String s) {
		setEscapeHtml(true);
		log(s);
		setEscapeHtml(false);

	}

	/**
	 * Appending <code>s</code> to the report
	 * 
	 * @param s
	 */
	public static void log(String s) {
		log(s, false);
	}

	/**
	 * Appending <code>s</code> to the report
	 * 
	 * @param s
	 */
	public static void log(String s, boolean logToStandardOut) {
		s = s + "\n";
		org.testng.Reporter.log(toHtml(s), false);
		if (logToStandardOut) {
			System.out.println(s);
		}
	}

	public static void log(final String s, Style style) {
		if (null != style) {
			System.out.println(s);
			log(appendStyleParagraph(s, style), false);
		} else {
			log(s);
		}

	}

	private static String appendStyleParagraph(String s, Style style) {
		final StringBuilder sb = new StringBuilder();
		sb.append("<p>");
		sb.append("<").append(style.value).append(">");
		sb.append(s);
		sb.append("</").append(style.value).append(">");
		sb.append("</p>");
		return sb.toString();
	}

	private static String appendColorParagraph(String s, Color color) {
		if (color == null) {
			return s;
		}
		final StringBuilder sb = new StringBuilder();
		sb.append("<p style='color:").append(color.name()).append("'>");
		sb.append(s);
		sb.append("</p>");
		return sb.toString();

	}

	public static void log(final String s, Style style, Color color) {
		String newS = s;
		if (null == style) {
			style = Style.REGULAR;
		}
		if (null != color) {
			newS = appendColorParagraph(newS, color);
		}
		if (style != Style.REGULAR) {
			newS = appendStyleParagraph(newS, style);
		}
		if (style != Style.REGULAR || color != null) {
			log(newS, false);
			System.out.println(s);
		} else {
			log(s);
		}
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
		log(title, body, null);
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
	public static void log(String title, String body, Color color) {
		if (null == title) {
			title = "title";
		}
		System.out.println(title + "\n");
		if (body != null) {
			System.out.println(body + "\n");
		}
		if (null == body || body.isEmpty()) {
			title = appendColorParagraph(title, color);
			log(title);
			return;
		}
		startLogToggle(title, color);
		log(body);
		stopLogToggle();

	}

	public static void startLogToggle(String title) {
		if (inToggle) {
			return;
		}
		inToggle = true;
		if (null == title) {
			title = "link";
		}
		StringBuilder toggleElement = new StringBuilder();
		final long id = System.currentTimeMillis() + new Random().nextInt(10000);

		// Creating link
		toggleElement.append(" <a href=\"javascript:toggleElement('");
		toggleElement.append(id);
		toggleElement.append("', 'block')\" title=\"Click to expand/collapse\"><b>");
		toggleElement.append(title).append("</b></a><br>");

		// Creating body
		toggleElement.append("<div class='stackTrace' id='");
		toggleElement.append(id);
		toggleElement.append("' style='display: none;'>");
		log(toggleElement.toString(), false);
	}

	public static void startLogToggle(String title, Color color) {
		startLogToggle(appendColorParagraph(title, color));
	}

	public static void stopLogToggle() {
		if (!inToggle) {
			return;
		}
		log("</div>", false);
		inToggle = false;
	}

	private static String toHtml(String str) {
		return str.replace("\n", "<br/>");
	}

	public static void logImage(String title, final File file) {
		File newFile = copyFileToReporterFolder(file);
		if (null == newFile) {
			return;
		}
		if (null == title) {
			title = file.getName();
		}
		log("<img src='" + newFile.getName() + "' alt='" + title + "' >");

	}

	private static File copyFileToReporterFolder(File file) {
		if (null == file || !file.exists() || !file.isFile()) {
			// File is not exist
			return null;
		}

		// Creating parent folder
		final File parentFolder = new File(
				new File(getCurrentTestResult().getTestContext().getOutputDirectory()).getParent() + File.separator
						+ "html");
		if (!parentFolder.exists()) {
			if (!parentFolder.mkdirs()) {
				log("Failed to create folder for logging file");
			}
		}

		// Copying the file to the parent folder
		final File newFile = new File(parentFolder, file.getName());
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

}