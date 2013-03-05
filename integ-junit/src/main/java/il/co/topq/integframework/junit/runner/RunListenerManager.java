package il.co.topq.integframework.junit.runner;

import java.util.ArrayList;
import java.util.List;

import org.junit.runner.notification.RunListener;

public class RunListenerManager {

	private static RunListenerManager instance;
	/**
	 * This list is used only to create listener from the spring context.
	 */
	private List<RunListener> listeners;

	private RunListenerManager() {
	}

	public static RunListenerManager getInstance() {
		if (null == instance) {
			instance = new RunListenerManager();
		}
		return instance;
	}

	public void addRunListener(RunListener listener) {
		if (null == listeners) {
			listeners = new ArrayList<RunListener>();
		}
		listeners.add(listener);
	}

	public List<RunListener> getListeners() {
		return listeners;
	}

	public void setListeners(List<RunListener> listeners) {
		this.listeners = listeners;
	}

}
