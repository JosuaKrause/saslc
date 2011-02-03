#
# saslc is a little compiler for the declarative language SASL.
#
# Building the project:
# If you are lucky to have ANT you may use the corresponding script (build.xml) to
# build the project. If this is not the case you can use the shell script
# (build.sh) to build it. Unfortunately the shell script should be the
# second choice, because currently the java-cup created warnings are not
# removed and the created jars are copies with different manifests in the
# script.
#
# Using it:
# saslc (when built) comes with some handy tools:
# - saslc.jar simply compiles incoming SASL code into SK code.
#    The code is passed to the standard in and the compiled output
#    goes to the standard out or a file if you name one. To use it
#    on files you certainly benefit from redirects:
#      java -jar saslc.jar test.sk < test.sasl
#
# - sasln.jar links one or more sk files to one sk file without symbolic names.
#    Normally it is not necessary to link sk files to run them but the linking
#    process generates much smaller files and unnecessary / unused symbols are
#    thrown away. Due to the fact of not to have to handle symbols and names in
#    linked sk files there is no way to run saslc.jar generated sk directly and
#    without linking:
#      java -jar sasln.jar -out runnable.sk prelude.sk lib.sk program.sk ...
#
# - sk.jar runs a previously linked sk file.
#    The linked sk file can be passed as argument:
#      java -jar sk.jar runnable.sk
#
# - run.jar compiles, links and runs given SASL programs.
#    The arguments can be an arbitrary number of sasl files or code snippets:
#      java -jar run.jar sasl_lib/prelude.sasl "main = take 5 @ iterate {x -> [x]} [];"
#
# - sasl_make.jar reads a makefile and compiles, links and optionally runs a
#    complete project. This example uses the makefile test.smake and
#    runs the project:
#      java -jar sasl_make.jar -r test.smake
#
# For further descriptions use --help on the commands.
#
# The latest updates of saslc can be found here: https://github.com/JosuaKrause/saslc
#
