package udbparser.usingperl;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FilenameFilter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.StringTokenizer;

import syntax.global.Define;
import syntax.global.Include;
import syntax.statement.DeclarationStatement;
import syntax.statement.Function;
import syntax.statement.Statement;
import syntax.variable.Variable;
import udbparser.cfg.CFGFromUdb;
import udbparser.udbrawdata.FileStub;
import udbparser.udbrawdata.UdbCFGNode;
import udbparser.udbrawdata.UdbLexemeNode;

public class UdbInfoRetriever {
   private ArrayList<ArrayList<UdbCFGNode>> cfgs;
   private ArrayList<Function> functions;
   // 2023-02-22(wed) SoheeJung
   // headers, defines, globals addition
   private ArrayList<Include> headers = new ArrayList<>();
   private ArrayList<Define> defines;
   private ArrayList<DeclarationStatement> globals;
   private HashSet<Variable> variables;
   private static HashMap<Statement, Boolean> visitedStmt;

   public void makeFunctionsFromUDB(String srcPath) {

      /* 1. make udb and analyze */
      convertFuncNamesInFile(srcPath); // convert TASK(n) into TASK_n in C file.
      makeUDBFromFiles(srcPath);
      PerlConnectionManager cm = new PerlConnectionManager(srcPath + ".und");
      /* 2. Get all function's name */
      functions = cm.getAllFunc();

      // 2023-02-22(Wed) SoheeJung
      // Get all header's information
      /*
       * headers = cm.getHeader();
       * for (Include header : headers) {
       * System.out.println("This is header Information # " + header.getRawdata());
       * }
       */

      // 2023-02-22(Wed) SoheeJung
      // Get all define's information
      defines = cm.getDefine();
      for (Define define : defines) {
         System.out.println("This is define Information # " + define.getRawdata());
      }

      /* 3. Get all function's CFG */
      cfgs = cm.getCFGs(functions);

      // 2023-02-22(Wed) SoheeJung
      // Get all global variable information
      globals = cm.getGlobal();
      // globals = new ArrayList<>();
      // for (DeclarationStatement decl : globals) {
      // System.out.println("This is global Statement # " + decl.getRawStringData());
      // }
      // for (int i = 0 ; i < globals.size() ; i++){
      // if (globals.get(i).getRawStringData().contains("enum "))
      // {globals.remove(globals.get(i)); i--;}
      // }
      // getGlobalInfo(srcPath);

      /* 4. Create FileStub */
      variables = cm.getAllVarInFile();
      for (Variable var : variables) {
         System.out.println("## variables : " + var);
      }
      // FileStub filestub = createFileStub(srcPath);

      /* 5. Translate design from UdbCFGNode into Function */
      for (int i = 0; i < functions.size(); i++) {
         CFGFromUdb cfgTool = new CFGFromUdb();
         functions.get(i).setBody(cfgTool.makeFunctionCFGFromUdb(cfgs.get(i), functions.get(i).getName()));
      }
      System.out.println("\n# UdbInfoRetriever # Finish translating UdbCFGNode -> Function.");

      // for (Function func : functions) {
      // filestub.convertFuncTagIntoCfg(func);
      // }

      // InsertExprIntoStmt is not fixing yet.
      /* 6. make expression list of functions */
      /*
       * for(Function function : functions) { new InsertExprIntoStmt(functions,
       * function); } System.out.
       * println("\n# UdbInfoRetriever # Finish translating Understand Lexeme Node into Expression of our design."
       * );
       */
   }

