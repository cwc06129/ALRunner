package traverse.general.ALAutomationTraverser;

import java.io.PrintWriter;
import java.util.HashMap;

import ALautomation.InterestVarList;
import syntax.expression.ArrayExpression;
import syntax.expression.AtomicExpression;
import syntax.expression.BinaryExpression;
import syntax.expression.BracketExpression;
import syntax.expression.CallExpression;
import syntax.expression.CastExpression;
import syntax.expression.CompoundExpression;
import syntax.expression.ConditionalExpression;
import syntax.expression.DeclExpression;
import syntax.expression.EmptyExpression;
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
// import udbparser.expr.ASTFromStatement;

public class CodePrintTraverser {
    
	/**
	 * check overlaped visit
	 */
	HashMap<Statement, Boolean> visited = new HashMap<Statement, Boolean>();
	// ASTFromStatement expr = new ASTFromStatement();
	HashMap<Function, Integer> numberOfParam = new HashMap<Function, Integer>();
	int level = 0; //chl@2023.02.14 - tab print
    boolean els = false; //chl@2023.02.14- els check
	boolean cast = false;
	/**
	 * traverse whole statement on a function
	 * @param f
	 */
	public void traverse(Function f, PrintWriter writer, InterestVarList inputvar, InterestVarList statevar, InterestVarList outputvar, String printfString) {
        level = 0;
		if (f.returnLibraryType() == false) {
			writer.print(f.getReturn_type()+" "+f.getName()+"(");
			if (f.getParameters() != null) {
				for (Variable v : f.getParameters()){
					if(f.getParameters().get(f.getParameters().size()-1) == v){
						writer.print(v.getType() + " " + v.getName());
					} else {
						writer.print(v.getType() + " " + v.getName()+", ");
					}
				}
			}
			writer.print(") ");
			writer.println("{");
			level ++;
			if (f.getParameters() == null)
				numberOfParam.put(f, 0);
			else
				numberOfParam.put(f, f.getParameters().size());

			// 2023-05-08(Mon) SoheeJung
			// if INPUT / STATE variable, then make rt_state.{variable} = rt_input.{variable} statement
			if(f.getName().equals("model_step")) {
				for(String s : inputvar.getNameList()) {
					if(statevar.getNameList().contains(s)) {
						writer.println("\trt_state." + s + " = rt_input." + s + ";");
					}
				}
				writer.println("\t" + printfString);
			}
			
			traverse(f.getBody(), writer);
			writer.println("}");
		}
	}
	
	/**
	 * traverse each statement
	 * Note that, some of classes are not listed as it is sufficient to traverse some abstract class
	 * @param f
	 */
	private void traverse(Statement f, PrintWriter writer) {
		if (visited.containsKey(f)) {
			return;
		}
		//writer.println(f);
		
		System.out.println(f.getExpressions());
		
		visited.put(f, true); 
		visit(f, writer);
		//chl@2023.02.14 - continue to last if
		
        if (f instanceof IfStatement){
            traverse((IfStatement)f, writer);
        } else if (f instanceof BranchStatement) { // for, do, if
            traverse((BranchStatement)f, writer);
		} else if (f instanceof CaseStatement) {
			traverse((CaseStatement)f, writer);
		} else if (f instanceof CompoundStatement) {
			traverse((CompoundStatement)f, writer);
		} else if (f instanceof ControlStatement) { // goto, continue, break, exit, return 
			traverse((ControlStatement)f, writer);
		} else if (f instanceof DeclarationStatement) {
            traverse((DeclarationStatement)f, writer);            
		} else if (f instanceof DefaultStatement) {
            traverse((DefaultStatement)f, writer);            
		} else if (f instanceof EmptyStatement) {
			traverse((EmptyStatement)f, writer);
		} else if (f instanceof PseudoStatement) {
			traverse((PseudoStatement)f, writer);
		} else if (f instanceof ExpressionStatement) {
            traverse((ExpressionStatement)f, writer);            
		} else if (f instanceof LabeledStatement) { // label
            traverse((LabeledStatement)f, writer);            
		} else if (f instanceof SwitchStatement) {
			traverse((SwitchStatement)f, writer);            
		} else if (f instanceof NullStatement) {
			return;
		} else {
			throw new RuntimeException("unknown statement: " + f.getClass().getName());
		}
		
		
	}
	
