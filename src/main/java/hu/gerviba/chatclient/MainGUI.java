package hu.gerviba.chatclient;

import hu.gerviba.chatclient.gui.PreWindow;

public class MainGUI {

	public static void main(String[] args) {
		System.out.println("[SYS] Launching GUI");
		PreWindow gui = new PreWindow();
		gui.init();
	}

}
