/**
 * 2023-05-24(Wed) SoheeJung
 * ClassName : GenAssumePyConstructor
 * Automation of gen_assume_assert_from_model.py's generation
 * [from] manually generated model.c & manually generated make_trace.c
 */
package ALautomation;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.FilenameFilter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;

import syntax.expression.BinaryExpression;
import syntax.expression.CompoundExpression;
import syntax.expression.Expression;
import syntax.expression.IdExpression;
import syntax.expression.LibraryCallExpression;
import syntax.global.Define;
import syntax.statement.CompoundStatement;
import syntax.statement.DeclarationStatement;
import syntax.statement.ExpressionStatement;
import syntax.statement.Function;
import syntax.statement.Statement;
import syntax.variable.Variable;
import traverse.general.ExprTraverser;
import udbparser.usingperl.UdbInfoRetriever;

public class GenAssumePyConstructor {
    private InterestVarList inputVarList;
    private InterestVarList stateVarList;
    private InterestVarList outputVarList;
    private Integer unwind;
    private Integer k_ind;
    // 2023-05-30(Tue) SoheeJung
    // model_step function's printf element save
    private ArrayList<String> topElement;
    private ArrayList<String> bottomElement;

    // constructor
    public GenAssumePyConstructor() {
        this.inputVarList = new InterestVarList();
        this.inputVarList.setType("input");
        this.stateVarList = new InterestVarList();
        this.stateVarList.setType("state");
        this.outputVarList = new InterestVarList();
        this.outputVarList.setType("output");
        this.topElement = new ArrayList<String>();
        this.bottomElement = new ArrayList<String>();
        this.unwind = 0;
        this.k_ind = 0;
    }

    /**
     * @date 2023-03-23(Thu)
     * @author SoheeJung
     * @name getInfoFromString
     * @param str : input string that have to preprocess
     * 1. make string no space
     * 2. split by colon(:)
     * 3. return data from second element of string array
     * [example] 
     * (input) func : main
     * (output) main
     */
    public String getDataFromString(String str) {
        str = str.replaceAll(" ", "");
        String[] strlist = str.trim().split(":");
        return strlist[1];
    }

    /**
     * @date 2023-05-24(Wed)
     * @author SoheeJung
     * @name parseTxtInfo
     * @param filePath : spec.txt file path
     * @param variables : model.c variables information
     * read spec.txt and get information (interest variable, unwind, k-induction)
     */
    public void parseTxtInfo(String filePath, HashSet<Variable> variables) throws IOException {
        BufferedReader reader = new BufferedReader(new FileReader(filePath));
        String contents; // file contents

        while((contents = reader.readLine()) != null) {
            // unnecessary information
            if(contents.contains(">> func") || contents.contains(">> trace_m") || contents.contains(">> trace_n") || contents.contains("//")) {
                // do-nothing
            } 
            // process unwind
            else if(contents.contains(">> unwind")) {
                setUnwind(Integer.valueOf(getDataFromString(contents)));
            }
            // process k-induction
            else if(contents.contains(">> k-ind")) {
                setK_ind(Integer.valueOf(getDataFromString(contents)));
            }
            // 2023-05-25(Thu) SoheeJung
            // key interest variable check and process key interest variable
            else if(contents.contains(">> key")) {
                String processString = contents.split(":")[1].trim();
                String[] strlist = processString.split(" ");
                if(strlist[1].contains("input")) {
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
                    for(Variable var : variables) {
                        if(strlist[0].equals(var.getName())) {
                            var.setVarScope(scopelist);
                        }
                    }
                }
                else if(strlist[1].contains("state")) {
                    getStateVarList().getNameList().add(0, strlist[0]);
                    getStateVarList().setKeyCheck(true);
                }
                else if(strlist[1].contains("output")) {
                    getOutputVarList().getNameList().add(0, strlist[0]);
                    getOutputVarList().setKeyCheck(true);
                }
            }
            // process interest variable
            else {
                String[] strlist = contents.split(" ");
                if(strlist[1].contains("input")) {
                    getInputVarList().getNameList().add(strlist[0]);
                    
                    // 2023-03-31(Fri) SoheeJung
                    // make input variable scope list from spec.txt
                    String[] scopestr = strlist[2].split(",");
                    ArrayList<Integer> scopelist = new ArrayList<Integer>();
                    scopelist.add(Integer.valueOf(scopestr[0]));
                    scopelist.add(Integer.valueOf(scopestr[1]));
                    
                    // 2023-04-09(Sun) SoheeJung
                    // if variable is input interest variable, then set variable scope
                    for(Variable var : variables) {
                        if(strlist[0].equals(var.getName())) {
                            var.setVarScope(scopelist);
                        }
                    }
                }
                else if(strlist[1].contains("state")) {
                    getStateVarList().getNameList().add(strlist[0]);
                }
                else if(strlist[1].contains("output")) {
                    getOutputVarList().getNameList().add(strlist[0]);
                }
            }
        }
    }

