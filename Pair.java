package decisionTree;

class Pair<A, B> implements Comparable {
	A data;
	B cat;

	public Pair(final A data, final B cat) {
		this.data = data;
		this.cat = cat;
	}

	void print(String delimiter) {
		System.out.print("(" + data + "," + cat + ")" + delimiter);
	}

	public int compareTo(Object other) {
		float res = (Float) data - (Float) ((Pair) other).data;
		// System.out.println(res);
		if (res > 0) {
			return 1;
		} else if (res < 0) {
			return -1;
		} else {
			return ((String) cat).compareTo((String) ((Pair) other).cat);
		}
	}
}
