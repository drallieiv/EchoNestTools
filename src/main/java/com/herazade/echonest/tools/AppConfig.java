package com.herazade.echonest.tools;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.herazade.echonest.tools.swing.MainFrame;

@Configuration
public class AppConfig {

	@Bean
	MainFrame getMainFrame(){	
		MainFrame main = new MainFrame();
		main.init();
		return main;
	}

}
