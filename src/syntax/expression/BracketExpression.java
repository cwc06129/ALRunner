package syntax.expression;

public class BracketExpression extends Expression {
	private String type;//chl@2023.02.28 - (,{,[
	private boolean position;//chl@2023.02.28 - true: beginning, false: end

	public String getType() {
		return this.type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public boolean getPosition() {
		return this.position;
	}

	public void setPosition(boolean position) {
		this.position = position;
	}
	 

	//chl@2023.02.07 - print template
	public String toString() {
		return this.getClass().getSimpleName();
	}
	//chl@2023.02.08 - code print override
	public String getCode() {
			if (type.equals("{}")){
				if (position == true) return "{";
				else return "}";
			}else if (type.equals("[]")){
				if (position == true) return "[";
				else return "]";
			}else if (type.equals("()")){
				if (position == true) return "(";
				else return ")";
			}
			return "";

	}
}
