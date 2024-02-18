#!/bin/sh
javac -d class -cp .:$(find ./lib -name '*.jar' -exec echo -n {}: \;) $(find ./src -name *.java)
jar cf ALRunner.jar -C class .

# java -cp ALRunner.jar testcase.MakeASTTest