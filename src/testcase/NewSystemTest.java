package testcase;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Scanner;
import java.util.Set;
import java.util.Stack;

import syntax.statement.CaseStatement;
import syntax.statement.CompoundStatement;
import syntax.statement.DefaultStatement;
import syntax.statement.Function;
import syntax.statement.GotoStatement;
import syntax.statement.IfStatement;
import syntax.statement.LoopStatement;
import syntax.statement.NullStatement;
import syntax.statement.Statement;
import syntax.statement.SwitchStatement;
import udbparser.udbrawdata.UdbLexemeNode;
import udbparser.usingperl.UdbInfoRetriever;

public class NewSystemTest {
	private ArrayList<Function> functions;
	private HashMap<String, ArrayList<String>> callGraph;
	private HashMap<Statement, Boolean> visitedStmt;
	private HashMap<String, Boolean> visitedCall;
	
	public static void main(String[] args) throws InterruptedException, IOException {
		NewSystemTest test = new NewSystemTest();
		
		UdbInfoRetriever udbInfoRetriever = new UdbInfoRetriever();
		
		String srcPath = "./data/injuntest/functest04.c";
		udbInfoRetriever.makeFunctionsFromUDB(srcPath);
		
		test.setFunctions(udbInfoRetriever.getFunctions());
		test.setCallGraph(test.makeCallGraph(test.getFunctions()));
		test.printCallGraph(test.getCallGraph());
		
		int index = 1;
		System.out.println("<<Function List>>");
		
		for(Function f : test.getFunctions()) {
			System.out.println((index++) + " : " + f.getName());
		}
		System.out.println();
		
		Scanner keyboard = new Scanner(System.in);
		System.out.print("Choose number from functionList > ");
		
		int choice = keyboard.nextInt();
		Function startFunc = test.getFunctions().get(choice - 1);
		
		test.setVisitedStmt(new HashMap<Statement, Boolean>());
		
		test.traverse2(((CompoundStatement)startFunc.getBody()).getFirst());
	}

	// 20210820 고려해보기
	// 월요일부터 이거 어떻게 할 건지 고려해보기
	public void traverse2(Statement upStmt) {
		Stack<Statement> traverseStack = new Stack<Statement>();
		Stack<Statement> subStack = new Stack<Statement>();
		Statement nextstmt = new Statement();
		nextstmt = upStmt.getNext();
		
		while(nextstmt != null) {
			printUnvisitedStmt(nextstmt);
			
			if(nextstmt instanceof IfStatement) {
				Statement thenstmt = new Statement();
				thenstmt = ((IfStatement)nextstmt).getThen();
				if(thenstmt instanceof CompoundStatement) {
					Statement cmpstmt = new Statement();
					cmpstmt = ((CompoundStatement)thenstmt).getFirst();
					while(cmpstmt != null) {
						System.out.print("cmp_then => ");
						printUnvisitedStmt(cmpstmt);
						cmpstmt = cmpstmt.getNext();
					}
				}
				else {
					System.out.print("then => ");
					printUnvisitedStmt(thenstmt);
				}
				
				Statement elsestmt = new Statement();
				elsestmt = ((IfStatement)nextstmt).getEls();
				if(!(elsestmt instanceof NullStatement)) {
					if(elsestmt instanceof CompoundStatement) {
						Statement cmpstmt = new Statement();
						cmpstmt = ((CompoundStatement)elsestmt).getFirst();
						traverseStack.push(cmpstmt);
					}
					else traverseStack.push(elsestmt);
				}
			}
			
			else if(nextstmt instanceof GotoStatement) {
				nextstmt = nextstmt.getNextLine();
				traverseStack.push(nextstmt);
			}
			
			else if(nextstmt instanceof LoopStatement) {
				Statement thenstmt = new Statement();
				thenstmt = ((LoopStatement)nextstmt).getThen();
				if(thenstmt instanceof CompoundStatement) {
					Statement cmpstmt = new Statement();
					cmpstmt = ((CompoundStatement)thenstmt).getFirst();
					while(cmpstmt != null) {
						System.out.print("loop_then => ");
						printUnvisitedStmt(cmpstmt);
						cmpstmt = cmpstmt.getNext();
					}
				}
				else {
					System.out.print("then => ");
					printUnvisitedStmt(thenstmt);
				}
				nextstmt = nextstmt.getNext();
				traverseStack.push(nextstmt);
			}
			
			else if(nextstmt instanceof SwitchStatement) {
				ArrayList<CaseStatement> cases = new ArrayList<CaseStatement>();
				CaseStatement cs = new CaseStatement();
				cases = ((SwitchStatement)nextstmt).getCases();
				for(int i = 0; i<cases.size(); i++) {
					cs = cases.get(i);
					System.out.print("case => ");
					printUnvisitedStmt(cs);
 					Statement bodystmt = new Statement();
 					bodystmt = cs.getBody();
 					if(bodystmt instanceof CompoundStatement) {
 						Statement cmpstmt = new Statement();
 						cmpstmt = ((CompoundStatement)bodystmt).getFirst();
 						while(cmpstmt != null) {
 							if(getVisitedStmt().containsKey(cmpstmt)) break;
 							System.out.print("body_cmp_then => ");
 							printUnvisitedStmt(cmpstmt);
 							cmpstmt = cmpstmt.getNext();
 						}
 					}
 					else {
 						System.out.print("body_then => ");
 						printUnvisitedStmt(bodystmt);
 					}
				}
				if(((SwitchStatement)nextstmt).getDefaultStatement().getStatementId() != 0) {
					DefaultStatement ds = new DefaultStatement();
					ds = ((SwitchStatement)nextstmt).getDefaultStatement();
					System.out.print("default => ");
					printUnvisitedStmt(ds);
					Statement bodystmt = new Statement();
 					bodystmt = ds.getBody();
 					if(bodystmt instanceof CompoundStatement) {
 						Statement cmpstmt = new Statement();
 						cmpstmt = ((CompoundStatement)bodystmt).getFirst();
 						while(cmpstmt != null) {
 							if(getVisitedStmt().containsKey(cmpstmt)) break;
 							System.out.print("default_cmp_then => ");
 							printUnvisitedStmt(cmpstmt);
 							cmpstmt = cmpstmt.getNext();
 						}
 					}
 					else {
 						System.out.print("body_then => ");
 						printUnvisitedStmt(bodystmt);
 					}
				}
				nextstmt = nextstmt.getNext();
				traverseStack.push(nextstmt);
			}
			
			else {
				nextstmt = nextstmt.getNext();
				traverseStack.push(nextstmt);
			}
			
			if(traverseStack.empty()) break;
			else nextstmt = traverseStack.pop();
		}
	}


