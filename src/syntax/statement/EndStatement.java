package syntax.statement;

public class EndStatement extends Statement {
	/* field */
	private Statement pairStatement;
	
	public EndStatement(){
		pairStatement = null;
	}
	
	/* getter and setter */
	public Statement getPairStatement() {
		if (pairStatement == null)
			return null;
		return pairStatement;
	}
	public void setPairStatement(Statement pairStatement) {
		this.pairStatement = new Statement();
		this.pairStatement = pairStatement;
	}
}
