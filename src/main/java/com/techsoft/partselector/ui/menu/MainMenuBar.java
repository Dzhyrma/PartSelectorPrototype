package com.techsoft.partselector.ui.menu;

import java.awt.Frame;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.awt.event.WindowEvent;

import javax.swing.AbstractAction;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.KeyStroke;

public class MainMenuBar extends JMenuBar{

	private static final long serialVersionUID = -663546014787181055L;

	private JMenuItem exitMenuItem;

	public MainMenuBar() {
		JMenu fileMenu = new JMenu("File");
		fileMenu.setMnemonic(KeyEvent.VK_F);
		fileMenu.getAccessibleContext().setAccessibleDescription("The only menu in this program that has menu items");

		this.exitMenuItem = new JMenuItem("Exit", KeyEvent.VK_X);
		this.exitMenuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_F4, ActionEvent.ALT_MASK));
		this.exitMenuItem.addActionListener(new AbstractAction() {

			private static final long serialVersionUID = -2626940174833563739L;

			@Override
			public void actionPerformed(ActionEvent e) {
				for (Frame frame : Frame.getFrames()) {
					if (frame.isActive()) {
						WindowEvent windowClosing = new WindowEvent(frame, WindowEvent.WINDOW_CLOSING);
						frame.dispatchEvent(windowClosing);
					}
				}
			}

		});
		fileMenu.add(this.exitMenuItem);

		this.add(fileMenu);
	}
}
