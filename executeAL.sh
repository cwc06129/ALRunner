#!/bin/sh
cp -r ./al-project ./al-$1
cp -r ./src/ALautomation/code/$1/generate/* ./al-$1/benchmarks/research/preprocess
cp -r ./src/ALautomation/code/$1/*.c ./al-$1/benchmarks/research/preprocess
cp -r ./src/ALautomation/code/$1/*.h ./al-$1/benchmarks/research/preprocess
cd ./al-$1/benchmarks/research/preprocess
./convertLinux.sh
cd ..
cd example/code
./generate_trace.sh
cd ..
nohup python3.8 run.py 1 gen &