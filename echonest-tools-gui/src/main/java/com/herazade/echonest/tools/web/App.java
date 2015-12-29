package com.herazade.echonest.tools.web;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Import;

import com.herazade.echonest.tools.core.EntCoreConfiguration;

@SpringBootApplication
@Import(EntCoreConfiguration.class)
public class App {
	
	 public static void main(String[] args) {
	        ApplicationContext ctx = SpringApplication.run(App.class, args);
	    }

}
