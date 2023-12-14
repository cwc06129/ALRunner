package testcase;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Scanner;
import java.util.Set;
import java.util.Stack;

import syntax.statement.BranchStatement;
import syntax.statement.CaseStatement;
import syntax.statement.CompoundStatement;
import syntax.statement.DefaultStatement;
import syntax.statement.DoStatement;
import syntax.statement.Function;
import syntax.statement.GotoStatement;
import syntax.statement.IfStatement;
import syntax.statement.LoopStatement;
import syntax.statement.NullStatement;
import syntax.statement.PseudoStatement;
import syntax.statement.ReturnStatement;
import syntax.statement.Statement;
import syntax.statement.SwitchStatement;
import udbparser.udbrawdata.UdbLexemeNode;
import udbparser.usingperl.UdbInfoRetriever;

public class SystemTest {
	private ArrayList<Function> functions;
	private HashMap<String, ArrayList<String>> callGraph;
	private HashMap<Statement, Boolean> visitedStmt;
	private HashMap<String, Boolean> visitedCall; // for downCall

	public static void main(String[] args) throws InterruptedException, IOException {
		SystemTest test = new SystemTest();
//		ForwardCfgTraverser traverser = new ForwardCfgTraverser();
//		ForwardCfgTraverserOrigin traverser = new ForwardCfgTraverserOrigin();
//		FunctionCFGPrinter cfgprint = new FunctionCFGPrinter();
		
		/* 0. Convert UDB to Design */
		UdbInfoRetriever udbInfoRetriever = new UdbInfoRetriever();

		// 0-1. Make Understand database(udb) and analyze it.
		String srcPath = "./data/injuntest/fortest.c";
		udbInfoRetriever.makeFunctionsFromUDB(srcPath);

		// 0-2. get All Functions in src.
		test.setFunctions(udbInfoRetriever.getFunctions());

		/* 1. print Call graph */
		test.setFunctions(udbInfoRetriever.getFunctions());
		test.setCallGraph(test.makeCallGraph(test.getFunctions()));
		test.printCallGraph(test.getCallGraph());

		/* 2. Prompt for User to choose one function. */
		//ArrayList<String> names = udbInfoRetriever.getFunctionNames();
		//printFunctionList(names);

		/* Print functionList */
		int index = 1;
		System.out.println("<<Function List>>");
		for(Function f : test.getFunctions()) {
			System.out.println((index++) + " : " + f.getName());
		}
		System.out.println();
		
		/* 3. Get choice from User */
		Scanner keyboard = new Scanner(System.in);
		System.out.printf("Choose number from functionList > ");
		int choice = keyboard.nextInt();
		Function startFunc = test.getFunctions().get(choice - 1);
		
		test.setVisitedStmt(new HashMap<Statement, Boolean>());
		
		/* 4. Traverse & Search */
		System.out.println("\n\n============== Control Flow of " + startFunc.getName() + " ==============\n\n");
		// 4.1 prepare for traverse
		test.setVisitedStmt(new HashMap<Statement, Boolean>());
		test.setVisitedCall(new HashMap<String, Boolean>());

		// 4-2. Traverse the function (include downCall)
		test.getVisitedCall().put(startFunc.getName(), true);
		test.traverse(((CompoundStatement) startFunc.getBody()).getLast());
//		Statement st = new Statement();
//		st = ((CompoundStatement)startFunc.getBody()).getFirst();
//		while(true) {
//			if(st == null) break;
//			System.out.println("this is statement : " + st);
//			st = st.getNext();
//		}
		System.out.println("this is first statement : " + ((CompoundStatement) startFunc.getBody()).getFirst().getNext());
		System.out.println("\n\n============== Finish Control Flow of " + startFunc.getName() + "==============");

		// 4-3. Find callers and add to upStack
		//test.upCall(startFunc.getName());

	}

	public void testing(String srcPath){
		UdbInfoRetriever udbInfoRetriever = new UdbInfoRetriever();
		udbInfoRetriever.makeFunctionsFromUDB(srcPath);

		setFunctions(udbInfoRetriever.getFunctions());

		for (int i = 0; i < udbInfoRetriever.getFunctions().size(); i++) {
			System.out.println("\nfunction: " + functions.get(i).getName());
			System.out.println("-----------------------------------------------");
			//FunctionCFGPrinter.printCFG2(getFunctions().get(i));
			System.out.println(" ");
		}
	}


