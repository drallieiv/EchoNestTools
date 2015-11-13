package com.herazade.echonest.tools;

import java.awt.Dimension;
import java.awt.Frame;

import javax.swing.WindowConstants;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.herazade.echonest.tools.swing.MainFrame;

@Configuration
public class AppConfig {

	@Bean
	MainFrame getMainFrame() {
		MainFrame main = new MainFrame();
		main.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
		main.setSize(new Dimension(800, 500));
		main.setState(Frame.NORMAL);
		main.setLocationRelativeTo(null);
		main.setVisible(true);
		return main;
	}

}
