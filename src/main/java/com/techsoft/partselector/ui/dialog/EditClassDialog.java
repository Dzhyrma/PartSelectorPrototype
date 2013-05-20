package com.techsoft.partselector.ui.dialog;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.Frame;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.AbstractCellEditor;
import javax.swing.BorderFactory;
import javax.swing.DefaultCellEditor;
import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.border.EtchedBorder;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.TableCellEditor;
import javax.swing.table.TableCellRenderer;

import com.techsoft.partselector.model.Part;
import com.techsoft.partselector.ui.util.ImageUtility;
import com.techsoft.partselector.util.reflect.ClassGenerator;
import com.techsoft.partselector.util.reflect.ClassReader;

public class EditClassDialog extends JDialog implements ActionListener {

	private class ButtonCellEditor extends AbstractCellEditor implements TableCellRenderer, TableCellEditor, ActionListener {

		private static final long serialVersionUID = -1863943625553505285L;

		private JButton deleteButton;
		private int row;

		public ButtonCellEditor() {
			this.deleteButton = new JButton(ImageUtility.createImageIcon("cross.png"));
			this.deleteButton.addActionListener(this);
		}

		@Override
		public void actionPerformed(ActionEvent e) {
			EditClassDialog.this.fieldsTable.getCellEditor().stopCellEditing();
			EditClassDialog.this.fieldsData.remove(this.row);
		}

		@Override
		public Object getCellEditorValue() {
			return null;
		}

		@Override
		public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column) {
			/*if (row >= EditClassDialog.this.classGenerator.getFieldNames().length)
				return null;*/
			return this.deleteButton;
		}

