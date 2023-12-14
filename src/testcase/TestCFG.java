package testcase;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.Stack;

import syntax.statement.BranchStatement;
import syntax.statement.CompoundStatement;
import syntax.statement.Function;
import syntax.statement.Statement;
//import udbparser.expr.ASTFromStatement;
//import udbparser.expr.InsertExprIntoStmt;
import udbparser.udbrawdata.UdbLexemeNode;
import udbparser.usingperl.UdbInfoRetriever;

public class TestCFG {
	private static ArrayList<Function> functions;
	private static Stack<String[]> upStack;
	private static HashMap<String, ArrayList<String>> callGraph;
	private static HashMap<Statement, Boolean> visitedStmt;
	private static HashMap<String, Boolean> visitedCall;

	public static void main(String[] args) throws InterruptedException, IOException {
		/* 0. Convert UDB to Design */
		UdbInfoRetriever udbInfoRetriever = new UdbInfoRetriever();

		// 0-1. Make Understand database(udb) and analyze it.
		String srcPath = "./data/injuntest/soheetest2.c";

		udbInfoRetriever.makeFunctionsFromUDB(srcPath);

		// 0-2. get All Functions in src.
		setFunctions(udbInfoRetriever.getFunctions());

		/* 1. print Call graph */
		setFunctions(udbInfoRetriever.getFunctions());
		setCallGraph(makeCallGraph(functions));
		printCallGraph(getCallGraph());

		// �� function�� parameter ������ �ľ��ؼ� makeASTFromLexOfStmt�Լ��� �Բ� �ѱ��.
		// ���� : 20210728
		HashMap<Function, Integer> numberOfParam = new HashMap<Function, Integer>();
		for (Function f : getFunctions()) {
			if (f.getParameters() == null)
				numberOfParam.put(f, 0);
			else
				numberOfParam.put(f, f.getParameters().size());
		}
		System.out.println(numberOfParam);

		/* 2. Prompt for User to choose one function. */
//		 ArrayList<String> names = udbInfoRetriever.getFunctionNames();
//		 printFunctionList(names);

		/* 3. Get choice from User */
//      Scanner keyboard = new Scanner(System.in);
//      System.out.printf("Choose number from functionList > ");
//      int choice = keyboard.nextInt();
//      Function startFunc = functions.get(choice-1);

		/* 4. Traverse & Search */
//      System.out.println("\n\n============== Control Flow of " + startFunc.getName() + " ==============\n\n");
//      
//      // 4.1 prepare for traverse
//      visitedStmt = new HashMap<Statement, Boolean>();
//      visitedCall = new HashMap<String, Boolean>();
//      
//      
//      // 4-2. Traverse the function (include downCall)
//      getVisitedCall().put(startFunc.getName(), true);
//      traverse(startFunc.getBody().getLast());
//      System.out.println("\n\n============== Finish Control Flow of " + startFunc.getName() + "==============");
//
//      
//      // 4-3. Find callers and add to upQueue
//      upStack = new Stack<String[]>();
//      for(String caller: findCaller(startFunc.getName())) {
//         String[] element = {caller, startFunc.getName()};   // caller, callee
//         upStack.push(element);
//      }
//      
//      // 4-4. Go to upcall
//      while(!upStack.empty()) {
//         
//         String[] calls = upStack.pop();   // pop first element
//         
//         upCall(calls[0], calls[1]); // caller, callee
//         
//         // find caller of calls[0].
//         ArrayList<String> callers = findCaller(calls[0]);
//         if(callers == null) continue;
//         for(String caller: callers) {
//            String[] element = {caller, calls[0]};   // caller, callee
//            upStack.push(element);
//         }
//      }

		/* For debugging: related with 4. Traverse & Search */
//      System.out.println("\n\n============== $$$Control Flow of " + names.get(choice - 1) + "==============");
//      Statement statement = functions.get(choice - 1).getBody().getFirst();
//      while (statement != null) {
//         System.out.println(statement);
//         statement = statement.getNext();
//      }
//      for(Statement statement : functions.get(choice - 1).getBody().getBody()) {
//         System.out.println(statement);
//      }

		/* For debugging: related with 0. Convert UDB to Design */

//		System.out.println("Original: " + ((CompoundStatement) functions.get(2).getBody()).getBody().get(11));
//		ASTFromStatement test = new ASTFromStatement();
//		test.makeASTFromLexOfStmt(((CompoundStatement) functions.get(2).getBody()).getBody().get(11));
//		if (((CompoundStatement) functions.get(2).getBody()).getBody().get(11) instanceof IfStatement) {
//			System.out.println(
//					"in!!!!!!!!!!!" + ((IfStatement) ((CompoundStatement) functions.get(2).getBody()).getBody().get(11))
//							.getCondition());
//		}

		// This code is print about third element in functions. But, This code can't
		// implement functions that have less than two elements.
		// So We should fix code. (Select Number Of functions (OR) All Functions)
		// I'll print all information for all functions.
		// 2021.07.23

//		for(Statement stmt: ((CompoundStatement) functions.get(2).getBody()).getBody()) {
//			ASTFromStatement test = new ASTFromStatement();
//			System.out.print("original: ");
//			for (UdbLexemeNode lex : stmt.getRawData()) {
//				System.out.print(lex.getData());
//			}
//			System.out.println();
//			
//			
//			test.makeASTFromLexOfStmt(stmt);
//		}

		// makeASTFromLexOfStmt�� numberOfParams �߰����ֱ�!
		// CallExpression�� ó���ϱ� ���ؼ� parameter�� ������ �ʿ�
		// ���� : 20210728
//		for(Function func : functions) {
//			for(Statement stmt : ((CompoundStatement)func.getBody()).getBody()) {
//				System.out.println("Statement : " + stmt);
//			}
//		}

		for (Function func : functions) {
			System.out.println("\n++++++++++++++++++ << Function " + func.getName() + " >> ++++++++++++++++++");
			for (Statement stmt : ((CompoundStatement) func.getBody()).getBody()) {
				System.out.println("this is stmt : " + stmt);
				//ASTFromStatement test = new ASTFromStatement();
				System.out.print("Original : ");
				for (UdbLexemeNode lex : stmt.getRawData()) {
					System.out.print(lex.getData());
				}
				System.out.println();
				// ArrayList<Function> numOfParam;
				//test.makeASTFromLexOfStmt(stmt, numberOfParam);
			}
		}

//		InsertExprIntoStmt test;
//		test = new InsertExprIntoStmt(((CompoundStatement) functions.get(0).getBody()).getBody().get(1));
//		System.out.println(((DeclarationStatement) (((CompoundStatement) functions.get(0).getBody()).getBody().get(1))).getDeclExpression());

	}

