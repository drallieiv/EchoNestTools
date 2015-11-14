package com.herazade.echonest.tools.gui;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.attribute.FileAttribute;
import java.util.Properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;

@Component
public class GuiConfig extends Properties {

	private Logger logger = LoggerFactory.getLogger(getClass());

	private static final String CONFIG_FILE = "config.properties";

	private static final String APP_NAME = "EchoNestTools";

	private static final String DEFAULT_PROJECT_SUBFOLDER = "projects";

	public static final String PROJECT_FOLDER = "project.folder";

	public GuiConfig() {

		ClassPathResource configFile = new ClassPathResource(CONFIG_FILE);

		try (InputStream config = configFile.getInputStream()) {
			this.load(config);
			logger.info("Configuration loaded from file.");
		} catch (IOException e) {
			logger.info("Configuration file not found. init with default values.");
			Path projectFolder = Paths.get(System.getenv("APPDATA"), APP_NAME, DEFAULT_PROJECT_SUBFOLDER);
			this.setProperty(PROJECT_FOLDER, projectFolder.toFile().getAbsolutePath());
			save();

			try {
				Files.createDirectories(projectFolder);
				logger.info("Default project folder created : {}", projectFolder);
			} catch (IOException e1) {
				logger.error("Project folder cannot be created : {}", projectFolder);
			}

		}
	}

	public boolean save() {
		try (OutputStream configSave = new FileOutputStream(CONFIG_FILE)) {
			// Create configuration File
			this.store(configSave, "GUI Configuration File");
			logger.info("Configuration file created.");
			return true;
		} catch (IOException e2) {
			logger.warn("Cannot save configuration file.");
			return false;
		}
	}
}
