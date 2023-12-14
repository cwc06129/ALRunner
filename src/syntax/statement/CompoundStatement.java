package syntax.statement;
import java.util.ArrayList;
import java.util.HashMap;

import udbparser.udbrawdata.UdbCFGNode;

public class CompoundStatement extends Statement {
	/* field */
	private ArrayList<Statement> body = new ArrayList<Statement>();
	private Statement first;
	private Statement last;
	
	
	/* method */
	/* Providing the compound statement, and returning the last statement of this compound statment*/
	public Statement makeCompound(HashMap<UdbCFGNode, Statement> ntos, HashMap<Statement, UdbCFGNode> ston, ArrayList<UdbCFGNode> cfg, int start, int destination) {
		Statement lastStatement = new Statement();
		int n = start;
		while (n < destination) {
			if (!(ntos.get(cfg.get(n)) instanceof EndStatement)) {
				lastStatement = ntos.get(cfg.get(n));
				this.addBody(ntos.get(cfg.get(n)));
			}
			if (cfg.get(n).getEnd_structure_node() != -2)
				n = cfg.get(n).getEnd_structure_node();
			n++;
		}
		return lastStatement;
	}
	
	public void addBody(Statement statement) {
		this.body.add(statement);
	}
	
	/* getter and setter */
	public Statement getFirst() {
		return this.first;
	}
	
	public void setFirst(Statement first) {
		this.first = first;
	}
	
	public Statement getLast() {
		return this.last;
	}
	
	public void setLast(Statement last) {
		this.last = last;
	}
	
	public ArrayList<Statement> getBody(){
		return this.body;
	}
	
	public void setBody(ArrayList<Statement> body) {
		this.body = body;
	}
	
}
