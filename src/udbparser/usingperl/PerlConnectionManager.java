package udbparser.usingperl;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import syntax.global.Define;
import syntax.global.Include;
import syntax.statement.DeclarationStatement;
import syntax.statement.Function;
import syntax.variable.Variable;
import udbparser.udbrawdata.UdbCFGNode;

public class PerlConnectionManager {
	private String perlPath;
	private String udbPath;
	private Tokenizer tokenizer;
	ArrayList<HashSet<Variable>> tempVarList;

	public PerlConnectionManager(String udbPath) {
		// Get root path of java project.
		try {
			setPerlPath(new File("./Understand_perl").getCanonicalPath());
		} catch (IOException e) {
			e.printStackTrace();
		}

		// Set path of udb project
		setUdbPath(udbPath);
		tokenizer = new Tokenizer();
		tempVarList = new ArrayList<HashSet<Variable>>();
	}
	
	
	public ArrayList<Function> getAllFunc() {

		/* 1. Create perl process */
		// 1-1. Prepare command list.
		List<String> commandList =
				UnderstandCmdManager.createCmd("uperl", getPerlPath() + "/function.pl");

		// 1-2. Initialize process.
		ProcessManager pm = new ProcessManager();
		pm.createProcess(commandList);

		/* 2. Start Communication */
		// 2-1. Write to Perl process
		pm.openSender();
		pm.send(getUdbPath());
		pm.send("FUNCTION/");
		pm.closeSender();

		// 2-2. Read from Perl process *
		ArrayList<Function> result = new ArrayList<Function>();
		String line;
		pm.openReceiver();
		while ((line = pm.receive()) != null) {
			System.out.println("\n# PerlConnectionManager.getAllFunc() # " + line);
			result = tokenizer.tokenizeFunc(line); // Tokenize the response string into List<String>
		}

		pm.closeReceiver();

		/* 3. Finish Communication */
		pm.finishProcess(); // Current thread will wait until perl process has terminated.

		return result;
	}

	
	public ArrayList<ArrayList<UdbCFGNode>> getCFGs(ArrayList<Function> funcs) {

		/* 1. Create perl process */
		// 1-1. Prepare command list.
		List<String> commandList = 
				UnderstandCmdManager.createCmd("uperl", getPerlPath() + "/cfg.pl");
		
		// 1-2. Initialize process.
		ProcessManager pm = new ProcessManager();
		pm.createProcess(commandList);

		/* 2. Start Communication */
		// 2-1. Write to Perl process.
		pm.openSender();
		pm.send(getUdbPath());
		for (Function func : funcs) {
			pm.send("CFG/" + func.getName());
		}
		pm.send("FINISH/");
		pm.closeSender();

		// 2-2. Read from Perl process.
		ArrayList<ArrayList<UdbCFGNode>> result = new ArrayList<ArrayList<UdbCFGNode>>();
		pm.openReceiver();
		String line = null;
		int i = 0;
		while ((line = pm.receive()) != null) {
			//System.out.println("I'm live : " + i);
			System.out.println("# PerlConnectionManager.getCFGs("+ funcs.get(i++).getName() +") # " + line);
			System.out.println();
			ArrayList<UdbCFGNode> cfg = new ArrayList<UdbCFGNode>();
			cfg = tokenizer.tokenizeCFG(line); // tokenize the response string into List<UdbCFGNode>
			result.add(cfg); // add List<UdbCFGNode> into List<List<UdbCFGNode>>

			tempVarList.add(tokenizer.getFuncVars());

			if (line.equals("FINISHED")) {
				pm.closeReceiver();
				pm.finishProcess(); // Current thread will wait until perl process has terminated.
			}
		}
		
		return result;
	}
	
	// 2023-02-22(Wed) SoheeJung
	// get header information from udb
	public ArrayList<Include> getHeader() {

		/* 1. Create perl process */
		// 1-1. Prepare command list.
		List<String> commandList = 
				UnderstandCmdManager.createCmd("uperl", getPerlPath() + "/header.pl");
		
		// 1-2. Initialize process.
		ProcessManager pm = new ProcessManager();
		pm.createProcess(commandList);

		/* 2. Start Communication */
		// 2-1. Write to Perl process. Send UDB path.
		pm.openSender();
		pm.send(getUdbPath());
		pm.closeSender();

		// 2-2. Read from Perl process.
		ArrayList<Include> result = new ArrayList<Include>();
		pm.openReceiver();
		String line = null;
		int i = 0;
		while ((line = pm.receive()) != null) {
			System.out.println("# PerlConnectionManager.getHeaders() # " + line);
			System.out.println();
			result = tokenizer.tokenizeHeader(line); // tokenize the response string into ArrayList<Include>
		}
		
		return result;
	}
	
	// 2023-02-22(Wed) SoheeJung
	// get define information from udb
	public ArrayList<Define> getDefine() {

		/* 1. Create perl process */
		// 1-1. Prepare command list.
		List<String> commandList = 
				UnderstandCmdManager.createCmd("uperl", getPerlPath() + "/define.pl");
		
		// 1-2. Initialize process.
		ProcessManager pm = new ProcessManager();
		pm.createProcess(commandList);

		/* 2. Start Communication */
		// 2-1. Write to Perl process. Send UDB path.
		pm.openSender();
		pm.send(getUdbPath());
		pm.closeSender();

		// 2-2. Read from Perl process.
		ArrayList<Define> result = new ArrayList<Define>();
		pm.openReceiver();
		String line = null;
		int i = 0;
		while ((line = pm.receive()) != null) {
			System.out.println("# PerlConnectionManager.getDefines() # " + line);
			System.out.println();
			result = tokenizer.tokenizeDefine(line); // tokenize the response string into ArrayList<Include>
		}
		
		return result;
	}
	
	// 2023-02-22(Wed) SoheeJung
	// get define information from udb
	public ArrayList<DeclarationStatement> getGlobal() {

		/* 1. Create perl process */
		// 1-1. Prepare command list.
		List<String> commandList = 
				UnderstandCmdManager.createCmd("uperl", getPerlPath() + "/global.pl");
		
		// 1-2. Initialize process.
		ProcessManager pm = new ProcessManager();
		pm.createProcess(commandList);

		/* 2. Start Communication */
		// 2-1. Write to Perl process. Send UDB path.
		pm.openSender();
		pm.send(getUdbPath());
		pm.closeSender();

		// 2-2. Read from Perl process.
		ArrayList<DeclarationStatement> result = new ArrayList<DeclarationStatement>();
		pm.openReceiver();
		String line = null;
		int i = 0;
		while ((line = pm.receive()) != null) {
			System.out.println("# PerlConnectionManager.getGlobals() # " + line);
			System.out.println();
			result = tokenizer.tokenizeGlobal(line); // tokenize the response string into ArrayList<Include>
		}
		
		return result;
	}
	
	public HashSet<Variable> getAllVarInFile(){
		return new HashSet<Variable>() {
			{
				for(int i = 0; i < tempVarList.size(); i++){
					addAll(tempVarList.get(i));
				}
			}
		};

	}
	
	/* Getters and Setters */
	public String getPerlPath() {
		return perlPath;
	}

	public void setPerlPath(String perlPath) {
		this.perlPath = perlPath;
	}

	public String getUdbPath() {
		return udbPath;
	}

	public void setUdbPath(String udbPath) {
		this.udbPath = udbPath;
	}

	public Tokenizer getTokenizer() {
		return tokenizer;
	}

	public void setTokenizer(Tokenizer tokenizer) {
		this.tokenizer = tokenizer;
	}
}