package util;

import syntax.statement.BreakStatement;
import syntax.statement.CaseStatement;
import syntax.statement.CompoundStatement;
import syntax.statement.ContinueStatement;
import syntax.statement.DeclarationStatement;
import syntax.statement.DefaultStatement;
import syntax.statement.DoStatement;
import syntax.statement.ExitStatement;
import syntax.statement.ExpressionStatement;
import syntax.statement.ForStatement;
import syntax.statement.Function;
import syntax.statement.GotoStatement;
import syntax.statement.IfStatement;
import syntax.statement.LabelStatement;
import syntax.statement.LoopStatement;
import syntax.statement.NullStatement;
import syntax.statement.PseudoStatement;
import syntax.statement.ReturnStatement;
import syntax.statement.Statement;
import syntax.statement.SwitchStatement;

public class FunctionCFGNextPrinter {
	private boolean flag_els = false;
   
	public void printAboutFunction(Function f) {
		printNextStatement(f.getBody());
	}
	
	public void printNextStatement(Statement stmt) {
		  if (stmt instanceof CompoundStatement) { printCompoundStatement((CompoundStatement) stmt); }
	        else if (stmt instanceof ExpressionStatement) { printExpressionStatement((ExpressionStatement) stmt); }
	        else if (stmt instanceof DeclarationStatement) { printDeclarationStatement((DeclarationStatement) stmt); } 
	        else if (stmt instanceof PseudoStatement) { printPseudoStatement((PseudoStatement) stmt); } 
	        else if (stmt instanceof IfStatement) { printIfStatement((IfStatement) stmt); } 
	        else if (stmt instanceof GotoStatement) { printGotoStatement((GotoStatement) stmt); } 
	        else if (stmt instanceof LabelStatement) { printLabelStatement((LabelStatement) stmt); } 
	        else if (stmt instanceof ForStatement) { printForStatement((ForStatement) stmt); } 
	        else if (stmt instanceof DoStatement) { printDoStatement((DoStatement) stmt); } 
	        else if (stmt instanceof LoopStatement) { printLoopStatement((LoopStatement) stmt); } 
	        else if (stmt instanceof SwitchStatement) { printSwitchStatement((SwitchStatement) stmt); } 
	        else if (stmt instanceof CaseStatement) { printCaseStatement((CaseStatement) stmt); } 
	        else if (stmt instanceof DefaultStatement) { printDefaultStatement((DefaultStatement) stmt); } 
	        else if (stmt instanceof ContinueStatement) { printContinueStatement((ContinueStatement) stmt); } 
	        else if (stmt instanceof BreakStatement) { printBreakStatement((BreakStatement) stmt); } 
	        else if (stmt instanceof ReturnStatement) { printReturnStatement((ReturnStatement) stmt); }
	        else if (stmt instanceof ExitStatement) { printExitStatement((ExitStatement) stmt); }
	}
	
	public void printExitStatement(ExitStatement stmt) {
		System.out.println(stmt);
		System.out.print("\t<<next>> : ");
		if (stmt.getNext() == null) {
        } else {
            System.out.println(stmt.getNext().printStatement());
        }
	}

	public void printReturnStatement(ReturnStatement stmt) {
		System.out.println(stmt);
		System.out.print("\t<<next>> : ");
		if (stmt.getNext() == null) {
        } else {
            System.out.println(stmt.getNext().printStatement());
        }
	}

	public void printBreakStatement(BreakStatement stmt) {
		System.out.println(stmt);
		System.out.print("\t<<next>> : ");
		if (stmt.getNext() == null) {
        } else {
            System.out.println(stmt.getNext().printStatement());
        }
	}

	public void printDefaultStatement(DefaultStatement stmt) {
		System.out.println(stmt);
		System.out.print("\t<<next>> : ");
		if (((DefaultStatement) stmt).getBody() instanceof CompoundStatement) {
            System.out.print(
                    ((CompoundStatement) ((DefaultStatement) stmt).getBody()).getFirst().printStatement() + " ");
        } else {
            System.out.print(((DefaultStatement) stmt).getBody().printStatement() + " ");
        }
		if (stmt.getNext() == null) {
        } else {
            System.out.println(stmt.getNext().printStatement());
        }
		printNextStatement(stmt.getBody());
	}

	public void printCaseStatement(CaseStatement stmt) {
		System.out.println(stmt);
		System.out.print("\t<<next>> : ");
		if (((CaseStatement) stmt).getBody() instanceof CompoundStatement) {
            System.out.print(
                    ((CompoundStatement) ((CaseStatement) stmt).getBody()).getFirst().printStatement() + " ");
        } else {
            System.out.print(((CaseStatement) stmt).getBody().printStatement() + " ");
        }
		if (stmt.getNext() == null) {
        } else {
            System.out.println(stmt.getNext().printStatement());
        }
		printNextStatement(stmt.getBody());
	}

	public void printContinueStatement(ContinueStatement stmt) {
		System.out.println(stmt);
		System.out.print("\t<<next>> : ");
		if (stmt.getNext() == null) {
        } else {
            System.out.println(stmt.getNext().printStatement());
        }
	}

