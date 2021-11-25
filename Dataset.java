package decisionTree;

import java.util.Random;
import java.util.Scanner;
import java.io.FileReader;
import java.io.BufferedReader;
import java.util.ArrayList;

class Dataset {
	AttributeSet attrSet;
	DataItem[] allTrainItems = {};
	DataItem[] trainItems = {};
	DataItem[] validItems = {};
	DataItem[] testItems = {};
	boolean PRINT = false;
	UtilClass util;

	public Dataset(String dsName, UtilClass util) {
		this.util = util;
		attrSet = new AttributeSet(dsName);
		loadDataItems(dsName, true);
		loadDataItems(dsName, false);
		if (PRINT) {
			attrSet.print();
			printDataItems(true);
			printDataItems(false);
		}
	}

	private void loadDataItems(String fName, boolean train) {
		Scanner sc = null;
		String trainOrTest = train ? "-train.txt" : "-test.txt";
		ArrayList<DataItem> tempAL = new ArrayList<DataItem>();
		try {
			FileReader fr = new FileReader("data/" + fName + trainOrTest);
			sc = new Scanner(new BufferedReader(fr));
		} catch (java.io.IOException e) {
			System.out.println(e.getMessage());
		}
		while (sc.hasNext()) {
			tempAL.add(new DataItem(sc.nextLine(), attrSet.attrs.length));
		}
		if (train) {
			allTrainItems = tempAL.toArray(allTrainItems);
		} else {
			testItems = tempAL.toArray(testItems);
		}
	}

	private void printDataItems(boolean train) {
		DataItem[] items = train ? allTrainItems : testItems;
		for (DataItem item : items) {
			item.print();
		}
		System.out.println(items.length + "\n");
	}

	public void splitTrainItems() {
		java.util.Random rand = new Random(util.form.getRandomSeed());
		int numOfValidationItems = (allTrainItems.length * util.form
				.getValidationPercent()) / 100;
		trainItems = new DataItem[allTrainItems.length - numOfValidationItems];
		validItems = new DataItem[numOfValidationItems];
		boolean[] selected = new boolean[allTrainItems.length];
		int i = 0;
		while (i < numOfValidationItems) {
			int r = rand.nextInt(allTrainItems.length);
			if (selected[r] == false) {
				selected[r] = true;
				validItems[i++] = allTrainItems[r];
			}
		}
		int j = 0;
		for (i = 0; i < allTrainItems.length; i++) {
			if (selected[i] == false) {
				trainItems[j++] = allTrainItems[i];
			}
		}
	}

	public void corruptData() {
		int index = attrSet.catIndex;
		String[] vals = attrSet.attrs[index].vals;
		java.util.Random rand = new Random(util.form.getRandomSeed() * 2);
		int num2Corrupt = (allTrainItems.length * util.form
				.getCorruptionPercent()) / 100;
		int i = 0;
		boolean[] selected = new boolean[allTrainItems.length];
		while (i < num2Corrupt) {
			int r = rand.nextInt(allTrainItems.length);
			if (selected[r] == false) {
				selected[r] = true;
				while (true) {
					int r2 = rand.nextInt(vals.length);
					if (!allTrainItems[r].fields[index].equals(vals[r2])) {
						allTrainItems[r].fields[index] = vals[r2];
						i++;
						break;
					}
				}
			}
		}
	}
}
