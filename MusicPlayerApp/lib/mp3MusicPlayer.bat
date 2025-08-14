@echo off
cd /d %~dp0
javac -cp ".;jl1.0.1.jar" MP3MusicPlayer.java
if %errorlevel% neq 0 (
    echo Compilation failed.
    pause
    exit /b
)
java -cp ".;jl1.0.1.jar" MP3MusicPlayer
pause
