#######################################################################
# Author: Natasha Yogananda Jeppu, natasha.yogananda.jeppu@cs.ox.ac.uk
#         University of Oxford
# Modifier: SoheeJung
#######################################################################

import sys
import pickle
import os
import subprocess
from termcolor import colored
import re
import ast
import numpy as np
import copy
from string import punctuation

def generate_assume_assert(s1_in_p, pred, s2_out_p, assumptions):
	prev_states = [p.split(': ')[1] if (len(p.split(': ')) > 1) else p.split(': ')[0] for p in s1_in_p]
	prev_states = list(np.unique(prev_states))
	f = open("code/code_proof.c", "w")
	f.write("#include <stddef.h>\n")
	f.write("#include <stdio.h> \n")
	f.write("#include <stdbool.h> \n")
	f.write("#include \"model.h\"\n")
	f.write("#include \"model.c\"\n")
	f.write("#include \"ecrobot_interface.h\"\n")

	f.write("int main()\n")
	f.write("{					\n\
	STATE state;				\n\
	STATE prev_state;			\n\
	INPUT input;				\n\
	INPUT prev_input;			\n\
	OUTPUT output;				\n\
	OUTPUT prev_output;			\n")

	## Assume STATE / OUTPUT Define Section
	f.write("\n\
	// STATE / OUTPUT assume \n\
	__CPROVER_assume(prev_state.motor_direction_status >= 0 && prev_state.motor_direction_status <= 2); \n\
	__CPROVER_assume(prev_state.motor_speed_status >= 0 && prev_state.motor_speed_status <= 3); \n\
	__CPROVER_assume(prev_state.motor_speed[0] >= -2 && prev_state.motor_speed[0] <= 23); \n\
	__CPROVER_assume(prev_state.motor_speed[1] >= -2 && prev_state.motor_speed[1] <= 23); \n\
	")

	## Assume INPUT Define Section
	f.write("\n\
	// INPUT assume \n\
	__CPROVER_assume(prev_input.obsDistance[0] >= 0 && prev_input.obsDistance[0] <= 25); \n\
	__CPROVER_assume(input.obsDistance[0] >= 0 && input.obsDistance[0] <= 25); \n\
	__CPROVER_assume(prev_input.obsDistance[1] >= 0 && prev_input.obsDistance[1] <= 25); \n\
	__CPROVER_assume(input.obsDistance[1] >= 0 && input.obsDistance[1] <= 25); \n\
	__CPROVER_assume(prev_input.obsDistance[2] >= 0 && prev_input.obsDistance[2] <= 25); \n\
	__CPROVER_assume(input.obsDistance[2] >= 0 && input.obsDistance[2] <= 25); \n\
	__CPROVER_assume(prev_input.obsDistance[3] >= 0 && prev_input.obsDistance[3] <= 25); \n\
	__CPROVER_assume(input.obsDistance[3] >= 0 && input.obsDistance[3] <= 25); \n\
	")

	f.write("\n\
	rt_state = prev_state;		\n\
	rt_input = prev_input;		\n\
	rt_output = prev_output;	\n\
	model_step();				\n\
	state = rt_state;			\n\
	output = rt_output;			\n\n")

	p_state_pair = pred.split(': ')
	s1 = p_state_pair[0 if len(p_state_pair) == 1 else 1]

	if len(p_state_pair) == 1:
		s1 = p_state_pair[0]
		v_str = 'state.motor_direction_status == ' + s1
		f.write("   __CPROVER_assume((" + v_str + "));\n")
	else:
		p = p_state_pair[0]
		s1 = p_state_pair[1]
		p = p.replace(' and ',' && ')
		p = p.replace(' or ',' || ')
		p = p.replace('rt_input','prev_input')
		p = p.replace('rt_output','prev_output')
		p = p.replace('not ','!')

		## Array interest variable processing (Hyobin)
		pred_vars = list(np.unique(re.findall(r"[a-zA-Z_][a-zA-Z0-9_]*", p)))
		for pred_var in pred_vars :
			if pred_var not in targetConvertedNames: continue
			i = targetConvertedNames.index(pred_var)
			p = p.replace(targetConvertedNames[i], targetNames[i])

		v_str = 'state.motor_direction_status == ' + s1
		f.write("  __CPROVER_assume((" + v_str + ")  && " + p)
		if prev_states:
			f.write(" && (")
			for i in range(len(prev_states)):
				s = prev_states[i]
				v_str = 'state.motor_direction_status == ' + s
				f.write("(" + v_str + ")")
				if i < len(prev_states)-1:
					f.write(" || ")
			f.write("));\n")
		else:
			f.write(");\n")

	if assumptions:
		for ass in assumptions:
			f.write('\t' + ass)
			f.write('\n')
		f.write('\n')
	f.write("	while(1)	\n\
	{						\n\
		bool found = false;	\n\
							\n\
		rt_state = state;	\n\
		rt_input = input;	\n\
		rt_output = output;	\n\
							\n\
		model_step();		\n\
							\n\
		// new input assume		\n\
		INPUT new_input;	\n\
		__CPROVER_assume(new_input.obsDistance[0] >= 0 && new_input.obsDistance[0] <= 25); \n\
		__CPROVER_assume(new_input.obsDistance[1] >= 0 && new_input.obsDistance[1] <= 25); \n\
		__CPROVER_assume(new_input.obsDistance[2] >= 0 && new_input.obsDistance[2] <= 25); \n\
		__CPROVER_assume(new_input.obsDistance[3] >= 0 && new_input.obsDistance[3] <= 25); \n\
		")

	f.write("\t" + "printf(\"OUTPUT: %d " + " ".join(targetExprs) + "\\n\", \n\
		state.motor_direction_status, " + ", ".join(targetNamesOutput) + ");\n")
	f.write("\t" + "printf(\"OUTPUT: %d " + " ".join(targetExprs) + "\\n\", \n\
		rt_state.motor_direction_status, " + ", ".join(targetNamesRTStructed) + ");\n")
	f.write("\t" + "printf(\"PROP: assert(!(state.motor_direction_status == %d && rt_state.motor_direction_status == %d && " + " && ".join(targetProp) + "));\", \n"\
		+ "prev_state.motor_direction_status, state.motor_direction_status, " + ", ".join(targetNamesPrevStructed) + ");\n")
	f.write("\t" + "printf(\"ASSUME: __CPROVER_assume(!(prev_state.motor_direction_status == %d && state.motor_direction_status == %d && " + " && ".join(targetAssume) + "));\", \n"\
		+ "prev_state.motor_direction_status, state.motor_direction_status, " + ", ".join(targetNamesPrevStructed) + ");\n")

	predicates = {}
	for pred in s2_out_p:
		p_state_pair = pred.split(': ')
		if len(p_state_pair) == 1:
			predicates[p_state_pair[0]] = 0
		else:
			p = p_state_pair[0]
			s1 = p_state_pair[1]
			predicates[s1] = p

	next_states_pred = [x for x in predicates if predicates[x]!=0]
	next_states_not_pred = [x for x in predicates if x not in next_states_pred]

	if not next_states_pred:
		if not next_states_not_pred:
			f.write("		assert(false);\n")
		else:
			f.write("		assert(")
			for i in range(len(next_states_not_pred)):
				s = next_states_not_pred[i]
				v_str = 'rt_state.motor_direction_status == ' + s
				f.write("(" + v_str + ")")
				if i != len(next_states_not_pred)-1:
					f.write(" || ")
			f.write(");\n")
	else:
		for i in range(len(next_states_pred)):
			x = next_states_pred[i]
			predicates[x] = predicates[x].replace(' and ',' && ')
			predicates[x] = predicates[x].replace(' or ',' || ')
			predicates[x] = predicates[x].replace('not ','!')
			predicates[x] = predicates[x].replace('rt_input','input')
			predicates[x] = predicates[x].replace('prev_state','state')
			predicates[x] = predicates[x].replace('rt_state','state')

			## Array interest variable processing (Hyobin)
			pred_vars = list(np.unique(re.findall(r"[a-zA-Z_][a-zA-Z0-9_]*", predicates[x])))
			for pred_var in pred_vars :
				if pred_var not in targetConvertedNames: continue
				i = targetConvertedNames.index(pred_var)
				predicates[x] = predicates[x].replace(targetConvertedNames[i], targetNames[i])

			f.write("		if (" + predicates[x] + ")\n")
			v_str = 'rt_state.motor_direction_status == ' + x
			f.write("		{	assert(" + v_str + ");\n")
			f.write("			found = true;}\n")

		f.write("		if (!found)\n")
		if not next_states_not_pred:
			f.write("			assert(false);\n")
		else:
			f.write("			assert(")
			for i in range(len(next_states_not_pred)):
				s = next_states_not_pred[i]
				v_str = 'rt_state.motor_direction_status== ' + s
				f.write("(" + v_str + ")")
				if i != len(next_states_not_pred)-1:
					f.write(" || ")
			f.write(");\n")

	f.write("	}\n")
	f.write("}\n")
	f.close()

