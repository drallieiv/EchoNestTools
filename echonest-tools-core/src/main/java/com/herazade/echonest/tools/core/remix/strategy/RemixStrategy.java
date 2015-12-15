package com.herazade.echonest.tools.core.remix.strategy;

import java.util.List;

import com.echonest.api.v4.TimedEvent;
import com.echonest.api.v4.TrackAnalysis;

/**
 * Common interface for all remixes
 * 
 * @author drallieiv
 *
 */
public interface RemixStrategy {

	/**
	 * Procedural Remix based on analysis
	 * 
	 * @param analysis
	 * @return
	 */
	public List<TimedEvent> getRemix(TrackAnalysis analysis);
}
