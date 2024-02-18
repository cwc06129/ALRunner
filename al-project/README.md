# ActiveLearning
Active Learning of Abstract System Models from Traces using Model Checking:

An active learning framework for generating concise abstractions of a system implementation, instrumented to track a set of system variables.
Generated abstractions admit all system traces defined over the set of system variables.

## Dependencies
1. [CBMC v5.34](https://github.com/diffblue/cbmc) (install and add to PATH)
2. [CVC4 v1.8](https://github.com/CVC4/CVC4-archived) (install and add to PATH)
3. [Trace2Model](https://github.com/natasha-jeppu/Trace2Model)
4. Python 3.9

## Usage
Clone and setup repository
~~~
git clone https://github.com/natasha-jeppu/ActiveLearning.git
cd ActiveLearning
~~~

The `benchmarks` folder has a set of example systems with Python modules for processing CBMC output.</br>
To run any benchmark `<bench_name>` in `<bench_parent_folder>`:
1. Navigate to the benchmark folder
~~~
cd ActiveLearning
cd benchmarks/<bench_parent_folder>/<bench_name>
~~~
2. Run active model learning to generate abstraction
~~~
python3 run_model.py 1
~~~

Generated models for each iteration of learning can be found in the `models` folder inside each benchmark.

