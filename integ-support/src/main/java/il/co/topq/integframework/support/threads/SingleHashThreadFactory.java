package il.co.topq.integframework.support.threads;

import static java.util.Collections.synchronizedMap;
import static java.util.concurrent.Executors.defaultThreadFactory;

import java.util.Map;
import java.util.WeakHashMap;
import java.util.concurrent.ThreadFactory;

/**
 * This {@link ThreadFactory} constructs a new thread only for newly added
 * {@link Runnable}s<br>
 * 
 * 
 * @author Aharon
 * 
 */
public final class SingleHashThreadFactory implements ThreadFactory {
	final ThreadFactory bakingThreadFactory;

	/**
	 * Create a new instance
	 * 
	 * @param bakedThreadFactory
	 *            a factory to delegate creations to
	 * 
	 */
	public SingleHashThreadFactory(ThreadFactory bakedThreadFactory) {
		this.bakingThreadFactory = bakedThreadFactory;
	}

	/**
	 * Create a new instance, delegate {@link #newThread(Runnable)} to
	 * {@link Executors#defaultThreadFactory()} if needed
	 */
	public SingleHashThreadFactory() {
		this(defaultThreadFactory());
	}

	private final Map<Runnable, Thread> allThreads = synchronizedMap(new WeakHashMap<Runnable, Thread>());

	/**
	 * Constructs a new {@link Thread} for the given {@link Runnable}, if it is
	 * the first time for it.<br>
	 * On next invocations of this method, the returned thread is the one that
	 * provided for the first one. <br>
	 * Note that the given runnable {@code (r1)} is regarded as <b>old</b> if
	 * another runnable {@code (r2)} was invoked on, and which<br>
	 * {@code r1.hashCode() == r2.hashCode() && r1.equals(r2)}.
	 * 
	 * @see Object#hashCode()
	 * @see Object#equals(Object)
	 * @see java.util.HashMap#get(Object)
	 * @see java.util.HashMap#put(Object, Object)
	 */
	@Override
	public Thread newThread(Runnable r) {
		synchronized (allThreads) {
			Thread targetThread = allThreads.get(r);
			if (targetThread == null) {
				allThreads.put(r, targetThread = bakingThreadFactory.newThread(r));
			}
			return targetThread;
		}
	}

}
