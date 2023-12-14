package syntax.expression;

import java.util.ArrayList;

import syntax.statement.Statement;

public class DefineExpression extends CallExpression {
    /* field */
	private String macroName;

	/* getter and setter */
	public String getmacroName() {
		return macroName;
	}
	public void setmacroName(String macroName) {
		this.macroName = macroName;
	}
	//chl@2023.02.07 - print template
	public String toString() {
		return this.getClass().getSimpleName() + "-> " + macroName;
	}	
	//chl@2023.02.08 - code print override
	public String getCode() {

		String code = "";
		code += macroName ;
		for(Expression e : this.getActualParameters()){
			if (e instanceof CompoundExpression){
				if (this.macroName.equals("sizeof") && (e instanceof BinaryExpression || e instanceof UnaryExpression)){
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
