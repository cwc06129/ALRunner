package traverse.general.ALAutomationTraverser;

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
import syntax.expression.IdExpression;
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
// import udbparser.expr.ASTFromStatement;

/**
 * @date 2023-05-04(Thu)
 * @author SoheeJung
 * <IdChangeTraverser Policy>
 * (1) Variables defined only for rt_input: delete a statement for a statement in which the rt_input and scanf libraries are called
 * (2) Variables defined only in rt_state / rt_output : variable name only modified
 * (3) Variables defined at the same time as rt_input and rt_state -> Processing in 'CodePrintTraverser'
 *     (3)-1. Add a statement rt_state.{variable} = rt_input.{variable} at the beginning of the model_step
 *     (3)-2. For statements in which the rand and scanf libraries are called, delete those statements
 * (4) variables defined at the same time as rt_state and rt_output : changing variables only to rt_state
 */
public class IdChangeTraverser {
	/**
	 * check overlaped visit
	 */
	HashMap<Statement, Boolean> visited = new HashMap<Statement, Boolean>();
	// ASTFromStatement expr = new ASTFromStatement();
	HashMap<Function, Integer> numberOfParam = new HashMap<Function, Integer>();
	int level = 0; //chl@2023.02.14 - tab print
    boolean els = false; //chl@2023.02.14- els check
	boolean cast = false;
	// 2023-03-28(Tue) SoheeJung : collect all the idExpression
	// ArrayList<IdExpression> idExpressionList = new ArrayList<IdExpression>();
	// 2023-05-04(Thu) SoheeJung : check library call with rt_input varible in ExpressionStatement
	boolean libraryInputCheck = false;

	/**
	 * traverse whole statement on a function
	 * @param f
	 */
	public void traverse(Function f, InterestVarList inputvar, InterestVarList statevar, InterestVarList outputvar) {
        level = 0;
		if (f.returnLibraryType() == false) {
			if (f.getParameters() == null)
				numberOfParam.put(f, 0);
			else
				numberOfParam.put(f, f.getParameters().size());
			
			traverse(f.getBody(), inputvar, statevar, outputvar);
		}
	}
	
	/**
	 * traverse each statement
	 * Note that, some of classes are not listed as it is sufficient to traverse some abstract class
	 * @param f
	 */
	private void traverse(Statement f, InterestVarList inputvar, InterestVarList statevar, InterestVarList outputvar) {
		if (visited.containsKey(f)) {
			return;
		}
		//System.out.println(f);

		visited.put(f, true); 
		visit(f);
		//chl@2023.02.14 - continue to last if
		
        if (f instanceof IfStatement){
            traverse((IfStatement)f, inputvar, statevar, outputvar);
        } else if (f instanceof BranchStatement) { // for, do, if
            traverse((BranchStatement)f, inputvar, statevar, outputvar);
		} else if (f instanceof CaseStatement) {
			traverse((CaseStatement)f, inputvar, statevar, outputvar);
		} else if (f instanceof CompoundStatement) {
			traverse((CompoundStatement)f, inputvar, statevar, outputvar);
		} else if (f instanceof ControlStatement) { // goto, continue, break, exit, return 
			traverse((ControlStatement)f, inputvar, statevar, outputvar);
		} else if (f instanceof DeclarationStatement) {
            traverse((DeclarationStatement)f, inputvar, statevar, outputvar);            
		} else if (f instanceof DefaultStatement) {
            traverse((DefaultStatement)f, inputvar, statevar, outputvar);            
		} else if (f instanceof EmptyStatement) {
			traverse((EmptyStatement)f, inputvar, statevar, outputvar);
		} else if (f instanceof PseudoStatement) {
			traverse((PseudoStatement)f, inputvar, statevar, outputvar);
		} else if (f instanceof ExpressionStatement) {
            traverse((ExpressionStatement)f, inputvar, statevar, outputvar);            
		} else if (f instanceof LabeledStatement) { // label
            traverse((LabeledStatement)f, inputvar, statevar, outputvar);            
		} else if (f instanceof SwitchStatement) {
			traverse((SwitchStatement)f, inputvar, statevar, outputvar);            
		} else if (f instanceof NullStatement) {
			return;
		} else {
			throw new RuntimeException("unknown statement: " + f.getClass().getName());
		}	
	}
	
