# To start the project.
# Compile with build.xml (ANT-Script)
# and then run 'dot_stefan.bat testfile.sasl' when on Windows.
# On a Unix machine you can simply run something like:

# java -jar StefanParser.jar < testfile.sasl | dot -Gcharset=utf8 -Tpdf > testout.pdf

# When compiling without ANT you may use these commands:

java -jar lib/java-cup-11a.jar -nopositions -symbols Terminal -parser Parser cup/subsasl.cup
mv Terminal.java src/xi/parser/
mv Parser.java src/xi/parser/
java -jar lib/JFlex.jar -d src/xi/lexer/ flex/subsasl.flex
javac -classpath lib/java-cup-11a.jar -sourcepath src/ -d bin/

java -cp lib/java-cup-11a.jar:bin/ stefan.TestParser < testfile.sasl | dot -Gcharset=utf8 -Tpdf > testout.pdf