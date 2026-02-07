# Ikuu VPN Android Project - Complete Setup Guide

## 📋 Prerequisites

Before running the build script, ensure you have:

### 1. Java Development Kit (JDK) 17
- **Download**: https://adoptium.net/temurin/releases/?version=17
- **Install**: Run the installer and follow the prompts
- **Verify**: Open terminal and run `java -version`
- **Expected output**: `openjdk version "17.0.18+8"`

### 2. Android SDK
You have two options:

#### Option A: Android Studio (Recommended)
- **Download**: https://developer.android.com/studio
- **Install**: Run the installer and follow the prompts
- **Benefits**: Includes JDK, Android SDK, Gradle, and all necessary tools
- **No additional setup required**

#### Option B: Command Line Tools Only
- **Download**: https://developer.android.com/studio#command-tools
- **Install**: Extract to a directory (e.g., `C:\android-sdk`)
- **Requires**: JDK 17 to be installed first
- **Setup**: Configure environment variables

### 3. System Requirements
- **RAM**: 8GB minimum (16GB recommended)
- **Disk Space**: 10GB minimum
- **OS**: Windows 10/11, macOS, or Linux

## 🚀 Quick Start (Windows)

### Using the Automated Script

1. **Right-click** on `setup_and_build.bat`
2. **Select** "Run as administrator"
3. **Wait** for the script to complete
4. **Find** the APK in `app/build/outputs/apk/debug/`

### Manual Steps

If you prefer to do it manually:

#### Step 1: Install JDK 17
```powershell
# Download JDK 17
Invoke-WebRequest -Uri "https://adoptium.net/temurin/releases/?version=17/jdk-17.0.18+8-x64-bin.zip" -OutFile "jdk17.zip"
Expand-Archive -Path jdk17.zip -DestinationPath "C:\Program Files\Java"

# Add to PATH
[Environment]::SetEnvironmentVariable("JAVA_HOME", "C:\Program Files\Java", "User")
[Environment]::SetEnvironmentVariable("Path", "$env:Path;C:\Program Files\Java\bin", "User")

# Verify
java -version
```

#### Step 2: Install Android SDK
```powershell
# Download Command Line Tools
Invoke-WebRequest -Uri "https://dl.google.com/android/repository/commandlinetools/latest/commandlinetools-win-8513334_latest.zip" -OutFile "cmdline-tools.zip"
Expand-Archive -Path cmdline-tools.zip -DestinationPath "C:\android-sdk"

# Set environment variables
[Environment]::SetEnvironmentVariable("ANDROID_HOME", "C:\android-sdk", "User")
[Environment]::SetEnvironmentVariable("ANDROID_SDK_ROOT", "C:\android-sdk", "User")

# Accept licenses
& "C:\android-sdk\cmdline-tools\latest\bin\sdkmanager.bat" --licenses

# Install packages
& "C:\android-sdk\cmdline-tools\latest\bin\sdkmanager.bat" "platform-tools;platform-33.0.2"
& "C:\android-sdk\cmdline-tools\latest\bin\sdkmanager.bat" "build-tools;33.0.2"
& "C:\android-sdk\cmdline-tools\latest\bin\sdkmanager.bat" "platforms;android-31"
& "C:\android-sdk\cmdline-tools\latest\bin\sdkmanager.bat" "build-tools;34.0.0"
& "C:\android-sdk\cmdline-tools\latest\bin\sdkmanager.bat" "platform-tools;34.0.0"
```

#### Step 3: Configure Project
```powershell
# Navigate to project directory
cd "d:\jinxin\vibe coding\clash++\ikuuvpn_PP"

# Create local.properties
"sdk.dir=C:\android-sdk" | Out-File -Encoding ASCII local.properties
"ndk.dir=C:\android-sdk\ndk-bundle" | Out-File -Encoding ASCII local.properties -Append

# Clean build
.\gradlew clean

# Build Debug APK
.\gradlew assembleDebug --stacktrace --info
```

#### Step 4: Find APK
```powershell
# Check if APK exists
if (Test-Path "app\build\outputs\apk\debug\app-debug.apk") {
    Write-Host "APK found!"
    Get-FileHash "app\build\outputs\apk\debug\app-debug.apk"
} else {
    Write-Host "APK not found!"
    Write-Host "Searching for APK files..."
    Get-ChildItem "app\build\outputs\apk\debug\*.apk" | Select-Object -First
}
```

## 🔧 Troubleshooting

### Java Not Found
```
[ERROR] Java is not installed!
```
**Solution**: Install JDK 17 first

### Android SDK Not Found
```
[ERROR] Android SDK not found!
```
**Solution**: Install Android Studio or Command Line Tools

### Build Failed
```
[ERROR] Build failed!
```
**Solution**: Check the error messages and ensure all dependencies are correct

### APK Not Found
```
[WARNING] APK not found at expected location!
```
**Solution**: Check if build completed successfully

## 📱 Using Android Studio

1. **Open Android Studio**
2. **File > Open >** Select project directory
3. **Wait for Gradle sync**
4. **Build > Build Bundle(s) / APK(s) > Build APK(s)**
5. **View results in "Run" window**

## 🎯 Expected Output

After successful build, you should find:
- **APK Location**: `app/build/outputs/apk/debug/app-debug.apk`
- **File Size**: Typically 10-50 MB
- **Package Name**: com.ikuuvpn.app

## 💡 Tips

- **First time**: Run the automated script to check prerequisites
- **Errors**: If you see errors, check the detailed error messages
- **Gradle**: The script uses the project's Gradle wrapper, no need to install Gradle separately
- **Clean build**: The script runs `gradlew clean` before building

## 📞 Support

If you encounter any issues:
1. Check Java version: `java -version`
2. Check Android SDK: Ensure it's properly installed
3. Check project structure: Ensure all files are present
4. Review build logs: Look for specific error messages

Good luck with your build!