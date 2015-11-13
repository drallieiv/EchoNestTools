package com.herazade.echonest.tools.swing;

import java.awt.BorderLayout;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.herazade.echonest.tools.swing.action.ExitAction;

public class MainFrame extends JFrame {

	private Logger logger = LoggerFactory.getLogger(getClass());

	private static final String ICON_FOLDER = "/icons/";
	
	private final ExitAction exitAction = new ExitAction(this);

	public MainFrame() {
		setTitle(Messages.getString("MainFrame.title")); //$NON-NLS-1$

		JMenuBar menuBar = new JMenuBar();
		setJMenuBar(menuBar);

		JMenu mnFile = new JMenu(Messages.getString("menu.file")); //$NON-NLS-1$
		menuBar.add(mnFile);

		JMenuItem mntmNewProject = new JMenuItem(Messages.getString("menu.file.project.new")); //$NON-NLS-1$
		mntmNewProject.setIcon(new ImageIcon(MainFrame.class.getResource(ICON_FOLDER + "folder.png")));
		mnFile.add(mntmNewProject);

		JMenuItem mntmLoadProject = new JMenuItem(Messages.getString("menu.file.project.load")); //$NON-NLS-1$
		mntmLoadProject.setIcon(new ImageIcon(MainFrame.class.getResource(ICON_FOLDER + "folder-import.png")));
		mnFile.add(mntmLoadProject);

		JMenuItem mntmSaveProject = new JMenuItem(Messages.getString("menu.file.project.save")); //$NON-NLS-1$
		mntmSaveProject.setIcon(new ImageIcon(MainFrame.class.getResource(ICON_FOLDER + "folder-export.png")));
		mntmSaveProject.setEnabled(false);
		mnFile.add(mntmSaveProject);

		JMenuItem mmtmExit = new JMenuItem(exitAction);

		mnFile.add(mmtmExit);
		getContentPane().setLayout(new BorderLayout(0, 0));
	}

}
