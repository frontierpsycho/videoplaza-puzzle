package org.videoplaza;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

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

		System.out.println("Input read.");
		System.out.println("Montly total impressions: "+monthlyTotal);
		System.out.println("Customer campaigns:\n"+customers);

		PuzzleSolver solver = new DynamicProgrammingPuzzleSolver(monthlyTotal, customers);
		solver.solve();

	}	
}

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
