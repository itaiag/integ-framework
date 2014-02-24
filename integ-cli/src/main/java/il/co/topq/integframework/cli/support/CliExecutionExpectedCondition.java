package il.co.topq.integframework.cli.support;

import il.co.topq.integframework.cli.process.CliCommandExecution;

import com.google.common.base.Function;

public interface CliExecutionExpectedCondition<T> extends Function<CliCommandExecution, T> {

}
