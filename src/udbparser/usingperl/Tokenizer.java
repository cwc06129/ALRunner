package udbparser.usingperl;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;

import syntax.global.Define;
import syntax.global.Include;
import syntax.statement.DeclarationStatement;
import syntax.statement.Function;
import syntax.variable.Variable;
import udbparser.udbrawdata.UdbCFGNode;
import udbparser.udbrawdata.UdbLexemeNode;

public class Tokenizer {
    private HashSet<Variable> funcVars = new HashSet<Variable>();
    // 2023-02-23(Thu) SoheeJung
    // tokenizeCFG & tokenizeGlobal use this shared variable
    private String filename;

    public ArrayList<Variable> tokenizeParams(String response) {
        if (response.equals("void")) return null;

        ArrayList<Variable> params = new ArrayList<Variable>();
        String[] vars = response.split(",");
        for (int i = 0; i < vars.length; i++) {
            String[] var = vars[i].split(" ");
            if (var[0].equals("")) {
                var[0] = "int";
            }


            String type = "";
            for (int j = 0; j < var.length - 1; j++) {
                type += var[j];
                if(j != var.length-2){
                    type += " ";
                }
            }
            params.add(new Variable(type, var[var.length - 1], "Parameter"));

        }

        return params;
    }


   public ArrayList<Function> tokenizeFunc(String response) {
      // response = ( <FUNCTION>return type:name:parameters:start_line:end_line</FUNCTION> )*
      // Tokenizing response into ArrayList<String>

      String[] func_infos = response.split("<FUNCTION>|</FUNCTION>");
      if(func_infos.length == 0){
         return null;
      }

      ArrayList<Function> funcs = new ArrayList<Function>();
      
      for(int i = 1; i < func_infos.length; i = i + 2){
         String[] func_info = func_infos[i].split(":");

         String return_type = func_info[0];
         if(func_info[0].equals("")){
            return_type = "void";
         }
         String name = func_info[1];
         ArrayList<Variable> params = tokenizeParams(func_info[2]);
         int start_line = Integer.parseInt(func_info[3]);
         int end_line = Integer.parseInt(func_info[4]);
         //System.out.println("parameter : " + params);
         funcs.add(new Function(return_type, name, params, start_line, end_line));
      }

      return funcs;
   }
   
   // 2023-02-22(Wed) SoheeJung
   // Tokenizing response into ArrayList<Include>
   // This is about "#include" line
   public ArrayList<Include> tokenizeHeader(String response) {
	      // response = ( <HEADER>header_name</HEADER> )*

	      String[] header_infos = response.split("<HEADER>|</HEADER>");
	      if(header_infos.length == 0){
	         return null;
	      }

	      ArrayList<Include> headers = new ArrayList<Include>();
	      
	      for(int i = 1; i < header_infos.length; i = i + 2){
	         String type = "#include";
	         String value = header_infos[i];
	         headers.add(new Include(type, value));
	      }

	      return headers;
   }
   
   // 2023-02-22(Wed) SoheeJung
   // Tokenizing response into ArrayList<Define>
   // This is about "#define" line
   public ArrayList<Define> tokenizeDefine(String response) {
	      // response = ( <MACRO>name:parameters:value</MACRO> )*

	      String[] define_infos = response.split("<MACRO>|</MACRO>");
	      if(define_infos.length == 0){
	         return null;
	      }

	      ArrayList<Define> defines = new ArrayList<Define>();
	      
	      for(int i = 1; i < define_infos.length; i = i + 2){
	    	 String[] define_info = define_infos[i].split(":");
	         
	    	 String type = "#define";
	         String name = define_info[0];
	         // if "#define" line have parameter, then return this values.
	         // But There is not parameter, then return null.
	         String parameter = "";
            String value = "";
	         if (define_info.length > 1){
               if (define_info[1] == "") {
                  parameter = null;
               } else {
                  parameter = define_info[1];
               }
               value = define_info[2];
            } else {
               parameter = null;
               value = null;
            }
	         defines.add(new Define(type, name, parameter, value));
	      }

	      return defines;
   }
   
