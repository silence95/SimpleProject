package com.httpserver.netty;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;

public class NettyHttpServer {
	
	public void start(int port) throws Exception {
		EventLoopGroup bossGroup = new NioEventLoopGroup(1);
		EventLoopGroup workerGroup = new NioEventLoopGroup(1);
		try {
			ServerBootstrap b = new ServerBootstrap();
			b.group(bossGroup, workerGroup).channel(NioServerSocketChannel.class)
					.childHandler(new HttpChannelInitializer())
					.option(ChannelOption.SO_BACKLOG, 1024)
					.childOption(ChannelOption.SO_REUSEADDR, Boolean.TRUE)
			        .childOption(ChannelOption.TCP_NODELAY, Boolean.TRUE)
					.childOption(ChannelOption.SO_KEEPALIVE, true);

			ChannelFuture future = b.bind(port).sync();

			future.channel().closeFuture().sync();
		} finally {
			workerGroup.shutdownGracefully();
			bossGroup.shutdownGracefully();
		}
	}

	public static void main(String[] args) throws Exception {
		NettyHttpServer server = new NettyHttpServer();
		server.start(8844);
	}
}
