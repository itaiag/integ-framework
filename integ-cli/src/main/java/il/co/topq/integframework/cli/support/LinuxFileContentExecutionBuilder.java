package il.co.topq.integframework.cli.support;

import il.co.topq.integframework.assertion.Assert;
import il.co.topq.integframework.cli.conn.CliConnection;
import il.co.topq.integframework.cli.process.CliCommandExecution;

public class LinuxFileContentExecutionBuilder {
	String name = "";
	private final CliConnection cliConnection;
	private boolean fromHead = false;
	private int maxLines = 10;

	public LinuxFileContentExecutionBuilder(CliConnection cliConnection, String fileName) {

		this.cliConnection = cliConnection;
		this.name = fileName;
	}

	LinuxFileContentExecutionBuilder named(String name) {
		this.name = name;
		return this;
	}

	public CliCommandExecution build() {
		if (name.length() == 0)
			throw new IllegalStateException("file name is not set");
		StringBuilder command = new StringBuilder();
		if (fromHead) {
			command.append("head ");
		} else {
			command.append("tail ");
		}
		command.append("-n ").append(maxLines).append(" ");
		command.append(name);

		return new CliCommandExecution(cliConnection, command.toString());
	}

	public CliExecutionExpectedCondition<String> containingText(final String expected) {

		return new CliExecutionExpectedCondition<String>() {

			@Override
			public String apply(CliCommandExecution input) {
				try {
					input.mustHaveResponse(expected).execute();
				} catch (Exception e) {
					Assert.fail("execution failed", e);
				}
				return input.getResult();
			}
		};

	}

	public CliExecutionExpectedCondition<String> notContainingText(final String expected) {

		return new CliExecutionExpectedCondition<String>() {

			@Override
			public String apply(CliCommandExecution input) {
				try {
					input.error(expected).execute();
				} catch (Exception e) {
					Assert.fail("execution failed", e);
				}
				return input.getResult();
			}
		};

	}
}
