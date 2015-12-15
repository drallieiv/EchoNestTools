package com.herazade.echonest.tools.core.audio;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Arrays;
import java.util.List;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.UnsupportedAudioFileException;

import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.echonest.api.v4.TimedEvent;

/**
 * Manager who can read and write audio files
 * 
 * @author drallieiv
 *
 */
public class AudioManager {

	Logger logger = LoggerFactory.getLogger(getClass());

	// If Dump Data in Memory
	private byte[] srcData;

	// Temp File for source Audio Data
	private File tempFile;

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
		// 1024 frame arbitrary buffer
		int bufferSize = 1024 * format.getFrameSize();

		// Compute the total remix length
		long dataLength = 0;
		for (TimedEvent timedEvent : remix) {
			dataLength += timedEvent.getDuration() * bytesPerSecond;
		}
		logger.debug("Expected file size : {} bytes", dataLength);

		// Prepare The Whole Data
		logger.debug("Create Wav file Headers");
		writeWavHeaders(out, format, dataLength);

		// Save Input Stream
		logger.debug("Save input Stream");
		saveInputStream(in);

		// Append all audio data
		for (TimedEvent timedEvent : remix) {
			logger.debug("Start writing next part : [{}]", timedEvent);

			int startByte = format.getFrameSize() * (int) (timedEvent.getStart() * bytesPerSecond / format.getFrameSize());
			int endByte = format.getFrameSize() * (int) ((startByte + timedEvent.getDuration() * bytesPerSecond) / format.getFrameSize());

			copyBytesFromInputStream(startByte, endByte, out);
		}

	}

	private void copyBytesFromInputStream(int startByte, int endByte,
			OutputStream out) throws IOException {
		IOUtils.write(Arrays.copyOfRange(srcData, startByte, endByte), out);
	}

	private void saveInputStream(AudioInputStream in) throws IOException {
		// Dump Source data once
		logger.debug("Load the full Music in Memory. Please wait ...");
		srcData = IOUtils.toByteArray(in);
	}

	// private void copyBytesFromInputStream(int startByte, int endByte,
	// OutputStream out) throws IOException {
	// RandomAccessFile raf = new RandomAccessFile(tempFile, "r");
	// int len = endByte - startByte;
	// byte[] part = new byte[len];
	// raf.readFully(part, startByte, len);
	// IOUtils.write(part, out);
	// }
	//
	// private void saveInputStream(AudioInputStream in) throws IOException {
	// // Save to Temp File
	// tempFile = File.createTempFile("enRemix", null);
	// logger.debug("Write raw Audio Data to temp file : {}",
	// tempFile.getAbsolutePath());
	// try (FileOutputStream tempFileOs = new FileOutputStream(tempFile);) {
	// IOUtils.copy(in, tempFileOs);
	// logger.debug("Audio Data temp file written");
	// }
	// }

	/**
	 * Manually write a wav header
	 * 
	 * @param out
	 * @param format
	 * @param dataLength
	 * @throws IOException
	 */
	private void writeWavHeaders(OutputStream out, AudioFormat format, long dataLength) throws IOException {

		int myBitsPerSample = format.getSampleSizeInBits();
		int myFormat = 1; // PCM
		long myChannels = format.getChannels();
		float mySampleRate = format.getSampleRate();
		float myByteRate = mySampleRate * myChannels * myBitsPerSample / 8;
		int myBlockAlign = (int) (myChannels * myBitsPerSample / 8);

		// Header is 36 bytes long + RIFF
		long fileSize = 36 + dataLength;
		// Format is 16 bytes long
		long formatChunkSize = 16;

		// Basic Headers and Size
		IOUtils.write("RIFF", out);
		// how big is the rest of this file?
		IOUtils.write(intToByteArray((int) fileSize), out);
		IOUtils.write("WAVE", out);
		IOUtils.write("fmt ", out);

		// Format (1st 16 bits chunk)
		out.write(intToByteArray((int) formatChunkSize), 0, 4);
		out.write(shortToByteArray((short) myFormat), 0, 2);
		out.write(shortToByteArray((short) myChannels), 0, 2);
		out.write(intToByteArray((int) mySampleRate), 0, 4);
		out.write(intToByteArray((int) myByteRate), 0, 4);
		out.write(shortToByteArray((short) myBlockAlign), 0, 2);
		out.write(shortToByteArray((short) myBitsPerSample), 0, 2);

		// Data, prefixed by "data" and the chunk size
		IOUtils.write("data", out);
		out.write(intToByteArray((int) dataLength), 0, 4);
	}

	/**
	 * Write integer as 4 byte binary
	 * 
	 * @param i
	 * @return
	 */
	private static byte[] intToByteArray(int i)
	{
		byte[] b = new byte[4];
		b[0] = (byte) (i & 0x00FF);
		b[1] = (byte) ((i >> 8) & 0x000000FF);
		b[2] = (byte) ((i >> 16) & 0x000000FF);
		b[3] = (byte) ((i >> 24) & 0x000000FF);
		return b;
	}

	/**
	 * Write short a 2 byte binary
	 * 
	 * @param data
	 * @return
	 */
	public static byte[] shortToByteArray(short data)
	{
		return new byte[] { (byte) (data & 0xff), (byte) ((data >>> 8) & 0xff) };
	}
}
