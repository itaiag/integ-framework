package il.co.topq.integframework.cli.support;

import il.co.topq.integframework.assertion.Assert;
import il.co.topq.integframework.assertion.CompareMethod;
import il.co.topq.integframework.assertion.LongCompareAssertion;
import il.co.topq.integframework.cli.conn.CliConnection;
import il.co.topq.integframework.cli.process.CliCommandExecution;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class LinuxFileInfoExecutionBuilder {

	String name = "";
	private final CliConnection cliConnection;

	private ResultType resultType = ResultType.FileSize;

	public LinuxFileInfoExecutionBuilder(CliConnection cliConnection, String fileName) {
		super();
		this.cliConnection = cliConnection;
		this.name = fileName;
	}

	LinuxFileInfoExecutionBuilder named(String name) {
		this.name = name;
		return this;
	}

	public CliCommandExecution build() {
		if (name.length() == 0)
			throw new IllegalStateException("file name is not set");
		return new CliCommandExecution(cliConnection, "ls -l --time-style=full-iso " + this.name
				+ " | grep -v '^total' | awk {'print " + resultType.awkPrint + "'}").error("No such file or directory").error(
				"cannot access");
	}

	public LinuxFileInfoExecutionBuilder getFileSize() {
		this.resultType = ResultType.FileSize;
		return this;
	}

	public LinuxFileInfoExecutionBuilder getFileDate() {
		this.resultType = ResultType.FileSize;
		return this;
	}

	public CliExecutionExpectedCondition<Long> fileSizeToBe(CompareMethod compareMethod, long expectedSize) {
		getFileSize();
		return execute(resultType, expectedSize, compareMethod);
	}

	private static CliExecutionExpectedCondition<Long> execute(final ResultType resultType, final long expected,
			final CompareMethod compareMethod) {
		return new CliExecutionExpectedCondition<Long>() {

			@Override
			public Long apply(CliCommandExecution input) {
				try {
					long result;
					input.execute();
					LongCompareAssertion compareAssertion = new LongCompareAssertion(expected, compareMethod);
					Assert.assertLogicHappens(result = resultType.parser.parse(input.getResult()), compareAssertion, 0l, true);
					return result;
				} catch (Exception e) {
					throw new RuntimeException(e);
				}

			}
		};
	}

	public enum ResultType {
		FileDate("$6,$7,$8", new Parser<Long>() {
			private final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS Z");

			@Override
			public Long parse(String s) throws ParseException {
				Date actual = dateFormat.parse(s);
				return actual.getTime();
			}

		}),

		FileSize("$5", new Parser<Long>() {
			@Override
			public Long parse(String s) {
				long actual = Long.parseLong(s);
				return actual;
			}
		});

		final String awkPrint;
		final Parser<?> parser;

		ResultType(final String awkPrint, final Parser<?> parser) {
			this.awkPrint = awkPrint;
			this.parser = parser;
		}

		private interface Parser<T extends Comparable<T>> {

			Long parse(String result) throws ParseException;
		}
	}

}
