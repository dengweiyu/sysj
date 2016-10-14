package com.li.videoapplication.data.network;

import android.util.Log;

import com.li.videoapplication.data.model.entity.Task;

import java.util.ArrayDeque;
import java.util.Collection;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * 功能：网络请求线程池
 */
public class RequestExecutor {

	private static final String ACTION = RequestExecutor.class.getName();
	private static final String TAG = RequestExecutor.class.getSimpleName();

	// private static final ExecutorService FIXED_THREAD_POOL = Executors.newFixedThreadPool(CPU_COUNT + 1);

	/**
	 * 功能：新线程执行
	 */
	public synchronized final static void start(Runnable runnable) {
		if (runnable != null) {
			try {
				Log.i(TAG, "start/runnable=" + runnable);
				Thread thread = new Thread(runnable);
				thread.start();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	/**
	 * 功能：单线程执行
	 */
	public synchronized final static void executeOnSerialExecutor(Runnable runnable) {
		if (SERIAL_EXECUTOR != null && runnable != null) {
			try {
				Log.i(TAG, "executeOnSerialExecutor/runnable=" + runnable);
				SERIAL_EXECUTOR.execute(runnable);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	/**
	 * 功能：线程池执行
	 */
	public synchronized final static void execute(Runnable runnable) {
		if (THREAD_POOL_EXECUTOR != null && runnable != null) {
			try {
				Log.i(TAG, "execute/runnable=" + runnable);
				THREAD_POOL_EXECUTOR.execute(runnable);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	/**
	 * 功能：移除等待任务
	 */
	public synchronized final static void removeTask(Runnable runnable) {
		if (POOL_WORK_QUEUE != null && runnable != null) {
			try {
				Log.i(TAG, "removeAllTask/runnable=" + runnable);
				POOL_WORK_QUEUE.remove(runnable);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	/**
	 * 功能：移除等待任务
	 */
	public synchronized final static void removeAllTask(Collection<Runnable> runnables) {
		if (POOL_WORK_QUEUE != null && runnables != null && runnables.size() > 0) {
			try {
				Log.i(TAG, "removeAllTask/runnables=" + runnables);
				POOL_WORK_QUEUE.removeAll(runnables);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	/**
	 * 功能：移除所有等待任务
	 */
	public synchronized final static void clearAllTask() {
		if (POOL_WORK_QUEUE != null && POOL_WORK_QUEUE.size() > 0) {
			try {
				POOL_WORK_QUEUE.clear();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	/**
	 * 功能：关闭，并等待任务执行完成，不接受新任务
	 */
	public synchronized final static void shutdown() {
		if (THREAD_POOL_EXECUTOR != null) {
			synchronized (RequestExecutor.class) {
				try {
					THREAD_POOL_EXECUTOR.shutdown();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
	}

	/**
	 * 功能：关闭，立即关闭，并挂起所有正在执行的线程，不接受新任务
	 */
	public synchronized final static void shutdownNow() {
		if (THREAD_POOL_EXECUTOR != null) {
			synchronized (RequestExecutor.class) {
				try {
					THREAD_POOL_EXECUTOR.shutdownNow();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
	}

	private static final int CPU_COUNT = Runtime.getRuntime().availableProcessors();
	public static final int CORE_POOL_SIZE = CPU_COUNT + 1;
	public static final int MAXIMUM_POOL_SIZE = CPU_COUNT * 2 + 1;
	private static final int KEEP_ALIVE = 1;

	private static final ThreadFactory THREAD_FACTORY = new ThreadFactory() {

		private final AtomicInteger count = new AtomicInteger(1);

		public Thread newThread(Runnable r) {
			return new Thread(r, "AsyncTask-" + count.getAndIncrement());
		}
	};

	private static final BlockingQueue<Runnable> POOL_WORK_QUEUE = new LinkedBlockingQueue<>(128);

	public static final ExecutorService THREAD_POOL_EXECUTOR = new ThreadPoolExecutor(CORE_POOL_SIZE,
			MAXIMUM_POOL_SIZE,
			KEEP_ALIVE,
			TimeUnit.SECONDS,
			POOL_WORK_QUEUE,
			THREAD_FACTORY);

	public static final Executor SERIAL_EXECUTOR = new SerialExecutor();

	private static class SerialExecutor implements Executor {

		final ArrayDeque<Runnable> mTasks = new ArrayDeque<>();
		Runnable mActive;

		public synchronized void execute(final Runnable r) {
			mTasks.offer(new Runnable() {
				public void run() {
					try {
						r.run();
					} finally {
						scheduleNext();
					}
				}
			});
			if (mActive == null) {
				scheduleNext();
			}
		}

		protected synchronized void scheduleNext() {
			if ((mActive = mTasks.poll()) != null) {
				THREAD_POOL_EXECUTOR.execute(mActive);
			}
		}
	}
}
