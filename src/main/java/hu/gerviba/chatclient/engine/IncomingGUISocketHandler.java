package hu.gerviba.chatclient.engine;

import java.io.IOException;

import hu.gerviba.chatclient.gui.ChatWindow;
import hu.gerviba.chatclient.gui.PasswordWindow;
import hu.gerviba.chatclient.gui.PreWindow;
import hu.gerviba.chatclient.util.Callback;
import hu.gerviba.chatclient.util.Util;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

public class IncomingGUISocketHandler extends SimpleChannelInboundHandler<String> {

	private PreWindow pw;
	private ChatWindow cw;
	private boolean handshake = false;
	
	public IncomingGUISocketHandler(ChatWindow cw, PreWindow pw) {
		this.cw = cw;
		this.pw = pw;
	}
	
	@Override
	protected void channelRead0(ChannelHandlerContext ctx, String msg) throws Exception {
		System.out.println(msg);
		if(handshake) {
			if(msg.length() > 6 && msg.startsWith("[GUI] ")) {
				if(msg.startsWith("[GUI] ROOM ")) {
					cw.setRoom(msg.substring(11, msg.length()));
				} else if(msg.startsWith("[GUI] PASSWORD ")) {
					new PasswordWindow().show(msg.substring(15, msg.length()), new Callback() {
						@Override
						public void call(String s) {
							ChatClient.getInstance().sendMessage("/password "+s);
						}
					});
				} else if(msg.equals("[GUI] FOCUS")) {
					cw.focus();
				}
			} else {
				cw.addLine(msg);
			}
		} else {
			switch(msg) {
				case "[GUI] HANDSHAKE_OK":
					pw.getFrame().dispose();
					pw = null;
					cw.setVisible();
					handshake = true;
					return;
				case "[GUI] HANDSHAKE_INVALID_PASSWORD":
					pw.setAllEnable(true);
					pw.setStatus("Invalid password");
					cw.dispose();
					ctx.close();
					return;
				case "[GUI] HANDSHAKE_ALREADY_ONLINE":
					pw.setAllEnable(true);
					pw.setStatus("Somebody is still online with this name");
					cw.dispose();
					ctx.close();
					return;
				case "[GUI] HANDSHAKE_SERVERPASS_INVALID":
					pw.setAllEnable(true);
					pw.setStatus("Server password is invalid");
					cw.dispose();
					ctx.close();
					return;
				case "[GUI] HANDSHAKE_SERVERPASS_NEEDED":
					pw.setAllEnable(false);
					pw.setStatus("Entering server password");
					cw.dispose();
					ctx.close();
					
					new PasswordWindow().show("Enter the server password:", new Callback() {
						@Override
						public void call(String s) {
							if(s == null) {
								pw.setStatus("Connection cancelled");
							} else {
								pw.performReconnect("s:"+Util.encryptPassword(s));
							}
						}
					});
					return;
				case "[GUI] HANDSHAKE_NO_GUESTS":
					pw.setAllEnable(true);
					pw.setStatus("Guest login is disabled in this server");
					ctx.close();
					return;
				case "[GUI] HANDSHAKE_REG_NO_NAME":
					pw.setAllEnable(true);
					pw.setStatus("Name field is empty!");
					ctx.close();
					return;
				case "[GUI] HANDSHAKE_REG_NO_PASS":
					pw.setAllEnable(true);
					pw.setStatus("Password field is empty!");
					ctx.close();
					return;
				case "[GUI] HANDSHAKE_REG_NO_MAIL":
					pw.setAllEnable(true);
					pw.setStatus("E-mail field is empty!");
					ctx.close();
					return;
				case "[GUI] HANDSHAKE_REG_NO_NICK":
					pw.setAllEnable(true);
					pw.setStatus("Nickname field is empty!");
					ctx.close();
					return;
				case "[GUI] HANDSHAKE_REG_WRONG_NAME":
					pw.setAllEnable(true);
					pw.setStatus("Invalid name format!");
					ctx.close();
					return;
				case "[GUI] HANDSHAKE_REG_WRONG_NICK":
					pw.setAllEnable(true);
					pw.setStatus("Invalid nickname format!");
					ctx.close();
					return;
				case "[GUI] HANDSHAKE_REG_WRONG_EMAIL":
					pw.setAllEnable(true);
					pw.setStatus("Invalid e-mail format!");
					ctx.close();
					return;
				case "[GUI] HANDSHAKE_REG_WRONG_HASH":
					pw.setAllEnable(true);
					pw.setStatus("Hashing failure!");
					ctx.close();
					return;
				case "[GUI] HANDSHAKE_REG_USED_NAME":
					pw.setAllEnable(true);
					pw.setStatus("This name is already in use!");
					ctx.close();
					return;
				case "[GUI] HANDSHAKE_REG_USED_NICK":
					pw.setAllEnable(true);
					pw.setStatus("This nickname is already in use!");
					ctx.close();
					return;
				case "[GUI] HANDSHAKE_REG_USED_MAIL":
					pw.setAllEnable(true);
					pw.setStatus("This e-mail address is already in use!");
					ctx.close();
					return;
			}
			if(msg.startsWith("[GUI] BAN ")) {
				pw.setAllEnable(true);
				pw.setStatus("Ban reason: "+msg.substring(10));
				ctx.close();
			}
		}
	}

	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
		if(!handshake) {
			pw.getFrame().setVisible(true);
			pw.setStatus("Connection lost");
			pw.setAllEnable(true);
			ctx.close();
		}
		if (cause instanceof IOException) {
			cw.addLine("[ERROR] Server forcibly closed the connection");
			System.err.println("[ERROR] Server forcibly closed the connection");
		} else {
			cw.addLine("[ERROR] Unnown exception");
			System.err.println("[ERROR] Unnown exception");
		}
		ctx.close();
	}

}
