package testcase;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

import syntax.statement.Function;
import udbparser.usingperl.UdbInfoRetriever;
import util.FunctionCFGNextPrinter;
import util.NewFunctionCFGPrinter;
import util.SourceCode;

public class TestAutomationEliminateComment {
	private ArrayList<Function> functions;
	private ArrayList<String> OriginalCode;
	private SourceCode CFGcreatecode;
//	private ArrayList<String> macros;

	public static void main(String[] args) throws InterruptedException, IOException {
		TestAutomationEliminateComment testAutomation = new TestAutomationEliminateComment();
		NewFunctionCFGPrinter functionCFGPrinter = new NewFunctionCFGPrinter();
		UdbInfoRetriever udbInfoRetriever = new UdbInfoRetriever();
		FunctionCFGNextPrinter nextprinter = new FunctionCFGNextPrinter();
		
		String CSrcPath = "./data/injuntest/hlsimple.c";
		testAutomation.setOriginalCode(new ArrayList<String>());
		
		udbInfoRetriever.makeFunctionsFromUDB(CSrcPath);
		testAutomation.setFunctions(udbInfoRetriever.getFunctions());
		testAutomation.setCFGcreatecode(new SourceCode());
		
//		testAutomation.setMacros(udbInfoRetriever.getMacros());
		
		int index = 1;
		System.out.println("<<Function List>>");
		for(Function f : testAutomation.getFunctions()) {
			System.out.println((index++) + " : " + f.getName());
		}
		
		Scanner keyboard = new Scanner(System.in);
		System.out.printf("Choose number from functionlist > ");
		int choice = keyboard.nextInt();
		Function startFunc = testAutomation.getFunctions().get(choice - 1);
		
		testAutomation.FileIO(CSrcPath, startFunc.getStart_line(), startFunc.getEnd_line());
		
		System.out.println("\nthis is next statement!!!!");
		nextprinter.printAboutFunction(startFunc);
		System.out.println("\n");
		
		functionCFGPrinter.printFunction2(startFunc);
		testAutomation.setCFGcreatecode(functionCFGPrinter.getCode());
		System.out.println();
		functionCFGPrinter.printCFG2(startFunc);
		//functionCFGPrinter.printFunctionBody(startFunc);
		
		// ArrayList compare�� ���� preprocessing ������ ��ģ��.
//		for(String str : testAutomation.getCFGcreatecode().getCode()) System.out.println(str);
		
		testAutomation.setOriginalCode(testAutomation.PreprocessingCode(testAutomation.getOriginalCode()));
		testAutomation.getCFGcreatecode().setCode(testAutomation.PreprocessingCode(testAutomation.getCFGcreatecode().getCode()));
		
		for(String str : testAutomation.getOriginalCode()) System.out.println("origin : " + str);
		System.out.println();
		for(String str : testAutomation.getCFGcreatecode().getCode()) System.out.println("cfg : " + str);
		
		// �� ���� ArrayList ���ϱ�
		
		boolean result;
		
		if(testAutomation.getCFGcreatecode().getCode().size() <= 1) result = true;
		else result = testAutomation.CompareStatementList(testAutomation.getOriginalCode(), testAutomation.getCFGcreatecode().getCode());
		System.out.println("Code Flow Comparison : " + result);
		
		testAutomation.PreprocessingCode(testAutomation.getOriginalCode());
	}


