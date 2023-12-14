package syntax.statement;

import syntax.expression.Expression;

public class BranchStatement extends Statement {
	/* field */	
	private Statement then = new Statement();
	private Statement els = new Statement();
	private Expression condition = new Expression();
	
	/* getter and setter */
	public Statement getThen() {
		if (this.then instanceof CompoundStatement) {
			return (Statement) (CompoundStatement) this.then;
		}
		else if (this.then instanceof IfStatement) {
			return (Statement) (IfStatement) this.then;
		}
		else if (this.then instanceof DoStatement) {
			return (Statement) (DoStatement) this.then;
		}
		else if (this.then instanceof ForStatement) {
			return (Statement) (ForStatement) this.then;
		}
		else if (this.then instanceof LoopStatement) {
			return (Statement) (LoopStatement) this.then;
		}
		else {
			return this.then;
		}
	}
	public void setThen(Statement then) {
		this.then = then;
	}
	public Statement getEls() {
		if (this.els instanceof CompoundStatement) {
			return (Statement) (CompoundStatement) this.els;
		}
		else if (this.els instanceof IfStatement) {
			return (Statement) (IfStatement) this.els;
		}
		else if (this.els instanceof DoStatement) {
			return (Statement) (DoStatement) this.els;
		}
		else if (this.els instanceof ForStatement) {
			return (Statement) (ForStatement) this.els;
		}
		else if (this.els instanceof LoopStatement) {
			return (Statement) (LoopStatement) this.els;
		}
		else {
			return this.els;
		}
	}
	public void setEls(Statement els) {
		this.els = els;
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

}
