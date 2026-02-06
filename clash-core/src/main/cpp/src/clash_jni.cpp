#include <jni.h>
#include <string>
#include <android/log.h>

#define TAG "ClashCore"
#define LOGD(...) __android_log_print(ANDROID_LOG_DEBUG, TAG, __VA_ARGS__)
#define LOGE(...) __android_log_print(ANDROID_LOG_ERROR, TAG, __VA_ARGS__)

extern "C" JNIEXPORT jstring JNICALL
Java_com_ikuuvpn_clashcore_ClashNative_startClash(
    JNIEnv* env,
    jobject,
    jstring config_path
) {
    const char* path = env->GetStringUTFChars(config_path, nullptr);
    
    LOGD("Starting Clash with config: %s", path);
    
    env->ReleaseStringUTFChars(config_path, path);
    
    return env->NewStringUTF("Clash started");
}

extern "C" JNIEXPORT void JNICALL
Java_com_ikuuvpn_clashcore_ClashNative_stopClash(
    JNIEnv* env,
    jobject
) {
    LOGD("Stopping Clash");
}

extern "C" JNIEXPORT jboolean JNICALL
Java_com_ikuuvpn_clashcore_ClashNative_isRunning(
    JNIEnv* env,
    jobject
) {
    return JNI_FALSE;
}

extern "C" JNIEXPORT jstring JNICALL
Java_com_ikuuvpn_clashcore_ClashNative_getVersion(
    JNIEnv* env,
    jobject
) {
    return env->NewStringUTF("1.0.0");
}