def run_code_proof():
	p = subprocess.Popen('goto-cc code/code_proof.c code/model.c -o code/code_proof.goto', stdout=subprocess.PIPE, stderr=subprocess.PIPE, shell=True)
	output,o_err = p.communicate()
	p.kill()
	if o_err and 'error' in str(o_err):
		print(o_err)
		exit(0)

	p = subprocess.Popen('cbmc code/code_proof.goto --unwind 10 --object-bits 16 --trace > out_assume_assert.txt', stdout=subprocess.PIPE, stderr=subprocess.PIPE, shell=True)
	try:
		output,o_err = p.communicate()
		p.kill()
		if o_err and 'error' in str(o_err):
			print(o_err)
			exit(0)
	except subprocess.TimeoutExpired:
		p.kill()
		print(colored("[WARNING] TIMEOUT",'magenta'))
	except subprocess.CalledProcessError:
		p.kill()
		print(colored("[ERROR] FAILED",'magenta'))

def get_trace():
	## enumeration variable dictionary definition
	motor_direction_status_str = ["Straight","Turn_left","Turn_right"]
	motor_speed_status_str = ["Stop","Slow_speed","Midium_speed","Fast_speed"]

	f = open("out_assume_assert.txt",'r')
	lines = f.readlines()
	f.close()

	if any(["VERIFICATION SUCCESSFUL" in line for line in lines]):
		return [], True

	new_trace = []
	new_traces = []
	count = 0
	for line in lines:
		if "OUTPUT: " in line and "format" not in line:
			temp = line.replace("OUTPUT: ","")
			temp = [int(x) for x in temp.split()]
			## enumeration value and string array matching
			temp[0] = motor_direction_status_str[temp[0]]
			temp[1] = motor_speed_status_str[temp[1]]
			temp = [str(x) for x in temp]

			count = count + 1
			new_trace.append(temp)
			if count == 2:
				new_traces.append(new_trace)
				count = 0
				new_trace = []

	return new_traces, False

