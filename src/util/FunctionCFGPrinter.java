package util;

import syntax.statement.*;
import udbparser.udbrawdata.UdbLexemeNode;

import java.util.ArrayList;

public class FunctionCFGPrinter {
    private boolean flag_els = false;
    private SourceCode code;
    
    public void printFunction2(Function function) {
    	code = new SourceCode();
//    	string들을 담을 변수를 누가 갖고 있는지 고려 후 작성하도록 하기
    	
        if (((CompoundStatement) function.getBody()).getFirst().getNext() instanceof NullStatement) {
            System.out.println(function.toString() +";");
        }
        else {
            System.out.println(function.toString() + " {");
            Statement stmt = (CompoundStatement) function.getBody();
            printStatement2(stmt);
            System.out.println("}\n");
        }
    }
    
    public void printStatement2(Statement stmt) {
        if (stmt instanceof CompoundStatement) { printCompoundStatement2((CompoundStatement) stmt); }
        else if (stmt instanceof ExpressionStatement) { printExpressionStatement2((ExpressionStatement) stmt); }
        else if (stmt instanceof DeclarationStatement) { printDeclarationStatement2((DeclarationStatement) stmt); } 
        else if (stmt instanceof PseudoStatement) { printPseudoStatement2((PseudoStatement) stmt); } 
        else if (stmt instanceof IfStatement) { printIfStatement2((IfStatement) stmt); } 
        else if (stmt instanceof GotoStatement) { printGotoStatement2((GotoStatement) stmt); } 
        else if (stmt instanceof LabelStatement) { printLabelStatement2((LabelStatement) stmt); } 
        else if (stmt instanceof ForStatement) { printForStatement2((ForStatement) stmt); } 
        else if (stmt instanceof DoStatement) { printDoStatement2((DoStatement) stmt); } 
        else if (stmt instanceof LoopStatement) { printLoopStatement2((LoopStatement) stmt); } 
        else if (stmt instanceof SwitchStatement) { printSwitchStatement2((SwitchStatement) stmt); } 
        else if (stmt instanceof CaseStatement) { printCaseStatement2((CaseStatement) stmt); } 
        else if (stmt instanceof DefaultStatement) { printDefaultStatement2((DefaultStatement) stmt); } 
        else if (stmt instanceof ContinueStatement) { printContinueStatement2((ContinueStatement) stmt); } 
        else if (stmt instanceof BreakStatement) { printBreakStatement2((BreakStatement) stmt); } 
        else if (stmt instanceof ReturnStatement) { printReturnStatement2((ReturnStatement) stmt); }
    }

    public void printCompoundStatement2(CompoundStatement stmt) {
        for(Statement s: stmt.getBody()) { printStatement2(s); }
    }

    public void printExpressionStatement2(ExpressionStatement stmt) {
        System.out.println(printBlank(stmt.getParents().size() + 1) + printLexemeData(stmt.getRawData()));
    }

    public void printDeclarationStatement2(DeclarationStatement stmt){
        System.out.println(printBlank(stmt.getParents().size() + 1) + printLexemeData(stmt.getRawData()));
    }

    public void printPseudoStatement2(PseudoStatement stmt) {}

    public void printIfStatement2(IfStatement stmt) {
        if (flag_els) {
            System.out.println("if " + printLexemeData(stmt.getRawData()) + " {");
            flag_els = false;
        }
        else {
        	System.out.println(printBlank(stmt.getParents().size() + 1) + "if " + printLexemeData(stmt.getRawData()) + " {"); 
        }
        printStatement2(stmt.getThen());
        System.out.println(printBlank(stmt.getParents().size() + 1) + "}");
        if (!(stmt.getEls() instanceof NullStatement)) {
            if (stmt.getEls() instanceof IfStatement) {
                flag_els = true;
                System.out.print(printBlank(stmt.getParents().size() + 1) + "else ");
                printStatement2(stmt.getEls());
            }
            else {
                System.out.println(printBlank(stmt.getParents().size() + 1) + "else {");
                printStatement2(stmt.getEls());
                System.out.println(printBlank(stmt.getParents().size() + 1) + "}");
            }
        }
    }

    public void printGotoStatement2(GotoStatement stmt) {
        System.out.println(printBlank(stmt.getParents().size() + 1) + "goto " + printLexemeData(stmt.getRawData()) + ";");
    }

    public void printLabelStatement2(LabelStatement stmt){
        System.out.println(printLexemeData(stmt.getRawData()) + ":");
    }

