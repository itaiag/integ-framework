package il.co.topq.integframework.hdfs;

import static org.apache.hadoop.io.IOUtils.copyBytes;
import il.co.topq.integframework.hdfs.support.HdfsWait;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.EnumSet;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.CreateFlag;
import org.apache.hadoop.fs.FileAlreadyExistsException;
import org.apache.hadoop.fs.Hdfs;
import org.apache.hadoop.fs.Options.CreateOpts;
import org.apache.hadoop.fs.ParentNotDirectoryException;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.fs.UnresolvedLinkException;
import org.apache.hadoop.fs.UnsupportedFileSystemException;
import org.apache.hadoop.hdfs.protocol.HdfsConstants;
import org.apache.hadoop.security.AccessControlException;

public class HDFSSystemModule {

	protected final Hdfs hdfs;
	private HdfsWait wait;

	public HDFSSystemModule(String host, int port, String userinfo)
			throws URISyntaxException, UnsupportedFileSystemException {
		Configuration conf = new Configuration();

		hdfs = (Hdfs) Hdfs.get(new URI(HdfsConstants.HDFS_URI_SCHEME, userinfo,
				host, port, "", "", ""), conf);
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

}