	public ArrayList<String> findCaller(String callee) {
		// parameter: callee(Function name) that want to find callers.
		// return value: list of callers.

		ArrayList<String> caller = new ArrayList<String>();

		Set<Entry<String, ArrayList<String>>> set = getCallGraph().entrySet();
		Iterator<Entry<String, ArrayList<String>>> itr = set.iterator();
		while (itr.hasNext()) {
			Map.Entry<String, ArrayList<String>> e = (Map.Entry<String, ArrayList<String>>) itr.next();
			if (e.getKey().equals(callee))
				continue; // to remove recursive call.

			// Find callee in values
			ArrayList<String> values = e.getValue();
			if (values.size() == 0) {
				continue;
			}

			for (int i = 0; i < values.size(); i++) {
				if (values.get(i).equals(callee)) { // remove recursive relations.
					caller.add(e.getKey());
				}
			}
		}

		if (caller.size() == 0)
			return null;

		return caller;
	}

	public Function getFunctionByName(String funcName) {
		// parameter: function's name
		// return value: Function type instance its name is funcName

		for (Function func : functions) {
			if (func.getName().equals(funcName)) {
				return func;
			}
		}

		return null;
	}

	public void traceback(Statement bottomStmt) {
		// parameter: last Statement in a function's body.
		Stack<Statement> traverseStack = new Stack<Statement>();
		Boolean printFlag = false;

		/* 1. Start from bottomNode(bottomStmt) */
		int lastIndex = 1;
		Statement currentStatement = bottomStmt;
		if (currentStatement instanceof PseudoStatement) { // we won't print PseudoStatement
			lastIndex = bottomStmt.getBefore().size();
		}

		for (int index = 0; index < lastIndex; index++) {
			currentStatement = bottomStmt.getBefore().get(index);
			while (true) {
				/* 2. Print all Nodes(Statements) in function(reversed order) */
				if (!getVisitedStmt().containsKey(currentStatement) && !(currentStatement instanceof NullStatement)) { // if
																														// currentStatement
																														// is
																														// unvisited
					printUnvisitedStmt(currentStatement);
					printFlag = true;
				}

				if (printFlag) {
					/* 3. push before statement to traverse stack */
					if (currentStatement.getBefore() != null) {
						for (int i = 0; i < currentStatement.getBefore().size(); i++) {
							Statement tempStmt = currentStatement.getBefore().get(i);

							if (!(tempStmt instanceof CompoundStatement)) { // In this case, CompoundStatement means
																			// then or else attribute is instance of
																			// Compound.
								traverseStack.push(tempStmt);
							}
						}
					}

					/* 4. If current statement contains Call */
					ArrayList<UdbLexemeNode> call_list = hasCall(currentStatement);
					if (call_list != null) {
						for (int i = call_list.size() - 1; i >= 0; i--) {
							// If Call is UserDefinedCall and unvisited
							// and unvisited call
							Function call = isUserDefinedCall(call_list.get(i).getData());
							if (call != null && !getVisitedCall().containsKey(call.getName())) {
								// then downCall
								downCall(call);
							}
						}

					}

				}
				printFlag = false;

				/* 5. Find next Node(Statement) */
				if (traverseStack.empty())
					break;
				currentStatement = traverseStack.pop();

			}
		}

	}

