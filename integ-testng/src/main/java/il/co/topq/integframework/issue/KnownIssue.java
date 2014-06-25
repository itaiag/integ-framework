package il.co.topq.integframework.issue;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target({ ElementType.METHOD })
public @interface KnownIssue {
	public @interface KnownIssues {
		KnownIssue[] value();
	}

	String issue() default "Known Issue";

	Class<? extends Throwable> throwableType() default AssertionError.class;

	IssueType type() default IssueType.PASS;

	String messageMustMatch() default ".*";

	long dueTo() default -1;
}