def check_validity_code(prop):
	f = open("code/code.c", "w")
	f.write("#include <stddef.h> \n")
	f.write("#include <stdio.h> \n")
	f.write("#include <stdbool.h> \n")
	f.write("#include \"model.h\"\n")
	f.write("#include \"model.c\"\n")
	f.write("#include \"ecrobot_interface.h\"\n")

	f.write("int main()\n")
	f.write("{						\n\
	model_initialize();\n")

	f.write("	while(1)			\n\
	{								\n\
		OUTPUT output = rt_output;	\n\
		STATE state = rt_state;		\n\
		INPUT input;				\n\
		rt_input = input;			\n\
		model_step();				\n\
									\n")

	f.write("		" + prop + ";\n")
	f.write("	}\n")
	f.write("}\n")
	f.close()

def get_prop_assumption():
	f = open("out_assume_assert.txt",'r')
	lines = f.readlines()
	f.close()

	if any(["too many addressed objects" in line for line in lines]):
		exit(0)

	if any(["PARSING ERROR" in line for line in lines]):
		exit(0)

	if any(["VERIFICATION SUCCESSFUL" in line for line in lines]):
		return [], []

	prop = []
	assumptions = []
	for line in lines:
		if "PROP: " in line and "format" not in line:
			prop.append(line.replace("PROP: ",""))
		if "ASSUME: " in line and "format" not in line:
			assumptions.append(line.replace("ASSUME: ",""))

	return prop, assumptions

