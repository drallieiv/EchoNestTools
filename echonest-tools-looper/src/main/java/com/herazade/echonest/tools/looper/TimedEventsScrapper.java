package com.herazade.echonest.tools.looper;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.echonest.api.v4.Segment;
import com.echonest.api.v4.TimedEvent;
import com.echonest.api.v4.TrackAnalysis;
import com.herazade.echonest.tools.core.remix.AnalysisUtils;
import com.herazade.echonest.tools.core.remix.BeatsComparator;
import com.herazade.echonest.tools.core.remix.TimedEventComparator;
import com.herazade.echonest.tools.looper.jump.Jump;
import com.herazade.echonest.tools.looper.jump.JumpMap;
import com.herazade.echonest.tools.looper.jump.comparators.JumpComparatorByDistance;
import com.herazade.echonest.tools.looper.jump.comparators.JumpComparatorByDuration;

/**
 * Scrapper class to find interesting point in an audio file
 * 
 * @author drallieiv
 *
 */
public abstract class TimedEventsScrapper {

	// Logger
	private Logger logger = LoggerFactory.getLogger(getClass());

	// Configurations

	// max branches allowed per beat
	private int maxBranches = 4;
	// max allowed distance threshold
	private int maxBranchThreshold = 80;

	// Weights for average
	private double timbreWeight = 1;
	private double pitchWeight = 10;
	private double loudStartWeight = 1;
	private double loudMaxWeight = 1;
	private double durationWeight = 100;
	private double confidenceWeight = 1;
	
	// Skip Jumps smaller then this
	private double minimumJumpTime = 0;
	
	// Only Look for jumps to the past
	private boolean backWardOnly = false;

	// Tools
	private TimedEventComparator timedEventComparator;

	/**
	 * Map for all events and their neighbors
	 */
	private JumpMap jumpMap;

	public TimedEventsScrapper(TrackAnalysis analysis) {
		// Init Timed Event Compoarator and run analysis prepare
		timedEventComparator = new TimedEventComparator(analysis.getSegments(), getTimedEventsFromAnalysis(analysis));
		timedEventComparator.connectAllOverlappingSegments(analysis);
	}

	/**
	 * Access to the timed events of a specific type
	 * 
	 * @param analysis
	 * @return
	 */
	abstract List<TimedEvent> getTimedEventsFromAnalysis(TrackAnalysis analysis);

	/**
	 * Search for Jumps until we have enough jumps
	 * 
	 * @param jumpCount
	 *            required number of jumps
	 * @return Jump Map
	 */
	public JumpMap scrapForJumps(int jumpCount) throws ScrapperException {
		int initialTreshold = 0;
		int tresholdStep = 1;
		int maxTreshold = 50;
		int nbJumps = 0;
		
		while(nbJumps < jumpCount){
			initialTreshold += tresholdStep;
			if(initialTreshold > maxTreshold){
				logger.warn("Scrapping Treshold Reached");
				break;
			}
			buildJumpMap(initialTreshold);
			nbJumps = jumpMap.count();
		}
		
		return jumpMap;
	}

	/**
	 * Build jumpMap
	 * 
	 * @param analysis
	 *            Echonest Analysis
	 * @param treshold
	 *            to limit to the closest neighbors
	 * @return List of possible jumps from each TE.
	 */
	public JumpMap buildJumpMap(int treshold) {
		return this.buildJumpMap(timedEventComparator.getTimedEvents(), treshold);
	}

	/**
	 * Build jumpMap
	 * 
	 * @param quantums
	 *            list of TimedEvents
	 * @param treshold
	 *            to limit to the closest neighbors
	 * @return List of possible jumps from each TE.
	 */
	public JumpMap buildJumpMap(List<TimedEvent> quantums, int treshold) {
		jumpMap = new JumpMap();

		for (TimedEvent q : quantums) {
			jumpMap.put(q, extractJumps(q, quantums, treshold));
		}

		return jumpMap;
	}

