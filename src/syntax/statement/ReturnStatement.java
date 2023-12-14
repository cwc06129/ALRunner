package syntax.statement;


import java.util.HashMap;
import java.util.ArrayList;

import syntax.expression.ControlExpression;
import syntax.expression.Expression;

public class ReturnStatement extends ControlStatement {
	/* field */
	private Expression returnExpr = new Expression();

	
	/* getter and setter */
	public Expression getReturnExpr() {
		return returnExpr;
	}
	public void setReturnExpr(Expression returnExpr) {
		this.returnExpr = returnExpr;
	}
	//chl@2023.02.08 - override
	public Expression getExpressions() {
		return getReturnExpr();
	}
	//chl@2023.02.13 - override
	public void setExpression(HashMap<Function, Integer> numberOfParam, HashMap<Function, Boolean> typeOfFunc, ArrayList<String> defines){
		//this.postfix = super.makePostfixFromLexOfStmt(numberOfParam); //get postfix of lex from statement class
		super.setExpression(numberOfParam, typeOfFunc, defines);
		ControlExpression Cntexpr = new ControlExpression();
		this.expressions = Cntexpr.makeExprFromLex(this.getRawData(), this.theNumberOfFunctionParameter, typeOfFunc, defines);

		Cntexpr.makeExpression(expressions); 
		this.setReturnExpr(Cntexpr);

		
		
		//return this.expressions.get(0);
	}
}
