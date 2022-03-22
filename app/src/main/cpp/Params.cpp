//
// Created by CL002 on 2021-6-1.
//

#include <XLog.h>
#include "Params.h"

Params::Params() {
    XLOGE("Params()的构造函数");
}

Params::~Params() {
    XLOGE("Params()的析构函数");
}

Params::Params(const Params &params) {
    XLOGE("Params()的拷贝构造函数");
}

 void Params::changeNum() const {
//    this->num = 22;
    XLOGE("changeNum-->%d",num);
}