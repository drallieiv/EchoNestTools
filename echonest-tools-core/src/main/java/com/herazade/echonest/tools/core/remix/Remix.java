package com.herazade.echonest.tools.core.remix;

import java.util.ArrayList;
import java.util.List;

import com.echonest.api.v4.Segment;

public class Remix {

	/**
	 * Treshow for segment similarity
	 */
	private static final int SIMILAR_TRESHOLD = 1;

	public Remix() {
		// TODO Auto-generated constructor stub
	}

	// Reduce the number of segments by merging them if similar
	public List<Segment> filterSegments(List<Segment> allSegments) {
		double threshold = .3;
		List<Segment> fsegs = new ArrayList<Segment>();

		// Add first Segment
		fsegs.add(allSegments.get(0));

		for (Segment segment : allSegments) {
			Segment lastSegment = fsegs.get(fsegs.size() - 1);
			if (isSimilar(segment, lastSegment) && segment.getConfidence() < threshold) {
				// Update last Segment duration
				lastSegment.setDuration(lastSegment.getDuration() + segment.getDuration());
			}
		}
		return fsegs;

	}

	/**
	 * Checks if 2 segments looks similar
	 * @param segment1
	 * @param segment2
	 * @return true if distance is below SIMILAR_TRESHOLD
	 */
	private boolean isSimilar(Segment segment1, Segment segment2) {
		double threshold = SIMILAR_TRESHOLD;
		double distance = AnalysisUtils.standardEuclideanDistance(segment1.getTimbre(), segment2.getTimbre());
		
		return (distance < threshold);
	}

}
