package com.herazade.echonest.tools.core.remix;

/**
 * Utility Class for basic Analysis
 * 
 * @author drallieiv
 *
 */
public class AnalysisUtils {

	private AnalysisUtils() {

	}

	public static double euclideanDistance(double[] l1, double[] l2, boolean weighted) {
		if(weighted){
			return weightedEuclideanDistance(l1, l2);
		}else{
			return standardEuclideanDistance(l1, l2);
		}
	}
	
	/**
	 * Computes the euclidean distance between 2 lists
	 * 
	 * @param l1
	 *            list of doubles
	 * @param l2
	 *            list of doubles
	 * @return euclidean distance
	 */
	public static double standardEuclideanDistance(double[] l1, double[] l2) {

		if (l1.length != l2.length) {
			throw new IllegalArgumentException("Both list must have the same number of elements");
		}

		double sum = 0;

		for (int i = 0; i < l1.length; i++) {
			double delta = (l2[i] - l1[i]);
			sum += delta * delta;
		}

		return Math.sqrt(sum);
	}

	/**
	 * Computes the euclidean distance between 2 lists with more weight on first elements.
	 * 
	 * @param l1
	 *            list of doubles
	 * @param l2
	 *            list of doubles
	 * @return euclidean distance
	 */
	public static double weightedEuclideanDistance(double[] l1, double[] l2) {

		if (l1.length != l2.length) {
			throw new IllegalArgumentException("Both list must have the same number of elements");
		}

		double sum = 0;

		for (int i = 0; i < l1.length; i++) {
			double delta = (l2[i] - l1[i]);
			double weightRatio = (1 / (i + 1));
			sum += delta * delta * weightRatio;
		}

		return Math.sqrt(sum);
	}
}
