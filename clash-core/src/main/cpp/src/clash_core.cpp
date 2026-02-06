#include "clash_jni.h"

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