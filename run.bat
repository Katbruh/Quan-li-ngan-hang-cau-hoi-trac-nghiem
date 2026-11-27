@echo off
chcp 65001 > nul
if not exist bin mkdir bin
dir /s /b src\*.java > bin\sources.txt
javac -encoding UTF-8 -d bin @bin\sources.txt
if errorlevel 1 (
    del bin\sources.txt > nul 2>&1
    echo Bien dich that bai!
    pause
    exit /b 1
)
del bin\sources.txt > nul 2>&1
java -cp bin Main
