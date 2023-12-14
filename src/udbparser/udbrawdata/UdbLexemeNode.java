package udbparser.udbrawdata;

import syntax.variable.Variable;

public class UdbLexemeNode {
	
	/* field */
	private String data;	// Lexeme data.
	private String ref_kind; // Reference Kind of Lexeme.
	private String token;	// Token kind of Lexeme.
	private Variable var;
	
	/* method */
	public UdbLexemeNode() {
		this.data = "";
		this.ref_kind = "";
		this.token = "";
		this.var = null;
	}

	public UdbLexemeNode(String data, String ref_kind, String token){
		this.data = data;
		this.ref_kind = ref_kind;
		this.token = token;
		this.var = null;
	}

	public UdbLexemeNode(String data, String ref_kind, String token, Variable var) {
		this.data = data;
		this.ref_kind = ref_kind;
		this.token = token;
		this.var = var;
	}
	
	@Override
	public String toString() {
		return "Lexeme [data=" + data + ", ref_kind=" + ref_kind + ", token=" + token + ", var=" + var +"]";
	}
	
	
	/* getter and setter */
	public String getData() {
		return data;
	}

	public void setData(String data) {
		this.data = data;
	}

	public String getRef_kind() {
		return ref_kind;
	}

	public void setRef_kind(String ref_kind) {
		this.ref_kind = ref_kind;
	}

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}

	public Variable getVar() {
		return var;
	}

	public void setVar(Variable var) {
		this.var = var;
	}
}
