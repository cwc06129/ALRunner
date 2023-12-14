package syntax.statement;

import java.util.HashMap;
import java.util.ArrayList;

import syntax.expression.ControlExpression;
import syntax.expression.Expression;

public class ExitStatement extends ControlStatement{
	/* field */
	private Expression exitExpression = new Expression();
	
	/* getter and setter */
	public Expression getExitExpression() {
		return exitExpression;
	}
	public void setExitExpression(Expression exitExpression) {
		this.exitExpression = exitExpression;
	}
	//chl@2023.02.08 - override
	public Expression getExpressions() {
		return getExitExpression();
	}

	//chl@2023.02.13 - override
	public void setExpression(HashMap<Function, Integer> numberOfParam, HashMap<Function, Boolean> typeOfFunc, ArrayList<String> defines){
		//this.postfix = super.makePostfixFromLexOfStmt(numberOfParam); //get postfix of lex from statement class
		super.setExpression(numberOfParam, typeOfFunc, defines);
		ControlExpression expr = new ControlExpression();
		this.expressions = expr.makeExprFromLex(this.getRawData(), this.theNumberOfFunctionParameter, typeOfFunc, defines);
		
		this.setExitExpression(expressions.get(0));
		
		
	}
}
