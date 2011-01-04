# builds the whole saslc project

# generate the parser
java -jar lib/java-cup-11a.jar -nopositions -symbols Terminal -parser Parser cup/subsasl.cup
mv Terminal.java src/xi/parser/
mv Parser.java src/xi/parser/

# generate the lexer
java -jar lib/JFlex.jar -d src/xi/lexer/ flex/subsasl.flex
rm src/xi/lexer/Lexer.java~

# compile the sources
javac -classpath lib/java-cup-11a.jar -d ./bin -sourcepath ./src ./src/stefan/SKout.java src/xi/go/Eval.java src/xi/linker/Linker.java

# create jars
jar cmf build/saslc.mf saslc.jar bin
jar cmf build/sasln.mf sasln.jar bin
jar cmf build/run.mf run.jar bin
jar cmf build/sk.mf sk.jar bin