	/**
	 * Neighbors Analysis. Based on calculateNearestNeighborsForQuantum from js
	 * looper.
	 * 
	 * @param teSrc
	 *            source TimedEvent analysed
	 * @param teAll
	 *            all TimedEvents in track
	 * @param treshold
	 *            limiter
	 * @return all TimeEvents that are neighbors;
	 */
	private List<Jump> extractJumps(TimedEvent teSrc, List<TimedEvent> teAll, double treshold) {

		List<Jump> jumps = new ArrayList<Jump>();
		List<Segment> sourceSegments = timedEventComparator.getSegments(teSrc);

		for (TimedEvent teDest : teAll) {
			if (teDest == teSrc) {
				// Skip Self Analysis
				continue;
			}
			
			if(backWardOnly && teSrc.getStart() < teDest.getStart()){
				// Skip Forward Jump
				// Note, if dest are in order we can quit now.
				continue;
			}
			
			if( Math.abs(teSrc.getStart() - teDest.getStart()) < minimumJumpTime){
				// Skip Jump too Small
				continue;
			}

			double sum = 0;

			List<Segment> testSegments = timedEventComparator.getSegments(teDest);
			int i = 0;
			for (Segment srcSegment : sourceSegments) {
				double distance = 100;
				if (i < testSegments.size()) {
					Segment tstSegment = testSegments.get(i);
					if (srcSegment != tstSegment) {
						distance = getSegDistances(srcSegment, tstSegment);
					}
				}
				sum += distance;
				i++;
			}

			double averageDistance = sum / i;

			// If average distance is below treshold we keep it
			if (averageDistance < treshold) {
				jumps.add(new Jump(teSrc, teDest, averageDistance));
			}

		}

		Collections.sort(jumps, JumpComparatorByDistance.getInstance());

		return jumps;
	}

	/**
	 * Get Distance between 2 segment
	 * 
	 * @param srcSegment
	 *            segment 1
	 * @param tstSegment
	 *            segment 2
	 * @return relative weighted distance
	 */
	private double getSegDistances(Segment srcSegment, Segment tstSegment) {
		double timbre = AnalysisUtils.weightedEuclideanDistance(srcSegment.getTimbre(), tstSegment.getTimbre());
		double pitch = AnalysisUtils.standardEuclideanDistance(srcSegment.getPitches(), tstSegment.getPitches());
		double sloudStart = Math.abs(srcSegment.getLoudnessStart() - tstSegment.getLoudnessStart());
		double sloudMax = Math.abs(srcSegment.getLoudnessMax() - tstSegment.getLoudnessMax());
		double duration = Math.abs(srcSegment.getDuration() - tstSegment.getDuration());
		double confidence = Math.abs(srcSegment.getConfidence() - tstSegment.getConfidence());

		return timbre * timbreWeight
				+ pitch * pitchWeight
				+ sloudStart * loudStartWeight
				+ sloudMax * loudMaxWeight
				+ duration * durationWeight
				+ confidence * confidenceWeight;

	}

	/**
	 * Count the total number of Neighbors scanned
	 * 
	 * @return
	 */
	public int countNeighbors() {
		int sum = 0;
		for (Entry<TimedEvent, List<Jump>> entry : jumpMap.entrySet()) {
			sum += entry.getValue().size();
		}
		return sum;
	}

	/**
	 * Count the total number of branches (possible jump starts)
	 * 
	 * @return
	 */
	public int countBranching() {
		int sum = 0;
		for (Entry<TimedEvent, List<Jump>> entry : jumpMap.entrySet()) {
			if (!entry.getValue().isEmpty()) {
				sum += 1;
			}
		}
		return sum;
	}

	/**
	 * @return the jumpMap
	 */
	public Map<TimedEvent, List<Jump>> getJumpMap() {
		return jumpMap;
	}

	/**
	 * @param backWardOnly the backWardOnly to set
	 */
	public void setBackWardOnly(boolean backWardOnly) {
		this.backWardOnly = backWardOnly;
	}

	/**
	 * @param minimumJumpTime the minimumJumpTime to set
	 */
	public void setMinimumJumpTime(double minimumJumpTime) {
		this.minimumJumpTime = minimumJumpTime;
	}
	
	

}
