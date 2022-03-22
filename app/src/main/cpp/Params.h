//
// Created by CL002 on 2021-6-1.
//
#include <string>
using namespace std;
#ifndef EXERCISENDK_PARAMS_H
#define EXERCISENDK_PARAMS_H

static int age = 10;

class Params {

public:
    int num;
    string name;
    int* passWord;

public:
    Params();
    ~Params();
    Params(const Params& params);
    void changeNum() const ;

    static int getValue(){
        return 11;
    }

};


#endif //EXERCISENDK_PARAMS_H
