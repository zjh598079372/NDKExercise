//
// Created by CL002 on 2022-6-30.
//

#include "IPlayer.h"
#include "demux/FFDemux.h"

bool IPlayer::open(const char *url) {
    if(!iDemux){
        iDemux = new FFDemux();
    }
    return iDemux->open(url);

}