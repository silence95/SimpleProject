package com.httpserver.netty;

import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpRequestDecoder;
import io.netty.handler.codec.http.HttpResponseEncoder;
import io.netty.handler.stream.ChunkedWriteHandler;

/**
 * @author Lucas on 2017/7/17.
 */
public class HttpChannelInitializer extends ChannelInitializer<SocketChannel> {

  @Override
  protected void initChannel(SocketChannel ch) throws Exception {
    ChannelPipeline pipeline = ch.pipeline();
    pipeline.addLast("decoder",new HttpRequestDecoder())
        .addLast("aggregator",new HttpObjectAggregator(1024 * 1024 * 16))
        .addLast("encoder",new HttpResponseEncoder())
        .addLast("chunkedWriter", new ChunkedWriteHandler())
        .addLast("handler", new HttpChannelInboundHandler());
  }

}
