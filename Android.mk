LOCAL_PATH := $(call my-dir)
include $(CLEAR_VARS)

LOCAL_MODULE := SafeParcel
LOCAL_SRC_FILES := $(call all-java-files-under, safe-parcel/src/main/java)

include $(BUILD_STATIC_JAVA_LIBRARY)
