package syntax.expression;

public class Operator {
	/* filed */
	private String operator;
	private String traverseRange;

	public String getTraverseRange() {
		return this.traverseRange;
	}

	public void setTraverseRange(String traverseRange) {
		this.traverseRange = traverseRange;
	}

	public Operator() {
		this.operator = null;
	}

	public Operator(String operator) {
		this.operator = operator;
	}

	/* getter and setter */
	public String getOperator() {
		return operator;
	}

	public void setOperator(String operator) {
		this.operator = operator;
	}
	
	//chl@2023.02.07 - print template
	public String toString() {
		return operator;
	}

	public String getCode() {
		if (operator != null)
			return operator;
		else
			return "";
	}
}
