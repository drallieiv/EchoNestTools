package com.herazade.echonest.tools.looper.jump;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Collectors;

import com.echonest.api.v4.TimedEvent;
import com.herazade.echonest.tools.looper.jump.comparators.JumpComparatorByDistance;

public class JumpMap extends HashMap<TimedEvent, List<Jump>>{

	/**
	 * Count Jumps
	 * @return number of jumps
	 */
	public int count(){
		return entrySet().stream().mapToInt(e -> e.getValue().size()).sum();	
	}
	
	/**
	 * Drop timedEvents with no Jump found
	 * @return filtered JumpMap
	 */
	public JumpMap filterEmpty(){
		JumpMap newMap = new JumpMap();
		newMap.putAll(entrySet().stream().filter(e -> e.getValue().size() > 0).collect(Collectors.toMap(e -> e.getKey(), e -> e.getValue())));
		return newMap;
	}

	/**
	 * Sort jumps by distance
	 * @return
	 */
	public List<Jump> getJumpList(){
		return entrySet().stream().flatMap(e -> e.getValue().stream()).sorted(new JumpComparatorByDistance()).collect(Collectors.toList());
	}

	// Only Log Jumps
	public String toString(){
		StringBuilder sb = new StringBuilder();
		for(Jump j : getJumpList()){
			sb.append(System.getProperty("line.separator")).append(j.toString());
		}
		return sb.toString();
	}
}
