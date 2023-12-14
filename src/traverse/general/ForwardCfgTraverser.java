package traverse.general;

import java.util.HashMap;

import syntax.statement.BranchStatement;
import syntax.statement.BreakStatement;
import syntax.statement.CaseStatement;
import syntax.statement.CompoundStatement;
import syntax.statement.ContinueStatement;
import syntax.statement.ControlStatement;
import syntax.statement.DeclarationStatement;
import syntax.statement.DefaultStatement;
import syntax.statement.DoStatement;
import syntax.statement.EmptyStatement;
import syntax.statement.ExitStatement;
import syntax.statement.ExpressionStatement;
import syntax.statement.ForStatement;
import syntax.statement.Function;
import syntax.statement.GotoStatement;
import syntax.statement.IfStatement;
import syntax.statement.LabelStatement;
import syntax.statement.LabeledStatement;
import syntax.statement.LoopStatement;
import syntax.statement.NullStatement;
import syntax.statement.PseudoStatement;
import syntax.statement.ReturnStatement;
import syntax.statement.Statement;
import syntax.statement.SwitchStatement;

/**
 * Traverse block level information in order
 * Order of visited element is equal to line order 
 * @author Dongwoo
 *
 */
public class ForwardCfgTraverser {
	/**
	 * check overlaped visit
	 */
	HashMap<Statement, Boolean> visited = new HashMap<Statement, Boolean>();
	
	/**
	 * traverse whole statement on a function
	 * @param f
	 */
	public void traverse(Function f) {
		traverse(f.getBody());
	}
	
	/**
	 * traverse each statement
	 * Note that, some of classes are not listed as it is sufficient to traverse some abstract class
	 * @param f
	 */
	private void traverse(Statement f) {
		if (visited.containsKey(f)) {
			return;
		}
		System.out.println(f);
		
		//chl@2023.02.08 - print Expressions
		/*		
		if (f instanceof ForStatement || f instanceof DeclarationStatement) {
			for (Expression expr : f.getListExpressions()) {
					System.out.println(expr);
				}
		}else if (f.getExpressions() != null) {
			System.out.println(f.getExpressions());
		} //출력을 오리지널 코드로 하기
		*/
		
//		System.out.println("this is next statement : " + f.getNext());
//		if(f instanceof IfStatement) System.out.println("this is else statement : " + ((IfStatement)f).getEls());
		visited.put(f, true); 
		visit(f);
		if (f instanceof BranchStatement) { // for, do, if
			traverse((BranchStatement)f);
		} else if (f instanceof CaseStatement) {
			traverse((CaseStatement)f);
		} else if (f instanceof CompoundStatement) {
			traverse((CompoundStatement)f);
		} else if (f instanceof ControlStatement) { // goto, continue, break, exit, return 
			traverse((ControlStatement)f);
		} else if (f instanceof DeclarationStatement) {
			traverse((DeclarationStatement)f);
		} else if (f instanceof DefaultStatement) {
			traverse((DefaultStatement)f);
		} else if (f instanceof EmptyStatement) {
			traverse((EmptyStatement)f);
		} else if (f instanceof PseudoStatement) {
			traverse((PseudoStatement)f);
		} else if (f instanceof ExpressionStatement) {
			traverse((ExpressionStatement)f);
		} else if (f instanceof LabeledStatement) { // label 
			traverse((LabeledStatement)f);
		} else if (f instanceof SwitchStatement) {
			traverse((SwitchStatement)f);
		} else if (f instanceof NullStatement) {
			return;
		} else {
			throw new RuntimeException("unknown statement: " + f.getClass().getName());
		}
	}
	
	// else ���� ���� ó���� ��ģ ��, next statement�� �ҷ����� �ʾƾ� �Ѵ�.
	// 20210906
	private void traverse(BranchStatement stmt) {
		traverse((Statement)stmt.getThen());
		traverse((Statement)stmt.getEls());
		// else ���� ���� ��쿡�� NullStatement�� ������ִµ�, ���� else ���� ���� ��쿡�� �ƿ� ������� ���� ���� ����.
		// 20210907
//		if(((Statement)stmt.getEls()).getStatementId() != 0)
//			traverse((Statement)stmt.getEls());
		//traverse((Statement)stmt.getNext());
	}
	private void traverse(CaseStatement stmt) {
		traverse((Statement)stmt.getBody());
	}
	private void traverse(CompoundStatement stmt) {
		for (Statement s : stmt.getBody()) {
			traverse(s);
		}
	}
	// exit�̳� return�� ������ �� ���� statement�� �̾���� ���� �ƴ϶� �ű⼭ ������ ��.
	// 20210827
	// list �������� �����Ϸ��� �ű⼭ ������ ������ �����ϸ� �ȵ�.
	// 20210906
	private void traverse(ControlStatement stmt) {
//		if(stmt instanceof ExitStatement) traverse((Statement)stmt.getNext());
//		else if(stmt instanceof ReturnStatement) traverse((Statement)stmt.getNext());
//		else if(stmt instanceof BreakStatement) traverse((Statement)stmt.getNext());
	}
	private void traverse(DeclarationStatement stmt) {
		traverse((Statement)stmt.getNext());
	}
	private void traverse(DefaultStatement stmt) {
		traverse((Statement)stmt.getBody());
	}
	// ExpressionStatement�� ���� ������� getNext�� statement�� �̾���� ���� �ƴ϶� �׳� EmptyStatement���� traverse�� �����.
	// 20210913
	private void traverse(EmptyStatement stmt) {
		//traverse((Statement)stmt.getNext());
	}
	private void traverse(PseudoStatement stmt) {}
	// ExpressionStatement�� ���ؼ� getNext�� statement�� �̾���� ���� �ƴ϶� �׳� ExpressionStatement���� traverse�� �����. -> leaf statement
	// 20210906
	private void traverse(ExpressionStatement stmt) {
		//traverse((Statement)stmt.getNext());
	}
	private void traverse(LabeledStatement stmt) {
		traverse((Statement)stmt.getNext());
	}
	private void traverse(SwitchStatement stmt) {
		for (CaseStatement c: stmt.getCases()) {
			traverse((Statement)c);
		}
		// default���� ���ؼ� ���Ǹ� �� ���־ default���� ��µ��� ����.
		// default���� ���� ���Ǹ� �߰��Ͽ� default���� ��µǵ��� ��.
		// 20210827
		if(stmt.getDefaultStatement().getStatementId() != 0)
			traverse((Statement)stmt.getDefaultStatement());
	}
	
