//
// Created by CL002 on 2022-6-30.
//

#include "IPlayerPorxy.h"


void IPlayerPorxy::Init(void *vm) {

    //创建player
    if(!iPlayer){
        iPlayer = new IPlayer();
    }
}

bool IPlayerPorxy::open(const char *url) {
    return iPlayer->open(url);
}
