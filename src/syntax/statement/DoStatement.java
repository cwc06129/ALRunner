package syntax.statement;

import java.util.ArrayList;
import java.util.HashMap;

import syntax.expression.ConditionalExpression;

public class DoStatement extends LoopStatement{
    public void setExpression(HashMap<Function, Integer> numberOfParam, HashMap<Function, Boolean> typeOfFunc, ArrayList<String> defines) {
        //this.postfix = super.makePostfixFromLexOfStmt(numberOfParam); //get postfix of lex from statement class
		super.setExpression(numberOfParam, typeOfFunc, defines);
		ConditionalExpression condexpr = new ConditionalExpression();
		this.expressions = condexpr.makeExprFromLex(this.getRawData(), this.theNumberOfFunctionParameter, typeOfFunc, defines);
		
        condexpr.makeExpression(expressions.get(0));

		this.setCondition(condexpr);
	}
}
