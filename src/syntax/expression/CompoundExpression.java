package syntax.expression;

import java.util.ArrayList;



public class CompoundExpression extends Expression {
	/* field */
	private ArrayList<Expression> expressions = new ArrayList<Expression>();

	/* method */
	public void AddExpression(Expression expression) {
		this.expressions.add(expression);
	}
	
	/* getter and setter */
	public ArrayList<Expression> getExpression() {
		return expressions;
	}
	public void setExpression(ArrayList<Expression> expressions) {
		this.expressions = expressions;
	}
	//chl@2023.02.07 - print template
	public String toString() {
		return this.getClass().getSimpleName() + expressions;
	}
	//chl@2023.02.08 - code print override
	public String getCode() {
		String code = "";
		if (this instanceof ArrayExpression)
			code += ((ArrayExpression)this).getCode();
		else if (this instanceof BinaryExpression)
			code += ((BinaryExpression)this).getCode();
		else if (this instanceof UnaryExpression)
			code += ((UnaryExpression)this).getCode();
		else if (this instanceof CallExpression)
			code += ((CallExpression)this).getCode();
		else if (this instanceof CastExpression)
			code += ((CastExpression)this).getCode();
		else if (this instanceof ConditionalExpression)
			code += ((ConditionalExpression)this).getCode();
		else if (this instanceof DeclExpression)
			code +=((DeclExpression)this).getCode();
		else {
			int curlybracket = 0;
			for (Expression e: expressions){
				code += e.getCode();
				if (e instanceof BracketExpression && ((BracketExpression)e).getType().equals("{}")){
					curlybracket = 1;
				}
				else if (curlybracket == 1 && e != expressions.get(expressions.size()-2)){
					code += ", ";
				}
				
			}
		}
		return code;
	}

}
