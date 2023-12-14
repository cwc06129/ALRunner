package syntax.expression;

public class CastExpression extends CompoundExpression {
	/* field */
	private Expression cast;
	private Type castType;

	public CastExpression(String castType) {
		this.setType(castType);
	}

	/* getter and setter */
	public Expression getCast() {
		return cast;
	}
	public void setCast(Expression cast) {
		this.cast = cast;
	}
	public Type getType() {
		return castType;
	}
	public void setType(String type) {
		this.castType = new Type(type);
	}
	//chl@2023.02.07 - print template
	public String toString() {
		return this.getClass().getSimpleName() + "-> " + cast + ", " + castType;
	}
	//chl@2023.02.08 - code print override
	public String getCode() {

		if (this.cast == null){
			return "(" + getType() + ")";
		}
		return "(" + getType() + ")" + cast.getCode();

	}
}
