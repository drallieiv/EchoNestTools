package com.herazade.echonest.tools.gui.swing.action;

import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;
import javax.swing.JDialog;

import com.herazade.echonest.tools.gui.swing.Messages;

/**
 * Action that opens a given dialog
 * @author drallieiv
 *
 */
public class OpenDialogAction extends AbstractAction{

	private static final long serialVersionUID = 6683103289661629393L;
	
	private JDialog dialog;

	public OpenDialogAction(JDialog dialog, String name) {
		this.dialog = dialog;
		putValue(NAME, name);
	}

	public void actionPerformed(ActionEvent e) {
		dialog.setVisible(true);
	}

}
