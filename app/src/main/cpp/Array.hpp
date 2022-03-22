//
// Created by CL002 on 2021-5-10.
//

#ifndef EXERCISENDK_ARRAY_HPP
#define EXERCISENDK_ARRAY_HPP

#endif //EXERCISENDK_ARRAY_HPP
#ifndef EXERCISENDK_ARRAYLIST_H
#define EXERCISENDK_ARRAYLIST_H

#endif //EXERCISENDK_ARRAYLIST_H

#include "include/XLog.h"
#include <malloc.h>

template<typename E>
class Array {

private:
    int index = 0;
    int len = 0;
    int size = 0;
    E* arr;
public:
    Array<E>();

    Array<E>(int len);

    void add(E e);

    E remvoe(int index);

    E get(int index);

    ~Array();

    void ensureCapacityInternal(int i);

    void grow(int capacity);
};



template<typename E>
Array<E>::Array() {
    Array(10);
}

template<typename E>
Array<E>::Array(int len) {
    this->size = len;
    this->arr = (E *) malloc(sizeof(E) * len);
}

template<typename E>
void Array<E>::add(E e) {
    ensureCapacityInternal(this->len + 1);
    this->arr[this->index] = e;
    XLOGE("arr[%d]==%s", this->index, *(arr + index));
    this->index++;
    this->len++;

}

template<typename E>
E Array<E>::remvoe(int index) {
    if (index < 0 || index > this->index)return NULL;
    E oldValue = *(arr+index);
    E* new_Arr = (E*)malloc(sizeof(E)*size);

    if (new_Arr){
        memcpy(new_Arr,this->arr,sizeof(E)*index);
        memcpy(new_Arr+index,this->arr+index+1,sizeof(E)*(size-index));
        this->index --;
        this->len --;
        free(this->arr);

    }

    this->arr = new_Arr;

    return oldValue;
}

template<typename E>
E Array<E>::get(int index1) {
    if (index1 > this->len - 1) return NULL;
    return *(arr+index1);
}

template<typename E>
Array<E>::~Array() {
    if (this->arr) {
        XLOGE("~Array()");
        free(this->arr);
        this->arr = NULL;
    }
}

template<typename E>
void Array<E>::ensureCapacityInternal(int minCapacity) {
    if (minCapacity - this->size > 0)
        grow(minCapacity);
}

template <typename E>
void Array<E>::grow(int minCapacity) {
    int oldCapacity = this->size;
    int newCapacity = oldCapacity + (oldCapacity >> 1);
    if (newCapacity - minCapacity < 0) {
        newCapacity = minCapacity;
    }
    E* new_array = (E *) malloc(sizeof(E) * newCapacity);
    if (new_array) {
        memcpy(new_array, this->arr, sizeof(E)*len); //字节
        free(this->arr);
    }

    this->arr = new_array;
    this->size = newCapacity;
}

