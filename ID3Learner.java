package decisionTree;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.HashSet;

import javax.swing.JOptionPane;

class ID3Learner {
	Dataset ds;
	TreeNode root;
	int catIndex;
	UtilClass util;
	Attribute catAttr;
	ArrayList<Rule> ruleset;

	/**
	 * Constructor
	 */
	public ID3Learner(Dataset ds, UtilClass util) {
		this.ds = ds;
		catIndex = ds.attrSet.catIndex;
		this.util = util;
		this.catAttr = ds.attrSet.attrs[ds.attrSet.catIndex];
	}

	/**
	 * Finds frequency of each category
	 */
	private HashMap<String, Integer> categoriesFrequency(DataItem[] items) {
		HashMap<String, Integer> catFreq = new HashMap<String, Integer>();
		try {
			for (DataItem item : items) {
				String val = item.fields[catIndex];
				if (catFreq.containsKey(val)) {
					catFreq.put(val, catFreq.get(val) + 1);
				} else {
					catFreq.put(val, 1);
				}
			}
		} catch (Exception e) {
		}
		return catFreq;
	}

	/**
	 * Finds the most frequent category among a set of items
	 */
	private String mostFreq(HashMap<String, Integer> hashMap) {
		Iterator iter = hashMap.entrySet().iterator();
		int max = 0;
		String res = "";
		while (iter.hasNext()) {
			Map.Entry<String, Integer> element = (Map.Entry) iter.next();
			// System.out.println(element.getValue() + " " + element.getKey());
			if (element.getValue() > max) {
				res = element.getKey();
				max = element.getValue();
			}
		}
		return res;
	}

	/**
	 * Calculates entropy for a given set of data
	 */
	private float calcEntropy(String[] temp) {
		float entropy = 0;
		HashMap<String, Integer> hm = new HashMap<String, Integer>();
		for (String s : temp) {
			if (hm.containsKey(s)) {
				hm.put(s, hm.get(s) + 1);
			} else {
				hm.put(s, 1);
			}
		}
		Iterator iter = hm.entrySet().iterator();
		while (iter.hasNext()) {
			Map.Entry<String, Integer> el = (Map.Entry) iter.next();
			float pi = (float) el.getValue() / temp.length;
			entropy -= (pi * Math.log(pi) / Math.log(2));
			// System.out.println(count + " of " + temp.length + ". pi:" + pi +
			// " en:" + entropy);
		}
		return entropy;
	}

	/**
	 * Selects the best attribute based on their gains
	 */
	private int[] selectBestAttr(DataItem[] items, ArrayList<Attribute> attrs) {
		float[][] gain = new float[attrs.size()][2];
		String cats[] = new String[items.length];
		for (int i = 0; i < items.length; i++) {
			cats[i] = items[i].fields[catIndex];
		}
		float entropy = calcEntropy(cats);
		System.out.println("entropy: " + entropy);

		for (int i = 0; i < attrs.size(); i++) {
			gain[i][0] = entropy;
			if (attrs.get(i).cont) {
				selectBestContAttr(items, attrs, gain, i);
			} else {
				selectBestDiscAttr(items, attrs, gain, i);
			}
		}
		int[] bestIndex = { -1, -1 };
		float bestVal = Integer.MIN_VALUE;
		for (int i = 0; i < gain.length; i++) {
			if (gain[i][0] >= bestVal) {
				bestIndex[0] = i;
				bestIndex[1] = (int) gain[i][1];
				bestVal = gain[i][0];
			}
			System.out.println(attrs.get(i).name + " gain:" + gain[i][0]);
			if (attrs.get(i).cont)
				System.out.println(" thr:"
						+ attrs.get(i).dThresholds[(int) gain[i][1]]);
		}
		System.out.println("Best Attribute:" + attrs.get(bestIndex[0]).name
				+ " with gain:" + bestVal + "\n");
		if (gain[bestIndex[0]][0] <= 0) {
			bestIndex[0] = -1;
		}
		return bestIndex;
	}

