package com.herazade.echonest.tools.core.project;

import java.io.File;
import java.nio.file.Path;

import org.springframework.util.StringUtils;

import com.echonest.api.v4.TrackAnalysis;

/**
 * EchoNestTools Project
 * 
 * @author drallieiv
 *
 */
public class EntProject {

	/**
	 * Unique Name for the projet
	 */
	private String projectName;

	/**
	 * Filesystem Path to the MP3 File
	 */
	private String mp3FilePath;

	/**
	 * EchoNest Track Id
	 */
	private String trackId;

	/**
	 * Track Analysis
	 */
	private TrackAnalysis analysis;

	/**
	 * Has the file been submitted to EchoNest
	 * 
	 * @return true if we have a Track ID from echonest
	 */
	public boolean isFileSubmitted() {
		return !StringUtils.isEmpty(trackId);
	}

	/**
	 * Has the file been submitted to EchoNest
	 * 
	 * @return true if we have the analysis
	 */
	public boolean hasAnalysis() {
		return analysis != null;
	}

	/**
	 * Access to MP3 File
	 * @return
	 */
	public File getMp3File() {
		if (mp3FilePath == null) {
			throw new IllegalArgumentException("Missing MP3 File Path");
		}
		return new File(mp3FilePath);
	}

	// BASIC GETTERS AND SETTERS

	/**
	 * @return the projectName
	 */
	public String getProjectName() {
		return projectName;
	}

	/**
	 * @param projectName
	 *            the projectName to set
	 */
	public void setProjectName(String projectName) {
		this.projectName = projectName;
	}

	/**
	 * @return the mp3FilePath
	 */
	public String getMp3FilePath() {
		return mp3FilePath;
	}

	/**
	 * @param mp3FilePath
	 *            the mp3FilePath to set
	 */
	public void setMp3FilePath(String mp3FilePath) {
		this.mp3FilePath = mp3FilePath;
	}

	/**
	 * @return the trackId
	 */
	public String getTrackId() {
		return trackId;
	}

	/**
	 * @param trackId
	 *            the trackId to set
	 */
	public void setTrackId(String trackId) {
		this.trackId = trackId;
	}

	/**
	 * @return the analysis
	 */
	public TrackAnalysis getAnalysis() {
		return analysis;
	}

	/**
	 * @param analysis
	 *            the analysis to set
	 */
	public void setAnalysis(TrackAnalysis analysis) {
		this.analysis = analysis;
	}

}
