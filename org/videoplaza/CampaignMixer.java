package org.videoplaza;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * CampaignMixer finds the best combination of campaigns to be 
 * sold to clients of our Imaginary Ad Company (R), depending
 * on a monthly total number of impressions available, and a
 * set of ad campaigns.
 *
 * The implementation is decoupled from the algorith actually used 
 * to calculate the best combination. An interface, PuzzleSolver,
 * is used to solve the problem. 
 *
 * This class has a main function that reads the input of the problem,
 * handles errors, collects the output and returns it in the specified
 * form.
 *
 * @author Alexander Altanis
 * @version 1.0
 */
public class CampaignMixer {
	public static void main(String[] args) {
		if (args.length < 1) {
			System.err.println("ERROR: Path to input file not provided");
			System.exit(1);
		}
		String filename = args[0];
		int monthlyTotal = 0;

		Map<String,Tuple<Integer,Integer>> customers = new LinkedHashMap<String,Tuple<Integer,Integer>>();

		try {
			BufferedReader br = new BufferedReader(new FileReader(filename));

			monthlyTotal = new Integer(br.readLine());

			String line;
			while ((line = br.readLine()) != null) {
				String[] tokens = line.split(",");
				try {
					customers.put(tokens[0], new Tuple<Integer,Integer>(new Integer(tokens[1]), new Integer(tokens[2])));
				} catch (ArrayIndexOutOfBoundsException aioobe) {
					System.err.println("ERROR: Malformed line in input: '"+line+"', not enough elements");
					System.exit(1);
				} catch (NumberFormatException nfe) {
					System.err.println("ERROR: Malformed line in input: '"+line+"', not a number");
					System.exit(1);
				}
			}
		} catch (IOException ioe) {
			System.err.println("ERROR: Error reading file '"+filename+"'");
			System.exit(1);
		}

		PuzzleSolver solver = new DynamicProgrammingPuzzleSolver(monthlyTotal, customers);
		Map<String,Integer> solution = solver.solve();

		int value = 0;
		int impressions = 0;
		for(Map.Entry<String,Integer> entry : solution.entrySet())
		{
			int number = entry.getValue();
			int customerImpressions = number * customers.get(entry.getKey()).first;
			int customerValue = number * customers.get(entry.getKey()).second;

			System.out.println(entry.getKey()+","+entry.getValue()+","+customerImpressions+","+customerValue);

			impressions += customerImpressions;
			value += customerValue;
		}
		System.out.println(impressions+","+value);

	}	
}

/**
 * A generic Tuple implementation for use with the CampaignMixer.
 */
class Tuple<A,B> {
	// public because there is no need in this class for
	// austere access restrictions
	public A first;
	public B second;

	public Tuple(A first, B second) {
		this.first = first;
		this.second = second;
	}

	public String toString() {
		return "("+first.toString()+", "+second.toString()+")";
	}
}
