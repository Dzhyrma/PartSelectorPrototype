package com.techsoft.partselector.ui.panel;

import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FilenameFilter;
import java.util.Map;

import javax.swing.BorderFactory;
import javax.swing.GroupLayout;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.SwingConstants;
import javax.swing.border.EtchedBorder;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import com.techsoft.partselector.consts.Paths;
import com.techsoft.partselector.model.Assembly;
import com.techsoft.partselector.model.HashVector;
import com.techsoft.partselector.ui.util.ImageUtility;
import com.techsoft.partselector.util.io.AssemblyReader;
import com.techsoft.partselector.util.io.ExtensionFileFilter;
import com.techsoft.partselector.util.knowledgebase.KnowledgeBase;
import com.techsoft.partselector.util.knowledgebase.KnowledgeBaseExtractor;

public class KnowledgePanel extends JPanel implements ActionListener, ListSelectionListener {

	private static final long serialVersionUID = 4107476292153220384L;

	private JButton loadNewAssemblyButton, fireExtractingRuleButton, addResultButton;
	private final JFrame mainFrame;
	private JFileChooser fileChooser;
	private JTextArea assemblyTextArea, resultTextArea;
	private Assembly assembly;
	private JComboBox<String> ruleComboBox;
	private Map<HashVector, Integer> fireResultMap;

	public KnowledgePanel(JFrame mainFrame) {
		this.mainFrame = mainFrame;
		this.fileChooser = new JFileChooser();

		GroupLayout layout = new GroupLayout(this);
		this.setLayout(layout);
		layout.setAutoCreateGaps(true);
		layout.setAutoCreateContainerGaps(true);
		this.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(EtchedBorder.LOWERED), "Knowledge Extractor"));
		this.setMinimumSize(new Dimension(100, 300));

		this.loadNewAssemblyButton = new JButton("Load new assembly", ImageUtility.createImageIcon("arrow.png"));
		this.loadNewAssemblyButton.addActionListener(this);
		this.fireExtractingRuleButton = new JButton("Fire Rule", ImageUtility.createImageIcon("fire.png"));
		this.fireExtractingRuleButton.addActionListener(this);
		this.fireExtractingRuleButton.setEnabled(false);
		this.addResultButton = new JButton("Add result to the Knowledge Base", ImageUtility.createImageIcon("gear--plus.png"));
		this.addResultButton.addActionListener(this);
		this.addResultButton.setEnabled(false);

		this.assemblyTextArea = new JTextArea();
		this.assemblyTextArea.setEditable(false);
		this.assemblyTextArea.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(EtchedBorder.LOWERED)));
		JScrollPane assemblyTextAreaScroller = new JScrollPane(this.assemblyTextArea);
		assemblyTextAreaScroller.setPreferredSize(new Dimension(150, 100));
		assemblyTextAreaScroller.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(EtchedBorder.RAISED), "Assembly parsing result"));
		this.resultTextArea = new JTextArea();
		this.resultTextArea.setEditable(false);
		this.resultTextArea.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(EtchedBorder.LOWERED)));
		JScrollPane resultTextAreaScroller = new JScrollPane(this.resultTextArea);
		resultTextAreaScroller.setPreferredSize(new Dimension(150, 100));
		resultTextAreaScroller.setBorder(BorderFactory
		                .createTitledBorder(BorderFactory.createEtchedBorder(EtchedBorder.RAISED), "Extracting rule fire results"));

		this.ruleComboBox = new JComboBox<String>();
		this.ruleComboBox.setPreferredSize(new Dimension(100, 20));
		this.updateComboBox();

		layout.setHorizontalGroup(layout.createSequentialGroup().addGroup(
		                layout.createParallelGroup().addComponent(this.loadNewAssemblyButton).addComponent(assemblyTextAreaScroller, 300, 300, 300)).addGroup(
		                layout.createParallelGroup().addComponent(this.fireExtractingRuleButton).addComponent(this.ruleComboBox).addComponent(
		                                resultTextAreaScroller).addComponent(this.addResultButton)));

		layout.linkSize(SwingConstants.VERTICAL, this.loadNewAssemblyButton, this.fireExtractingRuleButton, this.ruleComboBox);
		layout.linkSize(SwingConstants.HORIZONTAL, this.loadNewAssemblyButton, this.fireExtractingRuleButton, this.ruleComboBox, this.addResultButton,
		                assemblyTextAreaScroller, resultTextAreaScroller);

		layout.setVerticalGroup(layout.createSequentialGroup().addGroup(
		                layout.createParallelGroup().addComponent(this.loadNewAssemblyButton).addComponent(this.fireExtractingRuleButton)).addGroup(
		                layout.createParallelGroup().addComponent(assemblyTextAreaScroller).addGroup(
		                                layout.createSequentialGroup().addComponent(this.ruleComboBox).addComponent(this.addResultButton).addComponent(
		                                                resultTextAreaScroller))));
	}

	private void updateComboBox() {
		File dir = new File(Paths.RULES_PATH + Paths.EXTRACTION_RULES_PATH);
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
		if (e.getSource() == this.loadNewAssemblyButton) {
			this.fileChooser.setFileFilter(new ExtensionFileFilter("xml", "Assemblies in the *.xml format"));
			int result = this.fileChooser.showOpenDialog(this.mainFrame);
			if (result == JFileChooser.APPROVE_OPTION) {
				this.assembly = AssemblyReader.readAssemblyFromXML(this.fileChooser.getSelectedFile());
				if (this.assembly != null)
					this.fireExtractingRuleButton.setEnabled(true);
				else
					this.fireExtractingRuleButton.setEnabled(false);
				this.assemblyTextArea.setText(this.assembly.toString());
			}
		} else if (e.getSource() == this.fireExtractingRuleButton) {
			KnowledgeBaseExtractor knowledgeBaseExtractor = new KnowledgeBaseExtractor();
			this.fireResultMap = knowledgeBaseExtractor.extractKnowledge(this.assembly, this.ruleComboBox.getItemAt(this.ruleComboBox.getSelectedIndex()));
			if (this.fireResultMap == null || this.fireResultMap.size() == 0)
				this.addResultButton.setEnabled(false);
			else
				this.addResultButton.setEnabled(true);
			StringBuilder resultText = new StringBuilder();
			for (HashVector hashVector : this.fireResultMap.keySet())
				resultText.append('(').append(this.fireResultMap.get(hashVector)).append(")\n").append(hashVector).append('\n');
			this.resultTextArea.setText(resultText.toString());
		} else if (e.getSource() == this.addResultButton) {
			KnowledgeBase.getInstance().update(this.fireResultMap);
			this.fireResultMap = null;
			this.resultTextArea.setText("");
			this.addResultButton.setEnabled(false);
			JOptionPane.showMessageDialog(this.mainFrame, "Extracted results were added successfully!", "Knowledge Base Update",
			                JOptionPane.INFORMATION_MESSAGE);
		}
	}

	@Override
	public void valueChanged(ListSelectionEvent e) {
		// TODO Auto-generated method stub

	}

}
