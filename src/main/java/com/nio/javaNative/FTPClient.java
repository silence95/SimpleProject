package com.nio.javaNative;

import com.thread.ThreadFactoryHelper;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.StandardSocketOptions;
import java.nio.channels.AsynchronousChannelGroup;
import java.nio.channels.AsynchronousSocketChannel;
import java.nio.channels.CompletionHandler;
import java.nio.channels.spi.AsynchronousChannelProvider;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import sun.rmi.transport.Transport;

/**
 * @author siling
 * @Date 2017/12/23.
 */
public class FTPClient {

  private final static Logger logger = LoggerFactory.getLogger(FTPClient.class);

  public static void main(String[] args) throws IOException {
    ExecutorService executorService = Executors.newCachedThreadPool(ThreadFactoryHelper.create("aio-handle", false));
    // initialSize : executorService threadPool
    AsynchronousChannelProvider sProvider = AsynchronousChannelProvider.provider();
    AsynchronousChannelGroup channelGroup = sProvider.openAsynchronousChannelGroup(executorService, 2);
    AsynchronousSocketChannel channel = sProvider.openAsynchronousSocketChannel(channelGroup);
    updateOption(channel);
    InetSocketAddress socketAddress = new InetSocketAddress("ftp.gnu.org", 21);
//    channel.connect(socketAddress, this, mConnectHandler);
  }

  private static CompletionHandler mConnectHandler = new CompletionHandler() {

    @Override
    public void completed(Object result, Object attachment) {

    }

    @Override
    public void failed(Throwable exc, Object attachment) {

    }
  };

  private static void updateOption(AsynchronousSocketChannel channel) {
    try {
      channel.setOption(StandardSocketOptions.SO_SNDBUF, 512 * 1024)
          .setOption(StandardSocketOptions.SO_RCVBUF, 512 * 1024)
          .setOption(StandardSocketOptions.SO_KEEPALIVE, true)
          .setOption(StandardSocketOptions.SO_REUSEADDR, true)
          .setOption(StandardSocketOptions.TCP_NODELAY, true);
    } catch (IOException e) {
      logger.error("set option error -> " + e.getMessage(), e);
    }
  }
}
