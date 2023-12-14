package traverse.general;

import java.util.HashMap;

import syntax.expression.ArrayExpression;
import syntax.expression.AtomicExpression;
import syntax.expression.BinaryExpression;
import syntax.expression.BracketExpression;
import syntax.expression.CallExpression;
import syntax.expression.CastExpression;
import syntax.expression.CompoundExpression;
import syntax.expression.ConditionalExpression;
import syntax.expression.DeclExpression;
import syntax.expression.Expression;
import syntax.expression.LibraryCallExpression;
import syntax.expression.TypedefExpression;
import syntax.expression.UnaryExpression;
import syntax.expression.UserDefinedCallExpression;
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
import syntax.variable.Variable;

public class CodeFromStmtTraverser {
    
	/**
	 * check overlaped visit
	 */
	HashMap<Statement, Boolean> visited = new HashMap<Statement, Boolean>();
	//ASTFromStatement expr = new ASTFromStatement();
	HashMap<Function, Integer> numberOfParam = new HashMap<Function, Integer>();
	int level = 0; //chl@2023.02.14 - tab print
    boolean els = false; //chl@2023.02.14- els check
	boolean cast = false;
	/**
	 * traverse whole statement on a function
	 * @param f
	 */
	public void traverse(Function f) {
        level = 0;
		if (f.returnLibraryType() == false) {
			System.out.print(f.getReturn_type()+" "+f.getName()+"(");
			if (f.getParameters() != null) {
				for (Variable v : f.getParameters()){
					if(f.getParameters().get(f.getParameters().size()-1) == v){
						System.out.print(v.getType() + " " + v.getName());
					} else {
						System.out.print(v.getType() + " " + v.getName()+", ");
					}
				}
			}
			System.out.print(") ");
			System.out.println("{");
			level ++;
			if (f.getParameters() == null)
				numberOfParam.put(f, 0);
			else
				numberOfParam.put(f, f.getParameters().size());
			
			traverse(f.getBody());
			System.out.println("}");
		}
	}
	
