package com.herazade.echonest.tools.core.remix;

import com.echonest.api.v4.TrackAnalysis;

public class BeatsComparator extends TimedEventComparator {

	public BeatsComparator(TrackAnalysis analysis) {
		super(analysis.getSegments(), analysis.getBeats());
	}


}