	public void printLoopStatement(LoopStatement stmt) {
		System.out.println(stmt);
		System.out.print("\t<<next>> : ");
		if (((LoopStatement) stmt).getThen() instanceof CompoundStatement) {
            System.out.print(
                    ((CompoundStatement) ((LoopStatement) stmt).getThen()).getFirst().printStatement() + " ");
        } else {
            System.out.print(((LoopStatement) stmt).getThen().printStatement() + " ");
        }
		if (stmt.getNext() == null) {
        } else {
            System.out.println(stmt.getNext().printStatement());
        }
		printNextStatement(stmt.getThen());
	}

	public void printSwitchStatement(SwitchStatement stmt) {
		System.out.println(stmt);
		System.out.print("\t<<next>> : ");
		for (CaseStatement caseStmt : ((SwitchStatement) stmt).getCases()) {
            System.out.print(caseStmt.printStatement() + " ");
        }
        if (!(((SwitchStatement) stmt).getDefaultStatement().getBody() instanceof NullStatement)) {
            System.out.print(((SwitchStatement) stmt).getDefaultStatement().printStatement() + " ");
        }
        if (stmt.getNext() == null) {
        } else {
            System.out.println(stmt.getNext().printStatement());
        }
		for (CaseStatement caseStmt: stmt.getCases()) { printNextStatement(caseStmt); }
		if (!(stmt.getDefaultStatement().getBody() instanceof NullStatement)) {
	           printNextStatement(stmt.getDefaultStatement().getBody());
	       }
	}

	public void printDoStatement(DoStatement stmt) {
		System.out.println(stmt);
		System.out.print("\t<<next>> : ");
		if (((LoopStatement) stmt).getThen() instanceof CompoundStatement) {
            System.out.print(
                    ((CompoundStatement) ((LoopStatement) stmt).getThen()).getFirst().printStatement() + " ");
        } else {
            System.out.print(((LoopStatement) stmt).getThen().printStatement() + " ");
        }
		if (stmt.getNext() == null) {
        } else {
            System.out.println(stmt.getNext().printStatement());
        }
		printNextStatement(stmt.getThen());
	}

	public void printLabelStatement(LabelStatement stmt) {
		System.out.println(stmt);
		System.out.print("\t<<next>> : ");
		if (stmt.getNext() == null) {
        } else {
            System.out.println(stmt.getNext().printStatement());
        }
	}

	public void printForStatement(ForStatement stmt) {
		System.out.println(stmt);
		System.out.print("\t<<next>> : ");
		if (((LoopStatement) stmt).getThen() instanceof CompoundStatement) {
            System.out.print(
                    ((CompoundStatement) ((LoopStatement) stmt).getThen()).getFirst().printStatement() + " ");
        } else {
            System.out.print(((LoopStatement) stmt).getThen().printStatement() + " ");
        }
		if (stmt.getNext() == null) {
        } else {
            System.out.println(stmt.getNext().printStatement());
        }
		printNextStatement(stmt.getThen());
	}

	public void printIfStatement(IfStatement stmt) {
		 if (flag_els) {
			 	System.out.println(stmt);
	            flag_els = false;
	        }
        else {
        	System.out.println(stmt);
        }
		System.out.print("\t<<next>> : ");
		if (((IfStatement) stmt).getThen() instanceof CompoundStatement) {
            System.out.print(
                    ((CompoundStatement) ((IfStatement) stmt).getThen()).getFirst().printStatement() + " ");
        } else {
            System.out.print(((IfStatement) stmt).getThen().printStatement() + " ");
        }

        if (((IfStatement) stmt).getEls() instanceof NullStatement) {

        } else if (((IfStatement) stmt).getEls() instanceof CompoundStatement) {
            System.out.print(
                    ((CompoundStatement) ((IfStatement) stmt).getEls()).getFirst().printStatement() + " ");
        } else {
            System.out.print(((IfStatement) stmt).getEls().printStatement() + " ");
        }
        if (stmt.getNext() == null) {
        } else {
            System.out.println(stmt.getNext().printStatement());
        }
        printNextStatement(stmt.getThen());
        if (!(stmt.getEls() instanceof NullStatement)) {
            if (stmt.getEls() instanceof IfStatement) {
                flag_els = true;
                printNextStatement(stmt.getEls());
            }
            else {
                printNextStatement(stmt.getEls());
            }
        }
	}

	public void printGotoStatement(GotoStatement stmt) {
		System.out.println(stmt);
		System.out.print("\t<<next>> : ");
		if (stmt.getNext() == null) {
        } else {
            System.out.println(stmt.getNext().printStatement());
        }
	}

	public void printDeclarationStatement(DeclarationStatement stmt) {
		System.out.println(stmt);
		System.out.print("\t<<next>> : ");
		if (stmt.getNext() == null) {
        } else {
            System.out.println(stmt.getNext().printStatement());
        }
	}

	public void printPseudoStatement(PseudoStatement stmt) {
		System.out.println(stmt);
		System.out.print("\t<<next>> : ");
		if (stmt.getNext() == null) {
        } else {
            System.out.println(stmt.getNext().printStatement());
        }
	}

	public void printCompoundStatement(CompoundStatement stmt) {
		for(Statement s : stmt.getBody()) printNextStatement(s);
	}

	public void printExpressionStatement(ExpressionStatement stmt) {
		System.out.println(stmt);
		System.out.print("\t<<next>> : ");
		if (stmt.getNext() == null) {
        } else {
            System.out.println(stmt.getNext().printStatement());
        }
	}
	
	
}
