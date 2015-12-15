package com.herazade.echonest.tools.looper.jump;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.echonest.api.v4.TimedEvent;
import com.herazade.echonest.tools.core.cli.TimedEventHelper;

/**
 * Utility Class for Jumps
 * 
 * @author drallieiv
 *
 */
public class Jumps {

	// Logger
	private static Logger logger = LoggerFactory.getLogger(Jumps.class);
	
	private Jumps() {
		// hidden Constructor
	}

	public static List<TimedEvent> toTimedEvents(Collection<Jump> jumps, double start, double end) {
		List<TimedEvent> mix = new ArrayList<TimedEvent>();

		// Current Position
		double marker = start;

		for (Jump jump : jumps) {
			if (jump.getSource().getStart() < marker) {
				throw new IllegalArgumentException("Impossible Jump " + jump.toString() + " from position " + marker);
			}
			mix.add(TimedEventHelper.buildTimeEvent(marker, jump.getSource().getStart()));
			marker = jump.getDestination().getStart();
		}
		
		// Add Last Segment
		if(marker > end){
			logger.warn("End is after current position. Skip last part");
		}else{
			if(marker != end){
				mix.add(TimedEventHelper.buildTimeEvent(marker, end));
			}
		}
		
		return mix;
	}
}
