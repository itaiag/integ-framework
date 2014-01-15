
package il.co.topq.integframework.hdfs;

import java.net.URI;
import java.net.URISyntaxException;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Hdfs;
import org.apache.hadoop.fs.UnsupportedFileSystemException;
import org.apache.hadoop.hdfs.protocol.HdfsConstants;

public class HDFSSystemModule {

	protected final Hdfs hdfs;

	public HDFSSystemModule(String host, int port, String userinfo) throws URISyntaxException,
			UnsupportedFileSystemException {
		Configuration conf = new Configuration();

		hdfs = (Hdfs) Hdfs.get(new URI(HdfsConstants.HDFS_URI_SCHEME, userinfo, host,
				port, "", "", ""), conf);
	}

	public Hdfs getHdfs() {
		return hdfs;
	}
}
