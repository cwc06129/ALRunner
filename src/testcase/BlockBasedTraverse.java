package testcase;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Scanner;
import java.util.Set;

import syntax.statement.CompoundStatement;
import syntax.statement.Function;
import syntax.statement.Statement;
import traverse.general.ForwardCfgTraverser;
import udbparser.udbrawdata.UdbLexemeNode;
import udbparser.usingperl.UdbInfoRetriever;
import util.FunctionCFGPrinter;

public class BlockBasedTraverse {
	private ArrayList<Function> functions;
	private HashMap<String, ArrayList<String>> callGraph;
	
	public static void main(String[] args) throws InterruptedException, IOException {
		long start = System.currentTimeMillis(); //시작하는 시점 계산

		BlockBasedTraverse blockbasedtraverse = new BlockBasedTraverse();
		ForwardCfgTraverser traverser = new ForwardCfgTraverser();
		FunctionCFGPrinter cfgprint = new FunctionCFGPrinter();
		
		UdbInfoRetriever udbInfoRetriever = new UdbInfoRetriever();
		
		String srcPath = "./data/injuntest/soheetest.c";
		udbInfoRetriever.makeFunctionsFromUDB(srcPath);
		blockbasedtraverse.setFunctions(udbInfoRetriever.getFunctions());
		
		blockbasedtraverse.setFunctions(udbInfoRetriever.getFunctions());
		blockbasedtraverse.setCallGraph(blockbasedtraverse.makeCallGraph(blockbasedtraverse.getFunctions()));
		blockbasedtraverse.printCallGraph(blockbasedtraverse.getCallGraph());
		
		int index = 1;
		System.out.println("<<Function List>>");
		for(Function f : blockbasedtraverse.getFunctions()) {
			System.out.println((index++) + " : " + f.getName());
		}
		System.out.println();

		long end = System.currentTimeMillis(); //프로그램이 끝나는 시점 계산
		System.out.println("=============================== Execution time ===============================");
		System.out.println(( end - start )/1000.0 + "seconds");
		System.out.println();
		
		Scanner keyboard = new Scanner(System.in);
		System.out.printf("Choose number from functionlist > ");
		int choice = keyboard.nextInt();
		Function startFunc = blockbasedtraverse.getFunctions().get(choice - 1);
		// FunctionCFGPrinter에 담겨 있는 출력 결과는 차후에 비교해보기
		// 우선은 제대로 traverse가 되는지를 먼저 testing하는 것이 맞음
		// 20210903
//		cfgprint.printCFG2(startFunc);
//		System.out.println();
//		cfgprint.printFunction2(startFunc);
		
		System.out.println("\n\n============== Control Flow of " + startFunc.getName() + " ==============\n\n");
		traverser.traverse(startFunc);
		System.out.println("\n\n============== Finish Control Flow of " + startFunc.getName() + "==============");
	}

	public void printCallGraph(HashMap<String, ArrayList<String>> callgraph) {
		System.out.println("\n================= Caller : Callee =================\n");
		
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
				if(i != values.size() - 1) {
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
		
		for(UdbLexemeNode lxmnode : stmt.getRawData()) {
			if(lxmnode.getRef_kind().equals("Call")) {
				lexemes.add(lxmnode);
			}
		}
		
		if(lexemes.size() == 0) return null;
		
		return lexemes;
	}

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
}
