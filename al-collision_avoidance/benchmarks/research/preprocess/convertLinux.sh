#! /bin/bash
# preprocessing all the files
mkdir final
tr -d '\015' < make_trace.c > final/make_trace.c
tr -d '\015' < generate_trace.sh > final/generate_trace.sh
tr -d '\015' < gen_assume_assert_from_model.py > final/gen_assume_assert_from_model.py
tr -d '\015' < model.c > final/model.c
tr -d '\015' < model.h > final/model.h
rm make_trace.c
rm generate_trace.sh
rm gen_assume_assert_from_model.py
rm model.c
rm model.h
cd final
mv * ../
cd ..
rm -R final
chmod 755 generate_trace.sh

# code location moving part
mv * ../example/code
mv ../example/code/convertLinux.sh .
mv ../example/code/gen_assume_assert_from_model.py ../example