def get_model(model_file):
	f = open(model_file,'r')
	lines = f.readlines()
	f.close()

	ind = [i for i in range(len(lines)) if "Number of states:" in lines[i]][-1]

	model = re.findall('\[\[.*\]\]', lines[ind-1])[0]
	model = model.replace('\\\'','\'')
	return ast.literal_eval(model)

def get_prop_assumption_str(val):
	return val

def gen_check_init_proof(s2_out_p):
	f = open("code/code_proof.c", "w")
	f.write("#include <stddef.h>\n")
	f.write("#include <stdio.h> \n")
	f.write("#include <stdbool.h> \n")
	f.write("#include\"model.h\"\n")
	f.write("#include \"model.c\"\n")
	f.write("#include \"ecrobot_interface.h\"\n")

	f.write("int main()\n")
	f.write("{			\n\
	model_initialize();	\n\
						\n\
	INPUT input;		\n\
	INPUT prev_input;	\n\
						\n")

	f.write("\n\
	// INPUT assume \n\
	__CPROVER_assume(prev_input.obsDistance[0] >= 0 && prev_input.obsDistance[0] <= 25); \n\
	__CPROVER_assume(input.obsDistance[0] >= 0 && input.obsDistance[0] <= 25); \n\
	__CPROVER_assume(prev_input.obsDistance[1] >= 0 && prev_input.obsDistance[1] <= 25); \n\
	__CPROVER_assume(input.obsDistance[1] >= 0 && input.obsDistance[1] <= 25); \n\
	__CPROVER_assume(prev_input.obsDistance[2] >= 0 && prev_input.obsDistance[2] <= 25); \n\
	__CPROVER_assume(input.obsDistance[2] >= 0 && input.obsDistance[2] <= 25); \n\
	__CPROVER_assume(prev_input.obsDistance[3] >= 0 && prev_input.obsDistance[3] <= 25); \n\
	__CPROVER_assume(input.obsDistance[3] >= 0 && input.obsDistance[3] <= 25); \n\
	")

	f.write("	while(1)			\n\
	{								\n\
		bool found = false;			\n\
		STATE state = rt_state;		\n\
		OUTPUT output = rt_output;	\n\
		rt_input = input;			\n\
									\n\
		model_step();				\n\
									\n\
		// new input assume		\n\
		INPUT new_input;	\n\
		__CPROVER_assume(new_input.obsDistance[0] >= 0 && new_input.obsDistance[0] <= 25); \n\
		__CPROVER_assume(new_input.obsDistance[1] >= 0 && new_input.obsDistance[1] <= 25); \n\
		__CPROVER_assume(new_input.obsDistance[2] >= 0 && new_input.obsDistance[2] <= 25); \n\
		__CPROVER_assume(new_input.obsDistance[3] >= 0 && new_input.obsDistance[3] <= 25); \n\
		")

	f.write("\
		printf(\"OUTPUT: %d " + " ".join(targetExprs) + "\\n\",						\n\
			state.motor_direction_status," + ','.join(targetNamesOutput) + ");			\n")
	f.write("\
		printf(\"OUTPUT: %d " + " ".join(targetExprs) + "\\n\",						\n\
			rt_state.motor_direction_status," + ','.join(targetNamesRTStructed) + ");			\n")

	predicates = {}
	for pred in s2_out_p:
		p_state_pair = pred.split(': ')
		if len(p_state_pair) == 1:
			predicates[p_state_pair[0]] = 0
		else:
			p = p_state_pair[0]
			s1 = p_state_pair[1]
			predicates[s1] = p

	next_states_pred = [x for x in predicates if predicates[x]!=0]
	next_states_not_pred = [x for x in predicates if x not in next_states_pred]

	if not next_states_pred:
		if not next_states_not_pred:
			f.write("		assert(false);\n")
		else:
			f.write("		assert(")
			for i in range(len(next_states_not_pred)):
				s = next_states_not_pred[i]
				v_str = 'rt_state.motor_direction_status == ' + s
				f.write("(" + v_str + ")")
				if i != len(next_states_not_pred)-1:
					f.write(" || ")
			f.write(");\n")
	else:
		for i in range(len(next_states_pred)):
			x = next_states_pred[i]
			predicates[x] = predicates[x].replace(' and ',' && ')
			predicates[x] = predicates[x].replace(' or ',' || ')
			predicates[x] = predicates[x].replace('not ','!')
			predicates[x] = predicates[x].replace('rt_input','input')
			predicates[x] = predicates[x].replace('prev_state','state')
			predicates[x] = predicates[x].replace('rt_state','state')

			## Array interest variable processing (Hyobin)
			pred_vars = list(np.unique(re.findall(r"[a-zA-Z_][a-zA-Z0-9_]*", predicates[x])))
			for pred_var in pred_vars :
				if pred_var not in targetConvertedNames: continue
				i = targetConvertedNames.index(pred_var)
				predicates[x] = predicates[x].replace(targetConvertedNames[i], targetNames[i])

			f.write("		if (" + predicates[x] + ")\n")
			v_str = 'rt_state.motor_direction_status == ' + x
			f.write("		{	assert(" + v_str + ");\n")
			f.write("			found = true;}\n")

		f.write("		if (!found)\n")
		if not next_states_not_pred:
			f.write("			assert(false);\n")
		else:
			f.write("			assert(")
			for i in range(len(next_states_not_pred)):
				s = next_states_not_pred[i]
				v_str = 'rt_state.motor_direction_status == ' + s
				f.write("(" + v_str + ")")
				if i != len(next_states_not_pred)-1:
					f.write(" || ")
			f.write(");\n")

	f.write("	}\n")
	f.write("}\n")
	f.close()

