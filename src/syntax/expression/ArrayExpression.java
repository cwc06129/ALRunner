package syntax.expression;

import java.util.ArrayList;

public class ArrayExpression extends CompoundExpression {
	/* field */
	private ArrayExpression nestedArrayExpression;
	private IdExpression atomic;
	private ArrayList<Expression> indexExpression;

	/* method */

	public ArrayExpression(IdExpression atomic) {
		this.setAtomic(atomic);
		this.indexExpression = new ArrayList<Expression>();
	}

	public ArrayExpression(IdExpression atomic, ArrayList<Expression> indexExpression) {
		this.setAtomic(atomic);
		this.setIndexExpression(indexExpression);
	}

	public ArrayExpression(ArrayExpression nestedArrayExpression, IdExpression atomic, ArrayList<Expression> indexExpression) {
		this.setNestedArrayExpression(nestedArrayExpression);
		this.setAtomic(atomic);
		this.setIndexExpression(indexExpression);
	}

	/* getter and setter */
	public ArrayExpression getNestedArrayExpression() {
		return nestedArrayExpression;
	}

	public void setNestedArrayExpression(ArrayExpression nestedArrayExpression) {
		this.nestedArrayExpression = nestedArrayExpression;
	}

	public IdExpression getAtomic() {
		return atomic;
	}

	public void setAtomic(IdExpression atomic) {
		this.atomic = atomic;
	}

	public ArrayList<Expression> getIndexExpression() {
		return indexExpression;
	}

	public void setIndexExpression(ArrayList<Expression> indexExpression) {
		this.indexExpression = indexExpression;
	}

	public void addIndexExpression(Expression indexExpression) {
		this.indexExpression.add(indexExpression);
	}

	//chl@2023.02.07 - print template
	public String toString() {
		return this.getClass().getSimpleName();
	}
	//chl@2023.02.08 - code print override
	public String getCode() {

		String code = atomic.getCode();
		for (Expression e : indexExpression){
			code += e.getCode();
		}
		return code;
		// indexExpression.getCode() + "]";

	}
}
