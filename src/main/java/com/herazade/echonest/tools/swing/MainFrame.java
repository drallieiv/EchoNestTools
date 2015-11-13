package com.herazade.echonest.tools.swing;

import java.awt.Dimension;
import java.awt.Frame;

import javax.swing.JFrame;
import javax.swing.WindowConstants;
import javax.swing.JMenuBar;
import javax.swing.JMenu;
import javax.swing.JMenuItem;

public class MainFrame extends JFrame {
	public MainFrame() {
		
		JMenuBar menuBar = new JMenuBar();
		setJMenuBar(menuBar);
		
		JMenu mnFile = new JMenu(Messages.getString("menu.file")); //$NON-NLS-1$
		menuBar.add(mnFile);
		
		JMenuItem mntmLoadProject = new JMenuItem(Messages.getString("menu.file.newproject")); //$NON-NLS-1$
		mnFile.add(mntmLoadProject);
		
		JMenuItem mntmLoadProject_1 = new JMenuItem(Messages.getString("MainFrame.mntmLoadProject_1.text")); //$NON-NLS-1$
		mnFile.add(mntmLoadProject_1);
		
		JMenuItem mntmSaveProject = new JMenuItem(Messages.getString("MainFrame.mntmSaveProject.text")); //$NON-NLS-1$
		mnFile.add(mntmSaveProject);
	}

    public void init() {
        setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        setSize(new Dimension(600, 400));
        setState(Frame.NORMAL);
        setVisible(true);
    }
}
