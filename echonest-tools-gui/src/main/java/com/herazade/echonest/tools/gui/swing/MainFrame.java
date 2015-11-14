package com.herazade.echonest.tools.gui.swing;

import java.awt.BorderLayout;
import java.util.Properties;

import javax.inject.Inject;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ConfigurableApplicationContext;

import com.herazade.echonest.tools.gui.GuiConfig;
import com.herazade.echonest.tools.gui.swing.action.ExitAction;
import com.herazade.echonest.tools.gui.swing.action.OpenDialogAction;

public class MainFrame extends JFrame {

	/**
	 * Logger
	 */
	private Logger logger = LoggerFactory.getLogger(getClass());

	/**
	 * Spring Context
	 */
	private ConfigurableApplicationContext context;

	private PreferenceDialog preferenceDialog;

	/**
	 * Sub-folder for icons
	 */
	private static final String ICON_FOLDER = "/icons/";

	public MainFrame() {
		setTitle(Messages.getString("MainFrame.title")); //$NON-NLS-1$

		JMenuBar menuBar = new JMenuBar();
		setJMenuBar(menuBar);

		JMenu mnFile = new JMenu(Messages.getString("menu.file")); //$NON-NLS-1$
		menuBar.add(mnFile);

		JMenuItem mntmNewProject = new JMenuItem(Messages.getString("menu.file.project.new")); //$NON-NLS-1$
		mntmNewProject.setIcon(new ImageIcon(MainFrame.class.getResource(ICON_FOLDER + "folder.png")));
		mnFile.add(mntmNewProject);

		JMenuItem mntmLoadProject = new JMenuItem(Messages.getString("menu.file.project.open")); //$NON-NLS-1$
		mntmLoadProject.setIcon(new ImageIcon(MainFrame.class.getResource(ICON_FOLDER + "folder-import.png")));
		mnFile.add(mntmLoadProject);

		JMenuItem mmtmExit = new JMenuItem(new ExitAction(this));
		mnFile.add(mmtmExit);

		JMenu mnNewMenu = new JMenu(Messages.getString("menu.options")); //$NON-NLS-1$
		menuBar.add(mnNewMenu);

		preferenceDialog = new PreferenceDialog();
		OpenDialogAction openPrefDialogAction = new OpenDialogAction(preferenceDialog, Messages.getString("menu.options.pref"));
		JMenuItem mntmNewMenuItem = new JMenuItem(openPrefDialogAction); //$NON-NLS-1$	

		mnNewMenu.add(mntmNewMenuItem);

		getContentPane().setLayout(new BorderLayout(0, 0));
	}

	/**
	 * Save Spring context
	 * 
	 * @param context
	 */
	public void setSpringContext(ConfigurableApplicationContext context) {
		this.context = context;
	}

	/**
	 * @param guiConfig
	 *            the guiConfig to set
	 */
	@Inject
	public void setGuiConfig(GuiConfig guiConfig) {
		preferenceDialog.loadGuiConfig(guiConfig);
	}

}
