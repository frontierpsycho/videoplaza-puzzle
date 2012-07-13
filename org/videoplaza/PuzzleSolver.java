package org.videoplaza;

import java.util.Map;
import java.util.List;

public abstract class PuzzleSolver {
	protected int monthlyTotal;
	protected Map<String,Tuple<Integer,Integer>> customers;

	public PuzzleSolver(int monthlyTotal, Map<String,Tuple<Integer,Integer>> customers) {
		this.monthlyTotal = monthlyTotal;
		this.customers = customers;
	}

	public abstract List<Tuple<String,Integer>> solve();
}
