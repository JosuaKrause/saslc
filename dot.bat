@echo off
if "%1" == "" goto usage
if exist temp.dot goto already
java -jar Parser.jar -dot %1 temp.dot
dot.exe -Gcharset=utf8 -Tpdf temp.dot > %1.pdf
del temp.dot
goto end
:already
echo temp.dot exists already - please make sure that there is no such file!
:usage
echo "usage dot <filename>"
:end
