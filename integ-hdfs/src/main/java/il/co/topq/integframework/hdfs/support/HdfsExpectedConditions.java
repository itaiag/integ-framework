package il.co.topq.integframework.hdfs.support;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

import org.apache.hadoop.fs.*;
import org.apache.hadoop.security.AccessControlException;

import com.google.common.base.Predicate;

public abstract class HdfsExpectedConditions {
	@SuppressWarnings("unused")
	private final int _;

	private HdfsExpectedConditions() {
		_ = 1;
	}

	public static Predicate<Hdfs> fileExists(final String path) {
		return fileExists(new Path(path));
	}

	public static Predicate<Hdfs> fileExists(final Path path) {
		return new Predicate<Hdfs>() {

			@Override
			public String toString() {
				return "the file in " + path.toString() + " to be openable";
			};

			@Override
			public boolean apply(Hdfs hdfs) {
				try {
					hdfs.open(path).close();
				} catch (AccessControlException e) {
					throw new RuntimeException(e);
				} catch (FileNotFoundException e) {
					return false;
				} catch (UnresolvedLinkException e) {
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

	public static HdfsExpectedCondition<InputStream> fileIsReadable(final Path path) {
		return new HdfsExpectedCondition<InputStream>() {

			@Override
			public String toString() {
				return "the file in " + path.toString() + " to be openable";
			};

			@Override
			public InputStream apply(Hdfs hdfs) {
				try {
					return hdfs.open(path);
				} catch (AccessControlException e) {
					throw new RuntimeException(e);
				} catch (FileNotFoundException e) {
					return null;
				} catch (UnresolvedLinkException e) {
					return null;
				} catch (IllegalArgumentException e) {
					throw e;
				} catch (IOException e) {
					throw new RuntimeException(e);
				}
			}
		};
	}

	public static HdfsExpectedCondition<Path> directoryConatins(final Path directory, final String file) {
		return new HdfsExpectedCondition<Path>() {
			@Override
			public String toString() {
				return "the directory " + directory.toString() + " to contain the file " + file;
			};

			@Override
			public Path apply(Hdfs hdfs) {
				try {
					RemoteIterator<LocatedFileStatus> locatedStatusIterator = hdfs.listLocatedStatus(directory);
					while (locatedStatusIterator.hasNext()) {
						LocatedFileStatus locatedFileStatus = locatedStatusIterator.next();
						if (0 == locatedFileStatus.getPath().compareTo(new Path(directory, file))) {
							return locatedFileStatus.getPath();
						}
					}
				} catch (AccessControlException e) {
					throw new RuntimeException(e);
				} catch (FileNotFoundException e) {
					throw null;
				} catch (UnresolvedLinkException e) {
					throw new RuntimeException(e);
				} catch (IllegalArgumentException e) {
					throw e;
				} catch (IOException e) {
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
	public static HdfsExpectedCondition<Path> directoryContains(final Path directory, final String file, final boolean isRegexp) {
		if (!isRegexp)
			return directoryConatins(directory, file);
		return new HdfsExpectedCondition<Path>() {
			@Override
			public String toString() {
				return "the directory " + directory.toString() + " to contain a file matchin the regular expression " + file;
			};

			@Override
			public Path apply(Hdfs hdfs) {
				try {
					RemoteIterator<LocatedFileStatus> locatedStatusIterator = hdfs.listLocatedStatus(directory);
					while (locatedStatusIterator.hasNext()) {
						LocatedFileStatus locatedFileStatus = locatedStatusIterator.next();
						if (locatedFileStatus.getPath().getName().matches(file)) {
							return locatedFileStatus.getPath();
						}
					}
				} catch (AccessControlException e) {
					throw new RuntimeException(e);
				} catch (FileNotFoundException e) {
					return null;
				} catch (UnresolvedLinkException e) {
					throw new RuntimeException(e);
				} catch (IllegalArgumentException e) {
					throw e;
				} catch (IOException e) {
					throw new RuntimeException(e);
				}
				return null;
			}
		};
	}

	public static Predicate<Hdfs> isDirectory(final Path path) {
		return new Predicate<Hdfs>() {
			@Override
			public String toString() {
				return "the path " + path.toString() + " is a directory";
			}

			@Override
			public boolean apply(Hdfs hdfs) {
				try {
					return hdfs.getFileStatus(path).isDirectory();
				} catch (UnresolvedLinkException e) {
					return false;
				} catch (IOException e) {
					return false;
				}
			}
		};
	}
}
