package testcase;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import syntax.statement.Function;
import udbparser.usingperl.UdbInfoRetriever;
import util.NewFunctionCFGPrinter;
import util.SourceCode;

public class NewTestAutomation {
	private ArrayList<Function> functions;
	private ArrayList<String> OriginalCode;
//	private ArrayList<String> Macros;
	private SourceCode CFGcreatecode;

	public static void main(String[] args) throws InterruptedException, IOException {
		long start_time = System.currentTimeMillis();
		String filepath = "./data/filelist.xlsx";
		
		File file = new File(filepath);
		FileInputStream inputStream = new FileInputStream(file);
		XSSFWorkbook xworkbook = new XSSFWorkbook(inputStream);
		
		XSSFSheet curSheet;
		XSSFRow curRow;
		
		curSheet = xworkbook.getSheetAt(0);
		
		int row = curSheet.getPhysicalNumberOfRows();
		
		for(int i = 1; i < row; i++) {
			String filename = new String();
			curRow = curSheet.getRow(i);
			filename = String.valueOf(curRow.getCell(0));
			
			NewTestAutomation NewTestAutomation = new NewTestAutomation();
			NewFunctionCFGPrinter functionCFGPrinter = new NewFunctionCFGPrinter();
			UdbInfoRetriever udbInfoRetriever = new UdbInfoRetriever();
			
			NewTestAutomation.setOriginalCode(new ArrayList<String>());
			
			udbInfoRetriever.makeFunctionsFromUDB(filename);
			NewTestAutomation.setFunctions(udbInfoRetriever.getFunctions());
			NewTestAutomation.setCFGcreatecode(new SourceCode());
			
			//NewTestAutomation.setMacros(udbInfoRetriever.getMacros());
			
			int index = 1;
			System.out.println("<<Function List>>");
			for(Function f : NewTestAutomation.getFunctions()) {
				System.out.println((index++) + " : " + f.getName());
			}
			
			int FunctionNumber = 1;
			for(Function f : NewTestAutomation.getFunctions()) {
				NewTestAutomation.FileIO(filename, f.getStart_line(), f.getEnd_line());
				
				functionCFGPrinter.printFunction2(f);
				NewTestAutomation.setCFGcreatecode(functionCFGPrinter.getCode());
				System.out.println();
				
				NewTestAutomation.setOriginalCode(NewTestAutomation.PreprocessingCode(NewTestAutomation.getOriginalCode()));
				NewTestAutomation.getCFGcreatecode().setCode(NewTestAutomation.PreprocessingCode(NewTestAutomation.getCFGcreatecode().getCode()));
				
				boolean result;
				if(NewTestAutomation.getCFGcreatecode().getCode().size() <= 1) result = true;
				else result = NewTestAutomation.CompareStatementList(NewTestAutomation.getOriginalCode(), NewTestAutomation.getCFGcreatecode().getCode());
				System.out.println("Code Flow Comparison : " + result);
				if(result) curRow.createCell(FunctionNumber++).setCellValue(f.getName() + " : pass");
				else curRow.createCell(FunctionNumber++).setCellValue(f.getName() + " : fail");
				
				FileOutputStream outputStream = new FileOutputStream(file);
				xworkbook.write(outputStream);
			}
		}
		
		long end_time = System.currentTimeMillis();
		System.out.println("=============================== Execution time ===============================");
		int minute = (int) (((end_time - start_time) / 1000.0) / 60);
		long second = (long) (((end_time - start_time) / 1000.0) % 60);
		System.out.println(minute + " minutes " + second + " seconds");
	}

	public void FileIO(String srcpath, int startline, int endline) {
		ArrayList<String> FunctionCode;
		//boolean macro_flag = false;
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
		for(int i=0; i<code.size(); i++) {
			code.set(i, code.get(i).replaceAll("[\\\n\\\t ;]", ""));
			code.set(i, code.get(i).replaceAll("\\\\n", ""));
		}

		for(String str : code) {
			if(!(str.equals(""))) preprocessinglist.add(str);
		}
		
		return preprocessinglist;
	}
	
	// 기존의 c코드와 생성된 CFG 노드의 statement list 비교하기
	public boolean CompareStatementList(ArrayList<String> listA, ArrayList<String> listB) {
		boolean result = true;
		String lista = "";
		String listb = "";
		String[] FinalListA = null;
		String[] FinalListB = null;
		
		
		for(String str : listA) lista = lista + str;
		for(String str : listB) listb = listb + str;
		
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
//		return Macros;
//	}
//
//	public void setMacros(ArrayList<String> macros) {
//		Macros = macros;
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
