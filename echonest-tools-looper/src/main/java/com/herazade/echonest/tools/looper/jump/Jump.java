package com.herazade.echonest.tools.looper.jump;

import com.echonest.api.v4.TimedEvent;

/**
 * Jump action for a TimedEvent to another
 * 
 * @author drallieiv
 *
 */
public class Jump {

	private TimedEvent source;

	private TimedEvent destination;

	private double distance;

	public Jump(TimedEvent source, TimedEvent destination, double distance) {
		super();
		this.source = source;
		this.destination = destination;
		this.distance = distance;
	}

	public double from() {
		return source.getStart();
	}

	public double to() {
		return destination.getStart();
	}

	/**
	 * Absolute value of the jump lenght
	 * 
	 * @return duration in seconds
	 */
	public double getJumpTime() {
		return Math.abs(to() - from());
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "[" + timeFormat(from()) + " > " + timeFormat(to()) + " {" + from() + " > " + to() + "} ( " + (int) (to() - from()) + "s *" + String.format("%.2f", distance) + "*) ]";
	}

	private String timeFormat(double time) {
		String timeSeparator = ":";
		String msTimeSeparator = ".";
		String twoDigitFormat = "%02d";
		String threeDigitFormat = "%03d";

		int hours = (int) (time / 3600);
		int minutes = (int) (time % 3600 / 60);
		int seconds = (int) (time % 60);
		int ms = (int) ((time * 1000) % 60);
		StringBuilder sb = new StringBuilder();
		if (hours > 0) {
			sb.append(String.format(twoDigitFormat, hours)).append(timeSeparator);
		}
		sb.append(String.format(twoDigitFormat, minutes)).append(timeSeparator);
		sb.append(String.format(twoDigitFormat, seconds)).append(msTimeSeparator);
		sb.append(String.format(threeDigitFormat, ms));
		return sb.toString();
	}

	/**
	 * @return the source
	 */
	public TimedEvent getSource() {
		return source;
	}

	/**
	 * @return the destination
	 */
	public TimedEvent getDestination() {
		return destination;
	}

	/**
	 * @return the distance
	 */
	public double getDistance() {
		return distance;
	}

}
