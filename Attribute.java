package decisionTree;

import java.util.Scanner;
import java.util.Arrays;
import java.util.ArrayList;

class Attribute {
	String name;
	String[] vals = {};
	boolean cont;
	float[] dThresholds;
	int index;
	boolean catAttr;

	public Attribute(String line, int index) {
		Scanner sc = new Scanner(line);
		name = sc.next();
		String temp = sc.next();
		if (temp.equals("continuous")) {
			cont = true;
		} else {
			ArrayList<String> tempAL = new ArrayList<String>();
			tempAL.add(temp);
			while (sc.hasNext()) {
				tempAL.add(sc.next());
			}
			vals = tempAL.toArray(vals);
		}
		this.index = index;
		this.catAttr = false;
	}

	public void print() {
		System.out.print(name + " values:" + Arrays.toString(vals) + " index:"
				+ index);
		if (cont)
			System.out.print(" Cont, Disc Vals: "
					+ Arrays.toString(dThresholds) + " size:"
					+ dThresholds.length);
		if (catAttr) {
			System.out.println("  ====> [Category Attribute]");
		} else {
			System.out.println();
		}
	}
}
