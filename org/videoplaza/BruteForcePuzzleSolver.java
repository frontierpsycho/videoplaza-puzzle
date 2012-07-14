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
	private int currentIndex, bestSum, maxTimes;
	private List<String> customerNameList;
	private List<String> bestCombination;

	public BruteForcePuzzleSolver(int monthlyTotal, Map<String,Tuple<Integer,Integer>> customers) {
		super(monthlyTotal, customers);
		this.currentIndex = 0;
		this.bestSum = 0;

		// determine the maximum amount of times any campaign fits into the monthly quota
		this.maxTimes = 0;
		for(Map.Entry<String,Tuple<Integer,Integer>> customer : customers.entrySet()) {
			this.maxTimes = Math.max(this.maxTimes, monthlyTotal / customer.getValue().first);
		}

		this.currentCombination = new ArrayList<String>(maxTimes);
		this.bestCombination = new ArrayList<String>(maxTimes);
		this.customerNameList = new ArrayList<String>(customers.keySet());
	}


	public List<Tuple<String,Integer>> solve() {

		// sort by impressions per campaign, ascending
		Collections.sort(customerNameList, new Comparator<String>() {
			public int compare(String a, String b) {
				return customers.get(a).first - customers.get(b).first;
			}
		});

		for(int i = maxTimes; i > 0; i--)
		{
			System.out.println("Trying combinations of "+i);

			// init
			int sum = 0;
			currentCombination.clear();

			// initialize current combination with first combination
			for(int currentIndex = 0; currentIndex < i; currentIndex++)
			{
				currentCombination.add(customerNameList.get(0));
				// FIXME check the first one as well!
			}

			while(nextCombination(currentCombination))
			{
				sum = examineCombination(currentCombination);
				/*if(sum == -1)
				  {
				  continue nextCombination;
				  } else */if (sum >= bestSum) {
					  bestSum = sum;
					  bestCombination = new ArrayList<String>(currentCombination);
				  }
			}
		}
		System.out.println(bestCombination);
		System.out.println(examineCombination(bestCombination));
		return null;
	}

	private boolean nextCombination(List<String> combination)
	{
		ListIterator<String> iter = combination.listIterator();

		boolean flipped = true;

		while(flipped)
		{
			flipped = false;

			try {
				String campaign = iter.next();
				int index = customerNameList.indexOf(campaign);

				// if this was the last campaign, re-init and cause the next one to be incremented
				if(index + 1 == customerNameList.size())
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

	private int examineCombination(List<String> combination)
	{
		int impressionSum = 0;
		int incomeSum = 0;

		for(String campaign : combination)
		{
			impressionSum += customers.get(campaign).first;
			if(impressionSum > monthlyTotal)
			{
				// monthly total exceeded, invalid combination
				return -1;
			}
			incomeSum += customers.get(campaign).second;
		}
		return incomeSum;
	}
}
