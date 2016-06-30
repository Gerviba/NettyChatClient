package hu.gerviba.chatclient.engine;

import java.io.IOException;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

public class IncomingCLISocketHandler extends SimpleChannelInboundHandler<String> {

	@Override
	protected void channelRead0(ChannelHandlerContext ctx, String msg) throws Exception {
		System.out.println(msg);
	}

	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
		if (cause instanceof IOException) {
			System.err.println("[ERROR] Server forcibly closed the connection");
		} else {
			System.err.println("[ERROR] Unnown exception");
		}
		ctx.close();
	}

}
