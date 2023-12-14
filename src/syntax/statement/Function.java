package syntax.statement;
import java.util.ArrayList;

import syntax.expression.IdExpression;
import syntax.variable.Variable;

public class Function implements Cloneable {
	/* field */
	private String return_type;
	private String name;
	private ArrayList<Variable> parameters;
	private Statement body;
	private int start_line;
	private int end_line;
	private ArrayList<IdExpression> formalParameter;
	private ArrayList<IdExpression> localVariables;

	public Function(){
		body = new Statement();
	}

	public Function(String return_type, String name, ArrayList<Variable> parameters, int start_line, int end_line){
		this.return_type = return_type;
		this.name = name;
		this.parameters = parameters;
		this.body = new Statement();
		this.start_line = start_line;
		this.end_line = end_line;
	}
	
	/* method */
	public void addFormalParameter(IdExpression formalParameter) {
		this.formalParameter.add(formalParameter);
	}
	public void addLocalVariables(IdExpression localVariable) {
		this.localVariables.add(localVariable);
	}
	
	/* getter and setter */
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public Statement getBody(){
		return (Statement) (CompoundStatement) body;
	}
	public void setBody(Statement body) {
		this.body = body;
	}
	public ArrayList<IdExpression> getFormalParameter() {
		return formalParameter;
	}
	public void setFormalParameter(ArrayList<IdExpression> formalParameter) {
		this.formalParameter = formalParameter;
	}
	public ArrayList<IdExpression> getLocalVariables() {
		return localVariables;
	}
	public void setLocalVariables(ArrayList<IdExpression> localVariables) {
		this.localVariables = localVariables;
	}
	public String getReturn_type() {
		return return_type;
	}
	public void setReturn_type(String return_type) {
		this.return_type = return_type;
	}
	public ArrayList<Variable> getParameters() {
		return parameters;
	}
	public void setParameters(ArrayList<Variable> parameters) {
		this.parameters = parameters;
	}
	public int getStart_line() { return start_line; }
	public void setStart_line(int start_line) {
		this.start_line = start_line;
	}
	public int getEnd_line() {
		return end_line;
	}
	public void setEnd_line(int end_line) {
		this.end_line = end_line;
	}


	//chl@2023.02.17 - check librarycall(1) or userdefinedcall(0)
	public boolean returnLibraryType(){

		if (((CompoundStatement)this.getBody()).getBody().size() == 3){//librarycall -> compoundstatement:pesudostatement-nullstatement-pesudostatement
			if (((CompoundStatement)this.getBody()).getBody().get(0) instanceof PseudoStatement && ((CompoundStatement)this.getBody()).getBody().get(1) instanceof NullStatement && ((CompoundStatement)this.getBody()).getBody().get(2) instanceof PseudoStatement)
				return true;
		}
		
		return false;
	}

	@Override
	public String toString() {
		StringBuilder result = new StringBuilder();
		result.append(return_type + " " + name + "(");

		if(parameters != null) {
			for (int i = 0; i < parameters.size(); i++) {
				if (parameters.size() > 1 && i < parameters.size() - 1) {
					result.append(", ");
				}
				result.append(parameters.get(i).getType() + " " + parameters.get(i).getName());
			}
		}
		result.append(")");

		return String.valueOf(result);
	}
}