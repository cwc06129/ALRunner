package syntax.statement;

import java.util.HashMap;
import java.util.ArrayList;

import syntax.expression.Expression;

public class ExpressionStatement extends Statement {
	/* field */
	private Expression expression = new Expression();
	

	//sohee
	private boolean haveLibraryCall = false;
	private Expression fixed_expression;

	public boolean isHaveLibraryCall() {
		return haveLibraryCall;
	 }
	 public void setHaveLibraryCall(boolean haveLibraryCall) {
		this.haveLibraryCall = haveLibraryCall;
	 }
	 public Expression getFixed_expression() {
		return fixed_expression;
	 }
	 public void setFixed_expression(Expression fixed_expression) {
		this.fixed_expression = new Expression();
		this.fixed_expression = fixed_expression;
	 }
	
	/*chl@2023.02.08 - check next loop or then*/
	//private Expression then = new Expression();
	
	/* getter and setter */
	public Expression getExpression() {
		return expression;
	}
	public void setExpression(Expression expression) {
		this.expression = expression;
	}
	//chl@2023.02.08 - override
	public Expression getExpressions() {
		return getExpression();
	}	
	//chl@2023.02.13 - override
	public void setExpression(HashMap<Function, Integer> numberOfParam, HashMap<Function, Boolean> typeOfFunc, ArrayList<String> defines){
		//this.postfix = super.makePostfixFromLexOfStmt(numberOfParam); //get postfix of lex from statement class
		super.setExpression(numberOfParam, typeOfFunc, defines);
		Expression expr = new Expression();
		this.expressions = expr.makeExprFromLex(this.getRawData(), this.theNumberOfFunctionParameter, typeOfFunc, defines);

		expr = expressions.get(0);
		this.setExpression(expr);
		
	}
}
