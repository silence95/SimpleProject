package com.httpserver.netty;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class BusinessThreadPool {

  private static Logger log = LoggerFactory.getLogger(BusinessThreadPool.class);
  
  private BusinessThreadPool() {}
  private static BusinessThreadPool instance = new BusinessThreadPool();
  public static BusinessThreadPool getInstance() {
    return instance;
  }
  
  private ExecutorService executorService = Executors.newCachedThreadPool();
  
  /**
   * use cachedThreadPool to execute the runnable task
   * @param runnable
   * @param timeout
   */
  public void submit(Runnable runnable) {
    Future<?> future = executorService.submit(runnable);
    printException(future);
  }
  
  public void submit(Runnable runnable, long timeout) {
    Future<?> future = executorService.submit(runnable);
    printException(future, timeout);
  }
  
  private void printException(Future<?> future, long timeout) {
    try {
      future.get(timeout, TimeUnit.MILLISECONDS);
    } catch (InterruptedException | ExecutionException | TimeoutException e) {
      log.error("the runnable task cause an error", e);
    };
  }

  /*
   * print thread exception
   */
  private void printException(Future<?> future) {
    try {
      future.get();
    } catch (InterruptedException | ExecutionException e) {
      log.error("the runnable task cause an error", e);
    }
  }
}
