package com.techsoft.partselector.ui.panel;

import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.Map;

import javax.swing.BorderFactory;
import javax.swing.GroupLayout;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.ListSelectionModel;
import javax.swing.GroupLayout.Alignment;
import javax.swing.SwingConstants;
import javax.swing.border.EtchedBorder;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import com.techsoft.partselector.model.Part;
import com.techsoft.partselector.ui.dialog.EditClassDialog;
import com.techsoft.partselector.ui.dialog.MappingDialog;
import com.techsoft.partselector.ui.util.ImageUtility;
import com.techsoft.partselector.util.io.FileRemover;
import com.techsoft.partselector.util.io.ExtensionFileFilter;
import com.techsoft.partselector.util.io.PartReader;
import com.techsoft.partselector.util.knowledgebase.PartLibrary;
import com.techsoft.partselector.util.reflect.ClassCompiler;
import com.techsoft.partselector.util.reflect.ClassGenerator;
import com.techsoft.partselector.util.reflect.ClassReader;

/**
 * @uml.dependency   supplier="com.techsoft.partselector.ui.dialog.MappingDialog"
 * @uml.dependency   supplier="com.techsoft.partselector.ui.dialog.EditClassDialog"
 */
public class LibraryPanel extends JPanel implements ActionListener, ListSelectionListener {

	private static final long serialVersionUID = -3389243649999461716L;

	private JButton addNewClassButton;

	private JButton addNewPartsButton;

	private JButton deleteClassButton;

	private JButton deletePartsButton;

	private JButton editClassButton;
	private JFrame mainFrame;
	private JList<String> classList;
	private JList<Part> partList;
	private JFileChooser fileChooser;

