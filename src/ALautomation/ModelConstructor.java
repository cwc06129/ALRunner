/**
 * 2023-03-23(Thu) SoheeJung
 * ClassName : ModelConstructor
 * Active Learning automation main class
 */
package ALautomation;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.FilenameFilter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

import syntax.expression.BinaryExpression;
import syntax.expression.BracketExpression;
import syntax.expression.CompoundExpression;
import syntax.expression.DeclExpression;
import syntax.expression.Expression;
import syntax.expression.IdExpression;
import syntax.expression.LibraryCallExpression;
import syntax.expression.LiteralExpression;
import syntax.expression.NullExpression;
import syntax.expression.Type;
import syntax.expression.TypedefExpression;
import syntax.global.Define;
import syntax.global.Include;
import syntax.statement.CompoundStatement;
import syntax.statement.DeclarationStatement;
import syntax.statement.ExpressionStatement;
import syntax.statement.Function;
import syntax.statement.PseudoStatement;
import syntax.variable.Variable;
import traverse.general.ExprTraverser;
import traverse.general.ALAutomationTraverser.CodePrintTraverser;
import traverse.general.ALAutomationTraverser.IdChangeTraverser;
import udbparser.usingperl.UdbInfoRetriever;

public class ModelConstructor {
    private InterestVarList inputVarList;
    private InterestVarList stateVarList;
    private InterestVarList outputVarList;
    private String targetFunc;
    private Integer trace_m;
    private Integer trace_n;
    private Integer unwind;
    private Integer k_ind;
    // 2023-10-24(Tue) SoheeJung : object-bits variable addition
    private Integer object_bits;
    // 2023-09-11(Mon) SoheeJung : enumeration information hashmap
    private HashMap<String, String[]> enumMap = new HashMap<String, String[]>();
    // 2023-10-19(Thu) SoheeJung : array information hashmap
    private HashMap<String, Integer> arrMap = new HashMap<String, Integer>();
    // 2023-05-30(Tue) SoheeJung
    // model_step function's printf element save
    private ArrayList<String> topElement;
    private ArrayList<String> bottomElement;
    // 2023-10-31(Tue) SoheeJung : struct information hashmap
    private HashMap<String, ArrayList<DeclExpression>> structMap = new HashMap<String, ArrayList<DeclExpression>>();
    // 2023-11-01(Wed) SoheeJung : struct variable scope
    /*
     * [This structure structure]
     * 1. key : structure variable name
     * 2. value : ArrayList<Integer>
     * 2-1. 0 index element : interest structure type (0 : INPUT / 1 : STATE / 2 :
     * OUTPUT)
     * 2-2. 1 index element : variable scope first value
     * 2-3. 2 index element : variable scope last value
     */
    private HashMap<String, ArrayList<Integer>> structVarScope = new HashMap<String, ArrayList<Integer>>();
    private String structKeyVar = null;
    // 2023-11-03(Fri) SoheeJung : constant element list
    private ArrayList<String> constList = new ArrayList<String>();

    // constructor
    public ModelConstructor() {
        this.inputVarList = new InterestVarList();
        this.inputVarList.setType("input");
        this.stateVarList = new InterestVarList();
        this.stateVarList.setType("state");
        this.outputVarList = new InterestVarList();
        this.outputVarList.setType("output");
        this.targetFunc = null;
        this.trace_m = 0;
        this.trace_n = 0;
    }

    /**
     * @date 2023-03-23(Thu)
     * @author SoheeJung
     * @name getInfoFromString
     * @param str : input string that have to preprocess
     *            1. make string no space
     *            2. split by colon(:)
     *            3. return data from second element of string array
     *            [example]
     *            (input) func : main
     *            (output) main
     */
    public String getDataFromString(String str) {
        str = str.replaceAll(" ", "");
        String[] strlist = str.trim().split(":");
        return strlist[1];
    }

    /**
     * @date 2023-03-23(Thu)
     * @author SoheeJung
     * @name parseTxtInfo
     * @param filename : spec.txt
     *                 read spec.txt and get information
     */
    public void parseTxtInfo(String filename, HashSet<Variable> variables) throws IOException {
        BufferedReader reader = new BufferedReader(new FileReader(filename));
        String contents; // file contents
        // 2023-10-31(Tue) SoheeJung : structure checking variable
        boolean structCheck = false;
        String structName = "";
        ArrayList<DeclExpression> declExprList = new ArrayList<DeclExpression>();

        while ((contents = reader.readLine()) != null) {
            // 2023-10-31(Tue) SoheeJung
            // struct processing
            if (contents.contains(">> struct name")) {
                structCheck = true;
                String[] strlist = contents.trim().split(":");
                structName = strlist[1].strip();
            } else if (contents.contains(">> struct element start")) {
                structCheck = true;
            } else if (structCheck && !contents.contains(">> struct element finish")) {
                String codeLine = contents.split(";")[0];
                String[] str1 = codeLine.split(" ");
                String typeStr = "";
                String varNameStr = str1[str1.length - 1];
                for (int i = 0; i < str1.length - 1; i++) {
                    if (i == str1.length - 2)
                        typeStr += str1[i];
                    else
                        typeStr += str1[i] + " ";
                }

                IdExpression idExpr = new IdExpression(varNameStr, "", typeStr, null);
                Type type = new Type(typeStr);
                BinaryExpression binExpr = new BinaryExpression();
                binExpr.setLhsOperand(idExpr);
                binExpr.setOperator(null);
                binExpr.setRhsOperand(null);
                DeclExpression declExpr = new DeclExpression(binExpr, idExpr, type);
                declExprList.add(declExpr);
            } else if (contents.contains(">> struct element finish")) {
                getStructMap().put(structName, declExprList);
                structCheck = false;
            }
            /* struct processing finish */

            // 2023-11-03(Fri) SoheeJung : constant type process part addition
            else if (contents.contains(">> constant")) {
                String constStr = getDataFromString(contents);
                String[] constTempList = constStr.split(",");
                for (String c : constTempList) {
                    getConstList().add(c);
                }
            }
            /* constant processing finish */

            // process target function
            else if (contents.contains(">> func")) {
                setTargetFunc(getDataFromString(contents));
            }
            // process trace iteration count : m, n
            // else if(contents.contains(">> trace_m")) {
            // setTrace_m(Integer.valueOf(getDataFromString(contents)));
            // }
            // else if(contents.contains(">> trace_n")) {
            // setTrace_n(Integer.valueOf(getDataFromString(contents)));
            // }
            // // 2023-05-24(Wed) SoheeJung
            // // [addition] process unwind and k-induction
            // // process unwind
            // else if(contents.contains(">> unwind")) {
            // setUnwind(Integer.valueOf(getDataFromString(contents)));
            // }
            // // process k-induction
            // else if(contents.contains(">> k-ind")) {
            // setK_ind(Integer.valueOf(getDataFromString(contents)));
            // }
            else if (contents.contains("//")) {
                // do-nothing
            }
            // 2023-05-25(Thu) SoheeJung
            // key interest variable check and process key interest variable
            else if (contents.contains(">> key")) {
                String processString = contents.split(":")[1].trim();
                String[] strlist = processString.split(" ");
                if (strlist[1].contains("input")) {
                    getInputVarList().getNameList().add(0, strlist[0]);
                    getInputVarList().setKeyCheck(true);

                    // 2023-03-31(Fri) SoheeJung
                    // make input variable scope list from spec.txt
                    String[] scopestr = strlist[2].split(",");
                    ArrayList<Integer> scopelist = new ArrayList<Integer>();
                    scopelist.add(Integer.valueOf(scopestr[0]));
                    scopelist.add(Integer.valueOf(scopestr[1]));

                    // 2023-04-09(Sun) SoheeJung
                    // if variable is input interest variable, then set variable scope
                    for (Variable var : variables) {
                        if (strlist[0].equals(var.getName())) {
                            var.setVarScope(scopelist);
                        }
                    }

                    // 2023-10-19(Thu) SoheeJung
                    // if variable is array type, then add arrayHashmap
                    if (strlist.length > 3) {
                        String[] arrinfo = strlist[3].split(",");
                        ArrayList<Integer> indexinfo = new ArrayList<Integer>();
                        getArrMap().put(strlist[0], Integer.valueOf(arrinfo[1]));
                    }

                    // 2023-11-01(Wed) SoheeJung
                    // if variable is struct type, then add structVarScope Hashmap
                    if (strlist[0].contains(".")) {
                        setStructKeyVar(strlist[0]);
                        scopelist.add(0, 0); // if variable is INPUT type, then put 0(INPUT) to 0 index element
                        getStructVarScope().put(strlist[0], scopelist);
                    }
                } else if (strlist[1].contains("state")) {
                    getStateVarList().getNameList().add(0, strlist[0]);
                    getStateVarList().setKeyCheck(true);

                    // 2023-10-19(Thu) SoheeJung
                    // make state variable scope list from spec.txt
                    String[] scopestr = strlist[2].split(",");
                    ArrayList<Integer> scopelist = new ArrayList<Integer>();
                    scopelist.add(Integer.valueOf(scopestr[0]));
                    scopelist.add(Integer.valueOf(scopestr[1]));

                    // 2023-10-19(Thu) SoheeJung
                    // if variable is state interest variable, then set variable scope
                    for (Variable var : variables) {
                        if (strlist[0].equals(var.getName())) {
                            var.setVarScope(scopelist);
                        }
                    }

                    // 2023-10-19(Thu) SoheeJung
                    // if variable is array type, then add arrayHashmap
                    if (strlist.length > 3) {
                        String[] arrinfo = strlist[3].split(",");
                        ArrayList<Integer> indexinfo = new ArrayList<Integer>();
                        getArrMap().put(strlist[0], Integer.valueOf(arrinfo[1]));
                    }

                    // 2023-11-01(Wed) SoheeJung
                    // if variable is struct type, then add structVarScope Hashmap
                    if (strlist[0].contains(".")) {
                        setStructKeyVar(strlist[0]);
                        scopelist.add(0, 1); // if variable is INPUT type, then put 1(STATE) to 0 index element
                        getStructVarScope().put(strlist[0], scopelist);
                    }
                } else if (strlist[1].contains("output")) {
                    getOutputVarList().getNameList().add(0, strlist[0]);
                    getOutputVarList().setKeyCheck(true);

                    // 2023-10-19(Thu) SoheeJung
                    // make output variable scope list from spec.txt
                    String[] scopestr = strlist[2].split(",");
                    ArrayList<Integer> scopelist = new ArrayList<Integer>();
                    scopelist.add(Integer.valueOf(scopestr[0]));
                    scopelist.add(Integer.valueOf(scopestr[1]));

                    // 2023-10-19(Thu) SoheeJung
                    // if variable is output interest variable, then set variable scope
                    for (Variable var : variables) {
                        if (strlist[0].equals(var.getName())) {
                            var.setVarScope(scopelist);
                        }
                    }

                    // 2023-10-19(Thu) SoheeJung
                    // if variable is array type, then add arrayHashmap
                    if (strlist.length > 3) {
                        String[] arrinfo = strlist[3].split(",");
                        ArrayList<Integer> indexinfo = new ArrayList<Integer>();
                        getArrMap().put(strlist[0], Integer.valueOf(arrinfo[1]));
                    }

                    // 2023-11-01(Wed) SoheeJung
                    // if variable is struct type, then add structVarScope Hashmap
                    if (strlist[0].contains(".")) {
                        setStructKeyVar(strlist[0]);
                        scopelist.add(0, 2); // if variable is INPUT type, then put 2(OUTPUT) to 0 index element
                        getStructVarScope().put(strlist[0], scopelist);
                    }
                }
            }
            // 2023-10-19(Thu) SoheeJung
            // enumeration process part
            else if (contents.contains(">> enum")) {
                String enumString = getDataFromString(contents);
                String[] strlist1 = enumString.split("\\{");
                String[] strlist2 = strlist1[1].split("\\}");
                String[] enumElement = strlist2[0].split(",");
                getEnumMap().put(strlist1[0], enumElement);
            }
            // process interest variable
            else {
                String[] strlist = contents.split(" ");
                if (strlist[1].contains("input")) {
                    getInputVarList().getNameList().add(strlist[0]);

                    // 2023-03-31(Fri) SoheeJung
                    // make input variable scope list from spec.txt
                    String[] scopestr = strlist[2].split(",");
                    ArrayList<Integer> scopelist = new ArrayList<Integer>();
                    scopelist.add(Integer.valueOf(scopestr[0]));
                    scopelist.add(Integer.valueOf(scopestr[1]));

                    // 2023-04-09(Sun) SoheeJung
                    // if variable is input interest variable, then set variable scope
                    for (Variable var : variables) {
                        if (strlist[0].equals(var.getName())) {
                            var.setVarScope(scopelist);
                        }
                    }

                    // 2023-10-19(Thu) SoheeJung
                    // if variable is array type, then add arrayHashmap
                    if (strlist.length > 3) {
                        String[] arrinfo = strlist[3].split(",");
                        ArrayList<Integer> indexinfo = new ArrayList<Integer>();
                        getArrMap().put(strlist[0], Integer.valueOf(arrinfo[1]));
                    }

                    // 2023-11-01(Wed) SoheeJung
                    // if variable is struct type, then add structVarScope Hashmap
                    if (strlist[0].contains(".")) {
                        scopelist.add(0, 0); // if variable is INPUT type, then put 0(INPUT) to 0 index element
                        getStructVarScope().put(strlist[0], scopelist);
                    }
                } else if (strlist[1].contains("state")) {
                    getStateVarList().getNameList().add(strlist[0]);

                    // 2023-10-19(Thu) SoheeJung
                    // make state variable scope list from spec.txt
                    String[] scopestr = strlist[2].split(",");
                    ArrayList<Integer> scopelist = new ArrayList<Integer>();
                    scopelist.add(Integer.valueOf(scopestr[0]));
                    scopelist.add(Integer.valueOf(scopestr[1]));

                    // 2023-10-19(Thu) SoheeJung
                    // if variable is state interest variable, then set variable scope
                    for (Variable var : variables) {
                        if (strlist[0].equals(var.getName())) {
                            var.setVarScope(scopelist);
                        }
                    }

                    // 2023-10-19(Thu) SoheeJung
                    // if variable is array type, then add arrayHashmap
                    if (strlist.length > 3) {
                        String[] arrinfo = strlist[3].split(",");
                        ArrayList<Integer> indexinfo = new ArrayList<Integer>();
                        getArrMap().put(strlist[0], Integer.valueOf(arrinfo[1]));
                    }

                    // 2023-11-01(Wed) SoheeJung
                    // if variable is struct type, then add structVarScope Hashmap
                    if (strlist[0].contains(".")) {
                        scopelist.add(0, 1); // if variable is INPUT type, then put 1(STATE) to 0 index element
                        getStructVarScope().put(strlist[0], scopelist);
                    }
                } else if (strlist[1].contains("output")) {
                    getOutputVarList().getNameList().add(strlist[0]);

                    // 2023-10-19(Thu) SoheeJung
                    // make output variable scope list from spec.txt
                    String[] scopestr = strlist[2].split(",");
                    ArrayList<Integer> scopelist = new ArrayList<Integer>();
                    scopelist.add(Integer.valueOf(scopestr[0]));
                    scopelist.add(Integer.valueOf(scopestr[1]));

                    // 2023-10-19(Thu) SoheeJung
                    // if variable is output interest variable, then set variable scope
                    for (Variable var : variables) {
                        if (strlist[0].equals(var.getName())) {
                            var.setVarScope(scopelist);
                        }
                    }

                    // 2023-10-19(Thu) SoheeJung
                    // if variable is array type, then add arrayHashmap
                    if (strlist.length > 3) {
                        String[] arrinfo = strlist[3].split(",");
                        ArrayList<Integer> indexinfo = new ArrayList<Integer>();
                        getArrMap().put(strlist[0], Integer.valueOf(arrinfo[1]));
                    }

                    // 2023-11-01(Wed) SoheeJung
                    // if variable is struct type, then add structVarScope Hashmap
                    if (strlist[0].contains(".")) {
                        scopelist.add(0, 2); // if variable is INPUT type, then put 2(OUTPUT) to 0 index element
                        getStructVarScope().put(strlist[0], scopelist);
                    }
                }
            }
        }

        reader.close();
    }

    /**
     * @date 2023-03-24(Fri)
     * @author SoheeJung
     * @name makeModelHFile
     * @param variables : get parameter variables from udbInfoRetriver's variables
     * @throws IOException
     *                     1. print INPUT / STATE / OUTPUT structure (Even if there
     *                     is no variable inside, the structure is created.)
     *                     1-1. get variable hashset from main function, then
     *                     compare variable's name to interest variable's name
     *                     1-2. if there is same one, then print type and variable
     *                     name.
     *                     2. print additional statement below.
     *                     extern STATE rt_state;
     *                     extern INPUT rt_input;
     *                     extern OUTPUT rt_output;
     *                     extern void model_initialize(void);
     *                     extern void model_step(void);
     */
    public void makeModelHFile(UdbInfoRetriever udbInfoRetriever, String directoryPath) throws IOException {
        File file = new File(directoryPath + "/model.h");
        PrintWriter writer = new PrintWriter(file);

        writer.println("#pragma once");
        writer.println("#ifndef _ENUM_DEFINE");
        // 2023-03-24(Fri) SoheeJung
        // Enum information processing part
        writer.println("//enum information");
        // 2023-09-08(Fri) SoheeJung : enumeration declaration statement print
        // 2023-09-11(Mon) SoheeJung : save enumeration information to hashmap
        // String enumString;
        // String[] enumElement = {};
        // for(DeclarationStatement declStmt : udbInfoRetriever.getGlobals()) {
        // if(declStmt.getDeclExpression().get(0) instanceof TypedefExpression) {
        // TypedefExpression typedefExpr =
        // ((TypedefExpression)declStmt.getDeclExpression().get(0));
        // writer.print(typedefExpr.getCode());
        // if (typedefExpr.getExpressions().isEmpty()) {
        // if (typedefExpr.getNick() != null) writer.print(typedefExpr.getNick());
        // }
        // else {
        // for (Expression e : (typedefExpr.getExpressions())) {
        // if (e instanceof BracketExpression && ((BracketExpression)e).getPosition()){
        // writer.println(e.getCode());
        // } else if (e instanceof BracketExpression) {
        // writer.print(e.getCode());
        // } else {
        // if(typedefExpr.getType().equals("enum")) {
        // enumString = e.getCode().replace(" ", "");
        // enumString = enumString.replace("{", "");
        // enumString = enumString.replace("}", "");
        // enumElement = enumString.split("\\,");
        // System.out.println("enumString : " + enumString);
        // writer.print(e.getCode());
        // }
        // else writer.println(e.getCode()+";");
        // }
        // }
        // if (typedefExpr.getNick() != null) {
        // writer.print(" " + (typedefExpr.getNick()));
        // getEnumMap().put(typedefExpr.getNick(), enumElement);
        // }
        // else {
        // getEnumMap().put("", enumElement);
        // }
        // }
        // writer.println(";");
        // }
        // }
        /* enumeration declaration statement print finish */

        // 2023-10-19(Thu) SoheeJung
        // [new version of enumeration part]
        // for(DeclarationStatement declStmt : udbInfoRetriever.getGlobals()) {
        // // if declarationStatement in global part has 'enum' string, then make enum
        // statement to model.h with some preprocessing.
        // if(declStmt.getRawStringData().contains("enum")) {
        // String declString = declStmt.getRawStringData().replace("typedefenum",
        // "typedef enum ");
        // writer.println(declString);
        // }
        // }
        /* new version of enumeration part finish */

        // 2023-10-24(Tue) SoheeJung
        // [new version of enumeration part only using spec.txt]
        for (String enumName : getEnumMap().keySet()) {
            for (Variable var : udbInfoRetriever.getVariables()) {
                if (!var.getFileName().equals("NULL") && var.getScope().equals("Global")) {
                    if (var.getName().equals(enumName)) {
                        String typeName = var.getType();
                        writer.print("typedef enum _" + typeName + " {");
                        for (int i = 0; i < getEnumMap().get(enumName).length; i++) {
                            if (i == getEnumMap().get(enumName).length - 1)
                                writer.print(getEnumMap().get(enumName)[i]);
                            else
                                writer.print(getEnumMap().get(enumName)[i] + ", ");
                        }
                        writer.println("} " + typeName + ";");
                    }
                }
            }
        }
        /* new version of enumeration part finish */

        writer.println("#define _ENUM_DEFINE");
        writer.println("#endif\n");

        // 2023-11-01(Wed) SoheeJung
        // [global struct part only using spec.txt]
        for (String structName : getStructMap().keySet()) {
            String[] nameNick = structName.split(" ");
            writer.println("typedef struct " + nameNick[0] + "{");
            ArrayList<DeclExpression> structElementList = getStructMap().get(structName);
            for (DeclExpression declExpr : structElementList) {
                writer.println("\t" + declExpr.getCode() + ";");
            }
            writer.println("} " + nameNick[1] + ";");
        }
        writer.println();
        /* global struct part finish */

        // 2023-11-01(Wed) SoheeJung : duplicate definition elimination
        Set<String> inputStructElement = new HashSet<String>();
        Set<String> stateStructElement = new HashSet<String>();
        Set<String> outputStructElement = new HashSet<String>();

        // 2023-11-01(Wed) SoheeJung : duplicate interest variable save elimination
        Set<String> inputStructVarSave = new HashSet<String>();
        Set<String> stateStructVarSave = new HashSet<String>();
        Set<String> outputStructVarSave = new HashSet<String>();

        // 2024-02-18(Sun) SoheeJung : INPUT structure generation part
        writer.println("typedef struct {");
        for (DeclarationStatement declstmt : udbInfoRetriever.getGlobals()) {
            DeclExpression declExpr = (DeclExpression) declstmt.getDeclExpression().get(0);
            if (declExpr.getBinaryExpression().getLhsOperand() instanceof IdExpression) {
                IdExpression varIdExpr = (IdExpression) declExpr.getBinaryExpression().getLhsOperand();
                if (getInputVarList().getNameList().contains(varIdExpr.getName())) {
                    if (!getArrMap().containsKey(varIdExpr.getName()))
                        inputStructElement.add("\t" + varIdExpr.getType() + " " + varIdExpr.getName() + ";");
                    else {
                        String varType = varIdExpr.getType().toString().split(" ")[0];
                        inputStructElement.add("\t" + varType + " " + varIdExpr.getName() + "["
                                + String.valueOf(getArrMap().get(varIdExpr.getName())) + "];");
                    }
                } else {
                    // 2023-11-01(Tue) SoheeJung : struct variable processing code
                    for (String structName : getStructMap().keySet()) {
                        for (String inputStr : getInputVarList().getNameList()) {
                            if (inputStr.contains(".")) {
                                String[] tempStr = inputStr.split("\\."); // get structure variable name
                                if (tempStr[0].equals(varIdExpr.getName())) {
                                    inputStructElement
                                            .add("\t" + varIdExpr.getType() + " " + varIdExpr.getName() + ";");
                                    inputStructVarSave.add(varIdExpr.getName());
                                }
                            }
                        }
                    }
                }
            } else if (declExpr.getBinaryExpression().getLhsOperand() instanceof CompoundExpression) {
                CompoundExpression cpdExpr = (CompoundExpression) declExpr.getBinaryExpression().getLhsOperand();
                IdExpression idExpr = (IdExpression) cpdExpr.getExpression().get(0);
                if (getInputVarList().getNameList().contains(idExpr.getName())) {
                    if (!getArrMap().containsKey(idExpr.getName()))
                        inputStructElement.add("\t" + idExpr.getType() + " " + idExpr.getName() + ";");
                    else {
                        String varType = idExpr.getType().toString().split(" ")[0];
                        inputStructElement.add("\t" + varType + " " + idExpr.getName() + "["
                                + String.valueOf(getArrMap().get(idExpr.getName())) + "];");
                    }
                }
            }
        }

        // 2024-02-18(Sun) SoheeJung : struct variable processing part (using getStructVar data structure)
        for(String structVariableName : getStructVarScope().keySet()) {
            if(getStructVarScope().get(structVariableName).get(0) == 0) {
                String structName = structVariableName.split("\\.")[0];
                for (Variable v : udbInfoRetriever.getVariables()) {
                    if(v.getName().equals(structName)) {
                        System.out.println("we find it! (input) " + structName);
                        inputStructElement.add("\t" + v.getType() + " " + structName + ";");
                        inputStructVarSave.add(structName);
                    }
                }
            }
        }
        /* struct variable processing finish */

        for (String inputFinalStr : inputStructElement) {
            writer.println(inputFinalStr);
        }

        writer.println("} INPUT;\n");
        /* INPUT generation part finish */

        // 2024-02-18(Sun) SoheeJung : STATE structure generation part
        writer.println("typedef struct {");
        for (DeclarationStatement declstmt : udbInfoRetriever.getGlobals()) {
            DeclExpression declExpr = (DeclExpression) declstmt.getDeclExpression().get(0);
            if (declExpr.getBinaryExpression().getLhsOperand() instanceof IdExpression) {
                IdExpression varIdExpr = (IdExpression) declExpr.getBinaryExpression().getLhsOperand();
                if (getStateVarList().getNameList().contains(varIdExpr.getName())) {
                    if (!getArrMap().containsKey(varIdExpr.getName()))
                        stateStructElement.add("\t" + varIdExpr.getType() + " " + varIdExpr.getName() + ";");
                    else {
                        String varType = varIdExpr.getType().toString().split(" ")[0];
                        stateStructElement.add("\t" + varType + " " + varIdExpr.getName() + "["
                                + String.valueOf(getArrMap().get(varIdExpr.getName())) + "];");
                    }
                } else {
                    // 2023-11-01(Tue) SoheeJung : struct variable processing code
                    for (String structName : getStructMap().keySet()) {
                        for (String stateStr : getStateVarList().getNameList()) {
                            if (stateStr.contains(".")) {
                                String[] tempStr = stateStr.split("\\."); // get structure variable name
                                if (tempStr[0].equals(varIdExpr.getName())) {
                                    stateStructElement
                                            .add("\t" + varIdExpr.getType() + " " + varIdExpr.getName() + ";");
                                    stateStructVarSave.add(varIdExpr.getName());
                                }
                            }
                        }
                    }
                }
            } else if (declExpr.getBinaryExpression().getLhsOperand() instanceof CompoundExpression) {
                CompoundExpression cpdExpr = (CompoundExpression) declExpr.getBinaryExpression().getLhsOperand();
                IdExpression idExpr = (IdExpression) cpdExpr.getExpression().get(0);
                if (getStateVarList().getNameList().contains(idExpr.getName())) {
                    if (!getArrMap().containsKey(idExpr.getName()))
                        stateStructElement.add("\t" + idExpr.getType() + " " + idExpr.getName() + ";");
                    else {
                        String varType = idExpr.getType().toString().split(" ")[0];
                        stateStructElement.add("\t" + varType + " " + idExpr.getName() + "["
                                + String.valueOf(getArrMap().get(idExpr.getName())) + "];");
                    }
                }
            }
        }

        // 2024-02-18(Sun) SoheeJung : struct variable processing part (using getStructVar data structure)
        for(String structVariableName : getStructVarScope().keySet()) {
            if(getStructVarScope().get(structVariableName).get(0) == 1) {
                String structName = structVariableName.split("\\.")[0];
                for (Variable v : udbInfoRetriever.getVariables()) {
                    if(v.getName().equals(structName)) {
                        System.out.println("we find it! (state) " + structName);
                        stateStructElement.add("\t" + v.getType() + " " + structName + ";");
                        stateStructVarSave.add(structName);
                    }
                }
            }
        }
        /* struct variable processing finish */

        for (String stateFinalStr : stateStructElement) {
            writer.println(stateFinalStr);
        }

        writer.println("} STATE;\n");
        /* STATE generation part finish */

        // 2024-02-18(Sun) SoheeJung : OUTPUT structure generation part
        writer.println("typedef struct {");
        for (DeclarationStatement declstmt : udbInfoRetriever.getGlobals()) {
            DeclExpression declExpr = (DeclExpression) declstmt.getDeclExpression().get(0);
            if (declExpr.getBinaryExpression().getLhsOperand() instanceof IdExpression) {
                IdExpression varIdExpr = (IdExpression) declExpr.getBinaryExpression().getLhsOperand();
                if (getOutputVarList().getNameList().contains(varIdExpr.getName())) {
                    if (!getArrMap().containsKey(varIdExpr.getName()))
                        outputStructElement.add("\t" + varIdExpr.getType() + " " + varIdExpr.getName() + ";");
                    else {
                        String varType = varIdExpr.getType().toString().split(" ")[0];
                        outputStructElement.add("\t" + varType + " " + varIdExpr.getName() + "["
                                + String.valueOf(getArrMap().get(varIdExpr.getName())) + "];");
                    }
                } else {
                    // 2023-11-01(Tue) SoheeJung : struct variable processing code
                    for (String structName : getStructMap().keySet()) {
                        for (String outputStr : getOutputVarList().getNameList()) {
                            if (outputStr.contains(".")) {
                                String[] tempStr = outputStr.split("\\."); // get structure variable name
                                if (tempStr[0].equals(varIdExpr.getName())) {
                                    outputStructElement
                                            .add("\t" + varIdExpr.getType() + " " + varIdExpr.getName() + ";");
                                    outputStructVarSave.add(varIdExpr.getName());
                                }
                            }
                        }
                    }
                }
            } else if (declExpr.getBinaryExpression().getLhsOperand() instanceof CompoundExpression) {
                CompoundExpression cpdExpr = (CompoundExpression) declExpr.getBinaryExpression().getLhsOperand();
                IdExpression idExpr = (IdExpression) cpdExpr.getExpression().get(0);
                if (getOutputVarList().getNameList().contains(idExpr.getName())) {
                    if (!getArrMap().containsKey(idExpr.getName()))
                        outputStructElement.add("\t" + idExpr.getType() + " " + idExpr.getName() + ";");
                    else {
                        String varType = idExpr.getType().toString().split(" ")[0];
                        outputStructElement.add("\t" + varType + " " + idExpr.getName() + "["
                                + String.valueOf(getArrMap().get(idExpr.getName())) + "];");
                    }
                }
            }
        }

        // 2024-02-18(Sun) SoheeJung : struct variable processing part (using getStructVar data structure)
        for(String structVariableName : getStructVarScope().keySet()) {
            if(getStructVarScope().get(structVariableName).get(0) == 2) {
                String structName = structVariableName.split("\\.")[0];
                for (Variable v : udbInfoRetriever.getVariables()) {
                    if(v.getName().equals(structName)) {
                        System.out.println("we find it! (output) " + structName);
                        outputStructElement.add("\t" + v.getType() + " " + structName + ";");
                        outputStructVarSave.add(structName);
                    }
                }
            }
        }
        /* struct variable processing finish */

        for (String outputFinalStr : outputStructElement) {
            writer.println(outputFinalStr);
        }

        writer.println("} OUTPUT;\n");
        /* OUTPUT generation part finish */

        writer.println("extern INPUT rt_input;");
        writer.println("extern STATE rt_state;");
        writer.println("extern OUTPUT rt_output;");
        writer.println("extern void model_initialize(void);");
        writer.println("extern void model_step(void);");

        writer.close();

        // 2023-11-01(Wed) SoheeJung : struct variable addition to NameList of interest
        // variable list
        for (String inputStr : inputStructVarSave) {
            getInputVarList().getNameList().add(inputStr);
        }
        for (String stateStr : stateStructVarSave) {
            getStateVarList().getNameList().add(stateStr);
        }
        for (String outputStr : outputStructVarSave) {
            getOutputVarList().getNameList().add(outputStr);
        }
        /* struct variable addition finish */
    }