	public void upCall(String callee) {
		String currentCallee = callee;

		/* 1. Find caller of currentCallee */
		List<String> callers = findCaller(currentCallee);
		if (callers == null) {
			return;
		}

		for (String next : callers) {
			if (!getVisitedCall().containsKey(next)) {

				// Find Statement that has callee.
				Statement keyStmt = null;
				Function caller = getFunctionByName(next);
				for (Statement stmt : ((CompoundStatement) caller.getBody()).getBody()) {
					for (UdbLexemeNode lxmNode : stmt.getRawData()) {
						// if lxmNode has callee
						if (lxmNode.getRef_kind().equals("Call") && lxmNode.getData().equals(currentCallee)) {
							keyStmt = stmt;
						}
					}
				}

				// Trace back from keyStmt to root of currentCall.
				System.out.println("\n============== upCall(" + next + ") ==============");
				System.out.println(keyStmt);
				Stack<Statement> tempStack = new Stack<Statement>();
				if (keyStmt.getBefore() != null) {
					for (int i = 0; i < keyStmt.getBefore().size(); i++) {
						Statement tempStmt = keyStmt.getBefore().get(i);

						if (!(tempStmt instanceof CompoundStatement)) { // In this case, CompoundStatement means then or
																		// else attribute is instance of Compound.
							tempStack.push(tempStmt);
						}
					}
				}
				while (!tempStack.empty()) {
					traceback(tempStack.pop());
				}
				System.out.println("================ Finish upCall(" + next + ") ================\n");

				// upcall by next Statement
				upCall(next);
			}
		}

	}

	public void downCall(Function func) {
		// parameter: Function that want to traverse.

		getVisitedCall().put(func.getName(), true);

		// specify parameter of traverse from func
		System.out.println("\n============== downCall(" + func.getName() + ") ==============");
		traverse(((CompoundStatement) func.getBody()).getLast());
		System.out.println("================ Finish downCall(" + func.getName() + ") ================\n");

	}

	public Function isUserDefinedCall(String funcName) {
		// parameter: a name of function.
		// return value: Function instance that has same name with funcName.
		// If doesn't exist, returns null.

		for (Function func : getFunctions()) {
			if (func.getName().equals(funcName)) {
				return func;
			}
		}

		// is not userDefinedCall
		return null;
	}

	public void printUnvisitedStmt(Statement stmt) {

		/* 1. Check if stmt in hashMap(visitedStmt) */
		if (getVisitedStmt().containsKey(stmt)) {
			return; // not print
		}

		/* 2. Print this Statement */
		System.out.println("+-+-+-+- result : " + stmt + " -+-+-+-+");
		
		/* 3. Add Stmt to hashMap */
		getVisitedStmt().put(stmt, true);
	}

