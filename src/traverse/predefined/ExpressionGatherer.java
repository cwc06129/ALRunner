package traverse.predefined;

import java.util.ArrayList;
import java.util.List;

import syntax.expression.Expression;
import syntax.statement.Statement;
import traverse.general.DFSExprTraverser;

public class ExpressionGatherer extends DFSExprTraverser {
	
	private List<Expression> allExpressions = new ArrayList<>();
	
	public List<Expression> getAllExpression(Statement f) {
		allExpressions.clear();
		traverse(f);
		return allExpressions;
	}
	
	@Override
	public void visit(Expression f) {
		allExpressions.add(f);
	}

}
