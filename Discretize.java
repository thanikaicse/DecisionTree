package decisionTree;

import java.util.ArrayList;
import java.util.HashSet;

class Discretize {

	Dataset ds;
	float catEntropy;

	public Discretize(Dataset ds) {
		this.ds = ds;
		System.out.println("Threshold values for discretizing is calculated!");
		System.out.println();
	}

	@SuppressWarnings("unchecked")
	private ArrayList<Pair<Float, String>> extractData(Attribute attr) {
		int index = attr.index;
		int catIndex = ds.attrSet.catIndex;
		ArrayList<Pair<Float, String>> items = new ArrayList<Pair<Float, String>>();

		// attr.print();
		for (DataItem item : ds.trainItems) {
			Pair<Float, String> newPair = new Pair<Float, String>(
					Float.parseFloat(item.fields[index]), item.fields[catIndex]);
			items.add(newPair);
		}
		java.util.Collections.sort(items);
		return items;
	}

	/**
	 * it is not used in the program
	 * 
	 * @param items
	 * @return
	 */
	private HashSet<Float> findBorders(ArrayList<Pair<Float, String>> items) {
		HashSet<Float> vals = new HashSet<Float>();
		Pair<Float, String> curItem = items.get(0);
		for (Pair<Float, String> item : items) {
			if (!curItem.cat.equals(item.cat) && curItem.data != item.data) {
				vals.add((curItem.data + item.data) / 2);
			}
			curItem = item;
		}
		return vals;
	}

	public void discretizeAll() {
		for (Attribute attr : ds.attrSet.attrs) {
			if (attr.cont) {
				ArrayList<Pair<Float, String>> items = extractData(attr);
				HashSet<Float> thresholds = findBorders(items);
				attr.dThresholds = new float[thresholds.size()];
				int i = 0;
				for (float f : thresholds) {
					attr.dThresholds[i++] = f;
				}
			}
		}
	}
}
