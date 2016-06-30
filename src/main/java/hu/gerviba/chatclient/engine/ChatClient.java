package hu.gerviba.chatclient.engine;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.concurrent.ConcurrentLinkedDeque;

import hu.gerviba.chatclient.gui.ChatWindow;
import hu.gerviba.chatclient.gui.PreWindow;
import hu.gerviba.chatclient.util.PipelineInitializer;
import hu.gerviba.chatclient.util.Util;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;

public class ChatClient {
	
	public static final String VERSION = "v0.1.0 Beta";
	private static ChatClient instance;
	
	private final String host;
	private final int port;
	private final String username;
	private final String password;
	
	private ConcurrentLinkedDeque<String> deque = new ConcurrentLinkedDeque<>();

	public ChatClient(String host, int port, String username, String password) {
		this.host = host;
		this.port = port;
		this.username = username;
		this.password = password;
		
		instance = this;
	}

	public void initCLI() {
		EventLoopGroup group = new NioEventLoopGroup();

		try {
			Bootstrap bootstarp = new Bootstrap().group(group).channel(NioSocketChannel.class)
					.handler(new PipelineInitializer(new IncomingCLISocketHandler()));

			Channel channel = bootstarp.connect(host, port).sync().channel();
			channel.writeAndFlush("//HANDSHAKE// u:" + username + " p:" + Util.encryptPassword(password) + "\r\n");

			BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
			while (true) {
				String line = in.readLine();
				channel.writeAndFlush(line + "\r\n");
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			group.shutdownGracefully();
		}
	}

	public void initGUI(ChatWindow cw, PreWindow pw, String params) {
		EventLoopGroup group = new NioEventLoopGroup();

		try {
			Bootstrap bootstarp = new Bootstrap().group(group).channel(NioSocketChannel.class)
					.handler(new PipelineInitializer(new IncomingGUISocketHandler(cw, pw)));

			Channel channel = bootstarp.connect(host, port).sync().channel();
			channel.writeAndFlush("//HANDSHAKE// u:" + username + " p:" + Util.encryptPassword(password) + " -g " + params + "\r\n");
			
			for(;;) {
				if(!deque.isEmpty()) {
					String msg = deque.removeFirst();
					channel.writeAndFlush(msg + "\r\n");
					System.out.println("[>] "+msg);
				}
			}
		} catch (Exception e) {
			if(e.getMessage().contains("Connection refused")) {
				pw.setStatus("Connection refused");
				pw.setAllEnable(true);
			} else {
				pw.setStatus("A connection problem occurred");
				pw.setAllEnable(true);
			}
			e.printStackTrace();
		} finally {
			group.shutdownGracefully();
		}
	}

	public void sendMessage(String message) {
		if(message.length() > 0)
			deque.addLast(message);
	}

	public static ChatClient getInstance() {
		return instance;
	}
	
}