   public void makeUDBFromFiles(String filePath) {
      // parameter can be directory or just one c file.

      /* 0. Check if a file exists */
      File f = new File(filePath);
      if (!f.exists()) {
         throw new RuntimeException(
               "# UdbInfoRetriever # Error occured! Src file not found. Please check your srcPath.");
      }

      /* 1. Prepare command list. */
      String srcPath = filePath;
      String udbPath = srcPath + ".und";

      System.out.println("this is srcPath : " + srcPath);
      System.out.println("this is udbPath : " + udbPath);

      // // command: und create -db object-follower.udb -languages c++ add
      // object-follower analyze -all
      // List<String> commandList = UnderstandCmdManager.createCmd("und",
      // "create " + "-db " + udbPath + " " + "-languages c++" + " " + "add " +
      // srcPath + " " + "analyze -all");
      // /* Modification Finish */

      // 2023-05-15(Mon) SoheeJung
      // make single udb from multiple .c file
      /* Modification Start */
      // 1. remove current target file from compile file list
      ArrayList<String> srcFilePathList = splitFilePath(srcPath);
      String srcDirectory = srcFilePathList.get(0) + "/";
      // 1-1. get all files from code directory
      File dir = new File(srcDirectory);
      // 1-2. filter ".c" and ".h" files from code directory
      FilenameFilter filter = new FilenameFilter() {
         public boolean accept(File f, String name) {
            return (name.contains(".c") && !name.contains(".und"));
         }
      };
      String[] filenames = dir.list(filter);
      String allCfileString = "";

      for (String filename : filenames) {
         allCfileString += srcDirectory + filename + " ";
      }

      // command: und create -db object-follower.udb -languages c++ add
      // object-follower analyze -all
      List<String> commandList = UnderstandCmdManager.createCmd("und",
            "create " + "-db " + udbPath + " " + "-languages c++" + " " + "add " + allCfileString + "analyze -all");
      // "create " + "-db " + udbPath + " " + "-languages c++" + " " + "add " +
      // srcPath + " " + "analyze -all");
      /* Modification Finish */

      /* 2. Initialize process. */
      ProcessManager pm = new ProcessManager();
      pm.createProcess(commandList);

      System.out.println("# UdbInfoRetriever # Waiting for creating " + udbPath);

      // If you didn't open&close Receiver, finishProcess() will never returns.
      pm.openReceiver();
      pm.closeReceiver();

      int result = pm.finishProcess();
      if (result == 0) {
         System.out.println("# UdbInfoRetriever # Successfully created " + udbPath);
      } else {
         throw new RuntimeException("# UdbInfoRetriever # Error occured! Failed to create " + udbPath);
      }

   }

   public FileStub createFileStub(String srcPath) {
      List<String> list = new ArrayList<>();
      try {
         Path path = Paths.get(srcPath);
         Charset charset = StandardCharsets.UTF_8;
         list = Files.readAllLines(path, charset);
      } catch (IOException e) {
         e.printStackTrace();
      }

      int stop_line = 0;
      for (int i = 0; i < functions.size(); i++) {
         if (functions.get(i).getStart_line() != 0 && functions.get(i).getEnd_line() != 0) {
            stop_line = functions.get(i).getStart_line();
            break;
         }
      }

      FileStub fileStub = new FileStub();
      String new_data = "";
      for (int i = 0; i < stop_line - 1; i++) {
         new_data += list.get(i) + "\n";
      }
      fileStub.setDataWithOutFuncTag(new_data);

      for (int i = 0; i < functions.size(); i++) {
         if ((functions.get(i).getStart_line() != 0) && (functions.get(i).getEnd_line() != 0)) {
            new_data += "\n<FUNCTION>" + functions.get(i).getName() + "</FUNCTION>\n";
         }
      }
      fileStub.setDataWithFuncTag(new_data);

      return fileStub;
   }

   public boolean checkUnvisitedStmt(Statement stmt) {
      if (getVisitedStmt().get(stmt)) {
         return true;
      } else {
         this.visitedStmt.put(stmt, true);
         return false;
      }
   }

   public void convertFuncNamesInFile(String filePath) {
      String preprocessor = "#define TASK(n) void TASK_##n" + "\n" + "#define FUNC(n, m) k n FUNC_##k";
      String first_preprocessor = "#define TASK(n) void TASK_##n";
      File mFile = new File(filePath);
      FileInputStream fileInputStream = null;
      try {
         fileInputStream = new FileInputStream(mFile);
         BufferedReader br = new BufferedReader(new InputStreamReader(fileInputStream));
         String result = "";
         String line = "";
         while ((line = br.readLine()) != null) {
            if (line.equals(first_preprocessor)) {
               return;
            }
            if (line.contains("__extension__")) {
               line = line.replace("__extension__", "");
            }
            result = result + "\n" + line;
         }

         result = preprocessor + "\n" + result;

         mFile.delete();
         FileOutputStream fos = new FileOutputStream(mFile);
         fos.write(result.getBytes());
         fos.flush();
      } catch (IOException e) {
         e.printStackTrace();
      }
   }