def gen_k_ind_files():
	p = subprocess.Popen('goto-cc code/code.c code/model.c -o code/code.goto', stdout=subprocess.PIPE, stderr=subprocess.PIPE, shell=True)
	output,o_err = p.communicate()
	p.kill()
	if o_err and 'error' in str(o_err):
		print(o_err)
		exit(0)

	p = subprocess.Popen('goto-instrument code/code.goto --k-induction 10 --base-case code/code_base', stdout=subprocess.PIPE, stderr=subprocess.PIPE, shell=True)
	output,o_err = p.communicate()
	p.kill()
	if o_err and 'error' in str(o_err):
		print(o_err)
		exit(0)

	p = subprocess.Popen('goto-instrument code/code.goto --k-induction 10 --step-case code/code_step', stdout=subprocess.PIPE, stderr=subprocess.PIPE, shell=True)
	output,o_err = p.communicate()
	p.kill()
	if o_err and 'error' in str(o_err):
		print(o_err)
		exit(0)

def run_k_ind_base():
	p = subprocess.Popen('cbmc code/code_base --object-bits 16 > out_base.txt', stdout=subprocess.PIPE, stderr=subprocess.PIPE, shell=True)
	try:
		output,o_err = p.communicate()
		p.kill()
		if o_err and 'error' in str(o_err):
			print(o_err)
			exit(0)

	except subprocess.TimeoutExpired:
		p.kill()
		print(colored("[WARNING] TIMEOUT",'magenta'))
	except subprocess.CalledProcessError:
		p.kill()
		print(colored("[ERROR] FAILED",'magenta'))

def run_k_ind_step():
	p = subprocess.Popen('cbmc code/code_step --object-bits 16 > out_step.txt', stdout=subprocess.PIPE, stderr=subprocess.PIPE, shell=True)
	try:
		output,o_err = p.communicate()
		p.kill()
		if o_err and 'error' in str(o_err):
			print(o_err)
			exit(0)

	except subprocess.TimeoutExpired:
		p.kill()
		print(colored("[WARNING] TIMEOUT",'magenta'))
	except subprocess.CalledProcessError:
		p.kill()
		print(colored("[ERROR] FAILED",'magenta'))

def check_validity_k_ind():
	gen_k_ind_files()
	run_k_ind_base()
	f1 = open("out_base.txt",'r')
	base_lines = f1.readlines()
	f1.close()

	if any(["too many addressed objects" in line for line in base_lines]):
		exit(0)

	if any(["PARSING ERROR" in line for line in base_lines]):
		exit(0)

	if any(["VERIFICATION SUCCESSFUL" in line for line in base_lines]):
		print(colored("Base case SUCCESSFUL", 'magenta'))
		run_k_ind_step()
		f2 = open("out_step.txt",'r')
		step_lines = f2.readlines()
		f2.close()

		if any(["too many addressed objects" in line for line in step_lines]):
			exit(0)

		if any(["PARSING ERROR" in line for line in step_lines]):
			exit(0)

		if any(["VERIFICATION SUCCESSFUL" in line for line in step_lines]):
			print(colored("Step case SUCCESSFUL", 'magenta'))
			return True, True
		return True, False

	return False, False