	/**
	 * <hook function>
	 * visit current statement
	 * If it is not leaf class on the diagram, it further visit its one-level more concrete class,
	 * such that Statement -> BranchStatement or BranchStatement -> IfStatement
	 * @param f
	 */
	public void visit(Statement f) {
		if (f instanceof BranchStatement) {
			visit((BranchStatement)f);
		} else if (f instanceof CompoundStatement) {
			visit((CompoundStatement)f);
		} else if (f instanceof ControlStatement) {
			visit((ControlStatement)f);
		} else if (f instanceof DeclarationStatement) {
			visit((DeclarationStatement)f);
		} else if (f instanceof EmptyStatement) {
			visit((EmptyStatement)f);
		} else if (f instanceof PseudoStatement) {
			visit((PseudoStatement)f);
		} else if (f instanceof ExpressionStatement) {
			visit((ExpressionStatement)f);
		} else if (f instanceof LabeledStatement) {
			visit((LabeledStatement)f);
		} else if (f instanceof SwitchStatement) {
			visit((SwitchStatement)f);
		} else if (f instanceof NullStatement) {
			return;
		} else {
			System.out.println(f);
			throw new RuntimeException("unknown statement: " + f.getClass().getName());
		}
	}
	public void visit(BranchStatement stmt) {
		if (stmt instanceof IfStatement) {
			visit((IfStatement)stmt);
		} else if (stmt instanceof LoopStatement) {
			visit((LoopStatement)stmt);
		} else {
			throw new RuntimeException("unknown statement: " + stmt.getClass().getName());
		}
	}
	public void visit(BreakStatement stmt) {}
	public void visit(CaseStatement stmt) {}
	public void visit(CompoundStatement stmt) {}
	public void visit(ContinueStatement stmt) {}
	public void visit(ControlStatement stmt) {
		if (stmt instanceof GotoStatement) {
			visit((GotoStatement)stmt);
		} else if (stmt instanceof ReturnStatement) {
			visit((ReturnStatement)stmt);
		} else if (stmt instanceof ContinueStatement) {
			visit((ContinueStatement)stmt);
		} else if (stmt instanceof BreakStatement) {
			visit((BreakStatement)stmt);
		}
		else if (stmt instanceof ExitStatement) {
			visit((ExitStatement)stmt);
		}
		else {
			throw new RuntimeException("unknown statement: " + stmt.getClass().getName());
		}
	}
	public void visit(DeclarationStatement stmt) {}
	public void visit(DoStatement stmt) {}
	public void visit(EmptyStatement stmt) {}
	public void visit(PseudoStatement stmt) {}
	public void visit(ExitStatement stmt) {}
	public void visit(ExpressionStatement stmt) {}
	public void visit(ForStatement stmt) {}
	public void visit(GotoStatement stmt) {}
	public void visit(IfStatement stmt) {}
	// LabelStatement�� leafNode�� �ǵ��� �ش� visit ���� ������ ��.
	// 20210827
	public void visit(LabelStatement stmt) {}
	public void visit(DefaultStatement stmt) {}
	public void visit(LabeledStatement stmt) {
		if (stmt instanceof LabelStatement) {
			visit((LabelStatement)stmt);
		} else if (stmt instanceof DefaultStatement) {
			visit((DefaultStatement)stmt);
		} else if (stmt instanceof CaseStatement) {
			visit((CaseStatement)stmt);
		} else {
			throw new RuntimeException("unknown statement: " + stmt.getClass().getName());
		}
	}
	public void visit(LoopStatement stmt) {
		if (stmt instanceof ForStatement) {
			visit((ForStatement)stmt);
		}
		else if (stmt instanceof DoStatement) {
			visit((DoStatement)stmt);
		}
//		else {
//			throw new RuntimeException("unknown statement: " + stmt.getClass().getName());
//	}
	}
	public void visit(ReturnStatement stmt) {}
	public void visit(SwitchStatement stmt) {}

}
