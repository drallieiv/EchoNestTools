package com.herazade.echonest.tools.gui.swing;

import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Properties;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JTextField;

import com.herazade.echonest.tools.gui.GuiConfig;

public class PreferenceDialog extends JDialog {

	/**
	 * GUI File Chooser for folder path
	 */
	private JFileChooser folderPathFileChooser;

	/**
	 * GUI Text display for current folder path
	 */
	private JTextField folderPathField;

	private GuiConfig guiConfig;

	public PreferenceDialog() {
		// Init GUI
		init();

	}

	public void loadGuiConfig(GuiConfig guiConfig) {
		this.guiConfig = guiConfig;
		this.folderPathField.setText(guiConfig.getProperty(GuiConfig.PROJECT_FOLDER));
	}

	/**
	 * Swing Initialization
	 */
	private void init() {
		setTitle(Messages.getString("menu.options.pref")); //$NON-NLS-1$
		setModal(true);
		setAlwaysOnTop(true);
		getContentPane();
		getContentPane().setLayout(new FlowLayout(FlowLayout.CENTER, 5, 5));

		JLabel folderPathLabel = new JLabel(Messages.getString("PreferenceDialog.folderPath.label")); //$NON-NLS-1$

		folderPathField = new JTextField(20);

		JButton folderPathButton = new JButton(Messages.getString("PreferenceDialog.folderPath.browse")); //$NON-NLS-1$
		folderPathButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent evt) {
				folderPathActionPerformed(evt);
			}
		});

		folderPathFileChooser = new JFileChooser(folderPathField.getText());
		folderPathFileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);

		add(folderPathLabel);
		add(folderPathField);
		add(folderPathButton);

		pack();
		setLocationRelativeTo(null);
	}

	private void folderPathActionPerformed(ActionEvent evt) {
		if (folderPathFileChooser.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
			String path = folderPathFileChooser.getSelectedFile().getAbsolutePath();
			folderPathField.setText(path);
			guiConfig.setProperty(GuiConfig.PROJECT_FOLDER, path);
			guiConfig.save();
		}
	}

}
