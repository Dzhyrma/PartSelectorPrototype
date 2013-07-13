package com.techsoft.partselector.ui.frame;

import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.util.Enumeration;

import javax.swing.GroupLayout;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JSplitPane;
import javax.swing.UIManager;
import javax.swing.plaf.FontUIResource;

import com.techsoft.partselector.ui.menu.MainMenuBar;
import com.techsoft.partselector.ui.panel.KnowledgePanel;
import com.techsoft.partselector.ui.panel.LibraryPanel;
import com.techsoft.partselector.ui.panel.RuleBasedSelectorPanel;

/**
 * @author    Andrii Dzhyrma
 * @uml.dependency   supplier="com.techsoft.partselector.ui.menu.MainMenuBar"
 */
public class MainFrame extends JFrame implements WindowListener {

	private static final long serialVersionUID = 8073600424665819047L;

	/**
     * @uml.property  name="libraryPanel"
     * @uml.associationEnd  
     */
	private LibraryPanel libraryPanel;
	/**
     * @uml.property  name="knowledgePanel"
     * @uml.associationEnd  
     */
	private KnowledgePanel knowledgePanel;
	/**
     * @uml.property  name="ruleBasedSelector"
     * @uml.associationEnd  
     */
	private RuleBasedSelectorPanel ruleBasedSelector;

	public MainFrame() {
		this.initFrame();

		GroupLayout layout = new GroupLayout(this.getContentPane());
		getContentPane().setLayout(layout);
		layout.setAutoCreateGaps(true);
		layout.setAutoCreateContainerGaps(true);

		this.libraryPanel = new LibraryPanel(this);
		this.knowledgePanel = new KnowledgePanel(this);
		this.ruleBasedSelector = new RuleBasedSelectorPanel(this);
		JSplitPane splitPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT, this.ruleBasedSelector, this.knowledgePanel);
		splitPane.setOneTouchExpandable(true);
		splitPane.setDividerLocation(300);
		splitPane.setBorder(null);

		layout.setHorizontalGroup(layout.createSequentialGroup().addComponent(this.libraryPanel).addComponent(splitPane));
		layout.setVerticalGroup(layout.createSequentialGroup().addGroup(
		                layout.createParallelGroup(GroupLayout.Alignment.LEADING).addComponent(this.libraryPanel).addComponent(splitPane)));
	}

	private void initFrame() {
		this.setUIFont(new FontUIResource("Arial", Font.PLAIN, 12));
		this.setSize(new Dimension(1080, 900));
		this.setResizable(false);
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
