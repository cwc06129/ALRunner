package syntax.statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import syntax.expression.DeclExpression;
import syntax.expression.Expression;
import syntax.expression.TypedefExpression;
import udbparser.udbrawdata.UdbCFGNode;
import udbparser.udbrawdata.UdbLexemeNode;

public class DeclarationStatement extends Statement {
	/* field */
	private List<Expression> declExpression;
	
	/* method */
	public void addDeclExpression(DeclExpression declExpression) {
		this.declExpression.add(declExpression);
	}
	
	/* getter and setter */
	public List<Expression> getDeclExpression() {
		return declExpression;
	}

	public void setDeclExpression(List<Expression> declExpression2) {
		this.declExpression = declExpression2;
	}
	//chl@2023.02.08 - override
	public ArrayList<Expression> getListExpressions() {
		ArrayList<Expression> tmp = new ArrayList<Expression>();
		tmp.addAll(declExpression);
		return tmp;
	}

	//chl@2023.02.13 - assign expression
	public void setExpression(HashMap<Function, Integer> numberOfParam, HashMap<Function, Boolean> typeOfFunc, ArrayList<String> defines){
		//this.postfix = super.makePostfixFromLexOfStmt(numberOfParam); //get postfix of lex from statement class
 		super.setExpression(numberOfParam, typeOfFunc, defines);

		DeclExpression declExpr = new DeclExpression(null, null, null);
		this.expressions = declExpr.makeExprFromLex(this.getRawData(), this.theNumberOfFunctionParameter, typeOfFunc, defines);


		declExpression = new ArrayList<Expression>();

		for (Expression exp : this.expressions) {
			if (exp instanceof TypedefExpression){
				((TypedefExpression)exp).makeDeclExpr();
				declExpression.add(exp);
				continue;
			} else {
				DeclExpression decl = new DeclExpression(null, null, null);
				decl.makeDeclExpression(exp);
				declExpression.add(decl);
			}
		}
		

		this.setDeclExpression(declExpression);
		
	}

	public ArrayList<Integer> findComma(ArrayList<UdbLexemeNode> rawData){
		ArrayList<Integer> comma = new ArrayList<Integer>();
		for (int i=0 ; i<rawData.size() ; i++){
			if (rawData.get(i).getData().equals(",")){
				comma.add(i);
			}
		}
		return comma;
	}
}
