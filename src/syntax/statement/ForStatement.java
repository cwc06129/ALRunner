package syntax.statement;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

import syntax.expression.BracketExpression;
import syntax.expression.CastExpression;
import syntax.expression.CompoundExpression;
import syntax.expression.ConditionalExpression;
import syntax.expression.DeclExpression;
import syntax.expression.EmptyExpression;
import syntax.expression.Expression;
import syntax.expression.LiteralExpression;
import syntax.expression.NullExpression;
import udbparser.udbrawdata.UdbLexemeNode;

public class ForStatement extends LoopStatement {
	/* field */
	private Expression initExpression = new Expression();
	private Expression iterExpression = new Expression();
	
	
	/* getter and setter */
	public Expression getInitExpression() {
		return initExpression;
	}
	public void setInitExpression(Expression initExpression) {
		this.initExpression = initExpression;
	}
	public Expression getIterExpression() {
		return iterExpression;
	}
	public void setIterExpression(Expression iterExpression) {
		this.iterExpression = iterExpression;
	}
	//chl@2023.02.08 - override
	public ArrayList<Expression> getListExpressions() {
		ArrayList<Expression> tmp = new ArrayList<Expression>();
		tmp.add(getInitExpression());
		tmp.add(getIterExpression());
		return tmp;
	}

	/*chl@2023.02.13 - makeExpression func */
    public void setExpression(HashMap<Function, Integer> numberOfParam, HashMap<Function, Boolean> typeOfFunc, ArrayList<String> defines) {
		//this.postfix = super.makePostfixFromLexOfStmt(numberOfParam); //get postfix of lex from statement class
		super.setExpression(numberOfParam, typeOfFunc, defines);
		ConditionalExpression condexpr = new ConditionalExpression();
		this.expressions = condexpr.makeExprFromLex(this.getRawData(), this.theNumberOfFunctionParameter, typeOfFunc, defines);
	
		int type = 0, init = 0;
		ArrayList<Expression> set = new ArrayList<Expression>();
		for (int i=0 ; i < 3 ; i++){
			set.add(new NullExpression());
		}
		for (int i=0 ; i < ((CompoundExpression)this.expressions.get(0)).getExpression().size() ; i++){
			if (((CompoundExpression)this.expressions.get(0)).getExpression().get(i) instanceof LiteralExpression && ((LiteralExpression)((CompoundExpression)this.expressions.get(0)).getExpression().get(i)).getConstant().equals(";") ){
				type ++;
			} else if (((CompoundExpression)this.expressions.get(0)).getExpression().get(i) instanceof BracketExpression) {
				continue;
			} else if (((CompoundExpression)this.expressions.get(0)).getExpression().get(i) instanceof CastExpression){
				init = 1;
			} else {
				set.remove(type);
				set.add(type, ((CompoundExpression)this.expressions.get(0)).getExpression().get(i));
			}
		}

		if (init == 1) {
			DeclExpression declExpr = new DeclExpression(null, null, null);
			declExpr.makeDeclExpression(set.get(0));
			this.setInitExpression(declExpr);
		} else {
			this.setInitExpression(set.get(0));
		}
		this.setCondition(set.get(1));
		this.setIterExpression(set.get(2));

		//this.setCondition(condexpr);
		
	}


	public ArrayList<ArrayList<UdbLexemeNode>> splitedBySemiColon(ArrayList<UdbLexemeNode> lexemes) {
		ArrayList<ArrayList<UdbLexemeNode>> bufs = new ArrayList<ArrayList<UdbLexemeNode>>();
		ArrayList<UdbLexemeNode> buf;
		buf = new ArrayList<UdbLexemeNode>();
		for (UdbLexemeNode lexeme : lexemes) {
			if (lexeme.getData().equals(";")) {
				bufs.add(buf);
				buf = new ArrayList<UdbLexemeNode>();
			} else {
				buf.add(lexeme);
			}
		}
		bufs.add(buf);
		return bufs;
	}
}
