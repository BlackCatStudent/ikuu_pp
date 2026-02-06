#include <jni.h>
#include <string>
#include <android/log.h>

#define TAG "ClashCore"

extern "C" {
    void startClash(const char* configPath);
    void stopClash();
    int isRunning();
    const char* getVersion();
}

extern "C" {
    void logMessage(const char* message);
}

extern "C++" {
    void startClash(const char* configPath) {
        logMessage("Starting Clash");
    }

    void stopClash() {
        logMessage("Stopping Clash");
    }

    int isRunning() {
        return 0;
    }

    const char* getVersion() {
        return "1.0.0";
    }
}

extern "C" {
    void logMessage(const char* message) {
        __android_log_print(ANDROID_LOG_DEBUG, TAG, "%s", message);
    }
}