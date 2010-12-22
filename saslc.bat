@echo off
if "%1" == "" goto usage
java -jar saslc.jar %1.sk < %1
goto end
:usage
echo "usage sk_out <filename>"
:end
