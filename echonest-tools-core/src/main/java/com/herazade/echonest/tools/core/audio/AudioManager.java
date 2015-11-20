package com.herazade.echonest.tools.core.audio;

import java.io.BufferedInputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.SequenceInputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.media.Format;
import javax.media.Manager;
import javax.sound.sampled.AudioFileFormat.Type;
import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.SourceDataLine;
import javax.sound.sampled.UnsupportedAudioFileException;
import javax.sound.sampled.spi.AudioFileWriter;

import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.echonest.api.v4.TimedEvent;
import com.sun.media.codec.audio.mpa.JavaDecoder;
import com.sun.media.format.WavAudioFormat;

public class AudioManager {

	Logger logger = LoggerFactory.getLogger(getClass());

	/**
	 * Samples 16bits/s
	 */
	private static final int SAMPLE_SIZE = 16;

	public AudioInputStream openMp3AsPcm(InputStream file) throws UnsupportedAudioFileException, IOException {

		if (file == null) {
			throw new IllegalArgumentException("Null File");
		}

		AudioInputStream in = AudioSystem.getAudioInputStream(file);

		AudioFormat baseFormat = in.getFormat();
		logger.debug("Input Format : {}", baseFormat);

		AudioFormat decodedFormat = new AudioFormat(AudioFormat.Encoding.PCM_SIGNED,
				baseFormat.getSampleRate(),
				SAMPLE_SIZE,
				baseFormat.getChannels(),
				baseFormat.getChannels() * 2,
				baseFormat.getSampleRate(),
				false);

		AudioInputStream decodedIs = AudioSystem.getAudioInputStream(decodedFormat, in);

		// Copy Everything into buffer
		BufferedInputStream bufferedInputStream = new BufferedInputStream(decodedIs);
		return new AudioInputStream(bufferedInputStream, decodedIs.getFormat(), decodedIs.getFrameLength());
	}

	/**
	 * 
	 * @param remix
	 * @param out
	 * @throws IOException
	 */
	public void writeRemix(List<TimedEvent> remix, AudioInputStream in, OutputStream out) throws IOException {

		AudioFormat format = in.getFormat();
		int bytesPerSecond = format.getFrameSize() * (int) format.getFrameRate();

		logger.debug("Mark Supported : {}", in.markSupported());

		List<AudioInputStream> parts = new ArrayList<AudioInputStream>();
		long totalLenght = 0;

		for (TimedEvent timedEvent : remix) {
			in.mark(0);
			in.reset();
			in.skip((long) (timedEvent.getStart() * bytesPerSecond));
			long framesOfAudioToCopy = (long) (timedEvent.getDuration() * (int) format.getFrameRate());
			AudioInputStream part = new AudioInputStream(in, format, framesOfAudioToCopy);
			AudioSystem.write(part, Type.WAVE, out);

			// parts.add(new AudioInputStream(new ByteArrayInputStream(part),
			// format, framesOfAudioToCopy));
			totalLenght += framesOfAudioToCopy;

		}

		// SequenceInputStream sequence = new
		// SequenceInputStream(Collections.enumeration(parts));
		// AudioInputStream joined = new AudioInputStream(sequence, format,
		// totalLenght);
		// AudioSystem.write(joined, Type.WAVE, out);
	}
}
