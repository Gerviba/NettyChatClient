package hu.gerviba.chatclient;

import hu.gerviba.chatclient.engine.ChatClient;

public class MainCLI {

	public static void main(String[] args) {
		new ChatClient("127.0.0.1", 4000, "Gerviba", "teszt").initCLI();
	}
	
}
