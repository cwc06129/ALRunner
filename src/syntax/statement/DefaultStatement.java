package syntax.statement;

public class DefaultStatement extends LabeledStatement {
	/* field */
	private SwitchStatement switchStatement;
	private Statement body = new Statement();
	
	/* getter and setter */
	public SwitchStatement getSwitchStatement() {
		return switchStatement;
	}
	public void setSwitchStatement(SwitchStatement switchStatement) {
		this.switchStatement = new SwitchStatement();
		this.switchStatement = switchStatement;
	}
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
}