	// FirstStmt부터 traverse하는 method 하나 더 만들기
	// 20210813
	public void traverse2(Statement upStmt) {
		Stack<Statement> traverseStack = new Stack<Statement>();
		Statement nextstmt = new Statement();
		nextstmt = upStmt.getNext();
		while(nextstmt != null) {
//			if(getVisitedStmt().containsKey(nextstmt)) continue;
			System.out.println("traverse result : " + nextstmt);
			getVisitedStmt().put(nextstmt, true);
			
			if(nextstmt instanceof IfStatement) {
				Statement thenstmt = new Statement();
				thenstmt = ((IfStatement)nextstmt).getThen();
				if(thenstmt instanceof CompoundStatement) {
					Statement cmpstmt = new Statement();
					cmpstmt = ((CompoundStatement)thenstmt).getFirst();
					while(cmpstmt != null) {
//						if(!getVisitedStmt().containsKey(cmpstmt))
//							System.out.println("traverse result(cmp_then) : " + cmpstmt);
						System.out.println("traverse result(cmp_then) : " + cmpstmt);
						getVisitedStmt().put(cmpstmt, true);
						cmpstmt = cmpstmt.getNext();
					
					}
				}
				else {
					System.out.println("traverse result(then) : " + thenstmt);
					getVisitedStmt().put(thenstmt, true);
				}
				
				Statement elsestmt = new Statement();
				elsestmt = ((IfStatement)nextstmt).getEls();
				if(elsestmt instanceof CompoundStatement) {
					Statement cmpstmt = new Statement();
					cmpstmt = ((CompoundStatement)elsestmt).getFirst();
					traverseStack.push(((CompoundStatement) elsestmt).getFirst());
				}
				else traverseStack.push(elsestmt);
			}
			// Goto Statement일 경우에는 Label Statement와 Goto Statement를 계속 반복해서 traverse를 진행하기 때문에 nextline으로 넘어가준다.
			// 20210820
			else if(nextstmt instanceof GotoStatement) {
				nextstmt = nextstmt.getNextLine();
				traverseStack.push(nextstmt);
			}
			// Loop Statement일 경우에는 반복문을 계속해서 traverse하기 때문에 이를 방지하기 위해 nextstmt를 임의로 지정해준다.
			// 20210820
			else if(nextstmt instanceof LoopStatement) {
				Statement thenstmt = new Statement();
				thenstmt = ((LoopStatement)nextstmt).getThen();
				if(thenstmt instanceof CompoundStatement) {
					Statement cmpstmt = new Statement();
					cmpstmt = ((CompoundStatement)thenstmt).getFirst();
					while(cmpstmt != null) {
						if(getVisitedStmt().containsKey(cmpstmt)) break;
						System.out.println("traverse result(cmp_then) : " + cmpstmt);
						getVisitedStmt().put(cmpstmt, true);
						cmpstmt = cmpstmt.getNext();
					}
				}
				else {
					System.out.println("traverse result(then) : " + thenstmt);
					getVisitedStmt().put(thenstmt, true);
				}
				nextstmt = nextstmt.getNext();
				traverseStack.push(nextstmt);
			}
			// Case Statement일 경우에는 Case문과 Default문에 대해서 고려를 해줘야 함.
			// 20210820
			else if(nextstmt instanceof SwitchStatement) {
				ArrayList<CaseStatement> cases = new ArrayList<CaseStatement>();
				CaseStatement cs = new CaseStatement();
				cases = ((SwitchStatement)nextstmt).getCases();
				for(int i = 0; i<cases.size(); i++) {
					cs = cases.get(i);
 					System.out.println("traverse result(case) : " + cs);
 					getVisitedStmt().put(cs, true);
 					Statement bodystmt = new Statement();
 					bodystmt = cs.getBody();
 					if(bodystmt instanceof CompoundStatement) {
 						Statement cmpstmt = new Statement();
 						cmpstmt = ((CompoundStatement)bodystmt).getFirst();
 						while(cmpstmt != null) {
 							if(getVisitedStmt().containsKey(cmpstmt)) break;
 							System.out.println("traverse result(body_cmp_then) : " + cmpstmt);
 							getVisitedStmt().put(cmpstmt, true);
 							cmpstmt = cmpstmt.getNext();
 						}
 					}
 					else {
 						System.out.println("traverse result(body_then) : " + bodystmt);
 						getVisitedStmt().put(bodystmt, true);
 					}
 					getVisitedStmt().put(bodystmt, true);
				}
				if(((SwitchStatement)nextstmt).getDefaultStatement() != null) {
					DefaultStatement ds = new DefaultStatement();
					ds = ((SwitchStatement)nextstmt).getDefaultStatement();
					System.out.println("traverse result(default) : " + ds);
					getVisitedStmt().put(ds, true);
					Statement bodystmt = new Statement();
 					bodystmt = ds.getBody();
 					if(bodystmt instanceof CompoundStatement) {
 						Statement cmpstmt = new Statement();
 						cmpstmt = ((CompoundStatement)bodystmt).getFirst();
 						while(cmpstmt != null) {
 							if(getVisitedStmt().containsKey(cmpstmt)) break;
 							System.out.println("traverse result(default_cmp_then) : " + cmpstmt);
 							getVisitedStmt().put(cmpstmt, true);
 							cmpstmt = cmpstmt.getNext();
 						}
 					}
 					else {
 						System.out.println("traverse result(body_then) : " + bodystmt);
 						getVisitedStmt().put(bodystmt, true);
 					}
 					getVisitedStmt().put(bodystmt, true);
				}
				nextstmt = nextstmt.getNext();
				traverseStack.push(nextstmt);
			}
			else {
				nextstmt = nextstmt.getNext();
				traverseStack.push(nextstmt);
			}
			
			nextstmt = traverseStack.pop();
		}
		setVisitedStmt(new HashMap<Statement, Boolean>());
	}
	
