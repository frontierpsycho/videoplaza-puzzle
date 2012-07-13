package org.videoplaza;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.ListIterator;
import java.util.NoSuchElementException;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

public class BruteForcePuzzleSolver extends PuzzleSolver {
	private List<String> currentCombination;
	private int currentIndex;
	private int totalSum;
	private List<String> customerNameList;

	public BruteForcePuzzleSolver(int monthlyTotal, Map<String,Tuple<Integer,Integer>> customers) {
		super(monthlyTotal, customers);
		this.currentIndex = 0;
		this.totalSum = 0;

		// determine the maximum amount of times any campaign fits into the monthly quota
		int maxTimes = 0;
		for(Map.Entry<String,Tuple<Integer,Integer>> customer : customers.entrySet()) {
			maxTimes = Math.max(maxTimes, monthlyTotal / customer.getValue().first);
		}

		this.currentCombination = new ArrayList<String>(maxTimes);
		this.customerNameList = new ArrayList<String>(customers.keySet());
	}


	public List<Tuple<String,Integer>> solve() {

		// sort by impressions per campaign, ascending
		Collections.sort(customerNameList, new Comparator<String>() {
			public int compare(String a, String b) {
				return customers.get(a).first - customers.get(b).first;
			}
		});

		for(int i = currentCombination.size(); i > 0; i--)
		{
			// init
			int sum = 0;
			currentCombination.clear();

			// initialize current combination with first combination
			for(int currentIndex = 0; currentIndex < i; currentIndex++)
			{
				currentCombination.add(customerNameList.get(0));
			}

			while(nextCombination(currentCombination))
			{
				System.out.println(currentCombination);
			}
		}
		return null;
	}

	private int combinationSum(List<String> comb)
	{
		int sum = 0;
		for(String campaign : comb)
		{
			sum += customers.get(campaign).second;
		}

		return sum;
	}

	private boolean nextCombination(List<String> combination)
	{
		ListIterator<String> iter = combination.listIterator();

		int customersSize = customerNameList.size();
		boolean flipped = true;

		while(flipped)
		{
			flipped = false;

			try {
				String campaign = iter.next();
				int index = customerNameList.indexOf(campaign);

				// if this was the last campaign, re-init and cause the next one to be incremented
				if(index + 1 == customersSize)
				{
					iter.set(customerNameList.get(0));
					flipped = true;
				} else {
					iter.set(customerNameList.get(index + 1));
				}
			} catch (NoSuchElementException nsee) {
				// flipping reached the last element
				// this was the last combination
				return false;
			}	
		}
		return true;
	}
}
