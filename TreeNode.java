package decisionTree;

class TreeNode {
	// final int MAX_NUM_OF_VALS = 10;
	String label;
	boolean isLeaf;
	TreeBranch[] branches;
	Attribute attr;

	public TreeNode(String label, Attribute attr) {
		this.label = label;
		this.isLeaf = true;
		this.attr = attr;
	}

	public TreeNode(Attribute attr, String[] branchVals, TreeNode[] children) {
		this.attr = attr;
		this.branches = new TreeBranch[branchVals.length];
		for (int i = 0; i < branchVals.length; i++) {
			branches[i] = new TreeBranch(branchVals[i], children[i]);
		}
		this.label = this.attr.name;
	}

	public void print(String str) {
		String tempStr = "";
		if (!this.isLeaf)
			tempStr = " (" + this.attr.index + ")";
		System.out.println(str + this.label + tempStr);
		if (this.branches != null) {
			for (TreeBranch branch : this.branches) {
				System.out.println(str + "   |---" + branch.val);
				branch.child.print(str + "   |     ");
			}
		}
	}
}
