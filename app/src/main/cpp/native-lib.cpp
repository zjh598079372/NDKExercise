#include <jni.h>
#include <string>
#include "Array.hpp"
#include "include/Student.h"
#include "LinkedList.hpp"
#include <vector>
#include <set>
#include <link.h>
#include <__undef_macros>
#include <algorithm>
#include <list>
#include <stdlib.h>
#include <stdio.h>
#include <iostream>
#include <locale.h>
#include <stack>
#include <math.h>
#include "priorityQueue.hpp"
#include "BST.hpp"
#include "Params.h"


using namespace std;

struct FindStudentByName : public binary_function<Student, Student, bool> {

    bool operator()(const Student &std1, const Student &std2) const {
        int value = 0;
        if (std1.getName().compare(std2.getName()) == 0) {
            value = 1;
        }
        return value;
    }
};

struct compareStudentByName : public binary_function<Student,Student,bool>{
    bool operator()(const Student &stu1,const Student &stu2) const {
        int value = 0;
        if(stu1.getName().compare(stu2.getName())==0){
            value = 1;
        }
        return value;
    }
};

void updateParams(Params &params);

void monifyValue(int *pInt);

//Params getParams();
Params getParams() {
    Params params;
    params.num = 10;
    params.name = "小张";
    int a = 123456;
    params.passWord = &a;
    XLOGE("Params-->params内-->的地址-->%p", &params);
    return params;
}

struct node {
    bool operator()(const Student &i, const Student &j) {
        return i.getAge() > j.getAge();
    }
};

template<typename E>
struct TreeNode {
    TreeNode *left;
    TreeNode *right;
    E valeu;

    TreeNode(E valeu, TreeNode *left, TreeNode *right) {
        this->valeu = valeu;
        this->left = left;
        this->right = right;
    }

};

template<typename E>
void printNode(const TreeNode<E> *treeNode) {
    XLOGE("preOrderTree-->value-->%c", treeNode->valeu);
    int a = 10;
    int b = 20;

}

template<typename E>
struct NodeLink {
    E value;
    NodeLink *next;

    NodeLink(E v, NodeLink *nodeLink) {
        this->value = v;
        this->next = nodeLink;
    }
};

template<typename E>
void reverseLink(NodeLink<E>* header) {
    NodeLink<E> *temp = header->next;
    NodeLink<E> *pre = NULL;
    NodeLink<E>* cur = NULL;
    while (temp) {
        cur = temp;
        temp = temp->next;
        cur->next = pre;
        pre = cur;
    }

    header->next = pre;
}

template<typename E>
void preOrderTree(const TreeNode<E> *node, void (*printNode1)(const TreeNode<E> *treeNode)) {
    if (node == NULL) return;
    printNode1(node);
    preOrderTree(node->left, printNode);
    preOrderTree(node->right, printNode);
}

template<typename E>
void midOrderTree(const TreeNode<E> *node, void (*print2)(const TreeNode<E> *treeNode)) {
    if (node == NULL) return;
    midOrderTree(node->left, printNode);
    print2(node);
    midOrderTree(node->right, printNode);
}

template<typename E>
void backOrderTree(const TreeNode<E> *node, void(*print3)(const TreeNode<E> *treeNode)) {
    if (node == NULL) return;
    backOrderTree(node->left, printNode);
    backOrderTree(node->right, printNode);
    print3(node);
}

template<typename E>
void leverOrderTree(TreeNode<E> *node, void(*print4)(const TreeNode<E> *treeNode)) {
    list<TreeNode<E> *> treeList;
    if (node) {
        treeList.push_back(node);
    } else {
        return;
    }
    while (treeList.size() > 0) {
        TreeNode<E> *node1 = treeList.front();
        treeList.pop_front();
        print4(node1);
        if (node1->left) {
            treeList.push_back(node1->left);
        }
        if (node1->right) {
            treeList.push_back(node1->right);
        }
    }
}

/**
 * 序列化
 * @tparam E
 * @param node
 * @param str
 */
template <typename E>
void serializaTree(TreeNode<E>* node,std::string* str){
    if(node == NULL){
        (*str).append("#");
        return;
    } else{
        (*str).append(string(1,node->valeu));
    }

    serializaTree(node->left,str);
    serializaTree(node->right,str);
}

/**
 * 反序列化
 * @tparam E
 * @param node
 * @param str
 */
