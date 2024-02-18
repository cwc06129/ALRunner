#######################################################################
# Author: Natasha Yogananda Jeppu, natasha.yogananda.jeppu@cs.ox.ac.uk
#         University of Oxford
#######################################################################

import sys
import pickle
import os
import subprocess
from termcolor import colored
import re
import time
import ast
import numpy as np

def call_syn_next_event(file):
	cmd = 'python3.8 ../../../Trace2Model/syn_next_event.py -i ' + file
	# cmd = 'python3.8 ../../../../Trace2Model/syn_next_event.py -i ' + file + \
		# ' -dv Block_0_4 rt_input.main_a:N rt_input.main_b:N rt_input.main_c:N rt_state.triangle_a:N rt_state.triangle_b:N rt_state.triangle_c:N rt_state.triangle_result:N' + \
		# ' -dv Block_2_26 rt_state.triangle_a:N rt_state.triangle_b:N rt_state.triangle_c:N rt_state.triangle_match:N rt_state.triangle_result:N' + \
		# ' -dv Block_2_29 rt_state.triangle_a:N rt_state.triangle_b:N rt_state.triangle_c:N rt_state.triangle_match:N rt_state.triangle_result:N' + \
		# ' -dv Block_2_32 rt_state.triangle_a:N rt_state.triangle_b:N rt_state.triangle_c:N rt_state.triangle_match:N rt_state.triangle_result:N' + \
		# ' -dv Block_2_75 rt_state.triangle_result:N rt_output.triangle_return:N'
	print(cmd)
	p = subprocess.Popen(cmd, stdout=sys.stdout, stderr=sys.stdout, shell=True)
	try:
		output,o_err = p.communicate()
		p.kill()
	except subprocess.TimeoutExpired:
		p.kill()
		print(colored("[WARNING] TIMEOUT",'magenta'))
	except subprocess.CalledProcessError:
		p.kill()
		print(colored("[ERROR] FAILED",'magenta'))

def call_learn_model(file, ind):
	cmd = 'python3.8 ../../../Trace2Model/learn_model.py --dfa --incr -t ./models -i ' + file + ' > model' + str(ind) + '.txt'
	print(cmd)
	p = subprocess.Popen(cmd, stdout=sys.stdout, stderr=sys.stdout, shell=True)
	try:
		output,o_err = p.communicate()
		p.kill()
	except subprocess.TimeoutExpired:
		p.kill()
		print(colored("[WARNING] TIMEOUT",'magenta'))
	except subprocess.CalledProcessError:
		p.kill()
		print(colored("[ERROR] FAILED",'magenta'))

def call_gen_assume_assert_model(file, ind):
	cmd = 'python3.8 gen_assume_assert_from_model.py ' + file
	if ind == 1:
		cmd = cmd + ' nil'
	else:
		cmd = cmd + ' model' + str(ind-1) + '_valid.pkl'
	print(cmd)
	p = subprocess.Popen(cmd, stdout=sys.stdout, stderr=sys.stdout, shell=True)
	try:
		output,o_err = p.communicate()
		p.kill()
	except subprocess.TimeoutExpired:
		p.kill()
		print(colored("[WARNING] TIMEOUT",'magenta'))
	except subprocess.CalledProcessError:
		p.kill()
		print(colored("[ERROR] FAILED",'magenta'))

def copy_traces_to_file(all_new_traces, ind):
	f = open('traces/trace' + str(ind+1) + '.txt','a')
	for valid_trace in all_new_traces:
		for t in valid_trace['trace']:
			f.write(' '.join(t) + '\n')
		f.write("trace\n")

	f.close()

def copy_files(file, new_file):
	cmd = 'cp ' + file + ' ' + new_file
	print(cmd)
	p = subprocess.Popen(cmd, stdout=sys.stdout, stderr=subprocess.PIPE, shell=True)
	try:
		output,o_err = p.communicate()
		p.kill()
	except subprocess.TimeoutExpired:
		p.kill()
		print(colored("[WARNING] TIMEOUT",'magenta'))
	except subprocess.CalledProcessError:
		p.kill()
		print(colored("[ERROR] FAILED",'magenta'))


def get_model(model_file):
	f = open(model_file,'r')
	lines = f.readlines()
	f.close()

	ind = [i for i in range(len(lines)) if "Number of states:" in lines[i]][-1]

	model = re.findall('\[\[.*\]\]', lines[ind-1])[0]
	model = model.replace('\\\'','\'')
	return ast.literal_eval(model)

