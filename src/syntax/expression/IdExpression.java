package syntax.expression;

import syntax.variable.Variable;

public class IdExpression extends AtomicExpression {
	/* field */
	private String name;
	private String scope;
	private Type type;
	private Variable var;
	private String classifier;
	//for allocator
	private String up_down = "";
	private int value = 999;

	//sohee
	private String fixed_name;

	public String getFixed_name() {
		return this.fixed_name;
	}

	public void setFixed_name(String fixed_name) {
		this.fixed_name = fixed_name;
	}


	public String getUp_down() {
		return this.up_down;
	}

	public void setUp_down(String up_down) {
		this.up_down = up_down;
	}

	public int getValue() {
		return this.value;
	}

	public void setValue(int value) {
		this.value = value;
	}

	public String getClassifier() {
		return this.classifier;
	}

	public void setClassifier(String classifier) {
		this.classifier = classifier;
	}

	public Variable getVar() {
		return this.var;
	}

	public void setVar(Variable var) {
		this.var = var;
	}

	public IdExpression(String name, String scope, String type, Variable var) {
		this.setName(name);
		this.setScope(scope);
		this.setType(type);
		this.setVar(var);
	}

	/* method */
	public String getRawData(char c) {
		//"%d %d\n" -> \"%d %d\\n\"
		return name.replace('"','\"').replace("\n","\\n");
	}

	/* getter and setter */
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getScope() {
		return scope;
	}
	public void setScope(String scope) {
		this.scope = scope;
	}
	public Type getType() {
		return type;
	}
	public void setType(String type) {
		this.type = new Type(type);
	}
	//chl@2023.02.07 - print template
	public String toString() {
		return this.getClass().getSimpleName() + "-> " + name + ", " + scope + ", " + type;
	}
	//chl@2023.02.08 - code print override
	// public String getCode() {
	// 		return name;
	// }
	
	// 2023-04-09(Sun) SoheeJung
	// add fixed_name return part
	public String getCode() {
		if(fixed_name != null) return fixed_name;
		else return name;
	}
}