	/**
	 * traverse each statement
	 * Note that, some of classes are not listed as it is sufficient to traverse some abstract class
	 * @param f
	 */
	public void traverse(Statement f) {
		if (visited.containsKey(f)) {
			return;
		}
		//System.out.println(f);

		visited.put(f, true); 
		visit(f);
		//chl@2023.02.14 - continue to last if
		
        if (f instanceof IfStatement){
            traverse((IfStatement)f);
        } else if (f instanceof BranchStatement) { // for, do, if
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
	
	//chl@2023.02.14 - traverse for printing code
    private void traverse(IfStatement stmt) {
        printlevel();
        if (els == true){
            System.out.print("else if");
            els = false;
        }
        else
            System.out.print("if");
        print((IfStatement)stmt);
		System.out.println("{");
        level ++;
		traverse((Statement)stmt.getThen());
        level --;
        printlevel();
        System.out.println("}"); 
        els = true;
		traverse((Statement)stmt.getEls());
        els = false;
	}
	private void traverse(BranchStatement stmt) {   
          
		printlevel();
        if (stmt instanceof ForStatement){
            System.out.print("for");
            print((ForStatement)stmt);
        } else if (stmt instanceof DoStatement){
            System.out.print("do");
		} else if (stmt instanceof LoopStatement){
			System.out.print("while");
			print((LoopStatement)stmt);
		}
			
        System.out.println("{");
        level ++;  
            
		traverse((Statement)stmt.getThen());
        els = true;
		traverse((Statement)stmt.getEls());
        els = false;

		level --;
        printlevel();
        System.out.print("}");

        if(stmt instanceof DoStatement){
            System.out.print("while");
			printExpression(((DoStatement)stmt).getCondition());
            System.out.print(";");
        }
        System.out.println();
	}
	private void traverse(CaseStatement stmt) {
		printlevel();
        System.out.print("case ");
        printExpression(stmt.getCondition());
        System.out.println(" :");
        level ++;
		traverse((Statement)stmt.getBody());
        level --;
	}
	private void traverse(CompoundStatement stmt) {
		if (els == true) {
			printlevel();
			System.out.print("else");
			System.out.println("{");
			level ++;
		}
		for (Statement s : stmt.getBody()) {
			if (els == true){
				els = false;
				traverse(s);
				els = true;
			}
			else {
				traverse(s);
			}
		}
		if (els == true) {
			level --;
			printlevel();
			System.out.println("}");
			els = false;
		}
	}
	private void traverse(ControlStatement stmt) {
        if(stmt instanceof GotoStatement) traverse((GotoStatement)stmt);
		else if(stmt instanceof ReturnStatement) traverse((ReturnStatement)stmt);
        else if(stmt instanceof BreakStatement) traverse((BreakStatement)stmt);
        else if(stmt instanceof ExitStatement) traverse((ExitStatement)stmt);
        else if(stmt instanceof ContinueStatement) traverse((ContinueStatement)stmt);
	}
    public void traverse(GotoStatement stmt) {
        printlevel();
        System.out.print("goto " + stmt.getLabel().getName());
        System.out.println(";");
    }
    public void traverse(ReturnStatement stmt) {
        printlevel();
        System.out.print(stmt.getReturnExpr().getCode());
        System.out.println(";");
    }
    public void traverse(BreakStatement stmt) {
        printlevel();
        System.out.println("break;");
    }
    public void traverse(ExitStatement stmt) {
        printlevel();
        printExpression(stmt.getExitExpression());
        System.out.println(";");
    }
    public void traverse(ContinueStatement stmt) {
        printlevel();
        printExpression(stmt.getExpression());
        System.out.println(";");
    }
    

	private void traverse(DeclarationStatement stmt) {
		printlevel();
        print((DeclarationStatement)stmt);
		if(stmt.getNext() != null) traverse((Statement)stmt.getNext());
	}
	private void traverse(DefaultStatement stmt) {
        printlevel();
		System.out.println("default :");
        level ++;
		traverse((Statement)stmt.getBody());
        level --;
	}
	private void traverse(EmptyStatement stmt) {
	}
	private void traverse(PseudoStatement stmt) {}
	private void traverse(ExpressionStatement stmt) {
		//printlevel();
        print((ExpressionStatement)stmt);
	}
	private void traverse(LabeledStatement stmt) {
		printlevel();
        System.out.println(((LabelStatement)stmt).getLabel().getName() + ":");
		traverse((Statement)stmt.getNext());
	}
	private void traverse(SwitchStatement stmt) {
		printlevel();
        System.out.print("switch");
        System.out.print(stmt.getExpression().getCode());
        System.out.println("{");
        level++;
		for (CaseStatement c: stmt.getCases()) {
			traverse((Statement)c);
		}
		if(stmt.getDefaultStatement().getStatementId() != 0)
			traverse((Statement)stmt.getDefaultStatement());
        level--;
        printlevel();
        System.out.println("}");
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
    public void printlevel(){
        for (int i=0 ; i<level ; i++){
            System.out.print("\t");
        }
    }

    /*chl@2023.02.14 - print expression from statement */
    public void print(ForStatement stmt){
        System.out.print("(");
        printExpression(stmt.getInitExpression());
        System.out.print(" ; ");
        printExpression(stmt.getCondition());
        System.out.print(" ; ");
        printExpression(stmt.getIterExpression());
        System.out.print(")");
    }

	public void print(LoopStatement stmt){
        printExpression(stmt.getCondition());
    }

    public void print(DeclarationStatement stmt){
		if (els == true) {
			printlevel();
			System.out.print("else");
			System.out.println("{");
			level ++;
		}


		//multi decl line
		else if (stmt.getDeclExpression().size() > 1){
			for (Expression e : stmt.getDeclExpression()){
				if (e != stmt.getDeclExpression().get(0)) printlevel();
				printExpression(e);
				System.out.println(";");
			}
		}
		else {
			for (Expression e : stmt.getDeclExpression()){
				//chl@2023.02.20 - temporary array exception (declaration without pointer)
				if (e instanceof DeclExpression && ((DeclExpression)e).getBinaryExpression().getLhsOperand() instanceof ArrayExpression){
					System.out.print(((DeclExpression)e).getType().toString().replace("*", "")+ " " +((DeclExpression)e).getBinaryExpression().getCode());
				} 
				else {
					printExpression(e);
					if(stmt.getDeclExpression().get(stmt.getDeclExpression().size()-1) != e){
						System.out.print(" ");
					}
				}
			}
			System.out.println(";");
		}

		if (els == true) {
			//else 구문 
			level --;
			printlevel();
			System.out.println("}");
			els = false;
		}
    }

    public void print(ExpressionStatement stmt){
		if (els == true) {
			printlevel();
			System.out.print("else");
			System.out.println("{");
			level ++;
		}		
		
		printlevel();
        printExpression(stmt.getExpression());
        System.out.println(";");

		if (els == true) {
			//else 구문 
			level --;
			printlevel();
			System.out.println("}");
			els = false;
		}
    }

    public void print(IfStatement stmt){
        printExpression(stmt.getCondition());
    }

    /*chl@2023.02.14 - print expression */
    public void printExpression(Expression f){
		if (f instanceof TypedefExpression){
			System.out.print(((TypedefExpression)f).getCode());
			if (((TypedefExpression)f).getExpressions().isEmpty()){
				if (((TypedefExpression)f).getNick() != null)
					System.out.print(((TypedefExpression)f).getNick());
			}
			else {
				for (Expression e : ((TypedefExpression)f).getExpressions()){
					if (e instanceof BracketExpression && ((BracketExpression)e).getPosition()){
						System.out.println(e.getCode());
						level ++;
					} else if (e instanceof BracketExpression) {
						level --;
						printlevel();
						System.out.print(e.getCode());					
					} else {
						printlevel();
						if(((TypedefExpression)f).getType().equals("enum")) System.out.print(e.getCode());
						else System.out.println(e.getCode()+";");
					}
				}
				if (((TypedefExpression)f).getNick() != null)
					System.out.print(" " + ((TypedefExpression)f).getNick());
			}
		}
        else if (f instanceof ConditionalExpression) {
			System.out.print(((ConditionalExpression)f).getCode());
		} 
		else if (f instanceof ArrayExpression) {
			System.out.print(((ArrayExpression)f).getCode());
		}
		else if (f instanceof UnaryExpression) {
			System.out.print(((UnaryExpression)f).getCode());
		}
		else if (f instanceof BinaryExpression) {
			System.out.print(((BinaryExpression)f).getCode());
		}
		else if (f instanceof CastExpression) {
			System.out.print(((CastExpression)f).getCode());
		}
		else if (f instanceof DeclExpression) {
			System.out.print(((DeclExpression)f).getCode());
		}
		else if (f instanceof UserDefinedCallExpression) {
			System.out.print(((UserDefinedCallExpression)f).getCode());
		}
        else if (f instanceof CallExpression){
            System.out.print(((CallExpression)f).getCode());
        }
		else if (f instanceof LibraryCallExpression) {
			System.out.print(((LibraryCallExpression)f).getCode());
            /*for (Expression e : ((CallExpression)f).getActualParameters()){
                printExpression(e);
            }*/
		}
		else if(f instanceof AtomicExpression) {
			//empty body : do not need to traverse
		} 
		else if (f instanceof CompoundExpression){//chl@2023.02.13 - compoundExpression
			//for (Expression e : ((CompoundExpression)f).getExpression()){
			//	traverse(e);
			//}
		}
		else {
			throw new RuntimeException("unknown expression: " + f.getClass().getName());
		}
		/*chl@2023.02.09 - nested code*/
		//if (f.getNext() != null) {
		//	traverse(f.getNext());
		//}
    }
}
