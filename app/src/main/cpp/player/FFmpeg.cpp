//
// Created by zjh on 2022/7/23.
//

#include <pthread.h>
#include "FFmpeg.h"
#include "audio/FFAudio.h"


FFmpeg::FFmpeg(FFJniCallback *ffJniCallback, const char *path) : ffJniCallback(ffJniCallback) {
    //复制一个url,免得外面异步调用时把url销毁了
    this->url = (char *) (malloc(strlen(path) + 1));
    memcpy(this->url, path, strlen(path) + 1);
}

FFmpeg::~FFmpeg() {
    release();
}

bool FFmpeg::prepare() {
    return prepare(Thread_Mode::THREAD_MAIN);
}

jobject FFmpeg::initAudioTrack(JNIEnv *env) {

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

void* audioThread(void* arg){
    FFmpeg* fFmpeg = (FFmpeg *)(arg);
    XLOGE("audioThread----->");
    fFmpeg->prepare(Thread_Mode::THREAD_CHILD);
    return 0;
}

bool FFmpeg::prepareAsync() {
    pthread_t audioPthread = NULL;
    pthread_create(&audioPthread,NULL,audioThread,this);
    pthread_detach(audioPthread);
    return true;
}

bool FFmpeg::prepare(Thread_Mode threadMode) {
    av_register_all();
    avformat_network_init();
    int result;
    int audioIndex = 0;
    result = avformat_open_input(&avFormatContext, url, NULL, NULL);
    XLOGE("result-->value-->%s,%d", av_err2str(result), result);
    if (result) {
        ffJniCallback->onErrorListener(threadMode, result, av_err2str(result));
        release();
        return false;
    }
    XLOGE("result-->value-->%s,%d", av_err2str(result), result);
    result = avformat_find_stream_info(avFormatContext, NULL);
    if (result) {
        ffJniCallback->onErrorListener(threadMode, result, av_err2str(result));
        release();
        return false;
    }
    audioIndex = av_find_best_stream(avFormatContext, AVMEDIA_TYPE_AUDIO, -1, -1, NULL, 0);
    if (audioIndex < 0) {
        ffJniCallback->onErrorListener(threadMode, result, av_err2str(result));
        release();
        return false;
    }
    ffAudio = new FFAudio(audioIndex,ffJniCallback,avFormatContext,threadMode);
    ffAudio->analysisStream();
    if(ffJniCallback){
        ffJniCallback->onPerpared(threadMode);
    }

    return true;
}

void FFmpeg::play() {
    if(ffAudio){
        ffAudio->play();
    }
}

void FFmpeg::release() {
    if (avFrame) {
        av_frame_free(&avFrame);
        avFrame = NULL;
    }
    if (avPacket) {
        av_packet_free(&avPacket);
        avPacket = NULL;
    }
    if (avCodecContext) {
        avcodec_close(avCodecContext);
        avcodec_free_context(&avCodecContext);
        avCodecContext = 0;
    }
    if (avFormatContext) {
        avformat_close_input(&avFormatContext);
        avformat_free_context(avFormatContext);
        avFormatContext = 0;
    }

    if(ffAudio){
        delete ffAudio;
        ffAudio = NULL;
    }

    avformat_network_deinit();

    if(url){
        free(url);
        url = NULL;
    }
}

void FFmpeg::exitPlay() {
    if(ffAudio && ffAudio->ffPlayStatus){
        ffAudio->ffPlayStatus->isExit = !ffAudio->ffPlayStatus->isExit;
    }

}
