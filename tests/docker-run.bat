@echo off
setlocal
cd /d "%~dp0"

echo.
echo Build docker image
echo.
docker build -t playwright-tests .
if errorlevel 1 (
    echo Build failed!
    pause
    exit /b 1
)

echo.
echo Run docker container
echo.
docker run --rm --ipc=host ^
    -v "%CD%\target:/app/target" ^
    playwright-tests ^
    mvn clean test -B ^
    -Dheadless=true ^
    -Dmaven.clean.failOnError=false ^
    -Dcheckstyle.skip=true ^
    "-DwebUrl=http://host.docker.internal:8001"

set "EXIT_CODE=%errorlevel%"

echo.
echo Results
echo.
if %EXIT_CODE% equ 0 (
    echo Tests PASSED
) else (
    echo Tests FAILED ^(Exit code: %EXIT_CODE%^)
)

echo
echo "Generate Allure report"
echo

mvn allure:serve

echo.
pause
exit /b %EXIT_CODE%