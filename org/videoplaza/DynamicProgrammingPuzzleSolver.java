package org.videoplaza;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Iterator;
import java.util.Map;
import java.util.HashMap;

public class DynamicProgrammingPuzzleSolver extends PuzzleSolver {
	// this will hold the best sums and combinations for each max value
	private Map<Integer,Tuple<Integer, Map<String,Integer>>> table;
	private List<String> customerNameList;
	private int GCD;

	public DynamicProgrammingPuzzleSolver(int monthlyTotal, Map<String,Tuple<Integer,Integer>> customers) {
		super(monthlyTotal, customers);

		this.customerNameList = new ArrayList<String>(customers.keySet());

		Iterator<Tuple<Integer,Integer>> iter = customers.values().iterator();
		BigInteger GCD = BigInteger.valueOf(iter.next().first);

		for(; iter.hasNext();)
		{
			GCD = GCD.gcd(BigInteger.valueOf(iter.next().first));
		}
		this.GCD = GCD.intValue();
		
		divideByGCD();

		this.table = new HashMap<Integer,Tuple<Integer, Map<String,Integer>>>();
		for(int i = 0; i < this.monthlyTotal; i++)
		{
			initTableRow(i);
		}
	}	

	public Map<String,Integer> solve() {
		Collections.sort(customerNameList, new Comparator<String>() {
			public int compare(String a, String b) {
				Tuple<Integer,Integer> customerA = customers.get(a);
				Tuple<Integer,Integer> customerB = customers.get(b);

				// sort by value per impression, descending
				return Float.compare((float)customerB.second/customerB.first, (float)customerA.second/customerA.first);
			}
		});

		for(String customerName : customerNameList)
		{
			for(int i = 1; i < monthlyTotal; i++)
			{
				int campaignSize = customers.get(customerName).first;
				int campaignValue = customers.get(customerName).second;

				if(i >= campaignSize)
				{
					int newSum = table.get(i - campaignSize).first + campaignValue;
					Tuple<Integer,Map<String,Integer>> existingRow = table.get(i);

					// if new sum is better than the one achieved so far
					// keep it and replace combination
					if(newSum > existingRow.first) {
						existingRow.first = newSum;

						// fetch table from i - campaignSize and add new element
						Map<String,Integer> combinationWithout = table.get(i - campaignSize).second;

						int newCount = combinationWithout.containsKey(customerName) ? combinationWithout.get(customerName) + 1 : 1;

						// replace combination with updated one to match the new sum
						existingRow.second.clear();
						existingRow.second.putAll(combinationWithout);
						existingRow.second.put(customerName, newCount);
					}
				}
			}
		}
		int temp = monthlyTotal;
		while(table.get(temp) == null)
		{
			temp--;
		}

		restoreByGCD();
		
		Map<String,Integer> result = table.get(temp).second;

		return result;
	}

	private void initTableRow(int i)
	{
		Map<String,Integer> bestComb = new HashMap<String,Integer>(customers.size());
		this.table.put(i, new Tuple<Integer,Map<String,Integer>>(0, bestComb));
	}

	private void divideByGCD()
	{
		for(Tuple<Integer,Integer> customer : this.customers.values())
		{
			customer.first = customer.first/this.GCD;
		}
		this.monthlyTotal = this.monthlyTotal/this.GCD;
	}

	private void restoreByGCD()
	{
		for(Tuple<Integer,Integer> customer : this.customers.values())
		{
			customer.first = customer.first*this.GCD;
		}
		this.monthlyTotal = this.monthlyTotal*this.GCD;
	}
}
