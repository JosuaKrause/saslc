#
# This branch is no longer maintained because of structural changes!
#
# saslc is a little compiler for the declarative language SASL.
# This version comes with a multi-threaded execution environment which
# evaluates for example the following subexpressions "(3 + 5)" and "(9 - 2)"
# independent and simultaneously in the SASL program "main = (3 + 5)*(9 - 2);"
# Shared subexpressions like "x" in "{x -> x + x}" however will still be
# evaluated once.
#
# Building the project:
# If you are lucky to have eclipse you may use the ANT script (build.xml) to
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
#    The usage of this tool can be learned with the command line option -help.
#    Normally it is not necessary to link sk files to run them but the linking
#    process generates much smaller files and unnecessary / unused symbols are
#    thrown away. Due to the fact of not to have to handle symbols and names in
#    linked sk files there is no way to run saslc.jar generated sk directly and
#    without linking:
#      java -jar sasln.jar -out runnable.sk prelude.sk lib.sk program.sk...
#
# - sk.jar runs a previously linked sk file.
#    As of now further arguments are ignored:
#      java -jar sk.jar runnable.sk
#
# - run.jar compiles, links and runs given SASL programs.
#    The arguments can be an arbitrary number of sasl files or code snippets:
#      java -jar run.jar sasl_lib/prelude.sasl "main = take 5 @ iterate {x -> [x]} [];"
#
