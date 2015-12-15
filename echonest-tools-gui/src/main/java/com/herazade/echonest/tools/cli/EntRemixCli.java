package com.herazade.echonest.tools.cli;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.UnsupportedAudioFileException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.oxm.XmlMappingException;

import com.echonest.api.v4.EchoNestException;
import com.echonest.api.v4.TimedEvent;
import com.echonest.api.v4.TrackAnalysis;
import com.herazade.echonest.tools.core.EntCoreConfiguration;
import com.herazade.echonest.tools.core.audio.AudioManager;
import com.herazade.echonest.tools.core.project.EntProject;
import com.herazade.echonest.tools.core.project.EntProjectManager;
import com.herazade.echonest.tools.looper.remix.strategy.SimpleLooperRemixStrategy;

public class EntRemixCli {

	public static void main(String[] args) throws XmlMappingException, IOException, EchoNestException, UnsupportedAudioFileException {
		
		Logger logger = LoggerFactory.getLogger(EntRemixCli.class);
		
		logger.info(" -- Start --");

		if (args.length < 1) {
			logger.error("Parameters required : herazadeLooper.exe file.mp3 timeInSeconds");
			logger.error("herazadeLooper.exe file.mp3 timeInSeconds");
			logger.error("herazadeLooper.exe projectFile.xml");
			return;
		}

		ApplicationContext context = new AnnotationConfigApplicationContext(EntCoreConfiguration.class);
		EntProjectManager entProjectManager = context.getBean(EntProjectManager.class);

		// 1. LOADING

		// Load Project or Create New One
		EntProject project = null;

		if (args.length == 1) {
			// Load Project Mode
			Path projectPath = Paths.get(args[0]);
			if (projectPath.toFile().exists()) {
				logger.info("Load Project File : {}", projectPath);
				project = entProjectManager.loadProject(projectPath.toFile());
			} else {

			}
		} else if (args.length == 2) {
			// New Project Mode : Simple Loop
			Path mp3Path = Paths.get(args[0]).toAbsolutePath();

			double requestedDuration;
			try {
				requestedDuration = Double.parseDouble(args[1]);
			} catch (NumberFormatException e) {
				logger.error("Invalid duration [{}] (2nd argument)", args[1]);
				return;
			}

			if (!Files.exists(mp3Path)) {
				return;
			}

			String mp3fileName = mp3Path.getFileName().toString().replace(".mp3", "");
			logger.info("Base Filename : {}", mp3fileName);
			Path projectFilePath = mp3Path.getParent().resolve(mp3fileName + ".project.xml");

			project = new EntProject();
			project.setMp3FilePath(mp3Path.toString());
			project.setProjectName("Generated Simple Looper Project for " + mp3Path.getFileName());

			// Add Simple Loop Remix Strategy
			SimpleLooperRemixStrategy remixStrategy = new SimpleLooperRemixStrategy();
			remixStrategy.setMinimumTime(requestedDuration);
			project.setRemixStrategy(remixStrategy);

			logger.info("Create new Project File : {}", projectFilePath);
			entProjectManager.saveAsFile(project, projectFilePath.toFile());
		} else {
			return;
		}

		// 2. ANALYSIS

		TrackAnalysis analysis;
		if (project.getAnalysis() != null) {
			logger.info("Analysis loaded from project file");
			analysis = project.getAnalysis();
		} else {
			logger.info("Run Analysis. Please Wait ...");
			analysis = entProjectManager.runAnalysis(project);
			if(analysis == null){
				return;
			}
			// entProjectManager.saveAsFile(project, projectPath.toFile());
		}

		if (project.getRemixStrategy() == null) {
			logger.info("No Remix Strategy defined");
		} else {
			logger.info("Remix Strategy : {}", project.getRemixStrategy().getClass().getSimpleName());

			AudioManager audioManager = new AudioManager();

			File inFile = project.getMp3File();
			File out = Paths.get(project.getRemixFilePath()).toFile();

			try (
					InputStream in = new BufferedInputStream(new FileInputStream(inFile));
					OutputStream outFile = new FileOutputStream(out);
					AudioInputStream audioIn = audioManager.openMp3AsPcm(in);) {

				List<TimedEvent> remix = project.getRemixStrategy().getRemix(analysis);

				logger.info("Write Remix to File {}", out.getAbsolutePath());
				audioManager.writeRemix(remix, audioIn, outFile);

				outFile.close();
				logger.info("File written : {}", out.getAbsolutePath());
			} catch (UnsupportedAudioFileException e) {
				logger.error("Fail Remixing : {} ", inFile, e);
				throw e;
			}

		}

		logger.info("DONE");
	}

}