   // 2023-02-22(Wed) SoheeJung
   // Tokenizing response into ArrayList<DeclarationStatement>
   // This is about global variable declaration statement part
   public ArrayList<DeclarationStatement> tokenizeGlobal(String response) {
	      // response = ( <GLOBAL>type:name:value</GLOBAL> )*

	      String[] global_infos = response.split("<GLOBAL>|</GLOBAL>");
	      if(global_infos.length == 0){
	         return null;
	      }

	      ArrayList<DeclarationStatement> globalStatements = new ArrayList<DeclarationStatement>();
	      
	      for(int i = 1; i < global_infos.length; i = i + 2){
	    	 String[] global_info = global_infos[i].split(":");
	         
	    	 String type = "";
	    	 String name = "";
	    	 String value = "";
	  
	    	 if (global_info.length == 2) {
		    	 type = global_info[0];
		         name = global_info[1];
		         value = null;
	    	 } else {
	    		 type = global_info[0];
		         name = global_info[1];
		         value = global_info[2];
	    	 }
	         
	         /* Make Declaration Statement */
	         DeclarationStatement globalDeclarationStatement = new DeclarationStatement();
	         
	         // if globalVar is not in funcVars, then add this value into funcVars
	         Variable globalVar = new Variable(type, name, filename, "Global");
	         if((globalVar != null) && !funcVars.contains(globalVar)){
                this.funcVars.add(globalVar);
            }
	         
	        // Make UdbLexemNode
	        UdbLexemeNode globalLexemeType = new UdbLexemeNode(type, "NULL", "Keyword", null);
	        UdbLexemeNode globalLexemeSpace = new UdbLexemeNode(" ", "NULL", "Whitespace", null);
	        UdbLexemeNode globalLexemeName = new UdbLexemeNode(name, "Init", "Identifier", globalVar);	        
	        UdbLexemeNode globalLexemePunctuation = new UdbLexemeNode(";", "NULL", "Punctuation", null);
	        
	        // Make globalDeclarationStatement
	        globalDeclarationStatement.addLexemeNode(globalLexemeType);
	        globalDeclarationStatement.addLexemeNode(globalLexemeSpace);
	        globalDeclarationStatement.addLexemeNode(globalLexemeName);
	        globalDeclarationStatement.addLexemeNode(globalLexemeSpace);
	        if (value != null) {
	        	UdbLexemeNode globalLexemeOperator = new UdbLexemeNode("=", "NULL", "Operator", null);
		        UdbLexemeNode globalLexemeValue = new UdbLexemeNode(value, "NULL", "Literal", null);
	        	globalDeclarationStatement.addLexemeNode(globalLexemeOperator);
		        globalDeclarationStatement.addLexemeNode(globalLexemeSpace);
		        globalDeclarationStatement.addLexemeNode(globalLexemeValue);
	        }
	        globalDeclarationStatement.addLexemeNode(globalLexemePunctuation);
	        globalDeclarationStatement.setRawData(globalDeclarationStatement.getRawData());
	        
	        globalStatements.add(globalDeclarationStatement);
	      }

	      return globalStatements;
   }
   