		@Override
		public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
			this.row = row;
			return this.deleteButton;
		}
	}

	private class MyTableModel extends AbstractTableModel {
		private static final long serialVersionUID = 1951834965819650766L;
		private String[] columnNames = { "Name", "Type", "" };

		public Class<?> getColumnClass(int c) {
			if (c == 1)
				return Class.class;
			if (c == 2)
				return ButtonCellEditor.class;
			return getValueAt(0, c).getClass();
		}

		public int getColumnCount() {
			return columnNames.length;
		}

		public String getColumnName(int col) {
			return this.columnNames[col];
		}

		public int getRowCount() {
			return EditClassDialog.this.classGenerator.getFieldNames().length;
		}

		public Object getValueAt(int row, int col) {
			if (col == 0)
				return EditClassDialog.this.classGenerator.getFieldNames()[row];
			if (col == 1)
				return EditClassDialog.this.classGenerator.getFieldType(EditClassDialog.this.classGenerator.getFieldNames()[row]);
			return null;
		}

		public boolean isCellEditable(int row, int col) {
			return true;
		}

		public void remove(int row) {
			EditClassDialog.this.classGenerator.removeField(EditClassDialog.this.classGenerator.getFieldNames()[row]);
			this.fireTableRowsDeleted(row, row);
			this.fireTableCellUpdated(row, 2);
		}

		/*
		 * Don't need to implement this method unless your table's
		 * data can change.
		 */
		public void setValueAt(Object value, int row, int col) {
			if (col == 1 && value instanceof Class<?>) {
				EditClassDialog.this.classGenerator.updateFieldType(EditClassDialog.this.classGenerator.getFieldNames()[row], (Class<?>) value);
			}
			if (col == 0 && value != null && !EditClassDialog.this.classGenerator.getFieldNames()[row].equals(value)) {
				String string = value.toString();
				if (EditClassDialog.this.classGenerator.isFieldExists(string))
					JOptionPane.showMessageDialog(EditClassDialog.this, "Field with this name already exists.", "Warning", JOptionPane.WARNING_MESSAGE);
				else if (string.length() == 0)
					JOptionPane.showMessageDialog(EditClassDialog.this, "Field name should not be empty.", "Warning", JOptionPane.WARNING_MESSAGE);
				else if (!string.matches("^[a-z][a-zA-Z0-9]*?$"))
					JOptionPane.showMessageDialog(EditClassDialog.this, "Field name is not correct.", "Warning", JOptionPane.WARNING_MESSAGE);
				else
					EditClassDialog.this.classGenerator.updateFieldName(EditClassDialog.this.classGenerator.getFieldNames()[row], string);
			}
			this.fireTableDataChanged();
		}
	}

	private static EditClassDialog dialog;
	private static final long serialVersionUID = 5168445697218784619L;

	public static void showDialog(Component locationComponent) {
		Frame frame = JOptionPane.getFrameForComponent(locationComponent);
		dialog = new EditClassDialog(frame);
		dialog.setLocationRelativeTo(locationComponent);
		dialog.setVisible(true);
	}

	/*public static void showDialog(Component locationComponent, Class<?> clazz) {
		Frame frame = JOptionPane.getFrameForComponent(locationComponent);
		dialog = new EditClassDialog(frame, clazz);
		dialog.setLocationRelativeTo(locationComponent);
		dialog.setVisible(true);
	}*/

	private JButton acceptButton, cancelButton, addFieldButton;
	private JComboBox<Class<? extends Part>> classExtendsComboBox;
	private ClassGenerator classGenerator;
	private JTextField classNameTextField;
	private MyTableModel fieldsData;
	private JTable fieldsTable;

	/*private EditClassDialog(Frame frame, Class<?> clazz) {
		super(frame, "Edit Class", true);
		this.classGenerator = new ClassGenerator(clazz);

		this.initFrame();
	}*/

	public EditClassDialog(Frame frame) {
		super(frame, "New Class", true);
		this.classGenerator = new ClassGenerator("NewClass");
		this.classGenerator.setSuperClass(Part.class);

		this.initFrame();
	}

	@SuppressWarnings("unchecked")
	private void initFrame() {
		GroupLayout layout = new GroupLayout(this.getContentPane());
		this.getContentPane().setLayout(layout);
		layout.setAutoCreateGaps(true);
		layout.setAutoCreateContainerGaps(true);

		this.setMinimumSize(new Dimension(500, 300));

		this.classNameTextField = new JTextField(this.classGenerator.getName());
		//this.classNameTextField.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(EtchedBorder.LOWERED), "Class name"));

		this.classExtendsComboBox = new JComboBox<Class<? extends Part>>();
		this.classExtendsComboBox.addItem(Part.class);
		for (String className : ClassReader.getInstance().getClassNames()) {
			try {
				Class<?> clazz = ClassReader.getInstance().loadClass(className);
				if (Part.class.isAssignableFrom(clazz))
					this.classExtendsComboBox.addItem((Class<? extends Part>) ClassReader.getInstance().loadClass(className));
			} catch (ClassNotFoundException e) {
				continue;
			}
		}
		this.classExtendsComboBox.addActionListener(this);

		this.acceptButton = new JButton("Accept");
		this.acceptButton.addActionListener(this);
		this.cancelButton = new JButton("Cancel");
		this.cancelButton.addActionListener(this);
		this.addFieldButton = new JButton("Add new field", ImageUtility.createImageIcon("plus.png"));
		this.addFieldButton.addActionListener(this);

		this.fieldsData = new MyTableModel();
		this.fieldsTable = new JTable(this.fieldsData);
		this.fieldsTable.setPreferredScrollableViewportSize(new Dimension(500, 70));
		this.fieldsTable.setFillsViewportHeight(true);
		JComboBox<Class<?>> fieldTypeComboBox = new JComboBox<Class<?>>();
		fieldTypeComboBox.addItem(String.class);
		fieldTypeComboBox.addItem(double.class);
		fieldTypeComboBox.addItem(int.class);
		ButtonCellEditor editor = new ButtonCellEditor();
		this.fieldsTable.getColumnModel().getColumn(2).setCellRenderer(editor);
		this.fieldsTable.getColumnModel().getColumn(1).setCellEditor(new DefaultCellEditor(fieldTypeComboBox));
		this.fieldsTable.getColumnModel().getColumn(2).setCellEditor(editor);
		this.fieldsTable.getColumnModel().getColumn(2).setMaxWidth(22);
		this.fieldsTable.getColumnModel().getColumn(2).setMinWidth(22);
		this.fieldsTable.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(EtchedBorder.RAISED)));

		JScrollPane scrollPane = new JScrollPane(this.fieldsTable);
		scrollPane.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(EtchedBorder.LOWERED), "Fields"));

		JLabel classNameLabel = new JLabel("Name:");
		JLabel extendsLabel = new JLabel("Extends:");

		layout.setHorizontalGroup(layout.createParallelGroup(Alignment.CENTER).addGroup(
		                layout.createSequentialGroup().addGroup(layout.createParallelGroup().addComponent(classNameLabel).addComponent(extendsLabel)).addGroup(
		                                layout.createParallelGroup().addComponent(this.classNameTextField).addComponent(this.classExtendsComboBox)))
		                .addComponent(scrollPane).addGroup(layout.createSequentialGroup().addComponent(this.acceptButton).addComponent(this.cancelButton))
		                .addComponent(this.addFieldButton));
		layout.linkSize(SwingConstants.VERTICAL, this.acceptButton, this.cancelButton, this.classNameTextField, this.classExtendsComboBox, classNameLabel,
		                extendsLabel);
		layout.setVerticalGroup(layout.createSequentialGroup().addGroup(
		                layout.createParallelGroup().addComponent(classNameLabel).addComponent(this.classNameTextField)).addGroup(
		                layout.createParallelGroup().addComponent(extendsLabel).addComponent(this.classExtendsComboBox)).addComponent(this.addFieldButton)
		                .addComponent(scrollPane).addGroup(layout.createParallelGroup().addComponent(this.acceptButton).addComponent(this.cancelButton)));

		this.pack();
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == this.acceptButton) {
			String className = this.classNameTextField.getText();
			String[] classNames = ClassReader.getInstance().getClassNames();
			for (String string : classNames)
				if (className.compareToIgnoreCase(string) == 0) {
					JOptionPane.showMessageDialog(this, "Class with this name already exists.", "Warning", JOptionPane.WARNING_MESSAGE);
					return;
				}
			if (!className.matches("[a-zA-Z_$][a-zA-Z\\d_$]*"))
				JOptionPane.showMessageDialog(this, "Class name is not correct.", "Warning", JOptionPane.WARNING_MESSAGE);
			else {
				this.classGenerator.setName(this.classNameTextField.getText());
				this.classGenerator.saveClass();
				EditClassDialog.dialog.setVisible(false);
			}
		} else if (e.getSource() == this.classExtendsComboBox) {
			this.classGenerator.setSuperClass(this.classExtendsComboBox.getItemAt(this.classExtendsComboBox.getSelectedIndex()));
			this.fieldsTable.repaint();
		} else if (e.getSource() == this.cancelButton) {
			EditClassDialog.dialog.setVisible(false);
		} else if (e.getSource() == this.addFieldButton) {
			String fieldName = "newField";
			if (this.classGenerator.isFieldExists(fieldName)) {
				int index = 1;
				String newFieldName = fieldName + index;
				while (this.classGenerator.isFieldExists(newFieldName))
					newFieldName = fieldName + ++index;
				fieldName = newFieldName;
			}
			this.classGenerator.addField(fieldName, String.class);
			this.fieldsData.fireTableDataChanged();
		}
	}
}
