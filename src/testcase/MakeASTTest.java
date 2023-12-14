package testcase;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.Stack;

import syntax.global.Define;
import syntax.global.Include;
import syntax.statement.BranchStatement;
import syntax.statement.CompoundStatement;
import syntax.statement.DeclarationStatement;
import syntax.statement.Function;
import syntax.statement.Statement;
import traverse.general.CodeFromStmtTraverser;
import traverse.general.ExprTraverser;
import traverse.general.ForwardCfgTraverser;
import udbparser.udbrawdata.UdbLexemeNode;
import udbparser.usingperl.UdbInfoRetriever;


public class MakeASTTest {
	private static ArrayList<Function> functions;
	private static Stack<String[]> upStack;
	private static HashMap<String, ArrayList<String>> callGraph;
	private static HashMap<Statement, Boolean> visitedStmt;
	private static HashMap<String, Boolean> visitedCall;

	public static void main(String[] args) throws InterruptedException, IOException {
		UdbInfoRetriever udbInfoRetriever = new UdbInfoRetriever();

		String srcPath = "./src/ALautomation/code/cdplayer/cdplayer.c";//crowdquake/crowdquake.c";//Test_Example/projector_AL/model.c";//hlsimple.c";//Test_Example/projector_AL/model.c";//collision_avoidance.c";

		udbInfoRetriever.makeFunctionsFromUDB(srcPath); //Perl -> Lexeme & Variable print

		setFunctions(udbInfoRetriever.getFunctions());
		setCallGraph(makeCallGraph(functions));
		printCallGraph(getCallGraph()); //Caller Callee 출력

		HashMap<Function, Integer> numberOfParam = new HashMap<Function, Integer>();
		HashMap<Function, Boolean> typeOfFunc = new HashMap<Function, Boolean>(); //chl@2023.02.17 - check for function type
		for (Function f : getFunctions()) { //f: add, main, div, sub, multiple, etc.
			if (f.getParameters() == null)
				numberOfParam.put(f, 0);
			else
				numberOfParam.put(f, f.getParameters().size());
			typeOfFunc.put(f, f.returnLibraryType());
		}
		System.out.println(numberOfParam); //including f from above for loop
		


		ArrayList<String> defines = new ArrayList<String>();
		for (Define d : udbInfoRetriever.getDefines()){//chl@2023.02.23 - define list make
			defines.add(d.getName());
		}

		
		/*chl@2023.02.08 - Expression make*/
		ExprTraverser exprtrav = new ExprTraverser(numberOfParam, typeOfFunc, defines); 
		//CodeFromStmtTraverser dfscode = new CodeFromStmtTraverser();
		System.out.println("Expression assignment Taverse: ");
		for(Function f : getFunctions()) {
			/*chl@2023.02.08 - Expression make*/
			System.out.println(f);
			exprtrav.traverse(f);
			//dfscode.traverse(f);
			System.out.println();
		}
		System.out.println();
		
		
		
		
  		/*chl@2023.02.08 - cfg traverse*/
		ForwardCfgTraverser testcfg = new ForwardCfgTraverser();
		for(Function f : getFunctions()) {
			System.out.println("Statement Traverse: ");
			
			testcfg.traverse(f);
			System.out.println();
		}
		
		
		/*chl@2023.02.08 - code print*/
		//CodeFromStmtTraverser dfscode = new CodeFromStmtTraverser();
		System.out.println("Make Code Traverse: ");

		for (Include i : udbInfoRetriever.getHeaders()){//chl@2023.02.23 - include print
			System.out.println(i.getRawdata());
		}
		System.out.println();
		for (Define d : udbInfoRetriever.getDefines()){//chl@2023.02.23 - define print
			System.out.println(d.getRawdata());
		}
		System.out.println();
		for (DeclarationStatement s : udbInfoRetriever.getGlobals()){//chl@2023.02.23 - global variable print
			s.setExpression(numberOfParam, typeOfFunc, defines);
			CodeFromStmtTraverser test = new CodeFromStmtTraverser();
			test.traverse(s);
		}
		System.out.println();

		CodeFromStmtTraverser dfscode = new CodeFromStmtTraverser();
		for(Function f : getFunctions()) {			
			dfscode.traverse(f);
			System.out.println();
		}
		
	}

	public static ArrayList<String> findCaller(String callee) {
		ArrayList<String> caller = new ArrayList<String>();

		Set<Entry<String, ArrayList<String>>> set = getCallGraph().entrySet();
		Iterator<Entry<String, ArrayList<String>>> itr = set.iterator();
		while (itr.hasNext()) {
			Map.Entry<String, ArrayList<String>> e = (Map.Entry<String, ArrayList<String>>) itr.next();
			if (e.getKey().equals(callee))
				continue; 
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
		for (Function func : functions) {
			if (func.getName().equals(funcName)) {
				return func;
			}
		}

		return null;
	}

	public static void upCall(String caller, String callee) {
		Statement keyStatement = null;

		Function func_caller = getFunctionByName(caller);

		for (Statement stmt : ((CompoundStatement) func_caller.getBody()).getBody()) {
			for (UdbLexemeNode lxmNode : stmt.getRawData()) {

				if (lxmNode.getRef_kind().equals("Call") && lxmNode.getData().equals(callee)) {
					keyStatement = stmt;
				}
			}
		}

		System.out.println("\n================ upCall(" + caller + ") ================");
		System.out.println(keyStatement);

		System.out.println("================ Finish upCall(" + caller + ") ================\n");
	}

	public static void downCall(Function func) {
		getVisitedCall().put(func.getName(), true);
		System.out.println("\n============== downCall(" + func.getName() + ") ==============");
		traverse(((CompoundStatement) func.getBody()).getLast());
		System.out.println("================ Finish downCall(" + func.getName() + ") ================\n");

	}

	public static Function isUserDefinedCall(String funcName) {
		for (Function func : getFunctions()) {
			if (func.getName().equals(funcName)) {
				return func;
			}
		}
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
		MakeASTTest.callGraph = callGraph;
	}

	public static ArrayList<Function> getFunctions() {
		return functions;
	}

	public static void setFunctions(ArrayList<Function> functions) {
		MakeASTTest.functions = functions;
	}

	public static Stack<String[]> getUpStack() {
		return upStack;
	}

	public static void setUpStack(Stack<String[]> upStack) {
		MakeASTTest.upStack = upStack;
	}

	public static HashMap<Statement, Boolean> getVisitedStmt() {
		return visitedStmt;
	}

	public static void setVisitedStmt(HashMap<Statement, Boolean> visitedStmt) {
		MakeASTTest.visitedStmt = visitedStmt;
	}

	public static HashMap<String, Boolean> getVisitedCall() {
		return visitedCall;
	}

	public static void setVisitedCall(HashMap<String, Boolean> visitedCall) {
		MakeASTTest.visitedCall = visitedCall;
	}
}
