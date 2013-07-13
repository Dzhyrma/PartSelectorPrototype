package com.techsoft.partselector.ui.panel;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Vector;

import javax.swing.BorderFactory;
import javax.swing.DefaultListCellRenderer;
import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.SwingConstants;
import javax.swing.border.EtchedBorder;
import javax.swing.table.AbstractTableModel;

import org.drools.KnowledgeBase;
import org.drools.KnowledgeBaseFactory;
import org.drools.builder.KnowledgeBuilder;
import org.drools.builder.KnowledgeBuilderError;
import org.drools.builder.KnowledgeBuilderErrors;
import org.drools.builder.KnowledgeBuilderFactory;
import org.drools.builder.ResourceType;
import org.drools.io.ResourceFactory;
import org.drools.runtime.StatefulKnowledgeSession;

import com.techsoft.partselector.consts.Paths;
import com.techsoft.partselector.model.AssemblyNode;
import com.techsoft.partselector.model.HashVector;
import com.techsoft.partselector.model.Part;
import com.techsoft.partselector.ui.frame.ResultFrame;
import com.techsoft.partselector.ui.util.ImageUtility;
import com.techsoft.partselector.util.comparator.RankComparator;
import com.techsoft.partselector.util.knowledgebase.HistoryKnowledgeBase;
import com.techsoft.partselector.util.knowledgebase.PartLibrary;
import com.techsoft.partselector.util.reflect.ClassReader;
import com.techsoft.partselector.util.rules.InputParameters;
import com.techsoft.partselector.util.rules.RuleLibrary;
import com.techsoft.partselector.util.rules.RuleModel;
import com.techsoft.partselector.util.rules.RuleResult;

/**
 * @author          Andrii Dzhyrma
 * @uml.dependency   supplier="com.techsoft.partselector.ui.frame.ResultFrame"
 */
public class RuleBasedSelectorPanel extends JPanel implements ActionListener {

	private static final long serialVersionUID = -6436312302252686831L;

	private final JFrame mainFrame;
	private JComboBox<RuleModel> ruleComboBox;
	private JButton fireRuleButton;
	private JTable parametersTable;
	private JList<String> classList;
	/**
     * @uml.property  name="parametersData"
     * @uml.associationEnd  
     */
	private MyTableModel parametersData;
	private KnowledgeBase knowledgeBase;

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

