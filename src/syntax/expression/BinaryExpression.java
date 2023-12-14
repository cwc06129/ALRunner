package syntax.expression;

public class BinaryExpression extends CompoundExpression {
	/* field */
	private Operator operator = new Operator();
	private Expression lhsOperand = new Expression();
	private Expression rhsOperand = new Expression();

	public BinaryExpression(){

	}
	public BinaryExpression(Operator operator, Expression lhsOperand, Expression rhsOperand){
		this.operator = operator;
		this.lhsOperand = lhsOperand;
		this.rhsOperand = rhsOperand;
	}

	/* getter and setter */
	public Operator getOperator() {
		return operator;
	}

	public void setOperator(String operator) {
		this.operator = new Operator(operator);
	}

	public Expression getLhsOperand() {
		return lhsOperand;
	}

	public void setLhsOperand(Expression lhsOperand) {
		this.lhsOperand = lhsOperand;
	}

	public Expression getRhsOperand() {
		return rhsOperand;
	}

	public void setRhsOperand(Expression rhsOperand) {
		this.rhsOperand = rhsOperand;
	}
	
	//chl@2023.02.07 - print template
	public String toString() {
		return this.getClass().getSimpleName() + "-> operator: " + operator + ", left: " + lhsOperand + ", right: " + rhsOperand;
	}

	//chl@2023.02.08 - code print override
	public String getCode() {

		String code = this.lhsOperand.getCode();
		if ((this.operator.getOperator() != null && this.operator.getOperator().equals(".")) || (this.operator.getOperator() != null && this.operator.getOperator().equals("->"))){
			code += this.operator.getCode() + this.rhsOperand.getCode();
		} else if (this.operator.getOperator() == null){
			code += "";
		} else {
			code +=  " " + this.operator.getCode() + " " + this.rhsOperand.getCode();
		}
		return code;

	}
}
