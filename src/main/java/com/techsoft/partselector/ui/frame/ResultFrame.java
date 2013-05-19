package com.techsoft.partselector.ui.frame;

import java.awt.Dimension;
import java.awt.Font;
import java.util.Enumeration;
import java.util.List;
import java.util.Map;

import javax.swing.GroupLayout;
import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.UIManager;
import javax.swing.plaf.FontUIResource;
import javax.swing.table.AbstractTableModel;

import com.techsoft.partselector.model.HashVector;
import com.techsoft.partselector.util.knowledgebase.HistoryKnowledgeBase;

public class ResultFrame extends JFrame {

	private static final long serialVersionUID = 5103221892277231207L;

	private List<HashVector> result;
	private JFrame mainFrame;

	public ResultFrame(List<HashVector> result, JFrame mainFrame) {
		this.result = result;
		this.mainFrame = mainFrame;
		initFrame();

		GroupLayout layout = new GroupLayout(this.getContentPane());
		this.getContentPane().setLayout(layout);
		layout.setAutoCreateGaps(true);
		layout.setAutoCreateContainerGaps(true);

		JTable table = new JTable(new MyTableModel(this.result));
		table.setPreferredScrollableViewportSize(new Dimension(500, 70));
		table.getColumnModel().getColumn(0).setMaxWidth(100);
		table.setFillsViewportHeight(true);
		table.setAutoCreateRowSorter(true);

		//Create the scroll pane and add the table to it.
		JScrollPane scrollPane = new JScrollPane(table);

		layout.setHorizontalGroup(layout.createSequentialGroup().addComponent(scrollPane));
		layout.setVerticalGroup(layout.createSequentialGroup().addComponent(scrollPane));
	}

	class MyTableModel extends AbstractTableModel {
		private static final long serialVersionUID = 1951834965819650766L;
		private String[] columnNames = { "Frequency", "Combination" };
		private Object[][] data;

		public MyTableModel(List<HashVector> result) {
			Map<HashVector, Integer> frequency = HistoryKnowledgeBase.getInstance().getFrequencyMap();
			if (result == null) {
				this.data = new Object[0][2];
				return;
			}
			this.data = new Object[result.size()][2];
			for (int i = 0; i < this.data.length; i++) {
				HashVector hashVector = result.get(i);
				Integer freq = frequency.get(hashVector);
				this.data[i][0] = freq == null ? Integer.valueOf(0) : freq;
				this.data[i][1] = hashVector.toString();
			}
		}

		public int getColumnCount() {
			return columnNames.length;
		}

		public int getRowCount() {
			return data.length;
		}

		public String getColumnName(int col) {
			return columnNames[col];
		}

		public Object getValueAt(int row, int col) {
			return data[row][col];
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

		/*
		 * Don't need to implement this method unless your table's
		 * editable.
		 */
		public boolean isCellEditable(int row, int col) {
			//Note that the data/cell address is constant,
			//no matter where the cell appears onscreen.
			if (col < 2) {
				return false;
			} else {
				return true;
			}
		}

		/*
		 * Don't need to implement this method unless your table's
		 * data can change.
		 */
		public void setValueAt(Object value, int row, int col) {
			data[row][col] = value;
		}
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

	private void initFrame() {
		this.setUIFont(new FontUIResource("Arial", Font.PLAIN, 12));
		this.setMinimumSize(new Dimension(850, 500));
		this.setLocationRelativeTo(this.mainFrame);
		this.setTitle("Results");
		//this.pack();
		this.setVisible(true);
	}
}