def dfa_traverse(model, trace_events, pred, s2):
	trace_events_pref = []
	start_state = [x[2] for x in model if x[1] == 'start' and x[0] == 1]
	assert(len(start_state) == 1)
	for ind1 in range(len(trace_events)):
		state = start_state[0]
		for ind2 in range(len(trace_events[ind1])):
			event = trace_events[ind1][ind2]
			next_state = [x[2] for x in model if x[1] == event and x[0] == state]

			if not next_state:
				print("no next state")
				exit(0)

			assert(len(next_state) == 1)
			if event == pred and next_state[0] == s2:
				trace_events_pref = [ind1, ind2]
				return trace_events_pref
			state = next_state[0]
	return trace_events_pref

def get_trace_n_trace_events(e_file):
	f = open(e_file,'r')
	data = [x.replace('\n','') for x in f.readlines()]
	f.close()
	trace_ind = [ind for ind in range(len(data)) if data[ind] == 'start']

	trace_events = []
	trace = []
	for i in range(len(trace_ind)-1):
		t_list = data[trace_ind[i]+1:trace_ind[i+1]]
		trace_events.append(t_list)
		## initial state definition
		temp = ['Straight']
		[temp.append(x.split(': ')[1]) if len(x.split(': '))>1 else temp.append(x.split(': ')[0]) for x in t_list]
		trace.append(temp)
	return trace, trace_events

## main
model_file = sys.argv[1]
all_valid_traces_file = sys.argv[2]
assumptions_file = all_valid_traces_file.replace('valid','ass')

## assume value assign part ([original] types.txt [modification] spec.txt)
targetLen = 7
targetNames = ["motor_speed_status", "motor_speed[0]", "motor_speed[1]", "obsDistance[0]", "obsDistance[1]", "obsDistance[2]", "obsDistance[3]"]
targetTypes = ["state", "state", "state", "input", "input", "input", "input"]
targetExprs = ["%d", "%d", "%d", "%d", "%d", "%d", "%d"]
topElement = ["motor_speed_status", "motor_speed[0]", "motor_speed[1]", "obsDistance[0]", "obsDistance[1]", "obsDistance[2]", "obsDistance[3]"]
bottomElement = []
targetConvertedNames = [name.replace("[", "_").replace("]","") for name in targetNames]
## targetNamesStructed = [targetTypes[i] + "." + targetNames[i] for i in range(targetLen)]
targetNamesOutput = [targetNames[i] if targetTypes[i] == "const" else ("" if targetNames[i] in topElement else "rt_") + targetTypes[i] + "." + targetNames[i] for i in range(targetLen)]
targetNamesRTStructed = [targetNames[i] if targetTypes[i] == "const" else ("new_" if targetTypes[i] == "input" else "rt_") + targetTypes[i] + "." + targetNames[i] for i in range(targetLen)]
targetNamesStructedforProp = [targetNames[i] if targetTypes[i] == "const" else ("" if targetNames[i] in topElement else "rt_") + targetTypes[i] + "." + targetNames[i] for i in range(targetLen)]
targetNamesPrevStructed = [targetNames[i] if targetTypes[i] == "const" else ("prev_" if targetNames[i] in topElement else "") + targetTypes[i] + "." + targetNames[i] for i in range(targetLen)]
targetProp = [targetNamesStructedforProp[i] + "==" + targetExprs[i] for i in range(targetLen)]
targetAssume = [targetNamesPrevStructed[i] + "==" + targetExprs[i] for i in range(targetLen)]
## assume value assign part Finish

if all_valid_traces_file != 'nil':
	input_file = all_valid_traces_file
	with open(input_file, 'rb') as f:
		all_valid_traces = pickle.load(f)
