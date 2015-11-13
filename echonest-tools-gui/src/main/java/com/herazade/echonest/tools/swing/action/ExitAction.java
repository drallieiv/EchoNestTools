package com.herazade.echonest.tools.swing.action;

import java.awt.event.ActionEvent;
import java.awt.event.WindowEvent;

import javax.swing.AbstractAction;
import javax.swing.JFrame;

import com.herazade.echonest.tools.swing.Messages;

/**
 * Exit application action. Close frame
 * 
 * @author drallieiv
 *
 */
public class ExitAction extends AbstractAction {

	private JFrame parentFrame;

	public ExitAction(JFrame parentFrame) {
		this.parentFrame = parentFrame;
		putValue(NAME, Messages.getString("action.exit.name")); //$NON-NLS-1$
	}

	public void actionPerformed(ActionEvent e) {
		parentFrame.dispatchEvent(new WindowEvent(parentFrame, WindowEvent.WINDOW_CLOSING));
	}
}