    /**
     * @date 2023-03-27(Mon)
     * @author SoheeJung
     * @name makeModelInitialize
     * @param udbInfoRetriever : get CFG information
     *                         1. collect DeclarationStatement from global statement
     *                         and function CFG.
     *                         2. make model_initialize function
     *                         2-1. DeclarationStatement -> ExpressionStatement
     *                         2-2. [To-do] if there is no initial value, then using
     *                         frama-c.
     *                         2-3. add initializing expression statement to
     *                         model_initailize function
     */
    public void makeModelInitialize(UdbInfoRetriever udbInfoRetriever) {
        ArrayList<DeclarationStatement> declStmtList = new ArrayList<DeclarationStatement>();
        ArrayList<ExpressionStatement> exprStmtList = new ArrayList<ExpressionStatement>();

        // 1. collect DeclarationStatment
        for (DeclarationStatement declstmt : udbInfoRetriever.getGlobals()) {
            declStmtList.add(declstmt);
        }
        // 2023-05-04(Thu) SoheeJung
        // if variable's scope is local, then we should not initailize in
        // model_initialize function.
        // DeclCollectTraverser cfg = new DeclCollectTraverser();
        // for(Function f : udbInfoRetriever.getFunctions()) {
        // System.out.println("decl function: " + f.getName());
        // cfg.traverse(f);
        // }
        // for(DeclarationStatement declstmt : cfg.getDeclStmtList()) {
        // declStmtList.add(declstmt);
        // }

        // 2. make model_initilize function (CFG form)
        // make model_initialize function
        Function initializeModel = new Function("void", "model_initialize", null, 0, 0);

        // DeclarationStatement -> ExpressionStatement
        for (DeclarationStatement declstmt : declStmtList) {
            // 2023-03-28(Tue) SoheeJung
            // if binary expression's operator is null, then set "="
            // 2023-04-10(Mon) SoheeJung
            // setting on fixed new annotation form, not original binary expression form
            // 2023-09-08(Fri) SoheeJung : DeclExpression don't contain Typedef Expression

            if (declstmt.getDeclExpression().get(0) instanceof DeclExpression) {
                DeclExpression inputDeclstmt = ((DeclExpression) (declstmt.getDeclExpression().get(0)));
                // 2023-10-19(Thu) SoheeJung

                // if BinaryExpresion's rhs-operand is compoundExpression, then get
                // LiteralExpressions and make each array declStatement
                if (inputDeclstmt.getBinaryExpression().getRhsOperand() instanceof CompoundExpression) {
                    Integer arrIndex = 0; // check array index
                    for (Expression expr : ((CompoundExpression) (inputDeclstmt.getBinaryExpression().getRhsOperand()))
                            .getExpression()) {
                        if (expr instanceof LiteralExpression) {
                            BinaryExpression binExpr = new BinaryExpression(); // deep-copy
                            CompoundExpression lhsExpr = new CompoundExpression(); // (example) arr[1]
                            // variable name
                            lhsExpr.AddExpression(inputDeclstmt.getBinaryExpression().getLhsOperand());
                            // bracket start expression
                            BracketExpression bracketStart = new BracketExpression();
                            bracketStart.setType("[]");
                            bracketStart.setPosition(true);
                            lhsExpr.AddExpression(bracketStart);
                            // literal expression
                            LiteralExpression arrayIndexExpr = new LiteralExpression(String.valueOf(arrIndex));
                            arrIndex += 1;
                            lhsExpr.AddExpression(arrayIndexExpr);
                            // bracket end expression
                            BracketExpression bracketEnd = new BracketExpression();
                            bracketEnd.setType("[]");
                            bracketEnd.setPosition(false);
                            lhsExpr.AddExpression(bracketEnd);

                            binExpr.setLhsOperand(lhsExpr);
                            binExpr.setOperator("=");
                            binExpr.setRhsOperand(expr);

                            ExpressionStatement exprstmt = new ExpressionStatement();
                            exprstmt.setExpression(binExpr);
                            exprStmtList.add(exprstmt);
                        }
                    }
                }
                // if BinaryExpression's lhs-operand is IdExpression that contains array
                // element, then make each array declStatment assigned 0
                else if (getArrMap().containsKey(inputDeclstmt.getBinaryExpression().getLhsOperand().getCode())) {
                    String lhsVarName = inputDeclstmt.getBinaryExpression().getLhsOperand().getCode();
                    ArrayList<CompoundExpression> bracketList = new ArrayList<CompoundExpression>();

                    for (int i = 0; i < (getArrMap().get(lhsVarName)); i++) {
                        BinaryExpression binExpr = new BinaryExpression(); // deep-copy
                        CompoundExpression lhsExpr = new CompoundExpression(); // (example) arr[1]
                        // variable name
                        lhsExpr.AddExpression(inputDeclstmt.getBinaryExpression().getLhsOperand());
                        // bracket start expression
                        BracketExpression bracketStart = new BracketExpression();
                        bracketStart.setType("[]");
                        bracketStart.setPosition(true);
                        lhsExpr.AddExpression(bracketStart);
                        // literal expression
                        LiteralExpression arrayIndexExpr = new LiteralExpression(String.valueOf(i));
                        lhsExpr.AddExpression(arrayIndexExpr);
                        // bracket end expression
                        BracketExpression bracketEnd = new BracketExpression();
                        bracketEnd.setType("[]");
                        bracketEnd.setPosition(false);
                        lhsExpr.AddExpression(bracketEnd);

                        binExpr.setLhsOperand(lhsExpr);
                        binExpr.setOperator("=");
                        binExpr.setRhsOperand(new LiteralExpression("0"));

                        ExpressionStatement exprstmt = new ExpressionStatement();
                        exprstmt.setExpression(binExpr);
                        exprStmtList.add(exprstmt);
                    }
                }
                // if BinaryExpression has no rhs-operand, then assign 0
                else if (inputDeclstmt.getBinaryExpression().getRhsOperand() instanceof NullExpression) {
                    // 2023-10-27(Fri) SoheeJung
                    // if rhsOperand is NullExpression and lhsOperand is ArrayExpression, then
                    // process this part
                    if (inputDeclstmt.getBinaryExpression().getLhsOperand().getCode().contains("[")
                            && inputDeclstmt.getBinaryExpression().getLhsOperand().getCode().contains("]")) {
                        String tempExprCode = inputDeclstmt.getBinaryExpression().getLhsOperand().getCode();
                        String str1 = tempExprCode.split("\\[")[1];
                        Integer indexNum = Integer.valueOf(str1.split("]")[0]);

                        for (int i = 0; i < indexNum; i++) {
                            BinaryExpression binExpr = new BinaryExpression(); // deep-copy
                            CompoundExpression lhsExpr = new CompoundExpression(); // (example) arr[1]
                            // variable name
                            CompoundExpression cpdExpr = (CompoundExpression) (inputDeclstmt.getBinaryExpression()
                                    .getLhsOperand());
                            IdExpression idExpr = (IdExpression) (cpdExpr.getExpression().get(0));
                            lhsExpr.AddExpression(idExpr);
                            // bracket start expression
                            BracketExpression bracketStart = new BracketExpression();
                            bracketStart.setType("[]");
                            bracketStart.setPosition(true);
                            lhsExpr.AddExpression(bracketStart);
                            // literal expression
                            LiteralExpression arrayIndexExpr = new LiteralExpression(String.valueOf(i));
                            lhsExpr.AddExpression(arrayIndexExpr);
                            // bracket end expression
                            BracketExpression bracketEnd = new BracketExpression();
                            bracketEnd.setType("[]");
                            bracketEnd.setPosition(false);
                            lhsExpr.AddExpression(bracketEnd);

                            binExpr.setLhsOperand(lhsExpr);
                            binExpr.setOperator("=");
                            binExpr.setRhsOperand(new LiteralExpression("0"));

                            ExpressionStatement exprstmt = new ExpressionStatement();
                            exprstmt.setExpression(binExpr);
                            exprStmtList.add(exprstmt);
                        }
                    }
                    // if rhsOperand is NullExpression and lhsOperand is not ArrayExpression, then
                    // process this part
                    else {
                        boolean makeExprCheck = true;
                        BinaryExpression binExpr = new BinaryExpression(); // deep-copy
                        // 2023-11-01(Wed) SoheeJung : if variable is struct,then don't make expression
                        // statement
                        for (String nameNick : getStructMap().keySet()) {
                            IdExpression idExpr = (IdExpression) (inputDeclstmt.getBinaryExpression().getLhsOperand());
                            if (nameNick.contains(idExpr.getType().toString())) {
                                makeExprCheck = false;
                            }
                        }

                        if (makeExprCheck) {
                            binExpr.setLhsOperand(inputDeclstmt.getBinaryExpression().getLhsOperand());
                            binExpr.setOperator("=");
                            binExpr.setRhsOperand(new LiteralExpression("0"));

                            ExpressionStatement exprstmt = new ExpressionStatement();
                            exprstmt.setExpression(binExpr);
                            exprStmtList.add(exprstmt);
                        }
                        /* struct variable initialize print finish */
                    }
                } else {
                    ExpressionStatement exprstmt = new ExpressionStatement();
                    exprstmt.setExpression(inputDeclstmt.getBinaryExpression());
                    exprStmtList.add(exprstmt);
                }
            }
        }

        // add ExressionStatement to model_initailize function
        CompoundStatement initializedBody = new CompoundStatement(); // function consists of a compound statement
        PseudoStatement pseudoStatement1 = new PseudoStatement(); // function first statement
        PseudoStatement pseudoStatement2 = new PseudoStatement(); // function final statement

        // relation connection
        pseudoStatement1.setNext(exprStmtList.get(0));
        exprStmtList.get(0).addBefore(pseudoStatement1);
        for (int i = 0; i < exprStmtList.size(); i++) {
            if (i != 0)
                exprStmtList.get(i).addBefore(exprStmtList.get(i - 1));
            if (i != (exprStmtList.size() - 1))
                exprStmtList.get(i).setNext(exprStmtList.get(i + 1));
        }
        exprStmtList.get(exprStmtList.size() - 1).setNext(pseudoStatement2);
        pseudoStatement2.addBefore(exprStmtList.get(exprStmtList.size() - 1));

        // add all the statement to model_initialize function's body
        initializedBody.addBody(pseudoStatement1);
        for (ExpressionStatement e : exprStmtList) {
            initializedBody.addBody(e);
        }
        initializedBody.addBody(pseudoStatement2);

        // set function's first statement : pseudoStatement1
        // set function's last statement : pseudoStatement2
        initializedBody.setFirst(pseudoStatement1);
        initializedBody.setLast(pseudoStatement2);

        // set model_initialize function's body
        initializeModel.setBody(initializedBody);
        udbInfoRetriever.getFunctions().add(initializeModel);
    }

    /**
     * @date 2023-03-24(Fri)
     * @author SoheeJung
     * @name makeModelCFile
     * @param udbInfoRetriever : get CFG information
     * @throws IOException
     *                     1. Change from target function name to 'model_step'
     *                     2. make model_initialize function to initialize all the
     *                     variable
     *                     (save all the DeclarationStatement information, then make
     *                     model_initialize function.)
     *                     3. In the case of interest variables, it is changed from
     *                     the original variable name to the structure variable
     *                     name.
     *                     (travse parsetree, and fine idExpression related with
     *                     interest variable, then new annotation addition for
     *                     them.)
     *                     4. In the case of printf statement, make first and then
     *                     copy and paste this statement whenever user want.
     */
    public void makeModelCFile(UdbInfoRetriever udbInfoRetriever, HashMap<Function, Integer> numberOfParam,
            HashMap<Function, Boolean> typeOfFunc, ArrayList<String> defines, String directoryPath) throws IOException {
        // 1. Change target function name
        for (Function f : udbInfoRetriever.getFunctions()) {
            if (f.getName().equals(getTargetFunc())) {
                f.setName("model_step");
                f.setReturn_type("void");
            }
        }

        // 2. make model_initailize function
        makeModelInitialize(udbInfoRetriever);

        // 3. change from original variable to structure variable name
        modelCVariableChanger(udbInfoRetriever, directoryPath);
    }

    /**
     * @date 2023-04-09(Sun)
     * @author SoheeJung
     * @name modelCVariableChanger
     * @param udbInfoRetriever
     * @throws IOException
     *                     1. traverse statement first.
     *                     1-1. which statement is to be changed.
     *                     (1) expression statement
     *                     (2) statement contains interest variable
     *                     (3) statement contains scanf or rand function call
     *                     2. traverse expression.
     *                     2-1. If the above conditions are met, change
     *                     idExpression.
     */
    public void modelCVariableChanger(UdbInfoRetriever udbInfoRetriever, String directoryPath) throws IOException {
        File file = new File(directoryPath + "/model.c");
        PrintWriter writer = new PrintWriter(new FileWriter(file));

        IdChangeTraverser idChangeTraverser = new IdChangeTraverser();
        for (Function f : udbInfoRetriever.getFunctions()) {
            idChangeTraverser.traverse(f, getInputVarList(), getStateVarList(), getOutputVarList());
        }

        // 2023-04-09(Sun) SoheeJung
        // Code Print to 'model.c'
        for (Define d : udbInfoRetriever.getDefines()) { // define print
            writer.println(d.getRawdata());
        }
        writer.println();

        for (Include i : udbInfoRetriever.getHeaders()) { // include print
            writer.println(i.getRawdata());
        }
        writer.println("#include \"model.h\"");
        writer.println();

        writer.println("INPUT rt_input;");
        writer.println("STATE rt_state;");
        writer.println("OUTPUT rt_output;");
        writer.println();

        for (DeclarationStatement s : udbInfoRetriever.getGlobals()) { // global variable print
            for (Expression e : s.getDeclExpression()) {
                // 2023-05-01(Mon) SoheeJung
                // if DeclarationStatement have rt_*.{variable} form, then no print.
                if ((e.getCode().contains("rt_input.")) || (e.getCode().contains("rt_state."))
                        || (e.getCode().contains("rt_output.")))
                    continue;

                // 2024-02-18(Sun) SoheeJung : if DeclarationStatement contain structure information that is interst variable, then no print.
                boolean interstStructCheck = false;
                for (String structName : getStructVarScope().keySet()) {
                    String tempName = structName.split("\\.")[0];
                    if(e.getCode().contains(tempName)) {
                        interstStructCheck = true;
                        break;
                    }
                }
                if(interstStructCheck) continue;

                // 2023-10-19(Thu) SoheeJung
                // if Expression's instance is not TypedefExpression and Expression contains
                // bracketExpression, then print specific position
                if (!(e instanceof TypedefExpression)) {
                    // if Expression contains bracketExpression
                    if (e.getCode().contains("[") && e.getCode().contains("]") && e.getCode().contains("{")
                            && e.getCode().contains("}")) {

                        String[] globalArrayStringlist = e.getCode().split(" ");

                        writer.print(globalArrayStringlist[0] + " ");
                        writer.print(globalArrayStringlist[2]);
                        writer.print(globalArrayStringlist[1] + " ");

                        for (int ind = 3; ind < globalArrayStringlist.length - 1; ind++) {
                            writer.print(globalArrayStringlist[ind] + " ");
                        }

                        writer.println(globalArrayStringlist[globalArrayStringlist.length - 1] + ";");
                    } else
                        writer.println(e.getCode() + ";");
                }
            }
        }
        writer.println();

        // 2023-09-11(Mon) SoheeJung : enumeration string array mapping
        for (String strKey : getEnumMap().keySet()) {
            String[] enumList = getEnumMap().get(strKey);
            writer.print("char * " + strKey + "_str[] = {");
            for (int i = 0; i < enumList.length; i++) {
                // if enumList's last element, then print }
                if (i == enumList.length - 1) {
                    writer.print("\"" + enumList[i] + "\"}");
                }
                // if enumList's non-last element, then print ,
                else {
                    writer.print("\"" + enumList[i] + "\",");
                }
            }
            writer.println(";");
        }
        writer.println();
        /* enumeration string array mapping finish */

        // make printf statement
        String printfString = makePrintfStatement(udbInfoRetriever, writer);
        // System.out.println(printfString);

        CodePrintTraverser codePrintTraverser = new CodePrintTraverser();
        for (Function f : udbInfoRetriever.getFunctions()) {
            codePrintTraverser.traverse(f, writer, getInputVarList(), getStateVarList(), getOutputVarList(),
                    printfString);
            writer.println();
        }
        writer.println();

        writer.close();
    }

