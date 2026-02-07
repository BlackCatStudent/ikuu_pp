@echo off
REM Ikuu VPN Android Project - Complete Setup and Build Script
echo.
echo ========================================
echo   Ikuu VPN Android Project Setup
echo ========================================
echo.

REM Check if running as administrator
net session >nul 2>&1
if %errorLevel% neq 0 (
    echo.
    echo [ERROR] Please run this script as Administrator!
    echo.
    pause
    exit /b 1
)

echo [INFO] Checking system requirements...
echo.

REM Check Java installation
echo [1/5] Checking Java installation...
java -version >nul 2>&1
if %errorLevel% neq 0 (
    echo [ERROR] Java is not installed or not in PATH!
    echo.
    echo Please install JDK 17 from:
    echo   https://adoptium.net/temurin/releases/?version=17
    echo.
    pause
    exit /b 1
)

for /f "tokens=3" %%a in ('java -version 2^>^&1') do (
    set JAVA_VERSION=%%a
    set JAVA_MAJOR=%%b
)

echo [OK] Found Java version: %JAVA_VERSION%

REM Check if Java 17
if not "%JAVA_MAJOR%"=="17" (
    echo [WARNING] Java version is not 17. Found: %JAVA_VERSION%
    echo [WARNING] This may cause build issues!
    echo.
)

echo.
echo [2/5] Checking Android SDK...
echo.

REM Check Android SDK
if exist "%LOCALAPPDATA%\Android\Sdk" (
    echo [OK] Android SDK found at: %LOCALAPPDATA%\Android\Sdk
    set ANDROID_HOME=%LOCALAPPDATA%\Android\Sdk
) else (
    echo [ERROR] Android SDK not found!
    echo.
    echo Please install Android Studio from:
    echo   https://developer.android.com/studio
    echo.
    echo Or install Command Line Tools from:
    echo   https://developer.android.com/studio#command-tools
    echo.
    pause
    exit /b 1
)

echo [OK] Android SDK configured at: %ANDROID_HOME%
echo.

REM Configure project
echo.
echo [3/5] Configuring project...
echo.

REM Create local.properties if not exists
if not exist local.properties (
    echo [INFO] Creating local.properties...
    (
        echo sdk.dir=%ANDROID_HOME%
        echo ndk.dir=%ANDROID_HOME%\ndk-bundle
    ) > local.properties
    echo [OK] local.properties created
)

echo.
echo [4/5] Cleaning previous build...
echo.

cd /d "%~dp0"
call gradlew clean

echo.
echo [5/5] Building Debug APK...
echo.

call gradlew assembleDebug --stacktrace --info

if %errorLevel% neq 0 (
    echo.
    echo [ERROR] Build failed! Check the error messages above.
    echo.
    pause
    exit /b 1
)

echo.
echo [6/5] Build completed successfully!
echo.

REM Check if APK was created
if exist app\build\outputs\apk\debug\app-debug.apk (
    echo [OK] APK created at: app\build\outputs\apk\debug\app-debug.apk
    echo.
    echo [7/5] APK Information:
    aapt dump badging app\build\outputs\apk\debug\app-debug.apk
    echo.
) else (
    echo [WARNING] APK not found at expected location!
    echo.
    echo [7/5] Searching for APK files...
    dir /s /b app\build\outputs\apk\debug\*.apk 2>nul
    echo.
)

echo.
echo ========================================
echo   Setup and Build Complete
echo ========================================
echo.
pause