	//chl@2023.02.14 - traverse for printing code
    private void traverse(IfStatement stmt, PrintWriter writer) {
        printlevel(writer);
        if (els == true){
            writer.print("else if");
            els = false;
        }
        else
            writer.print("if");
        print((IfStatement)stmt, writer);
		writer.println("{");
        level ++;
		traverse((Statement)stmt.getThen(), writer);
        level --;
        printlevel(writer);
        writer.println("}"); 
        els = true;
		traverse((Statement)stmt.getEls(), writer);
        els = false;
	}
	private void traverse(BranchStatement stmt, PrintWriter writer) {   
          
		printlevel(writer);
        if (stmt instanceof ForStatement){
            writer.print("for");
            print((ForStatement)stmt, writer);
        } else if (stmt instanceof DoStatement){
            writer.print("do");
		} else if (stmt instanceof LoopStatement){
			writer.print("while");
			print((LoopStatement)stmt, writer);
		}
			
        writer.println("{");
        level ++;  
            
		traverse((Statement)stmt.getThen(), writer);
        els = true;
		traverse((Statement)stmt.getEls(), writer);
        els = false;

		level --;
        printlevel(writer);
        writer.print("}");

        if(stmt instanceof DoStatement){
            writer.print("while");
			printExpression(((DoStatement)stmt).getCondition(), writer);
            writer.print(";");
        }
        writer.println();
	}
	private void traverse(CaseStatement stmt, PrintWriter writer) {
		printlevel(writer);
        writer.print("case ");
        printExpression(stmt.getCondition(), writer);
        writer.println(" :");
        level ++;
		traverse((Statement)stmt.getBody(), writer);
        level --;
	}
	private void traverse(CompoundStatement stmt, PrintWriter writer) {
		if (els == true) {
			printlevel(writer);
			writer.print("else");
			writer.println("{");
			level ++;
		}
		for (Statement s : stmt.getBody()) {
			if (els == true){
				els = false;
				traverse(s, writer);
				els = true;
			}
			else {
				traverse(s, writer);
			}
		}
		if (els == true) {
			level --;
			printlevel(writer);
			writer.println("}");
			els = false;
		}
	}
	private void traverse(ControlStatement stmt, PrintWriter writer) {
        if(stmt instanceof GotoStatement) traverse((GotoStatement)stmt, writer);
		else if(stmt instanceof ReturnStatement) traverse((ReturnStatement)stmt, writer);
        else if(stmt instanceof BreakStatement) traverse((BreakStatement)stmt, writer);
        else if(stmt instanceof ExitStatement) traverse((ExitStatement)stmt, writer);
        else if(stmt instanceof ContinueStatement) traverse((ContinueStatement)stmt, writer);
	}
    public void traverse(GotoStatement stmt, PrintWriter writer) {
        printlevel(writer);
        writer.print("goto " + stmt.getLabel().getName());
        writer.println(";");
    }
    public void traverse(ReturnStatement stmt, PrintWriter writer) {
        printlevel(writer);
        writer.print(stmt.getReturnExpr().getCode());
        writer.println(";");
    }
    public void traverse(BreakStatement stmt, PrintWriter writer) {
        printlevel(writer);
        writer.println("break;");
    }
    public void traverse(ExitStatement stmt, PrintWriter writer) {
        printlevel(writer);
        printExpression(stmt.getExitExpression(), writer);
        writer.println(";");
    }
    public void traverse(ContinueStatement stmt, PrintWriter writer) {
        printlevel(writer);
        printExpression(stmt.getExpression(), writer);
        writer.println(";");
    }
    

	private void traverse(DeclarationStatement stmt, PrintWriter writer) {
		printlevel(writer);
        print((DeclarationStatement)stmt, writer);
		traverse((Statement)stmt.getNext(), writer);
	}
	private void traverse(DefaultStatement stmt, PrintWriter writer) {
        printlevel(writer);
		writer.println("default :");
        level ++;
		traverse((Statement)stmt.getBody(), writer);
        level --;
	}
	private void traverse(EmptyStatement stmt, PrintWriter writer) {
	}
	private void traverse(PseudoStatement stmt, PrintWriter writer) {}
	private void traverse(ExpressionStatement stmt, PrintWriter writer) {
		//printlevel(writer);
        print((ExpressionStatement)stmt, writer);
	}
	private void traverse(LabeledStatement stmt, PrintWriter writer) {
		printlevel(writer);
        writer.println(((LabelStatement)stmt).getLabel().getName() + ":");
		traverse((Statement)stmt.getNext(), writer);
	}
	private void traverse(SwitchStatement stmt, PrintWriter writer) {
		printlevel(writer);
        writer.print("switch");
        writer.print(stmt.getExpression().getCode());
        writer.println("{");
        level++;
		for (CaseStatement c: stmt.getCases()) {
			traverse((Statement)c, writer);
		}
		if(stmt.getDefaultStatement().getStatementId() != 0)
			traverse((Statement)stmt.getDefaultStatement(), writer);
        level--;
        printlevel(writer);
        writer.println("}");
	}
	
