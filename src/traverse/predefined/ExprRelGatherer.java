package traverse.predefined;

import java.util.ArrayList;
import java.util.List;

import syntax.expression.ArrayExpression;
import syntax.expression.BinaryExpression;
import syntax.expression.CallExpression;
import syntax.expression.CastExpression;
import syntax.expression.ConditionalExpression;
import syntax.expression.DeclExpression;
import syntax.expression.Expression;
import syntax.expression.UnaryExpression;
import syntax.statement.Statement;
import traverse.general.DFSExprTraverser;

public class ExprRelGatherer extends DFSExprTraverser {
	
	private List<ExprRel> allExprRel = new ArrayList<>();
	
	public List<ExprRel> getAllRelation(Statement f) {
		allExprRel.clear();
		traverse(f);
		return allExprRel;
	}
	
	@Override
	public void visit(ConditionalExpression f) {
		allExprRel.add(new ExprRel(f, f.getCondition()));
		allExprRel.add(new ExprRel(f, f.getTrueExpr()));
		allExprRel.add(new ExprRel(f, f.getFalseExpr()));
	}
	
	@Override
	public void visit(ArrayExpression f) {
		//allExprRel.add(new ExprRel(f, f.getIndexExpression()));
		if(f.getNestedArrayExpression() != null)
			allExprRel.add(new ExprRel(f, f.getNestedArrayExpression()));
	}
	
	@Override
	public void visit(UnaryExpression f) {
		allExprRel.add(new ExprRel(f, f.getOperand()));
	}
	
	@Override
	public void visit(BinaryExpression f) {
		allExprRel.add(new ExprRel(f, f.getLhsOperand()));
		allExprRel.add(new ExprRel(f, f.getRhsOperand()));
	}
	
	@Override
	public void visit(CallExpression f) {
		for (Expression e : f.getActualParameters()) {
			allExprRel.add(new ExprRel(f, e));
		}
	}
	
	@Override
	public void visit(CastExpression f) {
		allExprRel.add(new ExprRel(f, f.getCast()));
	}
	
	@Override
	public void visit(DeclExpression f) {
		// ����(20210723)
		for(Expression e : f.getExpression())
			allExprRel.add(new ExprRel(f, e));
		if(f.getBinaryExpression() != null)
			allExprRel.add(new ExprRel(f, f.getBinaryExpression()));
	}
	
	public class ExprRel {
		private Expression predecessor;
		private Expression successor;
		public ExprRel(Expression predecessor, Expression successor) {
			super();
			this.predecessor = predecessor;
			this.successor = successor;
		}
		public Expression getPredecessor() {
			return predecessor;
		}
		public void setPredecessor(Expression predecessor) {
			this.predecessor = predecessor;
		}
		public Expression getSuccessor() {
			return successor;
		}
		public void setSuccessor(Expression successor) {
			this.successor = successor;
		}
	}

}