	public LibraryPanel(JFrame mainFrame) {
		this.mainFrame = mainFrame;
		this.fileChooser = new JFileChooser(new File(System.getProperty("user.dir")));

		GroupLayout layout = new GroupLayout(this);
		this.setLayout(layout);
		layout.setAutoCreateGaps(true);
		layout.setAutoCreateContainerGaps(true);
		this.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(EtchedBorder.LOWERED), "PartL Library"));

		this.addNewClassButton = new JButton("New Class", ImageUtility.createImageIcon("plus.png"));
		this.addNewClassButton.addActionListener(this);
		this.addNewPartsButton = new JButton("New Parts", ImageUtility.createImageIcon("plus.png"));
		this.addNewPartsButton.addActionListener(this);
		this.deleteClassButton = new JButton("Delete Class", ImageUtility.createImageIcon("cross.png"));
		this.deleteClassButton.addActionListener(this);
		this.deleteClassButton.setEnabled(false);
		this.deletePartsButton = new JButton("Delete Parts", ImageUtility.createImageIcon("cross.png"));
		this.deletePartsButton.addActionListener(this);
		this.editClassButton = new JButton("Edit Class", ImageUtility.createImageIcon("pencil.png"));
		this.editClassButton.addActionListener(this);
		this.editClassButton.setVisible(false);
		this.editClassButton.setEnabled(false);

		this.classList = new JList<String>(ClassReader.getInstance().getClassNames());
		this.classList.addListSelectionListener(this);
		this.classList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		this.classList.setLayoutOrientation(JList.VERTICAL);
		this.classList.setVisibleRowCount(-1);
		this.classList.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(EtchedBorder.LOWERED)));
		final JScrollPane classListScroller = new JScrollPane(this.classList);
		classListScroller.setPreferredSize(new Dimension(150, 100));
		classListScroller.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(EtchedBorder.RAISED), "Classes"));

		this.partList = new JList<Part>();
		this.partList.addListSelectionListener(this);
		this.partList.setSelectionMode(ListSelectionModel.SINGLE_INTERVAL_SELECTION);
		this.partList.setLayoutOrientation(JList.VERTICAL);
		this.partList.setVisibleRowCount(-1);
		this.partList.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(EtchedBorder.LOWERED)));
		final JScrollPane partListScroller = new JScrollPane(this.partList);
		partListScroller.setPreferredSize(new Dimension(210, 100));
		partListScroller.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(EtchedBorder.RAISED), "Parts"));

		layout.setHorizontalGroup(layout.createSequentialGroup().addGroup(
		                layout.createParallelGroup(Alignment.LEADING).addComponent(this.addNewClassButton).addComponent(this.editClassButton).addComponent(
		                                classListScroller).addComponent(this.deleteClassButton)).addGroup(
		                layout.createParallelGroup(Alignment.LEADING).addComponent(this.addNewPartsButton).addComponent(partListScroller).addComponent(
		                                this.deletePartsButton)));

		layout.linkSize(SwingConstants.HORIZONTAL, classListScroller, this.addNewClassButton, this.deleteClassButton, this.editClassButton);
		layout.linkSize(SwingConstants.HORIZONTAL, partListScroller, this.addNewPartsButton, this.deletePartsButton);

		layout.setVerticalGroup(layout.createParallelGroup().addGroup(
		                layout.createSequentialGroup().addComponent(this.addNewClassButton).addComponent(this.editClassButton).addComponent(classListScroller)
		                                .addComponent(this.deleteClassButton))
		                .addGroup(layout.createSequentialGroup().addComponent(this.addNewPartsButton).addComponent(partListScroller).addComponent(
		                                this.deletePartsButton)));

		this.refreshButtons();
	}

	private void updatePartList(Class<?> clazz) {
		this.partList.setListData(PartLibrary.getInstance().getParts(clazz));
	}

	@SuppressWarnings("unchecked")
	// There is checking before the cast operation that guarantee the safety
	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == this.addNewPartsButton) {
			if (this.classList.getSelectedIndices().length == 0) {
				this.refreshButtons();
				return;
			}
			this.fileChooser.setFileFilter(new ExtensionFileFilter("par", "Files with a part information. *.par"));
			int result = this.fileChooser.showOpenDialog(this.mainFrame);
			if (result == JFileChooser.APPROVE_OPTION) {
				File file = this.fileChooser.getSelectedFile();
				String className = this.classList.getSelectedValue().toString();
				try {
					Class<?> clazz = ClassReader.getInstance().loadClass(className);
					if (!Part.class.isAssignableFrom(clazz)) {
						JOptionPane.showConfirmDialog(this.mainFrame,
						                "The class you have selected does not extend main Part class. We advice you to delete current class.",
						                "Class inheritance", JOptionPane.INFORMATION_MESSAGE);
						return;
					}
					ClassGenerator classGenerator = new ClassGenerator((Class<? extends Part>) clazz);
					String[] classFieldNames = classGenerator.getAllFieldNames();
					PartReader partReader = new PartReader();
					partReader.loadInstances(file);
					String[] partFieldNames = partReader.getNames();
					Map<String, String> attributeMap =
					    MappingDialog.showDialog(this.mainFrame, "Class [" + className + "], File [" + file.getName() + "]", "Map creation", classFieldNames,
					                    partFieldNames);
					//ImageUtility.loadIcon(file.getPath().replaceFirst("[.][^.]+$", "") + '\\' + file.getName().replaceFirst("[.][^.]+$", "") + ".bmp")

					if (attributeMap == null)
						return;
					if (Part.class.isAssignableFrom(clazz) && partReader.convertToObjects((Class<? extends Part>) clazz, attributeMap)) {
						PartLibrary.getInstance().addParts(clazz, partReader.getParts());
						this.updatePartList(clazz);
					}
				} catch (ClassNotFoundException exception) {
					System.err.println(exception);
				}
			}
		} else if (e.getSource() == this.deletePartsButton) {
			String className = this.classList.getSelectedValue().toString();
			Class<?> clazz;
			try {
				clazz = ClassReader.getInstance().loadClass(className);
				PartLibrary.getInstance().removeParts(clazz, this.partList.getSelectedValuesList());
				this.updatePartList(ClassReader.getInstance().loadClass(className));
			} catch (ClassNotFoundException exception) {
				System.err.println(exception);
			}
		} else if (e.getSource() == this.deleteClassButton) {
			int result =
			    JOptionPane.showConfirmDialog(this.mainFrame,
			                    "This operation will delete also inherited classes and parts in the library.\nAre you sure you want to delete this class?",
			                    "Class deleting", JOptionPane.YES_NO_OPTION);
			if (result == JOptionPane.YES_OPTION) {
				if (!FileRemover.removeClass(this.classList.getSelectedValue().toString())) {
					JOptionPane.showMessageDialog(this.mainFrame, "Some files are read only.", "Delete warning", JOptionPane.WARNING_MESSAGE);
				}
				this.classList.setListData(ClassReader.getInstance().getClassNames());
			}
		} else if (e.getSource() == this.addNewClassButton) {
			EditClassDialog.showDialog(this.mainFrame);
			ClassCompiler.compileAllClasses();
			this.classList.setListData(ClassReader.getInstance().getClassNames());
		}
	}

	@Override
	public void valueChanged(ListSelectionEvent e) {
		this.refreshButtons();
		if (e.getSource() == this.classList) {
			if (this.classList.getSelectedIndices().length > 0) {
				String className = this.classList.getSelectedValue().toString();
				try {
					this.updatePartList(ClassReader.getInstance().loadClass(className));
					return;
				} catch (ClassNotFoundException exception) {
					System.err.println(exception);
				}
			}
			this.partList.setListData(new Part[0]);
		}
	}

	private void refreshButtons() {
		if (this.classList.getSelectedIndices().length > 0) {
			this.addNewPartsButton.setEnabled(true);
			this.deleteClassButton.setEnabled(true);
			this.editClassButton.setEnabled(true);
			this.deletePartsButton.setEnabled(this.partList.getSelectedIndices().length > 0);
		} else {
			this.addNewPartsButton.setEnabled(false);
			this.deleteClassButton.setEnabled(false);
			this.deletePartsButton.setEnabled(false);
			this.editClassButton.setEnabled(false);
		}
	}
}
