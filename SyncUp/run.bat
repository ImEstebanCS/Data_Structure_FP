@echo off
echo ========================================
echo   Ejecutando SyncUp Application
echo ========================================
echo.

REM Configurar JAVA_HOME si no está configurado
if "%JAVA_HOME%"=="" (
    set JAVA_HOME=C:\Program Files\Java\jdk-22
)

REM Ejecutar con Maven
call mvnw.cmd javafx:run

pause


