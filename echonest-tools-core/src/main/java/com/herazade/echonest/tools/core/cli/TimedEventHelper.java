package com.herazade.echonest.tools.core.cli;

import com.echonest.api.v4.TimedEvent;

/**
 * Helper Class
 * 
 * @author drallieiv
 *
 */
public class TimedEventHelper {

	private TimedEventHelper() {

	}

	public static TimedEvent buildTimeEvent(double start, double end) {
		if(start > end){
			throw new IllegalArgumentException("Start must be before End");
		}
		double duration = end - start;
		return new TimedEvent(start, duration);
	}

}