	public void traverse(Statement bottomStmt) {
		// parameter: last Statement in a function's body.
		Stack<Statement> traverseStack = new Stack<Statement>();
		Boolean printFlag = false;
		System.out.println("BottomStmt : " + bottomStmt);
		
		/* 1. Start from bottomNode(bottomStmt) */
		for (int index = bottomStmt.getBefore().size() - 1; index >= 0; index--) {
			Statement currentStatement = bottomStmt.getBefore().get(index);
			
			while (true) {
				System.out.println("\t  currentStatement : " + currentStatement);
				System.out.println("\t  currentStatement getBefore : " + currentStatement.getBefore());
				//System.out.println("traverseStack : " + traverseStack);
				/* 2. Print all Nodes(Statements) in function(reversed order) */
				if (!getVisitedStmt().containsKey(currentStatement)) { // if currentStatement is unvisited

					if (currentStatement instanceof BranchStatement) {
						BranchStatement bstmt = (BranchStatement) currentStatement;
						//System.out.println("bstmt : " + bstmt);
						Statement thenStmt = bstmt.getThen();
						if (thenStmt instanceof CompoundStatement) {
							thenStmt = ((CompoundStatement) thenStmt).getFirst();
						}
						//System.out.println("\t  First then Statement : " + thenStmt);
						// LoopStatement에 대해서 traverse가 제대로 진행되지 않아서 시도해보기
						// 20210726
						if (currentStatement instanceof LoopStatement) { 
							if(currentStatement instanceof DoStatement) {
								printUnvisitedStmt(bstmt);
								printFlag = true;
							}
							else {
								//printUnvisitedStmt(bstmt);
								printFlag = true;
							}
						}

						if (getVisitedStmt().containsKey(thenStmt)) {
							//System.out.println("\t  then first Statement : " + thenStmt);
							//System.out.println("thenStatement : " + thenStmt);
							//System.out.println("Yes. I'm here!");
							Statement elseStmt = bstmt.getEls();
							//System.out.println("\t ++++++++++++ elseStmt : " + elseStmt);
							if (elseStmt instanceof CompoundStatement) {
								elseStmt = ((CompoundStatement) elseStmt).getFirst();
							}
							//System.out.println("This is elsestmt : " + elseStmt);
							if (elseStmt instanceof NullStatement || getVisitedStmt().containsKey(elseStmt)) {
								printUnvisitedStmt(bstmt);
								printFlag = true;
							}
						}

					} else {
						//System.out.println("current Statement : " + currentStatement);
						//System.out.println("current before Statement : " + currentStatement.getBefore());
						if (!(currentStatement instanceof NullStatement)) {
							printUnvisitedStmt(currentStatement);
							printFlag = true;
						}
					}

				}

				if (printFlag) {
					/* 3. push before statement to traverse stack */
					if (currentStatement.getBefore() != null) {
						for (int i = 0; i < currentStatement.getBefore().size(); i++) {
							Statement tempStmt = currentStatement.getBefore().get(i);
							//System.out.println("tempStmt : " + tempStmt);
							if(tempStmt instanceof CompoundStatement) System.out.println("Compound statement : " + tempStmt);
							if (!(tempStmt instanceof CompoundStatement)) { // In this case, CompoundStatement means
																			// then or else attribute is instance of Compound.
								if(((currentStatement instanceof DefaultStatement) && (tempStmt instanceof CaseStatement)) || ((currentStatement instanceof CaseStatement) && (tempStmt instanceof CaseStatement)) || ((currentStatement instanceof ReturnStatement) && (tempStmt instanceof CaseStatement)))
									continue;
								//System.out.println("\t +++++++++++ temp Statement : " + tempStmt);
								traverseStack.push(tempStmt);
							}
						}
					}

					/* 4. If current statement contains Call */
					ArrayList<UdbLexemeNode> call_list = hasCall(currentStatement);
//					if (call_list != null) {
//						for (int i = call_list.size() - 1; i >= 0; i--) {
//							// If Call is UserDefinedCall and unvisited
//							// and unvisited call
//							Function call = isUserDefinedCall(call_list.get(i).getData());
//							if (call != null && !getVisitedCall().containsKey(call.getName())) {
//								// then downCall
//								downCall(call);
//							}
//						}
//
//					}

				}
				printFlag = false;
				
				System.out.println("Stack : " + traverseStack);
				
				/* 5. Find next Node(Statement) */
				if (traverseStack.empty()) {
					System.out.println("Ok! I will break!");
					break;
				}
				
//				System.out.println("[ Pop Before ]");
//				System.out.println(traverseStack);
				currentStatement = traverseStack.pop();
//				System.out.println("[ Pop After ]");
//				System.out.println(traverseStack);

			}

		}
	}

