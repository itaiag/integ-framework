package il.co.topq.integframework.hdfs.support;

import java.util.concurrent.TimeUnit;

import org.apache.hadoop.fs.Hdfs;

public class HdfsWait extends FluentWait<Hdfs> {

	private static final int DEFAULT_POLLING_INTERVAL_IN_MILLISECONDS = 500;
	private static final int DEFAULT_TIMEOUT_IN_MINUTES = 2;

	public HdfsWait(Hdfs input) {
		super(input);
		withTimeout(DEFAULT_TIMEOUT_IN_MINUTES, TimeUnit.MINUTES);
		pollingEvery(DEFAULT_POLLING_INTERVAL_IN_MILLISECONDS, TimeUnit.MILLISECONDS);
	}

}
