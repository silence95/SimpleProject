package com.httpserver.netty;

import java.util.ArrayList;
import java.util.List;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInboundHandler;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.http.HttpRequestDecoder;
import io.netty.handler.codec.http.HttpResponseEncoder;

public class NettyHttpServer {

	private List<Class> classes = new ArrayList<>();
	
	public NettyHttpServer(List<Class> classes) {
		this.classes = classes;
	}
	
	public void start(int port) throws Exception {
		EventLoopGroup bossGroup = new NioEventLoopGroup();
		EventLoopGroup workerGroup = new NioEventLoopGroup();
		try {
			ServerBootstrap b = new ServerBootstrap();
			b.group(bossGroup, workerGroup).channel(NioServerSocketChannel.class)
					.childHandler(new ChannelInitializer<SocketChannel>() {
						@Override
						public void initChannel(SocketChannel ch) throws Exception {
							// server端发送的是httpResponse，所以要使用HttpResponseEncoder进行编码
							ch.pipeline().addLast(new HttpResponseEncoder());
							// server端接收到的是httpRequest，所以要使用HttpRequestDecoder进行解码
							ch.pipeline().addLast(new HttpRequestDecoder());
							if(classes != null && !classes.isEmpty()) {
								for(Class clazz : classes) {
									ch.pipeline().addLast((ChannelInboundHandler)clazz.newInstance());
								}
							}
						}
					}).option(ChannelOption.SO_BACKLOG, 128).childOption(ChannelOption.SO_KEEPALIVE, true);

			ChannelFuture future = b.bind(port).sync();

			future.channel().closeFuture().sync();
		} finally {
			workerGroup.shutdownGracefully();
			bossGroup.shutdownGracefully();
		}
	}

	public static void main(String[] args) throws Exception {
		List<Class> classes = new ArrayList<>();
		classes.add(HttpServerInboundHandler.class);
		NettyHttpServer server = new NettyHttpServer(classes);
		server.start(8844);
	}
}