    /**
     * @date 2023-05-02(Tue)
     * @author SoheeJung
     * @name splitFilePath
     * @return filePathArrayList : index0 -> file path / index1 -> file name
     * split file path and file name
     */
    public ArrayList<String> splitFilePath(String filepath) {
        int lastSplitIndex = filepath.lastIndexOf("/");
        ArrayList<String> filePathList = new ArrayList<String>();

        if(0 < lastSplitIndex) {
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
     * <Code>
     * [Original version] make gen_assume_assert_from_model.py file (automatically) -> but, assume modify manually
     * [new version] make gen_assume_assert_from_model.py file automatically using manually written model.c
     * 
     * <Policy>
     * [original version] use types.txt
     * [modified version] use spec.txt
     */
    private void makeGenAssumeAssertFromModelPyFile(String srcPath, HashSet<Variable> variables) throws IOException {
        File file = new File("./src/ALautomation/code/generate/gen_assume_assert_from_model.py");
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
        writer.println("\tprev_states = [p.split(': ')[1] if (len(p.split(': ')) > 1) else p.split(': ')[0] for p in s1_in_p]");
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
        for(String filename : filenames) {
            // if filename is same as target file name, then continue
            if(filename.equals(srcFilePathList.get(1))) continue;
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
        
        // if STATE / OUTPUT interest variable, then make assume statement. (But, this part write the actual value manually)
        for(String s : getStateVarList().getNameList()) {
            writer.println("\t__CPROVER_assume(prev_state." + s + " >= ? && prev_state." + s + " <= ?); \\n\\");
        }
        for(String s : getOutputVarList().getNameList()) {
            writer.println("\t__CPROVER_assume(prev_output." + s + " >= ? && prev_output." + s + " <= ?); \\n\\");
        }
        for(String s : getStateVarList().getNameList()) {
            writer.println("\t__CPROVER_assume(state." + s + " >= ? && state." + s + " <= ?); \\n\\");
        }
        for(String s : getOutputVarList().getNameList()) {
            writer.println("\t__CPROVER_assume(output." + s + " >= ? && output." + s + " <= ?); \\n\\");
        }
        writer.println("\t\")\n");

        writer.println("\t## Assume INPUT Define Section");
        writer.println("\tf.write(\"\\n\\");
        writer.println("\t// INPUT assume \\n\\");

        // if INPUT interest variable, then make assume statement. (automatically)
        for(String s : getInputVarList().getNameList()) {
            for(Variable v : variables) {
                if(s.equals(v.getName()) && !v.getFileName().equals("NULL")) {
                    writer.println("\t__CPROVER_assume(prev_input." + s + " >= " + String.valueOf(v.getVarScope().get(0)) + " && prev_input." + s + " <= " + String.valueOf(v.getVarScope().get(1)) + "); \\n\\");
                    writer.println("\t__CPROVER_assume(input." + s + " >= " + String.valueOf(v.getVarScope().get(0)) + " && input." + s + " <= " + String.valueOf(v.getVarScope().get(1)) + "); \\n\\");
                }
            }
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
        // 2023-05-25(Thu) SoheeJung
        // find key interest variable from state / output list, then make statement
        if(getStateVarList().isKeyCheck()) writer.println("\t\tv_str = 'state." + getStateVarList().getNameList().get(0) + " == ' + s1");
        else if(getOutputVarList().isKeyCheck()) writer.println("\t\tv_str = 'output." + getOutputVarList().getNameList().get(0) + " == ' + s1");
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

        // 2023-05-25(Thu) SoheeJung
        // find key interest variable from state / output list, then make statement
        if(getStateVarList().isKeyCheck()) writer.println("\t\tv_str = 'state." + getStateVarList().getNameList().get(0) + " == ' + s1");
        else if(getOutputVarList().isKeyCheck()) writer.println("\t\tv_str = 'output." + getOutputVarList().getNameList().get(0) + " == ' + s1");
        /* Finish Modification */

        writer.println("\t\tf.write(\"  __CPROVER_assume((\" + v_str + \")  && \" + p)");
        writer.println("\t\tif prev_states:");
        writer.println("\t\t\tf.write(\" && (\")");
        writer.println("\t\t\tfor i in range(len(prev_states)):");
        writer.println("\t\t\t\ts = prev_states[i]");

        // 2023-05-25(Thu) SoheeJung
        // find key interest variable from state / output list, then make statement
        if(getStateVarList().isKeyCheck()) writer.println("\t\t\t\tv_str = 'state." + getStateVarList().getNameList().get(0) + " == ' + s");
        else if(getOutputVarList().isKeyCheck()) writer.println("\t\t\t\tv_str = 'output." + getOutputVarList().getNameList().get(0) + " == ' + s");
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

        // if INPUT interest variable, then make new_input assume statement. (automatically)
        for(String s : getInputVarList().getNameList()) {
            for(Variable v : variables) {
                if(s.equals(v.getName()) && !v.getFileName().equals("NULL")) {
                    writer.println("\t\t__CPROVER_assume(new_input." + s + " >= " + String.valueOf(v.getVarScope().get(0)) + " && new_input." + s + " <= " + String.valueOf(v.getVarScope().get(1)) + "); \\n\\");
                }
            }
        }
        writer.println("\t\t\")\n");

        // 2023-05-25(Thu) SoheeJung
        // find key interest variable from state / output list, then make statement
        writer.println("\tf.write(\"\\t\" + \"printf(\\\"OUTPUT: %d \" + \" \".join(targetExprs) + \"\\\\n\\\", \\n\\");

        if(getStateVarList().isKeyCheck()) {
            if(getTopElement().contains(getStateVarList().getNameList().get(0))) {
                writer.println("\t\tstate." + getStateVarList().getNameList().get(0) + ", \" + \", \".join(targetNamesOutput) + \");\\n\")");
            } else {
                writer.println("\t\trt_state." + getStateVarList().getNameList().get(0) + ", \" + \", \".join(targetNamesOutput) + \");\\n\")");
            }
        }
        else if(getOutputVarList().isKeyCheck()) {
            if(getTopElement().contains(getOutputVarList().getNameList().get(0))) {
                writer.println("\t\toutput." + getOutputVarList().getNameList().get(0) + ", \" + \", \".join(targetNamesOutput) + \");\\n\")");
            }
            else {
                writer.println("\t\trt_output." + getOutputVarList().getNameList().get(0) + ", \" + \", \".join(targetNamesOutput) + \");\\n\")");
            }
        }

        writer.println("\tf.write(\"\\t\" + \"printf(\\\"OUTPUT: %d \" + \" \".join(targetExprs) + \"\\\\n\\\", \\n\\");

        if(getStateVarList().isKeyCheck()) writer.println("\t\trt_state." + getStateVarList().getNameList().get(0) + ", \" + \", \".join(targetNamesRTStructed) + \");\\n\")");
        else if(getOutputVarList().isKeyCheck()) writer.println("\t\trt_output." + getOutputVarList().getNameList().get(0) + ", \" + \", \".join(targetNamesRTStructed) + \");\\n\")");

        if(getStateVarList().isKeyCheck()) {
            // �뿬湲곗꽌遺��꽣 �닔�젙�븯湲�~~~~~~~~~~~!!!!!!!
            if(getTopElement().contains(getStateVarList().getNameList().get(0))) {
                writer.println("\tf.write(\"\\t\" + \"printf(\\\"PROP: assert(!(state." + getStateVarList().getNameList().get(0) + " == %d && rt_state." + getStateVarList().getNameList().get(0) + " == %d && \" + \" && \".join(targetProp) + \"));\\\", \\n\"\\");
            }
            else {
                writer.println("\tf.write(\"\\t\" + \"printf(\\\"PROP: assert(!(rt_state." + getStateVarList().getNameList().get(0) + " == %d && rt_state." + getStateVarList().getNameList().get(0) + " == %d && \" + \" && \".join(targetProp) + \"));\\\", \\n\"\\");
            }
        }
        else if(getOutputVarList().isKeyCheck()) {
            if(getTopElement().contains(getOutputVarList().getNameList().get(0))) {
                writer.println("\tf.write(\"\\t\" + \"printf(\\\"PROP: assert(!(output." + getOutputVarList().getNameList().get(0) + " == %d && rt_output." + getOutputVarList().getNameList().get(0) + " == %d && \" + \" && \".join(targetProp) + \"));\\\", \\n\"\\");
            }
            else {
                writer.println("\tf.write(\"\\t\" + \"printf(\\\"PROP: assert(!(rt_output." + getOutputVarList().getNameList().get(0) + " == %d && rt_output." + getOutputVarList().getNameList().get(0) + " == %d && \" + \" && \".join(targetProp) + \"));\\\", \\n\"\\");
            }
        }

		if(getStateVarList().isKeyCheck()) {
            if(getTopElement().contains(getStateVarList().getNameList().get(0))) {
                writer.println("\t\t+ \"prev_state." + getStateVarList().getNameList().get(0) + ", state." + getStateVarList().getNameList().get(0) + ", \" + \", \".join(targetNamesPrevStructed) + \");\\n\")");
            }
            else {
                writer.println("\t\t+ \"state." + getStateVarList().getNameList().get(0) + ", state." + getStateVarList().getNameList().get(0) + ", \" + \", \".join(targetNamesPrevStructed) + \");\\n\")");
            }
        }
        else if(getOutputVarList().isKeyCheck()) {
            if(getTopElement().contains(getOutputVarList().getNameList().get(0))) {
                writer.println("\t\t+ \"prev_output." + getOutputVarList().getNameList().get(0) + ", output." + getOutputVarList().getNameList().get(0) + ", \" + \", \".join(targetNamesPrevStructed) + \");\\n\")");
            }
            else {
                writer.println("\t\t+ \"output." + getOutputVarList().getNameList().get(0) + ", output." + getOutputVarList().getNameList().get(0) + ", \" + \", \".join(targetNamesPrevStructed) + \");\\n\")");
            }
        }

        if(getStateVarList().isKeyCheck()) {
            if(getTopElement().contains(getStateVarList().getNameList().get(0))) {
                writer.println("\tf.write(\"\\t\" + \"printf(\\\"ASSUME: __CPROVER_assume(!(prev_state." + getStateVarList().getNameList().get(0) + " == %d && state." + getStateVarList().getNameList().get(0) + " == %d && \" + \" && \".join(targetAssume) + \"));\\\", \\n\"\\");
            }
            else {
                writer.println("\tf.write(\"\\t\" + \"printf(\\\"ASSUME: __CPROVER_assume(!(state." + getStateVarList().getNameList().get(0) + " == %d && state." + getStateVarList().getNameList().get(0) + " == %d && \" + \" && \".join(targetAssume) + \"));\\\", \\n\"\\");
            }
        }
        else if(getOutputVarList().isKeyCheck()) {
            if(getTopElement().contains(getOutputVarList().getNameList().get(0))) {
                writer.println("\tf.write(\"\\t\" + \"printf(\\\"ASSUME: __CPROVER_assume(!(prev_output." + getOutputVarList().getNameList().get(0) + " == %d && output." + getOutputVarList().getNameList().get(0) + " == %d && \" + \" && \".join(targetAssume) + \"));\\\", \\n\"\\");
            }
            else {
                writer.println("\tf.write(\"\\t\" + \"printf(\\\"ASSUME: __CPROVER_assume(!(output." + getOutputVarList().getNameList().get(0) + " == %d && output." + getOutputVarList().getNameList().get(0) + " == %d && \" + \" && \".join(targetAssume) + \"));\\\", \\n\"\\");
            }
        }

        if(getStateVarList().isKeyCheck()) {
            if(getTopElement().contains(getStateVarList().getNameList().get(0))) {
                writer.println("\t\t+ \"prev_state." + getStateVarList().getNameList().get(0) + ", state." + getStateVarList().getNameList().get(0) + ", \" + \", \".join(targetNamesPrevStructed) + \");\\n\")\n");
            }
            else {
                writer.println("\t\t+ \"state." + getStateVarList().getNameList().get(0) + ", state." + getStateVarList().getNameList().get(0) + ", \" + \", \".join(targetNamesPrevStructed) + \");\\n\")\n");
            }
        }
        else if(getOutputVarList().isKeyCheck()) {
            if(getTopElement().contains(getOutputVarList().getNameList().get(0))) {
                writer.println("\t\t+ \"prev_output." + getOutputVarList().getNameList().get(0) + ", output." + getOutputVarList().getNameList().get(0) + ", \" + \", \".join(targetNamesPrevStructed) + \");\\n\")\n");
            }
            else {
                writer.println("\t\t+ \"output." + getOutputVarList().getNameList().get(0) + ", output." + getOutputVarList().getNameList().get(0) + ", \" + \", \".join(targetNamesPrevStructed) + \");\\n\")\n");
            }
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

        // 2023-05-25(Thu) SoheeJung
        // find key interest variable from state / output list, then make statement
        if(getStateVarList().isKeyCheck()) writer.println("\t\t\t\tv_str = 'rt_state." + getStateVarList().getNameList().get(0) + " == ' + s");
        else if(getOutputVarList().isKeyCheck()) writer.println("\t\t\t\tv_str = 'rt_output." + getOutputVarList().getNameList().get(0) + " == ' + s");
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
        writer.println("\t\t\tpredicates[x] = predicates[x].replace('prev_state','state')\n");

        writer.println("\t\t\t## Array interest variable processing (Hyobin)");
        writer.println("\t\t\tpred_vars = list(np.unique(re.findall(r\"[a-zA-Z_][a-zA-Z0-9_]*\", predicates[x])))");
        writer.println("\t\t\tfor pred_var in pred_vars :");
        writer.println("\t\t\t\tif pred_var not in targetConvertedNames: continue");
        writer.println("\t\t\t\ti = targetConvertedNames.index(pred_var)");
        writer.println("\t\t\t\tpredicates[x] = predicates[x].replace(targetConvertedNames[i], targetNames[i])\n");

        writer.println("\t\t\tf.write(\"		if (\" + predicates[x] + \")\\n\")");

        // 2023-05-25(Thu) SoheeJung
        // find key interest variable from state / output list, then make statement
        if(getStateVarList().isKeyCheck()) writer.println("\t\t\tv_str = 'rt_state." + getStateVarList().getNameList().get(0) + " == ' + x");
        else if(getOutputVarList().isKeyCheck()) writer.println("\t\t\tv_str = 'rt_output." + getOutputVarList().getNameList().get(0) + " == ' + x");
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

        // 2023-05-25(Thu) SoheeJung
        // find key interest variable from state / output list, then make statement
        if(getStateVarList().isKeyCheck()) writer.println("\t\t\t\tv_str = 'rt_state." + getStateVarList().getNameList().get(0) + "== ' + s");
        else if(getOutputVarList().isKeyCheck()) writer.println("\t\t\t\tv_str = 'rt_output." + getOutputVarList().getNameList().get(0) + "== ' + s");
        /* Finish Modification */

        writer.println("\t\t\t\tf.write(\"(\" + v_str + \")\")");
        writer.println("\t\t\t\tif i != len(next_states_not_pred)-1:");
        writer.println("\t\t\t\t\tf.write(\" || \")");
        writer.println("\t\t\tf.write(\");\\n\")\n");

        writer.println("\tf.write(\"	}\\n\")");
        writer.println("\tf.write(\"}\\n\")");
        writer.println("\tf.close()\n");

        writer.println("def run_code_proof():");
        writer.println("\tp = subprocess.Popen('goto-cc code/code_proof.c code/model.c -o code/code_proof.goto', stdout=subprocess.PIPE, stderr=subprocess.PIPE, shell=True)");
        writer.println("\toutput,o_err = p.communicate()");
        writer.println("\tp.kill()");
        writer.println("\tif o_err and 'error' in str(o_err):");
        writer.println("\t\tprint(o_err)");
        writer.println("\t\texit(0)\n");

        // 2023-05-25(Thu) SoheeJung
        // make unwind option using spec.txt information
        writer.println("\tp = subprocess.Popen('cbmc code/code_proof.goto --unwind " + getUnwind() + " --object-bits 16" + " --trace > out_assume_assert.txt', stdout=subprocess.PIPE, stderr=subprocess.PIPE, shell=True)");
        
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
        writer.println("\t## state_str = [?]\n");
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
        writer.println("\t\t\t## temp[0] = state_str[temp[0]] ?");
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
        for(String filename : filenames) {
            // if filename is same as target file name, then continue
            if(filename.equals(srcFilePathList.get(1))) continue;
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
        for(String filename : filenames) {
            // if filename is same as target file name, then continue
            if(filename.equals(srcFilePathList.get(1))) continue;
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
        for(String s : getInputVarList().getNameList()) {
            for(Variable v : variables) {
                if(s.equals(v.getName()) && !v.getFileName().equals("NULL")) {
                    writer.println("\t__CPROVER_assume(prev_input." + s + " >= " + String.valueOf(v.getVarScope().get(0)) + " && prev_input." + s + " <= " + String.valueOf(v.getVarScope().get(1)) + "); \\n\\");
                    writer.println("\t__CPROVER_assume(input." + s + " >= " + String.valueOf(v.getVarScope().get(0)) + " && input." + s + " <= " + String.valueOf(v.getVarScope().get(1)) + "); \\n\\");
                }
            }
        }
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

        // if INPUT interest variable, then make new_input assume statement. (automatically)
        for(String s : getInputVarList().getNameList()) {
            for(Variable v : variables) {
                if(s.equals(v.getName()) && !v.getFileName().equals("NULL")) {
                    writer.println("\t\t__CPROVER_assume(new_input." + s + " >= " + String.valueOf(v.getVarScope().get(0)) + " && new_input." + s + " <= " + String.valueOf(v.getVarScope().get(1)) + "); \\n\\");
                }
            }
        }
        writer.println("\t\t\")\n");

        writer.println("\tf.write(\"\\");
        writer.println("\t\tprintf(\\\"OUTPUT: %d \" + \" \".join(targetExprs) + \"\\\\n\\\",						\\n\\");

        // 2023-05-25(Thu) SoheeJung
        // find key interest variable from state / output list, then make statement
        if(getStateVarList().isKeyCheck()) writer.println("\t\t\tstate." + getStateVarList().getNameList().get(0) + ",\" + ','.join(targetNamesOutput) + \");			\\n\")");
        else if(getOutputVarList().isKeyCheck()) writer.println("\t\t\toutput." + getOutputVarList().getNameList().get(0) + ",\" + ','.join(targetNamesOutput) + \");			\\n\")");
        /* Finish Modification */

        writer.println("\tf.write(\"\\");
        writer.println("\t\tprintf(\\\"OUTPUT: %d \" + \" \".join(targetExprs) + \"\\\\n\\\",						\\n\\");

        // 2023-05-25(Thu) SoheeJung
        // find key interest variable from state / output list, then make statement
        if(getStateVarList().isKeyCheck()) writer.println("\t\t\trt_state." + getStateVarList().getNameList().get(0) + ",\" + ','.join(targetNamesRTStructed) + \");			\\n\")");
        else if(getOutputVarList().isKeyCheck()) writer.println("\t\t\trt_output." + getOutputVarList().getNameList().get(0) + ",\" + ','.join(targetNamesRTStructed) + \");			\\n\")");
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

        // 2023-05-25(Thu) SoheeJung
        // find key interest variable from state / output list, then make statement
        if(getStateVarList().isKeyCheck()) writer.println("\t\t\t\tv_str = 'rt_state." + getStateVarList().getNameList().get(0) + " == ' + s");
        else if(getOutputVarList().isKeyCheck()) writer.println("\t\t\t\tv_str = 'rt_output." + getOutputVarList().getNameList().get(0) + " == ' + s");
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
        writer.println("\t\t\tpredicates[x] = predicates[x].replace('prev_state','state')\n");

        writer.println("\t\t\t## Array interest variable processing (Hyobin)");
        writer.println("\t\t\tpred_vars = list(np.unique(re.findall(r\"[a-zA-Z_][a-zA-Z0-9_]*\", predicates[x])))");
        writer.println("\t\t\tfor pred_var in pred_vars :");
        writer.println("\t\t\t\tif pred_var not in targetConvertedNames: continue");
        writer.println("\t\t\t\ti = targetConvertedNames.index(pred_var)");
        writer.println("\t\t\t\tpredicates[x] = predicates[x].replace(targetConvertedNames[i], targetNames[i])\n");

        writer.println("\t\t\tf.write(\"		if (\" + predicates[x] + \")\\n\")");

        // 2023-05-25(Thu) SoheeJung
        // find key interest variable from state / output list, then make statement
        if(getStateVarList().isKeyCheck()) writer.println("\t\t\tv_str = 'rt_state." + getStateVarList().getNameList().get(0) + " == ' + x");
        else if(getOutputVarList().isKeyCheck()) writer.println("\t\t\tv_str = 'rt_output." + getOutputVarList().getNameList().get(0) + " == ' + x");
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

        // 2023-05-25(Thu) SoheeJung
        // find key interest variable from state / output list, then make statement
        if(getStateVarList().isKeyCheck()) writer.println("\t\t\t\tv_str = 'rt_state." + getStateVarList().getNameList().get(0) + " == ' + s");
        else if(getOutputVarList().isKeyCheck()) writer.println("\t\t\t\tv_str = 'rt_output." + getOutputVarList().getNameList().get(0) + " == ' + s");
        /* Finish Modification */

        writer.println("\t\t\t\tf.write(\"(\" + v_str + \")\")");
        writer.println("\t\t\t\tif i != len(next_states_not_pred)-1:");
        writer.println("\t\t\t\t\tf.write(\" || \")");
        writer.println("\t\t\tf.write(\");\\n\")\n");
        writer.println("\tf.write(\"	}\\n\")");
        writer.println("\tf.write(\"}\\n\")");
        writer.println("\tf.close()\n");

        writer.println("def gen_k_ind_files():");
        writer.println("\tp = subprocess.Popen('goto-cc code/code.c code/model.c -o code/code.goto', stdout=subprocess.PIPE, stderr=subprocess.PIPE, shell=True)");
        writer.println("\toutput,o_err = p.communicate()");
        writer.println("\tp.kill()");
        writer.println("\tif o_err and 'error' in str(o_err):");
        writer.println("\t\tprint(o_err)");
        writer.println("\t\texit(0)\n");

        // 2023-05-25(Thu) SoheeJung
        // make k-induction option using spec.txt information
        writer.println("\tp = subprocess.Popen('goto-instrument code/code.goto --k-induction " + getK_ind() + " --base-case code/code_base', stdout=subprocess.PIPE, stderr=subprocess.PIPE, shell=True)");

        writer.println("\toutput,o_err = p.communicate()");
        writer.println("\tp.kill()");
        writer.println("\tif o_err and 'error' in str(o_err):");
        writer.println("\t\tprint(o_err)");
        writer.println("\t\texit(0)\n");

        // 2023-05-25(Thu) SoheeJung
        // make k-induction option using spec.txt information
        writer.println("\tp = subprocess.Popen('goto-instrument code/code.goto --k-induction " + getK_ind() + " --step-case code/code_step', stdout=subprocess.PIPE, stderr=subprocess.PIPE, shell=True)");

        writer.println("\toutput,o_err = p.communicate()");
        writer.println("\tp.kill()");
        writer.println("\tif o_err and 'error' in str(o_err):");
        writer.println("\t\tprint(o_err)");
        writer.println("\t\texit(0)\n");
        
        writer.println("def run_k_ind_base():");

        // 2023-05-25(Thu) SoheeJung
        // make object bits option using spec.txt unwind information
        writer.println("\tp = subprocess.Popen('cbmc code/code_base --object-bits 16" + " > out_base.txt', stdout=subprocess.PIPE, stderr=subprocess.PIPE, shell=True)");

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
        writer.println("\tp = subprocess.Popen('cbmc code/code_step --object-bits 16" + " > out_step.txt', stdout=subprocess.PIPE, stderr=subprocess.PIPE, shell=True)");

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
        writer.println("\t\ttemp = ['?']");
        writer.println("\t\t[temp.append(x.split(': ')[1]) if len(x.split(': '))>1 else temp.append(x.split(': ')[0]) for x in t_list]");
        writer.println("\t\ttrace.append(temp)");
        writer.println("\treturn trace, trace_events\n");

        writer.println("## main");
        writer.println("model_file = sys.argv[1]");
        writer.println("all_valid_traces_file = sys.argv[2]");
        writer.println("assumptions_file = all_valid_traces_file.replace('valid','ass')\n");
        
        writer.println("## assume value assign part ([original] types.txt [modification] spec.txt)");
        writer.println("targetLen = " + String.valueOf(getInputVarList().getNameList().size() + getStateVarList().getNameList().size() + getOutputVarList().getNameList().size() - 1));
        
        // 2023-05-16(Tue) SoheeJung
        // make assume print using spec.txt's information
        /* Start interest variable print */
        String varNameString = "";
        String varTypeString = "";
        String varExprString = "";
        int count = 0;

        for(String s : getStateVarList().getNameList()) {
            if((getInputVarList().getNameList().size() == 0) && (getOutputVarList().getNameList().size() == 0)) {
                if(getStateVarList().isKeyCheck() && count == 0) {
                    // do nothing -> first element is key interest value
                } else if(count < (getStateVarList().getNameList().size() - 1)) {
                    varNameString += "\"" + s +  "\", ";
                    varTypeString += "\"state\", ";
                    varExprString += "\"%d\", ";
                } else {
                    varNameString += "\"" + s + "\"";
                    varTypeString += "\"state\"";
                    varExprString += "\"%d\"";
                }
                count++;
            } else {
                if(getStateVarList().isKeyCheck() && count == 0) {
                    // do nothing -> first element is key interest value
                } else {
                    varNameString += "\"" + s +  "\", ";
                    varTypeString += "\"state\", ";
                    varExprString += "\"%d\", ";
                }
                count++;
            }
        }

        count = 0;
        for(String s : getInputVarList().getNameList()) {
            if ((getOutputVarList().getNameList().size() == 0) || (getOutputVarList().isKeyCheck() && getOutputVarList().getNameList().size() == 1)) {
                if(count < (getInputVarList().getNameList().size() - 1)) {
                    varNameString += "\"" + s +  "\", ";
                    varTypeString += "\"input\", ";
                    varExprString += "\"%d\", ";
                } else {
                    varNameString += "\"" + s + "\"";
                    varTypeString += "\"input\"";
                    varExprString += "\"%d\"";
                }
                count++;
            } else {
                varNameString += "\"" + s +  "\", ";
                varTypeString += "\"input\", ";
                varExprString += "\"%d\", ";
            }
        }

        count = 0;
        for(String s : getOutputVarList().getNameList()) {
            if(getOutputVarList().isKeyCheck() && count == 0) {
                // do nothing -> first element is key interest value
            }
            else if(count < (getOutputVarList().getNameList().size() - 1)) {
                varNameString += "\"" + s +  "\", ";
                varTypeString += "\"output\", ";
                varExprString += "\"%d\", ";
            } else {
                varNameString += "\"" + s + "\"";
                varTypeString += "\"output\"";
                varExprString += "\"%d\"";
            }
            count++;
        }

        writer.println("targetNames = [" + varNameString + "]");
        writer.println("targetTypes = [" + varTypeString + "]");
        writer.println("targetExprs = [" + varExprString + "]");
        /* Finish interest variable print */

        // 2023-05-30(Tue) SoheeJung
        // print model_step's top printf elements & bottom printf elements
        ArrayList<String> newTop = new ArrayList<>();
        for(String s : getTopElement()) newTop.add("\"" + s + "\"");
        String topelem = String.join(", ", newTop);
        writer.println("topElement = [" + topelem + "]");

        ArrayList<String> newBottom = new ArrayList<>();
        for(String s : getBottomElement()) newBottom.add("\"" + s + "\"");
        String bottomelem = String.join(", ", newBottom);
        writer.println("bottomElement = [" + bottomelem + "]");

        writer.println("targetConvertedNames = [name.replace(\"[\", \"_\").replace(\"]\",\"\") for name in targetNames]");
        writer.println("## targetNamesStructed = [targetTypes[i] + \".\" + targetNames[i] for i in range(targetLen)]");
        writer.println("targetNamesOutput = [(\"\" if targetNames[i] in topElement else \"rt_\") + targetTypes[i] + \".\" + targetNames[i] for i in range(targetLen)]");
        writer.println("targetNamesRTStructed = [(\"new_\" if targetTypes[i] == \"input\" else \"rt_\") + targetTypes[i] + \".\" + targetNames[i] for i in range(targetLen)]");
        writer.println("targetNamesStructedforProp = [(\"\" if targetNames[i] in topElement else \"rt_\") + targetTypes[i] + \".\" + targetNames[i] for i in range(targetLen)]");
        writer.println("targetNamesPrevStructed = [(\"prev_\" if targetNames[i] in topElement else \"\") + targetTypes[i] + \".\" + targetNames[i] for i in range(targetLen)]");
        writer.println("targetProp = [targetNamesStructedforProp[i] + \"==\" + targetExprs[i] for i in range(targetLen)]");
        writer.println("targetAssume = [targetNamesPrevStructed[i] + \"==\" + targetExprs[i] for i in range(targetLen)]");
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
        writer.println("\t\t\ttemp_dict['trace_events'] = [x.split()[0] for x in lines[trace_ind[i]+1:trace_ind[i+1]]]");
        writer.println("\t\t\ttemp_dict['ass'] = []");
        writer.println("\t\t\tall_valid_traces.append(temp_dict)\n");
        writer.println("if assumptions_file != 'nil':");
        writer.println("\tinput_file = assumptions_file");
        writer.println("\twith open(input_file, 'rb') as f:");
        writer.println("\t\tnew_assumptions = pickle.load(f)");
        writer.println("else:");
        writer.println("\tnew_assumptions = []\n");
        writer.println("trace_events_file = 'traces/' + model_file.replace('model','trace').replace('.txt','_events.txt')");
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
        writer.println("\t\t\t\t\tif any([(x['trace'][0] == t1 and x['trace'][1][0] != t2 and not x['maybe_valid']) for x in all_valid_traces]):");
        writer.println("\t\t\t\t\t\tprint(colored(\"INVALID CE\",'red'))");
        writer.println("\t\t\t\t\t\tprint(new_traces[j])");
        writer.println("\t\t\t\t\t\tinvalid_trace.append(new_traces[j])");
        writer.println("\t\t\t\t\t\trepeat = True");
        writer.println("\t\t\t\t\t\tnew_assumptions.append(a)");
        writer.println("\t\t\t\t\t\tvalid_trace = [x for x in valid_trace if x != new_traces[j]]");
        writer.println("\t\t\t\t\t\tcontinue\n");
        writer.println("\t\t\t\t\t# conflict with other 'maybe valid CE'");
        writer.println("\t\t\t\t\tif any([(x['trace'][0] == t1 and x['trace'][1][0] != t2 and x['maybe_valid']) for x in all_valid_traces]):");
        writer.println("\t\t\t\t\t\trepeat = True");
        writer.println("\t\t\t\t\t\tconflicts = [x for x in all_valid_traces if (x['trace'][0] == t1 and x['trace'][1][0] != t2 and x['maybe_valid'])]");
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
        writer.println("\t\t\t\tif any([(x['trace'][0] == t1 and x['trace'][1][0] != t2 and x['maybe_valid']) for x in all_valid_traces]):");
        writer.println("\t\t\t\t\tconflicts = [x for x in all_valid_traces if (x['trace'][0] == t1 and x['trace'][1][0] != t2 and x['maybe_valid'])]");
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

    /**
     * @date 2023-05-26(Fri)
     * @author SoheeJung
     * @name parsePrintfFromModelC
     * parse printf statement from model_step function
     * save the information using HashMap<String(top/bottom),ArrayList<String>(variable names)>
     * if printf is on top, then save the variable name top's arrayList
     * If printf is on bottom, then save the variable name bottom's arrayList
     */
    public void parsePrintfFromModelC(Function function) {
        // model_step's first statement
        Statement topStmt = ((CompoundStatement)function.getBody()).getFirst().getNext();
        // first statement's instace type checking
        if(topStmt instanceof ExpressionStatement) {
            Expression topExpr = ((ExpressionStatement)topStmt).getExpression();
            if(topExpr instanceof LibraryCallExpression) {
                Expression element = ((LibraryCallExpression)topExpr).getActualParameters().get(0);
                // save the element to top element array (option 1)
                PrintfExprTraverse(element, 1);
            }
        }

        // model_step's last statement checking
        if((((CompoundStatement)function.getBody()).getLast().getBefore().size() == 1) && (((CompoundStatement)function.getBody()).getLast().getBefore().get(0) instanceof ExpressionStatement)) {
            Statement bottomStatement = ((CompoundStatement)function.getBody()).getLast().getBefore().get(0);
            Expression bottomExpr = ((ExpressionStatement)bottomStatement).getExpression();
            if(bottomExpr instanceof LibraryCallExpression) {
                Expression element = ((LibraryCallExpression)bottomExpr).getActualParameters().get(0);
                // save the element to top element array (option 1)
                PrintfExprTraverse(element, 2);
            }
        }
    }

    /**
     * @date 2023-05-30(Tue)
     * @author SoheeJung
     * @name PrintfExprTraverse
     * @param expr : Printf Library Call Expression's compound expression
     * printf's element is composed of CompoundExpression, so we need to traverse this element.
     * option 1 : top element / option 2 : bottom element
     */
    public void PrintfExprTraverse(Expression expr, Integer option) {
        if(expr instanceof IdExpression) {
            if(!((IdExpression)expr).getName().contains("rt_")) {
                // top element
                if(option == 1) getTopElement().add(((IdExpression)expr).getName());
                // bottom element
                else if(option == 2) getBottomElement().add(((IdExpression)expr).getName());
            }
        }
        else if(expr instanceof BinaryExpression) {
            PrintfExprTraverse(((BinaryExpression)expr).getLhsOperand(), option);
            PrintfExprTraverse(((BinaryExpression)expr).getRhsOperand(), option);
        }
        else if(expr instanceof CompoundExpression) {
            for(Expression e : ((CompoundExpression)expr).getExpression()) {
                PrintfExprTraverse(e, option);
            }
        }
        else {
            // do nothing
        }
    }

    // execute GenAssumePyConstructor (main function)
    public static void main(String[] args) throws IOException{
        GenAssumePyConstructor genAssumePyConstructor = new GenAssumePyConstructor();
        // model.c udb information make
        UdbInfoRetriever udbInfoOrigin = new UdbInfoRetriever();
        UdbInfoRetriever udbInfoModelC = new UdbInfoRetriever();
        // make_trace.c udb information make
        // UdbInfoRetriever udbInfomakeTrace = new UdbInfoRetriever();
        String originalCodePath = "./src/ALautomation/code/projector/projector.c";
        String modelCpath = "./src/ALautomation/code/generate/model.c";
        // String makeTracepath = "";        

        // get all CFG information from UDB and make CFG
        udbInfoOrigin.makeFunctionsFromUDB(originalCodePath);
        udbInfoModelC.makeFunctionsFromUDB(modelCpath);
        // udbInfomakeTrace.makeFunctionsFromUDB(makeTracepath);

        // 1. Original code expression part
        HashMap<Function, Integer> numberOfParam1 = new HashMap<Function, Integer>();
		HashMap<Function, Boolean> typeOfFunc1 = new HashMap<Function, Boolean>(); // check for function type
		for (Function f : udbInfoOrigin.getFunctions()) {
			if (f.getParameters() == null)
				numberOfParam1.put(f, 0);
			else
				numberOfParam1.put(f, f.getParameters().size());
			typeOfFunc1.put(f, f.returnLibraryType());
		}
		
		ArrayList<String> defines1 = new ArrayList<String>();
        // define list make
		for (Define d : udbInfoOrigin.getDefines()){
			defines1.add(d.getName());
		}

        // global declaration statement expression make
        for (DeclarationStatement s : udbInfoOrigin.getGlobals()){
			s.setExpression(numberOfParam1, typeOfFunc1, defines1);
		}

        // function's expression make
        ExprTraverser exprtrav1 = new ExprTraverser(numberOfParam1, typeOfFunc1, defines1); 
		for(Function f : udbInfoOrigin.getFunctions()) {
			exprtrav1.traverse(f);
		}

        // 2. model.c expression part
        // model.c expression make
        HashMap<Function, Integer> numberOfParam2 = new HashMap<Function, Integer>();
		HashMap<Function, Boolean> typeOfFunc2 = new HashMap<Function, Boolean>(); // check for function type
		for (Function f : udbInfoModelC.getFunctions()) {
			if (f.getParameters() == null)
				numberOfParam2.put(f, 0);
			else
				numberOfParam2.put(f, f.getParameters().size());
			typeOfFunc2.put(f, f.returnLibraryType());
		}
		
		ArrayList<String> defines2 = new ArrayList<String>();
        // define list make
		for (Define d : udbInfoModelC.getDefines()){
			defines2.add(d.getName());
		}

        // global declaration statement expression make
        for (DeclarationStatement s : udbInfoModelC.getGlobals()){
			s.setExpression(numberOfParam2, typeOfFunc2, defines2);
		}

        // function's expression make
        ExprTraverser exprtrav = new ExprTraverser(numberOfParam2, typeOfFunc2, defines2); 
		for(Function f : udbInfoModelC.getFunctions()) {
			exprtrav.traverse(f);
		}

        // spec.txt file parsing (interest variable & unwind & k-induction) 
        genAssumePyConstructor.parseTxtInfo("./src/ALautomation/code/projector/spec.txt", udbInfoOrigin.getVariables());

        // printf statement parsing from model.c
        for(Function f : udbInfoModelC.getFunctions()) {
            if(f.getName().equals("model_step")) genAssumePyConstructor.parsePrintfFromModelC(f);
        }

        // make gen_assume_assert_from_model.py
        // [To-do] parameter addition : printf statement parsing and return where it is. 
        genAssumePyConstructor.makeGenAssumeAssertFromModelPyFile(modelCpath, udbInfoOrigin.getVariables());
    }

    // Getter & Setter
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
}
