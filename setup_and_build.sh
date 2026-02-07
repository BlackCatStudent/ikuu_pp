#!/bin/bash

# Ikuu VPN Android Project - Complete Setup and Build Script

echo ""
echo "========================================"
echo "  Ikuu VPN Android Project Setup"
echo "========================================"
echo ""

# Check if running as root
if [ "$EUID" -ne 0 ]; then
    echo "[ERROR] Please run this script as normal user, not root"
    exit 1
fi

# Check Java installation
echo "[1/5] Checking Java installation..."
if ! command -v java &> /dev/null; then
    echo "[ERROR] Java is not installed!"
    echo ""
    echo "Please install JDK 17 from:"
    echo "  https://adoptium.net/temurin/releases/?version=17"
    echo ""
    exit 1
fi

JAVA_VERSION=$(java -version 2>&1 | head -n 1 | awk -F '"' '{print $2}')
echo "[OK] Found Java version: $JAVA_VERSION"

# Check if Java 17
JAVA_MAJOR=$(echo $JAVA_VERSION | cut -d. -f1)
if [ "$JAVA_MAJOR" != "17" ]; then
    echo "[WARNING] Java version is not 17. Found: $JAVA_VERSION"
    echo "[WARNING] This may cause build issues!"
    echo ""
fi

echo ""
echo "[2/5] Checking Android SDK..."
echo ""

# Check Android SDK
if [ -d "$ANDROID_HOME" ]; then
    echo "[OK] Android SDK found at: $ANDROID_HOME"
else
    echo "[ERROR] Android SDK not found!"
    echo ""
    echo "Please install Android Studio from:"
    echo "  https://developer.android.com/studio"
    echo ""
    echo "Or install Command Line Tools from:"
    echo "  https://developer.android.com/studio#command-tools"
    echo ""
    exit 1
fi

echo "[OK] Android SDK configured at: $ANDROID_HOME"
echo ""

# Configure project
echo ""
echo "[3/5] Configuring project..."
echo ""

# Create local.properties if not exists
if [ ! -f local.properties ]; then
    echo "[INFO] Creating local.properties..."
    cat > local.properties <<EOF
sdk.dir=$ANDROID_HOME
ndk.dir=$ANDROID_HOME/ndk-bundle
EOF
    echo "[OK] local.properties created"
fi

echo ""
echo "[4/5] Cleaning previous build..."
echo ""

./gradlew clean

echo ""
echo "[5/5] Building Debug APK..."
echo ""

./gradlew assembleDebug --stacktrace --info

if [ $? -ne 0 ]; then
    echo ""
    echo "[ERROR] Build failed! Check the error messages above."
    echo ""
    exit 1
fi

echo ""
echo "[6/5] Build completed successfully!"
echo ""

# Check if APK was created
if [ -f app/build/outputs/apk/debug/app-debug.apk ]; then
    echo "[OK] APK created at: app/build/outputs/apk/debug/app-debug.apk"
    echo ""
    echo "[7/5] APK Information:"
    if command -v aapt &> /dev/null; then
        aapt dump badging app/build/outputs/apk/debug/app-debug.apk
    else
        echo "[WARNING] aapt not found, skipping APK info"
    fi
    echo ""
else
    echo "[WARNING] APK not found at expected location!"
    echo ""
    echo "[7/5] Searching for APK files..."
    find app/build/outputs/apk/debug -name "*.apk" -type f 2>/dev/null || echo "No APK files found"
    echo ""
fi

echo ""
echo "========================================"
echo "  Setup and Build Complete"
echo "========================================"
echo ""
echo "Press Enter to exit..."
read