package il.co.topq.integframework.ivalidator.repository;

/**
 * 
 * @author Itai_Agmon
 *
 */
public class FileRepositoryException extends Exception {

	private static final long serialVersionUID = 1L;

	public FileRepositoryException(String message, Throwable throwable) {
		super(message, throwable);
	}

	public FileRepositoryException(String message) {
		super(message);
	}

}