   /* Getters and Setters */

   public ArrayList<Function> getFunctions() {
      return functions;
   }

   public void setFunctions(ArrayList<Function> functions) {
      this.functions = functions;
   }

   public ArrayList<ArrayList<UdbCFGNode>> getCfgs() {
      return cfgs;
   }

   public void setCfgs(ArrayList<ArrayList<UdbCFGNode>> cfgs) {
      this.cfgs = cfgs;
   }

   public HashMap<Statement, Boolean> getVisitedStmt() {
      return visitedStmt;
   }

   public void setVisitedStmt(HashMap<Statement, Boolean> visitedStmt) {
      this.visitedStmt = visitedStmt;
   }

   public ArrayList<Include> getHeaders() {
      return headers;
   }

   public void setHeaders(ArrayList<Include> headers) {
      this.headers = headers;
   }

   public ArrayList<Define> getDefines() {
      return defines;
   }

   public void setDefines(ArrayList<Define> defines) {
      this.defines = defines;
   }

   public ArrayList<DeclarationStatement> getGlobals() {
      return globals;
   }

   public void setGlobals(ArrayList<DeclarationStatement> globals) {
      this.globals = globals;
   }

   public HashSet<Variable> getVariables() {
      return this.variables;
   }

   public void setVariables(HashSet<Variable> variables) {
      this.variables = variables;
   }

