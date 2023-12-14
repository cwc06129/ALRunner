package syntax.expression;

public class AtomicExpression extends Expression {
	//chl@2023.02.07 - print template
	public String toString() {
		return this.getClass().getSimpleName();
	}
	//chl@2023.02.08 - code print override
	public String getCode() {
		
			return "";
	}
}
