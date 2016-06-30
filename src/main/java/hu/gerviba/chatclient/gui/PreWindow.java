package hu.gerviba.chatclient.gui;

import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.Toolkit;
import java.awt.Window.Type;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.HashMap;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.SwingConstants;

import com.alee.laf.WebLookAndFeel;

import hu.gerviba.chatclient.engine.ChatClient;
import hu.gerviba.chatclient.util.Util;

public class PreWindow {

	private JFrame frame;
	private JPanel panel;
	private volatile JLabel statusLabel; // TODO: Check
	private JButton loginButton;
	private JButton registerButton;
	private volatile boolean login;

	private HashMap<String, JTextField> components = new HashMap<>();

	public PreWindow() {
		initWindow();
	}

	private void initWindow() {
		frame = new JFrame("Chat Client " + ChatClient.VERSION);
		frame.setSize(300, 438);
		frame.setLayout(new GridLayout(1, 1));
		Dimension dimension = Toolkit.getDefaultToolkit().getScreenSize();
		frame.setLocation((int) ((dimension.getWidth() - frame.getWidth()) / 2),
				(int) ((dimension.getHeight() - frame.getHeight()) / 2));
		frame.setResizable(false);
		frame.setIconImage(Toolkit.getDefaultToolkit().getImage(getClass().getResource("/icon.ico")));
		frame.setType(Type.UTILITY);

		/*
		 * try {
		 * UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		 * MetalLookAndFeel.setCurrentTheme(new DefaultMetalTheme()); } catch
		 * (Exception e) { e.printStackTrace(); }
		 */
		WebLookAndFeel.install();

		frame.addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent windowEvent) {
				System.exit(0);
			}
		});
		panel = new JPanel();
		panel.setLayout(null);

		frame.add(panel);
		frame.setVisible(true);
	}

	public void init() {

		statusLabel = new JLabel("If you like it, please fork me on Github", SwingConstants.CENTER);
		statusLabel.setFont(new Font(statusLabel.getFont().getFontName(), Font.BOLD, statusLabel.getFont().getSize()));
		statusLabel.setBounds(10, 378, 270, 30);
		panel.add(statusLabel);

		KeyListener logListener = new KeyListener() {

			@Override
			public void keyTyped(KeyEvent e) {
			}

			@Override
			public void keyReleased(KeyEvent e) {
				if (e.getKeyCode() == 10) {
					login = true;
					ChatWindow cw = new ChatWindow();
					cw.init();

					String host;
					int port;
					if (components.get("logHost").getText().indexOf(':') == -1) {
						host = components.get("logHost").getText();
						port = 4000;
					} else {
						host = components.get("logHost").getText().substring(0,
								components.get("logHost").getText().indexOf(':'));
						port = Util.parseInt(components.get("logHost").getText().substring(
								components.get("logHost").getText().indexOf(':') + 1,
								components.get("logHost").getText().length()), 4000);
					}

					new Thread() {
						@Override
						public void run() {
							ChatClient cc = new ChatClient(host, port,
									components.get("logUser").getText().replace(" ", "_"),
									components.get("logPass").getText());
							cc.initGUI(cw, PreWindow.this, "");
						}
					}.start();
				}
			}

			@Override
			public void keyPressed(KeyEvent e) {
			}
		};

		final JLabel loginLabel = new JLabel("Login:");
		loginLabel.setFont(new Font(loginLabel.getFont().getFontName(), Font.BOLD, loginLabel.getFont().getSize()));
		loginLabel.setBounds(10, 10, 70, 30);

		final JLabel logUserLabel = new JLabel("Username");
		final JLabel logPassLabel = new JLabel("Password");
		final JLabel logHostLabel = new JLabel("Server");

		logUserLabel.setBounds(10, 40, 70, 30);
		logPassLabel.setBounds(10, 70, 70, 30);
		logHostLabel.setBounds(10, 100, 70, 30);

		final JTextField logUserText = new JTextField(6);
		final JPasswordField logPassText = new JPasswordField(6);
		final JTextField logHostText = new JTextField(6);

		logUserText.setBounds(80, 42, 200, 30);
		logUserText.addKeyListener(logListener);
		logPassText.setBounds(80, 72, 200, 30);
		logPassText.addKeyListener(logListener);
		logHostText.setBounds(80, 102, 200, 30);
		logHostText.addKeyListener(logListener);

		KeyListener regListener = new KeyListener() {

			@Override
			public void keyTyped(KeyEvent e) {
			}

			@Override
			public void keyReleased(KeyEvent e) {
				if (e.getKeyCode() == 10) {
					login = false;
					ChatWindow cw = new ChatWindow();
					cw.init();

					String host;
					int port;
					if (components.get("regHost").getText().indexOf(':') == -1) {
						host = components.get("regHost").getText();
						port = 4000;
					} else {
						host = components.get("regHost").getText().substring(0,
								components.get("regHost").getText().indexOf(':'));
						port = Util.parseInt(components.get("regHost").getText().substring(
								components.get("regHost").getText().indexOf(':') + 1,
								components.get("regHost").getText().length()), 4000);
					}

					new Thread() {
						@Override
						public void run() {
							ChatClient cc = new ChatClient(host, port,
									components.get("regUser").getText().replace(" ", "_"),
									components.get("regPass").getText());
							cc.initGUI(cw, PreWindow.this, "-r m:" + components.get("regMail").getText() + " n:"
									+ components.get("regNick").getText());
						}
					}.start();
				}
			}

			@Override
			public void keyPressed(KeyEvent e) {
			}
		};
		
		final JLabel registerLabel = new JLabel("Register:");
		registerLabel.setFont(new Font(registerLabel.getFont().getFontName(), Font.BOLD, registerLabel.getFont().getSize()));
		registerLabel.setBounds(10, 160, 70, 30);

		final JLabel regUserLabel = new JLabel("Username");
		final JLabel regPassLabel = new JLabel("Password");
		final JLabel regNickLabel = new JLabel("Nickname");
		final JLabel regMailLabel = new JLabel("Email");
		final JLabel regHostLabel = new JLabel("Server");

		regUserLabel.setBounds(10, 190, 70, 30);
		regUserLabel.addKeyListener(regListener);
		regPassLabel.setBounds(10, 220, 70, 30);
		regPassLabel.addKeyListener(regListener);
		regNickLabel.setBounds(10, 250, 70, 30);
		regNickLabel.addKeyListener(regListener);
		regMailLabel.setBounds(10, 280, 70, 30);
		regMailLabel.addKeyListener(regListener);
		regHostLabel.setBounds(10, 310, 70, 30);
		regHostLabel.addKeyListener(regListener);

		final JTextField regUserText = new JTextField();
		final JPasswordField regPassText = new JPasswordField();
		final JTextField regNickText = new JTextField();
		final JTextField regMailText = new JTextField();
		final JTextField regHostText = new JTextField();

		regUserText.setBounds(80, 192, 200, 30);
		regPassText.setBounds(80, 222, 200, 30);
		regNickText.setBounds(80, 252, 200, 30);
		regMailText.setBounds(80, 282, 200, 30);
		regHostText.setBounds(80, 312, 200, 30);

		loginButton = new JButton("Log in");
		registerButton = new JButton("Register");

		loginButton.setActionCommand("LOGIN");
		registerButton.setActionCommand("REGISTER");

		loginButton.setBounds(210, 134, 70, 30);
		registerButton.setBounds(210, 344, 70, 30);

		loginButton.addActionListener(new ButtonClickListener());
		registerButton.addActionListener(new ButtonClickListener());

		logHostText.setText(System.getProperty("host", "127.0.0.1"));
		regHostText.setText(System.getProperty("host", "127.0.0.1"));
		logUserText.setText(System.getProperty("user", ""));

		panel.add(loginLabel);
		panel.add(logUserLabel);
		panel.add(logUserText);
		panel.add(logPassLabel);
		panel.add(logPassText);
		panel.add(logHostLabel);
		panel.add(logHostText);
		panel.add(loginButton);

		panel.add(registerLabel);
		panel.add(regUserLabel);
		panel.add(regUserText);
		panel.add(regPassLabel);
		panel.add(regPassText);
		panel.add(regNickLabel);
		panel.add(regNickText);
		panel.add(regMailLabel);
		panel.add(regMailText);
		panel.add(regHostLabel);
		panel.add(regHostText);
		panel.add(registerButton);

		components.put("logUser", logUserText);
		components.put("logPass", logPassText);
		components.put("logHost", logHostText);

		components.put("regUser", regUserText);
		components.put("regPass", regPassText);
		components.put("regNick", regNickText);
		components.put("regMail", regMailText);
		components.put("regHost", regHostText);

		panel.repaint();
	}

	private class ButtonClickListener implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent e) {
			String command = e.getActionCommand();

			statusLabel.setText("Connecting...");
			setAllEnable(false);

			if (command.equals("LOGIN")) {
				login = true;
				ChatWindow cw = new ChatWindow();
				cw.init();

				String host;
				int port;
				if (components.get("logHost").getText().indexOf(':') == -1) {
					host = components.get("logHost").getText();
					port = 4000;
				} else {
					host = components.get("logHost").getText().substring(0,
							components.get("logHost").getText().indexOf(':'));
					port = Util.parseInt(components.get("logHost").getText().substring(
							components.get("logHost").getText().indexOf(':') + 1,
							components.get("logHost").getText().length()), 4000);
				}

				new Thread() {
					@Override
					public void run() {
						ChatClient cc = new ChatClient(host, port,
								components.get("logUser").getText().replace(" ", "_"),
								components.get("logPass").getText());
						cc.initGUI(cw, PreWindow.this, "");
					}
				}.start();
			} else {
				login = false;
				ChatWindow cw = new ChatWindow();
				cw.init();

				String host;
				int port;
				if (components.get("regHost").getText().indexOf(':') == -1) {
					host = components.get("regHost").getText();
					port = 4000;
				} else {
					host = components.get("regHost").getText().substring(0,
							components.get("regHost").getText().indexOf(':'));
					port = Util.parseInt(components.get("regHost").getText().substring(
							components.get("regHost").getText().indexOf(':') + 1,
							components.get("regHost").getText().length()), 4000);
				}

				new Thread() {
					@Override
					public void run() {
						ChatClient cc = new ChatClient(host, port,
								components.get("regUser").getText().replace(" ", "_"),
								components.get("regPass").getText());
						cc.initGUI(cw, PreWindow.this, "-r m:" + components.get("regMail").getText() + " n:"
								+ components.get("regNick").getText());
					}
				}.start();
			}
		}
	}

	public void setAllEnable(boolean enable) {
		loginButton.setEnabled(enable);
		registerButton.setEnabled(enable);
		for (JTextField jtf : components.values()) {
			jtf.setEnabled(enable);
		}
	}

	public JFrame getFrame() {
		return frame;
	}

	public void setStatus(String status) {
		statusLabel.setText(status);
	}

	public void performReconnect(String param) {
		if (login) {
			ChatWindow cw = new ChatWindow();
			cw.init();

			String host;
			int port;
			if (components.get("logHost").getText().indexOf(':') == -1) {
				host = components.get("logHost").getText();
				port = 4000;
			} else {
				host = components.get("logHost").getText().substring(0,
						components.get("logHost").getText().indexOf(':'));
				port = Util.parseInt(components.get("logHost").getText().substring(
						components.get("logHost").getText().indexOf(':') + 1,
						components.get("logHost").getText().length()), 4000);
			}

			new Thread() {
				@Override
				public void run() {
					ChatClient cc = new ChatClient(host, port, components.get("logUser").getText().replace(" ", "_"),
							components.get("logPass").getText());
					cc.initGUI(cw, PreWindow.this, param);
				}
			}.start();
		} else {
			ChatWindow cw = new ChatWindow();
			cw.init();

			String host;
			int port;
			if (components.get("regHost").getText().indexOf(':') == -1) {
				host = components.get("regHost").getText();
				port = 4000;
			} else {
				host = components.get("regHost").getText().substring(0,
						components.get("regHost").getText().indexOf(':'));
				port = Util.parseInt(components.get("regHost").getText().substring(
						components.get("regHost").getText().indexOf(':') + 1,
						components.get("regHost").getText().length()), 4000);
			}

			new Thread() {
				@Override
				public void run() {
					ChatClient cc = new ChatClient(host, port, components.get("regUser").getText().replace(" ", "_"),
							components.get("regPass").getText());
					cc.initGUI(cw, PreWindow.this, param + " -r m:" + components.get("regMail").getText() + " n:"
							+ components.get("regNick").getText());
				}
			}.start();
		}
	}

}
