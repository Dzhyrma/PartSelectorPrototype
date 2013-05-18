package com.techsoft.partselector.ui;

import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.net.URL;
import java.util.Map;

import javax.swing.GroupLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.ListSelectionModel;
import javax.swing.GroupLayout.Alignment;
import javax.swing.SwingConstants;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import com.techsoft.partselector.model.Part;
import com.techsoft.partselector.util.PartFileFilter;
import com.techsoft.partselector.util.PartLibrary;
import com.techsoft.partselector.util.io.ClassReader;
import com.techsoft.partselector.util.io.PartReader;
import com.techsoft.partselector.util.reflect.ClassGenerator;

public class LibraryPanel extends JPanel implements ActionListener, ListSelectionListener {

	private static final long serialVersionUID = -3389243649999461716L;

	private JButton addNewClassButton, addNewPartsButton, deleteClassButton, deletePartsButton;
	private JList<String> classList;
	private JList<Part> partList;
	private JFileChooser fileChooser;

	public LibraryPanel() {
		this.fileChooser = new JFileChooser();

		GroupLayout layout = new GroupLayout(this);
		this.setLayout(layout);
		layout.setAutoCreateGaps(true);
		layout.setAutoCreateContainerGaps(true);

		this.addNewClassButton = new JButton("New Class", createImageIcon("images/plus.png"));
		this.addNewClassButton.addActionListener(this);
		this.addNewPartsButton = new JButton("New Parts", createImageIcon("images/plus.png"));
		this.addNewPartsButton.setEnabled(false);
		this.addNewPartsButton.addActionListener(this);
		this.deleteClassButton = new JButton("Delete Class", createImageIcon("images/cross.png"));
		this.deleteClassButton.addActionListener(this);
		this.deletePartsButton = new JButton("Delete Parts", createImageIcon("images/cross.png"));
		this.deletePartsButton.addActionListener(this);

		this.classList = new JList<String>(ClassReader.getInstance().getClassNames());
		this.classList.addListSelectionListener(this);
		this.classList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		this.classList.setLayoutOrientation(JList.VERTICAL);
		this.classList.setVisibleRowCount(-1);
		JScrollPane classListScroller = new JScrollPane(this.classList);
		classListScroller.setPreferredSize(new Dimension(150, 100));

		this.partList = new JList<Part>();
		this.partList.setSelectionMode(ListSelectionModel.SINGLE_INTERVAL_SELECTION);
		this.partList.setLayoutOrientation(JList.VERTICAL);
		this.partList.setVisibleRowCount(-1);
		JScrollPane partListScroller = new JScrollPane(this.partList);
		partListScroller.setPreferredSize(new Dimension(200, 100));

		layout.setHorizontalGroup(layout.createSequentialGroup().addGroup(
		                layout.createParallelGroup(Alignment.LEADING).addComponent(this.addNewClassButton).addComponent(classListScroller).addComponent(
		                                this.deleteClassButton)).addGroup(
		                layout.createParallelGroup(Alignment.LEADING).addComponent(this.addNewPartsButton).addComponent(partListScroller).addComponent(
		                                this.deletePartsButton)));
		layout.linkSize(SwingConstants.HORIZONTAL, classListScroller, this.addNewClassButton, this.deleteClassButton);
		layout.linkSize(SwingConstants.HORIZONTAL, partListScroller, this.addNewPartsButton, this.deletePartsButton);

		layout.setVerticalGroup(layout.createSequentialGroup().addGroup(
		                layout.createParallelGroup(Alignment.LEADING).addComponent(this.addNewClassButton).addComponent(this.addNewPartsButton)).addGroup(
		                layout.createParallelGroup(Alignment.LEADING).addComponent(classListScroller).addComponent(partListScroller)).addGroup(
		                layout.createParallelGroup(Alignment.LEADING).addComponent(this.deleteClassButton).addComponent(this.deletePartsButton)));
	}

	private static ImageIcon createImageIcon(String path) {
		URL imgURL = MainFrame.class.getResource(path);
		if (imgURL != null)
			return new ImageIcon(imgURL);
		else {
			System.err.println("Couldn't find file: " + path);
			return null;
		}
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
				this.addNewPartsButton.setEnabled(false);
				return;
			}
			this.fileChooser.setFileFilter(new PartFileFilter());
			int result = this.fileChooser.showOpenDialog(this);
			if (result == JFileChooser.APPROVE_OPTION) {
				File file = fileChooser.getSelectedFile();
				String className = this.classList.getSelectedValue().toString();
				try {
					Class<?> clazz = ClassReader.getInstance().loadClass(className);
					ClassGenerator classGenerator = new ClassGenerator(clazz);
					String[] classFieldNames = classGenerator.getFieldNames();
					System.out.println(file.getName());
					PartReader partReader = new PartReader();
					partReader.loadInstances(file);
					String[] partFieldNames = partReader.getNames();
					final JButton button = new JButton("Confirm");
					Map<String, String> attributeMap =
					    MappingDialog.showDialog(this, button, "Class [" + className + "], File [" + file.getName() + "]", "Map creation", classFieldNames,
					                    partFieldNames);
					if (attributeMap == null)
						return;
					System.out.println(attributeMap.toString());
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
				PartLibrary.getInstance().deleteParts(clazz, this.partList.getSelectedValuesList());
				this.updatePartList(ClassReader.getInstance().loadClass(className));
			} catch (ClassNotFoundException exception) {
				System.err.println(exception);
			}

		}
	}

	@Override
	public void valueChanged(ListSelectionEvent e) {
		if (e.getSource() == this.classList) {
			if (this.classList.getSelectedIndices().length > 0)
				this.addNewPartsButton.setEnabled(true);
			else
				this.addNewPartsButton.setEnabled(false);
			String className = this.classList.getSelectedValue().toString();
			try {
				this.updatePartList(ClassReader.getInstance().loadClass(className));
			} catch (ClassNotFoundException exception) {
				this.partList.setListData(new Part[0]);
				System.err.println(exception);
			}
		}

	}
}
