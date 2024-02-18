#! /bin/bash

# remove code folder's files
rm -R code/*_base
rm -R code/*_step
rm -R code/code.c
rm -R code/code_proof.c
rm -R code/*.goto

# remove models folder's files
rm -R models/trace*

# remove traces folder's files
rm -R traces/trace*

# remove files from this directory
rm -R model*.pkl
rm -R model*.txt
rm -R nohup.out
rm -R oneHourModel.txt
rm -R out_*