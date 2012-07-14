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
	private Map<Integer,Integer> table;
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
		
		for(Tuple<Integer,Integer> customer : customers.values())
		{
			customer.first = customer.first/this.GCD;
		}
		this.monthlyTotal = this.monthlyTotal/this.GCD;

		this.table = new HashMap<Integer,Integer>();
		this.table.put(0, 0);
	}	

	public List<Tuple<String,Integer>> solve() {
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

				// find the best sum of values with a sum of impressions less than tempMax
				// use previous best sums
				if(i <= campaignSize)
				{
					table.put(i, 0);
				} else {
					int newSum = table.get(i - campaignSize) + campaignValue;
					if(table.get(i) == null || newSum > table.get(i)) {
						table.put(i, newSum);
					}
				}
			}
		}
		int temp = monthlyTotal;
		while(table.get(temp) == null)
		{
			temp--;
		}
		System.out.println(table.get(temp));
		return null;
	}
}
