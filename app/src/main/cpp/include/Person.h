//
// Created by CL002 on 2021-5-12.
//

#ifndef EXERCISENDK_PERSON_H
#define EXERCISENDK_PERSON_H

#endif //EXERCISENDK_PERSON_H

using namespace std;

#include <string>
#include "XLog.h"

class Person{
private:
    string mName;
    int mAge;
public:
    Person(){}
    Person(string name,int age):mName(name),mAge(age){
    }

    string getName() const {
        return mName;
    }
    int getAge() const {
        return mAge;
    }

    void setName(string name){
        this->mName = name;
    }

    void setAge(int age){
        this->mAge = age;
    }

    virtual void getGrade(){

    }
};