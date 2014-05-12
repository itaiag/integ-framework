package il.co.topq.integframework.db;

import il.co.topq.integframework.reporting.Reporter;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.testng.ITestResult;

public class CSVResultSetPrinter implements ResultSetPrinter {

	public static final int INITIAL_STRING_SIZE = 128;


	private char separator;

	private char quotechar;

	private char escapechar;

	private String lineEnd;

	private String nullString = "~NULL~";

	/** The character used for escaping quotes. */
	public static final char DEFAULT_ESCAPE_CHARACTER = '"';

	/** The default separator to use if none is supplied to the constructor. */
	public static final char DEFAULT_SEPARATOR = ',';

	/**
	 * The default quote character to use if none is supplied to the
	 * constructor.
	 */
	public static final char DEFAULT_QUOTE_CHARACTER = '"';

	/** The quote constant to use when you wish to suppress all quoting. */
	public static final char NO_QUOTE_CHARACTER = '\u0000';

	/** The escape constant to use when you wish to suppress all escaping. */
	public static final char NO_ESCAPE_CHARACTER = '\u0000';

	/** Default line terminator uses platform encoding. */
	public static final String DEFAULT_LINE_END = "\n";

	@Override
	public void print(List<Map<String, Object>> resultList) {

		File file = null;
		try {
			file = File.createTempFile("resultsTable", ".csv");
			file.deleteOnExit();

			try {
				Writer writer = new FileWriter(file);

				for (Map<String, Object> map : resultList) {
					Set<String> fields = map.keySet();
					List<String> nextLine = new ArrayList<String>(fields.size());

					for (String field : fields) {
						Object value = map.get(field);
						if (value == null) {
							value = nullString;
						}
						nextLine.add(value.toString());
					}
					writeNext(nextLine, writer);
				}
				writer.close();
			} catch (IOException exception) {
				throw exception;
			}

		} catch (IOException e) {
			Reporter.log("failed to print resultset", e);
		}
		if (file != null && file.exists()) {
			Reporter.logFile("SQL Table", file);
		} else {
			Reporter.log("failed to print resultset", ITestResult.SUCCESS_PERCENTAGE_FAILURE);
		}
	}

	protected final void writeNext(List<String> nextLine, Writer pw) throws IOException {

		if (nextLine == null)
			return;

		StringBuilder sb = new StringBuilder(100);
		for (int i = 0; i < nextLine.size(); i++) {

			if (i != 0) {
				sb.append(separator);
			}

			String nextElement = nextLine.get(i);
			if (nextElement == null)
				continue;
			if (quotechar != NO_QUOTE_CHARACTER)
				sb.append(quotechar);

			sb.append(stringContainsSpecialCharacters(nextElement) ? processLine(nextElement) : nextElement);

			if (quotechar != NO_QUOTE_CHARACTER)
				sb.append(quotechar);
		}

		sb.append(lineEnd);
		pw.write(sb.toString());

	}

	private boolean stringContainsSpecialCharacters(String line) {
		return line.indexOf(quotechar) != -1 || line.indexOf(escapechar) != -1;
	}

	private StringBuilder processLine(String nextElement) {
		StringBuilder sb = new StringBuilder(INITIAL_STRING_SIZE);
		for (int j = 0; j < nextElement.length(); j++) {
			char nextChar = nextElement.charAt(j);
			if (escapechar != NO_ESCAPE_CHARACTER && nextChar == quotechar) {
				sb.append(escapechar).append(nextChar);
			} else if (escapechar != NO_ESCAPE_CHARACTER && nextChar == escapechar) {
				sb.append(escapechar).append(nextChar);
			} else {
				sb.append(nextChar);
			}
		}
		return sb;
	}

	public void setSeparator(char separator) {
		this.separator = separator;
	}

	public void setQuotechar(char quotechar) {
		this.quotechar = quotechar;
	}

	public void setEscapechar(char escapechar) {
		this.escapechar = escapechar;
	}

	public void setLineEnd(String lineEnd) {
		this.lineEnd = lineEnd;
	}

	public void setNullString(String nullString) {
		this.nullString = nullString;
	}
}