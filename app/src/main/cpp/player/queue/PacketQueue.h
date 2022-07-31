//
// Created by zjh on 2022/7/30.
//

#ifndef EXERCISENDK_PACKETQUEUE_H
#define EXERCISENDK_PACKETQUEUE_H

extern "C"{
#include "../../include/ffmpeg/libavcodec/avcodec.h"
}
#include <queue>
#include <pthread.h>
using namespace std;


class PacketQueue {
public:
    queue<AVPacket*>* avPacketQueue = NULL;
    pthread_mutex_t pMutex;
    pthread_cond_t pCond;
public:
    PacketQueue();
    ~PacketQueue();
    void push(AVPacket* avPacket);
    AVPacket* pop();
    void clear();



};


#endif //EXERCISENDK_PACKETQUEUE_H