    /**
     * @date 2023-04-03(Mon)
     * @author SoheeJung
     * @name makePrintfStatement
     *       make printf string from interest variable list.
     */
    public String makePrintfStatement(UdbInfoRetriever udbInfoRetriever, PrintWriter writer) {
        // 2023-10-23(Mon) SoheeJung
        // Find function that name is "model_step"
        Function modelStepFunction = new Function();
        for (Function function : udbInfoRetriever.getFunctions()) {
            if (function.getName().equals("model_step"))
                modelStepFunction = function;
        }

        // make printf statement to model_step last part
        // PseudoStatement startPseudoStmt =
        // (PseudoStatement)((CompoundStatement)(modelStepFunction.getBody())).getFirst();
        ExpressionStatement printfStmt = new ExpressionStatement();
        LibraryCallExpression libraryPrintf = new LibraryCallExpression();
        libraryPrintf.setFunctionalName("printf");

        String typeStr = "";
        String variableStr = "";
        // 2023-05-25(Thu) SoheeJung
        // if key variable is checking, then make printf format and argument using
        // second element
        boolean keyVarcheck = false;

        // writer.print("printf(\"");

        // 2023-11-02(Thu) SoheeJung : struct key variable check
        if (getStructKeyVar() != null) {
            String tempVarName = getStructKeyVar().replace(".", "_");
            if (getStructVarScope().get(getStructKeyVar()).get(0) == 1) // state
            {
                if (getEnumMap().containsKey(getStructKeyVar())) {
                    typeStr += "%s ";
                    variableStr += tempVarName + "_str[" + "rt_state." + getStructKeyVar() + "], ";
                } else {
                    typeStr += "%d ";
                    variableStr += "rt_state." + getStructKeyVar() + ", ";
                }
            } else if (getStructVarScope().get(getStructKeyVar()).get(0) == 2) // output
            {
                if (getEnumMap().containsKey(getStructKeyVar())) {
                    typeStr += "%s ";
                    variableStr += tempVarName + "_str[" + "rt_output." + getStructKeyVar() + "], ";
                } else {
                    typeStr += "%d ";
                    variableStr += "rt_output." + getStructKeyVar() + ", ";
                }
            }
        }

        // 2023-05-25(Thu) SoheeJung
        // consider key interest variable's type(state or output)
        // if key interest variable is state type, then print state variable first.
        // if key interest variable is output type, then print output variable first.
        else if (getStateVarList().isKeyCheck()) {
            for (Variable v : udbInfoRetriever.getVariables()) {
                if (getStateVarList().getNameList().get(0).equals(v.getName()) && !v.getFileName().equals("NULL")
                        && v.getScope().equals("Global")) {
                    if (v.getType().equals("int") || v.getType().equals("char") || v.getType().contains("int")) {
                        typeStr += "%d ";
                        variableStr += "rt_state." + getStateVarList().getNameList().get(0) + ", ";
                    } else {
                        if (getEnumMap().containsKey(v.getName())) {
                            typeStr += "%s ";
                            variableStr += v.getName() + "_str[" + "rt_state." + getStateVarList().getNameList().get(0)
                                    + "], ";
                        }
                    }
                }
            }
        } else if (getOutputVarList().isKeyCheck()) {
            for (Variable v : udbInfoRetriever.getVariables()) {
                if (getOutputVarList().getNameList().get(0).equals(v.getName()) && !v.getFileName().equals("NULL")
                        && v.getScope().equals("Global")) {
                    if (v.getType().equals("int") || v.getType().equals("char") || v.getType().contains("int")) {
                        typeStr += "%d ";
                        variableStr += "rt_output." + getOutputVarList().getNameList().get(0) + ", ";
                    } else {
                        if (getEnumMap().containsKey(v.getName())) {
                            typeStr += "%s ";
                            variableStr += v.getName() + "_str[" + "rt_output."
                                    + getOutputVarList().getNameList().get(0) + "], ";
                        }
                    }
                }
            }
        }

        for (String s : getStateVarList().getNameList()) {
            // 2023-11-02(Thu) SoheeJung : if interest variable is structure type, then
            // don't make element string
            boolean structCheck = false;
            for (String structName : getStructVarScope().keySet()) {
                if (structName.contains(s))
                    structCheck = true;
            }
            if (structCheck)
                continue;
            /* struct type variable checking finish */

            // 2023-05-25(Thu) SoheeJung
            // key variable checking -> if there is key variable, then make printf start at
            // second element (not first element)
            if (getStateVarList().isKeyCheck() && !keyVarcheck) {
                keyVarcheck = true;
                continue;
            }
            for (Variable v : udbInfoRetriever.getVariables()) {
                if (s.equals(v.getName()) && !v.getFileName().equals("NULL") && v.getScope().equals("Global")) {
                    // 2023-10-23(Mon) SoheeJung
                    // if interest variable is array variable, then make printf type string
                    // iteration time
                    if (getArrMap().containsKey(v.getName())) {
                        for (int repeat = 0; repeat < getArrMap().get(v.getName()); repeat++) {
                            if (v.getType().equals("int") || v.getType().equals("char")
                                    || v.getType().contains("int")) {
                                typeStr += "%d ";
                                variableStr += "rt_state." + s + "[" + String.valueOf(repeat) + "], ";
                            }
                        }
                    }
                    // if interest variable is enumeration type, then make printf with matching
                    // string array
                    else if (getEnumMap().containsKey(v.getName())) {
                        typeStr += "%s ";
                        variableStr += v.getName() + "_str[" + "rt_state." + s + "], ";
                    } else {
                        typeStr += "%d ";
                        variableStr += "rt_state." + v.getName() + ", ";
                    }
                }
            }
        }

        for (String s : getInputVarList().getNameList()) {
            // 2023-11-02(Thu) SoheeJung : if interest variable is structure type, then
            // don't make element string
            boolean structCheck = false;
            for (String structName : getStructVarScope().keySet()) {
                if (structName.contains(s))
                    structCheck = true;
            }
            if (structCheck)
                continue;
            /* struct type variable checking finish */
            for (Variable v : udbInfoRetriever.getVariables()) {
                if (s.equals(v.getName()) && !v.getFileName().equals("NULL") && v.getScope().equals("Global")) {
                    // 2023-10-23(Mon) SoheeJung
                    // if interest variable is array variable, then make printf type string
                    // iteration time
                    if (getArrMap().containsKey(v.getName())) {
                        for (int repeat = 0; repeat < getArrMap().get(v.getName()); repeat++) {
                            if (v.getType().equals("int") || v.getType().equals("char")
                                    || v.getType().contains("int")) {
                                typeStr += "%d ";
                                variableStr += "rt_input." + s + "[" + String.valueOf(repeat) + "], ";
                            }
                        }
                    }
                    // if interest variable is enumeration type, then make printf with matching
                    // string array
                    else if (getEnumMap().containsKey(v.getName())) {
                        typeStr += "%s ";
                        variableStr += v.getName() + "_str[" + "rt_input." + s + "], ";
                    } else {
                        typeStr += "%d ";
                        variableStr += "rt_input." + v.getName() + ", ";
                    }
                }
            }
        }

        for (String s : getOutputVarList().getNameList()) {
            // 2023-11-02(Thu) SoheeJung : if interest variable is structure type, then
            // don't make element string
            boolean structCheck = false;
            for (String structName : getStructVarScope().keySet()) {
                if (structName.contains(s))
                    structCheck = true;
            }
            if (structCheck)
                continue;
            /* struct type variable checking finish */

            // 2023-05-25(Thu) SoheeJung
            // key variable checking -> if there is key variable, then make printf start at
            // second element (not first element)
            if (getOutputVarList().isKeyCheck() && !keyVarcheck) {
                keyVarcheck = true;
                continue;
            }
            for (Variable v : udbInfoRetriever.getVariables()) {
                if (s.equals(v.getName()) && !v.getFileName().equals("NULL") && v.getScope().equals("Global")) {
                    // 2023-10-23(Mon) SoheeJung
                    // if interest variable is array variable, then make printf type string
                    // iteration time
                    if (getArrMap().containsKey(v.getName())) {
                        for (int repeat = 0; repeat < getArrMap().get(v.getName()); repeat++) {
                            if (v.getType().equals("int") || v.getType().equals("char")
                                    || v.getType().contains("int")) {
                                typeStr += "%d ";
                                variableStr += "rt_output." + s + "[" + String.valueOf(repeat) + "], ";
                            }
                        }
                    }
                    // if interest variable is enumeration type, then make printf with matching
                    // string array
                    else if (getEnumMap().containsKey(v.getName())) {
                        typeStr += "%s ";
                        variableStr += v.getName() + "_str[" + "rt_output." + s + "], ";
                    } else {
                        typeStr += "%d ";
                        variableStr += "rt_output." + v.getName() + ", ";
                    }
                }
            }
        }

        // 2023-11-02(Thu) SoheeJung : if variable is struct variable, then add this
        // variable to element string seperatly
        for (String structName : getStructVarScope().keySet())
            if (structName.equals(getStructKeyVar()))
                continue;
            else {
                String tempName = structName.replace(".", "_");

                if (getEnumMap().containsKey(structName)) {
                    if (getStructVarScope().get(structName).get(0) == 0) {
                        typeStr += "%s ";
                        variableStr += tempName + "_str[" + "rt_input." + structName + "], ";
                    } else if (getStructVarScope().get(structName).get(0) == 1) {
                        typeStr += "%s ";
                        variableStr += tempName + "_str[" + "rt_state." + structName + "], ";
                    } else {
                        typeStr += "%s ";
                        variableStr += tempName + "_str[" + "rt_output." + structName + "], ";
                    }
                } else {
                    if (getStructVarScope().get(structName).get(0) == 0) {
                        typeStr += "%d ";
                        variableStr += " rt_input." + structName + ", ";
                    } else if (getStructVarScope().get(structName).get(0) == 1) {
                        typeStr += "%d ";
                        variableStr += " rt_state." + structName + ", ";
                    } else {
                        typeStr += "%d ";
                        variableStr += " rt_output." + structName + ", ";
                    }
                }
            }

        // 2023-11-03(Fri) SoheeJung : if variable is constant type, then add this
        // element to string
        for (String constStr : getConstList()) {
            typeStr += "%d ";
            variableStr += constStr + ", ";
        }
        /* const variable process finish */

        typeStr = typeStr.substring(0, typeStr.length() - 1);
        typeStr += "\\n";
        // writer.print(typeStr);
        // writer.print("\\n\", ");
        variableStr = variableStr.substring(0, variableStr.length() - 2);
        // writer.print(variableStr);

        // 2023-10-23(Mon) SoheeJung
        // printf statement and expression assign part
        CompoundExpression fullPrintfExpr = new CompoundExpression();
        BracketExpression bracketStart = new BracketExpression();
        bracketStart.setType("()");
        bracketStart.setPosition(true);
        LiteralExpression typeExpr = new LiteralExpression("\"" + typeStr + "\"");
        LiteralExpression varExpr = new LiteralExpression(variableStr);
        BracketExpression bracketEnd = new BracketExpression();
        bracketEnd.setType("()");
        bracketEnd.setPosition(false);
        fullPrintfExpr.AddExpression(bracketStart);
        fullPrintfExpr.AddExpression(typeExpr);
        fullPrintfExpr.AddExpression(varExpr);
        fullPrintfExpr.AddExpression(bracketEnd);
        libraryPrintf.addActualParameters(fullPrintfExpr);
        printfStmt.setExpression(libraryPrintf);

        return printfStmt.getExpression().getCode() + ";";

        // writer.print(");");
    }

    /**
     * @date 2023-05-01(Mon)
     * @author SoheeJung
     * @name makeTracePrintf
     *       make printf string from interest variable list in make_trace.c file
     */
    public void makeTracePrintf(HashSet<Variable> variables, PrintWriter writer) {
        // 2023-05-25(Thu) SoheeJung
        // if key variable is checking, then make printf format and argument using
        // second element
        boolean keyVarcheck = false;
        String elementStr = "";

        // writer.print("\t\tprintf(\"//modify");

        // 2023-05-25(Thu) SoheeJung
        // consider key interest variable's type(state or output)
        // if key interest variable is state type, then print state variable first.
        // if key interest variable is output type, then print output variable first.
        // if(getStateVarList().isKeyCheck()) {
        // for(Variable v : variables) {
        // if(getStateVarList().getNameList().get(0).equals(v.getName()) &&
        // !v.getFileName().equals("NULL")) {
        // if(v.getType().equals("int")) {
        // writer.print(" rt_state." + getStateVarList().getNameList().get(0) + ":N");
        // } else {
        // writer.print(" rt_state." + getStateVarList().getNameList().get(0) + ":E");
        // }
        // }
        // }
        // }
        // else if(getOutputVarList().isKeyCheck()) {
        // for(Variable v : variables) {
        // if(getOutputVarList().getNameList().get(0).equals(v.getName()) &&
        // !v.getFileName().equals("NULL")) {
        // if(v.getType().equals("int")) {
        // writer.print(" rt_output." + getOutputVarList().getNameList().get(0) + ":N");
        // } else {
        // writer.print(" rt_output." + getOutputVarList().getNameList().get(0) + ":E");
        // }
        // }
        // }
        // }
        /* First element print finish */

        /* Remain elements print Start (Second element ~ Final element) */
        for (String s : getStateVarList().getNameList()) {
            // 2023-11-02(Thu) SoheeJung : if interest variable is structure type, then
            // don't make element string
            boolean structCheck = false;
            for (String structName : getStructVarScope().keySet()) {
                if (structName.contains(s))
                    structCheck = true;
            }
            if (structCheck)
                continue;
            /* struct type variable checking finish */

            // 2023-05-25(Thu) SoheeJung
            // key variable checking -> if there is key variable, then make printf start at
            // second element (not first element)
            if (getStateVarList().isKeyCheck() && !keyVarcheck) {
                keyVarcheck = true;
                continue;
            }
            for (Variable v : variables) {
                if (s.equals(v.getName()) && !v.getFileName().equals("NULL") && v.getScope().equals("Global")) {
                    if (getArrMap().containsKey(v.getName())) {
                        for (int i = 0; i < getArrMap().get(v.getName()); i++) {
                            if (v.getType().equals("int") || v.getType().equals("char")
                                    || v.getType().contains("int")) {
                                elementStr += " rt_state." + s + "_" + String.valueOf(i) + ":N";
                            } else {
                                elementStr += " rt_state." + s + "_" + String.valueOf(i) + ":E";
                            }
                        }
                    } else {
                        if (v.getType().equals("int") || v.getType().equals("char") || v.getType().contains("int")) {
                            elementStr += " rt_state." + s + ":N";
                        } else {
                            elementStr += " rt_state." + s + ":E";
                        }
                    }
                }
            }
        }

        for (String s : getInputVarList().getNameList()) {
            // 2023-11-02(Thu) SoheeJung : if interest variable is structure type, then
            // don't make element string
            boolean structCheck = false;
            for (String structName : getStructVarScope().keySet()) {
                if (structName.contains(s))
                    structCheck = true;
            }
            if (structCheck)
                continue;
            /* struct type variable checking finish */

            for (Variable v : variables) {
                if (s.equals(v.getName()) && !v.getFileName().equals("NULL") && v.getScope().equals("Global")) {
                    if (getArrMap().containsKey(v.getName())) {
                        for (int i = 0; i < getArrMap().get(v.getName()); i++) {
                            if (v.getType().equals("int") || v.getType().equals("char")
                                    || v.getType().contains("int")) {
                                elementStr += " rt_input." + s + "_" + String.valueOf(i) + ":N";
                            } else {
                                elementStr += " rt_input." + s + "_" + String.valueOf(i) + ":E";
                            }
                        }
                    } else {
                        if (v.getType().equals("int") || v.getType().equals("char") || v.getType().contains("int")) {
                            elementStr += " rt_input." + s + ":N";
                        } else {
                            elementStr += " rt_input." + s + ":E";
                        }
                    }
                }
            }
        }

        for (String s : getOutputVarList().getNameList()) {
            // 2023-11-02(Thu) SoheeJung : if interest variable is structure type, then
            // don't make element string
            boolean structCheck = false;
            for (String structName : getStructVarScope().keySet()) {
                if (structName.contains(s))
                    structCheck = true;
            }
            if (structCheck)
                continue;
            /* struct type variable checking finish */

            // 2023-05-25(Thu) SoheeJung
            // key variable checking -> if there is key variable, then make printf start at
            // second element (not first element)
            if (getOutputVarList().isKeyCheck() && !keyVarcheck) {
                keyVarcheck = true;
                continue;
            }
            for (Variable v : variables) {
                if (s.equals(v.getName()) && !v.getFileName().equals("NULL") && v.getScope().equals("Global")) {
                    if (getArrMap().containsKey(v.getName())) {
                        for (int i = 0; i < getArrMap().get(v.getName()); i++) {
                            if (v.getType().equals("int") || v.getType().equals("char")
                                    || v.getType().contains("int")) {
                                elementStr += " rt_output." + s + "_" + String.valueOf(i) + ":N";
                            } else {
                                elementStr += " rt_output." + s + "_" + String.valueOf(i) + ":E";
                            }
                        }
                    } else {
                        if (v.getType().equals("int") || v.getType().equals("char") || v.getType().contains("int")) {
                            elementStr += " rt_output." + s + ":N";
                        } else {
                            elementStr += " rt_output." + s + ":E";
                        }
                    }
                }
            }
        }

        // 2023-11-02(Thu) SoheeJung : if variable is struct variable, then add this
        // variable to element string seperatly
        for (String structName : getStructVarScope().keySet()) {
            if (structName.equals(getStructKeyVar()))
                continue;

            String finalName = "";
            if (structName.matches("([a-zA-Z0-9_.+-]+)\\[[0-9]\\]")) {
                finalName = structName.replace("[", "_");
                finalName = finalName.replace("]", "");
            } else
                finalName = structName;

            if (getEnumMap().containsKey(structName)) {
                if (getStructVarScope().get(structName).get(0) == 0) {
                    elementStr += " rt_input." + finalName + ":E";
                } else if (getStructVarScope().get(structName).get(0) == 1) {
                    elementStr += " rt_state." + finalName + ":E";
                } else {
                    elementStr += " rt_output." + finalName + ":E";
                }
            } else {
                if (getStructVarScope().get(structName).get(0) == 0) {
                    elementStr += " rt_input." + finalName + ":N";
                } else if (getStructVarScope().get(structName).get(0) == 1) {
                    elementStr += " rt_state." + finalName + ":N";
                } else {
                    elementStr += " rt_output." + finalName + ":N";
                }
            }
        }

        // 2023-11-03(Fri) SoheeJung : if variable is constant type, then add this
        // variable to element string
        for (String constStr : getConstList()) {
            elementStr += " " + constStr + ":N";
        }
        /* constant type processing finish */

        elementStr += "\\n\");";

        // 2023-11-02(Thu) SoheeJung : struct key variable check
        if (getStructKeyVar() != null) {
            if (getEnumMap().containsKey(getStructKeyVar())) {
                for (int i = 0; i < getEnumMap().get(getStructKeyVar()).length; i++) {
                    writer.println("\t\tprintf(\"" + getEnumMap().get(getStructKeyVar())[i] + elementStr);
                }
            } else {
                for (int i = getStructVarScope().get(getStructKeyVar()).get(1); i <= getStructVarScope()
                        .get(getStructKeyVar()).get(2); i++) {
                    writer.println("\t\tprintf(\"" + String.valueOf(i) + elementStr);
                }
            }
        }
        /* struct key variable check finish */

        else if (getStateVarList().isKeyCheck()) {
            for (Variable v : variables) {
                if (getStateVarList().getNameList().get(0).equals(v.getName()) && !v.getFileName().equals("NULL")
                        && v.getScope().equals("Global")) {
                    if (getEnumMap().containsKey(v.getName())) {
                        for (int i = 0; i < getEnumMap().get(v.getName()).length; i++) {
                            writer.println("\t\tprintf(\"" + getEnumMap().get(v.getName())[i] + elementStr);
                        }
                    } else {
                        for (int i = v.getVarScope().get(0); i <= v.getVarScope().get(1); i++) {
                            writer.println("\t\tprintf(\"" + String.valueOf(i) + elementStr);
                        }
                    }
                }
            }
        }

        else if (getOutputVarList().isKeyCheck()) {
            for (Variable v : variables) {
                if (getOutputVarList().getNameList().get(0).equals(v.getName()) && !v.getFileName().equals("NULL")
                        && v.getScope().equals("Global")) {
                    if (getEnumMap().containsKey(v.getName())) {
                        for (int i = 0; i < getEnumMap().get(v.getName()).length; i++) {
                            writer.println("\t\tprintf(\"" + getEnumMap().get(v.getName())[i] + elementStr);
                        }
                    } else {
                        for (int i = v.getVarScope().get(0); i <= v.getVarScope().get(1); i++) {
                            writer.println("\t\tprintf(\"" + String.valueOf(i) + elementStr);
                        }
                    }
                }
            }
        }
    }

    /**
     * @date 2023-05-01(Mon)
     * @author SoheeJung
     * @name makeMakeTraceCFile
     *       make make_trace.c file
     */
    public void makeMakeTraceCFile(HashSet<Variable> variables, String directoryPath) throws IOException {
        File file = new File(directoryPath + "/make_trace.c");
        PrintWriter writer = new PrintWriter(new FileWriter(file));

        writer.println("#include <stddef.h>");
        writer.println("#include <stdio.h>");
        writer.println("#include <stdlib.h>");
        writer.println("#include \"model.h\"");
        writer.println("#include <time.h>\n");

        // 1. get_rand_values function generation
        writer.println("INPUT get_rand_values() {");
        writer.println("\tINPUT in;");
        for (String s : getInputVarList().getNameList()) {
            // 2023-11-01(Wed) SoheeJung : struct variable processing part addition
            if (s.contains(".")) {
                if (getStructVarScope().get(s).get(0) >= 0) {
                    writer.println("\tin." + s + " = rand() % "
                            + String.valueOf(getStructVarScope().get(s).get(1) - getStructVarScope().get(s).get(0) + 1)
                            + " + " + String.valueOf(getStructVarScope().get(s).get(0)) + ";");
                } else {
                    writer.println("\tin." + s + " = rand() % "
                            + String.valueOf(getStructVarScope().get(s).get(1) - getStructVarScope().get(s).get(0) + 1)
                            + " " + String.valueOf(getStructVarScope().get(s).get(0)) + ";");
                }
            }
            /* struct varaible processing finish */
            else {
                boolean structCheck = false;
                for (Variable v : variables) {
                    // 2023-11-01(Wed) SoheeJung : if variable is struct type, then break
                    for (String structStr : getStructMap().keySet()) {
                        if (structStr.contains(v.getType()))
                            structCheck = true;
                    }
                    if (structCheck)
                        break;

                    if (s.equals(v.getName()) && !v.getFileName().equals("NULL") && v.getScope().equals("Global")) {
                        // 2023-10-23(Mon) SoheeJung
                        if (getArrMap().containsKey(v.getName())) {
                            for (int i = 0; i < getArrMap().get(v.getName()); i++) {
                                if (v.getVarScope().get(0) >= 0) {
                                    writer.println("\tin." + v.getName() + "[" + String.valueOf(i) + "] = rand() % "
                                            + String.valueOf(v.getVarScope().get(1) - v.getVarScope().get(0) + 1)
                                            + " + " + String.valueOf(v.getVarScope().get(0)) + ";");
                                } else {
                                    writer.println("\tin." + v.getName() + "[" + String.valueOf(i) + "] = rand() % "
                                            + String.valueOf(v.getVarScope().get(1) - v.getVarScope().get(0) + 1) + " "
                                            + String.valueOf(v.getVarScope().get(0)) + ";");
                                }
                            }
                        } else {
                            // 2023-05-17(Wed) SoheeJung
                            // if variable scope is negative number, then minus policy is necessary.
                            if (v.getVarScope().get(0) >= 0) {
                                writer.println("\tin." + v.getName() + " = rand() % "
                                        + String.valueOf(v.getVarScope().get(1) - v.getVarScope().get(0) + 1) + " + "
                                        + String.valueOf(v.getVarScope().get(0)) + ";");
                            } else {
                                writer.println("\tin." + v.getName() + " = rand() % "
                                        + String.valueOf(v.getVarScope().get(1) - v.getVarScope().get(0) + 1) + " "
                                        + String.valueOf(v.getVarScope().get(0)) + ";");
                            }
                        }
                    }
                }
            }
        }
        writer.println("\treturn in;");
        writer.println("}\n");

        // 2. real trace generation : main function
        writer.println("int main() {");
        writer.println("\tsrand(time(NULL));");
        writer.println("\tint inp[" + String.valueOf(trace_m) + "][" + String.valueOf(trace_n) + "];");
        writer.println("\tint header = 1;");
        writer.println("\tif(header) {");
        writer.println("\t\tprintf(\"types\\n\");");
        makeTracePrintf(variables, writer);
        writer.println("\t\tprintf(\"trace\\n\");");
        writer.println("\t}");
        writer.println("\t/* Initialize model */");
        writer.println("\tint i = 0;");
        writer.println("\twhile(i < sizeof(inp)/sizeof(inp[0])) {");
        writer.println("\t\tint j = 0;");
        writer.println("\t\tSTATE init_state;");
        writer.println("\t\tINPUT init_input;");
        writer.println("\t\tOUTPUT init_output;\n");
        writer.println("\t\trt_state = init_state;");
        writer.println("\t\trt_input = init_input;");
        writer.println("\t\trt_output = init_output;");
        writer.println("\t\tmodel_initialize();\n");
        writer.println("\t\twhile(j < sizeof(inp[0])/sizeof(inp[0][0])) {");
        writer.println("\t\t\trt_input = get_rand_values();");
        writer.println("\t\t\tmodel_step();");
        writer.println("\t\t\tj++;");
        writer.println("\t\t}");
        writer.println("\t\tprintf(\"trace\\n\");");
        writer.println("\t\ti++;");
        writer.println("\t}");
        writer.println("\treturn 0;");
        writer.println("}");
        writer.close();
    }

