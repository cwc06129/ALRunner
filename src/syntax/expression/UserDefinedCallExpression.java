package syntax.expression;

import syntax.statement.Function;

public class UserDefinedCallExpression extends CallExpression {
	/* field */
	private Function calleeFunction = new Function();

	/* getter and setter */
	public Function getCalleeFunction() {
		return calleeFunction;
	}
	public void setCalleeFunction(Function calleeFunction) {
		this.calleeFunction = calleeFunction;
	}
	//chl@2023.02.07 - print template
	public String toString() {
		return this.getClass().getSimpleName() + "-> " + calleeFunction;
	}
	//chl@2023.02.08 - code print override
	public String getCode() {

		String code = "";
		code += calleeFunction.getName() ;
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
}
