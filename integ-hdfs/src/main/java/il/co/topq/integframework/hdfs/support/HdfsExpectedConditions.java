package il.co.topq.integframework.hdfs.support;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

import org.apache.hadoop.fs.Hdfs;
import org.apache.hadoop.fs.LocatedFileStatus;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.fs.RemoteIterator;
import org.apache.hadoop.fs.UnresolvedLinkException;
import org.apache.hadoop.security.AccessControlException;

import com.google.common.base.Predicate;

public abstract class HdfsExpectedConditions {
	@SuppressWarnings("unused")
	private final int _;
	private HdfsExpectedConditions(){
		_=1;
	}
	
	public static Predicate<Hdfs> fileExists(final String path){
		return fileExists(new Path(path));
	}
	
	
	public static Predicate<Hdfs> fileExists(final Path path){
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

	public static HdfsExpectedCondition<InputStream> fileIsReadable(final Path path){
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

	public static Predicate<Hdfs> directoryConatins(final Path directory, final String file){
		return new Predicate<Hdfs> () {
			@Override
			public String toString() {
				return "the directory " + directory.toString() + " to contain the file " + file;
			};
				
			@Override
			public boolean apply(Hdfs hdfs) {
				try {
					RemoteIterator<LocatedFileStatus> locatedStatusIterator = hdfs.listLocatedStatus(directory);
					while (locatedStatusIterator.hasNext()){
						LocatedFileStatus locatedFileStatus = locatedStatusIterator.next();
						if (0==locatedFileStatus.getPath().compareTo(new Path(directory, file))){
							return true;
						}						
					}
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
				return false;
			}
		};
	}


	public static Predicate<Hdfs> directoryConatins(final Path directory, final String file, final boolean isRegexp){
		if (!isRegexp) return directoryConatins(directory, file);
		return new Predicate<Hdfs> () {
			@Override
			public String toString() {
				return "the directory " + directory.toString() + " to contain a file matchin the regular expression " + file;
			};
				
			@Override
			public boolean apply(Hdfs hdfs) {
				try {
					RemoteIterator<LocatedFileStatus> locatedStatusIterator = hdfs.listLocatedStatus(directory);
					while (locatedStatusIterator.hasNext()){
						LocatedFileStatus locatedFileStatus = locatedStatusIterator.next();
						if (locatedFileStatus.getPath().getName().matches(file)){
							return true;
						}						
					}
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
				return false;
			}
		};
	}
}
