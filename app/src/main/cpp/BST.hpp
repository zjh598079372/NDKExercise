//
// Created by CL002 on 2021-11-29.
//

#ifndef EXERCISENDK_BST_HPP
#define EXERCISENDK_BST_HPP

#endif //EXERCISENDK_BST_HPP

#include "XLog.h"


void visit(int value) {
    XLOGE("BST-->value-->%d",value);
}

template <typename K,typename V>
struct Tree_Node{
    Tree_Node* left = NULL;
    Tree_Node* right = NULL;
    K key;
    V value;
    Tree_Node(K key,V value){
        this ->key = key;
        this ->value = value;
        this ->left = NULL;
        this ->right = NULL;
    }

    Tree_Node(Tree_Node* node){
        this ->key = node ->key;
        this ->value = node ->value;
        this ->left = node ->left;
        this ->right = node ->right;
    }
};

template <typename K,typename V>
class BST{
private:
    int count;
    int index;
    Tree_Node<K,V>* root = NULL;
public:
    BST(int count);
    Tree_Node<K,V>* insert(Tree_Node<K,V>* pNode,K key,V value);
    void put(K key,V value);
    void preOrderTree();
    Tree_Node<K,V>* remove(Tree_Node<K, V> *pNode,K key);

    void remove(K key);
    void preOrderTree(Tree_Node<K,V>*pNode, void (*fun)(int));

    Tree_Node<K,V>* findLeftMax(Tree_Node<K, V> *pNode);

    Tree_Node<K,V>* deleteLeftMax(Tree_Node<K, V> *pNode);

    Tree_Node<K,V>* findNodeByKey(Tree_Node<K, V> *pNode, K key);

    Tree_Node<K,V>* findNode( K key);
};


template <typename K,typename V>
BST<K,V>::BST(int count):count(count) {

}



template<typename K, typename V>
void BST<K, V>::preOrderTree() {
    preOrderTree(root,visit);

}

template <typename K,typename V>
Tree_Node<K,V>* BST<K, V>::insert(Tree_Node<K,V>* pNode,K key,V value) {
    if (!pNode){
        count++;
        pNode = new Tree_Node<K, V>(key,value);
        return pNode;
    }

   if(key < pNode->key){
       pNode ->left = insert(pNode ->left,key,value);
   } else if(key > pNode ->key){
       pNode ->right = insert(pNode ->right,key,value);
   } else{
       pNode ->value = value;
   }

    return pNode;
}


template <typename K,typename V>
Tree_Node<K,V>* BST<K, V>::remove(Tree_Node<K, V> *pNode,K key) {
    if(!pNode){
        return NULL;
    }
    if (key < pNode ->key){
        pNode->left = remove(pNode->left,key);
    } else if(key > pNode ->key){
        pNode ->right = remove(pNode ->right,key);
    } else{
        if (!pNode ->left && !pNode ->right){  //左右都为空
            delete pNode;
            count --;
            return NULL;
        } else if (pNode ->left && !pNode ->right){  //右为空，左不为空时，用左节点生成一个Node返回去
            Tree_Node<K,V>* node = new Tree_Node<K,V>(pNode ->left);
            delete pNode;
            count --;
            return node;
        } else if(pNode ->right && !pNode ->left){  //左为空，右不为空时，用右节点生成一个Node返回去
            Tree_Node<K,V>* node = new Tree_Node<K,V>(pNode ->right);
            delete pNode;
            count --;
            return node;
        } else{  //左右都不为空时
            /**
             * 1、先找到左子树中最大的节点，生成一个新的节点，并把当前节点的左子树赋值给新节点的左节点，
             * 把当前节点的右子树赋值给新节点的右节点
             */
            Tree_Node<K,V>* max = new Tree_Node<K,V>(findLeftMax(pNode ->left));
            max ->right = pNode ->right;
            max ->left = deleteLeftMax(pNode ->left);
            //2、删除当前节点
            delete pNode;
            return max;
        }
    }

    return pNode;
}

template<typename K, typename V>
void BST<K, V>::preOrderTree(Tree_Node<K, V> *pNode, void (*fun)(int)) {
    if(!pNode){
        return;
    }
    fun(pNode->value);
    preOrderTree(pNode->left,fun);
    preOrderTree(pNode ->right,fun);

}

template <typename K, typename V>
void BST<K,V>::put(K key, V value) {
    root = insert(root,key,value);
}

template <typename K, typename V>
void BST<K,V>::remove(K key) {
    root = remove(root,key);
}


template<typename K, typename V>
Tree_Node<K,V>* BST<K,V>::deleteLeftMax(Tree_Node<K,V> *pNode) {

    if (!pNode ->right){
        Tree_Node<K,V>* node = pNode ->left;
        delete pNode;
        count --;
        return node;
    }
    pNode ->right = deleteLeftMax(pNode ->right);
    return pNode;
}

template<typename k, typename V>
Tree_Node<k,V>* BST<k,V>::findLeftMax(Tree_Node<k,V> *pNode) {
    if(!pNode ->right){
        return pNode;
    }
    return findLeftMax(pNode ->right);
}

template <typename K, typename V>
Tree_Node<K, V> * BST<K,V>::findNodeByKey(Tree_Node<K,V> *pNode, K key) {
    if(!pNode){
        return NULL;
    }
    if(key <pNode->key){
        return findNodeByKey(pNode ->left,key);
    } else if(key > pNode ->key){
        return findNodeByKey(pNode ->right, key);
    } else{
        return pNode;
    }
}

template <typename K, typename V>
Tree_Node<K, V> * BST<K,V>::findNode(K key) {
    Tree_Node<K,V>* node = findNodeByKey(root,key);
    return node;
}
