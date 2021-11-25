package decisionTree;

import java.util.Scanner;
import java.util.Arrays; 

class DataItem {
	
	String[] fields;
	boolean testResult;
	
	public DataItem(String line,int size) {
		fields = new String[size];
		Scanner sc = new Scanner(line);
		int i = 0;
		while (sc.hasNext()) { 
			fields[i++] = sc.next();
		}
		testResult = false;
	}
	
	public void print() { 
		System.out.println(Arrays.toString(fields));
	}
}