template <typename E>
TreeNode<char>* deserializaTree(const char** str){

    if(**str == '#'){
        *str += 1;
        return NULL;
    } else{
        TreeNode<char>* treeNode = new TreeNode<char>(**str,NULL,NULL);
        *str += 1;
        treeNode->left = deserializaTree<char>(str);
        treeNode->right = deserializaTree<char>(str);
        return treeNode;
    }
}

template <typename E>
int getTreeDepth(TreeNode<E> *node){
    if(node == NULL){
        return 0;
    }
    int leftDepth = getTreeDepth(node->left);
    int rightDepth = getTreeDepth(node->right);
//    XLOGE("getTreeDepth-->%d",depth);
    return max(leftDepth,rightDepth)+1;
}

void bubbleSort(int arr[], int length) {
    for (int i = 0; i < length - 1; i++) {
        for (int j = 0; j < length - 1 - i; j++) {
            if (arr[j] > arr[j + 1]) {
                int temp = arr[j];
                arr[j] = arr[j + 1];
                arr[j + 1] = temp;
            }
        }
    }
}

void selectSort(int arr[], int length) {
    int minIndex;
    for (int i = 0; i < length; i++) {
        minIndex = i;
        for (int j = i + 1; j < length; j++) {
            if (arr[j] < arr[minIndex]) {
                minIndex = j;
            }
        }
        if (arr[i] != arr[minIndex]) {
            //调换
            int temp = arr[i];
            arr[i] = arr[minIndex];
            arr[minIndex] = temp;

        }

    }
}

void insertSort(int *array, int length) {
    for (size_t i = 1; i < length; ++i)   // 注意：i是从1开始
    {
        int _nCurrent = array[i];       // 当前待排序的数字

        // 此时，下标为 0 ~ i-1的数字是有序的. 向后移动比当前序数字大的所有数，为该数腾出一>  个位置来。
        int _nLessEqualIndex = i - 1;
        while (_nLessEqualIndex >= 0 && array[_nLessEqualIndex] > _nCurrent) {
            array[_nLessEqualIndex + 1] = array[_nLessEqualIndex];
            --_nLessEqualIndex;
        }
        // 把新数插入到合适的位置
        array[_nLessEqualIndex + 1] = _nCurrent;
    }
}

void insertSort_1(int *arr, int length) {
    for (int i = 1; i < length; i++) {
        int temp = arr[i];
        int index = i;
        while (index > 0 && temp < arr[index - 1]) {
            int temp_index = arr[index - 1];
            arr[index - 1] = arr[index];
            arr[index] = temp_index;
            --index;
        }
        arr[index] = temp;
    }
}

void sellSort(int *arr, int length) {
    int step = length >> 1;
    while (step > 0) {
        for (int i = 0; i < step; i++) {
            int k = 0;
            for (int j = i; j < length; j += step) {
                k = j;
                int temp = arr[k];
                while (k > i) {
                    if (temp < arr[k - step]) {
                        arr[k] = arr[k - step];
                    } else {
                        break;
                    }
                    k -= step;
                }
                arr[k] = temp;
            }
        }

        step = step >> 1;
    }

}

void mergerSortData(int* arr,int left,int mid,int right){
    int* temp = (int*)malloc(sizeof(int)*(right-left+1));
    for (int n = left; n <=right; n++) {
        temp[n-left] = arr[n];
    }

    int i = left;
    int j = mid+1;
    int k = i;
    for (;k<=right;k++){
       if(i > mid){
           arr[k] = temp[j -left];
           j++;
       } else if(j > right){
           arr[k] = temp[i-left];
           i++;
       } else if(temp[i-left] < temp[j-left]){
           arr[k] = temp[i-left];
           i++;
       } else {
           arr[k] = temp[j-left];
           j++;
       }
    }

    if(temp){
        free(temp);
        temp = NULL;
    }


}

//归并排序
void mergerSort(int* arr,int left,int right){
    int mid = (left+right)>>1;
    if(left >= right){
        return;
    }
    mergerSort(arr,left,mid);
    mergerSort(arr,mid+1,right);
    mergerSortData(arr,left,mid,right);

}

int partition(int* arr,int left,int right){
    int val = arr[left];
    int p = left;
    for(int i = left+1; i<=right;i++){
        if(val > arr[i]){
            swap(arr[p+1],arr[i]);
            p++;
        }
    }

    swap(arr[left],arr[p]);

    return p;
}