	public static ArrayList<String> findCaller(String callee) {
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
				if (values.get(i).equals(callee)) {
					caller.add(e.getKey());
				}
			}
		}

		if (caller.size() == 0)
			return null;

		return caller;
	}

	public static Function getFunctionByName(String funcName) {
		// parameter: function's name
		// return value: Function type instance its name is funcName

		for (Function func : functions) {
			if (func.getName().equals(funcName)) {
				return func;
			}
		}

		return null;
	}

	public static void upCall(String caller, String callee) {
		Statement keyStatement = null;

		// find Statement that has callee in caller.
		Function func_caller = getFunctionByName(caller);

		for (Statement stmt : ((CompoundStatement) func_caller.getBody()).getBody()) {
			for (UdbLexemeNode lxmNode : stmt.getRawData()) {

				// if lxmNode has callee
				if (lxmNode.getRef_kind().equals("Call") && lxmNode.getData().equals(callee)) {
					keyStatement = stmt;
				}
			}
		}

		System.out.println("\n================ upCall(" + caller + ") ================");
		System.out.println(keyStatement);

		// traverse start from before keyStatement.
		// traverse(keyStatement.getBefore());
		System.out.println("================ Finish upCall(" + caller + ") ================\n");
	}

	public static void downCall(Function func) {
		// parameter: Function that want to traverse.

		getVisitedCall().put(func.getName(), true);

		// specify parameter of traverse from func
		System.out.println("\n============== downCall(" + func.getName() + ") ==============");
		traverse(((CompoundStatement) func.getBody()).getLast());
		System.out.println("================ Finish downCall(" + func.getName() + ") ================\n");

	}

	public static Function isUserDefinedCall(String funcName) {
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

	public static void printUnvisitedStmt(Statement stmt) {

		/* 1. Check if stmt in hashMap(visitedStmt) */
		if (getVisitedStmt().containsKey(stmt)) {
			return; // not print
		}

		/* 2. Print this Statement */
		System.out.println(stmt);

		/* 3. Add Stmt to hashMap */
		getVisitedStmt().put(stmt, true);
	}

	public static void traverse(Statement bottomStatment) {
		// parameter: last Statement in a function's body.
		Stack<Statement> traverseStack = new Stack<Statement>();
		Boolean printFlag = false;

		/* 1. Start from bottomNode(bottomStatement) */
		Statement currentStatement = bottomStatment;
		while (true) {

			/* 2. Print all Nodes(Statements) in function(reversed order) */
			if (!getVisitedStmt().containsKey(currentStatement)) {
				if (!(currentStatement instanceof BranchStatement)) {
					printUnvisitedStmt(currentStatement);
					printFlag = true;
				} else {
					BranchStatement bstmt = (BranchStatement) currentStatement;

					if (getVisitedStmt().containsKey(((CompoundStatement) bstmt.getThen()).getFirst())) {
						if (!getVisitedStmt().containsKey(bstmt.getEls())
								|| getVisitedStmt().containsKey(((CompoundStatement) bstmt.getEls()).getFirst())) {
							printUnvisitedStmt(currentStatement);
							printFlag = true;
						}
					}
				}

			}

			if (printFlag) {
				/* 3. push before statement to traverse stack */
				if (currentStatement.getBefore() != null) {
					for (int i = 0; i < currentStatement.getBefore().size(); i++) {
						traverseStack.push(currentStatement.getBefore().get(i));
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

//         try {
//            Thread.sleep(1000);
//         } catch (InterruptedException ex) {
//            //
//         }

			/* 5. Find next Node(Statement) */
			if (traverseStack.empty())
				break;
			currentStatement = traverseStack.pop();

		}
	}

	public static void printFunctionList(ArrayList<String> funcNames) {

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

	public static ArrayList<UdbLexemeNode> hasCall(Statement stmt) {
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

	public static void printCallGraph(HashMap<String, ArrayList<String>> callgraph) {

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

	private static HashMap<String, ArrayList<String>> makeCallGraph(ArrayList<Function> functions) {
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

//				//find kind of Call( UserDefined or Library)
//	            ExpressionStatement estmt = (ExpressionStatement) stmt;
//	
//	            String typeOfStmt;
//	            if (estmt.getExpression() instanceof UserDefinedCallExpression) { //instanceof UserDefinedCall
//	               typeOfStmt = "UserDefinedCall";
//	            } else { // instanceof LibraryCall
//	               typeOfStmt = "LibraryCall";
//	            }

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

	public static HashMap<String, ArrayList<String>> getCallGraph() {
		return callGraph;
	}

	public static void setCallGraph(HashMap<String, ArrayList<String>> callGraph) {
		TestCFG.callGraph = callGraph;
	}

	public static ArrayList<Function> getFunctions() {
		return functions;
	}

	public static void setFunctions(ArrayList<Function> functions) {
		TestCFG.functions = functions;
	}

	public static Stack<String[]> getUpStack() {
		return upStack;
	}

	public static void setUpStack(Stack<String[]> upStack) {
		TestCFG.upStack = upStack;
	}

	public static HashMap<Statement, Boolean> getVisitedStmt() {
		return visitedStmt;
	}

	public static void setVisitedStmt(HashMap<Statement, Boolean> visitedStmt) {
		TestCFG.visitedStmt = visitedStmt;
	}

	public static HashMap<String, Boolean> getVisitedCall() {
		return visitedCall;
	}

	public static void setVisitedCall(HashMap<String, Boolean> visitedCall) {
		TestCFG.visitedCall = visitedCall;
	}
}