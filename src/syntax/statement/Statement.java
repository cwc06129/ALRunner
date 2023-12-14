package syntax.statement;

import udbparser.expr.NewOperatorInfo;
import udbparser.udbrawdata.UdbLexemeNode;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Stack;

import syntax.expression.Expression;

public class Statement {
	/* field */
	private int statementId;
	private Statement next;
	private ArrayList<Statement> before;
	private Stack<Statement> parents;

	private ArrayList<UdbLexemeNode> rawData;
	private Statement end_block;
	private String rawStringData;

	private ArrayList<UdbLexemeNode> def;
	private ArrayList<UdbLexemeNode> ref;
	private Statement nextLine;

	//chl@2023.02.13 - makePostfixFromLexOfStmt
	protected HashMap<UdbLexemeNode, String> operatorKind;
	protected HashMap<UdbLexemeNode, Integer> theNumberOfFunctionParameter;
	protected HashMap<Function, Integer> NumOfParam;
	ArrayList<UdbLexemeNode> postfix;
	public ArrayList<Expression> expressions;

	public Statement getNextLine() {
		return nextLine;
	}

	public void setNextLine(Statement nextLine) {
		this.nextLine = nextLine;
	}

	/* method */
	public Statement() {
		next = null;
		before = null;
		rawData = new ArrayList<UdbLexemeNode>();
		end_block = null;
		parents = new Stack<Statement>();
		rawStringData = "";
		nextLine = null;

		def = new ArrayList<UdbLexemeNode>();
		ref = new ArrayList<UdbLexemeNode>();
	}

	public void addBefore(Statement statement) {
		if (before == null) {
			before = new ArrayList<Statement>();
		}
		this.before.add(statement);
	}

	public void addLexemeNode(UdbLexemeNode udbLexemeNode) {
		this.rawData.add(udbLexemeNode);
	}

	public String toString() {
		String str = "<" + this.getStatementId() + ". " + this.getClass().getSimpleName() + "> : " + this.rawStringData;
		return str;
	}

	public void addRawStringData(String str) {
		this.rawStringData = this.rawStringData + str;
	}

	public String printStatement() {
		String str = "<" + this.getStatementId() + ". " + this.getClass().getSimpleName() + ">";
		return str;
	}

	/* getter and setter */
	public Statement getNext() {
		return this.next;
	}

	public void setNext(Statement statement) {
		this.next = statement;
	}

	public ArrayList<Statement> getBefore() {
		return this.before;
	}

	public void setBefore(ArrayList<Statement> before) {
		if (this.before == null) {
			this.before = new ArrayList<Statement>();
		}
		this.before.clear();
		for (int i = 0; i < before.size(); i++) {
			this.before.add(before.get(i));
		}
	}

	public ArrayList<UdbLexemeNode> getRawData() {
		return this.rawData;
	}

	public void setRawData(ArrayList<UdbLexemeNode> rawData) {
		this.rawData = rawData;
		for (UdbLexemeNode lxm : rawData) {
			this.rawStringData = this.rawStringData + lxm.getData();
		}
	}

	public void setRawStringData(ArrayList<UdbLexemeNode> rawData){
		if (this.rawStringData.isEmpty() == false) this.rawStringData = "";
		for (UdbLexemeNode lxm : rawData) {
			this.rawStringData = this.rawStringData + lxm.getData();
		}
	}

	public Statement getEnd_block() {
		return this.end_block;
	}

	public void setEnd_block(Statement statement) {
		this.end_block = statement;
	}

	public int getStatementId() {
		return statementId;
	}

	public void setStatementId(int statementId) {
		this.statementId = statementId;
	}

	public Stack<Statement> getParents() {
		return this.parents;
	}

	public void setParents(Stack<Statement> parents) {
		this.parents = parents;
	}

	public String getRawStringData() {
		return this.rawStringData;
	}
	
	//chl@2023.02.08
	public Expression getExpressions() {
		return null;
	}
	
	//chl@2023.02.08 - for for-loop
	public ArrayList<Expression> getListExpressions() {
		return null;
	}

	public HashMap<UdbLexemeNode, String> getOperatorKind(){
		return operatorKind;
	}

	public HashMap<UdbLexemeNode, Integer> gettheNumberOfFunctionParameter(){
		return theNumberOfFunctionParameter;
	}

	public HashMap<Function, Integer> getNumOfParam(){
		return NumOfParam;
	}
	
	//chl@2023.02.13 - set expression list override
	public void setExpression(HashMap<Function, Integer> numberOfParam, HashMap<Function, Boolean> typeOfFunc, ArrayList<String> defines){
		theNumberOfFunctionParameter = new HashMap<UdbLexemeNode, Integer>();
		for (UdbLexemeNode lexeme : this.getRawData()){
			if (lexeme.getToken().equals("Identifier")) {
				if (lexeme.getRef_kind().equals("Call")) {
					for(Function f : numberOfParam.keySet()) {
						if(f.getName().equals(lexeme.getData())) {
							theNumberOfFunctionParameter.put(lexeme, numberOfParam.get(f));
							break;
						}
					}
				}
			}
		}
	}
}