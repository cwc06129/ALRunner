package syntax.expression;
import java.util.ArrayList;

import syntax.statement.Statement; 

public class CallExpression extends CompoundExpression {
	/* field */
	private ArrayList<Expression> actualParameters = new ArrayList<Expression>();
	private String functionalName;
	
	public String getFunctionalName() {
		return functionalName;
	}
	public void setFunctionalName(String functionalName) {
		this.functionalName = functionalName;
	}
	/* method */
	public void addActualParameters(Expression actualParameters) {
		this.actualParameters.add(actualParameters);
	}
	
	/* getter and setter */
	public ArrayList<Expression> getActualParameters() {
		return actualParameters;
	}
	public void setActualParameters(ArrayList<Expression> actualParameters) {
		this.actualParameters = actualParameters;
	}

	//chl@2023.02.07 - print template
	public String toString() {
		return this.getClass().getSimpleName() + "-> " + actualParameters;
	}
	//chl@2023.02.08 - code print override
	public String getCode() {

		String code = "";
		code += functionalName;
		for(Expression e : this.getActualParameters()){
			if (e instanceof CompoundExpression){
				for (Expression expr : ((CompoundExpression)e).getExpression()){
					code += expr.getCode();
					if (expr instanceof BracketExpression == false && ((CompoundExpression)e).getExpression().get(((CompoundExpression)e).getExpression().size()-2) != expr){
						code += ", ";
					}
				}
			} else {
				code += e.getCode();
				if (this.getActualParameters().get(this.getActualParameters().size()-1) != e){
					code += ", ";
				}
			}
		}
		return code;

	}

	public void makeExpression(Statement stmt, ArrayList<Expression> expressions){
		
	}
}
