//
// Created by CL002 on 2022-6-30.
//

#ifndef EXERCISENDK_IPLAYERPORXY_H
#define EXERCISENDK_IPLAYERPORXY_H


#include "IPlayer.h"

class IPlayerPorxy : public IPlayer{
public:

    IPlayer *iPlayer = 0;
    static IPlayerPorxy* Get(){
        static IPlayerPorxy iPlayerPorxy;
        return &iPlayerPorxy;
    }

    void Init(void* vm );

    virtual bool open(const char* url);



};


#endif //EXERCISENDK_IPLAYERPORXY_H
