package syntax.expression;

public class LiteralExpression extends AtomicExpression {
	/* field */
	private String constant;

	public LiteralExpression(String constant) {
		this.setConstant(constant);
	}
	
	/* getter and setter */
	public String getConstant() {
		return constant;
	}
	public void setConstant(String constant) {
		this.constant = constant;
	}
	//chl@2023.02.07 - print template
	public String toString() {
		return this.getClass().getSimpleName() + "-> " + constant;
	}	
	//chl@2023.02.08 - code print override
	public String getCode() {

		return constant;

	}
}
