package com.herazade.echonest.tools.looper.remix.strategy;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.echonest.api.v4.TimedEvent;
import com.echonest.api.v4.TrackAnalysis;
import com.herazade.echonest.tools.core.cli.TimedEventHelper;
import com.herazade.echonest.tools.core.remix.strategy.RemixException;
import com.herazade.echonest.tools.core.remix.strategy.RemixStrategy;
import com.herazade.echonest.tools.looper.BeatScrapper;
import com.herazade.echonest.tools.looper.ScrapperException;
import com.herazade.echonest.tools.looper.jump.Jump;
import com.herazade.echonest.tools.looper.jump.JumpMap;
import com.herazade.echonest.tools.looper.jump.Jumps;
import com.herazade.echonest.tools.looper.jump.comparators.JumpComparatorByDistance;

/**
 * Simple Strategy to make song longer
 * 
 * @author drallieiv
 *
 */
public class SimpleLooperRemixStrategy implements RemixStrategy {

	// Logger
	private Logger logger = LoggerFactory.getLogger(getClass());

	/**
	 * Should the start be included
	 */
	public boolean keepStart = true;

	/**
	 * Should the End be included
	 */
	public boolean keepEnd = true;

	/**
	 * Minimum time in seconds for remix
	 */
	public double minimumTime;

	/**
	 * Quality : <br>
	 * 100 = Use only best loop 0 = Use longest loop
	 */
	public int quality = 80;

	@Override
	public List<TimedEvent> getRemix(TrackAnalysis analysis) {

		logger.info("Build Simple Looper Remix with minimum time of {}s", minimumTime);

		List<TimedEvent> remix = new ArrayList<TimedEvent>();

		try {

			Double trackDuration = analysis.getDuration();

			// Requested time is not longer then track
			if (minimumTime < trackDuration) {
				remix.add(new TimedEvent(0, trackDuration));
				return remix;
			}

			BeatScrapper scrapper = new BeatScrapper(analysis);
			scrapper.setBackWardOnly(true);
			scrapper.setMinimumJumpTime(trackDuration * 0.04);

			// Search Jumps
			int nbJumps = 30;
			logger.info("Scrapping for potential Jumps");
			JumpMap jumpMap = scrapper.scrapForJumps(nbJumps);
			jumpMap = jumpMap.filterEmpty();

			if (jumpMap.isEmpty()) {
				throw new RemixException("Not a single Jump possibility Found");
			}

			// Sort Jumps by Distance
			List<Jump> jumps = jumpMap.getJumpList();

			Jump bestJump = jumps.iterator().next();

			logger.debug("Best Quality Jump : {}", bestJump);

			Jump selectedJump;

			if (quality == 0) {
				selectedJump = jumps.stream().sorted(new JumpComparatorByDistance()).findFirst().get();
			} else {
				double maxDistance = Math.pow(100d / quality, 2) * bestJump.getDistance() * 100 / 80;
				logger.debug("Allow jump below distance : {}", String.format("%.5f", maxDistance));
				selectedJump = jumps.stream().filter(j -> j.getDistance() <= maxDistance).sorted(new JumpComparatorByDistance().reversed()).findFirst().get();
				logger.debug("Selected Jump : {}", selectedJump);
			}

			// Create easier handlers
			double loopStartTime = selectedJump.to();
			double loopEndTime = selectedJump.from();

			// Compute how many times the loop has to be repeated
			double loopingTimeNeeded = minimumTime;

			if (keepStart) {
				loopingTimeNeeded = loopingTimeNeeded - loopStartTime;
			}

			if (keepEnd) {
				loopingTimeNeeded = loopingTimeNeeded - (trackDuration - loopEndTime);
			}
			int nbLoopsRequired = (int) Math.ceil(loopingTimeNeeded / selectedJump.getJumpTime());

			// Make Remix
			List<Jump> remixJumps = new ArrayList<Jump>();

			double remixStart = 0;
			double remixEnd = trackDuration;

			// Add Start if Requested
			if (keepStart) {
				logger.debug("Start from beginning");
			} else {
				logger.debug("Start at beginning of loop");
				remixStart = loopStartTime;
			}

			// Add End if Requested
			if (keepEnd) {
				logger.debug("End at end of original file");
			} else {
				remixEnd = loopEndTime;
			}

			// Center Loop
			logger.debug("Add Loop {} times", nbLoopsRequired);
			for (int i = 0; i < nbLoopsRequired; i++) {
				remixJumps.add(selectedJump);
			}

			// Create Remix
			remix = Jumps.toTimedEvents(remixJumps, remixStart, remixEnd);

			double totalTime = remix.stream().mapToDouble(te -> te.getDuration()).sum();

			logger.info("Total Remix Time : {}s", String.format("%.0f", totalTime));

			return remix;
		} catch (ScrapperException e) {
			throw new RemixException("Scrapper Error", e);
		}
	}

	/**
	 * @param keepStart
	 *            the keepStart to set
	 */
	public void setKeepStart(boolean keepStart) {
		this.keepStart = keepStart;
	}

	/**
	 * @param keepEnd
	 *            the keepEnd to set
	 */
	public void setKeepEnd(boolean keepEnd) {
		this.keepEnd = keepEnd;
	}

	/**
	 * @param minimumTime
	 *            the minimumTime to set
	 */
	public void setMinimumTime(double minimumTime) {
		this.minimumTime = minimumTime;
	}

	/**
	 * @param quality
	 *            the quality to set
	 */
	public void setQuality(int quality) {
		this.quality = quality;
	}

}