    /**
     * @date 2023-05-01(Mon)
     * @author SoheeJung
     * @name makeTraceShellFile
     *       make generate_trace.sh file
     */
    public void makeTraceShellFile(String srcPath, String directoryPath) throws IOException {
        File file = new File(directoryPath + "/generate_trace.sh");
        PrintWriter writer = new PrintWriter(new FileWriter(file));

        // 1. remove current target file from compile file list
        // ArrayList<String> srcFilePathList = splitFilePath(srcPath);
        // String srcDirectory = srcFilePathList.get(0) + "/";
        // // 1-1. get all files from code directory
        // File dir = new File(srcDirectory);
        // // 1-2. filter ".c" and ".h" files from code directory
        // FilenameFilter filter = new FilenameFilter() {
        // public boolean accept(File f, String name) {
        // return ((name.contains(".c") || name.contains(".h")) &&
        // !name.contains(".udb"));
        // }
        // };
        // String[] filenames = dir.list(filter);

        // // 2. create generate_trace.sh file
        writer.println("#! /bin/bash");
        writer.println("rm trace1.txt");
        writer.print("gcc");
        // for(String filename : filenames) {
        // // if filename is same as target file name, then continue
        // if(filename.equals(srcFilePathList.get(1))) continue;
        // writer.print(" " + filename);
        // }
        writer.print(" model.c make_trace.c -o a.out -lm\n");
        writer.println("./a.out >> trace1.txt");
        writer.println("cp trace1.txt ../traces/trace1.txt");

        writer.close();
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

    /**
     * @date 2023-05-15(Mon)
     * @new_version_date 2023-05-24(Wed)
     * @author SoheeJung
     * @name makeGenAssumeAssertFromModelPyFile
     * 
     *       <Code>
     *       [Original version] make gen_assume_assert_from_model.py file
     *       (automatically) -> but, assume modify manually
     *       [new version] make gen_assume_assert_from_model.py file automatically
     *       using manually written model.c
     * 
     *       <Policy>
     *       [original version] use types.txt
     *       [modified version] use spec.txt
     */
    private void makeGenAssumeAssertFromModelPyFile(String srcPath, HashSet<Variable> variables, String directoryPath)
            throws IOException {
        File file = new File(directoryPath + "/gen_assume_assert_from_model.py");
        PrintWriter writer = new PrintWriter(new FileWriter(file));

        writer.println("#######################################################################");
        writer.println("# Author: Natasha Yogananda Jeppu, natasha.yogananda.jeppu@cs.ox.ac.uk");
        writer.println("#         University of Oxford");
        writer.println("# Modifier: SoheeJung");
        writer.println("#######################################################################\n");

        writer.println("import sys");
        writer.println("import pickle");
        writer.println("import os");
        writer.println("import subprocess");
        writer.println("from termcolor import colored");
        writer.println("import re");
        writer.println("import ast");
        writer.println("import numpy as np");
        writer.println("import copy");
        writer.println("from string import punctuation\n");

        writer.println("def generate_assume_assert(s1_in_p, pred, s2_out_p, assumptions):");
        writer.println(
                "\tprev_states = [p.split(': ')[1] if (len(p.split(': ')) > 1) else p.split(': ')[0] for p in s1_in_p]");
        writer.println("\tprev_states = list(np.unique(prev_states))");
        writer.println("\tf = open(\"code/code_proof.c\", \"w\")");
        writer.println("\tf.write(\"#include <stddef.h>\\n\")");
        writer.println("\tf.write(\"#include <stdio.h> \\n\")");
        writer.println("\tf.write(\"#include <stdbool.h> \\n\")");
        writer.println("\tf.write(\"#include \\\"model.h\\\"\\n\")");
        writer.println("\tf.write(\"#include \\\"model.c\\\"\\n\")");

        // get all .h file from srcFilePath
        ArrayList<String> srcFilePathList = splitFilePath(srcPath);
        String srcDirectory = srcFilePathList.get(0) + "/";
        // 1. get all files from code directory
        File dir = new File(srcDirectory);
        // 2. filter ".h" files from code directory
        FilenameFilter filter = new FilenameFilter() {
            public boolean accept(File f, String name) {
                return (name.contains(".h"));
            }
        };
        String[] filenames = dir.list(filter);
        // 3. print header files that contains srcPath.
        for (String filename : filenames) {
            // if filename is same as target file name, then continue
            if (filename.equals(srcFilePathList.get(1)))
                continue;
            // print include line about header file that is defined in model.c
            writer.println("\tf.write(\"#include \\\"" + filename + "\\\"\\n\")");
        }

        writer.println();
        writer.println("\tf.write(\"int main()\\n\")");
        writer.println("\tf.write(\"{					\\n\\");
        writer.println("\tSTATE state;				\\n\\");
        writer.println("\tSTATE prev_state;			\\n\\");
        writer.println("\tINPUT input;				\\n\\");
        writer.println("\tINPUT prev_input;			\\n\\");
        writer.println("\tOUTPUT output;				\\n\\");
        writer.println("\tOUTPUT prev_output;			\\n\")");
        writer.println();

        writer.println("\t## Assume STATE / OUTPUT Define Section");
        writer.println("\tf.write(\"\\n\\");
        writer.println("\t// STATE / OUTPUT assume \\n\\");

        // if STATE / OUTPUT interest variable, then make assume statement. (But, this
        // part write the actual value manually)
        for (String s : getStateVarList().getNameList()) {
            // 2023-11-02(Thu) SoheeJung : if interest variable is structure type, then
            // don't make element string
            boolean structCheck = false;
            for (String structName : getStructVarScope().keySet()) {
                if (structName.contains(s))
                    structCheck = true;
            }
            if (structCheck)
                continue;
            /* struct type variable checking finish */

            for (Variable v : variables) {
                if (s.equals(v.getName()) && !v.getFileName().equals("NULL") && v.getScope().equals("Global")) {
                    if (v.getName().equals(s)) {
                        if (getArrMap().containsKey(v.getName())) {
                            for (int i = 0; i < getArrMap().get(v.getName()); i++) {
                                writer.println("\t__CPROVER_assume(prev_state." + s + "[" + String.valueOf(i) + "] >= "
                                        + String.valueOf(v.getVarScope().get(0)) + " && prev_state." + s + "["
                                        + String.valueOf(i) + "] <= " + String.valueOf(v.getVarScope().get(1))
                                        + "); \\n\\");
                            }
                        } else
                            writer.println("\t__CPROVER_assume(prev_state." + s + " >= "
                                    + String.valueOf(v.getVarScope().get(0)) + " && prev_state." + s + " <= "
                                    + String.valueOf(v.getVarScope().get(1)) + "); \\n\\");
                    }
                }
            }
        }
        for (String s : getOutputVarList().getNameList()) {
            // 2023-11-02(Thu) SoheeJung : if interest variable is structure type, then
            // don't make element string
            boolean structCheck = false;
            for (String structName : getStructVarScope().keySet()) {
                if (structName.contains(s))
                    structCheck = true;
            }
            if (structCheck)
                continue;
            /* struct type variable checking finish */

            for (Variable v : variables) {
                if (s.equals(v.getName()) && !v.getFileName().equals("NULL") && v.getScope().equals("Global")) {
                    if (v.getName().equals(s)) {
                        if (getArrMap().containsKey(v.getName())) {
                            for (int i = 0; i < getArrMap().get(v.getName()); i++) {
                                writer.println("\t__CPROVER_assume(prev_output." + s + "[" + String.valueOf(i) + "] >= "
                                        + String.valueOf(v.getVarScope().get(0)) + " && prev_output." + s + "["
                                        + String.valueOf(i) + "] <= " + String.valueOf(v.getVarScope().get(1))
                                        + "); \\n\\");
                            }
                        } else
                            writer.println("\t__CPROVER_assume(prev_output." + s + " >= "
                                    + String.valueOf(v.getVarScope().get(0)) + " && prev_output." + s + " <= "
                                    + String.valueOf(v.getVarScope().get(1)) + "); \\n\\");
                    }
                }
            }
        }

        // 2023-11-02(The) SoheeJung : struct variable assume addition
        for (String structName : getStructVarScope().keySet()) {
            if (getStructVarScope().get(structName).get(0) == 1) { // state
                writer.println("\t__CPROVER_assume(prev_state." + structName + " >= "
                        + String.valueOf(getStructVarScope().get(structName).get(1)) + " && prev_state." + structName
                        + " <= " + String.valueOf(getStructVarScope().get(structName).get(2)) + "); \\n\\");
            } else if (getStructVarScope().get(structName).get(0) == 2) { // output
                writer.println("\t__CPROVER_assume(prev_output." + structName + " >= "
                        + String.valueOf(getStructVarScope().get(structName).get(1)) + " && prev_output." + structName
                        + " <= " + String.valueOf(getStructVarScope().get(structName).get(2)) + "); \\n\\");
            }
        }
        /* struct variable assume finish */

        // for(String s : getStateVarList().getNameList()) {
        // writer.println("\t__CPROVER_assume(state." + s + " >= ? && state." + s + " <=
        // ?); \\n\\");
        // }
        // for(String s : getOutputVarList().getNameList()) {
        // writer.println("\t__CPROVER_assume(output." + s + " >= ? && output." + s + "
        // <= ?); \\n\\");
        // }
        writer.println("\t\")\n");

        writer.println("\t## Assume INPUT Define Section");
        writer.println("\tf.write(\"\\n\\");
        writer.println("\t// INPUT assume \\n\\");

        // if INPUT interest variable, then make assume statement. (automatically)
        for (String s : getInputVarList().getNameList()) {
            // 2023-11-02(Thu) SoheeJung : if interest variable is structure type, then
            // don't make element string
            boolean structCheck = false;
            for (String structName : getStructVarScope().keySet()) {
                if (structName.contains(s))
                    structCheck = true;
            }
            if (structCheck)
                continue;
            /* struct type variable checking finish */

            for (Variable v : variables) {
                if (s.equals(v.getName()) && !v.getFileName().equals("NULL") && v.getScope().equals("Global")) {
                    if (getArrMap().containsKey(v.getName())) {
                        for (int i = 0; i < getArrMap().get(v.getName()); i++) {
                            writer.println("\t__CPROVER_assume(prev_input." + s + "[" + String.valueOf(i) + "] >= "
                                    + String.valueOf(v.getVarScope().get(0)) + " && prev_input." + s + "["
                                    + String.valueOf(i) + "] <= " + String.valueOf(v.getVarScope().get(1))
                                    + "); \\n\\");
                            writer.println("\t__CPROVER_assume(input." + s + "[" + String.valueOf(i) + "] >= "
                                    + String.valueOf(v.getVarScope().get(0)) + " && input." + s + "["
                                    + String.valueOf(i) + "] <= " + String.valueOf(v.getVarScope().get(1))
                                    + "); \\n\\");
                        }
                    } else {
                        writer.println("\t__CPROVER_assume(prev_input." + s + " >= "
                                + String.valueOf(v.getVarScope().get(0)) + " && prev_input." + s + " <= "
                                + String.valueOf(v.getVarScope().get(1)) + "); \\n\\");
                        writer.println("\t__CPROVER_assume(input." + s + " >= " + String.valueOf(v.getVarScope().get(0))
                                + " && input." + s + " <= " + String.valueOf(v.getVarScope().get(1)) + "); \\n\\");
                    }
                }
            }

            // 2023-11-02(The) SoheeJung : struct variable assume addition
            for (String structName : getStructVarScope().keySet()) {
                if (getStructVarScope().get(structName).get(0) == 0) { // input
                    writer.println("\t__CPROVER_assume(prev_input." + structName + " >= "
                            + String.valueOf(getStructVarScope().get(structName).get(1)) + " && prev_input."
                            + structName + " <= " + String.valueOf(getStructVarScope().get(structName).get(2))
                            + "); \\n\\");
                    writer.println("\t__CPROVER_assume(input." + structName + " >= "
                            + String.valueOf(getStructVarScope().get(structName).get(1)) + " && input." + structName
                            + " <= " + String.valueOf(getStructVarScope().get(structName).get(2)) + "); \\n\\");
                }
            }
            /* struct variable assume finish */
        }
        writer.println("\t\")\n");

        writer.println("\tf.write(\"\\n\\");
        writer.println("\trt_state = prev_state;		\\n\\");
        writer.println("\trt_input = prev_input;		\\n\\");
        writer.println("\trt_output = prev_output;	\\n\\");
        writer.println("\tmodel_step();				\\n\\");
        writer.println("\tstate = rt_state;			\\n\\");
        writer.println("\toutput = rt_output;			\\n\\n\")");
        writer.println();

        writer.println("\tp_state_pair = pred.split(': ')");
        writer.println("\ts1 = p_state_pair[0 if len(p_state_pair) == 1 else 1]\n");

        writer.println("\tif len(p_state_pair) == 1:");
        writer.println("\t\ts1 = p_state_pair[0]");

        // 2023-11-02(Thu) SoheeJung : struct key variable part
        if (getStructKeyVar() != null) {
            if (getStructVarScope().get(getStructKeyVar()).get(0) == 1) { // state
                writer.println("\t\tv_str = 'state." + getStructKeyVar() + " == ' + s1");
            } else if (getStructVarScope().get(getStructKeyVar()).get(0) == 2) { // output
                writer.println("\t\tv_str = 'output." + getStructKeyVar() + " == ' + s1");
            }
        }
        /* struct key variable part finish */

        // 2023-05-25(Thu) SoheeJung
        // find key interest variable from state / output list, then make statement
        else if (getStateVarList().isKeyCheck())
            writer.println("\t\tv_str = 'state." + getStateVarList().getNameList().get(0) + " == ' + s1");
        else if (getOutputVarList().isKeyCheck())
            writer.println("\t\tv_str = 'output." + getOutputVarList().getNameList().get(0) + " == ' + s1");
        /* Finish Modification */

        writer.println("\t\tf.write(\"   __CPROVER_assume((\" + v_str + \"));\\n\")");
        writer.println("\telse:");
        writer.println("\t\tp = p_state_pair[0]");
        writer.println("\t\ts1 = p_state_pair[1]");
        writer.println("\t\tp = p.replace(' and ',' && ')");
        writer.println("\t\tp = p.replace(' or ',' || ')");
        writer.println("\t\tp = p.replace('rt_input','prev_input')");
        writer.println("\t\tp = p.replace('rt_output','prev_output')");
        writer.println("\t\tp = p.replace('not ','!')\n");

        writer.println("\t\t## Array interest variable processing (Hyobin)");
        writer.println("\t\tpred_vars = list(np.unique(re.findall(r\"[a-zA-Z_][a-zA-Z0-9_]*\", p)))");
        writer.println("\t\tfor pred_var in pred_vars :");
        writer.println("\t\t\tif pred_var not in targetConvertedNames: continue");
        writer.println("\t\t\ti = targetConvertedNames.index(pred_var)");
        writer.println("\t\t\tp = p.replace(targetConvertedNames[i], targetNames[i])\n");

        // 2023-11-02(Thu) SoheeJung : struct key variable part
        if (getStructKeyVar() != null) {
            if (getStructVarScope().get(getStructKeyVar()).get(0) == 1) { // state
                writer.println("\t\tv_str = 'state." + getStructKeyVar() + " == ' + s1");
            } else if (getStructVarScope().get(getStructKeyVar()).get(0) == 2) { // output
                writer.println("\t\tv_str = 'output." + getStructKeyVar() + " == ' + s1");
            }
        }
        /* struct key variable part finish */

        // 2023-05-25(Thu) SoheeJung
        // find key interest variable from state / output list, then make statement
        else if (getStateVarList().isKeyCheck())
            writer.println("\t\tv_str = 'state." + getStateVarList().getNameList().get(0) + " == ' + s1");
        else if (getOutputVarList().isKeyCheck())
            writer.println("\t\tv_str = 'output." + getOutputVarList().getNameList().get(0) + " == ' + s1");
        /* Finish Modification */

        writer.println("\t\tf.write(\"  __CPROVER_assume((\" + v_str + \")  && \" + p)");
        writer.println("\t\tif prev_states:");
        writer.println("\t\t\tf.write(\" && (\")");
        writer.println("\t\t\tfor i in range(len(prev_states)):");
        writer.println("\t\t\t\ts = prev_states[i]");

        // 2023-11-02(Thu) SoheeJung : struct key variable part
        if (getStructKeyVar() != null) {
            if (getStructVarScope().get(getStructKeyVar()).get(0) == 1) { // state
                writer.println("\t\t\t\tv_str = 'state." + getStructKeyVar() + " == ' + s");
            } else if (getStructVarScope().get(getStructKeyVar()).get(0) == 2) { // output
                writer.println("\t\t\t\tv_str = 'output." + getStructKeyVar() + " == ' + s");
            }
        }
        /* struct key variable part finish */

        // 2023-05-25(Thu) SoheeJung
        // find key interest variable from state / output list, then make statement
        else if (getStateVarList().isKeyCheck())
            writer.println("\t\t\t\tv_str = 'state." + getStateVarList().getNameList().get(0) + " == ' + s");
        else if (getOutputVarList().isKeyCheck())
            writer.println("\t\t\t\tv_str = 'output." + getOutputVarList().getNameList().get(0) + " == ' + s");
        /* Finish Modification */

        writer.println("\t\t\t\tf.write(\"(\" + v_str + \")\")");
        writer.println("\t\t\t\tif i < len(prev_states)-1:");
        writer.println("\t\t\t\t\tf.write(\" || \")");
        writer.println("\t\t\tf.write(\"));\\n\")");
        writer.println("\t\telse:");
        writer.println("\t\t\tf.write(\");\\n\")\n");

        writer.println("\tif assumptions:");
        writer.println("\t\tfor ass in assumptions:");
        writer.println("\t\t\tf.write('\\t' + ass)");
        writer.println("\t\t\tf.write('\\n')");
        writer.println("\t\tf.write('\\n')");

        writer.println("\tf.write(\"	while(1)	\\n\\");
        writer.println("\t{						\\n\\");
        writer.println("\t\tbool found = false;	\\n\\");
        writer.println("\t\t					\\n\\");
        writer.println("\t\trt_state = state;	\\n\\");
        writer.println("\t\trt_input = input;	\\n\\");
        writer.println("\t\trt_output = output;	\\n\\");
        writer.println("\t\t					\\n\\");
        writer.println("\t\tmodel_step();		\\n\\");
        writer.println("\t\t					\\n\\");
        writer.println("\t\t// new input assume		\\n\\");
        writer.println("\t\tINPUT new_input;	\\n\\");

        // if INPUT interest variable, then make new_input assume statement.
        // (automatically)
        for (String s : getInputVarList().getNameList()) {
            // 2023-11-02(Thu) SoheeJung : if interest variable is structure type, then
            // don't make element string
            boolean structCheck = false;
            for (String structName : getStructVarScope().keySet()) {
                if (structName.contains(s))
                    structCheck = true;
            }
            if (structCheck)
                continue;
            /* struct type variable checking finish */

            for (Variable v : variables) {
                if (s.equals(v.getName()) && !v.getFileName().equals("NULL") && v.getScope().equals("Global")) {
                    if (getArrMap().containsKey(v.getName())) {
                        for (int i = 0; i < getArrMap().get(v.getName()); i++) {
                            writer.println("\t\t__CPROVER_assume(new_input." + s + "[" + String.valueOf(i) + "] >= "
                                    + String.valueOf(v.getVarScope().get(0)) + " && new_input." + s + "["
                                    + String.valueOf(i) + "] <= " + String.valueOf(v.getVarScope().get(1))
                                    + "); \\n\\");
                        }
                    } else
                        writer.println("\t\t__CPROVER_assume(new_input." + s + " >= "
                                + String.valueOf(v.getVarScope().get(0)) + " && new_input." + s + " <= "
                                + String.valueOf(v.getVarScope().get(1)) + "); \\n\\");
                }
            }
        }

        // 2023-11-02(The) SoheeJung : struct variable assume addition
        for (String structName : getStructVarScope().keySet()) {
            if (getStructVarScope().get(structName).get(0) == 0) { // input
                writer.println("\t\t__CPROVER_assume(new_input." + structName + " >= "
                        + String.valueOf(getStructVarScope().get(structName).get(1)) + " && new_input." + structName
                        + " <= " + String.valueOf(getStructVarScope().get(structName).get(2)) + "); \\n\\");
            }
        }
        /* struct variable assume finish */
        writer.println("\t\t\")\n");

        // 2023-05-25(Thu) SoheeJung
        // find key interest variable from state / output list, then make statement
        writer.println("\tf.write(\"\\t\" + \"printf(\\\"OUTPUT: %d \" + \" \".join(targetExprs) + \"\\\\n\\\", \\n\\");

        // 2023-11-02(Thu) SoheeJung : struct key variable check
        if (getStructKeyVar() != null) {
            if (getStructVarScope().get(getStructKeyVar()).get(0) == 1) { // state
                writer.println("\t\tstate." + getStructKeyVar() + ", \" + \", \".join(targetNamesOutput) + \");\\n\")");
            } else if (getStructVarScope().get(getStructKeyVar()).get(0) == 2) { // output
                writer.println(
                        "\t\toutput." + getStructKeyVar() + ", \" + \", \".join(targetNamesOutput) + \");\\n\")");
            }
        } else if (getStateVarList().isKeyCheck()) {
            // if(getTopElement().contains(getStateVarList().getNameList().get(0))) {
            // writer.println("\t\tstate." + getStateVarList().getNameList().get(0) + ", \"
            // + \", \".join(targetNamesOutput) + \");\\n\")");
            // } else {
            // writer.println("\t\trt_state." + getStateVarList().getNameList().get(0) + ",
            // \" + \", \".join(targetNamesOutput) + \");\\n\")");
            // }
            writer.println("\t\tstate." + getStateVarList().getNameList().get(0)
                    + ", \" + \", \".join(targetNamesOutput) + \");\\n\")");
        } else if (getOutputVarList().isKeyCheck()) {
            // if(getTopElement().contains(getOutputVarList().getNameList().get(0))) {
            // writer.println("\t\toutput." + getOutputVarList().getNameList().get(0) + ",
            // \" + \", \".join(targetNamesOutput) + \");\\n\")");
            // }
            // else {
            // writer.println("\t\trt_output." + getOutputVarList().getNameList().get(0) +
            // ", \" + \", \".join(targetNamesOutput) + \");\\n\")");
            // }
            writer.println("\t\toutput." + getOutputVarList().getNameList().get(0)
                    + ", \" + \", \".join(targetNamesOutput) + \");\\n\")");
        }

        writer.println("\tf.write(\"\\t\" + \"printf(\\\"OUTPUT: %d \" + \" \".join(targetExprs) + \"\\\\n\\\", \\n\\");

        // 2023-11-02(Thu) SoheeJung : struct key variable check
        if (getStructKeyVar() != null) {
            if (getStructVarScope().get(getStructKeyVar()).get(0) == 1) { // state
                writer.println(
                        "\t\trt_state." + getStructKeyVar() + ", \" + \", \".join(targetNamesOutput) + \");\\n\")");
            } else if (getStructVarScope().get(getStructKeyVar()).get(0) == 2) { // output
                writer.println(
                        "\t\trt_output." + getStructKeyVar() + ", \" + \", \".join(targetNamesOutput) + \");\\n\")");
            }
        } else if (getStateVarList().isKeyCheck())
            writer.println("\t\trt_state." + getStateVarList().getNameList().get(0)
                    + ", \" + \", \".join(targetNamesRTStructed) + \");\\n\")");
        else if (getOutputVarList().isKeyCheck())
            writer.println("\t\trt_output." + getOutputVarList().getNameList().get(0)
                    + ", \" + \", \".join(targetNamesRTStructed) + \");\\n\")");

        // 2023-11-02(Thu) SoheeJung : struct key variable check
        if (getStructKeyVar() != null) {
            if (getStructVarScope().get(getStructKeyVar()).get(0) == 1) { // state
                writer.println("\tf.write(\"\\t\" + \"printf(\\\"PROP: assert(!(state." + getStructKeyVar()
                        + " == %d && rt_state." + getStructKeyVar()
                        + " == %d && \" + \" && \".join(targetProp) + \"));\\\", \\n\"\\");
            } else if (getStructVarScope().get(getStructKeyVar()).get(0) == 2) { // output
                writer.println("\tf.write(\"\\t\" + \"printf(\\\"PROP: assert(!(output." + getStructKeyVar()
                        + " == %d && rt_output." + getStructKeyVar()
                        + " == %d && \" + \" && \".join(targetProp) + \"));\\\", \\n\"\\");
            }
        } else if (getStateVarList().isKeyCheck()) {
            // if(getTopElement().contains(getStateVarList().getNameList().get(0))) {
            // writer.println("\tf.write(\"\\t\" + \"printf(\\\"PROP: assert(!(state." +
            // getStateVarList().getNameList().get(0) + " == %d && rt_state." +
            // getStateVarList().getNameList().get(0) + " == %d && \" + \" &&
            // \".join(targetProp) + \"));\\\", \\n\"\\");
            // }
            // else {
            // writer.println("\tf.write(\"\\t\" + \"printf(\\\"PROP: assert(!(rt_state." +
            // getStateVarList().getNameList().get(0) + " == %d && rt_state." +
            // getStateVarList().getNameList().get(0) + " == %d && \" + \" &&
            // \".join(targetProp) + \"));\\\", \\n\"\\");
            // }
            writer.println(
                    "\tf.write(\"\\t\" + \"printf(\\\"PROP: assert(!(state." + getStateVarList().getNameList().get(0)
                            + " == %d && rt_state." + getStateVarList().getNameList().get(0)
                            + " == %d && \" + \" && \".join(targetProp) + \"));\\\", \\n\"\\");
        } else if (getOutputVarList().isKeyCheck()) {
            // if(getTopElement().contains(getOutputVarList().getNameList().get(0))) {
            // writer.println("\tf.write(\"\\t\" + \"printf(\\\"PROP: assert(!(output." +
            // getOutputVarList().getNameList().get(0) + " == %d && rt_output." +
            // getOutputVarList().getNameList().get(0) + " == %d && \" + \" &&
            // \".join(targetProp) + \"));\\\", \\n\"\\");
            // }
            // else {
            // writer.println("\tf.write(\"\\t\" + \"printf(\\\"PROP: assert(!(rt_output." +
            // getOutputVarList().getNameList().get(0) + " == %d && rt_output." +
            // getOutputVarList().getNameList().get(0) + " == %d && \" + \" &&
            // \".join(targetProp) + \"));\\\", \\n\"\\");
            // }
            writer.println(
                    "\tf.write(\"\\t\" + \"printf(\\\"PROP: assert(!(output." + getOutputVarList().getNameList().get(0)
                            + " == %d && rt_output." + getOutputVarList().getNameList().get(0)
                            + " == %d && \" + \" && \".join(targetProp) + \"));\\\", \\n\"\\");
        }

        // 2023-11-02(Thu) SoheeJung : struct key variable check
        if (getStructKeyVar() != null) {
            if (getStructVarScope().get(getStructKeyVar()).get(0) == 1) { // state
                writer.println("\t\t+ \"prev_state." + getStructKeyVar() + ", state." + getStructKeyVar()
                        + ", \" + \", \".join(targetNamesPrevStructed) + \");\\n\")");
            } else if (getStructVarScope().get(getStructKeyVar()).get(0) == 2) { // output
                writer.println("\t\t+ \"prev_output." + getStructKeyVar() + ", output." + getStructKeyVar()
                        + ", \" + \", \".join(targetNamesPrevStructed) + \");\\n\")");
            }
        } else if (getStateVarList().isKeyCheck()) {
            // if(getTopElement().contains(getStateVarList().getNameList().get(0))) {
            // writer.println("\t\t+ \"prev_state." + getStateVarList().getNameList().get(0)
            // + ", state." + getStateVarList().getNameList().get(0) + ", \" + \",
            // \".join(targetNamesPrevStructed) + \");\\n\")");
            // }
            // else {
            // writer.println("\t\t+ \"state." + getStateVarList().getNameList().get(0) + ",
            // state." + getStateVarList().getNameList().get(0) + ", \" + \",
            // \".join(targetNamesPrevStructed) + \");\\n\")");
            // }
            writer.println("\t\t+ \"prev_state." + getStateVarList().getNameList().get(0) + ", state."
                    + getStateVarList().getNameList().get(0)
                    + ", \" + \", \".join(targetNamesPrevStructed) + \");\\n\")");
        } else if (getOutputVarList().isKeyCheck()) {
            // if(getTopElement().contains(getOutputVarList().getNameList().get(0))) {
            // writer.println("\t\t+ \"prev_output." +
            // getOutputVarList().getNameList().get(0) + ", output." +
            // getOutputVarList().getNameList().get(0) + ", \" + \",
            // \".join(targetNamesPrevStructed) + \");\\n\")");
            // }
            // else {
            // writer.println("\t\t+ \"output." + getOutputVarList().getNameList().get(0) +
            // ", output." + getOutputVarList().getNameList().get(0) + ", \" + \",
            // \".join(targetNamesPrevStructed) + \");\\n\")");
            // }
            writer.println("\t\t+ \"prev_output." + getOutputVarList().getNameList().get(0) + ", output."
                    + getOutputVarList().getNameList().get(0)
                    + ", \" + \", \".join(targetNamesPrevStructed) + \");\\n\")");
        }

        // 2023-11-02(Thu) SoheeJung : struct key variable check
        if (getStructKeyVar() != null) {
            if (getStructVarScope().get(getStructKeyVar()).get(0) == 1) { // state
                writer.println("\tf.write(\"\\t\" + \"printf(\\\"ASSUME: __CPROVER_assume(!(prev_state."
                        + getStructKeyVar() + " == %d && state." + getStructKeyVar()
                        + " == %d && \" + \" && \".join(targetAssume) + \"));\\\", \\n\"\\");
            } else if (getStructVarScope().get(getStructKeyVar()).get(0) == 2) { // output
                writer.println("\tf.write(\"\\t\" + \"printf(\\\"ASSUME: __CPROVER_assume(!(prev_output."
                        + getStructKeyVar() + " == %d && output." + getStructKeyVar()
                        + " == %d && \" + \" && \".join(targetAssume) + \"));\\\", \\n\"\\");
            }
        } else if (getStateVarList().isKeyCheck()) {
            // if(getTopElement().contains(getStateVarList().getNameList().get(0))) {
            // writer.println("\tf.write(\"\\t\" + \"printf(\\\"ASSUME:
            // __CPROVER_assume(!(prev_state." + getStateVarList().getNameList().get(0) + "
            // == %d && state." + getStateVarList().getNameList().get(0) + " == %d && \" +
            // \" && \".join(targetAssume) + \"));\\\", \\n\"\\");
            // }
            // else {
            // writer.println("\tf.write(\"\\t\" + \"printf(\\\"ASSUME:
            // __CPROVER_assume(!(state." + getStateVarList().getNameList().get(0) + " == %d
            // && state." + getStateVarList().getNameList().get(0) + " == %d && \" + \" &&
            // \".join(targetAssume) + \"));\\\", \\n\"\\");
            // }
            writer.println("\tf.write(\"\\t\" + \"printf(\\\"ASSUME: __CPROVER_assume(!(prev_state."
                    + getStateVarList().getNameList().get(0) + " == %d && state."
                    + getStateVarList().getNameList().get(0)
                    + " == %d && \" + \" && \".join(targetAssume) + \"));\\\", \\n\"\\");
        } else if (getOutputVarList().isKeyCheck()) {
            // if(getTopElement().contains(getOutputVarList().getNameList().get(0))) {
            // writer.println("\tf.write(\"\\t\" + \"printf(\\\"ASSUME:
            // __CPROVER_assume(!(prev_output." + getOutputVarList().getNameList().get(0) +
            // " == %d && output." + getOutputVarList().getNameList().get(0) + " == %d && \"
            // + \" && \".join(targetAssume) + \"));\\\", \\n\"\\");
            // }
            // else {
            // writer.println("\tf.write(\"\\t\" + \"printf(\\\"ASSUME:
            // __CPROVER_assume(!(output." + getOutputVarList().getNameList().get(0) + " ==
            // %d && output." + getOutputVarList().getNameList().get(0) + " == %d && \" + \"
            // && \".join(targetAssume) + \"));\\\", \\n\"\\");
            // }
            writer.println("\tf.write(\"\\t\" + \"printf(\\\"ASSUME: __CPROVER_assume(!(prev_output."
                    + getOutputVarList().getNameList().get(0) + " == %d && output."
                    + getOutputVarList().getNameList().get(0)
                    + " == %d && \" + \" && \".join(targetAssume) + \"));\\\", \\n\"\\");
        }

        // 2023-11-02(Thu) SoheeJung : struct key variable check
        if (getStructKeyVar() != null) {
            if (getStructVarScope().get(getStructKeyVar()).get(0) == 1) { // state
                writer.println("\t\t+ \"prev_state." + getStructKeyVar() + ", state." + getStructKeyVar()
                        + ", \" + \", \".join(targetNamesPrevStructed) + \");\\n\")\n");
            } else if (getStructVarScope().get(getStructKeyVar()).get(0) == 2) { // output
                writer.println("\t\t+ \"prev_output." + getStructKeyVar() + ", output." + getStructKeyVar()
                        + ", \" + \", \".join(targetNamesPrevStructed) + \");\\n\")\n");
            }
        } else if (getStateVarList().isKeyCheck()) {
            // if(getTopElement().contains(getStateVarList().getNameList().get(0))) {
            // writer.println("\t\t+ \"prev_state." + getStateVarList().getNameList().get(0)
            // + ", state." + getStateVarList().getNameList().get(0) + ", \" + \",
            // \".join(targetNamesPrevStructed) + \");\\n\")\n");
            // }
            // else {
            // writer.println("\t\t+ \"state." + getStateVarList().getNameList().get(0) + ",
            // state." + getStateVarList().getNameList().get(0) + ", \" + \",
            // \".join(targetNamesPrevStructed) + \");\\n\")\n");
            // }
            writer.println("\t\t+ \"prev_state." + getStateVarList().getNameList().get(0) + ", state."
                    + getStateVarList().getNameList().get(0)
                    + ", \" + \", \".join(targetNamesPrevStructed) + \");\\n\")\n");
        } else if (getOutputVarList().isKeyCheck()) {
            // if(getTopElement().contains(getOutputVarList().getNameList().get(0))) {
            // writer.println("\t\t+ \"prev_output." +
            // getOutputVarList().getNameList().get(0) + ", output." +
            // getOutputVarList().getNameList().get(0) + ", \" + \",
            // \".join(targetNamesPrevStructed) + \");\\n\")\n");
            // }
            // else {
            // writer.println("\t\t+ \"output." + getOutputVarList().getNameList().get(0) +
            // ", output." + getOutputVarList().getNameList().get(0) + ", \" + \",
            // \".join(targetNamesPrevStructed) + \");\\n\")\n");
            // }
            writer.println("\t\t+ \"prev_output." + getOutputVarList().getNameList().get(0) + ", output."
                    + getOutputVarList().getNameList().get(0)
                    + ", \" + \", \".join(targetNamesPrevStructed) + \");\\n\")\n");
        }
        /* Finish Modification */

        writer.println("\tpredicates = {}");
        writer.println("\tfor pred in s2_out_p:");
        writer.println("\t\tp_state_pair = pred.split(': ')");
        writer.println("\t\tif len(p_state_pair) == 1:");
        writer.println("\t\t\tpredicates[p_state_pair[0]] = 0");
        writer.println("\t\telse:");
        writer.println("\t\t\tp = p_state_pair[0]");
        writer.println("\t\t\ts1 = p_state_pair[1]");
        writer.println("\t\t\tpredicates[s1] = p\n");

        writer.println("\tnext_states_pred = [x for x in predicates if predicates[x]!=0]");
        writer.println("\tnext_states_not_pred = [x for x in predicates if x not in next_states_pred]\n");

        writer.println("\tif not next_states_pred:");
        writer.println("\t\tif not next_states_not_pred:");
        writer.println("\t\t\tf.write(\"		assert(false);\\n\")");
        writer.println("\t\telse:");
        writer.println("\t\t\tf.write(\"		assert(\")");
        writer.println("\t\t\tfor i in range(len(next_states_not_pred)):");
        writer.println("\t\t\t\ts = next_states_not_pred[i]");

        // 2023-11-02(Thu) SoheeJung : struct key variable check
        if (getStructKeyVar() != null) {
            if (getStructVarScope().get(getStructKeyVar()).get(0) == 1) { // state
                writer.println("\t\t\t\tv_str = 'rt_state." + getStructKeyVar() + " == ' + s");
            } else if (getStructVarScope().get(getStructKeyVar()).get(0) == 2) { // output
                writer.println("\t\t\t\tv_str = 'rt_output." + getStructKeyVar() + " == ' + s");
            }
        }
        // 2023-05-25(Thu) SoheeJung
        // find key interest variable from state / output list, then make statement
        else if (getStateVarList().isKeyCheck())
            writer.println("\t\t\t\tv_str = 'rt_state." + getStateVarList().getNameList().get(0) + " == ' + s");
        else if (getOutputVarList().isKeyCheck())
            writer.println("\t\t\t\tv_str = 'rt_output." + getOutputVarList().getNameList().get(0) + " == ' + s");
        /* Finish Modification */

        writer.println("\t\t\t\tf.write(\"(\" + v_str + \")\")");
        writer.println("\t\t\t\tif i != len(next_states_not_pred)-1:");
        writer.println("\t\t\t\t\tf.write(\" || \")");
        writer.println("\t\t\tf.write(\");\\n\")");
        writer.println("\telse:");
        writer.println("\t\tfor i in range(len(next_states_pred)):");
        writer.println("\t\t\tx = next_states_pred[i]");
        writer.println("\t\t\tpredicates[x] = predicates[x].replace(' and ',' && ')");
        writer.println("\t\t\tpredicates[x] = predicates[x].replace(' or ',' || ')");
        writer.println("\t\t\tpredicates[x] = predicates[x].replace('not ','!')");
        writer.println("\t\t\tpredicates[x] = predicates[x].replace('rt_input','input')");
        writer.println("\t\t\tpredicates[x] = predicates[x].replace('prev_state','state')");
        writer.println("\t\t\tpredicates[x] = predicates[x].replace('rt_state','state')\n");

        writer.println("\t\t\t## Array interest variable processing (Hyobin)");
        writer.println("\t\t\tpred_vars = list(np.unique(re.findall(r\"[a-zA-Z_][a-zA-Z0-9_]*\", predicates[x])))");
        writer.println("\t\t\tfor pred_var in pred_vars :");
        writer.println("\t\t\t\tif pred_var not in targetConvertedNames: continue");
        writer.println("\t\t\t\ti = targetConvertedNames.index(pred_var)");
        writer.println("\t\t\t\tpredicates[x] = predicates[x].replace(targetConvertedNames[i], targetNames[i])\n");

        writer.println("\t\t\tf.write(\"		if (\" + predicates[x] + \")\\n\")");

        // 2023-11-02(Thu) SoheeJung : struct key variable check
        if (getStructKeyVar() != null) {
            if (getStructVarScope().get(getStructKeyVar()).get(0) == 1) { // state
                writer.println("\t\t\tv_str = 'rt_state." + getStructKeyVar() + " == ' + x");
            } else if (getStructVarScope().get(getStructKeyVar()).get(0) == 2) { // output
                writer.println("\t\t\tv_str = 'rt_output." + getStructKeyVar() + " == ' + x");
            }
        }
        // 2023-05-25(Thu) SoheeJung
        // find key interest variable from state / output list, then make statement
        else if (getStateVarList().isKeyCheck())
            writer.println("\t\t\tv_str = 'rt_state." + getStateVarList().getNameList().get(0) + " == ' + x");
        else if (getOutputVarList().isKeyCheck())
            writer.println("\t\t\tv_str = 'rt_output." + getOutputVarList().getNameList().get(0) + " == ' + x");
        /* Finish Modification */

        writer.println("\t\t\tf.write(\"		{	assert(\" + v_str + \");\\n\")");
        writer.println("\t\t\tf.write(\"			found = true;}\\n\")\n");

        writer.println("\t\tf.write(\"		if (!found)\\n\")");
        writer.println("\t\tif not next_states_not_pred:");
        writer.println("\t\t\tf.write(\"			assert(false);\\n\")");
        writer.println("\t\telse:");
        writer.println("\t\t\tf.write(\"			assert(\")");
        writer.println("\t\t\tfor i in range(len(next_states_not_pred)):");
        writer.println("\t\t\t\ts = next_states_not_pred[i]");

        // 2023-11-02(Thu) SoheeJung : struct key variable check
        if (getStructKeyVar() != null) {
            if (getStructVarScope().get(getStructKeyVar()).get(0) == 1) { // state
                writer.println("\t\t\t\tv_str = 'rt_state." + getStructKeyVar() + "== ' + s");
            } else if (getStructVarScope().get(getStructKeyVar()).get(0) == 2) { // output
                writer.println("\t\t\t\tv_str = 'rt_output." + getStructKeyVar() + "== ' + s");
            }
        }
        // 2023-05-25(Thu) SoheeJung
        // find key interest variable from state / output list, then make statement
        else if (getStateVarList().isKeyCheck())
            writer.println("\t\t\t\tv_str = 'rt_state." + getStateVarList().getNameList().get(0) + "== ' + s");
        else if (getOutputVarList().isKeyCheck())
            writer.println("\t\t\t\tv_str = 'rt_output." + getOutputVarList().getNameList().get(0) + "== ' + s");
        /* Finish Modification */

        writer.println("\t\t\t\tf.write(\"(\" + v_str + \")\")");
        writer.println("\t\t\t\tif i != len(next_states_not_pred)-1:");
        writer.println("\t\t\t\t\tf.write(\" || \")");
        writer.println("\t\t\tf.write(\");\\n\")\n");

        writer.println("\tf.write(\"	}\\n\")");
        writer.println("\tf.write(\"}\\n\")");
        writer.println("\tf.close()\n");

        writer.println("def run_code_proof():");
        writer.println(
                "\tp = subprocess.Popen('goto-cc code/code_proof.c code/model.c -o code/code_proof.goto', stdout=subprocess.PIPE, stderr=subprocess.PIPE, shell=True)");
        writer.println("\toutput,o_err = p.communicate()");
        writer.println("\tp.kill()");
        writer.println("\tif o_err and 'error' in str(o_err):");
        writer.println("\t\tprint(o_err)");
        writer.println("\t\texit(0)\n");

        // 2023-05-25(Thu) SoheeJung
        // make unwind option using spec.txt information
        writer.println("\tp = subprocess.Popen('cbmc code/code_proof.goto --unwind " + getUnwind() + " --object-bits "
                + getObject_bits()
                + " --trace > out_assume_assert.txt', stdout=subprocess.PIPE, stderr=subprocess.PIPE, shell=True)");

        writer.println("\ttry:");
        writer.println("\t\toutput,o_err = p.communicate()");
        writer.println("\t\tp.kill()");
        writer.println("\t\tif o_err and 'error' in str(o_err):");
        writer.println("\t\t\tprint(o_err)");
        writer.println("\t\t\texit(0)");
        writer.println("\texcept subprocess.TimeoutExpired:");
        writer.println("\t\tp.kill()");
        writer.println("\t\tprint(colored(\"[WARNING] TIMEOUT\",'magenta'))");
        writer.println("\texcept subprocess.CalledProcessError:");
        writer.println("\t\tp.kill()");
        writer.println("\t\tprint(colored(\"[ERROR] FAILED\",'magenta'))\n");

        writer.println("def get_trace():");
        writer.println("\t## enumeration variable dictionary definition");
        // 2023-10-24(Tue) SoheeJung
        // array string definition part addition
        for (String strKey : getEnumMap().keySet()) {
            String[] enumList = getEnumMap().get(strKey);
            writer.print("\t" + strKey + "_str = [");
            for (int i = 0; i < enumList.length; i++) {
                // if enumList's last element, then print }
                if (i == enumList.length - 1) {
                    writer.print("\"" + enumList[i] + "\"]");
                }
                // if enumList's non-last element, then print ,
                else {
                    writer.print("\"" + enumList[i] + "\",");
                }
            }
            writer.println();
        }
        writer.println();
        /* array string definition part finish */

        // writer.println("\t## state_str = [?]\n");
        writer.println("\tf = open(\"out_assume_assert.txt\",'r')");
        writer.println("\tlines = f.readlines()");
        writer.println("\tf.close()\n");

        writer.println("\tif any([\"VERIFICATION SUCCESSFUL\" in line for line in lines]):");
        writer.println("\t\treturn [], True\n");

        writer.println("\tnew_trace = []");
        writer.println("\tnew_traces = []");
        writer.println("\tcount = 0");
        writer.println("\tfor line in lines:");
        writer.println("\t\tif \"OUTPUT: \" in line and \"format\" not in line:");
        writer.println("\t\t\ttemp = line.replace(\"OUTPUT: \",\"\")");
        writer.println("\t\t\ttemp = [int(x) for x in temp.split()]");
        writer.println("\t\t\t## enumeration value and string array matching");

        // 2023-10-24(Tue) SoheeJung
        // make tempInterest variable list (save all the interest variable to know
        // index)
        ArrayList<String> tempInterestVarList = new ArrayList<String>();
        // 2023-11-02(Thu) SoheeJung : struct key variable check
        if (getStructKeyVar() != null)
            tempInterestVarList.add(getStructKeyVar());
        else if (getStateVarList().isKeyCheck())
            tempInterestVarList.add(getStateVarList().getNameList().get(0));
        else if (getOutputVarList().isKeyCheck())
            tempInterestVarList.add(getOutputVarList().getNameList().get(0));

        if (getStateVarList().isKeyCheck()) {
            for (int i = 1; i < getStateVarList().getNameList().size(); i++) {
                // 2023-11-02(Thu) SoheeJung : if interest variable is structure type, then
                // don't make element string
                boolean structCheck = false;
                for (String structName : getStructVarScope().keySet()) {
                    if (structName.contains(getStateVarList().getNameList().get(i)))
                        structCheck = true;
                }
                if (structCheck)
                    continue;
                /* struct type variable checking finish */

                if (getArrMap().containsKey(getStateVarList().getNameList().get(i))) {
                    for (int j = 0; j < getArrMap().get(getStateVarList().getNameList().get(i)); j++) {
                        tempInterestVarList.add(getStateVarList().getNameList().get(i));
                    }
                } else
                    tempInterestVarList.add(getStateVarList().getNameList().get(i));
            }
        } else {
            for (int i = 0; i < getStateVarList().getNameList().size(); i++) {
                // 2023-11-02(Thu) SoheeJung : if interest variable is structure type, then
                // don't make element string
                boolean structCheck = false;
                for (String structName : getStructVarScope().keySet()) {
                    if (structName.contains(getStateVarList().getNameList().get(i)))
                        structCheck = true;
                }
                if (structCheck)
                    continue;
                /* struct type variable checking finish */

                if (getArrMap().containsKey(getStateVarList().getNameList().get(i))) {
                    for (int j = 0; j < getArrMap().get(getStateVarList().getNameList().get(i)); j++) {
                        tempInterestVarList.add(getStateVarList().getNameList().get(i));
                    }
                } else
                    tempInterestVarList.add(getStateVarList().getNameList().get(i));
            }
        }

        for (int i = 0; i < getInputVarList().getNameList().size(); i++) {
            // 2023-11-02(Thu) SoheeJung : if interest variable is structure type, then
            // don't make element string
            boolean structCheck = false;
            for (String structName : getStructVarScope().keySet()) {
                if (structName.contains(getInputVarList().getNameList().get(i)))
                    structCheck = true;
            }
            if (structCheck)
                continue;
            /* struct type variable checking finish */

            if (getArrMap().containsKey(getInputVarList().getNameList().get(i))) {
                for (int j = 0; j < getArrMap().get(getInputVarList().getNameList().get(i)); j++) {
                    tempInterestVarList.add(getInputVarList().getNameList().get(i));
                }
            } else
                tempInterestVarList.add(getInputVarList().getNameList().get(i));
        }

        if (getOutputVarList().isKeyCheck()) {
            for (int i = 1; i < getOutputVarList().getNameList().size(); i++) {
                // 2023-11-02(Thu) SoheeJung : if interest variable is structure type, then
                // don't make element string
                boolean structCheck = false;
                for (String structName : getStructVarScope().keySet()) {
                    if (structName.contains(getOutputVarList().getNameList().get(i)))
                        structCheck = true;
                }
                if (structCheck)
                    continue;
                /* struct type variable checking finish */

                if (getArrMap().containsKey(getOutputVarList().getNameList().get(i))) {
                    for (int j = 0; j < getArrMap().get(getOutputVarList().getNameList().get(i)); j++) {
                        tempInterestVarList.add(getOutputVarList().getNameList().get(i));
                    }
                } else
                    tempInterestVarList.add(getOutputVarList().getNameList().get(i));
            }
        } else {
            for (int i = 0; i < getOutputVarList().getNameList().size(); i++) {
                // 2023-11-02(Thu) SoheeJung : if interest variable is structure type, then
                // don't make element string
                boolean structCheck = false;
                for (String structName : getStructVarScope().keySet()) {
                    if (structName.contains(getOutputVarList().getNameList().get(i)))
                        structCheck = true;
                }
                if (structCheck)
                    continue;
                /* struct type variable checking finish */

                if (getArrMap().containsKey(getOutputVarList().getNameList().get(i))) {
                    for (int j = 0; j < getArrMap().get(getOutputVarList().getNameList().get(i)); j++) {
                        tempInterestVarList.add(getOutputVarList().getNameList().get(i));
                    }
                } else
                    tempInterestVarList.add(getOutputVarList().getNameList().get(i));
            }
        }

        for (String structName : getStructVarScope().keySet()) {
            if (structName.equals(getStructKeyVar()))
                continue;
            else
                tempInterestVarList.add(structName);
        }
        /* tempInterstVarList saving finish */

        // 2023-10-24(Tue) SoheeJung
        // string array and trace information mapping
        for (int i = 0; i < tempInterestVarList.size(); i++) {
            if (getEnumMap().containsKey(tempInterestVarList.get(i))) {
                writer.println("\t\t\ttemp[" + String.valueOf(i) + "] = " + tempInterestVarList.get(i) + "_str[temp["
                        + String.valueOf(i) + "]]");
            }
        }

        // writer.println("\t\t\t## temp[0] = state_str[temp[0]] ?");
        writer.println("\t\t\ttemp = [str(x) for x in temp]\n");

        writer.println("\t\t\tcount = count + 1");
        writer.println("\t\t\tnew_trace.append(temp)");
        writer.println("\t\t\tif count == 2:");
        writer.println("\t\t\t\tnew_traces.append(new_trace)");
        writer.println("\t\t\t\tcount = 0");
        writer.println("\t\t\t\tnew_trace = []\n");
        writer.println("\treturn new_traces, False\n");

        writer.println("def check_validity_code(prop):");
        writer.println("\tf = open(\"code/code.c\", \"w\")");
        writer.println("\tf.write(\"#include <stddef.h> \\n\")");
        writer.println("\tf.write(\"#include <stdio.h> \\n\")");
        writer.println("\tf.write(\"#include <stdbool.h> \\n\")");
        writer.println("\tf.write(\"#include \\\"model.h\\\"\\n\")");
        writer.println("\tf.write(\"#include \\\"model.c\\\"\\n\")");

        // print header files that contains srcPath.
        for (String filename : filenames) {
            // if filename is same as target file name, then continue
            if (filename.equals(srcFilePathList.get(1)))
                continue;
            // print include line about header file that is defined in model.c
            writer.println("\tf.write(\"#include \\\"" + filename + "\\\"\\n\")");
        }
        writer.println();

        writer.println("\tf.write(\"int main()\\n\")");
        writer.println("\tf.write(\"{						\\n\\");
        writer.println("\tmodel_initialize();\\n\")\n");

        writer.println("\tf.write(\"	while(1)			\\n\\");
        writer.println("\t{								\\n\\");
        writer.println("\t\tOUTPUT output = rt_output;	\\n\\");
        writer.println("\t\tSTATE state = rt_state;		\\n\\");
        writer.println("\t\tINPUT input;				\\n\\");
        writer.println("\t\trt_input = input;			\\n\\");
        writer.println("\t\tmodel_step();				\\n\\");
        writer.println("\t\t							\\n\")\n");

        writer.println("\tf.write(\"		\" + prop + \";\\n\")");
        writer.println("\tf.write(\"	}\\n\")");
        writer.println("\tf.write(\"}\\n\")");
        writer.println("\tf.close()\n");

        writer.println("def get_prop_assumption():");
        writer.println("\tf = open(\"out_assume_assert.txt\",'r')");
        writer.println("\tlines = f.readlines()");
        writer.println("\tf.close()\n");

        writer.println("\tif any([\"too many addressed objects\" in line for line in lines]):");
        writer.println("\t\texit(0)\n");
        writer.println("\tif any([\"PARSING ERROR\" in line for line in lines]):");
        writer.println("\t\texit(0)\n");
        writer.println("\tif any([\"VERIFICATION SUCCESSFUL\" in line for line in lines]):");
        writer.println("\t\treturn [], []\n");

        writer.println("\tprop = []");
        writer.println("\tassumptions = []");
        writer.println("\tfor line in lines:");
        writer.println("\t\tif \"PROP: \" in line and \"format\" not in line:");
        writer.println("\t\t\tprop.append(line.replace(\"PROP: \",\"\"))");
        writer.println("\t\tif \"ASSUME: \" in line and \"format\" not in line:");
        writer.println("\t\t\tassumptions.append(line.replace(\"ASSUME: \",\"\"))\n");
        writer.println("\treturn prop, assumptions\n");

        writer.println("def get_model(model_file):");
        writer.println("\tf = open(model_file,'r')");
        writer.println("\tlines = f.readlines()");
        writer.println("\tf.close()\n");
        writer.println("\tind = [i for i in range(len(lines)) if \"Number of states:\" in lines[i]][-1]\n");
        writer.println("\tmodel = re.findall('\\[\\[.*\\]\\]', lines[ind-1])[0]");
        writer.println("\tmodel = model.replace('\\\\\\'','\\'')");
        writer.println("\treturn ast.literal_eval(model)\n");

        writer.println("def get_prop_assumption_str(val):");
        writer.println("\treturn val\n");

        writer.println("def gen_check_init_proof(s2_out_p):");
        writer.println("\tf = open(\"code/code_proof.c\", \"w\")");
        writer.println("\tf.write(\"#include <stddef.h>\\n\")");
        writer.println("\tf.write(\"#include <stdio.h> \\n\")");
        writer.println("\tf.write(\"#include <stdbool.h> \\n\")");
        writer.println("\tf.write(\"#include\\\"model.h\\\"\\n\")");
        writer.println("\tf.write(\"#include \\\"model.c\\\"\\n\")");

        // print header files that contains srcPath.
        for (String filename : filenames) {
            // if filename is same as target file name, then continue
            if (filename.equals(srcFilePathList.get(1)))
                continue;
            // print include line about header file that is defined in model.c
            writer.println("\tf.write(\"#include \\\"" + filename + "\\\"\\n\")");
        }
        writer.println();

        writer.println("\tf.write(\"int main()\\n\")");
        writer.println("\tf.write(\"{			\\n\\");
        writer.println("\tmodel_initialize();	\\n\\");
        writer.println("\t					\\n\\");
        writer.println("\tINPUT input;		\\n\\");
        writer.println("\tINPUT prev_input;	\\n\\");
        writer.println("\t					\\n\")\n");

        writer.println("\tf.write(\"\\n\\");
        writer.println("\t// INPUT assume \\n\\");

        // if INPUT interest variable, then make assume statement. (automatically)
        for (String s : getInputVarList().getNameList()) {
            // 2023-11-02(Thu) SoheeJung : if interest variable is structure type, then
            // don't make element string
            boolean structCheck = false;
            for (String structName : getStructVarScope().keySet()) {
                if (structName.contains(s))
                    structCheck = true;
            }
            if (structCheck)
                continue;
            /* struct type variable checking finish */

            for (Variable v : variables) {
                if (s.equals(v.getName()) && !v.getFileName().equals("NULL") && v.getScope().equals("Global")) {
                    if (getArrMap().containsKey(v.getName())) {
                        for (int i = 0; i < getArrMap().get(v.getName()); i++) {
                            writer.println("\t__CPROVER_assume(prev_input." + s + "[" + String.valueOf(i) + "] >= "
                                    + String.valueOf(v.getVarScope().get(0)) + " && prev_input." + s + "["
                                    + String.valueOf(i) + "] <= " + String.valueOf(v.getVarScope().get(1))
                                    + "); \\n\\");
                            writer.println("\t__CPROVER_assume(input." + s + "[" + String.valueOf(i) + "] >= "
                                    + String.valueOf(v.getVarScope().get(0)) + " && input." + s + "["
                                    + String.valueOf(i) + "] <= " + String.valueOf(v.getVarScope().get(1))
                                    + "); \\n\\");
                        }
                    } else {
                        writer.println("\t__CPROVER_assume(prev_input." + s + " >= "
                                + String.valueOf(v.getVarScope().get(0)) + " && prev_input." + s + " <= "
                                + String.valueOf(v.getVarScope().get(1)) + "); \\n\\");
                        writer.println("\t__CPROVER_assume(input." + s + " >= " + String.valueOf(v.getVarScope().get(0))
                                + " && input." + s + " <= " + String.valueOf(v.getVarScope().get(1)) + "); \\n\\");
                    }
                }
            }
        }

        // 2023-11-02(The) SoheeJung : struct variable assume addition
        for (String structName : getStructVarScope().keySet()) {
            if (getStructVarScope().get(structName).get(0) == 0) { // input
                writer.println("\t__CPROVER_assume(prev_input." + structName + " >= "
                        + String.valueOf(getStructVarScope().get(structName).get(1)) + " && prev_input." + structName
                        + " <= " + String.valueOf(getStructVarScope().get(structName).get(2)) + "); \\n\\");
                writer.println("\t__CPROVER_assume(input." + structName + " >= "
                        + String.valueOf(getStructVarScope().get(structName).get(1)) + " && input." + structName
                        + " <= " + String.valueOf(getStructVarScope().get(structName).get(2)) + "); \\n\\");
            }
        }
        /* struct variable assume finish */
        writer.println("\t\")\n");

        writer.println("\tf.write(\"	while(1)			\\n\\");
        writer.println("\t{								\\n\\");
        writer.println("\t\tbool found = false;			\\n\\");
        writer.println("\t\tSTATE state = rt_state;		\\n\\");
        writer.println("\t\tOUTPUT output = rt_output;	\\n\\");
        writer.println("\t\trt_input = input;			\\n\\");
        writer.println("\t\t							\\n\\");
        writer.println("\t\tmodel_step();				\\n\\");
        writer.println("\t\t							\\n\\");
        writer.println("\t\t// new input assume		\\n\\");
        writer.println("\t\tINPUT new_input;	\\n\\");

        // if INPUT interest variable, then make new_input assume statement.
        // (automatically)
        for (String s : getInputVarList().getNameList()) {
            // 2023-11-02(Thu) SoheeJung : if interest variable is structure type, then
            // don't make element string
            boolean structCheck = false;
            for (String structName : getStructVarScope().keySet()) {
                if (structName.contains(s))
                    structCheck = true;
            }
            if (structCheck)
                continue;
            /* struct type variable checking finish */

            for (Variable v : variables) {
                if (s.equals(v.getName()) && !v.getFileName().equals("NULL") && v.getScope().equals("Global")) {
                    if (getArrMap().containsKey(v.getName())) {
                        for (int i = 0; i < getArrMap().get(v.getName()); i++) {
                            writer.println("\t\t__CPROVER_assume(new_input." + s + "[" + String.valueOf(i) + "] >= "
                                    + String.valueOf(v.getVarScope().get(0)) + " && new_input." + s + "["
                                    + String.valueOf(i) + "] <= " + String.valueOf(v.getVarScope().get(1))
                                    + "); \\n\\");
                        }
                    } else
                        writer.println("\t\t__CPROVER_assume(new_input." + s + " >= "
                                + String.valueOf(v.getVarScope().get(0)) + " && new_input." + s + " <= "
                                + String.valueOf(v.getVarScope().get(1)) + "); \\n\\");
                }
            }
        }

        // 2023-11-02(The) SoheeJung : struct variable assume addition
        for (String structName : getStructVarScope().keySet()) {
            if (getStructVarScope().get(structName).get(0) == 0) { // input
                writer.println("\t__CPROVER_assume(new_input." + structName + " >= "
                        + String.valueOf(getStructVarScope().get(structName).get(1)) + " && new_input." + structName
                        + " <= " + String.valueOf(getStructVarScope().get(structName).get(2)) + "); \\n\\");
            }
        }
        /* struct variable assume finish */
        writer.println("\t\t\")\n");

        writer.println("\tf.write(\"\\");
        writer.println(
                "\t\tprintf(\\\"OUTPUT: %d \" + \" \".join(targetExprs) + \"\\\\n\\\",						\\n\\");

        // 2023-11-02(Thu) SoheeJung : struct key variable check
        if (getStructKeyVar() != null) {
            if (getStructVarScope().get(getStructKeyVar()).get(0) == 1) { // state
                writer.println("\t\t\tstate." + getStructKeyVar()
                        + ",\" + ','.join(targetNamesOutput) + \");			\\n\")");
            } else if (getStructVarScope().get(getStructKeyVar()).get(0) == 2) { // output
                writer.println("\t\t\toutput." + getStructKeyVar()
                        + ",\" + ','.join(targetNamesOutput) + \");			\\n\")");
            }
        }
        // 2023-05-25(Thu) SoheeJung
        // find key interest variable from state / output list, then make statement
        else if (getStateVarList().isKeyCheck())
            writer.println("\t\t\tstate." + getStateVarList().getNameList().get(0)
                    + ",\" + ','.join(targetNamesOutput) + \");			\\n\")");
        else if (getOutputVarList().isKeyCheck())
            writer.println("\t\t\toutput." + getOutputVarList().getNameList().get(0)
                    + ",\" + ','.join(targetNamesOutput) + \");			\\n\")");
        /* Finish Modification */

        writer.println("\tf.write(\"\\");
        writer.println(
                "\t\tprintf(\\\"OUTPUT: %d \" + \" \".join(targetExprs) + \"\\\\n\\\",						\\n\\");

        // 2023-11-02(Thu) SoheeJung : struct key variable check
        if (getStructKeyVar() != null) {
            if (getStructVarScope().get(getStructKeyVar()).get(0) == 1) { // state
                writer.println("\t\t\trt_state." + getStructKeyVar()
                        + ",\" + ','.join(targetNamesRTStructed) + \");			\\n\")");
            } else if (getStructVarScope().get(getStructKeyVar()).get(0) == 2) { // output
                writer.println("\t\t\trt_output." + getStructKeyVar()
                        + ",\" + ','.join(targetNamesRTStructed) + \");			\\n\")");
            }
        }
        // 2023-05-25(Thu) SoheeJung
        // find key interest variable from state / output list, then make statement
        else if (getStateVarList().isKeyCheck())
            writer.println("\t\t\trt_state." + getStateVarList().getNameList().get(0)
                    + ",\" + ','.join(targetNamesRTStructed) + \");			\\n\")");
        else if (getOutputVarList().isKeyCheck())
            writer.println("\t\t\trt_output." + getOutputVarList().getNameList().get(0)
                    + ",\" + ','.join(targetNamesRTStructed) + \");			\\n\")");
        /* Finish Modification */
        writer.println();

        writer.println("\tpredicates = {}");
        writer.println("\tfor pred in s2_out_p:");
        writer.println("\t\tp_state_pair = pred.split(': ')");
        writer.println("\t\tif len(p_state_pair) == 1:");
        writer.println("\t\t\tpredicates[p_state_pair[0]] = 0");
        writer.println("\t\telse:");
        writer.println("\t\t\tp = p_state_pair[0]");
        writer.println("\t\t\ts1 = p_state_pair[1]");
        writer.println("\t\t\tpredicates[s1] = p\n");
        writer.println("\tnext_states_pred = [x for x in predicates if predicates[x]!=0]");
        writer.println("\tnext_states_not_pred = [x for x in predicates if x not in next_states_pred]\n");
        writer.println("\tif not next_states_pred:");
        writer.println("\t\tif not next_states_not_pred:");
        writer.println("\t\t\tf.write(\"		assert(false);\\n\")");
        writer.println("\t\telse:");
        writer.println("\t\t\tf.write(\"		assert(\")");
        writer.println("\t\t\tfor i in range(len(next_states_not_pred)):");
        writer.println("\t\t\t\ts = next_states_not_pred[i]");

        // 2023-11-02(Thu) SoheeJung : struct key variable check
        if (getStructKeyVar() != null) {
            if (getStructVarScope().get(getStructKeyVar()).get(0) == 1) { // state
                writer.println("\t\t\t\tv_str = 'rt_state." + getStructKeyVar() + " == ' + s");
            } else if (getStructVarScope().get(getStructKeyVar()).get(0) == 2) { // output
                writer.println("\t\t\t\tv_str = 'rt_output." + getStructKeyVar() + " == ' + s");
            }
        }
        // 2023-05-25(Thu) SoheeJung
        // find key interest variable from state / output list, then make statement
        else if (getStateVarList().isKeyCheck())
            writer.println("\t\t\t\tv_str = 'rt_state." + getStateVarList().getNameList().get(0) + " == ' + s");
        else if (getOutputVarList().isKeyCheck())
            writer.println("\t\t\t\tv_str = 'rt_output." + getOutputVarList().getNameList().get(0) + " == ' + s");
        /* Finish Modification */

        writer.println("\t\t\t\tf.write(\"(\" + v_str + \")\")");
        writer.println("\t\t\t\tif i != len(next_states_not_pred)-1:");
        writer.println("\t\t\t\t\tf.write(\" || \")");
        writer.println("\t\t\tf.write(\");\\n\")");
        writer.println("\telse:");
        writer.println("\t\tfor i in range(len(next_states_pred)):");
        writer.println("\t\t\tx = next_states_pred[i]");
        writer.println("\t\t\tpredicates[x] = predicates[x].replace(' and ',' && ')");
        writer.println("\t\t\tpredicates[x] = predicates[x].replace(' or ',' || ')");
        writer.println("\t\t\tpredicates[x] = predicates[x].replace('not ','!')");
        writer.println("\t\t\tpredicates[x] = predicates[x].replace('rt_input','input')");
        writer.println("\t\t\tpredicates[x] = predicates[x].replace('prev_state','state')");
        writer.println("\t\t\tpredicates[x] = predicates[x].replace('rt_state','state')\n");

        writer.println("\t\t\t## Array interest variable processing (Hyobin)");
        writer.println("\t\t\tpred_vars = list(np.unique(re.findall(r\"[a-zA-Z_][a-zA-Z0-9_]*\", predicates[x])))");
        writer.println("\t\t\tfor pred_var in pred_vars :");
        writer.println("\t\t\t\tif pred_var not in targetConvertedNames: continue");
        writer.println("\t\t\t\ti = targetConvertedNames.index(pred_var)");
        writer.println("\t\t\t\tpredicates[x] = predicates[x].replace(targetConvertedNames[i], targetNames[i])\n");

        writer.println("\t\t\tf.write(\"		if (\" + predicates[x] + \")\\n\")");

        // 2023-11-02(Thu) SoheeJung : struct key variable check
        if (getStructKeyVar() != null) {
            if (getStructVarScope().get(getStructKeyVar()).get(0) == 1) { // state
                writer.println("\t\t\tv_str = 'rt_state." + getStructKeyVar() + " == ' + x");
            } else if (getStructVarScope().get(getStructKeyVar()).get(0) == 2) { // output
                writer.println("\t\t\tv_str = 'rt_output." + getStructKeyVar() + " == ' + x");
            }
        }
        // 2023-05-25(Thu) SoheeJung
        // find key interest variable from state / output list, then make statement
        else if (getStateVarList().isKeyCheck())
            writer.println("\t\t\tv_str = 'rt_state." + getStateVarList().getNameList().get(0) + " == ' + x");
        else if (getOutputVarList().isKeyCheck())
            writer.println("\t\t\tv_str = 'rt_output." + getOutputVarList().getNameList().get(0) + " == ' + x");
        /* Finish Modification */

        writer.println("\t\t\tf.write(\"		{	assert(\" + v_str + \");\\n\")");
        writer.println("\t\t\tf.write(\"			found = true;}\\n\")\n");

        writer.println("\t\tf.write(\"		if (!found)\\n\")");
        writer.println("\t\tif not next_states_not_pred:");
        writer.println("\t\t\tf.write(\"			assert(false);\\n\")");
        writer.println("\t\telse:");
        writer.println("\t\t\tf.write(\"			assert(\")");
        writer.println("\t\t\tfor i in range(len(next_states_not_pred)):");
        writer.println("\t\t\t\ts = next_states_not_pred[i]");

        // 2023-11-02(Thu) SoheeJung : struct key variable check
        if (getStructKeyVar() != null) {
            if (getStructVarScope().get(getStructKeyVar()).get(0) == 1) { // state
                writer.println("\t\t\t\tv_str = 'rt_state." + getStructKeyVar() + " == ' + s");
            } else if (getStructVarScope().get(getStructKeyVar()).get(0) == 2) { // output
                writer.println("\t\t\t\tv_str = 'rt_output." + getStructKeyVar() + " == ' + s");
            }
        }
        // 2023-05-25(Thu) SoheeJung
        // find key interest variable from state / output list, then make statement
        else if (getStateVarList().isKeyCheck())
            writer.println("\t\t\t\tv_str = 'rt_state." + getStateVarList().getNameList().get(0) + " == ' + s");
        else if (getOutputVarList().isKeyCheck())
            writer.println("\t\t\t\tv_str = 'rt_output." + getOutputVarList().getNameList().get(0) + " == ' + s");
        /* Finish Modification */

        writer.println("\t\t\t\tf.write(\"(\" + v_str + \")\")");
        writer.println("\t\t\t\tif i != len(next_states_not_pred)-1:");
        writer.println("\t\t\t\t\tf.write(\" || \")");
        writer.println("\t\t\tf.write(\");\\n\")\n");
        writer.println("\tf.write(\"	}\\n\")");
        writer.println("\tf.write(\"}\\n\")");
        writer.println("\tf.close()\n");

        writer.println("def gen_k_ind_files():");
        writer.println(
                "\tp = subprocess.Popen('goto-cc code/code.c code/model.c -o code/code.goto', stdout=subprocess.PIPE, stderr=subprocess.PIPE, shell=True)");
        writer.println("\toutput,o_err = p.communicate()");
        writer.println("\tp.kill()");
        writer.println("\tif o_err and 'error' in str(o_err):");
        writer.println("\t\tprint(o_err)");
        writer.println("\t\texit(0)\n");

        // 2023-05-25(Thu) SoheeJung
        // make k-induction option using spec.txt information
        writer.println("\tp = subprocess.Popen('goto-instrument code/code.goto --k-induction " + getK_ind()
                + " --base-case code/code_base', stdout=subprocess.PIPE, stderr=subprocess.PIPE, shell=True)");

        writer.println("\toutput,o_err = p.communicate()");
        writer.println("\tp.kill()");
        writer.println("\tif o_err and 'error' in str(o_err):");
        writer.println("\t\tprint(o_err)");
        writer.println("\t\texit(0)\n");

        // 2023-05-25(Thu) SoheeJung
        // make k-induction option using spec.txt information
        writer.println("\tp = subprocess.Popen('goto-instrument code/code.goto --k-induction " + getK_ind()
                + " --step-case code/code_step', stdout=subprocess.PIPE, stderr=subprocess.PIPE, shell=True)");

        writer.println("\toutput,o_err = p.communicate()");
        writer.println("\tp.kill()");
        writer.println("\tif o_err and 'error' in str(o_err):");
        writer.println("\t\tprint(o_err)");
        writer.println("\t\texit(0)\n");

        writer.println("def run_k_ind_base():");

        // 2023-05-25(Thu) SoheeJung
        // make object bits option using spec.txt unwind information
        writer.println("\tp = subprocess.Popen('cbmc code/code_base --object-bits " + getObject_bits()
                + " > out_base.txt', stdout=subprocess.PIPE, stderr=subprocess.PIPE, shell=True)");

        writer.println("\ttry:");
        writer.println("\t\toutput,o_err = p.communicate()");
        writer.println("\t\tp.kill()");
        writer.println("\t\tif o_err and 'error' in str(o_err):");
        writer.println("\t\t\tprint(o_err)");
        writer.println("\t\t\texit(0)\n");
        writer.println("\texcept subprocess.TimeoutExpired:");
        writer.println("\t\tp.kill()");
        writer.println("\t\tprint(colored(\"[WARNING] TIMEOUT\",'magenta'))");
        writer.println("\texcept subprocess.CalledProcessError:");
        writer.println("\t\tp.kill()");
        writer.println("\t\tprint(colored(\"[ERROR] FAILED\",'magenta'))\n");

        writer.println("def run_k_ind_step():");

        // 2023-05-25(Thu) SoheeJung
        // make object bits option using spec.txt unwind information
        writer.println("\tp = subprocess.Popen('cbmc code/code_step --object-bits 16"
                + " > out_step.txt', stdout=subprocess.PIPE, stderr=subprocess.PIPE, shell=True)");

        writer.println("\ttry:");
        writer.println("\t\toutput,o_err = p.communicate()");
        writer.println("\t\tp.kill()");
        writer.println("\t\tif o_err and 'error' in str(o_err):");
        writer.println("\t\t\tprint(o_err)");
        writer.println("\t\t\texit(0)\n");
        writer.println("\texcept subprocess.TimeoutExpired:");
        writer.println("\t\tp.kill()");
        writer.println("\t\tprint(colored(\"[WARNING] TIMEOUT\",'magenta'))");
        writer.println("\texcept subprocess.CalledProcessError:");
        writer.println("\t\tp.kill()");
        writer.println("\t\tprint(colored(\"[ERROR] FAILED\",'magenta'))\n");

        writer.println("def check_validity_k_ind():");
        writer.println("\tgen_k_ind_files()");
        writer.println("\trun_k_ind_base()");
        writer.println("\tf1 = open(\"out_base.txt\",'r')");
        writer.println("\tbase_lines = f1.readlines()");
        writer.println("\tf1.close()\n");
        writer.println("\tif any([\"too many addressed objects\" in line for line in base_lines]):");
        writer.println("\t\texit(0)\n");
        writer.println("\tif any([\"PARSING ERROR\" in line for line in base_lines]):");
        writer.println("\t\texit(0)\n");
        writer.println("\tif any([\"VERIFICATION SUCCESSFUL\" in line for line in base_lines]):");
        writer.println("\t\tprint(colored(\"Base case SUCCESSFUL\", 'magenta'))");
        writer.println("\t\trun_k_ind_step()");
        writer.println("\t\tf2 = open(\"out_step.txt\",'r')");
        writer.println("\t\tstep_lines = f2.readlines()");
        writer.println("\t\tf2.close()\n");
        writer.println("\t\tif any([\"too many addressed objects\" in line for line in step_lines]):");
        writer.println("\t\t\texit(0)\n");
        writer.println("\t\tif any([\"PARSING ERROR\" in line for line in step_lines]):");
        writer.println("\t\t\texit(0)\n");
        writer.println("\t\tif any([\"VERIFICATION SUCCESSFUL\" in line for line in step_lines]):");
        writer.println("\t\t\tprint(colored(\"Step case SUCCESSFUL\", 'magenta'))");
        writer.println("\t\t\treturn True, True");
        writer.println("\t\treturn True, False\n");
        writer.println("\treturn False, False\n");

        writer.println("def dfa_traverse(model, trace_events, pred, s2):");
        writer.println("\ttrace_events_pref = []");
        writer.println("\tstart_state = [x[2] for x in model if x[1] == 'start' and x[0] == 1]");
        writer.println("\tassert(len(start_state) == 1)");
        writer.println("\tfor ind1 in range(len(trace_events)):");
        writer.println("\t\tstate = start_state[0]");
        writer.println("\t\tfor ind2 in range(len(trace_events[ind1])):");
        writer.println("\t\t\tevent = trace_events[ind1][ind2]");
        writer.println("\t\t\tnext_state = [x[2] for x in model if x[1] == event and x[0] == state]\n");
        writer.println("\t\t\tif not next_state:");
        writer.println("\t\t\t\tprint(\"no next state\")");
        writer.println("\t\t\t\texit(0)\n");
        writer.println("\t\t\tassert(len(next_state) == 1)");
        writer.println("\t\t\tif event == pred and next_state[0] == s2:");
        writer.println("\t\t\t\ttrace_events_pref = [ind1, ind2]");
        writer.println("\t\t\t\treturn trace_events_pref");
        writer.println("\t\t\tstate = next_state[0]");
        writer.println("\treturn trace_events_pref\n");

        writer.println("def get_trace_n_trace_events(e_file):");
        writer.println("\tf = open(e_file,'r')");
        writer.println("\tdata = [x.replace('\\n','') for x in f.readlines()]");
        writer.println("\tf.close()");
        writer.println("\ttrace_ind = [ind for ind in range(len(data)) if data[ind] == 'start']\n");
        writer.println("\ttrace_events = []");
        writer.println("\ttrace = []");
        writer.println("\tfor i in range(len(trace_ind)-1):");
        writer.println("\t\tt_list = data[trace_ind[i]+1:trace_ind[i+1]]");
        writer.println("\t\ttrace_events.append(t_list)");
        writer.println("\t\t## initial state definition");

        // 2023-11-02(Thu) SoheeJung : struct key variable check
        if (getStructKeyVar() != null) {
            if (getEnumMap().containsKey(getStructKeyVar()))
                writer.println("\t\ttemp = ['" + getEnumMap().get(getStructKeyVar())[0] + "']");
            else
                writer.println("\t\ttemp = ['" + getStructVarScope().get(getStructKeyVar()).get(1) + "']");
        }
        // 2023-10-24(Tue) SoheeJung
        // key interest variable initialize
        else if (getStateVarList().isKeyCheck()) {
            if (getEnumMap().containsKey(getStateVarList().getNameList().get(0)))
                writer.println("\t\ttemp = ['" + getEnumMap().get(getStateVarList().getNameList().get(0))[0] + "']");
            else {
                for (Variable v : variables) {
                    if (getStateVarList().getNameList().get(0).equals(v.getName())) {
                        if (!v.getFileName().equals("NULL") && v.getScope().equals("Global"))
                            writer.println("\t\ttemp = ['" + v.getVarScope().get(0) + "']");
                    }
                }
            }
        }

        else if (getOutputVarList().isKeyCheck()) {
            if (getEnumMap().containsKey(getOutputVarList().getNameList().get(0)))
                writer.println("\t\ttemp = ['" + getEnumMap().get(getOutputVarList().getNameList().get(0))[0] + "']");
            else {
                for (Variable v : variables) {
                    if (getOutputVarList().getNameList().get(0).equals(v.getName())) {
                        if (!v.getFileName().equals("NULL") && v.getScope().equals("Global"))
                            writer.println("\t\ttemp = ['" + v.getVarScope().get(0) + "']");
                    }
                }
            }
        }
        /* key interest variable initialize finish */

        // writer.println("\t\ttemp = ['?']");
        writer.println(
                "\t\t[temp.append(x.split(': ')[1]) if len(x.split(': '))>1 else temp.append(x.split(': ')[0]) for x in t_list]");
        writer.println("\t\ttrace.append(temp)");
        writer.println("\treturn trace, trace_events\n");

        writer.println("## main");
        writer.println("model_file = sys.argv[1]");
        writer.println("all_valid_traces_file = sys.argv[2]");
        writer.println("assumptions_file = all_valid_traces_file.replace('valid','ass')\n");

        writer.println("## assume value assign part ([original] types.txt [modification] spec.txt)");
        // [Original version of targetLen computation]
        // Integer targetLen = getInputVarList().getNameList().size() +
        // getStateVarList().getNameList().size() +
        // getOutputVarList().getNameList().size() - 1 +
        // getStructVarScope().keySet().size();
        // for(String key : getArrMap().keySet()) {
        // targetLen += getArrMap().get(key);
        // }
        // targetLen -= getArrMap().size();
        // writer.println("targetLen = " + String.valueOf(targetLen));

        // 2023-05-16(Tue) SoheeJung
        // make assume print using spec.txt's information
        /* Start interest variable print */
        String varNameString = "";
        String varTypeString = "";
        String varExprString = "";
        int count = 0;
        int targetLen = 0; // [New version of targetLen computation]

        for (String s : getStateVarList().getNameList()) {
            // 2023-11-02(Thu) SoheeJung : if interest variable is structure type, then
            // don't make element string
            boolean structCheck = false;
            for (String structName : getStructVarScope().keySet()) {
                if (structName.contains(s))
                    structCheck = true;
            }
            if (structCheck)
                continue;
            /* struct type variable checking finish */

            if ((getInputVarList().getNameList().size() == 0) && (getOutputVarList().getNameList().size() == 0)) {
                if (getStateVarList().isKeyCheck() && count == 0) {
                    // do nothing -> first element is key interest value
                }
                // else if(count < (getStateVarList().getNameList().size() - 1)) {
                // if(getArrMap().containsKey(s)) {
                // for(int i=0; i<getArrMap().get(s); i++) {
                // varNameString += "\"" + s + "["+ String.valueOf(i) + "]\", ";
                // }
                // }
                // else varNameString += "\"" + s + "\", ";
                // varTypeString += "\"state\", ";
                // varExprString += "\"%d\", ";
                // } else {
                // if(getArrMap().containsKey(s)) {
                // for(int i=0; i<getArrMap().get(s); i++) {
                // varNameString += "\"" + s + "["+ String.valueOf(i) + "]\"";
                // }
                // }
                // else varNameString += "\"" + s + "\"";
                // varTypeString += "\"state\"";
                // varExprString += "\"%d\"";
                // }
                if (getArrMap().containsKey(s)) {
                    for (int i = 0; i < getArrMap().get(s); i++) {
                        varNameString += "\"" + s + "[" + String.valueOf(i) + "]\", ";
                        varTypeString += "\"state\", ";
                        varExprString += "\"%d\", ";
                        targetLen += 1;
                    }
                } else {
                    varNameString += "\"" + s + "\", ";
                    varTypeString += "\"state\", ";
                    varExprString += "\"%d\", ";
                    targetLen += 1;
                }

                count++;
            } else {
                if (getStateVarList().isKeyCheck() && count == 0) {
                    // do nothing -> first element is key interest value
                } else {
                    if (getArrMap().containsKey(s)) {
                        for (int i = 0; i < getArrMap().get(s); i++) {
                            varNameString += "\"" + s + "[" + String.valueOf(i) + "]\", ";
                            varTypeString += "\"state\", ";
                            varExprString += "\"%d\", ";
                            targetLen += 1;
                        }
                    } else {
                        varNameString += "\"" + s + "\", ";
                        varTypeString += "\"state\", ";
                        varExprString += "\"%d\", ";
                        targetLen += 1;
                    }

                }
                count++;
            }
        }

        count = 0;
        for (String s : getInputVarList().getNameList()) {
            // 2023-11-02(Thu) SoheeJung : if interest variable is structure type, then
            // don't make element string
            boolean structCheck = false;
            for (String structName : getStructVarScope().keySet()) {
                if (structName.contains(s))
                    structCheck = true;
            }
            if (structCheck)
                continue;
            /* struct type variable checking finish */

            if ((getOutputVarList().getNameList().size() == 0)
                    || (getOutputVarList().isKeyCheck() && getOutputVarList().getNameList().size() == 1)) {
                // if(count < (getInputVarList().getNameList().size() - 1)) {
                // if(getArrMap().containsKey(s)) {
                // for(int i=0; i<getArrMap().get(s); i++) {
                // varNameString += "\"" + s + "["+ String.valueOf(i) + "]\", ";
                // }
                // }
                // else varNameString += "\"" + s + "\", ";
                // varTypeString += "\"input\", ";
                // varExprString += "\"%d\", ";
                // } else {
                // if(getArrMap().containsKey(s)) {
                // for(int i=0; i<getArrMap().get(s); i++) {
                // varNameString += "\"" + s + "["+ String.valueOf(i) + "]\"";
                // }
                // }
                // else varNameString += "\"" + s + "\"";
                // varTypeString += "\"input\"";
                // varExprString += "\"%d\"";
                // }
                if (getArrMap().containsKey(s)) {
                    for (int i = 0; i < getArrMap().get(s); i++) {
                        varNameString += "\"" + s + "[" + String.valueOf(i) + "]\", ";
                        varTypeString += "\"input\", ";
                        varExprString += "\"%d\", ";
                        targetLen += 1;
                    }
                } else {
                    varNameString += "\"" + s + "\", ";
                    varTypeString += "\"input\", ";
                    varExprString += "\"%d\", ";
                    targetLen += 1;
                }
                count++;
            } else {
                if (getArrMap().containsKey(s)) {
                    for (int i = 0; i < getArrMap().get(s); i++) {
                        varNameString += "\"" + s + "[" + String.valueOf(i) + "]\", ";
                        varTypeString += "\"input\", ";
                        varExprString += "\"%d\", ";
                        targetLen += 1;
                    }
                } else {
                    varNameString += "\"" + s + "\", ";
                    varTypeString += "\"input\", ";
                    varExprString += "\"%d\", ";
                    targetLen += 1;
                }
            }
        }

        count = 0;
        for (String s : getOutputVarList().getNameList()) {
            // 2023-11-02(Thu) SoheeJung : if interest variable is structure type, then
            // don't make element string
            boolean structCheck = false;
            for (String structName : getStructVarScope().keySet()) {
                if (structName.contains(s))
                    structCheck = true;
            }
            if (structCheck)
                continue;
            /* struct type variable checking finish */

            if (getOutputVarList().isKeyCheck() && count == 0) {
                // do nothing -> first element is key interest value
            }
            // else if(count < (getOutputVarList().getNameList().size() - 1)) {
            // if(getArrMap().containsKey(s)) {
            // for(int i=0; i<getArrMap().get(s); i++) {
            // varNameString += "\"" + s + "["+ String.valueOf(i) + "]\", ";
            // }
            // }
            // else varNameString += "\"" + s + "\", ";
            // varTypeString += "\"output\", ";
            // varExprString += "\"%d\", ";
            // } else {
            // if(getArrMap().containsKey(s)) {
            // for(int i=0; i<getArrMap().get(s); i++) {
            // varNameString += "\"" + s + "["+ String.valueOf(i) + "]\"";
            // }
            // }
            // else varNameString += "\"" + s + "\"";
            // varTypeString += "\"output\"";
            // varExprString += "\"%d\"";
            // }
            if (getArrMap().containsKey(s)) {
                for (int i = 0; i < getArrMap().get(s); i++) {
                    varNameString += "\"" + s + "[" + String.valueOf(i) + "]\", ";
                    varTypeString += "\"output\", ";
                    varExprString += "\"%d\", ";
                    targetLen += 1;
                }
            } else {
                varNameString += "\"" + s + "\", ";
                varTypeString += "\"output\", ";
                varExprString += "\"%d\", ";
                targetLen += 1;
            }
            count++;
        }

        // 2023-11-02(Thu) SoheeJung : struct variable
        for (String structName : getStructVarScope().keySet()) {
            if (structName.equals(getStructKeyVar()))
                continue;

            if (getStructVarScope().get(structName).get(0) == 0) { // INPUT
                varNameString += "\"" + structName + "\", ";
                varTypeString += "\"input\", ";
                varExprString += "\"%d\", ";
                targetLen += 1;
            } else if (getStructVarScope().get(structName).get(0) == 0) { // STATE
                varNameString += "\"" + structName + "\", ";
                varTypeString += "\"state\", ";
                varExprString += "\"%d\", ";
                targetLen += 1;
            } else { // OUTPUT
                varNameString += "\"" + structName + "\", ";
                varTypeString += "\"output\", ";
                varExprString += "\"%d\", ";
                targetLen += 1;
            }
        }
        /* struct variable finish */

        // 2023-11-03(Fri) SoheeJung : constant variable
        for (String constStr : getConstList()) {
            varNameString += "\"" + constStr + "\", ";
            varTypeString += "\"const\", ";
            varExprString += "\"%d\", ";
            targetLen += 1;
        }
        /* constant variable finish */

        varNameString = varNameString.substring(0, varNameString.length() - 2);
        varTypeString = varTypeString.substring(0, varTypeString.length() - 2);
        varExprString = varExprString.substring(0, varExprString.length() - 2);

        writer.println("targetLen = " + String.valueOf(targetLen));
        writer.println("targetNames = [" + varNameString + "]");
        writer.println("targetTypes = [" + varTypeString + "]");
        writer.println("targetExprs = [" + varExprString + "]");
        /* Finish interest variable print */

        // 2023-11-02(Thu) SoheeJung : top element print & bottom element print
        // modification
        writer.println("topElement = [" + varNameString + "]");
        writer.println("bottomElement = []");
        /* new version of element string finish */

        // // 2023-05-30(Tue) SoheeJung
        // // print model_step's top printf elements & bottom printf elements
        //// ArrayList<String> newTop = new ArrayList<>();
        //// for(String s : getTopElement()) newTop.add("\"" + s + "\"");
        //// String topelem = String.join(", ", newTop);
        //// writer.println("topElement = [" + topelem + "]");
        ////
        //// ArrayList<String> newBottom = new ArrayList<>();
        //// for(String s : getBottomElement()) newBottom.add("\"" + s + "\"");
        //// String bottomelem = String.join(", ", newBottom);
        //// writer.println("bottomElement = [" + bottomelem + "]");
        ////
        //
        // String topElementString = "";
        // writer.print("topElement = [");
        // boolean keyVarcheck = false;
        //
        // for(String s : getStateVarList().getNameList()) {
        // // 2023-05-25(Thu) SoheeJung
        // // key variable checking -> if there is key variable, then make printf start
        // at second element (not first element)
        // if(getStateVarList().isKeyCheck() && !keyVarcheck) {
        // keyVarcheck = true;
        // continue;
        // }
        // for(Variable v : variables) {
        // if(s.equals(v.getName()) && !v.getFileName().equals("NULL") &&
        // v.getScope().equals("Global")) {
        // if(getArrMap().containsKey(v.getName())) {
        // for(int i=0; i<getArrMap().get(v.getName()); i++) {
        // topElementString += "\"" + s + "[" + String.valueOf(i) + "]\", ";
        // }
        // }
        // else {
        // topElementString += "\"" + s + "\", ";
        // }
        // }
        // }
        // }
        //
        // for(String s : getInputVarList().getNameList()) {
        // for(Variable v : variables) {
        // if(s.equals(v.getName()) && !v.getFileName().equals("NULL") &&
        // v.getScope().equals("Global")) {
        // if(getArrMap().containsKey(v.getName())) {
        // for(int i=0; i<getArrMap().get(v.getName()); i++) {
        // topElementString += "\"" + s + "[" + String.valueOf(i) + "]\", ";
        // }
        // }
        // else {
        // topElementString += "\"" + s + "\", ";
        // }
        // }
        // }
        // }
        //
        // for(String s : getOutputVarList().getNameList()) {
        // // 2023-05-25(Thu) SoheeJung
        // // key variable checking -> if there is key variable, then make printf start
        // at second element (not first element)
        // if(getOutputVarList().isKeyCheck() && !keyVarcheck) {
        // keyVarcheck = true;
        // continue;
        // }
        // for(Variable v : variables) {
        // if(s.equals(v.getName()) && !v.getFileName().equals("NULL") &&
        // v.getScope().equals("Global")) {
        // if(getArrMap().containsKey(v.getName())) {
        // for(int i=0; i<getArrMap().get(v.getName()); i++) {
        // topElementString += "\"" + s + "[" + String.valueOf(i) + "]\", ";
        // }
        // }
        // else {
        // topElementString += "\"" + s + "\", ";
        // }
        // }
        // }
        // }
        // topElementString = topElementString.substring(0, topElementString.length() -
        // 2);
        // writer.print(topElementString);
        // writer.println("]");

        writer.println(
                "targetConvertedNames = [name.replace(\"[\", \"_\").replace(\"]\",\"\") for name in targetNames]");
        writer.println("## targetNamesStructed = [targetTypes[i] + \".\" + targetNames[i] for i in range(targetLen)]");
        // [Original Version] : No constant processing
        // writer.println("targetNamesOutput = [(\"\" if targetNames[i] in topElement
        // else \"rt_\") + targetTypes[i] + \".\" + targetNames[i] for i in
        // range(targetLen)]");
        // writer.println("targetNamesRTStructed = [(\"new_\" if targetTypes[i] ==
        // \"input\" else \"rt_\") + targetTypes[i] + \".\" + targetNames[i] for i in
        // range(targetLen)]");
        // writer.println("targetNamesStructedforProp = [(\"\" if targetNames[i] in
        // topElement else \"rt_\") + targetTypes[i] + \".\" + targetNames[i] for i in
        // range(targetLen)]");
        // writer.println("targetNamesPrevStructed = [(\"prev_\" if targetNames[i] in
        // topElement else \"\") + targetTypes[i] + \".\" + targetNames[i] for i in
        // range(targetLen)]");
        // [Modify Version] : contain constant processing
        writer.println(
                "targetNamesOutput = [targetNames[i] if targetTypes[i] == \"const\" else (\"\" if targetNames[i] in topElement else \"rt_\") + targetTypes[i] + \".\" + targetNames[i] for i in range(targetLen)]");
        writer.println(
                "targetNamesRTStructed = [targetNames[i] if targetTypes[i] == \"const\" else (\"new_\" if targetTypes[i] == \"input\" else \"rt_\") + targetTypes[i] + \".\" + targetNames[i] for i in range(targetLen)]");
        writer.println(
                "targetNamesStructedforProp = [targetNames[i] if targetTypes[i] == \"const\" else (\"\" if targetNames[i] in topElement else \"rt_\") + targetTypes[i] + \".\" + targetNames[i] for i in range(targetLen)]");
        writer.println(
                "targetNamesPrevStructed = [targetNames[i] if targetTypes[i] == \"const\" else (\"prev_\" if targetNames[i] in topElement else \"\") + targetTypes[i] + \".\" + targetNames[i] for i in range(targetLen)]");

        writer.println(
                "targetProp = [targetNamesStructedforProp[i] + \"==\" + targetExprs[i] for i in range(targetLen)]");
        writer.println(
                "targetAssume = [targetNamesPrevStructed[i] + \"==\" + targetExprs[i] for i in range(targetLen)]");
        writer.println("## assume value assign part Finish\n");

        writer.println("if all_valid_traces_file != 'nil':");
        writer.println("\tinput_file = all_valid_traces_file");
        writer.println("\twith open(input_file, 'rb') as f:");
        writer.println("\t\tall_valid_traces = pickle.load(f)");
        writer.println("else:");
        writer.println("\tall_valid_traces = []");
        writer.println("\tf = open('traces/trace1.txt','r')");
        writer.println("\tlines = [x.replace('\\n','') for x in f.readlines()]");
        writer.println("\ttrace_ind = [i for i in range(len(lines)) if lines[i] == 'trace']");
        writer.println("\tfor i in range(len(trace_ind)-1):");
        writer.println("\t\tfor j in range(trace_ind[i]+1, trace_ind[i+1]):");
        writer.println("\t\t\tif lines[j] == 'trace' or lines[j+1] == 'trace':");
        writer.println("\t\t\t\tcontinue");
        writer.println("\t\t\tt1 = lines[j].split()");
        writer.println("\t\t\tt2 = lines[j+1].split()");
        writer.println("\t\t\ttemp_dict = {}");
        writer.println("\t\t\ttemp_dict['trace'] = [t1,t2]");
        writer.println("\t\t\ttemp_dict['maybe_valid'] = False");
        writer.println(
                "\t\t\ttemp_dict['trace_events'] = [x.split()[0] for x in lines[trace_ind[i]+1:trace_ind[i+1]]]");
        writer.println("\t\t\ttemp_dict['ass'] = []");
        writer.println("\t\t\tall_valid_traces.append(temp_dict)\n");
        writer.println("if assumptions_file != 'nil':");
        writer.println("\tinput_file = assumptions_file");
        writer.println("\twith open(input_file, 'rb') as f:");
        writer.println("\t\tnew_assumptions = pickle.load(f)");
        writer.println("else:");
        writer.println("\tnew_assumptions = []\n");
        writer.println(
                "trace_events_file = 'traces/' + model_file.replace('model','trace').replace('.txt','_events.txt')");
        writer.println("trace_, trace_events_ = get_trace_n_trace_events(trace_events_file)\n");
        writer.println("model = get_model(model_file)");
        writer.println("states = [x[0] for x in model]");
        writer.println("[states.append(x[2]) for x in model]\n");
        writer.println("states = list(np.unique(states))");
        writer.println("in_pred = dict.fromkeys([str(x) for x in states],0)");
        writer.println("out_pred = dict.fromkeys([str(x) for x in states],0)\n");
        writer.println("for state in states:");
        writer.println("\tin_pred[str(state)] = []");
        writer.println("\tout_pred[str(state)] = []");
        writer.println("\t[in_pred[str(state)].append(x[1]) for x in model if x[2] == state and x[1]!='start']");
        writer.println("\tin_pred[str(state)] = list(np.unique(in_pred[str(state)], axis=0))\n");
        writer.println("\t[out_pred[str(state)].append(x[1]) for x in model if x[0] == state and x[1]!='start']");
        writer.println("\tout_pred[str(state)] = list(np.unique(out_pred[str(state)], axis=0))\n");
        writer.println("start_state = [x[2] for x in model if x[1] == 'start' and x[0] == 1]");
        writer.println("assert(len(start_state) == 1)");
        writer.println("start_state = start_state[0]\n");
        writer.println("invalid_trace = []");
        writer.println("for state in states:");
        writer.println("\tprint(state)\n");
        writer.println("\t# for trans s1 p s2 where s2 == state");
        writer.println("\ts2 = state");
        writer.println("\ts2_in_p = [x[1] for x in model if x[2] == s2 and x[1] != 'start']");
        writer.println("\ts2_in_p = list(np.unique(s2_in_p))");
        writer.println("\ts2_out_p = [x[1] for x in model if x[0] == s2 and x[1] != 'start']\n");
        writer.println("\tif state == start_state:");
        writer.println("\t\tprint(\"Outgoing: \", end=\"\")");
        writer.println("\t\tprint(s2_out_p)");
        writer.println("\t\tgen_check_init_proof(s2_out_p)");
        writer.println("\t\trun_code_proof()");
        writer.println("\t\tnew_traces, outcome = get_trace()");
        writer.println("\t\tif outcome:");
        writer.println("\t\t\tprint(colored(\"SUCCESSFUL: Init\", 'green'))");
        writer.println("\t\t\tprint('\\n')");
        writer.println("\t\telse:");
        writer.println("\t\t\tprint(colored(\"Failed: Init\", 'red'))");
        writer.println("\t\t\tprint(colored(\"New Trace\",'blue'))");
        writer.println("\t\t\tprint(colored(new_traces,'blue'))");
        writer.println("\t\t\tprint('\\n')");
        writer.println("\t\t\tif not new_traces:");
        writer.println("\t\t\t\texit(0)\n");
        writer.println("\t\t\tfor j in range(len(new_traces)):");
        writer.println("\t\t\t\ttemp_dict = {}");
        writer.println("\t\t\t\ttemp_dict['trace'] = new_traces[j]");
        writer.println("\t\t\t\ttemp_dict['maybe_valid'] = False");
        writer.println("\t\t\t\ttemp_dict['trace_events'] = [x[0] for x in new_traces[j]]");
        writer.println("\t\t\t\ttemp_dict['ass'] = []");
        writer.println("\t\t\t\tall_valid_traces.append(temp_dict)\n");
        writer.println("\ti = 0");
        writer.println("\trepeat = False");
        writer.println("\twhile i < len(s2_in_p):");
        writer.println("\t\tpred = s2_in_p[i]");
        writer.println("\t\ts1 = [x[0] for x in model if x[1] == pred and x[2] == s2]");
        writer.println("\t\ts1_in_p = [x[1] for x in model if x[2] in s1 and x[1] != 'start']\n");
        writer.println("\t\tprint(\"Predicate: \" + pred)");
        writer.println("\t\tprint(\"Outgoing: \", end=\"\")");
        writer.println("\t\tprint(s2_out_p)\n");
        writer.println("\t\ttrace_events_pref = dfa_traverse(model, trace_events_, pred, s2)");
        writer.println("\t\ttrace_prefix = trace_[trace_events_pref[0]][:trace_events_pref[1]+2]\n");
        writer.println("\t\tif not repeat:");
        writer.println("\t\t\ttemp_dict = {}");
        writer.println("\t\t\tvalid_trace = []\n");
        writer.println("\t\tnew_assumptions = list(np.unique(new_assumptions))");
        writer.println("\t\tgenerate_assume_assert([], pred, s2_out_p, new_assumptions)\n");
        writer.println("\t\trun_code_proof()");
        writer.println("\t\tnew_traces, outcome = get_trace()");
        writer.println("\t\tif outcome:");
        writer.println("\t\t\tprint(colored(\"SUCCESSFUL\", 'green'))");
        writer.println("\t\t\tprint('\\n')");
        writer.println("\t\t\ti = i + 1");
        writer.println("\t\t\tcontinue\n");
        writer.println("\t\tprint(colored(\"FAILED\", 'red'))");
        writer.println("\t\tprint(colored(\"New Trace\",'blue'))");
        writer.println("\t\tprint(colored(new_traces,'blue'))");
        writer.println("\t\tif not new_traces:");
        writer.println("\t\t\texit(0)");
        writer.println("\t\tprint('\\n')\n");
        writer.println("\t\trepeat = False");
        writer.println("\t\tprop, ass = get_prop_assumption()");
        writer.println("\t\tfor j in range(len(prop)):");
        writer.println("\t\t\tif new_traces[j] in valid_trace:");
        writer.println("\t\t\t\tprint(\"Already present\\n\")");
        writer.println("\t\t\t\tprint(new_traces[j])");
        writer.println("\t\t\t\tcontinue");
        writer.println("\t\t\tp = get_prop_assumption_str(prop[j])");
        writer.println("\t\t\ta = get_prop_assumption_str(ass[j])");
        writer.println("\t\t\tcheck_validity_code(p)");
        writer.println("\t\t\tinvalid_base, invalid_step = check_validity_k_ind()\n");
        writer.println("\t\t\tif invalid_base:");
        writer.println("\t\t\t\tif invalid_step:");
        writer.println("\t\t\t\t\tprint(colored(\"INVALID CE\",'red'))");
        writer.println("\t\t\t\t\tprint(new_traces[j])");
        writer.println("\t\t\t\t\tinvalid_trace.append(new_traces[j])");
        writer.println("\t\t\t\t\trepeat = True");
        writer.println("\t\t\t\t\tnew_assumptions.append(a)");
        writer.println("\t\t\t\t\tcontinue");
        writer.println("\t\t\t\telse:");
        writer.println("\t\t\t\t\tt1 = new_traces[j][0]");
        writer.println("\t\t\t\t\tt2 = new_traces[j][1][0]\n");
        writer.println("\t\t\t\t\t# conflict with 'valid CE'");
        writer.println(
                "\t\t\t\t\tif any([(x['trace'][0] == t1 and x['trace'][1][0] != t2 and not x['maybe_valid']) for x in all_valid_traces]):");
        writer.println("\t\t\t\t\t\tprint(colored(\"INVALID CE\",'red'))");
        writer.println("\t\t\t\t\t\tprint(new_traces[j])");
        writer.println("\t\t\t\t\t\tinvalid_trace.append(new_traces[j])");
        writer.println("\t\t\t\t\t\trepeat = True");
        writer.println("\t\t\t\t\t\tnew_assumptions.append(a)");
        writer.println("\t\t\t\t\t\tvalid_trace = [x for x in valid_trace if x != new_traces[j]]");
        writer.println("\t\t\t\t\t\tcontinue\n");
        writer.println("\t\t\t\t\t# conflict with other 'maybe valid CE'");
        writer.println(
                "\t\t\t\t\tif any([(x['trace'][0] == t1 and x['trace'][1][0] != t2 and x['maybe_valid']) for x in all_valid_traces]):");
        writer.println("\t\t\t\t\t\trepeat = True");
        writer.println(
                "\t\t\t\t\t\tconflicts = [x for x in all_valid_traces if (x['trace'][0] == t1 and x['trace'][1][0] != t2 and x['maybe_valid'])]");
        writer.println("\t\t\t\t\t\tif not conflicts:");
        writer.println("\t\t\t\t\t\t\texit(0)");
        writer.println("\t\t\t\t\t\tprint(colored(\"Conflicting...\",'blue'))");
        writer.println("\t\t\t\t\t\tprint(conflicts)\n");
        writer.println("\t\t\t\t\t\tnew_assumptions.append(a)");
        writer.println("\t\t\t\t\t\tfor c in conflicts:");
        writer.println("\t\t\t\t\t\t\tif not c['ass']:");
        writer.println("\t\t\t\t\t\t\t\tprint(\"Assumption missing...\")");
        writer.println("\t\t\t\t\t\t\t\texit(0)");
        writer.println("\t\t\t\t\t\t\tnew_assumptions.append(c['ass'])");
        writer.println("\t\t\t\t\t\t\tvalid_trace = [x for x in valid_trace if x != c['trace']]");
        writer.println("\t\t\t\t\t\tall_valid_traces = [x for x in all_valid_traces if x not in conflicts]");
        writer.println("\t\t\t\t\t\tcontinue\n");
        writer.println("\t\t\t\t\tprint(colored(\"[Maybe] Valid CE\",'green'))");
        writer.println("\t\t\t\t\tprint(new_traces[j])\n");
        writer.println("\t\t\t\t\ttemp_dict = {}");
        writer.println("\t\t\t\t\ttemp_dict['trace'] = new_traces[j]");
        writer.println("\t\t\t\t\ttemp_dict['maybe_valid'] = True");
        writer.println("\t\t\t\t\ttemp_dict['trace_events'] = trace_prefix.copy()");
        writer.println("\t\t\t\t\ttemp_dict['trace_events'].append(new_traces[j][1][0])");
        writer.println("\t\t\t\t\ttemp_dict['ass'] = a\n");
        writer.println("\t\t\t\t\tif temp_dict not in all_valid_traces:");
        writer.println("\t\t\t\t\t\tall_valid_traces.append(temp_dict)");
        writer.println("\t\t\t\t\telse:");
        writer.println("\t\t\t\t\t\tprint(\"Already present\\n\")\n");
        writer.println("\t\t\t\t\tif new_traces[j] not in valid_trace:");
        writer.println("\t\t\t\t\t\tvalid_trace.append(new_traces[j])");
        writer.println("\t\t\t\t\telse:");
        writer.println("\t\t\t\t\t\tprint(\"Already present\\n\")");
        writer.println("\t\t\telse:");
        writer.println("\t\t\t\tprint(colored(\"Valid CE\",'green'))");
        writer.println("\t\t\t\tprint(new_traces[j])");
        writer.println("\t\t\t\tt1 = new_traces[j][0]");
        writer.println("\t\t\t\tt2 = new_traces[j][1][0]\n");
        writer.println("\t\t\t\t# conflict with 'maybe valid CE'");
        writer.println(
                "\t\t\t\tif any([(x['trace'][0] == t1 and x['trace'][1][0] != t2 and x['maybe_valid']) for x in all_valid_traces]):");
        writer.println(
                "\t\t\t\t\tconflicts = [x for x in all_valid_traces if (x['trace'][0] == t1 and x['trace'][1][0] != t2 and x['maybe_valid'])]");
        writer.println("\t\t\t\t\tif not conflicts:");
        writer.println("\t\t\t\t\t\texit(0)");
        writer.println("\t\t\t\t\tprint(colored(\"Conflicting...\",'blue'))");
        writer.println("\t\t\t\t\tprint(conflicts)\n");
        writer.println("\t\t\t\t\tfor c in conflicts:");
        writer.println("\t\t\t\t\t\tif not c['ass']:");
        writer.println("\t\t\t\t\t\t\tprint(\"Assumption missing...\")");
        writer.println("\t\t\t\t\t\t\texit(0)");
        writer.println("\t\t\t\t\t\tnew_assumptions.append(c['ass'])");
        writer.println("\t\t\t\t\t\tvalid_trace = [x for x in valid_trace if x != c['trace']]");
        writer.println("\t\t\t\t\tall_valid_traces = [x for x in all_valid_traces if x not in conflicts]\n");
        writer.println("\t\t\t\ttemp_dict = {}");
        writer.println("\t\t\t\ttemp_dict['trace'] = new_traces[j]");
        writer.println("\t\t\t\ttemp_dict['maybe_valid'] = False");
        writer.println("\t\t\t\ttemp_dict['trace_events'] = trace_prefix.copy()");
        writer.println("\t\t\t\ttemp_dict['trace_events'].append(new_traces[j][1][0])");
        writer.println("\t\t\t\ttemp_dict['ass'] = []");
        writer.println("\t\t\t\tif temp_dict not in all_valid_traces:");
        writer.println("\t\t\t\t\tall_valid_traces.append(temp_dict)");
        writer.println("\t\t\t\telse:");
        writer.println("\t\t\t\t\tprint(\"Already present\\n\")\n");
        writer.println("\t\t\t\tif new_traces[j] not in valid_trace:");
        writer.println("\t\t\t\t\tvalid_trace.append(new_traces[j])");
        writer.println("\t\t\t\telse:");
        writer.println("\t\t\t\t\tprint(\"Already present\\n\")\n");
        writer.println("\t\t\tif len(valid_trace) == len(prop):");
        writer.println("\t\t\t\trepeat = False");
        writer.println("\t\t\t\tbreak\n");
        writer.println("\t\tif repeat:");
        writer.println("\t\t\tprint(\"Repeating\\n\")");
        writer.println("\t\telse:");
        writer.println("\t\t\ti = i + 1");
        writer.println("\t\t\tprint(\"\\n\")\n");
        writer.println("pickle_f = open(sys.argv[1].replace('.txt','') + '_valid.pkl','wb')");
        writer.println("pickle.dump(all_valid_traces,pickle_f)");
        writer.println("pickle_f.close()\n");
        writer.println("pickle_f = open(sys.argv[1].replace('.txt','') + '_ass.pkl','wb')");
        writer.println("pickle.dump(new_assumptions,pickle_f)");
        writer.println("pickle_f.close()");

        writer.close();
    }

    // execute ModelConstructor (main function)
    public static void main(String[] args) throws IOException {
        ModelConstructor modelConstructor = new ModelConstructor();
        UdbInfoRetriever udbInfoRetriever = new UdbInfoRetriever();

        // 2023-10-24(Tue) SoheeJung
        // external parameter definition part
        /**
         * 1. trace_m
         * 2. trace_n
         * 3. unwind
         * 4. k-induction
         * 5. object-bits
         * 6. filename
         */
        // default value assign part
        modelConstructor.setTrace_m(50);
        modelConstructor.setTrace_n(50);
        modelConstructor.setUnwind(10);
        modelConstructor.setK_ind(10);
        modelConstructor.setObject_bits(16);
        String filename = "";

        // external argument assign part
        if (args != null) {
            for (int i = 0; i < args.length; i += 2) {
                if (args.length % 2 == 1) {
                    System.err.println("External Arguments are not enough!!");
                    System.exit(2);
                } else {
                    if (args[i].equals("--trace_m")) {
                        modelConstructor.setTrace_m(Integer.valueOf(args[i + 1]));
                    } else if (args[i].equals("--trace_n")) {
                        modelConstructor.setTrace_n(Integer.valueOf(args[i + 1]));
                    } else if (args[i].equals("--unwind")) {
                        modelConstructor.setUnwind(Integer.valueOf(args[i + 1]));
                    } else if (args[i].equals("--k-induction")) {
                        modelConstructor.setK_ind(Integer.valueOf(args[i + 1]));
                    } else if (args[i].equals("--object-bits")) {
                        modelConstructor.setObject_bits(Integer.valueOf(args[i + 1]));
                    } else if (args[i].equals("--filename")) {
                        filename = args[i + 1];
                    }
                }
            }
        }
        /* external parameter definition part finish */

        String srcPath = "./src/ALautomation/code/" + filename + "/" + filename + ".c";

        // 2023-10-27(Fri) SoheeJung
        // final result save path modification
        String[] originPath = srcPath.split("/");
        String resultDirPath = "";
        for (int i = 0; i < originPath.length - 1; i++)
            resultDirPath += originPath[i] + "/";
        resultDirPath += "generate";
        Path directoryPath = Paths.get(resultDirPath);
        try {
            // generate directory
            Files.createDirectories(directoryPath);
        } catch (IOException e) {
            e.printStackTrace();
        }

        // 1. get all CFG information from UDB data and make CFG
        udbInfoRetriever.makeFunctionsFromUDB(srcPath); // Perl -> Lexeme & Variable

        // 2. expression make
        HashMap<Function, Integer> numberOfParam = new HashMap<Function, Integer>();
        HashMap<Function, Boolean> typeOfFunc = new HashMap<Function, Boolean>(); // check for function type
        for (Function f : udbInfoRetriever.getFunctions()) {
            if (f.getParameters() == null)
                numberOfParam.put(f, 0);
            else
                numberOfParam.put(f, f.getParameters().size());
            typeOfFunc.put(f, f.returnLibraryType());
        }

        ArrayList<String> defines = new ArrayList<String>();
        // define list make
        for (Define d : udbInfoRetriever.getDefines()) {
            defines.add(d.getName());
        }

        // 2023-10-30(Mon) SoheeJung
        // include part addition
        File include_file = new File(srcPath);
        FileInputStream fileInput = null;
        ArrayList<String> includeHeader = new ArrayList<String>();
        try {
            fileInput = new FileInputStream(include_file);
            BufferedReader br = new BufferedReader(new InputStreamReader(fileInput));
            String line = "";
            while ((line = br.readLine()) != null) {
                if (line.contains("#include")) {
                    includeHeader.add(line);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        for (String head : includeHeader) {
            udbInfoRetriever.getHeaders().add(new Include("#include", head.replace(" ", "").split("#include")[1]));
        }
        /* include header addition part finish */

        // global declaration statement expression make
        for (DeclarationStatement s : udbInfoRetriever.getGlobals()) {
            s.setExpression(numberOfParam, typeOfFunc, defines);
            // System.out.println(s);
        }

        // 2023-10-24(Tue) SoheeJung
        // global declaration array value assignment
        for (DeclarationStatement s : udbInfoRetriever.getGlobals()) {
            // System.out.println(s);
            DeclExpression declExpr = (DeclExpression) s.getDeclExpression().get(0);
            IdExpression varIdExpr = (IdExpression) declExpr.getBinaryExpression().getLhsOperand();
            File file = new File(srcPath);
            FileInputStream fileInputStream = null;
            // if array variables have initial values
            if (s.getRawStringData().contains("{...}")) {
                try {
                    fileInputStream = new FileInputStream(file);
                    BufferedReader br = new BufferedReader(new InputStreamReader(fileInputStream));
                    String line = "";
                    while ((line = br.readLine()) != null) {
                        if (line.contains("#define") || line.contains("#include"))
                            continue;
                        if (line.contains(varIdExpr.getName()) && line.contains("{") && line.contains("}")
                                && line.contains(";")) {
                            String[] str1 = line.split("\\{");
                            String[] str2 = str1[1].split("\\}");
                            String elementString = str2[0].replace(" ", "");
                            String[] elementList = elementString.split(",");

                            CompoundExpression declrhsOperand = new CompoundExpression();
                            BracketExpression bracket1 = new BracketExpression();
                            bracket1.setType("{}");
                            bracket1.setPosition(true);
                            declrhsOperand.AddExpression(bracket1);
                            for (String element : elementList) {
                                LiteralExpression literal = new LiteralExpression(element);
                                declrhsOperand.AddExpression(literal);
                            }
                            BracketExpression bracket2 = new BracketExpression();
                            bracket2.setType("{}");
                            bracket2.setPosition(false);
                            declrhsOperand.AddExpression(bracket2);

                            declExpr.getBinaryExpression().setRhsOperand(declrhsOperand);
                            break;
                        }
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            // 2023-10-25(Wed) SoheeJung
            // global variable value assign part
            else {
                // System.out.println("decl statement : " + s);
                // System.out.println("var id expr : " + varIdExpr.getName());
                try {
                    fileInputStream = new FileInputStream(file);
                    BufferedReader br = new BufferedReader(new InputStreamReader(fileInputStream));
                    String line = "";
                    while ((line = br.readLine()) != null) {
                        String templine = line.strip();
                        if (templine.length() == 0)
                            continue;
                        if (templine.charAt(0) == '/')
                            continue;
                        if (line.contains("#define") || line.contains("#include"))
                            continue;
                        if (line.contains(") {"))
                            break; // if function is exist, then break. because after function that is not global
                                   // section..

                        // if there is initial value
                        String lineSplit = line.split(";")[0];
                        String[] lineSplitList = lineSplit.split(" ");
                        ArrayList<String> lineList = new ArrayList<String>();
                        for (String lstr : lineSplitList) {
                            lstr = lstr.strip();
                            lineList.add(lstr);
                        }

                        if (lineList.contains(varIdExpr.getName()) && line.contains("=") && line.contains(";")
                                && !line.contains("[") && !line.contains("]")) {
                            // System.out.println(line);
                            String[] str1 = line.split(";");
                            line = str1[0];
                            str1 = line.split(" ");
                            for (int i = 0; i < str1.length; i++)
                                str1[i] = str1[i].strip();
                            declExpr.setType(new Type(str1[0]));
                            varIdExpr.setType(str1[0]);
                            declExpr.getBinaryExpression().setRhsOperand(new LiteralExpression(str1[3]));

                            // System.out.println(declExpr.getBinaryExpression());
                            break;
                        }
                        // if there is no initial value
                        else if ((lineList.contains(varIdExpr.getName())
                                || (lineList.contains("*" + varIdExpr.getName()))) && line.contains(";")
                                && !line.contains("[") && !line.contains("]") && !line.contains("(")
                                && !line.contains(")")) {
                            // System.out.println(line);
                            String[] str1 = line.split(";");
                            line = str1[0];
                            str1 = line.split(" ");
                            for (int i = 0; i < str1.length; i++)
                                str1[i] = str1[i].strip();
                            if (line.contains("*")) {
                                declExpr.setType(new Type(str1[0] + " *"));
                                varIdExpr.setType(str1[0] + " *");
                            } else {
                                declExpr.setType(new Type(str1[0]));
                                varIdExpr.setType(str1[0]);
                            }
                            declExpr.getBinaryExpression().setOperator(null);
                            declExpr.getBinaryExpression().setRhsOperand(new NullExpression());

                            // System.out.println(declExpr.getBinaryExpression());
                            break;
                        } else if (lineList.contains(varIdExpr.getName()) && line.contains(";") && line.contains("[")
                                && line.contains("]")) {
                            // System.out.println(line);
                            String[] str1 = line.split(";");
                            line = str1[0];
                            str1 = line.split(" ");
                            declExpr.setType(new Type(str1[0]));
                            varIdExpr.setType(str1[0]);
                            String indexVar = (str1[1].split("]")[0]);
                            indexVar = indexVar.split("\\[")[1];
                            // System.out.println(indexVar);
                            CompoundExpression cpdExpr = new CompoundExpression();
                            cpdExpr.AddExpression(varIdExpr);

                            BracketExpression bracketStart = new BracketExpression();
                            bracketStart.setType("[]");
                            bracketStart.setPosition(true);
                            cpdExpr.AddExpression(bracketStart);
                            // literal expression
                            LiteralExpression arrayIndexExpr = new LiteralExpression(indexVar);
                            cpdExpr.AddExpression(arrayIndexExpr);
                            // bracket end expression
                            BracketExpression bracketEnd = new BracketExpression();
                            bracketEnd.setType("[]");
                            bracketEnd.setPosition(false);
                            cpdExpr.AddExpression(bracketEnd);
                            declExpr.getBinaryExpression().setLhsOperand(cpdExpr);
                            declExpr.getBinaryExpression().setOperator(null);
                            declExpr.getBinaryExpression().setRhsOperand(new NullExpression());
                            break;
                        }
                    }
                }

                catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        // function's expression make
        ExprTraverser exprtrav = new ExprTraverser(numberOfParam, typeOfFunc, defines);
        for (Function f : udbInfoRetriever.getFunctions()) {
            exprtrav.traverse(f);
        }

        // 3. text file parsing 
        // 3-1. spec.txt file parsing
        modelConstructor.parseTxtInfo("./src/ALautomation/code/" + filename + "/spec.txt",
                udbInfoRetriever.getVariables());

        // 3-2. global.txt file parsing
        modelConstructor.parseTxtInfo("./src/ALautomation/code/" + filename + "/global.txt",
                udbInfoRetriever.getVariables());

        // 4. make model.h file
        // Process of array variable
        modelConstructor.makeModelHFile(udbInfoRetriever, resultDirPath);

        // 5. make model.c file
        modelConstructor.makeModelCFile(udbInfoRetriever, numberOfParam, typeOfFunc, defines, resultDirPath);

        // 6. make make_trace.c file
        modelConstructor.makeMakeTraceCFile(udbInfoRetriever.getVariables(), resultDirPath);

        // 7. make generate_trace.sh
        modelConstructor.makeTraceShellFile(srcPath, resultDirPath);

        // 8. make gen_assume_assert_from_model.py
        modelConstructor.makeGenAssumeAssertFromModelPyFile(srcPath, udbInfoRetriever.getVariables(), resultDirPath);
    }

    // getter & setter
    public InterestVarList getInputVarList() {
        return inputVarList;
    }

    public void setInputVarList(InterestVarList inputVarList) {
        this.inputVarList = inputVarList;
    }

    public InterestVarList getStateVarList() {
        return stateVarList;
    }

    public void setStateVarList(InterestVarList stateVarList) {
        this.stateVarList = stateVarList;
    }

    public InterestVarList getOutputVarList() {
        return outputVarList;
    }

    public void setOutputVarList(InterestVarList outputVarList) {
        this.outputVarList = outputVarList;
    }

    public String getTargetFunc() {
        return targetFunc;
    }

    public void setTargetFunc(String targetFunc) {
        this.targetFunc = targetFunc;
    }

    public Integer getTrace_m() {
        return trace_m;
    }

    public void setTrace_m(Integer trace_m) {
        this.trace_m = trace_m;
    }

    public Integer getTrace_n() {
        return trace_n;
    }

    public void setTrace_n(Integer trace_n) {
        this.trace_n = trace_n;
    }

    public HashMap<String, String[]> getEnumMap() {
        return enumMap;
    }

    public void setEnumMap(HashMap<String, String[]> enumMap) {
        this.enumMap = enumMap;
    }

    public HashMap<String, Integer> getArrMap() {
        return arrMap;
    }

    public void setArrMap(HashMap<String, Integer> arrMap) {
        this.arrMap = arrMap;
    }

    public ArrayList<String> getTopElement() {
        return topElement;
    }

    public void setTopElement(ArrayList<String> topElement) {
        this.topElement = topElement;
    }

    public ArrayList<String> getBottomElement() {
        return bottomElement;
    }

    public void setBottomElement(ArrayList<String> bottomElement) {
        this.bottomElement = bottomElement;
    }

    public Integer getUnwind() {
        return unwind;
    }

    public void setUnwind(Integer unwind) {
        this.unwind = unwind;
    }

    public Integer getK_ind() {
        return k_ind;
    }

    public void setK_ind(Integer k_ind) {
        this.k_ind = k_ind;
    }

    public Integer getObject_bits() {
        return object_bits;
    }

    public void setObject_bits(Integer object_bits) {
        this.object_bits = object_bits;
    }

    public HashMap<String, ArrayList<DeclExpression>> getStructMap() {
        return structMap;
    }

    public void setStructMap(HashMap<String, ArrayList<DeclExpression>> structMap) {
        this.structMap = structMap;
    }

    public HashMap<String, ArrayList<Integer>> getStructVarScope() {
        return structVarScope;
    }

    public void setStructVarScope(HashMap<String, ArrayList<Integer>> structVarScope) {
        this.structVarScope = structVarScope;
    }

    public String getStructKeyVar() {
        return structKeyVar;
    }

    public void setStructKeyVar(String structKeyVar) {
        this.structKeyVar = structKeyVar;
    }

    public ArrayList<String> getConstList() {
        return constList;
    }

    public void setConstList(ArrayList<String> constList) {
        this.constList = constList;
    }
}