else:
	all_valid_traces = []
	f = open('traces/trace1.txt','r')
	lines = [x.replace('\n','') for x in f.readlines()]
	trace_ind = [i for i in range(len(lines)) if lines[i] == 'trace']
	for i in range(len(trace_ind)-1):
		for j in range(trace_ind[i]+1, trace_ind[i+1]):
			if lines[j] == 'trace' or lines[j+1] == 'trace':
				continue
			t1 = lines[j].split()
			t2 = lines[j+1].split()
			temp_dict = {}
			temp_dict['trace'] = [t1,t2]
			temp_dict['maybe_valid'] = False
			temp_dict['trace_events'] = [x.split()[0] for x in lines[trace_ind[i]+1:trace_ind[i+1]]]
			temp_dict['ass'] = []
			all_valid_traces.append(temp_dict)

if assumptions_file != 'nil':
	input_file = assumptions_file
	with open(input_file, 'rb') as f:
		new_assumptions = pickle.load(f)
else:
	new_assumptions = []

trace_events_file = 'traces/' + model_file.replace('model','trace').replace('.txt','_events.txt')
trace_, trace_events_ = get_trace_n_trace_events(trace_events_file)

model = get_model(model_file)
states = [x[0] for x in model]
[states.append(x[2]) for x in model]

states = list(np.unique(states))
in_pred = dict.fromkeys([str(x) for x in states],0)
out_pred = dict.fromkeys([str(x) for x in states],0)

for state in states:
	in_pred[str(state)] = []
	out_pred[str(state)] = []
	[in_pred[str(state)].append(x[1]) for x in model if x[2] == state and x[1]!='start']
	in_pred[str(state)] = list(np.unique(in_pred[str(state)], axis=0))

	[out_pred[str(state)].append(x[1]) for x in model if x[0] == state and x[1]!='start']
	out_pred[str(state)] = list(np.unique(out_pred[str(state)], axis=0))

start_state = [x[2] for x in model if x[1] == 'start' and x[0] == 1]
assert(len(start_state) == 1)
start_state = start_state[0]

