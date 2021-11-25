package decisionTree;

import java.util.Scanner;
import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;

class AttributeSet { 
	
	Attribute[] attrs = {};
	int catIndex;
	
	public AttributeSet(String dsName) { 
		ArrayList<Attribute> tempAL = new ArrayList<Attribute>();
		String fName = "data/" + dsName + "-attr.txt";
		Scanner sc = null;
		try { 
			sc = new Scanner(new BufferedReader((new FileReader(fName))));
		}catch (java.io.IOException e) { 
			System.out.println(e.getMessage());
		}
		int index = 0;		
		while (sc.hasNext()) { 
			tempAL.add(new Attribute(sc.nextLine(), index++));
		}
		catIndex = tempAL.size() - 1;
		tempAL.get(catIndex).catAttr = true;
		attrs = tempAL.toArray(attrs);
	}
	
	public void print(){
		for (Attribute attr: attrs) {
			attr.print();
		}
		System.out.println("\n");
	}
}
