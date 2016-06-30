package hu.gerviba.chatclient.gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.Toolkit;
import java.awt.Window.Type;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.text.DefaultCaret;

import com.alee.laf.WebLookAndFeel;

import hu.gerviba.chatclient.engine.ChatClient;

public class ChatWindow {
	
	private JFrame frame;
	private JPanel panel;
	
	private volatile JTextArea area;
	private JTextField field;
	private volatile JLabel room;
	private boolean first = true;
	
	public ChatWindow() {
		initWindow();
	}

	private void initWindow() {
		frame = new JFrame("Chat Client "+ChatClient.VERSION);
		frame.setSize(800, 600);
		frame.setLayout(new GridLayout(1, 1));
		Dimension dimension = Toolkit.getDefaultToolkit().getScreenSize();
		frame.setLocation((int) ((dimension.getWidth() - frame.getWidth()) / 2),
				(int) ((dimension.getHeight() - frame.getHeight()) / 2));
		frame.setIconImage(Toolkit.getDefaultToolkit().getImage(getClass().getResource("/icon.ico")));
		frame.setType(Type.NORMAL);
		frame.getContentPane().setBackground(new Color(0, 0, 0));
		
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
		panel.setLayout(new BorderLayout());
		
		frame.add(panel);
		frame.setVisible(false);
	}
	
	public void init() {
		area = new JTextArea();
		area.setTabSize(8);
		area.setBackground(new Color(0, 0, 0));
		area.setForeground(new Color(200, 200, 200));
		area.setFont(new Font("Courier New", Font.PLAIN, 17));
		area.setBorder(null);
		area.setEditable(false);
		area.setLineWrap(true);
		area.setBorder(null);
		DefaultCaret caret = (DefaultCaret) area.getCaret();
	    caret.setUpdatePolicy(DefaultCaret.OUT_BOTTOM);
		
	    JScrollPane areaScroll = new JScrollPane();
	    areaScroll.setViewportView(area);
	    areaScroll.setBorder(null);
		
		field = new JTextField();
		field.setFont(new Font("Courier New", Font.PLAIN, 17));
		field.addKeyListener(new KeyListener() {
			
			@Override
			public void keyTyped(KeyEvent e) {}
			
			@Override
			public void keyReleased(KeyEvent e) {
				if(e.getKeyCode() == 10) {
					ChatClient.getInstance().sendMessage(field.getText());
					field.setText("");
				}
			}
			
			@Override
			public void keyPressed(KeyEvent e) {}
		});
		field.setBorder(null);
		
		room = new JLabel(" Connecting ");
		room.setFont(new Font(room.getFont().getFontName(), Font.BOLD, room.getFont().getSize()));
		room.setFont(new Font("Courier New", Font.PLAIN, 17));
		
		panel.add(areaScroll, BorderLayout.CENTER);
		
		JPanel bottomPanel = new JPanel();
		bottomPanel.setLayout(new BorderLayout());
		bottomPanel.add(room, BorderLayout.LINE_START);
		bottomPanel.add(field, BorderLayout.CENTER);
		
		panel.add(bottomPanel, BorderLayout.PAGE_END);

		frame.repaint();
		areaScroll.repaint();
	}

	public void addLine(String msg) {
		if(first) {
			area.setText(area.getText()+msg);
			first = false;
		} else {
			area.setText(area.getText()+"\r\n"+msg);
		}
	}
	
	public void setRoom(String room) {
		this.room.setText(" ("+room+"): ");
	}

	public void setVisible() {
		frame.setVisible(true);
		frame.repaint();
	}

	public void dispose() {
		frame.dispose();
	}

	public void focus() {
		frame.requestFocus();
		Toolkit.getDefaultToolkit().beep();
	}
	
}
