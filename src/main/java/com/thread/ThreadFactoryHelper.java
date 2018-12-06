package com.thread;

import java.util.concurrent.ThreadFactory;
import java.util.concurrent.atomic.AtomicLong;

/**
 * @author siling
 * @Date 2017/12/23.
 */
public class ThreadFactoryHelper {

  public static ThreadFactory create(final String name, final boolean daemon) {
    return new ThreadFactory() {

      private final AtomicLong mCount = new AtomicLong();

      public Thread newThread(Runnable r) {
        Thread thread = new Thread(name + "#" + mCount.getAndIncrement());
        thread.setDaemon(daemon);
        return thread;
      }
    };
  }
}
