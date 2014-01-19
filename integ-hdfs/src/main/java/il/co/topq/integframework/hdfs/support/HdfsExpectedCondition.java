package il.co.topq.integframework.hdfs.support;

import org.apache.hadoop.fs.Hdfs;

import com.google.common.base.Function;

public interface HdfsExpectedCondition<T> extends Function<Hdfs, T> {

}
