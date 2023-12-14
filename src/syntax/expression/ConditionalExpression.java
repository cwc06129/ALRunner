package syntax.expression;

public class ConditionalExpression extends CompoundExpression {
	/* field */
	private Expression condition;
	private Expression trueExpr;
	private Expression falseExpr;
	/*chl@2023.02.09 - for code generate*/
	//private String type = "";

	public ConditionalExpression () {
		this.condition = null;
		this.trueExpr = null;
		this.falseExpr = null;
	}
	
	/* getter and setter */
	public Expression getCondition() {
		return condition;
	}
	public void setCondition(Expression condition) {
		this.condition = condition;
	}
	public Expression getTrueExpr() {
		return trueExpr;
	}
	public void setTrueExpr(Expression trueExpr) {
		this.trueExpr = trueExpr;
	}
	public Expression getFalseExpr() {
		return falseExpr;
	}
	public void setFalseExpr(Expression falseExpr) {
		this.falseExpr = falseExpr;
	}
	//chl@2023.02.07 - print template
	public String toString() {
		return this.getClass().getSimpleName() + "-> " + condition + ", true: " + trueExpr + ", false: " + falseExpr;
	}
	//chl@2023.02.08 - code print override
	public String getCode() {

			return condition.getCode();

	}

	public void makeExpression(Expression expr){
		Expression condition = expr;
		
		/*chl@2023.02.08 - conditional cast*/
		this.setCondition(condition);
		
	}
}