	/**
	 * <hook function>
	 * visit current statement
	 * If it is not leaf class on the diagram, it further visit its one-level more concrete class,
	 * such that Statement -> BranchStatement or BranchStatement -> IfStatement
	 * @param f
	 */
	public void visit(Statement f, PrintWriter writer) {
		if (f instanceof BranchStatement) {
			visit((BranchStatement)f, writer);
		} else if (f instanceof CompoundStatement) {
			visit((CompoundStatement)f, writer);
		} else if (f instanceof ControlStatement) {
			visit((ControlStatement)f, writer);
		} else if (f instanceof DeclarationStatement) {
			visit((DeclarationStatement)f, writer);
		} else if (f instanceof EmptyStatement) {
			visit((EmptyStatement)f, writer);
		} else if (f instanceof PseudoStatement) {
			visit((PseudoStatement)f, writer);
		} else if (f instanceof ExpressionStatement) {
			visit((ExpressionStatement)f, writer);
		} else if (f instanceof LabeledStatement) {
			visit((LabeledStatement)f, writer);
		} else if (f instanceof SwitchStatement) {
			visit((SwitchStatement)f, writer);
		} else if (f instanceof NullStatement) {
			return;
		} else {
			writer.println(f);
			throw new RuntimeException("unknown statement: " + f.getClass().getName());
		}
	}
	public void visit(BranchStatement stmt, PrintWriter writer) {
		if (stmt instanceof IfStatement) {
			visit((IfStatement)stmt, writer);
		} else if (stmt instanceof LoopStatement) {
			visit((LoopStatement)stmt, writer);
		} else {
			throw new RuntimeException("unknown statement: " + stmt.getClass().getName());
		}
	}
	public void visit(BreakStatement stmt, PrintWriter writer) {}
	public void visit(CaseStatement stmt, PrintWriter writer) {}
	public void visit(CompoundStatement stmt, PrintWriter writer) {}
	public void visit(ContinueStatement stmt, PrintWriter writer) {}
	public void visit(ControlStatement stmt, PrintWriter writer) {
		if (stmt instanceof GotoStatement) {
			visit((GotoStatement)stmt, writer);
		} else if (stmt instanceof ReturnStatement) {
			visit((ReturnStatement)stmt, writer);
		} else if (stmt instanceof ContinueStatement) {
			visit((ContinueStatement)stmt, writer);
		} else if (stmt instanceof BreakStatement) {
			visit((BreakStatement)stmt, writer);
		}
		else if (stmt instanceof ExitStatement) {
			visit((ExitStatement)stmt, writer);
		}
		else {
			throw new RuntimeException("unknown statement: " + stmt.getClass().getName());
		}
	}
	public void visit(DeclarationStatement stmt, PrintWriter writer) {}
	public void visit(DoStatement stmt, PrintWriter writer) {}
	public void visit(EmptyStatement stmt, PrintWriter writer) {}
	public void visit(PseudoStatement stmt, PrintWriter writer) {}
	public void visit(ExitStatement stmt, PrintWriter writer) {}
	public void visit(ExpressionStatement stmt, PrintWriter writer) {}
	public void visit(ForStatement stmt, PrintWriter writer) {}
	public void visit(GotoStatement stmt, PrintWriter writer) {}
	public void visit(IfStatement stmt, PrintWriter writer) {}
	// LabelStatement�� leafNode�� �ǵ��� �ش� visit ���� ������ ��.
	// 20210827
	public void visit(LabelStatement stmt, PrintWriter writer) {}
	public void visit(DefaultStatement stmt, PrintWriter writer) {}
	public void visit(LabeledStatement stmt, PrintWriter writer) {
		if (stmt instanceof LabelStatement) {
			visit((LabelStatement)stmt, writer);
		} else if (stmt instanceof DefaultStatement) {
			visit((DefaultStatement)stmt, writer);
		} else if (stmt instanceof CaseStatement) {
			visit((CaseStatement)stmt, writer);
		} else {
			throw new RuntimeException("unknown statement: " + stmt.getClass().getName());
		}
	}
	public void visit(LoopStatement stmt, PrintWriter writer) {
		if (stmt instanceof ForStatement) {
			visit((ForStatement)stmt, writer);
		}
		else if (stmt instanceof DoStatement) {
			visit((DoStatement)stmt, writer);
		}
//		else {
//			throw new RuntimeException("unknown statement: " + stmt.getClass().getName());
//	}
	}
	public void visit(ReturnStatement stmt, PrintWriter writer) {}
	public void visit(SwitchStatement stmt, PrintWriter writer) {}
    public void printlevel(PrintWriter writer){
        for (int i=0 ; i<level ; i++){
            writer.print("\t");
        }
    }