//快排
void quickSort(int* arr,int left,int right){
    if(left>=right) return;
    int porsition = partition(arr,left,right);
    quickSort(arr,left,porsition-1);
    quickSort(arr,porsition+1,right);
}

wstring s2ws(const string &s) {
    size_t convertedChars = 0;
    string curLocale = setlocale(LC_ALL, NULL);   //curLocale="C"
    setlocale(LC_ALL, "chs");
    const char *source = s.c_str();
    size_t charNum = sizeof(char) * s.size() + 1;
    wchar_t *dest = new wchar_t[charNum];
    mbstowcs(dest, source, charNum);
    wstring result = dest;
    delete[] dest;
    setlocale(LC_ALL, curLocale.c_str());
    return result;
}

//void bubbleSort1(int* arr,int len){
//    for (int i = 0; i<len;i++){
//        for(int j = 1; j<len-i;j++){
//            if(arr[j]<arr[j-1]){
//                int temp = arr[j];
//                arr[j] = arr[j-1];
//                arr[j-1] = temp;
//            }
//        }
//    }
//}

void selectSort1(int *arr, int len) {
    for (int i = 0; i < len; i++) {
        int minIndex = i;
        for (int j = i; j < len; j++) {
            if (arr[j] < arr[minIndex]) {
                minIndex = j;
            }
        }
        if (minIndex != i) {
            int temp = arr[i];
            arr[i] = arr[minIndex];
            arr[minIndex] = temp;
        }
    }
}

void insertSort_2(int *arr, int len) {
    for (int i = 1; i < len; i++) {
        int temp = arr[i];
        int j = i;
        while (j >= 1) {
            if (temp < arr[j - 1]) {
                arr[j] = arr[j - 1];
            } else {
                break;
            }
            j--;
        }
        arr[j] = temp;

    }
}



