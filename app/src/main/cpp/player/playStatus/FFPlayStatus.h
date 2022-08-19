//
// Created by zjh on 2022/8/7.
//

#ifndef EXERCISENDK_FFPLAYSTATUS_H
#define EXERCISENDK_FFPLAYSTATUS_H


class FFPlayStatus {
public:
    /**
     * 是否退出，打算用这个变量来做退出(销毁)
     */
    volatile bool isExit = false;
};


#endif //EXERCISENDK_FFPLAYSTATUS_H
