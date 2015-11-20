package com.herazade.echonest.tools.core.remix;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.echonest.api.v4.Segment;
import com.echonest.api.v4.TimedEvent;
import com.echonest.api.v4.TrackAnalysis;

public class TimedEventComparator {

	private List<Segment> segments;

	private List<TimedEvent> timedEvents;

	private Map<TimedEvent, List<Segment>> overlappingSegmentsMap;

	public TimedEventComparator(List<Segment> segments, List<TimedEvent> timedEvents) {
		this.segments = segments;
		this.timedEvents = timedEvents;
	}

	/**
	 * Create a maps telling which segments
	 * 
	 * @param analysis
	 *            TrackAnalysis
	 */
	public void connectAllOverlappingSegments(TrackAnalysis analysis) {

		if (this.segments == null || this.timedEvents == null) {
			throw new IllegalArgumentException("Segments or timedEvents not set");
		}

		overlappingSegmentsMap = new HashMap<TimedEvent, List<Segment>>();

		// Loop Everything. Could be improved but we don't really care
		for (TimedEvent timedEvent : timedEvents) {
			ArrayList<Segment> overlappingSegments = new ArrayList<Segment>();
			for (Segment segment : segments) {
				// Segment ends before TimeEvent starts
				if (segment.getStart() + segment.getDuration() < timedEvent.getStart()) {
					continue;
				}
				// Segment starts after TimeEvent ends
				if (segment.getStart() > timedEvent.getStart() + timedEvent.getDuration()) {
					break;
				}
				// Else add Segment to list
				overlappingSegments.add(segment);
			}
			overlappingSegmentsMap.put(timedEvent, overlappingSegments);
		}
	}

	/**
	 * Access to segment mapping
	 * @param timedEvent a timedEvent
	 * @return list of segment for a given timedEvent
	 */
	public List<Segment> getSegments(TimedEvent timedEvent) {
		return overlappingSegmentsMap.get(timedEvent);
	}

	/**
	 * @param segments
	 *            the segments to set
	 */
	public void setSegments(List<Segment> segments) {
		this.segments = segments;
	}

	/**
	 * @param timedEvents
	 *            the timedEvents to set
	 */
	public void setTimedEvents(List<TimedEvent> timedEvents) {
		this.timedEvents = timedEvents;
	}

}