   public void getGlobalInfo(String filePath) {
      ArrayList<String> header = new ArrayList<>();
      ArrayList<String> define = new ArrayList<>();
      ArrayList<String> global = new ArrayList<>();

      ArrayList<UdbLexemeNode> globalList = new ArrayList<>();

      for (DeclarationStatement decl : globals) {
         for (UdbLexemeNode lexeme : decl.getRawData()) {
            if (lexeme.getToken().equals("Identifier")) {
               globalList.add(lexeme);
            }
         }
      }

      File mFile = new File(filePath);
      FileInputStream fileInputStream = null;
      try {
         fileInputStream = new FileInputStream(mFile);
         BufferedReader br = new BufferedReader(new InputStreamReader(fileInputStream));
         String line = "";
         while ((line = br.readLine()) != null) {
            // comment remove
            if (line.contains("//")) {
               line = line.substring(0, line.indexOf("//"));
            } else if (line.contains("/*")) {
               int begin = 0;
               int end = 0;
               while (true) {
                  begin = line.indexOf("/*", 0);
                  if (begin == -1)
                     break;
                  end = line.indexOf("*/", 0);
                  if (end == -1)
                     break;
                  if (end > 0 && begin == 0)
                     break;
                  String tmp_line = line.substring(0, begin - 1) + line.substring(end + 2, line.length());
                  line = tmp_line;
               }
            }
            if (line.length() == 0)
               continue;

            // functions check
            for (Function f : functions) {
               if (line.contains(f.toString())) {
                  int count = 0; // count bracket
                  boolean comment = false;
                  while (true) {
                     if (line == null)
                        continue;
                     if (line.contains("/*"))
                        comment = true;
                     if (line.contains("*/")) {
                        comment = false;
                        line = br.readLine();
                        continue;
                     }
                     if (comment == false && line.contains("{") && line.contains("//") == false)
                        count++;
                     if (comment == false && line.contains("}") && line.contains("//") == false)
                        count--;
                     if (count == 0)
                        break;
                     line = br.readLine();
                  }
               }
            }

            if (line.contains("define") && line.contains("(") == false) {
               define.add(line);
            } else if (line.contains("ifndef")) {
               define.add(line);
               while (true) {
                  line = br.readLine();
                  define.add(line);
                  defines.add(new Define("", "", "", line));
                  if (line.contains("endif"))
                     break;
               }
               continue;
            } else if (line.contains("#include")) {
               header.add(line);
            } else if (line.contains("enum")) {
               while (line.contains("}") == false) {
                  line += br.readLine().replace("\t", "");
               }
               if (line.contains("/*")) {
                  int begin = 0;
                  int end = 0;
                  while (true) {
                     begin = line.indexOf("/*", 0);
                     if (begin == -1)
                        break;
                     end = line.indexOf("*/", 0);
                     if (end == -1)
                        break;
                     String tmp_line = "";
                     if (begin == 0)
                        tmp_line = line.substring(end + 2, line.length());
                     else
                        tmp_line = line.substring(0, begin - 1) + " " + line.substring(end + 2, line.length());
                     line = tmp_line;
                  }
               }
               setGlobals(line, null);
               // global.add(line.replace(" ", ":").replace(";", ""));
            } else if (line.contains("struct")) {
               while (line.contains("}") == false) {
                  line += br.readLine().replace("\t", "");
               }
               if (line.contains("/*")) {
                  int begin = 0;
                  int end = 0;
                  while (true) {
                     begin = line.indexOf("/*", 0);
                     if (begin == -1)
                        break;
                     end = line.indexOf("*/", 0);
                     if (end == -1)
                        break;
                     String tmp_line = "";
                     if (begin == 0)
                        tmp_line = line.substring(end + 2, line.length());
                     else
                        tmp_line = line.substring(0, begin - 1) + " " + line.substring(end + 2, line.length());
                     line = tmp_line;
                  }
               }
               setGlobals(line, null);
            } else if (line.contains("{") && line.contains("}")) {
               for (UdbLexemeNode lexeme : globalList) {
                  if (line.matches(".*[^0-9a-zA-Z_]" + lexeme.getData() + "[^0-9a-zA-Z_].*")) {
                     for (DeclarationStatement decl : globals) {
                        if (decl.getRawStringData().matches(".*[^0-9a-zA-Z_]" + lexeme.getData() + "[^0-9a-zA-Z_].*")) {
                           setGlobals(line, decl);
                        }
                     }
                     continue;
                  }
               }
            }
         }
      } catch (IOException e) {
         e.printStackTrace();
      }

      // header

      for (String head : header) {
         headers.add(new Include("#include", head.replace(" ", "").split("#include")[1]));
      }

   }

