package decisionTree;

public class UtilClass {
	Form form;
	Dataset dataset;
	ID3Learner id3Learner;

	public void readFiles() {
		String name = form.getDatasetName();
		dataset = new Dataset(name, this);
		System.out.println("Attribute, tarin and test sets are loaded ("
				+ dataset.allTrainItems.length + ", "
				+ dataset.testItems.length + ").");
	}

	public void corruptData() {
		dataset.corruptData();
		System.out.println(form.getCorruptionPercent()
				* dataset.allTrainItems.length / 100
				+ " records of data corrupted.");
	}

	public void split2TrainAndValidation() {
		dataset.splitTrainItems();
		System.out.println("Splited to " + dataset.trainItems.length
				+ " train items and " + dataset.validItems.length
				+ " validation items.");
	}

	public void discretizeValues() {
		Discretize disc = new Discretize(dataset);
		disc.discretizeAll();
		for (Attribute attr : dataset.attrSet.attrs) {
			attr.print();
		}
		System.out.println();
	}

	public void learnID3() {
		System.out.println("\nStarting Learning...");
		id3Learner = new ID3Learner(dataset, this);
		id3Learner.learnTree();
		System.out.println("\nTest on train items:");
		Tester tester = new Tester(this);
		tester.testTree(dataset.trainItems);
		System.out.println("\nFinished Learning...");
	}

	public void testTree() {
		System.out.println("\nStarting Testing...");
		Tester tester = new Tester(this);
		tester.testTree(dataset.testItems);
		System.out.println("\nFinished Testing...");
	}

	public void genRules() {
		System.out.println("\nStarting rule generation...");
		id3Learner.convertTree2Ruleset();
		System.out.println("\nFinished rule generation...");
	}

	public void testRuleset() {
		System.out.println("\nStarting testing ruleset...");
		Tester tester = new Tester(this);
		tester.testRuleset();
		System.out.println("\nFinished testing ruleset...");
	}

	public void pruneRules() {
		System.out.println("\nStarting rule pruning...");
		id3Learner.pruneRules();
		System.out.println("Finished rule pruning...");
	}

	public void testPrunedRules() {
		System.out.println("\nStarting Testing Pruned Rules on train items...");
		Tester tester = new Tester(this);
		tester.testPrunedRules(dataset.trainItems);
		System.out.println("\nStarting Testing Pruned Rules on test items...");
		tester.testPrunedRules(dataset.testItems);
		System.out.println("\nFinished Testing Pruned Rules...");
	}

	public static void main(String[] args) {
		UtilClass util = new UtilClass();
		util.form = new Form(util);
	}

}
