package com.techsoft.partselector.ui;

import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.util.Enumeration;

import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.UIManager;
import javax.swing.plaf.FontUIResource;

public class MainFrame extends JFrame implements WindowListener, ActionListener {

	private static final long serialVersionUID = 8073600424665819047L;

	private LibraryPanel libraryPanel;

	public MainFrame() {
		this.initFrame();

		GroupLayout layout = new GroupLayout(getContentPane());
		getContentPane().setLayout(layout);
		layout.setAutoCreateGaps(true);
		layout.setAutoCreateContainerGaps(true);

		this.libraryPanel = new LibraryPanel();
		layout.setHorizontalGroup(layout.createSequentialGroup()
						.addGroup(layout.createParallelGroup(Alignment.LEADING)
							.addComponent(this.libraryPanel)));
		layout.setVerticalGroup(layout.createSequentialGroup()
						.addGroup(layout.createParallelGroup(Alignment.LEADING)
							.addComponent(this.libraryPanel)));
		
		//layout.linkSize(SwingConstants.HORIZONTAL, findButton, cancelButton);
	}

	private void initFrame() {
		this.setUIFont(new FontUIResource("Arial", Font.PLAIN, 12));
		this.setSize(new Dimension(800, 640));
		this.setLocationRelativeTo(null);
		this.setTitle("Part Selector");
		this.setJMenuBar(new MainMenuBar());
		this.addWindowListener(this);
		this.setVisible(true);
	}

	private void setUIFont(FontUIResource f) {
		Enumeration<?> keys = UIManager.getDefaults().keys();
		while (keys.hasMoreElements()) {
			Object key = keys.nextElement();
			Object value = UIManager.get(key);
			if (value != null && value instanceof FontUIResource)
				UIManager.put(key, f);
		}
	}

	@Override
    public void actionPerformed(ActionEvent e) {
	    // TODO Auto-generated method stub
	    
    }

	@Override
	public void windowActivated(WindowEvent e) {
	}

	@Override
	public void windowClosed(WindowEvent e) {
	}

	@Override
	public void windowClosing(WindowEvent e) {
		int result = JOptionPane.showConfirmDialog(this, "Are you sure you want to exit the application?", "Exit Application", JOptionPane.YES_NO_OPTION);
		if (result == JOptionPane.YES_OPTION)
			this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		else
			this.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
	}

	@Override
	public void windowDeactivated(WindowEvent e) {
	}

	@Override
	public void windowDeiconified(WindowEvent e) {
	}

	@Override
	public void windowIconified(WindowEvent e) {
	}

	@Override
	public void windowOpened(WindowEvent e) {
	}
}
