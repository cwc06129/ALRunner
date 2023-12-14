package syntax.expression;

import java.util.ArrayList;


public class ControlExpression extends CallExpression {
    /* field */
	private String functionalName;

	/* getter and setter */
	public String getFunctionalName() {
		return functionalName;
	}
	public void setFunctionalName(String functionalName) {
		this.functionalName = functionalName;
        super.setFunctionalName(functionalName);
	}
	//chl@2023.02.07 - print template
	public String toString() {
		return this.getClass().getSimpleName() + "-> " + functionalName;
	}	
	//chl@2023.02.08 - code print override
	public String getCode() {

        if (functionalName.equals("exit"))
            return super.getCode();
		String code = "";
		code += functionalName + " ";
		for(Expression e : this.getActualParameters()){
			code += e.getCode();
			if (this.getActualParameters().get(this.getActualParameters().size()-1) != e){
				code += " ";
			}
		}
		return code;

	}

	public void makeExpression(ArrayList<Expression> expressions){
        //for returnstatement
		this.functionalName = ((ControlExpression)expressions.get(0)).getFunctionalName();
		//ArrayList<Expression> returnParam = new ArrayList<Expression>();
			
		//if (expressions.size() > 1)
		//	returnParam.add(expressions.get(1));
		this.setActualParameters(((ControlExpression)expressions.get(0)).getActualParameters());
	}
}
