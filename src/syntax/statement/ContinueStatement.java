package syntax.statement;

import java.util.ArrayList;
import java.util.HashMap;

import syntax.expression.ControlExpression;
import syntax.expression.Expression;

public class ContinueStatement extends ControlStatement {
	/* field */
	private Expression expression = new Expression();
	
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
		ControlExpression Cntexpr = new ControlExpression();
		this.expressions = Cntexpr.makeExprFromLex(this.getRawData(), this.theNumberOfFunctionParameter, typeOfFunc, defines);

		
		this.setExpression(expressions.get(0));
		
	}
}
