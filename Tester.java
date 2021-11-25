package decisionTree;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public class Tester {
	UtilClass util;
	Dataset ds;
	DataItem[] testItems;
	TreeNode decTree;

	public Tester(UtilClass util) {
		this.util = util;
		this.ds = util.dataset;
		this.testItems = ds.testItems;
		this.decTree = util.id3Learner.root;
	}

	/**
	 * Tests the accuracy of pruned ruleset
	 */
	public void testPrunedRules(DataItem[] itemSet) {
		ArrayList<Rule> sortedRuleset = sortPrunedRules();
		int correct = 0, incorrect = 0;
		for (DataItem item : itemSet) {
			for (Rule rule : sortedRuleset) {
				if (testPrecond(item, rule, rule.bestPrunePoint)) {
					if (rule.cons.satisfiesCons(item)) {
						correct++;
					} else {
						incorrect++;
					}
					break;
				}
			}
		}
		System.out.println("Correct Predictions: " + correct);
		System.out.println("Incorrect Predictions: " + incorrect);
		System.out.println("Accuracy: "
				+ ((float) correct / (correct + incorrect)));
	}

	private ArrayList<Rule> sortPrunedRules() {
		ArrayList<Rule> sortedRuleset = new ArrayList<Rule>(
				util.id3Learner.ruleset);
		Collections.sort(sortedRuleset, new Comparator<Rule>() {
			public int compare(Rule r1, Rule r2) {
				return ((Float) r2.accuracy).compareTo(r1.accuracy);
			}
		});
		System.out.println("Sorted Rules:");
		for (Rule rule : sortedRuleset) {
			rule.print();
			System.out.println(rule.accuracy);
		}
		return sortedRuleset;
	}

	/**
	 * tests the accuracy of rulese producted from tree
	 */
	public void testRuleset() {
		int correct = 0, incorrect = 0;
		for (DataItem item : testItems) {
			item.print();
			for (Rule rule : util.id3Learner.ruleset) {
				if (testPrecond(item, rule, rule.preConds.length - 1)) {
					if (rule.cons.satisfiesCons(item)) {
						correct++;
						System.out.println("CORRECT");
					} else {
						incorrect++;
						System.out.println("INCORRECT");
					}
					break;
				}
			}
		}
		System.out.println("Correct Predictions: " + correct);
		System.out.println("Incorrect Predictions: " + incorrect);
		System.out.println("Accuracy: "
				+ ((float) correct / (correct + incorrect)));
	}

	/**
	 * tests if an item is correctly predicted using a rule (pruned or unpruned)
	 * 
	 * @param rule
	 * @param prunePoint
	 * @return
	 */
	public float testItemsUsingOneRule(Rule rule, int prunePoint,
			DataItem[] items) {
		int correct = 0, incorrect = 0;
		for (DataItem item : items) {
			if (testPrecond(item, rule, prunePoint)) {
				if (rule.cons.satisfiesCons(item)) {
					correct++;
				} else {
					incorrect++;
				}
			}
		}
		if (correct + incorrect > 0) {
			return ((float) correct / (correct + incorrect));
		} else
			return 0;
	}

	/**
	 * tests precondition of a rule over an item
	 * 
	 * @param item
	 * @param rule
	 * @param prunePoint
	 * @return
	 */
	private boolean testPrecond(DataItem item, Rule rule, int prunePoint) {
		boolean satisfy = true;
		for (int i = 0; i <= prunePoint; i++) {
			if (!rule.preConds[i].satisfiesPre(item)) {
				satisfy = false;
			}
		}
		return satisfy;
	}

	/**
	 * tests the accuracy of a tree
	 * 
	 * @return
	 */
	public float testTree(DataItem[] itemSet) {
		int correct = 0, incorrect = 0;
		for (DataItem item : itemSet) {
			if (testItemUsingTree(item) == 1) {
				correct++;
			} else {
				incorrect++;
			}
		}
		System.out.println("Correct Predictions: " + correct);
		System.out.println("Incorrect Predictions: "
				+ (itemSet.length - correct));
		System.out.println("Accuracy: " + ((float) correct / itemSet.length));
		return ((float) correct / itemSet.length);
	}

	/**
	 * tests only one item using tree
	 * 
	 * @param item
	 */
	public int testItemUsingTree(DataItem item) {
		TreeNode node = decTree;
		while (node.isLeaf == false) {
			int index = node.attr.index;
			for (TreeBranch branch : node.branches) {
				if (node.attr.cont) {
					float val = Float.parseFloat(item.fields[index]);
					if ((branch.val.startsWith(" < "))
							&& (val < Float.parseFloat(branch.val.substring(3)))) {
						node = branch.child;
						break;
					} else if ((branch.val.startsWith(" >= "))
							&& (val >= Float
									.parseFloat(branch.val.substring(4)))) {
						node = branch.child;
						break;
					}
				} else {
					if (item.fields[index].equals(branch.val)) {
						node = branch.child;
						break;
					}
				}
			}
		}
		item.print();
		String tempStr = "CORRECT";
		int res = 1;
		if (!item.fields[item.fields.length - 1].equals(node.label)) {
			tempStr = "INCORRECT!!!";
			res = -1;
		}
		System.out.println(tempStr + ", Prediction: " + node.label);
		return res;
	}
}
