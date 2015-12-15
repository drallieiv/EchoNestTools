package com.herazade.echonest.tools.core.audio;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.UnsupportedAudioFileException;

import org.apache.commons.io.IOUtils;
import org.junit.Ignore;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.herazade.echonest.tools.core.cli.RemixCli;

public class AudioManagerTest {

	private AudioManager audioManager = new AudioManager();

	// Logger
	private Logger logger = LoggerFactory.getLogger(getClass());

	@Test
	public void testOpenMp3AsPcmRickRoll() throws IOException, UnsupportedAudioFileException {
		copyMp3AudioData("files/test-music.mp3");
	}

	@Test
	@Ignore
	// Stuck for now
	public void testOpenMp3AsPcmFun() throws FileNotFoundException, IOException, UnsupportedAudioFileException {
		copyMp3AudioData("files/SomeNights.mp3");
	}

	private void copyMp3AudioData(String testFile) throws IOException, UnsupportedAudioFileException, FileNotFoundException {
		File tmpFile = File.createTempFile("testOpenMp3AsPcmRickRoll", null);
		try (
				InputStream inFile = RemixCli.class.getClassLoader().getResourceAsStream(testFile);
				AudioInputStream audioIn = audioManager.openMp3AsPcm(inFile);
				FileOutputStream fos = new FileOutputStream(tmpFile);) {
			IOUtils.copy(audioIn, fos);
			logger.info("RickRoll Data saved as {}", tmpFile);
		}
	}

}
