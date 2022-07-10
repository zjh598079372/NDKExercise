//
// Created by CL002 on 2022-6-30.
//

#ifndef EXERCISENDK_IPLAYER_H
#define EXERCISENDK_IPLAYER_H
#include "demux/IDemux.h"


class IPlayer {
public:

    virtual bool open(const char* url);

    IDemux* iDemux = 0;

};


#endif //EXERCISENDK_IPLAYER_H
