package com.herazade.echonest.tools.looper.jump.comparators;

import java.util.Comparator;

import com.herazade.echonest.tools.looper.jump.Jump;

/**
 * Compare 2 Jumps by Duration
 * 
 * @author drallieiv
 */
public class JumpComparatorByDuration implements Comparator<Jump> {

	public static JumpComparatorByDuration getInstance(){
		return new JumpComparatorByDuration();
	}
	
	@Override
	public int compare(Jump j1, Jump j2) {
		return Double.compare(j1.getJumpTime(), j2.getJumpTime());	
	}

}
