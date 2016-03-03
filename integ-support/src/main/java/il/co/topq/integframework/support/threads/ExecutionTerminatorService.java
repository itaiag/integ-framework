package il.co.topq.integframework.support.threads;

import java.util.Collection;
import java.util.List;
import java.util.concurrent.*;

import com.google.common.util.concurrent.ThreadFactoryBuilder;

public class ExecutionTerminatorService implements ExecutorService {

	private ExecutorService realTerminator;

	private final static class Terminator implements Runnable {
		private final ExecutorService toTerminate;
		final int timeout;
		final TimeUnit unit;

		public Terminator(ExecutorService toTerminate, int timeoutForAll, final TimeUnit unit) {
			this.toTerminate = toTerminate;
			this.timeout = timeoutForAll;
			this.unit = unit;
		}

		@Override
		public void run() {
			try {
				toTerminate.awaitTermination(timeout, unit);
			} catch (InterruptedException e) {
				Thread t = Thread.currentThread();
				t.getUncaughtExceptionHandler().uncaughtException(t, e);
			}
		}
	};

	final int timeout;
	final TimeUnit unit;

	public ExecutionTerminatorService(Collection<ExecutorService> executorServicesToWaitFor, final int timeoutForAll,
			final TimeUnit unit) {
		this.timeout = timeoutForAll;
		this.unit = unit;

		realTerminator = Executors.newFixedThreadPool(executorServicesToWaitFor.size(),
				new ThreadFactoryBuilder().setNameFormat("Terminator %d").build());
		for (final ExecutorService executorService : executorServicesToWaitFor) {
			realTerminator.execute(new Terminator(executorService, timeoutForAll, unit));
		}
		realTerminator.shutdown();
	}

	@Override
	public void execute(Runnable command) {
		throw new IllegalArgumentException();
	}

	@Override
	public void shutdown() {
		// already done in c'Tor
	}

	@Override
	public List<Runnable> shutdownNow() {
		return realTerminator.shutdownNow();
	}

	@Override
	public boolean isShutdown() {
		return realTerminator.isShutdown();
	}

	@Override
	public boolean isTerminated() {
		return realTerminator.isTerminated();
	}

	@Override
	public boolean awaitTermination(long timeout, TimeUnit unit) throws InterruptedException {
		return realTerminator.awaitTermination(timeout, unit);
	}

	@Override
	public <T> Future<T> submit(Callable<T> task) {
		throw new IllegalArgumentException();
	}

	@Override
	public <T> Future<T> submit(Runnable task, T result) {
		throw new IllegalArgumentException();
	}

	@Override
	public Future<?> submit(Runnable task) {
		throw new IllegalArgumentException();
	}

	@Override
	public <T> List<Future<T>> invokeAll(Collection<? extends Callable<T>> tasks) throws InterruptedException {
		throw new IllegalArgumentException();
	}

	@Override
	public <T> List<Future<T>> invokeAll(Collection<? extends Callable<T>> tasks, long timeout, TimeUnit unit)
			throws InterruptedException {
		throw new IllegalArgumentException();
	}

	@Override
	public <T> T invokeAny(Collection<? extends Callable<T>> tasks) throws InterruptedException, ExecutionException {
		throw new IllegalArgumentException();
	}

	@Override
	public <T> T invokeAny(Collection<? extends Callable<T>> tasks, long timeout, TimeUnit unit) throws InterruptedException,
			ExecutionException, TimeoutException {
		throw new IllegalArgumentException();
	}
}
