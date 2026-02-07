# Ikuu VPN - Automated Build Guide

## 🚀 Quick Start (Recommended)

### Step 1: Trigger Automated Build

1. **Visit**: https://github.com/BlackCatStudent/ikuu_pp/actions
2. **Find**: "Build APK (Automated)" workflow
3. **Click**: "Run workflow" button (right side)
4. **Optional**: Enter a tag (e.g., v1.0.0) or leave default
5. **Click**: "Run workflow" button (green)

### Step 2: Wait for Build

1. **Monitor**: Watch the workflow progress
2. **Wait**: 2-5 minutes for build to complete
3. **Check**: Look for green checkmark (✅)

### Step 3: Download APK

1. **Scroll**: To the bottom of the workflow page
2. **Find**: "Artifacts" section
3. **Download**: Click on the APK file you want
   - `app-debug` - Debug version (for testing)
   - `app-release` - Release version (for distribution)

## 📋 What This Workflow Does

This automated workflow:
- ✅ Sets up Java 17 automatically
- ✅ Sets up Android SDK automatically
- ✅ Builds both Debug and Release APKs
- ✅ Uploads APKs as artifacts
- ✅ Provides detailed build summary
- ✅ Uploads build logs if failed

## 🎯 Advantages

### No Local Setup Required
- ✅ No need to install JDK
- ✅ No need to install Android SDK
- ✅ No need to configure environment variables
- ✅ No need to manage dependencies

### Always Fresh Environment
- ✅ Clean build environment every time
- ✅ Latest tools and SDKs
- ✅ Consistent build results

### Easy to Use
- ✅ One-click build
- ✅ Download APKs directly from GitHub
- ✅ View build summary in GitHub Actions

## 🔧 Troubleshooting

### Build Failed?
1. Click on the failed step
2. View detailed error logs
3. Check the build logs artifact
4. Review error messages

### APK Not Found?
1. Check if build succeeded (green checkmark)
2. Check the "Check build output" step
3. Look for APK files listed
4. Download build logs for details

## 💡 Tips

- **First time?** Just run the workflow, no setup needed
- **Multiple builds?** Each build is independent, just run again
- **Custom tag?** Use tags like v1.0.0, v1.0.1, etc.
- **Debug vs Release**: Download both for testing and distribution

## 📱 Installing APK

Once you have the APK:

1. **Enable Unknown Sources**
   - Settings > Security > Unknown Sources
   - Allow installation from this source

2. **Install APK**
   - Open the downloaded APK file
   - Follow installation prompts

3. **Launch App**
   - Open Ikuu VPN from your app drawer
   - Grant necessary permissions

## 🎉 Summary

Using this automated workflow is the **easiest way** to build your Android app:
- No local setup required
- One-click build
- Automatic APK generation
- Direct download from GitHub

Just visit the Actions page and click "Run workflow" to get your APK!