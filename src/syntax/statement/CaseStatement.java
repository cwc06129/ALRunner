package syntax.statement;

import java.util.ArrayList;
import java.util.HashMap;

import syntax.expression.ConditionalExpression;
import syntax.expression.Expression;
import udbparser.udbrawdata.UdbLexemeNode;

public class CaseStatement extends LabeledStatement {
	/* field */
	private Statement body = new Statement();
	private SwitchStatement switchStatement = new SwitchStatement();
	private Expression condition = new Expression();
	
	/* getter and setter */
	public Statement getBody() {
		if (this.body instanceof CompoundStatement) {
			return (Statement) (CompoundStatement) this.body;
		}
		else if (this.body instanceof IfStatement) {
			return (Statement) (IfStatement) this.body;
		}
		else if (this.body instanceof DoStatement) {
			return (Statement) (DoStatement) this.body;
		}
		else if (this.body instanceof ForStatement) {
			return (Statement) (ForStatement) this.body;
		}
		else if (this.body instanceof LoopStatement) {
			return (Statement) (LoopStatement) this.body;
		}
		else if (this.body instanceof SwitchStatement) {
			return (Statement) (SwitchStatement) this.body;
		}
		else {
			return this.body;
		}
	}
	public void setBody(Statement body) {
		this.body = body;
	}
	public SwitchStatement getSwitchStatement() {
		return switchStatement;
	}
	public void setSwitchStatement(SwitchStatement switchStatement) {
		this.switchStatement = switchStatement;
	}
	public Expression getCondition() {
		return condition;
	}
	public void setCondition(Expression condition) {
		this.condition = condition;
	}
	//chl@2023.02.08 - override
	public Expression getExpressions() {
		return getCondition();
	}
	
	public void setExpression(HashMap<Function, Integer> numberOfParam, HashMap<Function, Boolean> typeOfFunc, ArrayList<String> defines) {
        //this.postfix = super.makePostfixFromLexOfStmt(numberOfParam); //get postfix of lex from statement class
		//chl@2023.02.13 - condition
		super.setExpression(numberOfParam, typeOfFunc, defines);
		ConditionalExpression condexpr = new ConditionalExpression();

		ArrayList<UdbLexemeNode> expressionlist = this.getRawData();
		
		for (int i=0 ; i<expressionlist.size() ; i++){
			if (expressionlist.get(i).getData().equals(":")){
				expressionlist.remove(i);
			}
		}

		this.expressions = condexpr.makeExprFromLex(this.getRawData(), this.theNumberOfFunctionParameter, typeOfFunc, defines);

        condexpr.makeExpression(expressions.get(0));

		this.setCondition(condexpr);
	
	}
}
