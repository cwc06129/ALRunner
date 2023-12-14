package util;

import java.util.ArrayList;

public class SourceCode {
	/* field */
	private String functionName;
	private ArrayList<String> code;
	
	/* method */
	public SourceCode() {
		code = new ArrayList<String>();
	}
	
	public void addCode(String newCode) {
		this.code.add(newCode);
	}
	
	/* getter and setter */
	public String getFunctionName() {
		return functionName;
	}

	public void setFunctionName(String functionName) {
		this.functionName = functionName;
	}
	
	public ArrayList<String> getCode(){
		return this.code;
	}
	
	public void setCode(ArrayList<String> code) {
		this.code = code;
	}
}
