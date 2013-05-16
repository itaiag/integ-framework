package il.co.topq.integframework.ivalidator;

import org.apache.commons.exec.DefaultExecutor;

/**
 * 
 * @author Itai_Agmon
 *
 */
public class NoErrorValidationExecutor extends DefaultExecutor {
	
    /** @see org.apache.commons.exec.Executor#isFailure(int) */
    public boolean isFailure(final int exitValue) {
        return false;
    }

}
