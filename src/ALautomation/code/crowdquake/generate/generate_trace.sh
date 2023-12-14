#! /bin/bash
rm trace1.txt
gcc model.c make_trace.c -o a.out -lm
./a.out >> trace1.txt
cp trace1.txt ../traces/trace1.txt