def create_trace_events_file(file, trace, predicates):
	f = open(file, 'w+')
	f.write('start\n')
	for i in range(len(trace)):
		for j in range(len(trace[i])-1):
			if trace[i][j] == trace[i][j+1]:
				f.write(trace[i][j+1])
				f.write('\n')
				continue

			pred = predicates[trace[i][j]][trace[i][j+1]]
			if pred == 0:
				f.write(trace[i][j+1])
			else:
				f.write(pred + ': ' + trace[i][j+1])
			f.write('\n')
		f.write('start\n')

	f.close()

def get_init_valid_trace():
	valid_traces = []
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
			valid_traces.append(temp_dict)
	return valid_traces

model_learn_total = 0
start_time = time.time()
ind = int(sys.argv[1])
mode = sys.argv[2]

while True:
	trace_file = 'traces/trace' + str(ind)
	trace_event_file = trace_file + '_events'
	if ind == 1:
		if mode == 'load' and os.path.isfile(trace_event_file + '.txt'):
			print(trace_event_file + ' already exists.')
		else:
			call_syn_next_event(trace_file + '.txt')

	new_model_file = 'model' + str(ind)
	old_model_file = 'model' + str(ind-1)
	if mode == 'load' and os.path.isfile(new_model_file + '.txt'):
		print(new_model_file + ' already exists.')
	else:
		model_learn_start = time.time()
		call_learn_model(trace_event_file + '.txt', ind)
		print("done")
		model_learn_end = time.time()
		model_learn_total = model_learn_total + (model_learn_end - model_learn_start)

	## 2023-06-14(Wed) SoheeJung
	## time checking addition (which model is generate in that time - per 1 hour)
	## start ##
	time_file = open('oneHourModel.txt', 'a')
	time_checking = time.time()
	time_file.write("time checking : " + str(time_checking - start_time) + "\n")
	time_file.write("old_model_file : " + old_model_file + "\n")
	time_file.write("new_model_file : " + new_model_file + "\n\n")
	## end ##

	new_model = get_model(new_model_file + '.txt')
	if ind == int(sys.argv[1]):
		old_model = []
	else:
		old_model = get_model(old_model_file + '.txt')

	if new_model == old_model:
		print(colored("[WARNING]No update!!\n",'red'))
		print("No new traces!")
		end_time = time.time()
		print(colored("DONE! Model Learning Total Time: " + str(model_learn_total),'blue'))
		print(colored("DONE! Total Time: " + str(end_time-start_time),'blue'))
		exit(0)

	call_gen_assume_assert_model(new_model_file + '.txt', ind)
	input_file = 'model' + str(ind) + '_valid.pkl'
	with open(input_file, 'rb') as f:
		all_valid_traces = pickle.load(f)

	if ind == int(sys.argv[1]):
		old_all_valid_traces = get_init_valid_trace()
	else:
		input_file = old_model_file + '_valid.pkl'
		with open(input_file, 'rb') as f:
			old_all_valid_traces = pickle.load(f)

	if all(x in old_all_valid_traces for x in all_valid_traces):
		print("No new traces!")
		end_time = time.time()
		print(colored("DONE! Model Learning Total Time: " + str(model_learn_total),'blue'))
		print(colored("DONE! Total Time: " + str(end_time-start_time),'blue'))
		exit(0)

	new_file = 'traces/trace' + str(ind+1)

	copy_files('traces/trace1.txt', new_file + '.txt')
	copy_traces_to_file(all_valid_traces,ind)

	model_learn_start = time.time()
	call_syn_next_event(new_file + '.txt')
	model_learn_end = time.time()
	model_learn_total = model_learn_total + (model_learn_end - model_learn_start)

	flag = False
	input_file = new_file + '.pkl'
	with open(input_file, 'rb') as f:
		predicates = pickle.load(f)
	for k1 in predicates:
		if predicates[k1] == 0:
			continue
		for k2 in predicates[k1]:
			if predicates[k1][k2] == '[ERROR] FAILED':
				flag = True
				print("Predicate synth failed!!")
				exit(0)

	ntrace = []
	for traces in all_valid_traces:
		if traces['trace_events'] not in ntrace:
			ntrace.append(traces['trace_events'])
	# print(ntrace)
		
	input_file = new_file + '.pkl'
	with open(input_file, 'rb') as f:
		predicates = pickle.load(f)

	create_trace_events_file(new_file + '_events.txt', ntrace, predicates)
	ind = ind + 1
