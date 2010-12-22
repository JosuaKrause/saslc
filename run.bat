@echo off
if "%1" == "" goto usage
java -jar run.jar %CMD_LINE_ARGS%
goto end
:usage
echo "usage run.bat [-out <output>] <filename|code>+"
:end