extern "C" JNIEXPORT jstring JNICALL
Java_com_example_exercisendk_MainActivity_stringFromJNI(
        JNIEnv *env,
        jobject /* this */) {

    string hello = "Hello from C++";

    /**
     * template模板类的使用，以及ArrayList的增删查的C++层的实现方法
     */
//    Array<char* >* array = new Array<char* >(3);
//    array->add("10");
//    array->add("20");
//    array->add("30");
//    array->add("40");
//    array->add("50");
//    array->add("60");
//    char* item = array->remvoe(0);
//    array->add("70");
//    char* getItem = array->get(0);
//    XLOGE("getItem(0)==%s",getItem);
//
//    if(array){
//        delete(array);
//        array = NULL;
//    }

/**
 * 1、static_cast< >()用于基本类型的转换以及有继承关系类中的转换，在编译时进行检测
 * 2、dynamic_cast<>()只能用于引用或指针类型的转换，用于具有继承关系类中并且基类中存有virtual函数的转换，由于它转换失败时会返回NULL，所以比较安全，使用中应该首选
 * 3、reinterpret_cast<>() 重新解释转换，不能用于基本类型之间的转换，多用于long handle转换成某类的指针，尽量少用
 * 4、const_cast<>() 用于const int a = 10 与int a = 10之前的转换，转换之后可以重新赋值
 */

/*
    //int转float
    float fl_num = static_cast<float >(12); //正确
//    float fl_num = dynamic_cast<float >(12);  //错误，编译时通不过，报error: 'float' is not a reference or pointer
 //   float fl_num = reinterpret_cast<float >(12); //错误，编译时通不过，报error: reinterpret_cast from 'int' to 'float' is not allowed
    XLOGE("fl_num==%lf",fl_num);

    //float转int
    int int_num = static_cast<int>(12.45f); //正确
 //   int int_num = dynamic_cast<int>(12.45f);  //错误，编译时通不过，报error: 'int' is not a reference or pointer
//    int int_num = reinterpret_cast<int>(12.45f);  //错误，编译时通不过，报error: reinterpret_cast from 'float' to 'int' is not allowed
    XLOGE("int_num==%d",int_num);

    Person person("张三",25);
    Student stu("李四",26);
    Person* p_point = new Person("绰立",5);
    Student* p_stu = new Student("张工",2);
//    person = static_cast<Person>(stu);  //正确，由下转上
//    stu = static_cast<Student>(person); //错误，由上转下

//    person = dynamic_cast<Person>(stu);  //错误error: 'Person' is not a reference or pointer
//    Student* c_point = dynamic_cast<Student*>(person);  //正确，由下转上
    long handle = reinterpret_cast<long>(p_stu);
    Student* stu_point = reinterpret_cast<Student*>(handle);
    XLOGE("stu_point.name()==%s,stu_point.age()==%d",stu_point->getName().c_str(),stu_point->getAge());
    XLOGE("Student的地址-->%ld",handle);
*/
    string aa;
//    char* chaStr = "123";
//    aa = chaStr;
    string a = "123789123AAaabbcc";
    a.append("12");
    a.erase(2);
    a.replace(0,2,"12");
    a.size();
    int aaa = 10;
    int* p = &aaa;
    monifyValue(p);
    XLOGE("aaa-->%d",aaa);
//    string b = "456"+a;
//    string* str = new string("1233");
//
////    a.append(b);  //拼接
//    a.erase(0,2);   //删除
//    XLOGE("str-->%s",a.c_str());
//    b.replace(1,3,"6666");
//    int pos = a.find("23",0);
//    int fpos = a.find_first_of("23",0);
//    transform(a.begin(),a.end(),a.begin(),std::tolower);
//    XLOGE("a.find(234,0)-->%d",pos);
//    XLOGE("a.find_first_of(234,0)-->%d",fpos);
//    int size = b.size();       //获取长度，和a.length()一样都是获取长度；a.length()最终调用了a .size()
////    a.length();     //获取长度
//    XLOGE("b.size()-->%d",size);
//    string asub = a.substr(2,5);
//    XLOGE("a-->%s",asub.c_str());

    vector<Student> obj;
    //增加
    obj.push_back(Student("张三",1));
    obj.push_back(Student("李四",2));
    obj.push_back(Student("王五",3));
    obj.push_back(Student("赵六",4));
    obj.push_back(Student("胡七",5));
    obj.insert(obj.begin()+3,Student("王八",6));
//
//    obj[2] = Student("12",6);



    //删除
//    obj.erase(obj.begin()+1);
//    obj.pop_back();
//    obj.clear();

    //修改
//    obj.at(0) = Student("老师",10);

    //查找
//    if(find(obj.begin(),obj.end(),Student("张三",1)) != obj.end()){
//        XLOGE("找到了");
//    } else{
//        XLOGE("没有找到");
//    };
    //当vector存放的是自定义类时，要重载()运算符进行比较
    vector<Student> ::iterator it = find_if(obj.begin(),obj.end(),bind2nd(FindStudentByName(),Student("老师",6)));
    if( it != obj.end()){
        XLOGE("找到了,name-->%s,age-->%d",it->getName().c_str(),it->getAge());
    } else{
        XLOGE("没有找到");
    }


//
//
//    for(vector<Student> ::iterator it = obj.begin(); it != obj.end();++it){
//        XLOGE("obj的值-->%s",it->getName().c_str());
//    }
//    XLOGE("obj的值的长度-->%d",obj.size());
//
//    int size = obj.size();
//    for(int i=0; i<size;i++){
//        XLOGE("obj的值-->%s",obj[i].getName().c_str());
//    }

//    list<Student> objList;
//    objList.push_back(Student("张一",2));
//    objList.push_back(Student("张二",1));
//    objList.push_back(Student("张三",5));
//    objList.push_back(Student("张四",3));
//    objList.push_back(Student("张五",4));
//    objList.push_back(Student("张六",0));
//
//    objList.size();

//    list<Student>::iterator it = objList.begin();
//    advance(it,3);
//    objList.insert(it,Student("赵七",70));
//    objList.insert(it,Student("王八",80));
//    objList.clear();
//    objList.erase(it);
//    objList.pop_front();
//    objList.push_back(1);
//    objList.push_back(5);
//    objList.push_back(3);
//    objList.push_back(4);
//    objList.push_back(0);
//    objList.sort(); // 从小到大;
//    objList.sort(greater<int>());   //从大到小;
//    objList.sort(node());


//    int i = 0;
//    for (list<Student>::iterator it = objList.begin(); it != objList.end();it++){
//        i++;
//        XLOGE("objList--%d-->%s",i,(*it).getName().c_str());
//    }

//    Student stu1 = objList.front();
//    XLOGE("Student-->%s",stu1.getName().c_str());

//    set<string> objSet;
//    objSet.insert("12");
//    objSet.insert("34");
//    objSet.insert("56");
//    objSet.insert("78");
//    objSet.erase("12");
//    objSet.erase(objSet.begin());
//    set<string> ::iterator obj = objSet.find("34");
//    XLOGE("set的值==%s",(*obj).c_str());
//    XLOGE("set的值==%d",objSet.count("34"));
//
//    for(set<string> ::iterator it = objSet.begin(); it != objSet.end(); it++){
//        XLOGE("set的值==%s",(*it).c_str());
//    }
//
//    pair <set<string>::iterator,set<string>::iterator> pair1;
//    pair1 = objSet.equal_range("111");
//
//    XLOGE("pair1的first值==%s",(*pair1.first).c_str());
//    XLOGE("pair1的second值==%s",(*pair1.second).c_str());

//    LinkedList<int> linkedList;
//    linkedList.Push(10);
//    linkedList.Push(20);
//    linkedList.Push(30);
//    linkedList.Push(40);
//    linkedList.remove(-1);
//    linkedList.remove(2);
//    linkedList.insert(0,0);
//    linkedList.Push(10);
//    linkedList.Push(20);
//    linkedList.insert(0,30);
//    linkedList.Push(40);
//    linkedList.remove(3);
//    linkedList.Push(50);
//    for (int i = 0; i<4;i++){
//        linkedList.remove(0);
//    }
//    linkedList.Push(60);
//    linkedList.remove(5);
//    linkedList.Push(60);
//    linkedList.insert(1,70);
//    linkedList.remove(6);
//    linkedList.remove(0);

//    for (int i = 0; i<linkedList.size;i++){
//        Node<int>* node = linkedList.get(i);
//        XLOGE("linkedList%d==%d",i,node->value);
//    }
//    XLOGE("=============================");
//    XLOGE("linkedList-->last-->%d",linkedList.last->value);

//    Node<int>* node = linkedList.get(3);
//    XLOGE("linkedList.get(3)==%d",node->value);
//    Node<int>* node4 = linkedList.get(4);
//    XLOGE("linkedList.get(4)==%d",node4);
//    const Params params = getParams();
//    int c = 22;
//     int& b = c;
//    const int& aa = b;
//    params.changeNum();
////    updateParams(params);
//    XLOGE("Params-->params.name-->%s",params.name.c_str());
//    XLOGE("Params-->params的地址-->%p",&params);
//    int arr[] ={2,1,5,0,4,3,-15,2,5,50,23,-30};
    int arr[] = {10, -3, -8, 0, 1, 3, 2, 5};

    //1、冒泡排序
//    bubbleSort(arr, sizeof(arr)/sizeof(arr[0]));
//    bubbleSort1(arr, sizeof(arr)/sizeof(arr[0]));

    //2、选择排序
//    selectSort(arr,sizeof(arr)/sizeof(arr[0]));
//    selectSort1(arr,sizeof(arr)/sizeof(arr[0]));

    //3、插入排序
//    insertSort_2(arr, sizeof(arr)/sizeof(arr[0]));

    //4、希尔排序
//    sellSort(arr, sizeof(arr) / sizeof(arr[0]));

    //5、归并排序
//    mergerSort(arr,0,(sizeof(arr) / sizeof(arr[0]))-1);
//    mergerSort(arr,0,(sizeof(arr) / sizeof(arr[0]))-1);

    //6、快排

//    quickSort(arr,0,(sizeof(arr)/ sizeof(arr[0]))-1);
//
//    for (int i = 0; i<sizeof(arr)/sizeof(arr[0]);i++){
//        XLOGE("arr[%d]==%d",i,arr[i]);
//    }

//    TreeNode<char>* A = new TreeNode<char>('A',NULL,NULL);
//    TreeNode<char>* B = new TreeNode<char>('B',NULL,NULL);
//    TreeNode<char>* C = new TreeNode<char>('C',NULL,NULL);
//    TreeNode<char>* D = new TreeNode<char>('D',NULL,NULL);
//    TreeNode<char>* E = new TreeNode<char>('E',NULL,NULL);
//    TreeNode<char>* F = new TreeNode<char>('F',NULL,NULL);
//    TreeNode<char>* G = new TreeNode<char>('G',NULL,NULL);
//    TreeNode<char>* H = new TreeNode<char>('H',NULL,NULL);
//
//    A->left = B;
//    A->right = C;
//    B->left = D;
//    B->right = E;
//    C->left = F;
//    D->left = G;
//    G->left = H;


    //前序遍历
//    preOrderTree(A,printNode);

    //中序遍历
//    midOrderTree(A,printNode);

    //后序遍历
//    backOrderTree(A,printNode);

    //层序遍历
//    leverOrderTree(A,printNode);

 //序列化
// string str;
// serializaTree(A,&str);
//// const char* aa = str.c_str();
// const char* aa =str.c_str();
//// strcpy(aa,str.c_str());
//char* bb ="ABD##E##CF###";
//    TreeNode<char>* node1 = deserializaTree<char>(&aa);
//    string string1;
//    serializaTree(node1,&string1);
//
//
//
//    XLOGE("serializaTree-->%s",string1.c_str());

//    int depth = getTreeDepth(A);
//    XLOGE("getTreeDepth-->%d",depth);

//    NodeLink<char> *Header = new NodeLink<char>('Header', NULL);
//    NodeLink<char> *A = new NodeLink<char>('A', NULL);
//    NodeLink<char> *B = new NodeLink<char>('B', NULL);
//    NodeLink<char> *C = new NodeLink<char>('C', NULL);
//    NodeLink<char> *D = new NodeLink<char>('D', NULL);
//    A->next = B;
//    B->next = C;
//    C->next = D;
//    Header->next = A;
//    //反转单链表
//    reverseLink(Header);
//    NodeLink<char>* nodeLink = Header->next;
//    while (nodeLink){
//        XLOGE("nodeLink-->value-->%c",nodeLink->value);
//        nodeLink = nodeLink->next;
//    }


     //优先级队列，堆排序

     priorityQueue<int>* queue = new priorityQueue<int>(10);

     queue->push(10);
     queue->push(8);
     queue->push(20);
     queue->push(3);
     queue->push(5);
     queue->push(-7);
     queue->push(11);
     queue->push(0);
     queue->push(60);

     while (!queue->isEmpty()){
         int value = queue->pop();
         XLOGE("priorityQueue-->value-->%d",value);
     }


     BST<int,int>* bst = new BST<int,int>(20);
     bst->put(5,5);
     bst->put(6,6);
     bst->put(7,7);
     bst->put(3,3);
     bst->put(2,2);
     bst->put(4,4);
     bst->put(9,9);

     bst ->remove(5);
//     bst ->remove(7);
//     bst ->remove(2);
//     bst ->remove(4);
     bst->preOrderTree();


     if(bst ->findNode(9)){
         XLOGE("findNode-->%d",bst ->findNode(9)->value);
     } else{
         XLOGE("findNode-->%s","返回为null");
     }


    return env->NewStringUTF(hello.c_str());
}
//
void monifyValue(int *pInt) {
    *pInt = 20;
  //  age = 30;

    XLOGE("monifyValue-->age-->%d", age);

}


void updateParams(Params &params) {
    params.name = "王五";
    XLOGE("Params-->updateParams内-->的地址-->%p", &params);
    XLOGE("Params-->updateParams内.name-->%s", params.name.c_str());

}

//Params getParams() {
//    Params params;
//    params.num = 10;
//    params.name = "小张";
//    int a = 123456;
//    params.passWord = &a;
//    XLOGE("Params-->params内-->的地址-->%p",&params);
//    return params;
//}

extern "C"
JNIEXPORT void JNICALL
Java_com_example_exercisendk_MainActivity_arraycopy(JNIEnv *env, jobject thiz, jobject src,
                                                    jint src_pos, jobject dest, jint dest_pos,
                                                    jint length) {
    // TODO: implement arraycopy()
    jobjectArray src_arr = static_cast<jobjectArray>(src);
    jobjectArray des_arr = static_cast<jobjectArray>(dest);
    if (src_arr == NULL || des_arr == NULL) {
        XLOGE("转换失败");
        return;
    }
    for (int i = 0; i < length; i++) {
        jobject src_jobject = env->GetObjectArrayElement(src_arr, i);
        env->SetObjectArrayElement(des_arr, i, src_jobject);
    }
}



