//
// Created by zjh on 2022/7/30.
//

#include "PacketQueue.h"

PacketQueue::PacketQueue() {
    avPacketQueue = new queue<AVPacket*>();
    pthread_mutex_init(&pMutex,NULL);
    pthread_cond_init(&pCond,NULL);
}

PacketQueue::~PacketQueue(){
    if (avPacketQueue){
        clear();
        delete avPacketQueue;
        avPacketQueue = NULL;
    }
    pthread_mutex_destroy(&pMutex);
    pthread_cond_destroy(&pCond);
}

void PacketQueue::push(AVPacket *avPacket) {
    pthread_mutex_lock(&pMutex);
    avPacketQueue->push(avPacket);
    pthread_cond_signal(&pCond);
    pthread_mutex_unlock(&pMutex);
}

AVPacket* PacketQueue::pop() {
    pthread_mutex_lock(&pMutex);
    while (avPacketQueue->size() <= 0){
        pthread_cond_wait(&pCond,&pMutex);
    }
    AVPacket* avPacket = avPacketQueue->front();
    avPacketQueue->pop();
    pthread_mutex_unlock(&pMutex);
    return avPacket;

}

void PacketQueue::clear() {
    while (avPacketQueue->size() > 0){
        AVPacket* avPacket = avPacketQueue->front();
        avPacketQueue->pop();
        av_packet_free(&avPacket);
    }

}