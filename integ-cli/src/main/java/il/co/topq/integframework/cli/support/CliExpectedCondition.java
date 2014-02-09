package il.co.topq.integframework.cli.support;


import il.co.topq.integframework.cli.conn.CliConnection;

import com.google.common.base.Function;

public interface CliExpectedCondition<T> extends Function<CliConnection, T> {

}
