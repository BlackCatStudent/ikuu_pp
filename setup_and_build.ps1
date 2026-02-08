# Ikuu VPN Android Project - Intelligent Setup and Build Script

Write-Host "========================================" -ForegroundColor Cyan
Write-Host "  Ikuu VPN Android Project Setup" -ForegroundColor Cyan
Write-Host "========================================" -ForegroundColor Cyan
Write-Host ""

# Check Java installation
Write-Host "[1/5] Checking Java installation..." -ForegroundColor Yellow
Write-Host ""

try {
    $javaVersion = java -version 2>&1 | Select-String -Pattern "version" | Select-Object -First 1 | ForEach-Object { $_.Split(" ")[1] }
    Write-Host "[OK] Found Java version: $javaVersion" -ForegroundColor Green

    # Check if Java 17
    $javaMajor = $javaVersion.Split(".")[0]
    if ($javaMajor -ne "17") {
        Write-Host "[WARNING] Java version is not 17. Found: $javaVersion" -ForegroundColor Yellow
        Write-Host "[WARNING] This may cause build issues!" -ForegroundColor Yellow
        Write-Host ""
    }
} catch {
    Write-Host "[ERROR] Java is not installed!" -ForegroundColor Red
    Write-Host ""
    Write-Host "Java is required for Android development." -ForegroundColor Yellow
    Write-Host ""
    Write-Host "Options:" -ForegroundColor Cyan
    Write-Host "  1. Install JDK 17 from: https://adoptium.net/temurin/releases/?version=17" -ForegroundColor Yellow
    Write-Host "  2. Install Android Studio (includes JDK): https://developer.android.com/studio" -ForegroundColor Yellow
    Write-Host ""
    Write-Host "Continuing without Java..." -ForegroundColor Yellow
    Write-Host ""
}

Write-Host ""
Write-Host "[2/5] Checking Android SDK..." -ForegroundColor Yellow
Write-Host ""

$androidHome = $env:ANDROID_HOME
if ([string]::IsNullOrEmpty($androidHome)) {
    Write-Host "[INFO] Android SDK not found in environment." -ForegroundColor Yellow
    Write-Host "[INFO] Will try to use project's local.properties..." -ForegroundColor Yellow
    Write-Host ""

    # Check if local.properties exists
    if (Test-Path "local.properties") {
        $localProps = Get-Content "local.properties"
        $sdkDir = ($localProps | Select-String -Pattern "sdk.dir=").ToString().Split("=")[1]
        if (Test-Path $sdkDir) {
            Write-Host "[OK] Found Android SDK in local.properties: $sdkDir" -ForegroundColor Green
            $androidHome = $sdkDir
        } else {
            Write-Host "[WARNING] local.properties exists but sdk.dir not found." -ForegroundColor Yellow
            Write-Host ""
        }
    } else {
        Write-Host "[WARNING] local.properties not found." -ForegroundColor Yellow
        Write-Host ""
    }

    if ([string]::IsNullOrEmpty($androidHome)) {
        Write-Host "[ERROR] Android SDK not found!" -ForegroundColor Red
        Write-Host ""
        Write-Host "Please install Android Studio from: https://developer.android.com/studio" -ForegroundColor Yellow
        Write-Host ""
        Write-Host "Continuing without Android SDK..." -ForegroundColor Yellow
        Write-Host ""
    }
} else {
    Write-Host "[OK] Android SDK configured at: $androidHome" -ForegroundColor Green
}

Write-Host ""
Write-Host "[3/5] Configuring project..." -ForegroundColor Yellow
Write-Host ""

# Create local.properties if not exists
if (-not (Test-Path "local.properties")) {
    Write-Host "[INFO] Creating local.properties..." -ForegroundColor Cyan
    @"
sdk.dir=$androidHome
"@ | Out-File -Encoding ASCII local.properties
    Write-Host "[OK] local.properties created" -ForegroundColor Green
}

Write-Host ""
Write-Host "[4/5] Cleaning previous build..." -ForegroundColor Yellow
Write-Host ""

if (Test-Path "gradlew.bat") {
    & .\gradlew.bat clean
    Write-Host "[OK] Clean completed" -ForegroundColor Green
} else {
    Write-Host "[ERROR] gradlew.bat not found!" -ForegroundColor Red
    Write-Host ""
    Read-Host "Press Enter to exit..."
    exit 1
}

Write-Host ""
Write-Host "[5/5] Building Debug APK..." -ForegroundColor Yellow
Write-Host ""

if (Test-Path "gradlew.bat") {
    $buildResult = & .\gradlew.bat assembleDebug --stacktrace --info 2>&1

    if ($LASTEXITCODE -eq 0) {
        Write-Host ""
        Write-Host "[6/5] Build completed successfully!" -ForegroundColor Green
        Write-Host ""

        # Check if APK was created
        $apkPath = "app\build\outputs\apk\debug\app-debug.apk"
        if (Test-Path $apkPath) {
            Write-Host "[OK] APK created at: $apkPath" -ForegroundColor Green
            Write-Host ""

            # Get file size
            $apkSize = (Get-Item $apkPath).Length / 1MB
            Write-Host "[7/5] APK Information:" -ForegroundColor Cyan
            Write-Host "  File: $apkPath" -ForegroundColor White
            Write-Host "  Size: $apkSize MB" -ForegroundColor White
            Write-Host ""

            # Try to get APK info using aapt
            try {
                if (Get-Command aapt -ErrorAction SilentlyContinue) {
                    Write-Host "  Package Info:" -ForegroundColor White
                    & aapt dump badging $apkPath | Select-String -Pattern "package:" | ForEach-Object { Write-Host "  $_" -ForegroundColor White }
                }
            } catch {
                Write-Host "[INFO] aapt not found, skipping detailed package info" -ForegroundColor Yellow
            }
        } else {
            Write-Host "[WARNING] APK not found at expected location!" -ForegroundColor Yellow
            Write-Host ""
            Write-Host "[7/5] Searching for APK files..." -ForegroundColor Cyan
            Get-ChildItem "app\build\outputs\apk\debug\*.apk" -ErrorAction SilentlyContinue | ForEach-Object {
                Write-Host "  Found: $($_.Name) - $($_.Length / 1MB) MB" -ForegroundColor White
            }
        }
    } else {
        Write-Host ""
        Write-Host "[ERROR] Build failed!" -ForegroundColor Red
        Write-Host ""
        Write-Host "Error output:" -ForegroundColor Yellow
        Write-Host $buildResult
        Write-Host ""
        Write-Host "Please check the error messages above." -ForegroundColor Yellow
        Write-Host ""
    }
} else {
    Write-Host "[ERROR] gradlew.bat not found!" -ForegroundColor Red
    Write-Host ""
}

Write-Host ""
Write-Host "========================================" -ForegroundColor Cyan
Write-Host "  Setup and Build Complete" -ForegroundColor Cyan
Write-Host "========================================" -ForegroundColor Cyan
Write-Host ""
Write-Host "Press Enter to exit..."
Read-Host