	private void selectBestDiscAttr(DataItem[] items,
			ArrayList<Attribute> attrs, float[][] gain, int attrNum) {
		int index = attrs.get(attrNum).index;
		HashSet<String> vals = new HashSet<String>();
		for (int j = 0; j < items.length; j++) {
			vals.add(items[j].fields[index]);
		}

		for (String val : vals) {
			ArrayList<String> tempCats = new ArrayList<String>();
			for (DataItem item : items) {
				if (item.fields[index].equals(val)) {
					tempCats.add(item.fields[catIndex]);
				}
			}
			float tempF = ((float) tempCats.size()) / ((float) items.length);
			tempF = tempF
					* (calcEntropy(tempCats
							.toArray(new String[tempCats.size()])));
			gain[attrNum][0] -= tempF;
		}
	}

	private void selectBestContAttr(DataItem[] items,
			ArrayList<Attribute> attrs, float[][] gain, int attrNum) {
		int index = attrs.get(attrNum).index;
		ArrayList<String> tempCats;
		float maxGain = -1;
		for (int i = 0; i < attrs.get(attrNum).dThresholds.length; i++) {
			float tempGain = gain[attrNum][0];
			tempCats = new ArrayList<String>();
			for (DataItem item : items) {
				if (Float.parseFloat(item.fields[index]) < attrs.get(attrNum).dThresholds[i]) {
					tempCats.add(item.fields[catIndex]);
				}
			}
			tempGain -= (((float) tempCats.size() / items.length) * calcEntropy(tempCats
					.toArray(new String[1])));

			tempCats = new ArrayList<String>();
			for (DataItem item : items) {
				if (Float.parseFloat(item.fields[index]) >= attrs.get(attrNum).dThresholds[i]) {
					tempCats.add(item.fields[catIndex]);
				}
			}
			tempGain -= (((float) tempCats.size() / items.length) * calcEntropy(tempCats
					.toArray(new String[1])));
			if (tempGain > maxGain) {
				maxGain = tempGain;
				gain[attrNum][1] = i;
			}
		}
		gain[attrNum][0] = maxGain;
	}

	/**
	 * Removes recently covered items (for discrete attributes)
	 */
	private DataItem[] genNewItemsDiscrete(DataItem[] items,
			Attribute selected, String val) {
		ArrayList<DataItem> res = new ArrayList<DataItem>();
		int index = selected.index;
		for (DataItem item : items) {
			if (item.fields[index].equals(val)) {
				res.add(item);
			}
		}
		return res.toArray(new DataItem[1]);
	}

	/**
	 * Removes recently covered items (for continuous attributes)
	 */
	private DataItem[] genNewItemsContinuous(DataItem[] items,
			Attribute selected, String operation, int bestThresholdIndex) {
		ArrayList<DataItem> res = new ArrayList<DataItem>();
		int index = selected.index;
		float threshold = selected.dThresholds[bestThresholdIndex];
		if (operation.equals("st")) {
			for (DataItem item : items) {
				if (Float.parseFloat(item.fields[index]) < threshold) {
					res.add(item);
				}
			}
		} else {
			for (DataItem item : items) {
				if (Float.parseFloat(item.fields[index]) >= threshold) {
					res.add(item);
				}
			}
		}
		return res.toArray(new DataItem[1]);
	}

	/**
	 * Removes recently covered attribute
	 */
	private ArrayList<Attribute> genNewAttrs(ArrayList<Attribute> attrs,
			Attribute selected) {
		ArrayList<Attribute> res = new ArrayList<Attribute>();
		for (Attribute att : attrs) {
			if (att != selected) {
				res.add(att);
			}
		}
		return res;
	}

	/**
	 * ID3 algorithm
	 */
	private TreeNode id3(DataItem[] items, ArrayList<Attribute> attrs) {
		TreeNode node = null;
		HashMap<String, Integer> catFreq = categoriesFrequency(items);
		if (catFreq.size() == 1) {
			node = new TreeNode(items[0].fields[catIndex], catAttr);
		} else if (attrs.size() == 0) {
			node = new TreeNode(mostFreq(catFreq), catAttr);
		} else {
			int[] bestAttrIndex = selectBestAttr(items, attrs);
			if (bestAttrIndex[0] == -1) {
				HashMap<String, Integer> freqs = categoriesFrequency(items);
				node = new TreeNode(mostFreq(freqs), catAttr);
			} else {
				Attribute selected = attrs.get(bestAttrIndex[0]);
				if (selected.cont) {
					node = id3Cont(items, attrs, selected, bestAttrIndex[1]);
				} else {
					node = id3Disc(items, attrs, selected);
				}
			}
		}
		return node;
	}

