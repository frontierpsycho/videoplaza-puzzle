package org.videoplaza;

import java.util.Map;
import java.util.List;

/**
 * Interface that solves the knapsack problem for a given 
 * maximum capacity and a set of weights and values.
 */
public abstract class PuzzleSolver {
	protected int monthlyTotal;
	protected Map<String,Tuple<Integer,Integer>> customers;

	public PuzzleSolver(int monthlyTotal, Map<String,Tuple<Integer,Integer>> customers) {
		this.monthlyTotal = monthlyTotal;
		this.customers = customers;
	}

	/**
	 * Solves the knapsack problem for the given values. 
	 *
	 * @return A map matching campaign names to times used per campaign (a solution to the knapsack problem).
	 */
	public abstract Map<String,Integer> solve();
}