	//chl@2023.02.14 - traverse for printing code
    private void traverse(IfStatement stmt, InterestVarList inputvar, InterestVarList statevar, InterestVarList outputvar) {
		traverseExprFromStatement((IfStatement)stmt, inputvar, statevar, outputvar);
		traverse((Statement)stmt.getThen(), inputvar, statevar, outputvar);
		traverse((Statement)stmt.getEls(), inputvar, statevar, outputvar);
	}
	private void traverse(BranchStatement stmt, InterestVarList inputvar, InterestVarList statevar, InterestVarList outputvar) {   
        if (stmt instanceof ForStatement){
            traverseExprFromStatement((ForStatement)stmt, inputvar, statevar, outputvar);
        } else if (stmt instanceof DoStatement){
		} else if (stmt instanceof LoopStatement){
			traverseExprFromStatement((LoopStatement)stmt, inputvar, statevar, outputvar);
		}
            
		traverse((Statement)stmt.getThen(), inputvar, statevar, outputvar);
		traverse((Statement)stmt.getEls(), inputvar, statevar, outputvar);

        if(stmt instanceof DoStatement){
			traverseExprStateOutput(((DoStatement)stmt).getCondition(), inputvar, statevar, outputvar);
        }
	}
	private void traverse(CaseStatement stmt, InterestVarList inputvar, InterestVarList statevar, InterestVarList outputvar) {
        traverseExprStateOutput(stmt.getCondition(), inputvar, statevar, outputvar);
		traverse((Statement)stmt.getBody(), inputvar, statevar, outputvar);
	}
	private void traverse(CompoundStatement stmt, InterestVarList inputvar, InterestVarList statevar, InterestVarList outputvar) {
		for (Statement s : stmt.getBody()) {
			traverse(s, inputvar, statevar, outputvar);
		}
	}
	private void traverse(ControlStatement stmt, InterestVarList inputvar, InterestVarList statevar, InterestVarList outputvar) {
        if(stmt instanceof GotoStatement) traverse((GotoStatement)stmt, inputvar, statevar, outputvar);
		else if(stmt instanceof ReturnStatement) traverse((ReturnStatement)stmt, inputvar, statevar, outputvar);
        else if(stmt instanceof BreakStatement) traverse((BreakStatement)stmt, inputvar, statevar, outputvar);
        else if(stmt instanceof ExitStatement) traverse((ExitStatement)stmt, inputvar, statevar, outputvar);
        else if(stmt instanceof ContinueStatement) traverse((ContinueStatement)stmt, inputvar, statevar, outputvar);
	}
    public void traverse(GotoStatement stmt, InterestVarList inputvar, InterestVarList statevar, InterestVarList outputvar) {
    }
    public void traverse(ReturnStatement stmt, InterestVarList inputvar, InterestVarList statevar, InterestVarList outputvar) {
        traverseExprStateOutput(stmt.getReturnExpr(), inputvar, statevar, outputvar);
    }
    public void traverse(BreakStatement stmt, InterestVarList inputvar, InterestVarList statevar, InterestVarList outputvar) {
    }
    public void traverse(ExitStatement stmt, InterestVarList inputvar, InterestVarList statevar, InterestVarList outputvar) {
        traverseExprStateOutput(stmt.getExitExpression(), inputvar, statevar, outputvar);
    }
    public void traverse(ContinueStatement stmt, InterestVarList inputvar, InterestVarList statevar, InterestVarList outputvar) {
        traverseExprStateOutput(stmt.getExpression(), inputvar, statevar, outputvar);
    }
	private void traverse(DeclarationStatement stmt, InterestVarList inputvar, InterestVarList statevar, InterestVarList outputvar) {
        traverseExprFromStatement((DeclarationStatement)stmt, inputvar, statevar, outputvar);
        if(stmt.getNext() != null) traverse((Statement)stmt.getNext(), inputvar, statevar, outputvar);
	}
	private void traverse(DefaultStatement stmt, InterestVarList inputvar, InterestVarList statevar, InterestVarList outputvar) {
		traverse((Statement)stmt.getBody(), inputvar, statevar, outputvar);
	}
	private void traverse(EmptyStatement stmt, InterestVarList inputvar, InterestVarList statevar, InterestVarList outputvar) {
	}
	private void traverse(PseudoStatement stmt, InterestVarList inputvar, InterestVarList statevar, InterestVarList outputvar) {}
	private void traverse(ExpressionStatement stmt, InterestVarList inputvar, InterestVarList statevar, InterestVarList outputvar) {
		traverseExprFromStatement((ExpressionStatement)stmt, inputvar, statevar, outputvar);
	}
	private void traverse(LabeledStatement stmt, InterestVarList inputvar, InterestVarList statevar, InterestVarList outputvar) {
		traverse((Statement)stmt.getNext(), inputvar, statevar, outputvar);
	}
	private void traverse(SwitchStatement stmt, InterestVarList inputvar, InterestVarList statevar, InterestVarList outputvar) {
        traverseExprStateOutput(stmt.getExpression(), inputvar, statevar, outputvar);
		for (CaseStatement c: stmt.getCases()) {
			traverse((Statement)c, inputvar, statevar, outputvar);
		}
		if(stmt.getDefaultStatement().getStatementId() != 0)
			traverse((Statement)stmt.getDefaultStatement(), inputvar, statevar, outputvar);
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
			// System.out.println(f);
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
    public void traverseExprFromStatement(ForStatement stmt, InterestVarList inputvar, InterestVarList statevar, InterestVarList outputvar){
        traverseExprStateOutput(stmt.getInitExpression(), inputvar, statevar, outputvar);
        traverseExprStateOutput(stmt.getCondition(), inputvar, statevar, outputvar);
        traverseExprStateOutput(stmt.getIterExpression(), inputvar, statevar, outputvar);
    }

	public void traverseExprFromStatement(LoopStatement stmt, InterestVarList inputvar, InterestVarList statevar, InterestVarList outputvar){
        traverseExprStateOutput(stmt.getCondition(), inputvar, statevar, outputvar);
    }

    public void traverseExprFromStatement(DeclarationStatement stmt, InterestVarList inputvar, InterestVarList statevar, InterestVarList outputvar){
		if (stmt.getDeclExpression().size() > 1){
			for (Expression e : stmt.getDeclExpression()){
				traverseExprStateOutput(e, inputvar, statevar, outputvar);
			}
		}
		else {
			for (Expression e : stmt.getDeclExpression()){
				//chl@2023.02.20 - temporary array exception (declaration without pointer)
				if (e instanceof DeclExpression && ((DeclExpression)e).getBinaryExpression().getLhsOperand() instanceof ArrayExpression){
					traverseExprStateOutput(((DeclExpression)e).getBinaryExpression(), inputvar, statevar, outputvar);
				} 
				else {
					traverseExprStateOutput(e, inputvar, statevar, outputvar);
				}
			}
		}
    }
    

	/**
	 * @date 2023-04-09(Sun)
	 * @author SoheeJung
	 * @name traverseExprFromStatement
	 * if traverse target is ExpressionStatement, then find library call in this statement. Finally change variable to interest variable form.
	 */
    public void traverseExprFromStatement(ExpressionStatement stmt, InterestVarList inputvar, InterestVarList statevar, InterestVarList outputvar){
		// (1) if ExpressionStatement have scanf and rand library call,
		// (2) and if ExpressionStatement have INPUT interest variable, 
		// then set fixed_name -> rt_input.{var_name}
		traverseLibraryCall(stmt, stmt.getExpression());
		if(stmt.isHaveLibraryCall()) {
			traverseExprInput(stmt.getExpression(), inputvar, statevar, outputvar);
			// 2023-05-04(Thu) SoheeJung
			// Using traverse, if ExpressionStatement has IdExpression that has fixed_name, then return true.
			traverseExprHasFixedName(stmt.getExpression(), inputvar, statevar, outputvar);
			// if Expression statement has rt_input variable and librarycall, then set fixed expression to EmptyExpression.
			if(libraryInputCheck) stmt.setFixed_expression(new EmptyExpression());
			libraryInputCheck = false;
		}
		else {
			traverseExprStateOutput(stmt.getExpression(), inputvar, statevar, outputvar);
		}
    }

	public void traverseExprHasFixedName(Expression f, InterestVarList inputvar, InterestVarList statevar, InterestVarList outputvar) {
		if (f instanceof IdExpression) {
			for(String s : inputvar.getNameList()) {
				if(((IdExpression)f).getName().equals(s)) {
					if(((IdExpression)f).getFixed_name() != null) libraryInputCheck = true;
				}
			}
		}
		else if (f instanceof TypedefExpression) {
			for (Expression e : ((TypedefExpression)f).getExpressions()) {
				if (e instanceof BracketExpression && ((BracketExpression)e).getPosition()) {
					traverseExprHasFixedName(e, inputvar, statevar, outputvar);
				} else if (e instanceof BracketExpression) {
					traverseExprHasFixedName(e, inputvar, statevar, outputvar);
				} else {
					traverseExprHasFixedName(e, inputvar, statevar, outputvar);
				}
			} 
		}
        else if (f instanceof ConditionalExpression) {
			traverseExprHasFixedName(((ConditionalExpression)f).getCondition(), inputvar, statevar, outputvar);
		} 
		else if (f instanceof ArrayExpression) {
			traverseExprHasFixedName(((ArrayExpression)f).getAtomic(), inputvar, statevar, outputvar);
			for (Expression e : ((ArrayExpression)f).getIndexExpression()) {
				traverseExprHasFixedName(e, inputvar, statevar, outputvar);
			}
		}
		else if (f instanceof UnaryExpression) {
			traverseExprHasFixedName(((UnaryExpression)f).getOperand(), inputvar, statevar, outputvar);
		}
		else if (f instanceof BinaryExpression) {
			traverseExprHasFixedName(((BinaryExpression)f).getLhsOperand(), inputvar, statevar, outputvar);
			traverseExprHasFixedName(((BinaryExpression)f).getRhsOperand(), inputvar, statevar, outputvar);
		}
		else if (f instanceof CastExpression) {
			traverseExprHasFixedName(((CastExpression)f).getCast(), inputvar, statevar, outputvar);
		}
		else if (f instanceof DeclExpression) {
			traverseExprHasFixedName(((DeclExpression)f).getBinaryExpression(), inputvar, statevar, outputvar);
			traverseExprHasFixedName(((DeclExpression)f).getIdExpression(), inputvar, statevar, outputvar);
		}
		else if (f instanceof UserDefinedCallExpression) {
		}
        else if (f instanceof CallExpression){
			for(Expression e : ((CallExpression)f).getActualParameters()) {
				traverseExprHasFixedName(e, inputvar, statevar, outputvar);
			}
        }
		else if (f instanceof LibraryCallExpression) {
		}
		else if(f instanceof AtomicExpression) {
		} 
		else if (f instanceof CompoundExpression){//chl@2023.02.13 - compoundExpression
			for(Expression e : ((CompoundExpression)f).getExpression()) {
				traverseExprHasFixedName(e, inputvar, statevar, outputvar);
			}
		} else if (f instanceof BracketExpression) {}
		// 2023-05-02(Tue) SoheeJung
		// Another expression don't mind.
		else {}
	}

	/**
	 * @date 2023-04-09(Sun)
	 * @author SoheeJung
	 * @name traverseExprInput
	 * fix variable -> rt_input.{variable}
	 */
	public void traverseExprInput(Expression f, InterestVarList inputvar, InterestVarList statevar, InterestVarList outputvar) {
		if (f instanceof IdExpression) {
			for(String s : inputvar.getNameList()) {
				if(((IdExpression)f).getName().equals(s)) {
					((IdExpression)f).setFixed_name("rt_input." + s);
				}
			}
			// getIdExpressionList().add((IdExpression)f);
		}
		else if (f instanceof TypedefExpression) {
			for (Expression e : ((TypedefExpression)f).getExpressions()) {
				if (e instanceof BracketExpression && ((BracketExpression)e).getPosition()) {
					traverseExprInput(e, inputvar, statevar, outputvar);
				} else if (e instanceof BracketExpression) {
					traverseExprInput(e, inputvar, statevar, outputvar);
				} else {
					traverseExprInput(e, inputvar, statevar, outputvar);
				}
			} 
		}
        else if (f instanceof ConditionalExpression) {
			traverseExprInput(((ConditionalExpression)f).getCondition(), inputvar, statevar, outputvar);
		} 
		else if (f instanceof ArrayExpression) {
			traverseExprInput(((ArrayExpression)f).getAtomic(), inputvar, statevar, outputvar);
			for (Expression e : ((ArrayExpression)f).getIndexExpression()) {
				traverseExprInput(e, inputvar, statevar, outputvar);
			}
		}
		else if (f instanceof UnaryExpression) {
			traverseExprInput(((UnaryExpression)f).getOperand(), inputvar, statevar, outputvar);
		}
		else if (f instanceof BinaryExpression) {
			traverseExprInput(((BinaryExpression)f).getLhsOperand(), inputvar, statevar, outputvar);
			traverseExprInput(((BinaryExpression)f).getRhsOperand(), inputvar, statevar, outputvar);
		}
		else if (f instanceof CastExpression) {
			traverseExprInput(((CastExpression)f).getCast(), inputvar, statevar, outputvar);
		}
		else if (f instanceof DeclExpression) {
			traverseExprInput(((DeclExpression)f).getBinaryExpression(), inputvar, statevar, outputvar);
			traverseExprInput(((DeclExpression)f).getIdExpression(), inputvar, statevar, outputvar);
		}
		else if (f instanceof UserDefinedCallExpression) {
			// 2023-05-10(Wed) SoheeJung
			// If expression's type is UserDefinedCallExpression, then get parameter from actualParameters.
			for(Expression e : ((CallExpression)f).getActualParameters()) {
				traverseExprInput(e, inputvar, statevar, outputvar);
			}
		}
        else if (f instanceof CallExpression){
			for(Expression e : ((CallExpression)f).getActualParameters()) {
				traverseExprInput(e, inputvar, statevar, outputvar);
			}
        }
		else if (f instanceof LibraryCallExpression) {
		}
		else if(f instanceof AtomicExpression) {
		} 
		else if (f instanceof CompoundExpression){//chl@2023.02.13 - compoundExpression
			for(Expression e : ((CompoundExpression)f).getExpression()) {
				traverseExprInput(e, inputvar, statevar, outputvar);
			}
		} else if (f instanceof BracketExpression) {}
		// 2023-05-02(Tue) SoheeJung
		// Another expression don't mind.
		else {}
	}

	/**
	 * @date 2023-04-09(Sun)
	 * @author SoheeJung
	 * @name traverseLibraryCall
	 * find scanf & rand library call
	 */
    public void traverseLibraryCall(ExpressionStatement stmt, Expression f) {
		if (f instanceof IdExpression) {
		}
		else if (f instanceof TypedefExpression){
		}
        else if (f instanceof ConditionalExpression) {
			traverseLibraryCall(stmt, ((ConditionalExpression)f).getCondition());
		} 
		else if (f instanceof ArrayExpression) {
			traverseLibraryCall(stmt, ((ArrayExpression)f).getAtomic());
			for (Expression e : ((ArrayExpression)f).getIndexExpression()) {
				traverseLibraryCall(stmt, e);
			}
		}
		else if (f instanceof UnaryExpression) {
			traverseLibraryCall(stmt, ((UnaryExpression)f).getOperand());
		}
		else if (f instanceof BinaryExpression) {
			traverseLibraryCall(stmt, ((BinaryExpression)f).getLhsOperand());
			traverseLibraryCall(stmt, ((BinaryExpression)f).getRhsOperand());
		}
		else if (f instanceof CastExpression) {
			traverseLibraryCall(stmt, ((CastExpression)f).getCast());
		}
		else if (f instanceof DeclExpression) {
			traverseLibraryCall(stmt, ((DeclExpression)f).getIdExpression());
		}
		else if (f instanceof UserDefinedCallExpression) {
		}
        // else if (f instanceof CallExpression){
		// 	for(Expression e : ((CallExpression)f).getActualParameters()) {
		// 		traverseLibraryCall(e);
		// 	}
        // }
		else if (f instanceof LibraryCallExpression) {
			if(((LibraryCallExpression)f).getFunctionalName().equals("rand") || ((LibraryCallExpression)f).getFunctionalName().equals("scanf")) {
				stmt.setHaveLibraryCall(true);
			}
		}
		else if(f instanceof AtomicExpression) {
		} 
		else if (f instanceof CompoundExpression){//chl@2023.02.13 - compoundExpression
			for(Expression e : ((CompoundExpression)f).getExpression()) {
				traverseLibraryCall(stmt, e);
			}
		} 
		// 2023-05-02(Tue) SoheeJung
		// Another expression don't mind.
		else {}
	}
	
	public void traverseExprFromStatement(IfStatement stmt, InterestVarList inputvar, InterestVarList statevar, InterestVarList outputvar){
		// System.out.println("this is if statement : " + stmt);
		// System.out.println("this is if condition : " + stmt.getCondition());
        traverseExprStateOutput(stmt.getCondition(), inputvar, statevar, outputvar);
    }

    /**
	 * @date 2023-03-28(Tue)
	 * @author SoheeJung
	 * @name traverseExprStateOutput
	 * fix variable -> rt_state.{variable} / rt_output.{variable}
	 */
    public void traverseExprStateOutput(Expression f, InterestVarList inputvar, InterestVarList statevar, InterestVarList outputvar){
		System.out.println("Expression : " + f);
		if (f instanceof IdExpression) {
			for(String s : inputvar.getNameList()) {
				if(((IdExpression)f).getName().equals(s) && !statevar.getNameList().contains(s)) {
					((IdExpression)f).setFixed_name("rt_input." + s);
				}
			}
			for(String s : statevar.getNameList()) {
				if(((IdExpression)f).getName().equals(s)) {
					((IdExpression)f).setFixed_name("rt_state." + s);
				}
			}
			for(String s : outputvar.getNameList()) {
				if(((IdExpression)f).getName().equals(s) && !statevar.getNameList().contains(s)) {
					((IdExpression)f).setFixed_name("rt_output." + s);
				}
			}
			// getIdExpressionList().add((IdExpression)f);
		}
		else if (f instanceof TypedefExpression){
			for (Expression e : ((TypedefExpression)f).getExpressions()) {
				if (e instanceof BracketExpression && ((BracketExpression)e).getPosition()) {
					traverseExprStateOutput(e, inputvar, statevar, outputvar);
				} else if (e instanceof BracketExpression) {
					traverseExprStateOutput(e, inputvar, statevar, outputvar);
				} else {
					traverseExprStateOutput(e, inputvar, statevar, outputvar);
				}
			} 
		}
        else if (f instanceof ConditionalExpression) {
			traverseExprStateOutput(((ConditionalExpression)f).getCondition(), inputvar, statevar, outputvar);
		} 
		else if (f instanceof ArrayExpression) {
			traverseExprStateOutput(((ArrayExpression)f).getAtomic(), inputvar, statevar, outputvar);
			for (Expression e : ((ArrayExpression)f).getIndexExpression()) {
				traverseExprStateOutput(e, inputvar, statevar, outputvar);
			}
		}
		else if (f instanceof UnaryExpression) {
			traverseExprStateOutput(((UnaryExpression)f).getOperand(), inputvar, statevar, outputvar);
		}
		else if (f instanceof BinaryExpression) {
			traverseExprStateOutput(((BinaryExpression)f).getLhsOperand(), inputvar, statevar, outputvar);
			traverseExprStateOutput(((BinaryExpression)f).getRhsOperand(), inputvar, statevar, outputvar);
		}
		else if (f instanceof CastExpression) {
			traverseExprStateOutput(((CastExpression)f).getCast(), inputvar, statevar, outputvar);
		}
		else if (f instanceof DeclExpression) {
			traverseExprStateOutput(((DeclExpression)f).getBinaryExpression(), inputvar, statevar, outputvar);
			traverseExprStateOutput(((DeclExpression)f).getIdExpression(), inputvar, statevar, outputvar);
		}
		else if (f instanceof UserDefinedCallExpression) {
			// 2023-05-10(Wed) SoheeJung
			// If expression's type is UserDefinedCallExpression, then get parameter from actualParameters.
			for(Expression e : ((CallExpression)f).getActualParameters()) {
				traverseExprStateOutput(e, inputvar, statevar, outputvar);
			}
		}
        else if (f instanceof CallExpression){
			for(Expression e : ((CallExpression)f).getActualParameters()) {
				traverseExprStateOutput(e, inputvar, statevar, outputvar);
			}
        }
		else if (f instanceof LibraryCallExpression) {
		}
		else if(f instanceof AtomicExpression) {
			// System.out.println(f.getCode());
		} 
		else if (f instanceof CompoundExpression){//chl@2023.02.13 - compoundExpression
			for(Expression e : ((CompoundExpression)f).getExpression()) {
				traverseExprStateOutput(e, inputvar, statevar, outputvar);
			}
		} 
		// 2023-05-02(Tue) SoheeJung
		// Another expression don't mind.
		else {}
		/*chl@2023.02.09 - nested code*/
		//if (f.getNext() != null) {
		//	traverse(f.getNext());
		//}
    }

	// getter & setter
	// public ArrayList<IdExpression> getIdExpressionList() {
	// 	return idExpressionList;
	// }

	// public void setIdExpressionList(ArrayList<IdExpression> idExpressionList) {
	// 	this.idExpressionList = idExpressionList;
	// }
}
