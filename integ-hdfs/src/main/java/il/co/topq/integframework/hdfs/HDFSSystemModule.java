package il.co.topq.integframework.hdfs;

import static org.apache.hadoop.io.IOUtils.copyBytes;
import il.co.topq.integframework.hdfs.support.HdfsExpectedCondition;
import il.co.topq.integframework.hdfs.support.HdfsWait;
import il.co.topq.integframework.reporting.Reporter;
import il.co.topq.integframework.support.TimeoutException;

import java.io.*;
import java.net.URI;
import java.net.URISyntaxException;
import java.security.PrivilegedExceptionAction;
import java.util.EnumSet;
import java.util.concurrent.TimeUnit;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.*;
import org.apache.hadoop.fs.Options.CreateOpts;
import org.apache.hadoop.hdfs.protocol.HdfsConstants;
import org.apache.hadoop.security.AccessControlException;
import org.apache.hadoop.security.UserGroupInformation;

import com.google.common.base.Optional;
import com.google.common.base.Predicate;

public class HDFSSystemModule {

	protected final Hdfs hdfs;
	private HdfsWait wait;
	protected UserGroupInformation ugi;

	public HDFSSystemModule(final String host, final int port, final String userinfo) throws URISyntaxException, IOException,
			InterruptedException {

		ugi = UserGroupInformation.getCurrentUser();
		if (userinfo != null) {
			String username;
			if (userinfo.contains(":")) {
				username = userinfo.split(":")[0];
			} else {
				username = userinfo;
			}
			System.setProperty("user.name", username);
			System.setProperty("HADOOP_USER_NAME", username);
			ugi = UserGroupInformation.createRemoteUser(username);
		}

		hdfs = ugi.doAs(new PrivilegedExceptionAction<Hdfs>() {
			@Override
			public Hdfs run() throws Exception {
				Configuration conf = new Configuration();
				UserGroupInformation.setConfiguration(conf);
				return (Hdfs) Hdfs.get(new URI(HdfsConstants.HDFS_URI_SCHEME, userinfo, host, port, "", "", ""), conf);

			}

		});

	}

	public Hdfs getHdfs() {
		return hdfs;
	}

	public HdfsWait getWait() {
		if (wait == null) {
			wait = new HdfsWait(getHdfs());
		}
		return wait;
	}

	public void copyFromLocal(File src, Path dst,
			EnumSet<CreateFlag> createFlag, CreateOpts... opts)
			throws AccessControlException, FileAlreadyExistsException,
			FileNotFoundException, ParentNotDirectoryException,
			UnsupportedFileSystemException, UnresolvedLinkException,
			IOException {

		copyBytes(new BufferedInputStream(new FileInputStream(src)),
				new BufferedOutputStream(hdfs.create(dst, createFlag, opts)),
				10240, true);
	}

	public void copyFromRemote(Path src, File dst)
			throws AccessControlException,
			FileNotFoundException,
			UnresolvedLinkException,
			IOException {

		copyBytes(new BufferedInputStream(hdfs.open(src)),
				new BufferedOutputStream(new FileOutputStream(dst)), 10240,
				true);
	}

	public <T> T validateThat(HdfsExpectedCondition<T> expectedCondition) throws Throwable {
		Reporter.log("Validating: " + expectedCondition.toString());
		HdfsWait oldWait = this.wait;
		this.wait = new HdfsWait(hdfs);
		try {
			return wait.withTimeout(0, TimeUnit.MILLISECONDS).until(expectedCondition);
		} catch (TimeoutException exception) {
			throw Optional.fromNullable(exception.getCause()).or(exception);
		} finally {
			this.wait = oldWait;
		}

	}

	public boolean validateThat(Predicate<Hdfs> predicate) {
		// TODO AssertTrue
		Reporter.log("Validating " + predicate.toString());
		return predicate.apply(hdfs);
	}
}
