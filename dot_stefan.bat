@echo off
if "%1" == "" goto usage
java -jar StefanParser.jar < %1 | dot.exe -Gcharset=utf8 -Tpdf > %1.pdf
java -jar StefanParser.jar --shared < %1 | dot.exe -Gcharset=utf8 -Tpdf > %1.shared.pdf
goto end
:usage
echo "usage dot_stefan <filename>"
:end
