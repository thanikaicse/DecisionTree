package decisionTree;

import java.util.ArrayList;
import java.util.Arrays;

public class Rule {

	int MAX_LEN = 10;
	RuleTerm[] preConds;
	RuleTerm cons;
	int curLen;
	int bestPrunePoint;
	float accuracy;

	public Rule() {
		bestPrunePoint = -1;
	}

	public Rule(RuleTerm[] terms, RuleTerm cons) {
		this.preConds = Arrays.copyOf(terms, terms.length);
		this.cons = cons;
		this.curLen = terms.length;
		bestPrunePoint = -1;
	}

	public ArrayList<Rule> genRules(TreeNode node, RuleTerm[] curTerms) {
		ArrayList<Rule> rules = new ArrayList<Rule>();
		RuleTerm term;
		if (!node.isLeaf) {
			for (int i = 0; i < node.branches.length; i++) {
				String brVal = node.branches[i].val;
				if (brVal.startsWith(" < ")) {
					term = new RuleTerm(node.attr, " < ", brVal.substring(3,
							brVal.length()));
				} else if (brVal.startsWith(" >= ")) {
					term = new RuleTerm(node.attr, " >= ", brVal.substring(4,
							brVal.length()));
				} else {
					term = new RuleTerm(node.attr, " = ", brVal);
				}
				RuleTerm[] rt = Arrays.copyOf(curTerms, curTerms.length + 1);
				rt[rt.length - 1] = term;
				rules.addAll(genRules(node.branches[i].child, rt));
			}
		} else {
			RuleTerm cons = new RuleTerm(node.attr, "", node.label);
			rules.add(new Rule(curTerms, cons));
			return rules;
		}
		return rules;
	}

	public void print() {
		System.out.print("If [");
		for (int i = 0; i < curLen; i++) {
			preConds[i].print();
			if (i != curLen - 1)
				System.out.print(" and ");
		}
		System.out.print("] Then");
		cons.print();
		System.out.println();
	}

	public void print(int prunePoint) {
		System.out.print("If [");
		for (int i = 0; i <= prunePoint; i++) {
			preConds[i].print();
			if (i != prunePoint)
				System.out.print(" and ");
		}
		System.out.print("] Then");
		cons.print();
		System.out.println();
	}
}