	public void printFunctionList(ArrayList<String> funcNames) {

		System.out.println("\n================= function List [id]: function name =================\n");
		int count = 1;
		int index = 1;
		for (int i = 0; i < funcNames.size(); i++) {

			System.out.printf("[%d]: %-26s", count, funcNames.get(i));
			count++;

			// print 4 function in one row
			if (index == 4) {
				index = 1;
				System.out.println("");
			} else {
				index++;
				System.out.printf("\t");
			}

		}
		System.out.println("\n");

	}

	public ArrayList<UdbLexemeNode> hasCall(Statement stmt) {
		// parameter: Statement that need to find Calls in it.
		// return value: list of LexemeNodes that contain Call.

		ArrayList<UdbLexemeNode> lexemes = new ArrayList<UdbLexemeNode>();

		// find name of Call
		for (UdbLexemeNode lxmNode : stmt.getRawData()) {
			/* if lxmNode has Call statement */
			if (lxmNode.getRef_kind().equals("Call")) {
				lexemes.add(lxmNode);
			}
		}

		if (lexemes.size() == 0) {
			return null;
		}
		return lexemes;
	}

	public void printCallGraph(HashMap<String, ArrayList<String>> callgraph) {

		System.out.println("\n================= Caller : Callee =================\n");

		Set<Entry<String, ArrayList<String>>> set = callgraph.entrySet();
		Iterator<Entry<String, ArrayList<String>>> itr = set.iterator();
		while (itr.hasNext()) {
			Map.Entry<String, ArrayList<String>> e = (Map.Entry<String, ArrayList<String>>) itr.next();
			System.out.printf("%-26s:", e.getKey());

			ArrayList<String> values = e.getValue();
			if (values.size() == 0) {
				System.out.println(" No Calls\n");
				continue;
			}
			for (int i = 0; i < values.size(); i++) {
				System.out.printf(" %s", values.get(i));
				if (i != values.size() - 1) {
					System.out.printf(",");
				}
			}
			System.out.println("\n");
		}

	}

	public HashMap<String, ArrayList<String>> makeCallGraph(ArrayList<Function> functions) {
		// parameter: list of functions(type of Function).
		// return value: HashMap (key: Caller, value: list of Callee).

		HashMap<String, ArrayList<String>> callgraph = new HashMap<String, ArrayList<String>>();

		/* 1. Look up funtions List */
		for (Function func : functions) {

			CompoundStatement cpstmt = new CompoundStatement();
			cpstmt = (CompoundStatement) func.getBody();

			/* 2. Look up every statement in func */
			ArrayList<String> values = new ArrayList<String>();
			for (Statement stmt : cpstmt.getBody()) {
				if (stmt == null)
					continue;

				ArrayList<UdbLexemeNode> udbLexemeNodes = hasCall(stmt);
				if (udbLexemeNodes == null) {
					continue;
				}

				// find kind of Call( UserDefined or Library)
				// ExpressionStatement estmt = (ExpressionStatement) stmt;
				//
				// String typeOfStmt;
				// if (estmt.getExpression() instanceof UserDefinedCallExpression) {
				// typeOfStmt = "UserDefinedCall";
				// } else { // instanceof LibraryCall
				// typeOfStmt = "LibraryCall";
				// }

				// add Call to value list

				for (int i = 0; i < udbLexemeNodes.size(); i++) {
					// values.add(udbLexemeNodes.get(i).getData() + "(" + typeOfStmt + ")");
					values.add(udbLexemeNodes.get(i).getData());
				}

			} // end-for

			// put key to hashmap
			callgraph.put(func.getName(), values);

		} // end-for

		return callgraph;
	}

	/* Getter and Setter */

	public HashMap<String, ArrayList<String>> getCallGraph() {
		return callGraph;
	}

	public void setCallGraph(HashMap<String, ArrayList<String>> callGraph) {
		this.callGraph = callGraph;
	}

	public ArrayList<Function> getFunctions() {
		return functions;
	}

	public void setFunctions(ArrayList<Function> functions) {
		this.functions = functions;
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