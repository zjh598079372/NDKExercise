//
// Created by CL002 on 2021-5-12.
//



#ifndef EXERCISENDK_STUDENT_H
#define EXERCISENDK_STUDENT_H

#endif //EXERCISENDK_STUDENT_H
#include "Person.h"
#include <string>

class Student : public Person{

public:
    Student():Person(){}
    Student(string name,int age):Person(name,age){

    }


};