    /*chl@2023.02.14 - print expression from statement */
    public void print(ForStatement stmt, PrintWriter writer){
        writer.print("(");
        printExpression(stmt.getInitExpression(), writer);
        writer.print(" ; ");
        printExpression(stmt.getCondition(), writer);
        writer.print(" ; ");
        printExpression(stmt.getIterExpression(), writer);
        writer.print(")");
    }

	public void print(LoopStatement stmt, PrintWriter writer){
        printExpression(stmt.getCondition(), writer);
    }

    public void print(DeclarationStatement stmt, PrintWriter writer){
		if (els == true) {
			printlevel(writer);
			writer.print("else");
			writer.println("{");
			level ++;
		}


		//multi decl line
		else if (stmt.getDeclExpression().size() > 1){
			for (Expression e : stmt.getDeclExpression()){
				if (e != stmt.getDeclExpression().get(0)) printlevel(writer);
				printExpression(e, writer);
				writer.println(";");
			}
		}
		else {
			for (Expression e : stmt.getDeclExpression()){
				//chl@2023.02.20 - temporary array exception (declaration without pointer)
				if (e instanceof DeclExpression && ((DeclExpression)e).getBinaryExpression().getLhsOperand() instanceof ArrayExpression){
					writer.print(((DeclExpression)e).getType().toString().replace("*", "")+ " " +((DeclExpression)e).getBinaryExpression().getCode());
				} 
				else {
					printExpression(e, writer);
					if(stmt.getDeclExpression().get(stmt.getDeclExpression().size()-1) != e){
						writer.print(" ");
					}
				}
			}
			writer.println(";");
		}

		if (els == true) {
			//else 구문 
			level --;
			printlevel(writer);
			writer.println("}");
			els = false;
		}
    }

    public void print(ExpressionStatement stmt, PrintWriter writer){
		if (els == true) {
			printlevel(writer);
			writer.print("else");
			writer.println("{");
			level ++;
		}		
		
		printlevel(writer);
		// 2023-05-04(Thu) SoheeJung
		// if ExpressionStatement has fixed_expression that is not null, then printExprsesion getFixed_expresssion.
		// if ExpressionStatement has fixed_expression that is null, then printExpression getExpression.
		if(stmt.getFixed_expression() != null) printExpression(stmt.getFixed_expression(), writer);
		else printExpression(stmt.getExpression(), writer);
        writer.println(";");

		if (els == true) {
			//else 구문 
			level --;
			printlevel(writer);
			writer.println("}");
			els = false;
		}
    }

    public void print(IfStatement stmt, PrintWriter writer){
        printExpression(stmt.getCondition(), writer);
    }

    /*chl@2023.02.14 - print expression */
    public void printExpression(Expression f, PrintWriter writer){
		if (f instanceof TypedefExpression){
			writer.print(((TypedefExpression)f).getCode());
			for (Expression e : ((TypedefExpression)f).getExpressions()){
				if (e instanceof BracketExpression && ((BracketExpression)e).getPosition()){
					writer.println(e.getCode());
					level ++;
				} else if (e instanceof BracketExpression) {
					level --;
					printlevel(writer);
					writer.print(e.getCode());					
				} else {
					printlevel(writer);
					writer.println(e.getCode()+";");
				}
			}
			if (((TypedefExpression)f).getNick() != null)
				writer.print(((TypedefExpression)f).getNick());
		}
        else if (f instanceof ConditionalExpression) {
			writer.print(((ConditionalExpression)f).getCode());
		} 
		else if (f instanceof ArrayExpression) {
			writer.print(((ArrayExpression)f).getCode());
		}
		else if (f instanceof UnaryExpression) {
			writer.print(((UnaryExpression)f).getCode());
		}
		else if (f instanceof BinaryExpression) {
			writer.print(((BinaryExpression)f).getCode());
		}
		else if (f instanceof CastExpression) {
			writer.print(((CastExpression)f).getCode());
		}
		else if (f instanceof DeclExpression) {
			writer.print(((DeclExpression)f).getCode());
		}
		else if (f instanceof UserDefinedCallExpression) {
			writer.print(((UserDefinedCallExpression)f).getCode());
		}
        else if (f instanceof CallExpression){
            writer.print(((CallExpression)f).getCode());
        }
		else if (f instanceof LibraryCallExpression) {
			writer.print(((LibraryCallExpression)f).getCode());
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
		// 2023-05-04(Thu) SoheeJung
		// we use EmptyExpression that present rt_input with librarycall expression, so we consider this case in traverse.
		else if (f instanceof EmptyExpression) {
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
