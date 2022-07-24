//
// Created by CL002 on 2022-6-30.
//

#include <jni.h>
#include "IPlayerPorxy.h"
#include "ConstDefine.h"
#include "demux/FFDemux.h"


void IPlayerPorxy::Init(JavaVM *vm, JNIEnv *env) {
    //创建player
    if (!iPlayer) {
        iPlayer = new IPlayer();
    }
    globalVm = reinterpret_cast<JavaVM *>(env->NewGlobalRef(reinterpret_cast<jobject>(vm)));


}

bool IPlayerPorxy::open(JNIEnv *env, const jobject thiz, const char *url) {
//    if (!fFJniCallback) {
//        fFJniCallback = new FFJniCallback(globalVm, env, thiz);
//    }
//    if (!zjhMedia) {
//        zjhMedia = new ZJHMedia(fFJniCallback);
//        zjhMedia->open(url);
//    }


    return true;
}


/**
 * AudioTrack(int streamType, int sampleRateInHz, int channelConfig, int audioFormat,
 *          int bufferSizeInBytes, int mode)
 *        static public int getMinBufferSize(int sampleRateInHz, int channelConfig, int audioFormat)
 * @param env
 * @return
 */
jobject IPlayerPorxy::initAudioTrack(JNIEnv *env) {

    jclass audioTrackClass = env->FindClass("android/media/AudioTrack");
    jmethodID initMethodID = env->GetMethodID(audioTrackClass, "<init>", "(IIIIII)V");
    jmethodID minBufferSizeID = env->GetStaticMethodID(audioTrackClass, "getMinBufferSize",
                                                       "(III)I");
    int bufferSize = env->CallStaticIntMethod(audioTrackClass, minBufferSizeID, SAMPLE_RATE,
                                              CHANNEL_CONFIG, AUDIO_FORMAT);
    jobject audioTrack = env->NewObject(audioTrackClass, initMethodID,
                                        STREAM_TYPE, SAMPLE_RATE, CHANNEL_CONFIG,
                                        AUDIO_FORMAT, bufferSize, MODE);
    jmethodID playMethodID = env->GetMethodID(audioTrackClass, "play", "()V");
    env->CallVoidMethod(audioTrack, playMethodID);
    return audioTrack;

}