invalid_trace = []
for state in states:
	print(state)

	# for trans s1 p s2 where s2 == state
	s2 = state
	s2_in_p = [x[1] for x in model if x[2] == s2 and x[1] != 'start']
	s2_in_p = list(np.unique(s2_in_p))
	s2_out_p = [x[1] for x in model if x[0] == s2 and x[1] != 'start']

	if state == start_state:
		print("Outgoing: ", end="")
		print(s2_out_p)
		gen_check_init_proof(s2_out_p)
		run_code_proof()
		new_traces, outcome = get_trace()
		if outcome:
			print(colored("SUCCESSFUL: Init", 'green'))
			print('\n')
		else:
			print(colored("Failed: Init", 'red'))
			print(colored("New Trace",'blue'))
			print(colored(new_traces,'blue'))
			print('\n')
			if not new_traces:
				exit(0)

			for j in range(len(new_traces)):
				temp_dict = {}
				temp_dict['trace'] = new_traces[j]
				temp_dict['maybe_valid'] = False
				temp_dict['trace_events'] = [x[0] for x in new_traces[j]]
				temp_dict['ass'] = []
				all_valid_traces.append(temp_dict)

	i = 0
	repeat = False
	while i < len(s2_in_p):
		pred = s2_in_p[i]
		s1 = [x[0] for x in model if x[1] == pred and x[2] == s2]
		s1_in_p = [x[1] for x in model if x[2] in s1 and x[1] != 'start']

		print("Predicate: " + pred)
		print("Outgoing: ", end="")
		print(s2_out_p)

		trace_events_pref = dfa_traverse(model, trace_events_, pred, s2)
		trace_prefix = trace_[trace_events_pref[0]][:trace_events_pref[1]+2]

		if not repeat:
			temp_dict = {}
			valid_trace = []

		new_assumptions = list(np.unique(new_assumptions))
		generate_assume_assert([], pred, s2_out_p, new_assumptions)

		run_code_proof()
		new_traces, outcome = get_trace()
		if outcome:
			print(colored("SUCCESSFUL", 'green'))
			print('\n')
			i = i + 1
			continue

		print(colored("FAILED", 'red'))
		print(colored("New Trace",'blue'))
		print(colored(new_traces,'blue'))
		if not new_traces:
			exit(0)
		print('\n')

		repeat = False
		prop, ass = get_prop_assumption()
		for j in range(len(prop)):
			if new_traces[j] in valid_trace:
				print("Already present\n")
				print(new_traces[j])
				continue
			p = get_prop_assumption_str(prop[j])
			a = get_prop_assumption_str(ass[j])
			check_validity_code(p)
			invalid_base, invalid_step = check_validity_k_ind()

			if invalid_base:
				if invalid_step:
					print(colored("INVALID CE",'red'))
					print(new_traces[j])
					invalid_trace.append(new_traces[j])
					repeat = True
					new_assumptions.append(a)
					continue
				else:
					t1 = new_traces[j][0]
					t2 = new_traces[j][1][0]

					# conflict with 'valid CE'
					if any([(x['trace'][0] == t1 and x['trace'][1][0] != t2 and not x['maybe_valid']) for x in all_valid_traces]):
						print(colored("INVALID CE",'red'))
						print(new_traces[j])
						invalid_trace.append(new_traces[j])
						repeat = True
						new_assumptions.append(a)
						valid_trace = [x for x in valid_trace if x != new_traces[j]]
						continue

					# conflict with other 'maybe valid CE'
					if any([(x['trace'][0] == t1 and x['trace'][1][0] != t2 and x['maybe_valid']) for x in all_valid_traces]):
						repeat = True
						conflicts = [x for x in all_valid_traces if (x['trace'][0] == t1 and x['trace'][1][0] != t2 and x['maybe_valid'])]
						if not conflicts:
							exit(0)
						print(colored("Conflicting...",'blue'))
						print(conflicts)

						new_assumptions.append(a)
						for c in conflicts:
							if not c['ass']:
								print("Assumption missing...")
								exit(0)
							new_assumptions.append(c['ass'])
							valid_trace = [x for x in valid_trace if x != c['trace']]
						all_valid_traces = [x for x in all_valid_traces if x not in conflicts]
						continue

					print(colored("[Maybe] Valid CE",'green'))
					print(new_traces[j])

					temp_dict = {}
					temp_dict['trace'] = new_traces[j]
					temp_dict['maybe_valid'] = True
					temp_dict['trace_events'] = trace_prefix.copy()
					temp_dict['trace_events'].append(new_traces[j][1][0])
					temp_dict['ass'] = a

					if temp_dict not in all_valid_traces:
						all_valid_traces.append(temp_dict)
					else:
						print("Already present\n")

					if new_traces[j] not in valid_trace:
						valid_trace.append(new_traces[j])
					else:
						print("Already present\n")
			else:
				print(colored("Valid CE",'green'))
				print(new_traces[j])
				t1 = new_traces[j][0]
				t2 = new_traces[j][1][0]

				# conflict with 'maybe valid CE'
				if any([(x['trace'][0] == t1 and x['trace'][1][0] != t2 and x['maybe_valid']) for x in all_valid_traces]):
					conflicts = [x for x in all_valid_traces if (x['trace'][0] == t1 and x['trace'][1][0] != t2 and x['maybe_valid'])]
					if not conflicts:
						exit(0)
					print(colored("Conflicting...",'blue'))
					print(conflicts)

					for c in conflicts:
						if not c['ass']:
							print("Assumption missing...")
							exit(0)
						new_assumptions.append(c['ass'])
						valid_trace = [x for x in valid_trace if x != c['trace']]
					all_valid_traces = [x for x in all_valid_traces if x not in conflicts]

				temp_dict = {}
				temp_dict['trace'] = new_traces[j]
				temp_dict['maybe_valid'] = False
				temp_dict['trace_events'] = trace_prefix.copy()
				temp_dict['trace_events'].append(new_traces[j][1][0])
				temp_dict['ass'] = []
				if temp_dict not in all_valid_traces:
					all_valid_traces.append(temp_dict)
				else:
					print("Already present\n")

				if new_traces[j] not in valid_trace:
					valid_trace.append(new_traces[j])
				else:
					print("Already present\n")

			if len(valid_trace) == len(prop):
				repeat = False
				break

		if repeat:
			print("Repeating\n")
		else:
			i = i + 1
			print("\n")

pickle_f = open(sys.argv[1].replace('.txt','') + '_valid.pkl','wb')
pickle.dump(all_valid_traces,pickle_f)
pickle_f.close()

pickle_f = open(sys.argv[1].replace('.txt','') + '_ass.pkl','wb')
pickle.dump(new_assumptions,pickle_f)
pickle_f.close()
