//
// Created by CL002 on 2021-5-31.
//

#ifndef EXERCISENDK_LINKEDLIST_HPP
#define EXERCISENDK_LINKEDLIST_HPP

template<typename E>
struct Node {
    Node<E> *next;
    Node<E> *pre;
    E value;

    Node(Node<E> *next, Node<E> *pre, E value) {
        this->next = next;
        this->pre = pre;
        this->value = value;
    }
};

template<typename E>
class LinkedList {
public:
    int size = 0;
    Node<E> *header = NULL;
    Node<E> *last = NULL;
public:
    LinkedList<E>();

    ~LinkedList<E>();

    void Push(E e);

    void remove(int index);

    void insert(int index, E e);

    Node<E> *get(int index);

    Node<E> *findLastNode(Node<E> *pNode);

    Node<E> *getNodeByIndex(int index);
};


template<typename E>
void LinkedList<E>::Push(E e) {
    //1、先创建一个Node
    Node<E> *new_node = new Node<E>(NULL, NULL, e);
    if (!header) {
        //2、1头指针为NULL时
        header = new_node;
    } else {
        //2、2头指针不为NULL时，遍历头指针拭到最后一个节点
        last->next = new_node;
        new_node->pre = last;
    }
    last = new_node;
    size++;
}

template<typename E>
Node<E> *LinkedList<E>::get(int index) {
    return getNodeByIndex(index);
}

template<typename E>
void LinkedList<E>::remove(int index) {
    Node<E> *curNode = getNodeByIndex(index);
    Node<E> *nextNode = getNodeByIndex(index + 1);
    Node<E> *preNode = getNodeByIndex(index - 1);
    if (curNode) {
        if (!preNode) { //前一个节点为NULL，则当前节点为头节点时
            header = nextNode;
        } else {
            if (nextNode) {
                preNode->next = nextNode;
                nextNode->pre = preNode;
            }
        }
        if (index + 1 == size) {
            last = preNode;
        }
        size--;
        delete (curNode);
    }
}

template<typename E>
void LinkedList<E>::insert(int index, E e) {
    if (index < 0 || index > size) return;
    Node<E> *insertNode = new Node<E>(NULL, NULL, e);
    Node<E> *preNode = getNodeByIndex(index - 1); //这个不会出现NULL
    Node<E> *curNode = getNodeByIndex(index);  //这个可能会出现NULL
    Node<E> *temp = header;
    if (index == 0) {
        header = insertNode;
        if (temp) {
            header->next = temp;
            temp->pre = header;
        }
    } else {
        preNode->next = insertNode;
        insertNode->next = curNode;
        insertNode->pre = preNode;
        if (curNode) {
            curNode->pre = insertNode;
        }
    }
    if (index == size) {
        last = insertNode;
    }
    size++;

}

template<typename E>
LinkedList<E>::LinkedList() {
    XLOGE("============LinkedList()=================");
};


template<typename E>
LinkedList<E>::~LinkedList() {
    Node<E> *tempNode = header;
    while (tempNode) {
        Node<E>* next = tempNode->next;
        delete (tempNode);
        tempNode = next;
    }


    XLOGE("============~LinkedList()=================");
}

template<typename E>
Node<E> *LinkedList<E>::findLastNode(Node<E> *header) {
    Node<E> *tempNode = header;
    while (tempNode->next) {
        tempNode = tempNode->next;
    }
    return tempNode;
}

template<typename E>
Node<E> *LinkedList<E>::getNodeByIndex(int index) {
    if (index < 0 || index > size - 1) return NULL;
    Node<E> *tempNode = header;
    if (index == 0)return tempNode;
    int tempIndex = 0;
    while (tempIndex != index) {
        tempNode = tempNode->next;
        tempIndex++;
    }
    return tempNode;
}


#endif //EXERCISENDK_LINKEDLIST_HPP
