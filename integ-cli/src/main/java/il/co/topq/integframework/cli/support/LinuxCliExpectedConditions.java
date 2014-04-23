package il.co.topq.integframework.cli.support;

import static il.co.topq.integframework.cli.support.CliExecutionExpectedConditions.executionLogicHappens;
import static il.co.topq.integframework.utils.Parsers.intParser;
import il.co.topq.integframework.assertion.ComparableAssertion;
import il.co.topq.integframework.assertion.CompareMethod;
import il.co.topq.integframework.cli.conn.CliConnection;
import il.co.topq.integframework.cli.conn.LinuxDefaultCliConnection;
import il.co.topq.integframework.cli.process.CliCommandExecution;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Iterator;

import com.google.common.base.Predicate;

public abstract class LinuxCliExpectedConditions {
	@SuppressWarnings("unused")
	private final int _;

	private LinuxCliExpectedConditions() {
		_ = 1;
	}

	/**
	 * an expectation of existence of a file.
	 * 
	 * @param path
	 *            the file path to find
	 * @return true if the file exists
	 */
	public static Predicate<LinuxDefaultCliConnection> fileExists(final String path) {
		return new Predicate<LinuxDefaultCliConnection>() {

			@Override
			public String toString() {
				return "the file in " + path.toString() + " to be openable";
			};

			@Override
			public boolean apply(LinuxDefaultCliConnection cliConnection) {
				try {
					cliConnection.get(path).close();
				} catch (FileNotFoundException e) {
					return false;
				} catch (IllegalArgumentException e) {
					throw e;
				} catch (IOException e) {
					throw new RuntimeException(e);
				}
				return true;
			}
		};
	}

	/**
	 * expectation for a file to be readable.
	 * 
	 * @param path
	 *            the file path to find
	 * @return an input stream to the file
	 */
	public static CliExpectedCondition<InputStream> fileIsReadable(final String path) {
		return new CliExpectedCondition<InputStream>() {

			@Override
			public String toString() {
				return "the file in " + path.toString() + " to be readable";
			};

			public InputStream apply(LinuxDefaultCliConnection cliConnection) {
				try {
					return cliConnection.get(path);
				} catch (FileNotFoundException e) {
					return null;
				} catch (IllegalArgumentException e) {
					throw e;
				} catch (IOException e) {
					throw new RuntimeException(e);
				}
			}

			@Override
			public InputStream apply(CliConnection input) {
				if (input instanceof LinuxDefaultCliConnection) {
					LinuxDefaultCliConnection cliConnection = (LinuxDefaultCliConnection) input;
					return apply(cliConnection);
				}
				return null;
			}
		};
	}

	/**
	 * an expectation for a directory to contain a file
	 * 
	 * @param directory
	 *            the path of the directory to find the file in
	 * @param file
	 *            the exact name of the file to find in the folder
	 * @return the filename
	 */
	public static CliExpectedCondition<String> directoryConatins(final String directory, final String file) {
		return new CliExpectedCondition<String>() {
			@Override
			public String toString() {
				return "the directory " + directory.toString() + " to contain the file " + file;
			};

			@Override
			public String apply(CliConnection cliConnection) {
				if (cliConnection instanceof LinuxDefaultCliConnection) {
					LinuxDefaultCliConnection linux = (LinuxDefaultCliConnection) cliConnection;
					return apply(linux);
				}
				throw new IllegalArgumentException("the connection type is not yet supported");
			}

			public String apply(LinuxDefaultCliConnection linux) {
				try {
					Iterator<String> filesInDirectory = linux.fileList(directory);
					while (filesInDirectory.hasNext()) {
						String currFileName = filesInDirectory.next();
						if (0 == currFileName.compareTo(file)) {
							return currFileName;
						}
					}
				} catch (FileNotFoundException e) {
					throw null;
				} catch (IllegalArgumentException e) {
					throw e;
				} catch (IOException e) {
					throw new RuntimeException(e);
				} catch (Exception e) {
					throw new RuntimeException(e);
				}
				return null;
			}
		};
	}