    public void printForStatement2(ForStatement stmt) {
        System.out.println(printBlank(stmt.getParents().size() + 1) + "for " + printLexemeData(stmt.getRawData()) + " {");
        printStatement2(stmt.getThen());
        System.out.println(printBlank(stmt.getParents().size() + 1) + "}");
    }

    public void printDoStatement2(DoStatement stmt) {
        System.out.println(printBlank(stmt.getParents().size() + 1) + "do {");
        printStatement2(stmt.getThen());
        System.out.println(printBlank(stmt.getParents().size() + 1) + "} while " + printLexemeData(stmt.getRawData()) + ";");
    }

    public void printLoopStatement2(LoopStatement stmt) {
        System.out.println(printBlank(stmt.getParents().size() + 1) + "while " + printLexemeData(stmt.getRawData()) + " {");
        printStatement2(stmt.getThen());
        System.out.println(printBlank(stmt.getParents().size() + 1) + "}");
    }

    public void printSwitchStatement2(SwitchStatement stmt) {
        System.out.println(printBlank(stmt.getParents().size() + 1) + "switch " + printLexemeData(stmt.getRawData()) + " {");
        for (CaseStatement caseStmt: stmt.getCases()) { printStatement2(caseStmt); }
        if (!(stmt.getDefaultStatement().getBody() instanceof NullStatement)) {
            System.out.println(printBlank(stmt.getParents().size() + 1) + "default:");
            printStatement2(stmt.getDefaultStatement().getBody());
        }
        System.out.println(printBlank(stmt.getParents().size() + 1) + "}");
    }

    public void printCaseStatement2(CaseStatement stmt) {
        System.out.println(printBlank(stmt.getParents().size() + 1) + printLexemeData(stmt.getRawData()));
        printStatement2(stmt.getBody());
    }

    public void printDefaultStatement2(DefaultStatement stmt) {
        System.out.println(printBlank(stmt.getParents().size() + 1) + printLexemeData(stmt.getRawData()));
        printStatement2(stmt.getBody());
    }

    public void printContinueStatement2(ContinueStatement stmt){
        System.out.println(printBlank(stmt.getParents().size() + 1) + printLexemeData(stmt.getRawData()) + ";");
    }

    public void printBreakStatement2(BreakStatement stmt){
        System.out.println(printBlank(stmt.getParents().size() + 1) + printLexemeData(stmt.getRawData()) + ";");
    }

    public void printReturnStatement2(ReturnStatement stmt) {
        System.out.println(printBlank(stmt.getParents().size() + 1) + printLexemeData(stmt.getRawData()));
    }

    public void printCFG2(Function f) {
        CompoundStatement body = new CompoundStatement();
        body = (CompoundStatement) f.getBody();

        for (Statement stmt : body.getBody()) {
            System.out.print(stmt + "\n\tnext: ");

            if (stmt instanceof IfStatement) {
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
            }

            else if (stmt instanceof LoopStatement) {
                if (((LoopStatement) stmt).getThen() instanceof CompoundStatement) {
                    System.out.print(
                            ((CompoundStatement) ((LoopStatement) stmt).getThen()).getFirst().printStatement() + " ");
                } else {
                    System.out.print(((LoopStatement) stmt).getThen().printStatement() + " ");
                }
            }

            else if (stmt instanceof SwitchStatement) {
                for (CaseStatement caseStmt : ((SwitchStatement) stmt).getCases()) {
                    System.out.print(caseStmt.printStatement() + " ");
                }
                if (!(((SwitchStatement) stmt).getDefaultStatement().getBody() instanceof NullStatement)) {
                    System.out.print(((SwitchStatement) stmt).getDefaultStatement().printStatement() + " ");
                }
            }

            else if (stmt instanceof CaseStatement) {
                if (((CaseStatement) stmt).getBody() instanceof CompoundStatement) {
                    System.out.print(
                            ((CompoundStatement) ((CaseStatement) stmt).getBody()).getFirst().printStatement() + " ");
                } else {
                    System.out.print(((CaseStatement) stmt).getBody().printStatement() + " ");
                }
            }

            if (stmt.getNext() == null) {
            } else {
                System.out.println(stmt.getNext().printStatement());
            }

            System.out.println("");
        }
    }

    public String printLexemeData(ArrayList<UdbLexemeNode> lexs) {
        String str = "";
        for (UdbLexemeNode lex : lexs) { str = str + lex.getData(); }
        return str;
    }

    public String printBlank(int i) {
        String blank = "";
        for (int c = 0; c < i; c++) { blank = blank + "\t";	}
        return blank;
    }
}