   public ArrayList<UdbCFGNode> tokenizeCFG(String response) {
      ArrayList<UdbCFGNode> CFGList = new ArrayList<UdbCFGNode>();

      /* 1. Statement tag */
      String[] stmtNodes = response.split("<Statement>|</Statement>");
      for(int k = 1; k < stmtNodes.length; k = k+2) {

         String stmtNode = stmtNodes[k];
         String[] cfgNode = stmtNode.split("<CFG>|</CFG>");
         // cfgNode[0] = null, cfgNode[1]= CFG info, cfgNode[2]= Lexemes

         /* 2. CFG tag(UdbCFGNode) */
         String[] arr2 = cfgNode[1].split(":");
         int[] nums = Arrays.asList(arr2).stream().mapToInt(Integer::parseInt).toArray();
         // esn(end)
         UdbCFGNode CFGElement = new UdbCFGNode(nums[0], nums[1], nums[2], nums[3], nums[4], nums[5]-1);

         // Get children
         // if response string of children is 2:3:4
         // children UdbCFGNode will be CFGList[1], CFGList[2], CFGList[3]
         ArrayList<Integer> children = new ArrayList<Integer>();
         for(int i = 6; i < nums.length; i++) {
            children.add(nums[i]-1);   // subtract 1 because array index start from 0.
         }
         CFGElement.setChild_nodes(children);

         /* 3. Lexemes tag(UdbLexemeNode) */
         CFGElement.setContents(tokenizeLexeme(cfgNode[2]));
         CFGList.add(CFGElement);
      }
      
      return CFGList;
   }

   public ArrayList<UdbLexemeNode> tokenizeLexeme(String str) {
      ArrayList<UdbLexemeNode> lexemeList = new ArrayList<UdbLexemeNode>();
      
      // str = ( data:reference_kind:token@ )*
      // Tokenizing str into UdbLexemeNode
      String[] lexemes = str.split("<Lexemes>|</Lexemes>");

      String[] lexeme = lexemes[1].split("<Lexeme>|</Lexeme>");
         for(int j = 1; j < lexeme.length; j += 2){

            String[] text_tag = lexeme[j].split("<text>|</text>");
            String text = text_tag[1];

            String[] tokenkind_tag = text_tag[2].split("<tokenkind>|</tokenkind>");
            String tokenkind = tokenkind_tag[1];

            String[] refkind_tag = tokenkind_tag[2].split("<refkind>|</refkind>");
            String refkind = refkind_tag[1];

            String[] file_tag = refkind_tag[2].split("<file>|</file>");
            String file = file_tag[1];
            filename = file;

            String[] scope_tag = file_tag[2].split("<scope>|</scope>");
            String scope = scope_tag[1];

            String[] type_tag = scope_tag[2].split("<type>|</type>");
            StringBuilder type = new StringBuilder(type_tag[1]);

            if(type.toString().contains("[")){
                String[] tmp = type.toString().split("\\[");
                String new_type = tmp[0];
                for(int i = 1; i < tmp.length; i++){
                    new_type += "*";
                }
                type = new StringBuilder(new_type);
            }

            Variable var = null;
            if(scope.equals("Global Object")){
               var = new Variable(type.toString(), text, file, "Global");
            }
            else if(scope.equals("Local Object")){
               var = new Variable(type.toString(), text, file, "Local");
            }
            else if(scope.equals("Parameter")){
                var = new Variable(type.toString(), text, file, "Parameter");
            }
            else if(scope.equals("Public Object")){
                var = new Variable(type.toString(), text, file, "Public");
            }
            // 占쏙옙占� lexeme data占쏙옙 占쏙옙占쌔쇽옙 UdbLexemeNode variable占쏙옙 占쏙옙占쏙옙占쏙옙娩占�.(20210730)
            else if(scope.equals("Typedef")){
            	var = new Variable(type.toString(), text, file, scope);
            }
            else if(type.toString().contains("struct")) {
            	var = new Variable(type.toString(), text, file, scope);
            }
            UdbLexemeNode lexemeElement = new UdbLexemeNode(text, refkind, tokenkind, var);
            lexemeList.add(lexemeElement);

            if((var != null) && !funcVars.contains(var)){
                this.funcVars.add(var);
            }
         }
      System.out.println(lexemeList);
      return lexemeList;
   }

    public HashSet<Variable> getFuncVars() {
        return funcVars;
    }

    public void setFuncVars(HashSet<Variable> funcVars) {
        funcVars = funcVars;
    }


	public String getFilename() {
		return filename;
	}


	public void setFilename(String filename) {
		this.filename = filename;
	}
}