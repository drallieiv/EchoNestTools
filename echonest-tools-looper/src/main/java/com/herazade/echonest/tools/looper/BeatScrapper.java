package com.herazade.echonest.tools.looper;

import java.util.List;

import com.echonest.api.v4.TimedEvent;
import com.echonest.api.v4.TrackAnalysis;

/**
 * Scrapper based on beats
 * @author drallieiv
 *
 */
public class BeatScrapper extends TimedEventsScrapper {

	public BeatScrapper(TrackAnalysis analysis) {
		super(analysis);
	}

	@Override
	List<TimedEvent> getTimedEventsFromAnalysis(TrackAnalysis analysis) {
		return analysis.getBeats();
	}

}
