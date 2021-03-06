package com.herazade.echonest.tools.gui;

import java.awt.Dimension;
import java.awt.Frame;

import javax.inject.Inject;
import javax.swing.WindowConstants;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.herazade.echonest.tools.gui.swing.MainFrame;

@Configuration
public class AppConfig {

	private Logger logger = LoggerFactory.getLogger(getClass());

	@Inject
	private ConfigurableApplicationContext context;

	@Bean
	public MainFrame getMainFrame() {
		MainFrame main = new MainFrame();
		
		main.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
		main.setSize(new Dimension(800, 500));
		main.setState(Frame.NORMAL);
		main.setLocationRelativeTo(null);
		main.setVisible(true);
		return main;
	}

}
