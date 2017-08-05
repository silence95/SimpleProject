package com.httpserver.netty;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.DefaultFullHttpResponse;
import io.netty.handler.codec.http.FullHttpRequest;
import io.netty.handler.codec.http.HttpResponseStatus;
import io.netty.handler.codec.http.HttpVersion;

public class HttpChannelInboundHandler extends SimpleChannelInboundHandler<FullHttpRequest> {

	private static Logger log = LoggerFactory.getLogger(HttpChannelInboundHandler.class);
	
	@Override
	protected void channelRead0(ChannelHandlerContext channelHandlerContext, FullHttpRequest fullHttpRequest) throws Exception {
		BusinessThreadPool.getInstance().submit(new Runnable() {
			@Override
			public void run() {
//				try {
//					Thread.sleep(5000);
//				} catch (InterruptedException e) {
//					e.printStackTrace();
//				}
				log.info("business thread pool start handle request");
				DefaultFullHttpResponse response = new DefaultFullHttpResponse(HttpVersion.HTTP_1_1, HttpResponseStatus.OK, Unpooled.wrappedBuffer("success".getBytes()));
				channelHandlerContext.writeAndFlush(response);
			}
		});
	}
	
	@Override
	public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
		log.info("channel complete");
//		ctx.writeAndFlush(Unpooled.EMPTY_BUFFER).addListener(ChannelFutureListener.CLOSE);
	    super.channelReadComplete(ctx);
	}

	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
		log.error("handler cause an exception", cause);
		ctx.close();
		super.exceptionCaught(ctx, cause);
	}

}