		this.parametersData = new MyTableModel();
		this.parametersTable = new JTable(this.parametersData);
		this.parametersTable.setPreferredScrollableViewportSize(new Dimension(500, 70));
		this.parametersTable.setFillsViewportHeight(true);
		final JScrollPane parametersScrollPane = new JScrollPane(this.parametersTable);
		parametersScrollPane.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(EtchedBorder.RAISED), "Input parameters"));

		this.ruleComboBox = new JComboBox<RuleModel>(RuleLibrary.getInstance().getRules());
		this.ruleComboBox.setPreferredSize(new Dimension(100, 20));
		this.ruleComboBox.addActionListener(this);
		//this.updateComboBox();
		this.classList = new JList<String>();
		this.classList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		this.classList.setLayoutOrientation(JList.VERTICAL);
		this.classList.setCellRenderer(new DefaultListCellRenderer() {
			private static final long serialVersionUID = 712471325928285602L;

			public Component getListCellRendererComponent(JList<?> list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
				super.getListCellRendererComponent(list, value, index, false, false);
				return this;
			}
		});
		this.classList.setVisibleRowCount(-1);
		this.classList.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(EtchedBorder.LOWERED)));
		final JScrollPane classListScroller = new JScrollPane(this.classList);
		classListScroller.setPreferredSize(new Dimension(150, 100));
		classListScroller.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(EtchedBorder.RAISED), "Pasrts to find"));

		layout.setHorizontalGroup(layout.createSequentialGroup().addGroup(
		                layout.createParallelGroup(Alignment.CENTER).addGroup(
		                                layout.createSequentialGroup().addComponent(this.ruleComboBox).addComponent(this.fireRuleButton)).addGroup(
		                                layout.createSequentialGroup().addComponent(classListScroller).addComponent(parametersScrollPane))));

		layout.linkSize(SwingConstants.VERTICAL, this.fireRuleButton, this.ruleComboBox);

		layout.setVerticalGroup(layout.createSequentialGroup().addGroup(
		                layout.createParallelGroup().addComponent(this.ruleComboBox).addComponent(this.fireRuleButton)).addGroup(
		                layout.createParallelGroup().addComponent(parametersScrollPane).addComponent(classListScroller)));

		this.updateInfo();
	}

	private boolean prepareKnowledgeBase(String path) {
		KnowledgeBuilder kbuilder = KnowledgeBuilderFactory.newKnowledgeBuilder();
		kbuilder.add(ResourceFactory.newClassPathResource(Paths.ASSEMBLING_RULES_PATH + path), ResourceType.DRL);
		KnowledgeBuilderErrors errors = kbuilder.getErrors();
		if (errors.size() > 0) {
			for (KnowledgeBuilderError error : errors)
				System.err.println(error);
			return false;
		}
		KnowledgeBase kbase = KnowledgeBaseFactory.newKnowledgeBase();
		kbase.addKnowledgePackages(kbuilder.getKnowledgePackages());
		this.knowledgeBase = kbase;
		return true;
	}

	private void updateInfo() {
		RuleModel selectedRule = this.ruleComboBox.getItemAt(this.ruleComboBox.getSelectedIndex());
		Vector<String> classesToLoad = new Vector<String>();
		for (String className : selectedRule.getClasses()) {
			classesToLoad.add(className);
		}
		this.classList.setListData(classesToLoad);
		List<String> selectedRuleParameters = selectedRule.getParameters();
		Object[][] data = new Object[selectedRuleParameters.size()][2];
		for (int i = 0; i < data.length; i++) {
			String name = selectedRuleParameters.get(i);
			data[i][0] = name;
			data[i][1] = this.newInstance(selectedRule.getParameter(name));
		}
		this.parametersData.setData(data);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == this.fireRuleButton) {
			RuleResult.getInstance().clearResults();
			RuleModel selectedRule = this.ruleComboBox.getItemAt(this.ruleComboBox.getSelectedIndex());
			String rulePath = selectedRule.getFileName();
			if (!prepareKnowledgeBase(rulePath)) {
				JOptionPane.showMessageDialog(this.mainFrame, "Could not prepare knowledge base for the rule: " + rulePath, "Rule knowledge base error",
				                JOptionPane.ERROR_MESSAGE);
				return;
			}
			StatefulKnowledgeSession ksession = this.knowledgeBase.newStatefulKnowledgeSession();
			InputParameters inputParameters = new InputParameters();
			Map<String, Object> parameters = this.parametersData.getData();
			for (String className : selectedRule.getClasses())
				try {
					parameters.put(className, ClassReader.getInstance().loadClass(className));
				} catch (ClassNotFoundException e1) {
					continue;
				}
			for (Class<?> clazz1 : ClassReader.getInstance().getClassMap().values()) {
				for (String className : selectedRule.getClasses()) {
					Class<?> clazz2 = null;
					try {
						clazz2 = ClassReader.getInstance().loadClass(className);
					} catch (ClassNotFoundException e1) {
						continue;
					}
					if (clazz2 != null && clazz1 != null && clazz2.isAssignableFrom(clazz1)) {
						Part[] parts = PartLibrary.getInstance().getParts(clazz1);
						if (parts != null)
							for (Part part : parts)
								if (part != null)
									ksession.insert(new AssemblyNode(part, null));
						break;
					}
				}
			}
			inputParameters.setParameters(parameters);
			ksession.insert(inputParameters);
			ksession.fireAllRules();
			List<HashVector> result = RuleResult.getInstance().getResults();
			if (result == null || result.size() == 0) {
				JOptionPane.showMessageDialog(this.mainFrame, "Rule based selector did not find any fitting combinations.\nTry to change input parameters.",
				                "No results", JOptionPane.INFORMATION_MESSAGE);
				return;
			}
			RankComparator<HashVector> comparator = HistoryKnowledgeBase.getInstance().getRankComparator();
			Collections.sort(result, comparator);
			new ResultFrame(result, this.mainFrame);
		} else if (e.getSource() == this.ruleComboBox) {
			this.updateInfo();
		}
	}

	private Object newInstance(Class<?> clazz) {
		if (clazz == boolean.class)
			return Boolean.valueOf(true);
		else if (clazz == byte.class)
			return Byte.valueOf((byte) 0);
		else if (clazz == char.class)
			return Character.valueOf('\0');
		else if (clazz == double.class)
			return Double.valueOf(0);
		else if (clazz == float.class)
			return Float.valueOf(0);
		else if (clazz == int.class)
			return Integer.valueOf(0);
		else if (clazz == long.class)
			return Long.valueOf(0);
		else if (clazz == short.class)
			return Short.valueOf((short) 0);
		else
			return "";
	}

	/**
     * @author  Andrii Dzhyrma 
     */
	class MyTableModel extends AbstractTableModel {
		private static final long serialVersionUID = -338367598597265732L;
		private String[] columnNames = { "Name", "Value" };
		/**
         * @uml.property  name="data"
         */
		private Object[][] data = new Object[0][2];

		public int getColumnCount() {
			return this.columnNames.length;
		}

		public int getRowCount() {
			return this.data.length;
		}

		public String getColumnName(int col) {
			return this.columnNames[col];
		}

		public Object getValueAt(int row, int col) {
			return this.data[row][col];
		}

		/*
		 * JTable uses this method to determine the default renderer/
		 * editor for each cell.  If we didn't implement this method,
		 * then the last column would contain text ("true"/"false"),
		 * rather than a check box.
		 */
		public Class<?> getColumnClass(int c) {
			return getValueAt(0, c).getClass();
		}

		public boolean isCellEditable(int row, int col) {
			//Note that the data/cell address is constant,
			//no matter where the cell appears on screen.
			if (col < 1)
				return false;
			return true;
		}

		public void setValueAt(Object value, int row, int col) {
			this.data[row][col] = value;
			super.fireTableCellUpdated(row, col);
		}

		/**
         * @param newData
         * @uml.property  name="data"
         */
		public void setData(Object[][] newData) {
        	if (newData != null && newData.length > 0 && newData[0].length == 2)
        		this.data = newData;
        	else
        		this.data = new Object[0][2];
        	super.fireTableDataChanged();
        }

		/**
         * @return
         * @uml.property  name="data"
         */
		public Map<String, Object> getData() {
        	Map<String, Object> result = new HashMap<String, Object>();
        	if (this.data == null)
        		return result;
        	for (int i = 0; i < this.data.length; i++) {
        		if (this.data[i] == null || this.data[i].length != 2)
        			continue;
        		result.put(this.data[i][0].toString(), this.data[i][1] == null ? 0 : (Number) this.data[i][1]);
        	}
        	return result;
        }
	}

}
