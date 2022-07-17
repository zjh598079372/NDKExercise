//
// Created by CL002 on 2022-6-30.
//

#include <jni.h>
#include "IPlayerPorxy.h"
#include "ConstDefine.h"


void IPlayerPorxy::Init(JavaVM* vm,JNIEnv *env) {
    //创建player
    if(!iPlayer){
        iPlayer = new IPlayer();
    }
//    globalVm = reinterpret_cast<JavaVM *>(env->NewGlobalRef(reinterpret_cast<jobject>(vm)));


}

bool IPlayerPorxy::open(JNIEnv* env, const jobject thiz,const char *url) {
    if(!fFJniCallback){
        fFJniCallback = new FFJniCallback(globalVm,env,thiz);
    }
    release_resorce();
    av_register_all();
    avformat_network_init();
    int result;
    int audioIndex = 0;
    result = avformat_open_input(&avFormatContext,url,NULL,NULL);
    if(result) {
        release_resorce();
        return false;
    }
    result = avformat_find_stream_info(avFormatContext,NULL);
    if(result) {
        release_resorce();
        return false;
    }
    audioIndex = av_find_best_stream(avFormatContext,AVMEDIA_TYPE_AUDIO,-1,-1,NULL,0);
    if(audioIndex < 0) {
        release_resorce();
        return false;
    }
    AVCodec * audioCodec = avcodec_find_decoder(avFormatContext->streams[audioIndex]->codecpar->codec_id);
    avCodecContext = avcodec_alloc_context3(audioCodec);
    result = avcodec_parameters_to_context(avCodecContext,avFormatContext->streams[audioIndex]->codecpar);
    if(result < 0) {
        XLOGE("result-->value-->%s,%d",av_err2str(result),47);
        release_resorce();
        return false;
    }
    result = avcodec_open2(avCodecContext,audioCodec,NULL);
    if(result) {
        XLOGE("result-->value-->%s,%d",av_err2str(result),53);
        release_resorce();
        return false;
    }
    audioTrack = initAudioTrack(env);
    jclass  audioTrackClass = env->FindClass("android/media/AudioTrack");
    jmethodID writeMethodID = env->GetMethodID(audioTrackClass,"write","([BII)I");
    avPacket = av_packet_alloc();
    avFrame = av_frame_alloc();
    int index = 1;
    while (av_read_frame(avFormatContext,avPacket) >= 0){
        if(avPacket->stream_index == audioIndex) {
            result = avcodec_send_packet(avCodecContext,avPacket);
            XLOGE("打印============41");
            if(result == 0){
                result = avcodec_receive_frame(avCodecContext,avFrame);
                if(result == 0){
                    XLOGE("解码第-->%d帧",index);
                    /**
                     * 用AudioTrack播放，上面创建完AudioTrack实例，以及调用了play()方法之后，
                     * 要调用下面的write(byte[] audioData, int offsetInBytes, int sizeInBytes)进行播放
                     * int write(@NonNull byte[] audioData, int offsetInBytes, int sizeInBytes)
                     */
                    //
                    //数组的大小 = 一帧的的采样率*通道数*字节数
                    int bufferSize = av_samples_get_buffer_size(NULL,avFrame->channels,avFrame->nb_samples,
                                               avCodecContext->sample_fmt,0);
                    jbyteArray byteArray = env->NewByteArray(bufferSize);
                    jbyte* byte = env->GetByteArrayElements(byteArray,NULL);
                    memcpy(byte,avFrame->data,bufferSize);
                    env->ReleaseByteArrayElements(byteArray,byte,0);
                    env->CallIntMethod(audioTrack,writeMethodID,byteArray,0,bufferSize);

                }
            }
            index ++;
        }

        av_frame_unref(avFrame);
        av_packet_unref(avPacket);

    }

    if(audioTrack != NULL){
        env->DeleteLocalRef(audioTrack);
    }
    XLOGE("result-->value-->%d",result);
    XLOGE("result-->value-->%s",av_err2str(result));
    release_resorce();
    return true;
}

void IPlayerPorxy::release_resorce() {
    if(avFrame) {
        av_frame_free(&avFrame);
        avFrame = NULL;
    }
    if(avPacket) {
        av_packet_free(&avPacket);
        avPacket = NULL;
    }
    if(avCodecContext) {
        avcodec_close(avCodecContext);
        avcodec_free_context(&avCodecContext);
        avCodecContext = 0;
    }
    if(avFormatContext){
        avformat_close_input(&avFormatContext);
        avformat_free_context(avFormatContext);
        avFormatContext = 0;
    }

    avformat_network_deinit();
    if(fFJniCallback){
        delete fFJniCallback;
        fFJniCallback = NULL;
    }

}

/**
 * AudioTrack(int streamType, int sampleRateInHz, int channelConfig, int audioFormat,
 *          int bufferSizeInBytes, int mode)
 *        static public int getMinBufferSize(int sampleRateInHz, int channelConfig, int audioFormat)
 * @param env
 * @return
 */
jobject  IPlayerPorxy::initAudioTrack(JNIEnv *env) {

    jclass  audioTrackClass = env->FindClass("android/media/AudioTrack");
    jmethodID initMethodID = env->GetMethodID(audioTrackClass,"<init>", "(IIIIII)V");
    jmethodID minBufferSizeID = env->GetStaticMethodID(audioTrackClass,"getMinBufferSize","(III)I");
    int bufferSize = env->CallStaticIntMethod(audioTrackClass,minBufferSizeID,SAMPLE_RATE,CHANNEL_CONFIG,AUDIO_FORMAT);
    jobject audioTrack = env->NewObject(audioTrackClass,initMethodID,
                                        STREAM_TYPE,SAMPLE_RATE,CHANNEL_CONFIG,
                                        AUDIO_FORMAT,bufferSize,MODE);
    jmethodID playMethodID = env->GetMethodID(audioTrackClass,"play","()V");
    env->CallVoidMethod(audioTrack,playMethodID);
    return audioTrack;

}
