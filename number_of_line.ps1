cd .\src\
(gci -include *.java -recurse | select-string .).Count