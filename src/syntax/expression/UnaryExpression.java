package syntax.expression;

public class UnaryExpression extends CompoundExpression {
	/* field */
	private Expression operand = new Expression();
	private Operator operator = new  Operator();
	private Boolean position; //false -> unary end, true -> unary front

	public Boolean getPosition() {
		return this.position;
	}

	public void setPosition(Boolean position) {
		this.position = position;
	}

	/* getter and setter */
	public Expression getOperand() {
		return operand;
	}
	public void setOperand(Expression operand) {
		this.operand = operand;
	}
	public Operator getOperator() {
		return operator;
	}
	public void setOperator(String operator) {
		this.operator = new Operator(operator);
	}
	//chl@2023.02.07 - print template
	public String toString() {
		return this.getClass().getSimpleName() + "-> " + operand + ", " + operator;
	}
	//chl@2023.02.08 - code print override
	public String getCode() {
		
		if (operand instanceof NullExpression) {
			return operator.getCode();
		} 
		if (operand instanceof LibraryCallExpression && ((LibraryCallExpression)operand).getFunctionalName().equals("malloc")){
			return operator.getCode() + operand.getCode();
		}
		if (operator.toString().contains("*") && operator.toString().length()>1){
			return operand.getCode();
		} 
		if (position == null){
			return operator.getCode();
		}
		if (position == false)
			return operand.getCode() + operator.toString();
		else 
			return operator.toString() + operand.getCode();

	}
}

