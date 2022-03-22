//
// Created by CL002 on 2021-7-14.
//
#include <stdlib.h>
#include <iostream>
#include "XLog.h"
using namespace std;

template <typename E>
class priorityQueue{

private:
    int index = 0;
    int count = 0;
    E* array;
public:
    priorityQueue<E>(int count);
    bool isEmpty();
    void push(E e);
    E pop();
    void shiftUp(int index);
    void shiftDown(int index);
    ~priorityQueue();


};

template <typename E>
priorityQueue<E>::priorityQueue(int count) {
    this->count = count;
    this->array = (E*)malloc(sizeof(E)*count);
}

template <typename E>
bool priorityQueue<E>::isEmpty() {
    return this->index == 0;
}

template <typename E>
void priorityQueue<E>::push(E e) {
    this->index++;
    array[index] = e;
    shiftUp(index);
}

template<typename E>
void priorityQueue<E>::shiftUp(int k) {
    int tempIndex = k;
    int parentIndex = k>>1;
    while (parentIndex >= 1){
        if(array[tempIndex] > array[parentIndex]){
            swap(array[tempIndex],array[parentIndex]);
            tempIndex = parentIndex;
            parentIndex = parentIndex >>1;
        } else{
            break;
        }
    }
}

template <typename E>
E priorityQueue<E>::pop() {
    if(this->index == 0){
        return NULL;
    }
    E max = this->array[1];
    array[1] = array[this->index];
    shiftDown(1);
    this->index--;
    return max;
}


template <typename E>
void priorityQueue<E>::shiftDown(int k) {
    int tempIndex = k;
    int chlidIndex = k<<1;
    while (chlidIndex <= this->index){
        int maxChildIndex = chlidIndex;
        if((chlidIndex + 1)<=this->index && this->array[chlidIndex] < this->array[chlidIndex+1]){
            maxChildIndex = chlidIndex+1;
        }
        swap(this->array[tempIndex],this->array[maxChildIndex]);
        tempIndex = maxChildIndex;
        chlidIndex = maxChildIndex << 1;
    }
}

template <typename E>
priorityQueue<E>::~priorityQueue() {
    if(this->array){
        free(this->array);
        this->array = NULL;
    }
}