   public void setGlobals(String line, DeclarationStatement decl) {
      // no typedef
      if (line.contains("enum")) {
         ArrayList<UdbLexemeNode> new_lexemes = new ArrayList<>();

         if (line.contains("{") == false) { // enum not contains {}
            StringTokenizer st = new StringTokenizer(line, " ", false);
            while (st.hasMoreTokens()) {
               String tmp = st.nextToken();
               if (tmp.equals(" ")) {
                  new_lexemes.add(new UdbLexemeNode(tmp, "NULL", "Whitespace", null));
               } else if (tmp.equals(",") || tmp.equals("*") || tmp.equals("=")) {
                  new_lexemes.add(new UdbLexemeNode(tmp, "NULL", "Operator", null));
               } else if (tmp.contains(";")) {
                  tmp.replace(" ", "");
                  new_lexemes.add(new UdbLexemeNode(tmp.split(";")[0], "NULL", "Identifier", null));
                  new_lexemes.add(new UdbLexemeNode(";", "NULL", "Punctuation", null));
               } else {
                  tmp.replace(" ", "");
                  if (isNumeric(tmp))
                     new_lexemes.add(new UdbLexemeNode(tmp, "NULL", "Literal", null));
                  else
                     new_lexemes.add(new UdbLexemeNode(tmp, "NULL", "Literal", null));
               }
            }

            String enum_name = line.split(" ")[2];
            if (enum_name.contains("typedef") && enum_name.equals("typedef") == false)
               new_lexemes.add(0, new UdbLexemeNode(enum_name.split("typedef")[1], "Define", "Identifier", null));
            else if (enum_name.contains("typedef") == false)
               new_lexemes.add(0, new UdbLexemeNode(enum_name, "Define", "Identifier", null));
            new_lexemes.add(0, new UdbLexemeNode("enum", "NULL", "Keyword", null));
            if (line.contains("typedef")) {
               new_lexemes.add(0, new UdbLexemeNode("typedef", "NULL", "Keyword", null));
               for (int i = new_lexemes.size() - 1; i > 0; i--) {
                  if (new_lexemes.get(i).getToken().equals("Literal")) {
                     new_lexemes.get(i).setRef_kind("Define");
                     new_lexemes.get(i).setToken("Identifier");
                     break;
                  }
                  if (new_lexemes.get(i).getData().equals("}")) {
                     break;
                  }
               }
            }
            // variable assign
            int tmp_ind = -1;
            for (int i = 0; i < new_lexemes.size(); i++) {
               Variable var = new Variable("", "", "", "global");
               if (new_lexemes.get(i).getRef_kind().equals("Define")) {
                  int ind = i;
                  var.setName(new_lexemes.get(i).getData());
                  String var_type = "";
                  ind--;
                  var_type = "enum";
                  var.setType(var_type);
                  new_lexemes.get(i).setVar(var);
                  tmp_ind = i;
               } else if (new_lexemes.get(i).getToken().equals("Identifier")
                     && new_lexemes.get(i).getRef_kind().equals("NULL")) {
                  new_lexemes.get(i).setVar(new_lexemes.get(tmp_ind).getVar());
               }
            }
         }

         else {
            String tmp_enum = "{" + line.split("\\{")[1]; // {...};
            StringTokenizer st = new StringTokenizer(tmp_enum.substring(0, tmp_enum.length() - 1), "{}*,= ", true);
            while (st.hasMoreTokens()) {
               String tmp = st.nextToken();
               if (tmp.equals("{") || tmp.equals("}")) {
                  new_lexemes.add(new UdbLexemeNode(tmp, "NULL", "Punctuation", null));
               } else if (tmp.equals(" ")) {
                  new_lexemes.add(new UdbLexemeNode(tmp, "NULL", "Whitespace", null));
               } else if (tmp.equals(",") || tmp.equals("*") || tmp.equals("=")) {
                  new_lexemes.add(new UdbLexemeNode(tmp, "NULL", "Operator", null));
               } else if (tmp.contains(";")) {
                  tmp.replace(" ", "");
                  new_lexemes.add(new UdbLexemeNode(tmp.split(";")[0], "Define", "Identifier", null));
                  new_lexemes.add(new UdbLexemeNode(";", "NULL", "Punctuation", null));
               } else {
                  tmp.replace(" ", "");
                  if (isNumeric(tmp))
                     new_lexemes.add(new UdbLexemeNode(tmp, "NULL", "Literal", null));
                  else
                     new_lexemes.add(new UdbLexemeNode(tmp, "NULL", "Literal", null));
               }
            }

            if (new_lexemes.get(new_lexemes.size() - 1).getData().equals("}") == false
                  && new_lexemes.get(new_lexemes.size() - 1).getData().equals(" ") == false) { // nick�� ��� ref_kind
                                                                                               // null, token identifier
               new_lexemes.get(new_lexemes.size() - 1).setToken("Identifier");
               new_lexemes.get(new_lexemes.size() - 1).setRef_kind("NULL");
            }

            String enum_name = line.split("\\{")[0].replace("enum", "").replace(" ", "");
            if (enum_name.contains("typedef") && enum_name.equals("typedef") == false)
               new_lexemes.add(0, new UdbLexemeNode(enum_name.split("typedef")[1], "Define", "Identifier", null));
            else if (enum_name.contains("typedef") == false)
               new_lexemes.add(0, new UdbLexemeNode(enum_name, "Define", "Identifier", null));
            new_lexemes.add(0, new UdbLexemeNode("enum", "NULL", "Keyword", null));
            if (line.contains("typedef")) {
               new_lexemes.add(0, new UdbLexemeNode("typedef", "NULL", "Keyword", null));
               for (int i = new_lexemes.size() - 1; i > 0; i--) {
                  if (new_lexemes.get(i).getToken().equals("Literal")) {
                     new_lexemes.get(i).setRef_kind("Define");
                     new_lexemes.get(i).setToken("Identifier");
                     break;
                  }
                  if (new_lexemes.get(i).getData().equals("}")) {
                     break;
                  }
               }
            }

            // variable assign
            int tmp_ind = -1;
            for (int i = 0; i < new_lexemes.size(); i++) {
               Variable var = new Variable("", "", "", "global");
               if (new_lexemes.get(i).getRef_kind().equals("Define")) {
                  int ind = i;
                  var.setName(new_lexemes.get(i).getData());
                  String var_type = "";
                  ind--;
                  // while (ind >-1 && new_lexemes.get(ind).getData().equals("{") == false &&
                  // new_lexemes.get(ind).getData().equals(";") == false){
                  // var_type = new_lexemes.get(ind).getData() + var_type;
                  // ind --;
                  // }
                  var_type = "enum";
                  var.setType(var_type);
                  new_lexemes.get(i).setVar(var);
                  tmp_ind = i;
               } else if (new_lexemes.get(i).getToken().equals("Identifier")
                     && new_lexemes.get(i).getRef_kind().equals("NULL")) {
                  new_lexemes.get(i).setVar(new_lexemes.get(tmp_ind).getVar());
               }
            }

            new_lexemes.add(new UdbLexemeNode(";", "NULL", "Punctuation", null));
         }

         DeclarationStatement struct_decl = new DeclarationStatement();
         struct_decl.setRawData(new_lexemes);
         globals.add(0, struct_decl);
      } else if (line.contains("struct")) {
         ArrayList<UdbLexemeNode> new_lexemes = new ArrayList<>();

         String tmp_struct = "{" + line.split("\\{")[1];
         StringTokenizer st = new StringTokenizer(tmp_struct.substring(0, tmp_struct.length() - 1), "{}* ", true);
         while (st.hasMoreTokens()) {
            String tmp = st.nextToken();
            if (tmp.equals("struct")) {
               new_lexemes.add(new UdbLexemeNode(tmp, "NULL", "Keyword", null));
            } else if (tmp.equals("{") || tmp.equals("}")) {
               new_lexemes.add(new UdbLexemeNode(tmp, "NULL", "Punctuation", null));
            } else if (tmp.equals(" ")) {
               new_lexemes.add(new UdbLexemeNode(tmp, "NULL", "Whitespace", null));
            } else if (tmp.equals(",") || tmp.equals("*")) {
               new_lexemes.add(new UdbLexemeNode(tmp, "NULL", "Operator", null));
            } else if (tmp.contains(";")) {
               tmp.replace(" ", "");
               new_lexemes.add(new UdbLexemeNode(tmp.split(";")[0], "Define", "Identifier", null));
               new_lexemes.add(new UdbLexemeNode(";", "NULL", "Punctuation", null));
            } else {
               // new_lexemes.add(new UdbLexemeNode(tmp, "NULL", "Literal", null));
               tmp.replace(" ", "");
               new_lexemes.add(new UdbLexemeNode(tmp, "Type", "Identifier", null));
            }
         }

         // variable assign

         for (int i = 0; i < new_lexemes.size(); i++) {
            Variable var = new Variable("", "", "", "global");
            if (new_lexemes.get(i).getRef_kind().equals("Define")) {
               int ind = i;
               var.setName(new_lexemes.get(i).getData());
               String var_type = "";
               ind--;
               while (ind > -1 && new_lexemes.get(ind).getData().equals("{") == false
                     && new_lexemes.get(ind).getData().equals(";") == false) {
                  var_type = new_lexemes.get(ind).getData() + var_type;
                  ind--;
               }
               var.setType(var_type);
               new_lexemes.get(i).setVar(var);

            }
         }

         String struct_name = line.split("\\{")[0].replace("struct", "").replace(" ", "");
         if (struct_name.contains("typedef") && struct_name.equals("typedef") == false)
            new_lexemes.add(0, new UdbLexemeNode(struct_name.split("typedef")[1], "Define", "Identifier", null));
         else if (struct_name.contains("typedef") == false)
            new_lexemes.add(0, new UdbLexemeNode(struct_name, "Define", "Identifier", null));
         new_lexemes.add(0, new UdbLexemeNode("struct", "NULL", "Keyword", null));
         if (line.contains("typedef")) {
            new_lexemes.add(0, new UdbLexemeNode("typedef", "NULL", "Keyword", null));
            for (int i = new_lexemes.size() - 1; i > 0; i--) {
               if (new_lexemes.get(i).getRef_kind().equals("Type")) {
                  new_lexemes.get(i).setRef_kind("Define");
                  break;
               }
               if (new_lexemes.get(i).getData().equals("}")) {
                  break;
               }
            }
         }
         new_lexemes.add(new UdbLexemeNode(";", "NULL", "Punctuation", null));

         DeclarationStatement struct_decl = new DeclarationStatement();
         struct_decl.setRawData(new_lexemes);
         globals.add(0, struct_decl);

      } else {
         for (int ind = 0; ind < decl.getRawData().size(); ind++) {
            if (decl.getRawData().get(ind).getData().contains("*")) {
               StringTokenizer st = new StringTokenizer(line.split("\\*")[0] + "*", " *", true);
               ArrayList<UdbLexemeNode> new_lexemes = new ArrayList<>();
               while (st.hasMoreTokens()) {
                  String tmp = st.nextToken();
                  if (tmp.equals("*"))
                     new_lexemes.add(new UdbLexemeNode(tmp, "NULL", "Operator", null));
                  else if (tmp.equals(" "))
                     new_lexemes.add(new UdbLexemeNode(tmp, "NULL", "Whitespace", null));
                  else
                     new_lexemes.add(new UdbLexemeNode(tmp, "NULL", "Keyword", null));
               }
               decl.getRawData().remove(ind);
               decl.getRawData().addAll(ind, new_lexemes);
               decl.setRawStringData(decl.getRawData());
               ind += new_lexemes.size();
            }
            if (decl.getRawData().get(ind).getData().contains("{")) {
               ArrayList<UdbLexemeNode> new_lexemes = new ArrayList<>();
               if (line.split("=").length < 2)
                  continue;
               StringTokenizer st = new StringTokenizer(line.split("=")[1].replace(" ", ""), "{,}", true);
               while (st.hasMoreTokens()) {
                  String tmp = st.nextToken();
                  if (tmp.equals("{") || tmp.equals("}")) {
                     new_lexemes.add(new UdbLexemeNode(tmp, "NULL", "Punctuation", null));
                  } else if (tmp.equals(" ")) {
                     new_lexemes.add(new UdbLexemeNode(tmp, "NULL", "Whitespace", null));
                  } else if (tmp.equals(",")) {
                     new_lexemes.add(new UdbLexemeNode(tmp, "NULL", "Operator", null));
                  } else if (tmp.equals(";"))
                     continue;
                  else {
                     new_lexemes.add(new UdbLexemeNode(tmp, "NULL", "Literal", null));
                  }
               }

               decl.getRawData().remove(ind);
               decl.getRawData().addAll(ind, new_lexemes);
               decl.setRawStringData(decl.getRawData());
               break;
            }
         }
      }
   }

   public static boolean isNumeric(String strNum) {
      if (strNum == null) {
         return false;
      }
      try {
         double d = Double.parseDouble(strNum);
      } catch (NumberFormatException nfe) {
         return false;
      }
      return true;
   }

   /**
    * @date 2023-05-02(Tue)
    * @author SoheeJung
    * @name splitFilePath
    * @return filePathArrayList : index0 -> file path / index1 -> file name
    *         split file path and file name
    */
   public ArrayList<String> splitFilePath(String filepath) {
      int lastSplitIndex = filepath.lastIndexOf("/");
      ArrayList<String> filePathList = new ArrayList<String>();

      if (0 < lastSplitIndex) {
         filePathList.add(filepath.substring(0, lastSplitIndex));
         filePathList.add(filepath.substring(lastSplitIndex + 1));
      }

      return filePathList;
   }
}