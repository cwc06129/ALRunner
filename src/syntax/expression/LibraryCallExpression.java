package syntax.expression;

import java.util.ArrayList;

import syntax.statement.Statement;

public class LibraryCallExpression extends CallExpression {
	/* field */
	private String functionalName;

	/* getter and setter */
	public String getFunctionalName() {
		return functionalName;
	}
	public void setFunctionalName(String functionalName) {
		this.functionalName = functionalName;
	}
	//chl@2023.02.07 - print template
	public String toString() {
		return this.getClass().getSimpleName() + "-> " + functionalName;
	}	
	//chl@2023.02.08 - code print override
	public String getCode() {

		String code = "";
		code += functionalName ;
		for(Expression e : this.getActualParameters()){
			if (e instanceof CompoundExpression){
				if (this.functionalName.equals("sizeof") && (e instanceof BinaryExpression || e instanceof UnaryExpression)){
					code += e.getCode();
					break;
				}
				for (Expression expr : ((CompoundExpression)e).getExpression()){
					code += expr.getCode();
					if (expr instanceof BracketExpression == false && ((CompoundExpression)e).getExpression().size()-2 >=0 && ((CompoundExpression)e).getExpression().get(((CompoundExpression)e).getExpression().size()-2) != expr){
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
		super.makeExpression(stmt, expressions);
	}
}
