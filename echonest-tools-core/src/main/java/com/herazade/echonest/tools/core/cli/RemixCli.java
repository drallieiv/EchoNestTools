package com.herazade.echonest.tools.core.cli;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.UnsupportedAudioFileException;

import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.echonest.api.v4.TimedEvent;
import com.herazade.echonest.tools.core.audio.AudioManager;
import com.herazade.echonest.tools.core.remix.strategy.ManualRemix;

/**
 * Main Class for Command Line Interface
 * 
 * @author drallieiv
 *
 */
public class RemixCli {

	public static void main(String[] args) {

		if (args.length < 1) {
			throw new IllegalArgumentException("Requires Action argument");
		}

		CliActionsEnum action = CliActionsEnum.valueOf(args[0].toUpperCase());
		if (action == null) {
			throw new IllegalArgumentException("Unknown Action " + args[0]);
		}

		switch (action) {
		case DEMO:
			demo();
			break;
		default:
			break;
		}
	}

	private static void demo() {
		AudioManager audioManager = new AudioManager();
		Logger logger = LoggerFactory.getLogger(RemixCli.class);

		File out = new File("target/remixed.wav");

		try (
				InputStream inFile = RemixCli.class.getClassLoader().getResourceAsStream("files/test-music.mp3");
				OutputStream outFile = new FileOutputStream(out);
				AudioInputStream audioIn = audioManager.openMp3AsPcm(inFile);) {

			ManualRemix remix = ManualRemix.buildNew().addPart(0, 45.60753);
			for (int i = 0; i < 60; i++) {
				remix.addPart(45.60753, 104.93201);
			}
			remix.addPart(104.93201, 213);

			audioManager.writeRemix(remix.getRemix(null), audioIn, outFile);

			outFile.close();
			logger.info("Done file written : {}", out.getAbsolutePath());

		} catch (FileNotFoundException e) {
			logger.error("", e);
		} catch (IOException e) {
			logger.error("", e);
		} catch (UnsupportedAudioFileException e) {
			logger.error("", e);
		}

	}
}
