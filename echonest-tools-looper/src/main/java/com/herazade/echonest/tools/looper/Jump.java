package com.herazade.echonest.tools.looper;

import com.echonest.api.v4.TimedEvent;

/**
 * Jump action for a TimedEvent to another
 * 
 * @author drallieiv
 *
 */
public class Jump implements Comparable<Jump> {

	private TimedEvent source;

	private TimedEvent destination;

	private double distance;

	public Jump(TimedEvent source, TimedEvent destination, double distance) {
		super();
		this.source = source;
		this.destination = destination;
	}

	@Override
	public int compareTo(Jump o) {
		if (distance == o.distance) {
			return 0;
		}

		return distance < o.distance ? -1 : 1;
	}

}
