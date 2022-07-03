//
// Created by zjh on 2022/7/3.
//

#include "FFDemux.h"


bool FFDemux::open(const char *url) {

    av_register_all();
    avformat_network_init();
    int result = avformat_open_input(&avFormatContext,url,NULL,NULL);
    result = avformat_find_stream_info(avFormatContext,NULL);
    XLOGE("result-->value-->%d",result);
    XLOGE("result-->value-->%s",strerror(result));
//    strerror(result);
//    std::string hello = "HelloWorld";
    avformat_network_deinit();
    return result;

}