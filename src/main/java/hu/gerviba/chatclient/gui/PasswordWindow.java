package hu.gerviba.chatclient.gui;

import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.Toolkit;
import java.awt.Window.Type;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPasswordField;

import com.alee.laf.WebLookAndFeel;

import hu.gerviba.chatclient.util.Callback;

public class PasswordWindow {

	private JFrame frame;
	private JPanel panel;
	private JLabel title; 
	private JPasswordField pass;
	private Callback event;
	
	public PasswordWindow() {
		initWindow();
	}

	private void initWindow() {
		frame = new JFrame("Enter the password to get access");
		frame.setSize(400, 140);
		frame.setLayout(new GridLayout(1, 1));
		Dimension dimension = Toolkit.getDefaultToolkit().getScreenSize();
		frame.setLocation((int) ((dimension.getWidth() - frame.getWidth()) / 2),
				(int) ((dimension.getHeight() - frame.getHeight()) / 2));
		frame.setResizable(false);
		frame.setType(Type.UTILITY);
		frame.setAutoRequestFocus(true);
		
		/*
		 * try {
		 * UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		 * MetalLookAndFeel.setCurrentTheme(new DefaultMetalTheme()); } catch
		 * (Exception e) { e.printStackTrace(); }
		 */
		WebLookAndFeel.install();

		frame.addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent windowEvent) {
				event.call(null);
				frame.dispose();
			}
		});
		panel = new JPanel();
		panel.setLayout(null);
		
		title = new JLabel();
		title.setBounds(10, 10, 376, 30);
		panel.add(title);
		
		pass = new JPasswordField();
		pass.setBounds(10, 40, 376, 30);
		pass.addKeyListener(new KeyListener() {
			
			@Override
			public void keyTyped(KeyEvent e) {}
			
			@Override
			public void keyReleased(KeyEvent e) {
				if(e.getKeyCode() == 10) {
					event.call(new String(pass.getPassword()));
					frame.dispose();
				}
			}
			
			@Override
			public void keyPressed(KeyEvent e) {}
		});
		panel.add(pass);
		
		final JButton button = new JButton(">>");
		button.setBounds(290, 74, 96, 30);
		button.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				event.call(new String(pass.getPassword()));
				frame.dispose();
			}
		});
		panel.add(button);
		
		frame.add(panel);
	}
	
	public void show(String title, Callback event) {
		this.title.setText(title);
		this.event = event;
		frame.setVisible(true);
		//frame.requestFocus();
	}
	
}