	/**
	 * find the first file/folder that matches the file name (or file pattern if
	 * the isRegexp set to true)
	 * 
	 * @param directory
	 *            the directory to search a file in
	 * @param file
	 *            the file name or expression
	 * @param isRegexp
	 *            set to true for regular expression
	 * @return the path of the found file
	 */
	public static CliExpectedCondition<String> directoryContains(final String directory, final String file, final boolean isRegexp) {
		if (!isRegexp)
			return directoryConatins(directory, file);
		return new CliExpectedCondition<String>() {
			@Override
			public String toString() {
				return "the directory " + directory.toString() + " to contain a file matchin the regular expression " + file;
			};

			@Override
			public String apply(CliConnection cliConnection) {
				if (cliConnection instanceof LinuxDefaultCliConnection) {
					LinuxDefaultCliConnection linux = (LinuxDefaultCliConnection) cliConnection;
					return apply(linux);
				}
				throw new IllegalArgumentException("the connection type is not yet supported");
			}

			public String apply(LinuxDefaultCliConnection linux) {
				try {
					Iterator<String> filesInDirectory = linux.fileList(directory);
					while (filesInDirectory.hasNext()) {
						String currFileName = filesInDirectory.next();
						if (currFileName.matches(file)) {
							return currFileName;
						}
					}
				} catch (FileNotFoundException e) {
					return null;
				} catch (IllegalArgumentException e) {
					throw e;
				} catch (IOException e) {
					throw new RuntimeException(e);
				} catch (Exception e) {
					throw new RuntimeException(e);
				}

				return null;
			}
		};
	}

	/**
	 * an expectation for a process to be running
	 * 
	 * @param processName
	 *            the name of the process (the name of the binary executable)
	 * @return true if the process is running
	 * @see man ps
	 */
	public static CliExpectedCondition<Boolean> processIsRunning(final String processName) {
		return new CliExpectedCondition<Boolean>() {

			@Override
			public String toString() {
				return "the process " + processName + " to be running";
			}

			@Override
			public Boolean apply(CliConnection cliConnection) {
				if (cliConnection instanceof LinuxDefaultCliConnection) {
					LinuxDefaultCliConnection linux = (LinuxDefaultCliConnection) cliConnection;
					try {
						return apply(linux);
					} catch (Exception e) {
						throw new RuntimeException(e);
					}
				}
				throw new IllegalArgumentException("the connection type is not yet supported");
			}

			public Boolean apply(LinuxDefaultCliConnection linux) throws Exception {
				return linux.isProccessRunning(processName);
			}
		};
	}

	/**
	 * an expectation for a process not to be running, i.e that the machine does
	 * not run a process with this name
	 * 
	 * @param processName
	 *            the name of the process (the name of the binary executable)
	 * @return true if there is no process running with the given name
	 * @see man ps
	 */

	public static CliExpectedCondition<Boolean> processIsNotRunning(final String processName) {
		return new CliExpectedCondition<Boolean>() {

			@Override
			public String toString() {
				return "the process " + processName + " to be not running";
			}

			@Override
			public Boolean apply(CliConnection cliConnection) {
				if (cliConnection instanceof LinuxDefaultCliConnection) {
					LinuxDefaultCliConnection linux = (LinuxDefaultCliConnection) cliConnection;
					try {
						return apply(linux);
					} catch (Exception e) {
						throw new RuntimeException(e);
					}
				}
				throw new IllegalArgumentException("the connection type is not yet supported");
			}

			public Boolean apply(LinuxDefaultCliConnection linux) throws Exception {
				return linux.isProccessNotRunning(processName);
			}
		};
	}

	public enum DirectoryItemType {
		File("f"), Directory("d"), SymbolicLink("l");
		final String typeCmd;

		private DirectoryItemType(String typeCmd) {
			this.typeCmd = typeCmd;
		}
	}

	public static CliExpectedCondition<Integer> directoryHas(final String directory, final CompareMethod compareMethod,
			final int expectedAmount, final DirectoryItemType itemType) {

		StringBuilder commandBuilder = new StringBuilder().append("find ").append(directory).append(" -maxdepth 1 -type ")
				.append(itemType.typeCmd).append(" | wc -l");
		final String findCommand = commandBuilder.toString();
		final ComparableAssertion<Integer> amountOfItemsAssertion = new ComparableAssertion<Integer>(compareMethod, expectedAmount);
		return new CliExpectedCondition<Integer>() {

			@Override
			public Integer apply(CliConnection cliConnection) {
				return executionLogicHappens(amountOfItemsAssertion, intParser).apply(
						new CliCommandExecution(cliConnection, findCommand).silently());
			}

			@Override
			public String toString() {
				return "the directory's " + directory + " amount of " + itemType.name() + "(s) to be " + compareMethod.toString()
						+ " " + expectedAmount;
			}
		};
	}
}
