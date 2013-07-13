package com.techsoft.partselector.ui.dialog;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Frame;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

/**
 * @author  Andrii Dzhyrma 
 */
public class MappingDialog extends JDialog implements ActionListener {
	private static final long serialVersionUID = 3737483740105218627L;
	/**
     * @uml.property  name="dialog"
     * @uml.associationEnd  
     */
	private static MappingDialog dialog;
	private static Map<String, String> value;
	private String[] possibleClassValues;
	private List<JComboBox<String>> comboBoxes;

	/** Set up and show the dialog. The first Component argument determines which
	 * frame the dialog depends on; it should be a component in the dialog's
	 * controlling frame. The second Component argument should be null if you
	 * want the dialog to come up with its left corner in the center of the
	 * screen; otherwise, it should be the component on top of which the dialog
	 * should appear.
	 * 
	 * @param imageIcon */
	public static Map<String, String> showDialog(Component locationComponent, String labelText, String title, String[] possibleClassValues,
	                String[] possiblePartValues) {
		Frame frame = JOptionPane.getFrameForComponent(locationComponent);
		Arrays.sort(possibleClassValues);
		Arrays.sort(possiblePartValues);
		dialog = new MappingDialog(frame, locationComponent, labelText, title, possibleClassValues, possiblePartValues);
		dialog.setVisible(true);
		return value;
	}

	private void setValue() {
		if (value == null)
			value = new HashMap<String, String>();
		else
			value.clear();
		for (int i = 0; i < this.possibleClassValues.length; i++)
			value.put(this.possibleClassValues[i], (String) comboBoxes.get(i).getSelectedItem());
	}

	private MappingDialog(Frame frame, Component locationComp, String labelText, String title, String[] possibleClassValues, String[] possiblePartValues) {
		super(frame, title, true);
		super.setResizable(false);

		this.possibleClassValues = possibleClassValues;
		//Create and initialize the buttons.
		final JButton cancelButton = new JButton("Cancel");
		cancelButton.addActionListener(this);
		//
		final JButton setButton = new JButton("Set");
		setButton.setActionCommand("Set");
		setButton.addActionListener(this);
		getRootPane().setDefaultButton(setButton);

		JPanel comboBoxPane = new JPanel();
		comboBoxPane.setLayout(new BoxLayout(comboBoxPane, BoxLayout.PAGE_AXIS));
		JLabel label = new JLabel(labelText);
		comboBoxPane.add(label);
		comboBoxPane.add(Box.createRigidArea(new Dimension(0, 10)));

		//JLabel scheme = new JLabel(icon, SwingConstants.CENTER);
		//comboBoxPane.add(scheme);
		this.comboBoxes = new ArrayList<JComboBox<String>>();
		for (int i = 0; i < possibleClassValues.length; i++) {
			JComboBox<String> comboBox = new JComboBox<String>(possiblePartValues);
			int selectedIndex = Arrays.binarySearch(possiblePartValues, possibleClassValues[i]);
			comboBox.setSelectedIndex(selectedIndex < 0 ? (selectedIndex == -1 ? 0 : -selectedIndex - 2) : selectedIndex);
			this.comboBoxes.add(comboBox);
			//listScroller.setPreferredSize(new Dimension(250, 80));
			comboBox.setAlignmentX(LEFT_ALIGNMENT);
			JLabel comboBoxLabel = new JLabel(possibleClassValues[i]);
			comboBoxLabel.setLabelFor(comboBox);
			comboBoxPane.add(comboBoxLabel);
			comboBoxPane.add(Box.createRigidArea(new Dimension(0, 5)));
			comboBoxPane.add(comboBox);
		}

		//Create a container so that we can add a title around
		//the scroll pane.  Can't add a title directly to the
		//scroll pane because its background would be white.
		//Lay out the label and scroll pane from top to bottom.

		comboBoxPane.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

		//Lay out the buttons from left to right.
		JPanel buttonPane = new JPanel();
		buttonPane.setLayout(new BoxLayout(buttonPane, BoxLayout.LINE_AXIS));
		buttonPane.setBorder(BorderFactory.createEmptyBorder(0, 10, 10, 10));
		buttonPane.add(Box.createHorizontalBox());
		buttonPane.add(setButton);
		buttonPane.add(Box.createRigidArea(new Dimension(10, 0)));
		buttonPane.add(cancelButton);

		//Put everything together, using the content pane's BorderLayout.
		Container contentPane = this.getContentPane();
		contentPane.add(comboBoxPane, BorderLayout.CENTER);
		contentPane.add(buttonPane, BorderLayout.PAGE_END);

		//Initialize values.
		this.pack();
		this.setLocationRelativeTo(locationComp);
	}

	//Handle clicks on the Set and Cancel buttons.
	public void actionPerformed(ActionEvent e) {
		if ("Set".equals(e.getActionCommand()))
			setValue();
		else
			value = null;

		MappingDialog.dialog.setVisible(false);
	}
}