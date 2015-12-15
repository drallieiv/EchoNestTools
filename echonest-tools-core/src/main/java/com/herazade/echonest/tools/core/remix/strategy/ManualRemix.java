package com.herazade.echonest.tools.core.remix.strategy;

import java.util.ArrayList;
import java.util.List;

import com.echonest.api.v4.TimedEvent;
import com.echonest.api.v4.TrackAnalysis;
import com.herazade.echonest.tools.core.cli.TimedEventHelper;
import com.thoughtworks.xstream.annotations.XStreamAlias;

@XStreamAlias("ManualRemix")
public class ManualRemix implements RemixStrategy{

	private List<TimedEvent> remix;
	
	/**
	 * Init a Manual Remix
	 * @return remix for builder pattern
	 */
	public static ManualRemix buildNew(){
		ManualRemix manualRemix = new ManualRemix();
		manualRemix.remix = new ArrayList<TimedEvent>();
		return manualRemix;
	}
	
	/**
	 * Add part to manual Remix 
	 * @param start
	 * @param end
	 * @return remix for builder pattern
	 */
	public ManualRemix addPart(double start, double end){
		remix.add(TimedEventHelper.buildTimeEvent(start, end));
		return this;
	}
	
	@Override
	public List<TimedEvent> getRemix(TrackAnalysis analysis) {
		return remix;
	}


}