	public void FileIO(String srcpath, int startline, int endline) {
		ArrayList<String> FunctionCode;
		boolean macro_flag = false;
		int count = 0;
		
		try {
			File file = new File(srcpath);
			FileReader filereader = new FileReader(file); 
			BufferedReader inFile = new BufferedReader(filereader);
			FunctionCode = new ArrayList<String>();
			
			System.out.println("start : " + startline);
			System.out.println("end : " + endline);
			
			if(file.exists()) {
				String line = null;
				while((line = inFile.readLine()) != null) {
					String[] SplitArray = null;
					count++;
					if(count < startline) continue;
					else if(count <= endline) {
						System.out.println("line : " + line);
						line = line.replaceAll("[\\t \\n]", "");
						if((line.length() >= 2) && (line.substring(0, 2).equals("//"))) continue;
						
//						if((line.length() >= 2) && (line.substring(0, 2).equals("/*"))) {
//							comment_flag = true;
//						}
//						if(comment_flag && (line.length() >= 2) && (line.substring(line.length() - 2, line.length()).equals("*/"))) {
//							comment_flag = false;
//							continue;
//						}
//						if(line.length() > 0 && comment_flag && line.charAt(0) == '*') continue;
//						
//						if(line.contains("/*") && line.contains("*/")) {
//							SplitArray = line.split("/[/*]");
//							for(String str : SplitArray) System.out.println("string : " + str);
//							line = SplitArray[0];
//						}
						
						if(line.contains("//")) {
							SplitArray = line.split("[//]");
							for(String str : SplitArray) System.out.println("string : " + str);
							line = SplitArray[0];
						}
//						if(line.contains("#ifndef")) {
//							SplitArray = line.split("#ifndef");
//							if(getMacros().contains(SplitArray[1]))
//								macro_flag = true;
//							continue;
//						}
//						if(line.contains("#ifdef")) {
//							SplitArray = line.split("#ifdef");
//							if(!(getMacros().contains(SplitArray[1])))
//								macro_flag = true;
//							continue;
//						}
//						if(line.contains("#endif")) {
//							macro_flag = false;
//							continue;
//						}
//						if(!macro_flag) FunctionCode.add(line);
						FunctionCode.add(line);
						
					}
					else if(count > endline) break;
				}
			}
			
			setOriginalCode(PreprocessingCode(FunctionCode));	
			
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public ArrayList<String> PreprocessingCode(ArrayList<String> code) {
		ArrayList<String> preprocessinglist = new ArrayList<String>();
		String[] SplitArray = null;
		for(int i=0; i<code.size(); i++) {
			code.set(i, code.get(i).replaceAll("[\\\n\\\t ;]", ""));
			code.set(i, code.get(i).replaceAll("\\\\n", ""));
		}

		for(String str : code) {
			if(!(str.equals(""))) preprocessinglist.add(str);
		}
		
		return preprocessinglist;
	}
	
	// ������ c�ڵ�� ������ CFG ����� statement list ���ϱ�
	public boolean CompareStatementList(ArrayList<String> listA, ArrayList<String> listB) {
		boolean result = true;
		String lista = "";
		String listb = "";
		String[] FinalListA = null;
		String[] FinalListB = null;
		
		
		for(String str : listA) 
		{
			System.out.println("listA : " + str);
			lista = lista + str;
		}
		for(String str : listB)
		{
			System.out.println("listB : " + str );
			listb = listb + str;
		}
		
		lista = lista.substring(lista.indexOf('{'), lista.length());
		listb = listb.substring(listb.indexOf('{'), listb.length());
		
		lista = lista.replaceAll("[{}]", "");
		listb = listb.replaceAll("[{}]", "");
		
		FinalListA = lista.split("/\\*|\\*/");
		FinalListB = listb.split("/\\*|\\*/");
		
		lista = "";
		listb = "";
		
		for(int i = 0; i < FinalListA.length; i = i + 2) lista = lista + FinalListA[i];
		for(int i = 0; i < FinalListB.length; i = i + 2) listb = listb + FinalListB[i];
		
		System.out.println(lista);
		System.out.println(listb);
		
		if(lista.equals(listb)) result = true;
		else result = false;
		
		return result;
	}

//	public ArrayList<String> getMacros() {
//		return macros;
//	}
//
//	public void setMacros(ArrayList<String> macros) {
//		this.macros = macros;
//	}
	
	public ArrayList<String> getOriginalCode() {
		return OriginalCode;
	}

	public void setOriginalCode(ArrayList<String> originalCode) {
		OriginalCode = originalCode;
	}
	
	public ArrayList<Function> getFunctions() {
		return functions;
	}

	public void setFunctions(ArrayList<Function> functions) {
		this.functions = functions;
	}

	public SourceCode getCFGcreatecode() {
		return CFGcreatecode;
	}

	public void setCFGcreatecode(SourceCode cFGcreatecode) {
		CFGcreatecode = cFGcreatecode;
	}
}
