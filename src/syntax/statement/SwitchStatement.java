package syntax.statement;
import java.util.ArrayList;
import java.util.HashMap;

import syntax.expression.Expression;


public class SwitchStatement extends Statement {
	/* field */
	private DefaultStatement defaultStatement;
	private ArrayList<CaseStatement> cases = new ArrayList<CaseStatement>();
	//chl@2023.02.15 - save idexpression for variable
	private Expression expression;
	
	/* method */
	public void addCase(CaseStatement caseStatement) {
		this.cases.add(caseStatement);
	}
	
	/* getter and setter */
	public DefaultStatement getDefaultStatement() {
		return defaultStatement;
	}
	public void setDefaultStatement(DefaultStatement defaultStatement) {
		if (this.defaultStatement == null) {
			this.defaultStatement = new DefaultStatement();
		}
		this.defaultStatement = defaultStatement;
	}
	public ArrayList<CaseStatement> getCases() {
		return cases;
	}

	public void setExpression(Expression expression){
		this.expression = expression;
	}

	public Expression getExpression(){
		return this.expression;
	}

	public void setExpression(HashMap<Function, Integer> numberOfParam, HashMap<Function, Boolean> typeOfFunc, ArrayList<String> defines) {
        //this.postfix = super.makePostfixFromLexOfStmt(numberOfParam); //get postfix of lex from statement class
		super.setExpression(numberOfParam, typeOfFunc, defines);
		Expression expr = new Expression();
		this.expressions = expr.makeExprFromLex(this.getRawData(), this.theNumberOfFunctionParameter, typeOfFunc, defines);


		this.setExpression(expressions.get(0));
		
	}
}
