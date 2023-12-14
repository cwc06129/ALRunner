package traverse.general;

import syntax.expression.*;
import syntax.statement.*;

/**
 * Traverse sub-expression in dfs order 
 * @author Dongwoo
 *
 */
public class DFSExprTraverser {
	
	/**
	 * traverse whole expression on the statement
	 * @param f
	 */
	public void traverse(Statement f) {
		/*chl@2023.02.08 - statement print*/
		System.out.println(f);
		
		if(f == null) {
			throw new NullPointerException("traverse > DFSExprTraverser.java : null statement");
		}
		else if(f instanceof ForStatement) {
			traverse(((ForStatement) f).getInitExpression());
			traverse(((ForStatement) f).getCondition());
			traverse(((ForStatement) f).getIterExpression());
		}
		else if(f instanceof IfStatement) {
			traverse(((IfStatement) f).getCondition());
		}
		else if(f instanceof LoopStatement) {
			traverse(((LoopStatement) f).getCondition());
		}
		else if(f instanceof DoStatement) {
			traverse(((DoStatement) f).getCondition());
		}
		else if(f instanceof DeclarationStatement) {
			for(Expression expr : ((DeclarationStatement) f).getDeclExpression())
				traverse(expr);
		}
		else if(f instanceof CaseStatement) {
			traverse(((CaseStatement) f).getCondition());
		}
		else if(f instanceof ReturnStatement) {
			traverse(((ReturnStatement) f).getReturnExpr());
		}
		else if(f instanceof ExitStatement) {
			traverse(((ExitStatement) f).getExitExpression());
		}
		else if(f instanceof ExpressionStatement) {
			traverse(((ExpressionStatement) f).getExpression());
		}
		else if(f instanceof PseudoStatement) {
			//empty body
		}
		else if(f instanceof ContinueStatement){//chl@2023.02.09 - need to add other statement
			traverse(((ContinueStatement) f).getExpression());
		}
		else {
			throw new RuntimeException("unknown statement : " + f.getClass().getName());
		}
	}
	
	/**
	 * traverse each expression
	 * Note that, some of leaf class is not listed as it is sufficient to traverse some abstract class
	 * @param f
	 */
	private void traverse(Expression f) {
		visit(f);
		
		/*chl@2023.02.08 - expression print*/
		System.out.println(f);
		
		if (f instanceof ConditionalExpression) {
			traverse((ConditionalExpression)f);
		} 
		else if (f instanceof ArrayExpression) {
			traverse((ArrayExpression)f);
		}
		else if (f instanceof UnaryExpression) {
			traverse((UnaryExpression)f);
		}
		else if (f instanceof BinaryExpression) {
			traverse((BinaryExpression)f);
		}
		else if (f instanceof CastExpression) {
			traverse((CastExpression) f);
		}
		else if (f instanceof DeclExpression) {
			traverse((DeclExpression)f);
		}
		else if (f instanceof UserDefinedCallExpression) {
			traverse((UserDefinedCallExpression)f);
		}
		else if (f instanceof LibraryCallExpression) {
			traverse((LibraryCallExpression)f);
		}
		else if(f instanceof AtomicExpression) {
			//empty body : do not need to traverse
		} 
		else if (f instanceof CompoundExpression){//chl@2023.02.13 - compoundExpression
			for (Expression e : ((CompoundExpression)f).getExpression()){
				traverse(e);
			}
		}
		else {
			throw new RuntimeException("unknown expression: " + f.getClass().getName());
		}
		/*chl@2023.02.09 - nested code*/
		if (f.getNext() != null) {
			traverse(f.getNext());
		}
	}
	private void traverse(ConditionalExpression f) {
		traverse(f.getCondition());
		traverse(f.getTrueExpr());
		traverse(f.getFalseExpr());
	}
	private void traverse(ArrayExpression f) {
		if(f.getNestedArrayExpression() != null) {
			traverse((Expression) f.getNestedArrayExpression());
		}
		else
			traverse(f.getAtomic());
		//traverse(f.getIndexExpression());
	}
	private void traverse(UnaryExpression f) {
		traverse(f.getOperand());
	}
	private void traverse(BinaryExpression f) {
		traverse(f.getLhsOperand());
		traverse(f.getRhsOperand());
	}
	private void traverse(UserDefinedCallExpression f) {
		for(Expression e : f.getActualParameters())
			traverse(e);
	}
	private void traverse(LibraryCallExpression f) {
		for(Expression e : f.getActualParameters())
			traverse(e);
	}
	private void traverse(DeclExpression f) {
		if(f.getBinaryExpression() != null)
			traverse((Expression) f.getBinaryExpression());
		else
			for(Expression e : f.getExpression())
				traverse(e);
	}
	private void traverse(CastExpression f) {
		traverse(f.getCast());
	}

	
	/**
	 * <hook function>
	 * visit current expression
	 * If it is not leaf class on the diagram, it further visit its one-level more concrete class,
	 * such that Expression -> CompoundExpression or CompoundExpression -> UnaryExpression
	 * @param f
	 */
	public void visit(Expression f) {
		if (f instanceof CompoundExpression) {
			visit((CompoundExpression)f);
		} else if (f instanceof AtomicExpression) {
			visit((AtomicExpression)f);
		} else {
			throw new RuntimeException("unknown expression: " + f.getClass().getName());
		}
	}
	public void visit(CompoundExpression f) {
		/* 
		if (f instanceof ConditionalExpression) {
			visit((ConditionalExpression)f);
		} 
		else if (f instanceof ArrayExpression) {
			visit((ArrayExpression)f);
		}
		else if (f instanceof UnaryExpression) {
			visit((UnaryExpression)f);
		} else if (f instanceof BinaryExpression) {
			visit((BinaryExpression)f);
		} else if (f instanceof CallExpression) {
			visit((CallExpression)f);
		} else if (f instanceof CastExpression) {
			visit((CastExpression)f);
		} else if (f instanceof DeclExpression) {
			visit((DeclExpression)f);
		} else {
			throw new RuntimeException("unknown expression: " + f.getClass().getName());
		}*/
	}
	public void visit(AtomicExpression f) {
		if (f instanceof NullExpression) {
			visit((NullExpression)f);
		} else if (f instanceof LiteralExpression) {
			visit((LiteralExpression)f);
		} else if (f instanceof IdExpression) {
			visit((IdExpression)f);
		} else {
			throw new RuntimeException("unknown expression: " + f.getClass().getName());
		}
	}
	public void visit(NullExpression f) {}
	public void visit(LiteralExpression f) {}
	public void visit(IdExpression f) {}
	public void visit(ConditionalExpression f) {}
	public void visit(ArrayExpression f) {}
	public void visit(UnaryExpression f) {}
	public void visit(BinaryExpression f) {}
	public void visit(CastExpression f) {}
	public void visit(DeclExpression f) {}
	public void visit(CallExpression f) {
		if (f instanceof UserDefinedCallExpression) {
			visit((UserDefinedCallExpression)f);
		} else if (f instanceof LibraryCallExpression) {
			visit((LibraryCallExpression)f);
		} else {
			throw new RuntimeException("unknown expression: " + f.getClass().getName());
		}
	}
	public void visit(UserDefinedCallExpression f) {}
	public void visit(LibraryCallExpression f) {}

}
