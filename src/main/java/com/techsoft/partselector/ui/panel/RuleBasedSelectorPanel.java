package com.techsoft.partselector.ui.panel;

import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FilenameFilter;
import javax.swing.BorderFactory;
import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EtchedBorder;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import com.techsoft.partselector.consts.Paths;
import com.techsoft.partselector.ui.util.ImageUtility;

public class RuleBasedSelectorPanel extends JPanel implements ActionListener, ListSelectionListener {

	private static final long serialVersionUID = -6436312302252686831L;

	private final JFrame mainFrame;
	private JComboBox<String> ruleComboBox;
	private JButton fireRuleButton;

	public RuleBasedSelectorPanel(JFrame mainFrame) {
		this.mainFrame = mainFrame;

		GroupLayout layout = new GroupLayout(this);
		this.setLayout(layout);
		layout.setAutoCreateGaps(true);
		layout.setAutoCreateContainerGaps(true);
		this.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(EtchedBorder.LOWERED), "Rule Based Selector"));
		this.setMinimumSize(new Dimension(100, 300));

		this.fireRuleButton = new JButton("Search for the part combinations", ImageUtility.createImageIcon("fire-big.png"));
		this.fireRuleButton.addActionListener(this);
		/*this.loadNewAssemblyButton = new JButton("Load new assembly", ImageUtility.createImageIcon("arrow.png"));
		this.loadNewAssemblyButton.addActionListener(this);
		this.fireExtractingRuleButton = new JButton("Fire Rule", ImageUtility.createImageIcon("fire.png"));
		this.fireExtractingRuleButton.addActionListener(this);
		this.fireExtractingRuleButton.setEnabled(false);
		this.addResultButton = new JButton("Add result to the Knowledge Base", ImageUtility.createImageIcon("gear--plus.png"));
		this.addResultButton.addActionListener(this);
		this.addResultButton.setEnabled(false);

		this.assemblyTextArea = new JTextArea();
		this.assemblyTextArea.setEditable(false);
		JScrollPane assemblyTextAreaScroller = new JScrollPane(this.assemblyTextArea);
		assemblyTextAreaScroller.setPreferredSize(new Dimension(150, 100));
		this.resultTextArea = new JTextArea();
		this.resultTextArea.setEditable(false);
		JScrollPane resultTextAreaScroller = new JScrollPane(this.resultTextArea);
		assemblyTextAreaScroller.setPreferredSize(new Dimension(150, 100));*/

		this.ruleComboBox = new JComboBox<String>();
		this.ruleComboBox.setPreferredSize(new Dimension(100, 20));
		this.updateComboBox();

		layout.setHorizontalGroup(layout.createSequentialGroup().addGroup(
		                layout.createParallelGroup(Alignment.CENTER).addComponent(this.ruleComboBox).addComponent(this.fireRuleButton)));

		layout.setVerticalGroup(layout.createSequentialGroup().addComponent(this.ruleComboBox).addComponent(this.fireRuleButton));
	}

	private void updateComboBox() {
		File dir = new File(Paths.RULES_PATH + Paths.ASSEMBLING_RULES_PATH);
		File[] fileList = dir.listFiles(new FilenameFilter() {

			@Override
			public boolean accept(File dir, String name) {
				return name.endsWith(".drl");
			}
		});
		if (fileList == null)
			return;
		for (File file : fileList)
			this.ruleComboBox.addItem(file.getName());
	}

	@Override
	public void actionPerformed(ActionEvent e) {
	}

	@Override
	public void valueChanged(ListSelectionEvent e) {
		// TODO Auto-generated method stub

	}

}