	public void printUnvisitedStmt(Statement stmt) {
		if(getVisitedStmt().containsKey(stmt)) return;
		System.out.println("traverse result : " + stmt);
		getVisitedStmt().put(stmt, true);
	}


	public void printCallGraph(HashMap<String, ArrayList<String>> callgraph) {
		System.out.println("\\n================= Caller : Callee =================\n");
		
		Set<Entry<String, ArrayList<String>>> set = callgraph.entrySet();
		Iterator<Entry<String, ArrayList<String>>> itr = set.iterator();
		
		while(itr.hasNext()) {
			Map.Entry<String, ArrayList<String>> e = (Map.Entry<String, ArrayList<String>>) itr.next();
			System.out.printf("%-26s:", e.getKey());
			
			ArrayList<String> values = e.getValue();
			
			if(values.size() == 0) {
				System.out.println(" No Calls\n");
				continue;
			}
			
			for(int i = 0; i < values.size(); i++) {
				System.out.printf(" %s", values.get(i));
				if (i != values.size() - 1) {
					System.out.printf(",");
				}
			}
			System.out.println("\n");
		}
	}


	public HashMap<String, ArrayList<String>> makeCallGraph(ArrayList<Function> functions) {
		HashMap<String, ArrayList<String>> callgraph = new HashMap<String, ArrayList<String>>();
		
		for(Function func : functions) {
			CompoundStatement cpstmt = new CompoundStatement();
			cpstmt = (CompoundStatement) func.getBody();
			
			ArrayList<String> values = new ArrayList<String>();
			for(Statement stmt : cpstmt.getBody()) {
				if(stmt == null) continue;
				
				ArrayList<UdbLexemeNode> udbLexemeNodes = hasCall(stmt);
				if(udbLexemeNodes == null) continue;
				
				for(int i = 0; i < udbLexemeNodes.size(); i++) {
					values.add(udbLexemeNodes.get(i).getData());
				}
			}
			callgraph.put(func.getName(), values);
		}
		return callgraph;
	}


	public ArrayList<UdbLexemeNode> hasCall(Statement stmt) {
		ArrayList<UdbLexemeNode> lexemes = new ArrayList<UdbLexemeNode>();
		
		for(UdbLexemeNode lxmNode : stmt.getRawData()) {
			if(lxmNode.getRef_kind().equals("Call")) {
				lexemes.add(lxmNode);
			}
		}
		
		if(lexemes.size() == 0) return null;
		return lexemes;
	}


	// getter and setter
	public ArrayList<Function> getFunctions() {
		return functions;
	}

	public void setFunctions(ArrayList<Function> functions) {
		this.functions = functions;
	}

	public HashMap<String, ArrayList<String>> getCallGraph() {
		return callGraph;
	}

	public void setCallGraph(HashMap<String, ArrayList<String>> callGraph) {
		this.callGraph = callGraph;
	}

	public HashMap<Statement, Boolean> getVisitedStmt() {
		return visitedStmt;
	}

	public void setVisitedStmt(HashMap<Statement, Boolean> visitedStmt) {
		this.visitedStmt = visitedStmt;
	}

	public HashMap<String, Boolean> getVisitedCall() {
		return visitedCall;
	}

	public void setVisitedCall(HashMap<String, Boolean> visitedCall) {
		this.visitedCall = visitedCall;
	}
}

