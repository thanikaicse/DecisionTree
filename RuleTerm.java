package decisionTree;

class RuleTerm {
	Attribute attr;
	String op;
	String val;

	public RuleTerm(Attribute attr, String op, String val) {
		this.attr = attr;
		this.op = op;
		this.val = val;
	}

	public void print() {
		String tempStr = (op == null || op == "") ? " = " : op;
		System.out.print(" (" + attr.name + tempStr + val + ")");
	}

	public boolean satisfiesPre(DataItem item) {
		int index = attr.index;
		if (op.equals(" = ")) {
			if (item.fields[index].equals(val)) {
				return true;
			}
		} else if (op.equals(" < ")) {
			if (Float.parseFloat(item.fields[index]) < (Float.parseFloat(val))) {
				return true;
			}
		} else if (op.equals(" >= ")) {
			if (Float.parseFloat(item.fields[index]) >= (Float.parseFloat(val))) {
				return true;
			}
		}
		return false;
	}

	public boolean satisfiesCons(DataItem item) {
		int index = attr.index;
		if (item.fields[index].equals(val)) {
			return true;
		}
		return false;
	}
}