	private TreeNode id3Disc(DataItem[] items, ArrayList<Attribute> attrs,
			Attribute selected) {
		TreeNode node;
		TreeNode[] children;
		children = new TreeNode[selected.vals.length];
		for (int i = 0; i < selected.vals.length; i++) {
			ArrayList<Attribute> newAttrs = genNewAttrs(attrs, selected);
			DataItem[] newItems = genNewItemsDiscrete(items, selected,
					selected.vals[i]);
			if (newItems.length == 0) {
				HashMap<String, Integer> freqs = categoriesFrequency(items);
				children[i] = new TreeNode(mostFreq(freqs), catAttr);
			} else {
				children[i] = id3(newItems, newAttrs);
			}
		}
		// node = new TreeNode(selected.name, selected.vals, children);
		node = new TreeNode(selected, selected.vals, children);

		return node;
	}

	private TreeNode id3Cont(DataItem[] items, ArrayList<Attribute> attrs,
			Attribute selected, int bestThresholdIndex) {
		TreeNode node;
		TreeNode[] children;
		children = new TreeNode[2];
		// creating 2 children for continuous attributes
		ArrayList<Attribute> newAttrs;
		for (int i = 0; i < 2; i++) {
			newAttrs = attrs;
			// smaller than
			String operation = "st";
			if (i == 1)
				// greater equal
				operation = "ge";
			DataItem[] newItems = genNewItemsContinuous(items, selected,
					operation, bestThresholdIndex);
			if (newItems.length == 0) {
				HashMap<String, Integer> freqs = categoriesFrequency(items);
				children[i] = new TreeNode(mostFreq(freqs), catAttr);
			} else {
				children[i] = id3(newItems, newAttrs);
			}
		}
		String[] branchVals = {
				" < " + selected.dThresholds[bestThresholdIndex],
				" >= " + selected.dThresholds[bestThresholdIndex] };
		// node = new TreeNode(selected.name, branchVals, children);
		node = new TreeNode(selected, branchVals, children);
		return node;
	}

	/**
	 * Calls ID3
	 */
	public void learnTree() {
		ArrayList<Attribute> curAttSet = new ArrayList<Attribute>(
				ds.attrSet.attrs.length - 1);
		for (int i = 0; i < ds.attrSet.attrs.length - 1; i++) {
			curAttSet.add(ds.attrSet.attrs[i]);
			// ds.attrSet.attrs[i].print();
		}
		root = id3(ds.trainItems, curAttSet);
		System.out.println("Decision tree was constructed!");
		root.print("");
	}

	public void convertTree2Ruleset() {
		ruleset = (new Rule()).genRules(root, new RuleTerm[0]);
		for (Rule rule : ruleset) {
			rule.print();
		}
	}

	public void pruneRules() {
		if (ds.validItems.length == 0) {
			JOptionPane.showMessageDialog(null, "Validation set is empty!",
					"Warning", 0);
		}
		Tester tester = new Tester(util);
		float maxAccuracy = -1;
		for (Rule rule : ruleset) {
			maxAccuracy = -1;
			for (int prunePoint = rule.preConds.length - 1; prunePoint >= 0; prunePoint--) {
				float accuracy = tester.testItemsUsingOneRule(rule, prunePoint,
						ds.validItems);
				rule.print(prunePoint);
				System.out.println(" accuracy: " + accuracy);
				if (accuracy >= maxAccuracy) {
					rule.bestPrunePoint = prunePoint;
					maxAccuracy = accuracy;
					rule.accuracy = accuracy;
				}
			}
			System.out.print("Best Rule is: ");
			rule.print(rule.bestPrunePoint);
			System.out.println();
		}
		for (Rule rule : ruleset) {
			rule.print(rule.bestPrunePoint);
			System.out.println("Accuracy: " + rule.accuracy);
		}
		System.out.println();
	}
}
