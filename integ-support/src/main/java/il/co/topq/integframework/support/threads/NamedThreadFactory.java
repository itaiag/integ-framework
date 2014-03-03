package il.co.topq.integframework.support.threads;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.atomic.AtomicInteger;

public class NamedThreadFactory implements ThreadFactory {
	private final String name;
	private static Map<String, AtomicInteger> names = new ConcurrentHashMap<String, AtomicInteger>();

	public NamedThreadFactory(String name) {
		synchronized (names) {
			if (!names.containsKey(name)) {
				names.put(name, new AtomicInteger());
			}
		}
		this.name = name;
	}

	@Override
	public Thread newThread(Runnable r) {
		Thread thread = Executors.defaultThreadFactory().newThread(r);
		thread.setName(name + "#" + names.get(name).incrementAndGet());
		return thread;
	}
}