//
// Created by zjh on 2022/7/3.
//

#ifndef EXERCISENDK_FFDEMUX_H
#define EXERCISENDK_FFDEMUX_H

#include "IDemux.h"
#include <string>
#include "../../include/XLog.h"
extern "C"{
#include "../../include/ffmpeg/libavformat/avformat.h"
}

class FFDemux : public IDemux{

public:
    AVFormatContext* avFormatContext = NULL;



    virtual bool open(const char *url);

};


#endif //EXERCISENDK_FFDEMUX_H
