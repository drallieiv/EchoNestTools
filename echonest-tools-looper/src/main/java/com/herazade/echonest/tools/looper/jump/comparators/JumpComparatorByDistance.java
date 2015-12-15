package com.herazade.echonest.tools.looper.jump.comparators;

import java.util.Comparator;

import com.herazade.echonest.tools.looper.jump.Jump;

/**
 * Compare 2 Jumps by Distance
 * 
 * @author drallieiv
 */
public class JumpComparatorByDistance implements Comparator<Jump> {

	public static JumpComparatorByDistance getInstance(){
		return new JumpComparatorByDistance();
	}
	
	@Override
	public int compare(Jump j1, Jump j2) {
		return Double.compare(j1.getDistance(), j2.getDistance());	
	}

}
