# builds the whole saslc project

# generate the parser
java -jar lib/java-cup-11a.jar -nopositions -symbols Terminal -parser Parser cup/subsasl.cup
mv Terminal.java src/xi/parser/
mv Parser.java src/xi/parser/
# TODO: sed Parser warnings...

# generate the lexer
java -jar lib/JFlex.jar -d src/xi/lexer/ flex/subsasl.flex
rm src/xi/lexer/Lexer.java~

# compile the sources
mkdir -pv bin
cd bin
javac -classpath ../lib/java-cup-11a.jar -d . -sourcepath ../src ../src/stefan/SKout.java ../src/xi/go/Eval.java ../src/xi/linker/Linker.java

# create jars
jar xf ../lib/java-cup-11a.jar | jar cf ../saslc.jar .
cd ..
# TODO: simple trick - refine later
cp saslc.jar sasln.jar
cp saslc.jar run.jar
cp saslc.jar sk.jar
jar umf build/saslc.mf saslc.jar
jar umf build/sasln.mf sasln.jar
jar umf build/run.mf run.jar
jar umf build/sk.mf sk.jar
